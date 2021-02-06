package com.pg.plantapps.nextactivitiesappservice.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.pg.plantapps.nextactivitiesappservice.model.Sheet;
import com.pg.plantapps.nextactivitiesappservice.model.dao.SheetDAO;

@RestController
@CrossOrigin
@RequestMapping("/sheets")
public class SheetRestController {

	private SheetDAO sheetDAO;

	@Autowired
	public SheetRestController(SheetDAO sheetDAO) {
		this.sheetDAO = sheetDAO;
	}

	@GetMapping("/")
	public List<Sheet> getActivityEstimates() {
		List<Sheet> result = sheetDAO.getSheets();
		if (result == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		System.out.println("calling get sheets API:");
		return result;
	}

}
