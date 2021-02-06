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

import com.pg.plantapps.nextactivitiesappservice.model.Sheet;
import com.pg.plantapps.nextactivitiesappservice.model.dao.SheetDAO;

@Component
public class JDBCSheetDAO implements SheetDAO {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public JDBCSheetDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	@Override
	public List<Sheet> getSheets() {
		List<Sheet> allSheets = new ArrayList<>();
		String sql = "splocal_SA_NextActivity_GetSheets;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		while (results.next()) {
			Sheet newSheet = createSheetFromRow(results);
			allSheets.add(newSheet);
		}
		return 	allSheets;
	}

	private Sheet createSheetFromRow(SqlRowSet results) {
	    Sheet newSheet = new Sheet();
	    newSheet.setSheetId(readValueReturnLong(results, "Sheet_Id"));
	    
	    newSheet.setSheetType(readValueReturnInteger(results, "Sheet_type"));
	    newSheet.setEquipment(results.getString("Equipment"));
	    newSheet.setSheetDesc(results.getString("Sheet_Desc"));
	    
	    newSheet.setUnitId(readValueReturnInteger(results, "Unit_Id"));
	    newSheet.setEventSubTypeId(readValueReturnInteger(results, "Event_Subtype_Id"));
	    newSheet.setEcId(readValueReturnInteger(results, "ec_id"));
	    newSheet.setDuration(readValueReturnInteger(results, "Duration"));
    
		return newSheet;
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

