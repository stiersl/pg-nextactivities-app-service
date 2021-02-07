package com.pg.plantapps.nextactivitiesappservice.model.dao.jdbc;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.pg.plantapps.nextactivitiesappservice.model.NextActivity;
import com.pg.plantapps.nextactivitiesappservice.model.dao.NextActivityDAO;

@Component
public class JDBCNextActivityDAO implements NextActivityDAO {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public JDBCNextActivityDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	@Override
	public List<NextActivity> getNextActivities(String puids) {
		List<NextActivity> allActivityEstimates = new ArrayList<>();
		String sql = "splocal_SA_NextActivityEstimate  ?;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql,puids);
		while (results.next()) {
			NextActivity newNextActivity = createNextActivityFromRow(results);
			allActivityEstimates.add(newNextActivity);
		}
		return 	allActivityEstimates;
	}

	private NextActivity createNextActivityFromRow(SqlRowSet results) {
    NextActivity newNextActivity = new NextActivity();
    
    newNextActivity.setSheetId(readValueReturnLong(results, "Sheet_id"));
    newNextActivity.setLastActivityID(readValueReturnLong(results, "LastActivityID"));
    
    newNextActivity.setEquipment(results.getString("Equipment"));
    newNextActivity.setSheetDesc(results.getString("Sheet_Desc"));
    newNextActivity.setLastActivityStatus(results.getString("LastActivityStatus"));
    newNextActivity.setLastActivityName(results.getString("LastActivityName"));
    newNextActivity.setNextActivityEstimateCD(results.getString("NextActivityEstimateCD"));
    newNextActivity.setActivityCDTracker(results.getString("ActivityCDTracker"));

    newNextActivity.setSheetType(readValueReturnInteger(results, "Sheet_type"));
    newNextActivity.setUnitId(readValueReturnInteger(results, "Unit_id"));
    newNextActivity.setDuration(readValueReturnInteger(results, "Duration"));
    newNextActivity.setCalendarTime(readValueReturnInteger(results, "CalendarTime"));
    newNextActivity.setDownTime(readValueReturnInteger(results, "DownTime"));
    newNextActivity.setUpTime(readValueReturnInteger(results, "UpTime"));
    newNextActivity.setRemainingTime(readValueReturnInteger(results, "RemainingTime"));
    
    newNextActivity.setCurrentTimeStamp(readValueReturnTS(results, "CurrentTimeStamp"));
    newNextActivity.setLastActivityStartTime(readValueReturnTS(results, "LastActivityStartTime"));
    newNextActivity.setLastActivityEndTime(readValueReturnTS(results, "LastActivityEndTime"));
    newNextActivity.setNextActivityEstimate(readValueReturnTS(results, "NextActivityEstimate"));
    newNextActivity.setColor(readValueReturnInteger(results, "Color"));
		return newNextActivity;
	}

	private Double readValueReturnDouble(SqlRowSet srs, String columnName) {
		String s = srs.getString(columnName);
		return (s != null) ? Double.parseDouble(s) : null;
	}

	private Integer readValueReturnInteger(SqlRowSet srs, String columnName) {
		String s = srs.getString(columnName);
		return (s != null) ? Integer.parseInt(s) : null;
  }
  
  private Long readValueReturnLong(SqlRowSet srs, String columnName) {
		String s = srs.getString(columnName);
		return (s != null) ? Long.parseLong(s) : null;
	}

	private Boolean readValueReturnBoolean(SqlRowSet srs, String columnName) {
		String s = srs.getString(columnName);
		return (s != null) ? Boolean.parseBoolean(s) : null;
	}

	private LocalDateTime readValueReturnLDT(SqlRowSet srs, String columnName) {
		Timestamp sqlTimestamp = srs.getTimestamp(columnName);
		return (sqlTimestamp != null) ? sqlTimestamp.toLocalDateTime() : null;
  }
  private Timestamp readValueReturnTS(SqlRowSet srs, String columnName) {
		Timestamp sqlTimestamp = srs.getTimestamp(columnName);
		return (sqlTimestamp != null) ? sqlTimestamp : null;
	}
  private Instant readValueReturnInstant(SqlRowSet srs, String columnName) {
		Timestamp sqlTimestamp = srs.getTimestamp(columnName);
		return (sqlTimestamp != null) ? sqlTimestamp.toInstant() : null;
	}
}

