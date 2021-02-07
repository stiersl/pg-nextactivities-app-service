USE [SOADB]
GO

/****** Object:  StoredProcedure [dbo].[splocal_SA_NextActivityEstimate]    Script Date: 2/7/2021 6:22:58 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


--================================================================================================
--Stored Procedure		:	splocal_SA_NextActivityEstimate
--Author				:	Steven Stier , Stier Automation LLC
--Date Created			:	12-14-2020
--Called By				:	Next Activities Estimate API
--Editor Tab Spacing	:	4
--================================================================================================
--Purpose:
--------------------------------------------------------------------------------------------------
-- This SP will get unique set of activites (timed and Some UDE) assigned to those Machines(PUIDs) 
-- and estimate times the next activity will be generated based upon Runtime (uptime) if required.
-- It will return the activites and calculations.
--================================================================================================
--Input Parameters:
--------------------------------------------------------------------------------------------------
--Input#	Input Name				Input Type			Description
---------	-------------------		----------			------------------------------------------
--	1		@PUIds					nVARCHAR(max)		Comma seperated List of PuIDs											
--================================================================================================
-- Edit History:
-------------------------------------------------------------------------------------------------
--Revision	Date			Who						What
--========	===========		=====================	==================================
-- 0.1		2020-12-14		Steven Stier			Creation of the Stored Procedure 
-- 0.2		2020-12-15		Steven Stier			removed option to pull Timed based activites.
-- 0.3		2021-01-08		Steven Stier			Added Color Field for Cell Coding
-- 0.4		2021-02-07		Steven Stier			Changed a bunch of field names
--
--================================================================================================
--TESTING
--------------------------------------------------------------------------------------------------
/*
Exec splocal_SA_NextActivityEstimate  "1" --SA Dev System
Exec splocal_SA_NextActivityEstimate  "4" --SA Dev System
Exec splocal_SA_NextActivityEstimate  "1,4" --SA Dev System
Exec splocal_SA_NextActivityEstimate  "100" --Funct STL box
Exec splocal_SA_NextActivityEstimate   "100,113" --Funct STL box - Lines 14 Converter, Line 25 Bagger
Exec splocal_SA_NextActivityEstimate   "189,233,224" --Funct STL box - Lines 10 Converter, Line 26 ATL
*/
--------------------------------------------------------------------------------------------------
--================================================================================================
CREATE OR ALTER       PROCEDURE [dbo].[splocal_SA_NextActivityEstimate]
                     @PUIds     nVARCHAR(max) = NULL
