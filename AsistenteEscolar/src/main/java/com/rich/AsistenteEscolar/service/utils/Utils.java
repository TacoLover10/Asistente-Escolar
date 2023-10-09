package com.rich.AsistenteEscolar.service.utils;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Utils {
	
	public String extractContentFromResponse(String jsonResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Parse the JSON response
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        // Extract the "content" string
        String content = rootNode.path("choices").get(0).path("message").path("content").asText();

        return content;
    }
	

	public String generateRandomUUID() {
		  UUID uuid = UUID.randomUUID();
 	        String uuidString = uuid.toString();
	        return uuidString;
	}
	
	public String getVbaString() {
		return "Sub InsertDataAndFunction()\r\n"
				+ "    Dim ws As Worksheet\r\n"
				+ "    Set ws = ActiveSheet ' Refer to the currently active sheet\r\n"
				+ "    \r\n"
				+ "    ' Set up Columns\r\n"
				+ "    ws.Range(\"A1\").Value = \"Full Name\"\r\n"
				+ "    ws.Range(\"B1\").Value = \"Age\"\r\n"
				+ "    ws.Range(\"C1\").Value = \"Base Salary\"\r\n"
				+ "    ws.Range(\"D1\").Value = \"Actual Salary\"\r\n"
				+ "    ws.Range(\"E1\").Value = \"Gender\"\r\n"
				+ "    ws.Range(\"F1\").Value = \"Start Date\"\r\n"
				+ "    ws.Range(\"G1\").Value = \"Years Working\"\r\n"
				+ "    ws.Range(\"H1\").Value = \"Vacation Days\"\r\n"
				+ "    \r\n"
				+ "    ' Set up Data Validation for Gender\r\n"
				+ "    With ws.Range(\"E2:E1000\").Validation\r\n"
				+ "        .Delete\r\n"
				+ "        .Add Type:=xlValidateList, AlertStyle:=xlValidAlertStop, Operator:=xlBetween, Formula1:=\"male,female,other\"\r\n"
				+ "        .IgnoreBlank = True\r\n"
				+ "        .InCellDropdown = True\r\n"
				+ "        .ShowInput = True\r\n"
				+ "        .ShowError = True\r\n"
				+ "    End With\r\n"
				+ "    \r\n"
				+ "    ' Add formulas\r\n"
				+ "    ws.Range(\"D2\").Formula = \"=IF(G2<=5,C2,C2*1.5*1.05^(G2-5))\"\r\n"
				+ "    ws.Range(\"G2\").Formula = \"=IF(F2=\"\"\"\", \"\"\"\", YEAR(TODAY())-YEAR(F2))\"\r\n"
				+ "    ws.Range(\"H2\").Formula = \"=IF(G2<=5,10,IF(G2<=7,15,20))\"\r\n"
				+ "    \r\n"
				+ "    ' Copy formulas down\r\n"
				+ "    ws.Range(\"D2:H2\").AutoFill Destination:=ws.Range(\"D2:H1000\"), Type:=xlFillDefault\r\n"
				+ "    \r\n"
				+ "End Sub\r\n"
				+ "";
	}

}
