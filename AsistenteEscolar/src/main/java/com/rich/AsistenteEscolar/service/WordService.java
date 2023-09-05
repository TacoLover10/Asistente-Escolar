package com.rich.AsistenteEscolar.service;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rich.AsistenteEscolar.service.utils.JsonUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WordService {
	
	@Autowired
    JsonUtils jsonUtils;

    public byte[] generateWordFile(ResponseEntity<String> responseEntity) throws IOException {
        String responseBody = jsonUtils.extractContentFromResponse(responseEntity.getBody());

        XWPFDocument document = new XWPFDocument();
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();

        int listNumber = 0;

        // Handle Markdown formatting
        Pattern pattern = Pattern.compile("(\\*\\*|\\*|##|#|\\-|\\d+\\.|`)");
        Matcher matcher = pattern.matcher(responseBody);
        int lastEnd = 0;

        while (matcher.find()) {
            String before = responseBody.substring(lastEnd, matcher.start());
            String markdown = matcher.group(1);
            run.setText(before);

            run = paragraph.createRun();

            switch (markdown) {
                case "#":
                    run.setBold(true);
                    run.setFontSize(24);
                    break;
                case "##":
                    run.setBold(true);
                    run.setFontSize(18);
                    break;
                case "**":
                    run.setBold(true);
                    break;
                case "*":
                    run.setItalic(true);
                    break;
                case "-":
                    run.setText("• ");
                    break;
                case "`":
                    run.setFontFamily("Courier New");
                    break;
                default: // For ordered list
                    listNumber++;
                    run.setText(listNumber + ". ");
                    break;
            }

            lastEnd = matcher.end();
        }

        String after = responseBody.substring(lastEnd);
        run.setText(after);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.write(byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }
}