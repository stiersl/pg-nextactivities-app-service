USE [SOADB]
GO

/****** Object:  StoredProcedure [dbo].[splocal_SA_NextActivities_GetSheets]    Script Date: 2/7/2021 6:21:22 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO



--================================================================================================
--Stored Procedure		:	splocal_SA_NextActivities_GetSheets
--Author				:	Steven Stier , Stier Automation LLC
--Date Created			:	2020-01-14
--Called By				:	Next Activities Estimate API
--Editor Tab Spacing	:	4
--================================================================================================
--Purpose:
--------------------------------------------------------------------------------------------------
-- This SP will get all Sheets to monitor for the Next Activites
--================================================================================================
--Input Parameters:
--------------------------------------------------------------------------------------------------
--Input#	Input Name				Input Type			Description
---------	-------------------		----------			------------------------------------------
--	none										
--================================================================================================
-- Edit History:
-------------------------------------------------------------------------------------------------
--Revision	Date			Who						What
--========	===========		=====================	==================================
-- 0.1		2020-01-14		Steven Stier			Creation of the Stored Procedure 
-- 0.2		2020-02-07		Steven Stier			new SP name splocal_SA_NextActivities_GetSheets
--
--================================================================================================
--TESTING
--------------------------------------------------------------------------------------------------
/*
Exec splocal_SA_NextActivities_GetSheets 
*/
--------------------------------------------------------------------------------------------------
--================================================================================================
CREATE OR ALTER    PROCEDURE [dbo].[splocal_SA_NextActivities_GetSheets]
--WITH ENCRYPTION  
AS
SET NOCOUNT ON  
BEGIN
DECLARE		@ED_FIELD_ID				int

  	--------------------------------------------------------------------------------------------------
	-- Initialize the temp tables
	--------------------------------------------------------------------------------------------------
	-- #Results = holds all the sheets that will be monitored
	--------------------------------------------------------------------------------------------------
    CREATE TABLE #Results(Sheet_Id   INT,
							Sheet_Desc nVarchar(50),
							Sheet_Type INT,
							Event_Subtype_Id INT,
                            Unit_id    INT,
                            Equipment  nVARCHAR(100),
							Ec_id INT,
							Duration INT
							)
		-------------------------------------------------------------------------------------------
		-- get the sheets for the Autolog Time based displays (Sheet Type = 1)
		-------------------------------------------------------------------------------------------
			
		INSERT INTO #Results( Sheet_Id,
								Sheet_Type,
								Sheet_Desc,
								Unit_id
								)
		SELECT S.Sheet_Id,
				1,
				S.Sheet_Desc,
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
                                                       
				WHERE S.Sheet_Type = 1  AND S.Is_Active = 1
				  
			

			-------------------------------------------------------------------------------------------
			-- gets all the sheets for the Autolog UDE Based Displays - (Sheet Type = 25)
			-------------------------------------------------------------------------------------------
			
            INSERT INTO #Results( Sheet_Id,
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
				   ------Create Activites= True ==> Display_option_id=444 and value = 1 
				   --- Sheet_type 25 = Autolog UDE displays
				   ----- Also determine if the UDP "EstimateActity" is set on UDPs--> Event Subtypes
				   --------------------------------------------------------------------------------------
                        JOIN Sheet_Display_Options AS SDO  with(nolock) ON SDO.Sheet_Id = S.Sheet_Id
                                                              AND SDO.Display_Option_Id = 444
                                                              AND SDO.Value = 1
                   WHERE S.Sheet_type=25 AND S.Is_Active = 1
				   AND S.Event_Subtype_Id IN ( SELECT keyid FROM Table_fields_values tfv with(nolock)
									  JOIN Table_fields tf with(nolock) on tfv.Table_field_id = tf.Table_field_id
				  				  WHERE (Table_field_desc = 'EstimateActivity')  AND  tfv.Value in ('1','Yes'))


			-- Get the Equipment Name - Production unit Name
			UPDATE #Results
				SET Equipment = (SELECT pu_desc FROM Prod_Units_Base  with(nolock) WHERE Pu_id = Unit_id)
			
			-----------------------------------------------------------------------------------------------------------------------
		    -- Get the Duration (used for final calculation on estimated Activity Endtime
			-----------------------------------------------------------------------------------------------------------------------
			-- Get the ECID
			UPDATE #Results
				SET Ec_id = (SELECT Ec_id from Event_Configuration ec  with(nolock) 
							where ec.Event_Subtype_Id  = #Results.Event_Subtype_Id  
							AND ec.Pu_id = #Results.Unit_id)
			
			--- For the UDE displays (sheet_type = 25)  we need to get the duration by getting the configuration from the EC_ID
			--Note were using the unique ed__field description = "TotalTime (In seconds)"
	

			-- get the Uptime Duration Frequency of the UDE model
			Set @ED_FIELD_ID = (SELECT  ED_field_id from ed_fields  with(nolock) 
								where Field_desc = 'TotalTime (In seconds)')

			UPDATE #Results
				SET Duration = (select convert(int, (convert( varchar(max),value)))   
											FROM event_configuration_values ecv with(nolock) 
											JOIN event_configuration_data ecd on ecd.ecv_id = ecv.ecv_id
											WHERE ecd.ec_id = #Results.Ec_id and ED_Field_Id = @ED_FIELD_ID) WHERE Sheet_Type = 25
			
			-- For time based Display used the configuraiton from the sheets
			UPDATE #Results
				SET Duration = (SELECT (s.Interval * 60) FROM Sheets AS s where s.sheet_id = #Results.Sheet_Id) WHERE Sheet_Type = 1
			---------------------------------------------------
			--- Return the results
			-----------------------------------------------------
			SELECT Sheet_Id,
					Sheet_Desc,
					Sheet_type,
					Equipment,
					Unit_Id,
					Event_Subtype_Id,
					ec_id,
					Duration
			FROM #Results ORDER BY Sheet_Desc
	DROP TABLE #Results
END
GO
GRANT EXECUTE ON [dbo].[splocal_SA_NextActivities_GetSheets] TO [comxclient] AS [dbo]
GO
GRANT EXECUTE ON [dbo].[splocal_SA_NextActivities_GetSheets] TO [public] AS [dbo]
GO