package com.rich.AsistenteEscolar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import com.aspose.cells.SaveFormat;
import com.aspose.cells.VbaModule;
import com.aspose.cells.VbaModuleCollection;
import com.aspose.cells.Workbook;
 

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComFailException;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;


import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
 
@Service
public class ChatGPTService {
	
	
	@Autowired
	PowerPointService powerPointService;
	
	@Autowired
	WordService wordService;
	
	    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
	    private static final String API_KEY = "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"; // replace with your actual OpenAI API key

	    public void getResponseFromGPT(String messageContent, HttpServletResponse servletResponse) throws Exception {
	        RestTemplate restTemplate = new RestTemplate();

	        // Create headers
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.setBearerAuth(API_KEY);

	        // Create request body
	        Map<String, Object> body = new HashMap<>();
	        body.put("model", "gpt-3.5-turbo");
	        body.put("messages", Collections.singletonList(
	                new HashMap<String, String>() {{
	                    put("role", "user");
	                    put("content", messageContent);
	                }}
	        ));

	        // Create an entity which includes headers and body
	        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

	        // Make the API call
	        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
	        System.err.println("response->" + response.toString());
	        
	        //Create the powerpoint presentation
	        createWithVba(response.getBody().toString(), servletResponse);
	        


	    }

		private void generateWordDoc(ResponseEntity<String> response, HttpServletResponse servletResponse) throws Exception {
			try {
      		  byte[] wordFileContent = wordService.generateWordFile(response.getBody().toString());
      		  
      		  
      		  servletResponse.setHeader("Content-Disposition", "attachment; filename=generated.docx");
                OutputStream outputStream = servletResponse.getOutputStream();
                outputStream.write(wordFileContent);
                outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		private void generatePresentation(ResponseEntity<String> response, HttpServletResponse servletResponse) {
			  try {
					powerPointService.createPowerPointFromResponse(response);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

 				// Set the response headers
		        servletResponse .setContentType("application/vnd.openxmlformats-officedocument.presentationml.presentation");
		        servletResponse.setHeader("Content-Disposition", "attachment; filename=presentation.pptx");

		        // Write the file to the response
		        try (InputStream in = new FileInputStream("presentation.pptx"); OutputStream out = servletResponse.getOutputStream()) {
		            byte[] buffer = new byte[1024];
		            int bytesRead;
		            while ((bytesRead = in.read(buffer)) != -1) {
		                out.write(buffer, 0, bytesRead);
		            }
		        } catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}//C:\\Users\\ricardo.regalado\\Desktop\\slide2.pptm			
		}

		public void createWithVba(String Code, HttpServletResponse servletResponse) throws Exception {
			  // Instantiate a new Workbook
			Workbook workbook = new Workbook("Libro1.xlsm");

			// Cambiar el código del módulo VBA
			VbaModuleCollection modules = workbook.getVbaProject().getModules();

			for (int i = 0; i < modules.getCount(); i++) {
				VbaModule module = modules.get(i);
				String code = Code;
					module.setCodes(code);
				
			}
			// Set the necessary response headers
		    servletResponse.setContentType("application/vnd.ms-excel.sheet.macroEnabled.12");  // for .xlsm files
		    servletResponse.setHeader("Content-Disposition", "attachment; filename=output.xlsm");
		    
		    // Write the workbook to the servlet response's output stream
		    workbook.save(servletResponse.getOutputStream(), SaveFormat.XLSM);
		}
	        
}
