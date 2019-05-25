package com.bezbednost.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.core.io.Resource;

@Controller
public class UploadDataController {
	
	@GetMapping("/files/{filename}")
	@ResponseBody
	public ResponseEntity<Resource> uploadData(@PathVariable String filename){
		
		Resource file = loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, 
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}
	
	private Resource loadAsResource(String filname) {
		return null;
	}

}
