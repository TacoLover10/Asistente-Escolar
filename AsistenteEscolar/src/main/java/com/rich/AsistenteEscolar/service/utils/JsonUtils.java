package com.rich.AsistenteEscolar.service.utils;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JsonUtils {
	
	public String extractContentFromResponse(String jsonResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Parse the JSON response
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        // Extract the "content" string
        String content = rootNode.path("choices").get(0).path("message").path("content").asText();

        return content;
    }

}