--WITH ENCRYPTION  
AS
SET NOCOUNT ON  
BEGIN
DECLARE @CurrentTimeStamp			datetime,
		@@Sheet_Id					int,
		@@LastActivityStartTime		datetime,
		@@LastActivityEndTime		datetime,
		@@Unit_ID					int,
		@@DownTime					int,
		@@DownTimeComplete			int,
		@ED_FIELD_ID				int,
		@DTHistoryLookback dateTime
	--------------------------------------------------------------------------------------------------
	--get the comma seperate PUIDs and populate @Units
	--------------------------------------------------------------------------------------------------
    DECLARE @Units TABLE(RowID  INT IDENTITY,
                         UnitId INT NULL)
    DECLARE @Xml XML
    IF @PUIds IS NOT NULL
        BEGIN
            SET @PUIds = REPLACE(@PUIds, ' ', '')
        END
    IF @PUIds IS NOT NULL
       AND LEN(@PUIds) = 0
        BEGIN
            SET @PUIds = NULL
        END
    IF @PUIds IS NOT NULL
        BEGIN
            SET @Xml = CAST('<X>'+replace(@PUIds, ',', '</X><X>')+'</X>' AS XML)
            INSERT INTO @Units( UnitId )
            SELECT N.value('.', 'int') FROM @Xml.nodes('X') AS T(N)
        END;
  	--------------------------------------------------------------------------------------------------
	-- Initialize the temp tables
	--------------------------------------------------------------------------------------------------
	-- #RecentActivities = holds all the activities that will be monitored
	--------------------------------------------------------------------------------------------------
    CREATE TABLE #RecentActivities(Sheet_Id   INT,
							Sheet_Type INT,
                            Sheet_Desc nVarchar(50),
							Event_Subtype_Id INT,
                            Unit_id    INT,
                            Equipment  nVARCHAR(100),
							Ec_id INT,
							Duration INT,
							LastActivityName nVarchar(100),
							LastActivityID INT,
							LastActivityStartTime datetime,
							LastActivityEndTime datetime,
							LastActivityStatusID INT,
							LastActivityStatus  nVarchar(50),
							CurrentTimeStamp datetime,
							NextActivityEstimate datetime,
							NextActivityEstimateCD nVarchar(50),
							ActivityCDTracker nVarchar(50),
							DownTime INT,
							CalendarTime INT,
							UpTime INT,
							RemainingTime INT,
							DownTimeComplete INT,
							CalendarTimeComplete INT,
							UpTimeComplete INT,
							RemainingTimeComplete INT,
							Color INT
							)
	--------------------------------------------------------------------------------------------------
	-- #tmpDowntimes-- will hold downtime Events we may need to summarize
	--------------------------------------------------------------------------------------------------
	CREATE TABLE #tmpDowntimes(TEDET_ID   INT,
							PU_ID INT,
							Start_Time datetime,
							End_time datetime)

    IF @PUIds IS NOT NULL
	
        BEGIN
			-------------------------------------------------------------------------------------------
			-- get the sheets for the Time based displays (Sheet Type = 1)
			-------------------------------------------------------------------------------------------

            INSERT INTO #RecentActivities( Sheet_Id,
									Sheet_Type,
                                    Sheet_Desc,
									Duration,
                                    Unit_id
									)
            SELECT S.Sheet_Id,
					1,
                   S.Sheet_Desc,
				   S.Interval,
                   SDO.Value AS Unit_Idf
                   FROM Sheets AS S
					---------------------------------------------------------------------------------
					-- Create Activites= True ==> Display_option_id=444 and value = 1 
					--  UnitforActivties = Display_option_id = 446 
					----------------------------------------------------------------------------------
				   
                        JOIN Sheet_Display_Options AS SDO1 ON SDO1.Sheet_Id = S.Sheet_Id
                                                              AND SDO1.Display_Option_Id = 444
                                                              AND SDO1.Value = 1
                        LEFT JOIN Sheet_Display_Options AS  SDO with(nolock) ON SDO.Sheet_Id = S.Sheet_Id
                                                                  AND SDO.Display_Option_Id = 446
                        JOIN @Units AS U ON U.UnitId = SDO.Value
                                                       
                   WHERE S.Sheet_Type = 1  AND S.Is_Active = 1	   
			-------------------------------------------------------------------------------------------
			-- get the sheets for the Production event and UDEs - (Sheet Type = 25)
			-------------------------------------------------------------------------------------------
			
            INSERT INTO #RecentActivities( Sheet_Id,
									Sheet_Type,
                                    Sheet_Desc,
									Event_Subtype_Id,
                                    Unit_id )
            SELECT S.Sheet_Id,
				   25,
                   S.Sheet_Desc,
				   Event_Subtype_Id,
                   S.Master_Unit AS Unit_Id
                   FROM Sheets AS S
				   -------------------------------------------------------------------------------------
				   -- Create activites option is enabled. 
				   -- Create Activites= True ==> Display_option_id=444 and value = 1 
				   --- Sheet_type 25 = UDP displays
				   -- --- Also determine if the UDP "EstimateActity" is set on UDPs--> Event Subtypes
				   --------------------------------------------------------------------------------------
                        JOIN Sheet_Display_Options AS SDO  with(nolock) ON SDO.Sheet_Id = S.Sheet_Id
                                                              AND SDO.Display_Option_Id = 444
                                                              AND SDO.Value = 1
                        JOIN @Units AS U ON U.UnitId = S.Master_Unit
                   WHERE S.Sheet_type=25 AND S.Is_Active = 1
				   AND S.Event_Subtype_Id IN ( SELECT keyid FROM Table_fields_values tfv with(nolock)
									  JOIN Table_fields tf with(nolock) on tfv.Table_field_id = tf.Table_field_id
				  				  WHERE (Table_field_desc = 'EstimateActivity')  AND  tfv.Value in ('1','Yes'))
			-------------------------------------------------------------------------------------------
			-- Update a bunch of Fields in #Recent Actitivities from various Sources
			-------------------------------------------------------------------------------------------
			-- Get the Equipment Name - Production unit Name
			UPDATE #RecentActivities
				SET Equipment = (SELECT pu_desc FROM Prod_Units_Base  with(nolock) WHERE Pu_id = Unit_id)
			
			-- Get the ECID
			UPDATE #RecentActivities
				SET Ec_id = (SELECT Ec_id from Event_Configuration ec  with(nolock) 
							where ec.Event_Subtype_Id  = #RecentActivities.Event_Subtype_Id  
							AND ec.Pu_id = #RecentActivities.Unit_id)
			
			-- get the Uptime Duration Frequency of the UDE model
			Set @ED_FIELD_ID = (select  ED_field_id from ed_fields  with(nolock) 
								where Field_desc = 'TotalTime (In seconds)')

			UPDATE #RecentActivities
				SET Duration = (select convert(int, (convert( varchar(max),value)))   
											FROM event_configuration_values ecv with(nolock) 
											JOIN event_configuration_data ecd on ecd.ecv_id = ecv.ecv_id
											WHERE ecd.ec_id = #RecentActivities.Ec_id and ED_Field_Id = @ED_FIELD_ID) WHERE Sheet_Type = 25
			-------------------------------------------------------------------------------------------
			-- get the oldest Activity for that sheet - use the execution_start_time 
			-- this will be the last Activity Start Time
			-------------------------------------------------------------------------------------------
			UPDATE #RecentActivities
				SET  LastActivityStartTime = (SELECT max(execution_start_time) FROM Activities a with(nolock) 
												WHERE a.Sheet_ID = #RecentActivities.Sheet_Id)
			
			-- get the LastActivity ID using that startime
			UPDATE #RecentActivities
				SET  LastActivityID = (SELECT Activity_id  FROM Activities a with(nolock) 
										WHERE a.Sheet_ID = #RecentActivities.Sheet_Id 
										and a.execution_start_time =  #RecentActivities.LastActivityStartTime)
		    -- get some other fields from the activites
			UPDATE #RecentActivities
				SET   LastActivityName = (SELECT Activity_Desc  FROM Activities a with(nolock) 
											WHERE  a.Activity_Id =  #RecentActivities.LastActivityID)
			UPDATE #RecentActivities
				SET  LastActivityStatusID = (SELECT Activity_Status  FROM Activities a with(nolock) 
											WHERE  a.Activity_Id =  #RecentActivities.LastActivityID)
			-- Decode the Activity Status
			UPDATE #RecentActivities
				SET  LastActivityStatus = 
					CASE LastActivityStatusID
                 		WHEN 1		THEN 'Not Started' 
	         			WHEN 2		THEN 'In Progress'
	         			WHEN 3		THEN 'System Complete'
						WHEN 4		THEN 'Skipped'
					 ELSE '???????????'
					END
			--Get the Endtime of the Activity
			UPDATE #RecentActivities
				SET  LastActivityEndTime = (SELECT End_Time  FROM Activities a with(nolock) 
											WHERE  a.Activity_Id =  #RecentActivities.LastActivityID)
			
			-- Get right now 
			--SELECT @CurrentTimeStamp = getdate()
			SELECT @CurrentTimeStamp = CURRENT_TIMESTAMP;
			--SELECT '@CurrentTimeStamp=', @CurrentTimeStamp

			UPDATE #RecentActivities
				SET  CurrentTimeStamp = @CurrentTimeStamp
			
			-------------------------------------------------------------------------------------------
			-- determine next activity for Time Based Activites Sheet_type = 1;
			-------------------------------------------------------------------------------------------
			UPDATE #RecentActivities
				SET  NextActivityEstimate = DATEADD(minute,Duration, LastActivityStartTime) WHERE Sheet_Type = 1
			UPDATE #RecentActivities
				SET  RemainingTime = DATEDIFF(s, @CurrentTimeStamp, NextActivityEstimate) Where Sheet_Type = 1
			UPDATE #RecentActivities
				SET  RemainingTimeComplete = DATEDIFF(s, LastActivityEndTime, NextActivityEstimate) 
					Where Sheet_Type = 1 and  LastActivityEndTime IS NOT NULL

			-------------------------------------------------------------------------------------------
			--Start: Determine next activity Estimate  for UDE Type activities
			-------------------------------------------------------------------------------------------
			
			-------------------------------------------------------------------------------------------
			-- Get all the Individual Downtime events from Timed_event_details we might need for later calculations.
			-- This should be the least impact on the timed event details table
			-------------------------------------------------------------------------------------------
			-- First Determine how far back we might have to look back for downtimes
			-------------------------------------------------------------------------------------------
			Select @DTHistoryLookback = (Select Min(LastActivityStartTime) from #RecentActivities)
			--Select '@DTHistoryLookback=' ,@DTHistoryLookback

			INSERT INTO #tmpDowntimes(TEDET_ID,
							PU_ID,
							Start_Time,
							End_time)
            SELECT ted.TEDet_Id,
				   ted.PU_Id,
                   ted.Start_Time,
				   ted.End_Time
                   FROM Timed_Event_Details AS ted with(nolock)
                        JOIN @Units AS U ON U.UnitId = ted.PU_Id                        
                   WHERE (End_Time is NULL) or (End_time > @DTHistoryLookback)
			--Select '#tmpDowntimes='
			--Select * from #tmpDowntimes
		
			-------------------------------------------------------------------------------------------
			-- This big cursor will go through each row in #RecentActivities temp Table one by
			-- by one and calculate the downtime for each activity and also calcualte the
			-- Downtime when the activity was completed.
			-------------------------------------------------------------------------------------------

			DECLARE	Activity_Cursor CURSOR FOR

			(Select Sheet_id,
					LastActivityStartTime,
					LastActivityEndTime,
					Unit_ID from #RecentActivities WHERE Sheet_Type = 25)

				FOR READ ONLY
			OPEN	Activity_Cursor
			FETCH	NEXT FROM Activity_Cursor INTO @@Sheet_ID, @@LastActivityStartTime, @@LastActivityEndTime, @@Unit_ID

			WHILE	@@Fetch_Status = 0

			BEGIN
				-------------------------------------------------------------------------------------------
				-- get downtime for right now for the activity
				-------------------------------------------------------------------------------------------
				Select @@DownTime = (SELECT SUM(datediff(s,IIF(Start_time < @@LastActivityStartTime, @@LastActivityStartTime, Start_time) 
																 ,Coalesce(End_Time, @CurrentTimeStamp ))) As DownTime
							from #tmpDowntimes WHERE ((Pu_ID = @@Unit_ID) and 
														 ((End_Time is NULL) or (End_time > @@LastActivityStartTime))
														))

				UPDATE #RecentActivities
				SET  DownTime = Coalesce(@@DownTime,0) where Sheet_id = @@Sheet_Id
				-------------------------------------------------------------------------------------------
				-- get Downtime,Calander Time, Uptime that was active when the activity Completed
				-------------------------------------------------------------------------------------------
				if @@LastActivityEndTime IS NOT NULL
				BEGIN

					Select @@DownTimeComplete = (SELECT SUM(datediff(s,IIF(Start_time < @@LastActivityStartTime, @@LastActivityStartTime, Start_time) 
											, IIF(Coalesce(End_Time, @@LastActivityEndTime) >= @@LastActivityEndTime, @@LastActivityEndTime, End_time) )) As DownTime
							from #tmpDowntimes WHERE ((Pu_ID = @@Unit_ID) and 
														 ((Start_time < @@LastActivityEndTime) and ((End_time > @@LastActivityStartTime) or (End_Time is NULL)))
														))
					UPDATE #RecentActivities
					SET  DownTimeComplete = Coalesce(@@DownTimeComplete,0) where Sheet_id = @@Sheet_Id
					UPDATE #RecentActivities
					SET  CalendarTimeComplete = DATEDIFF(s, LastActivityStartTime, @@LastActivityEndTime) where Sheet_id = @@Sheet_Id
					UPDATE #RecentActivities
					SET  UpTimeComplete = CalendarTimeComplete - DownTimeComplete where Sheet_id = @@Sheet_Id
					UPDATE #RecentActivities
					SET  RemainingTimeComplete = Duration - UpTimeComplete where Sheet_id = @@Sheet_Id
				END


				FETCH	NEXT FROM Activity_Cursor INTO @@Sheet_ID, @@LastActivityStartTime, @@LastActivityEndTime, @@Unit_ID
			END
			CLOSE 	Activity_Cursor
			DEALLOCATE Activity_Cursor

			
			-------------------------------------------------------------------------------------------
			--- Calculate the Amount of Calander Time - Time from the start of the activity to right now
			-------------------------------------------------------------------------------------------
			UPDATE #RecentActivities
				SET  CalendarTime = DATEDIFF(s, LastActivityStartTime, @CurrentTimeStamp)
			-------------------------------------------------------------------------------------------
			-- Determine the Amount of Uptime (runtime) = Calander time - Downtime since Activity 
			-- Began 
			-------------------------------------------------------------------------------------------
			UPDATE #RecentActivities
				SET  UpTime = CalendarTime - DownTime  WHERE Sheet_Type = 25
			-------------------------------------------------------------------------------------------
			-- Calculate the Remaining Time - Subtract the Uptime (runtime) from the value from the UDE 
			-- and that is how much 
			-- Runtime is left before the next activity is create.
			-- Note: this asssumes all the remaining time the line will be running
			-------------------------------------------------------------------------------------------
			UPDATE #RecentActivities
				SET  RemainingTime = Duration - UpTime  WHERE Sheet_Type = 25
			UPDATE #RecentActivities
				SET  RemainingTime = 0 Where RemainingTime < 0 and Sheet_Type = 25 
			-------------------------------------------------------------------------------------------
			-- now for why we do all of this....
			-- Determine the Next activity Estimate by adding the remaining Time Number to the Current 
			-- Timestamp
			-------------------------------------------------------------------------------------------
			UPDATE #RecentActivities
				SET  NextActivityEstimate = DATEADD(s,RemainingTime, @CurrentTimeStamp) WHERE Sheet_Type = 25
			-------------------------------------------------------------------------------------------
			-- END : Determine next activity Estimate  for UDE Type activities
			-------------------------------------------------------------------------------------------
			--Format as a Next Activity Estimate Countdown Timer
			-------------------------------------------------------------------------------------------
			UPDATE #RecentActivities
				SET  NextActivityEstimateCD =RIGHT('00'+ convert(varchar(2), RemainingTime/3600),2) + ':'  
												+ RIGHT('00'+ convert(varchar(2),(RemainingTime % 3600)/60),2)+ ':'  
												+ RIGHT('00'+ convert(varchar(2),(RemainingTime % 3600) % 60),2)
			
			-------------------------------------------------------------------------------------------
			-- Format the Activity CD Tracker
			-------------------------------------------------------------------------------------------
			UPDATE #RecentActivities
				SET  ActivityCDTracker = NextActivityEstimateCD where LastActivityEndTime IS NULL
			UPDATE #RecentActivities
				SET  ActivityCDTracker = 'Complete('
												+ RIGHT('00'+ convert(varchar(2), RemainingTimeComplete/3600),2) + ':'  
												+ RIGHT('00'+ convert(varchar(2),(RemainingTimeComplete % 3600)/60),2)+ ':'  
												+ RIGHT('00'+ convert(varchar(2),(RemainingTimeComplete % 3600) % 60),2) + ')'
												where LastActivityEndTime IS NOT NULL
			-------------------------------------------------------------------------------------------
			-- Set Optional Color Field
			-- Green (1)  - completed
			-- White-(0) kicked off and less than 15 minutes (CalendarTime < 15*60 = 900
			-- Yellow(3) - > 15 minutes and Less than 30 minutes Total CT < 30*60 = 1800
			-- Red(2) - > 30 Minutes CalendarTime > 1800
			------------------------------------------------------------------------------------------
			UPDATE #RecentActivities
				SET  Color = '1' where LastActivityEndTime IS NOT NULL;
			UPDATE #RecentActivities
				SET  Color = '2' where Color is NUll and CalendarTime >= 1800;
			UPDATE #RecentActivities
				SET  Color = '3' where Color is NUll and CalendarTime >= 900;
			UPDATE #RecentActivities
				SET  Color = '0' where Color is NUll

			-------------------------------------------------------------------------------------------
			-- return the results 
			-------------------------------------------------------------------------------------------
			SELECT Sheet_Id,
					Sheet_type,
					Equipment,
					Sheet_Desc,
					Unit_Id,
					Event_Subtype_Id,
					ec_id,
					Duration,
					CalendarTime,
					DownTime,
					UpTime,
					RemainingTime,
					CalendarTimeComplete,
					DownTimeComplete,
					UpTimeComplete,
					RemainingTimeComplete,
					LastActivityID,
					LastActivityName,
					CurrentTimeStamp,
					LastActivityStartTime,
					LastActivityEndTime,
					LastActivityStatusID,
					LastActivityStatus,
					NextActivityEstimate,
					NextActivityEstimateCD,
					ActivityCDTracker,
					Color
			FROM #RecentActivities ORDER BY Sheet_Desc
			
			
		END
	DROP TABLE #tmpDowntimes
    DROP TABLE #RecentActivities
END

GO
GRANT EXECUTE ON [dbo].[splocal_SA_NextActivityEstimate] TO [comxclient] AS [dbo]
GO
GRANT EXECUTE ON [dbo].[splocal_SA_NextActivityEstimate] TO [public] AS [dbo]
GO

