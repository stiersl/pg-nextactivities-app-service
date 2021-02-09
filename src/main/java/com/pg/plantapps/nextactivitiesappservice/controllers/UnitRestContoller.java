package com.pg.plantapps.nextactivitiesappservice.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.pg.plantapps.nextactivitiesappservice.model.Unit;
import com.pg.plantapps.nextactivitiesappservice.model.dao.UnitDAO;


@RestController
@CrossOrigin
@RequestMapping("/units")
public class UnitRestContoller {
	
	private UnitDAO unitDAO;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	public UnitRestContoller(UnitDAO unitDAO) {
		this.unitDAO = unitDAO;
	}
	
	//@GetMapping("/")
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public List<Unit> getPreferredUnits() {
		String token = request.getHeader("token");
		//System.out.println("token:" + token );
		List<Unit> result = unitDAO.getPreferredUnits(token);
		if (result == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		System.out.println("get Preferred Units API Call:");
		return result;
	}
}

