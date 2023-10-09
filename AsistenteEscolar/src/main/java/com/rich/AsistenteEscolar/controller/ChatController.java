package com.rich.AsistenteEscolar.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.rich.AsistenteEscolar.dto.ChatRequest;
import com.rich.AsistenteEscolar.dto.ChatResponse;
import com.rich.AsistenteEscolar.dto.Message;
import com.rich.AsistenteEscolar.service.ExcelService;
import com.rich.AsistenteEscolar.service.utils.Utils;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class ChatController {
    
    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate; 
    
    @Autowired
    private ExcelService excelService;
   
    @Autowired
    Utils utils;
    
    @Value("${openai.model}")
    private String model;
    
    @Value("${openai.api.url}")
    private String apiUrl;
    
    @Value("${openai.temperature}")
    private Double temperature;
    
    @GetMapping("/chat")
    public String chat(@RequestBody String prompt,  HttpServletResponse servletResponse) throws Exception {
        // create a request
        ChatRequest request = new ChatRequest(model, 1, temperature);
    	System.err.println("prompt-->" +  prompt);
        Message systemMessage = new Message("system", "Assistant is an expert VBA programmer designed to help a backend api create excel spreadsheets by providing only the VBA code for the requested functionality, with no comments or instructions of any kind");
        Message userMessage = new Message("user", prompt);
 		request.setMessages(Arrays.asList(systemMessage, userMessage) );
        
        // call the API
        ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);
        
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return "No response";
        }
        
        System.err.println("rawResponse-->" + response.getChoices().get(0).getMessage().getContent());
        
        //String result = StringUtils.substringBetween(response.getChoices().get(0).getMessage().getContent(), "Option Explicit", "```");
        String fixedResult = response.getChoices().get(0).getMessage().getContent().replace("Sheet1", "Hoja1");
        
        excelService.generateFile(utils.getVbaString(), servletResponse);
        
        // return the first response
        return fixedResult;
    }
}