package com.rich.AsistenteEscolar.service;
import org.apache.poi.xslf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rich.AsistenteEscolar.service.utils.JsonUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class PowerPointService {
	
	@Autowired
	JsonUtils jsonUtils;

    public void createPowerPointFromResponse(ResponseEntity<String> response) throws IOException {
        String responseBody = jsonUtils.extractContentFromResponse(response.getBody());

        // Split the response body into slides
        List<String> slides = Arrays.asList(responseBody.split("Diapositiva"));

        // Create a new PowerPoint presentation
        XMLSlideShow ppt = new XMLSlideShow();

        // Create a slide for each "Diapositiva"
        for (String slideContent : slides) {
            // Create a new slide
            XSLFSlide slide = ppt.createSlide();

            // Create a text box
            XSLFTextBox textBox = slide.createTextBox();
            textBox.setAnchor(new java.awt.Rectangle(50, 50, 500, 500));

            // Add the slide content to the text box
            XSLFTextParagraph paragraph = textBox.addNewTextParagraph();
            XSLFTextRun run = paragraph.addNewTextRun();
            run.setText(slideContent);
        }

        // Write the PowerPoint presentation to a file
        FileOutputStream out = new FileOutputStream("presentation.pptx");
        ppt.write(out);
        out.close();
        ppt.close();
    }
    
    





}
