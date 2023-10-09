package com.rich.AsistenteEscolar.service;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rich.AsistenteEscolar.service.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.FileOutputStream;
import java.io.File;

@Service
public class WordService {
	
	@Autowired
    Utils utils;

	public byte[] generateWordFile(String markdownString) throws Exception {
        XWPFDocument document = new XWPFDocument();
        
        String[] lines = markdownString.split("\n");
        
        for (String line : lines) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            
            if (line.startsWith("# ")) {
                paragraph.setStyle("Heading 1");
                run.setText(line.substring(2));
            } else if (line.startsWith("## ")) {
                paragraph.setStyle("Heading 2");
                run.setText(line.substring(3));
            } else if (line.startsWith("### ")) {
                paragraph.setStyle("Heading 3");
                run.setText(line.substring(4));
            } else if (line.startsWith("- ")) {
                paragraph.setStyle("ListBullet");
                run.setText(line.substring(2));
            } else if (line.startsWith("1. ")) {
                paragraph.setStyle("ListNumber");
                run.setText(line.substring(3));
            } else {
                run.setText(line);
            }
        }
        
        // Save the document to a byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.write(byteArrayOutputStream);
        
        return byteArrayOutputStream.toByteArray();
    }

}