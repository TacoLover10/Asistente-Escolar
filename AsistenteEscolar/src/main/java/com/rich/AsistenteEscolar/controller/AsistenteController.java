package com.rich.AsistenteEscolar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rich.AsistenteEscolar.service.ChatGPTService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/asistant/")
public class AsistenteController {
	
	@Autowired
	ChatGPTService chatGPTService;
	
	 @PostMapping(value ="/api/generateFile", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	 public void postString(@RequestBody String prompt, HttpServletResponse response) throws Exception {
		 
	        chatGPTService.getResponseFromGPT(prompt, response);

		 
	        // perform some operation with myString
	       // chatGPTService.getResponseFromGPT(prompt, response);
	    }
}
