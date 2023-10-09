package com.rich.AsistenteEscolar.service;

import org.springframework.stereotype.Component;

import com.aspose.cells.SaveFormat;
import com.aspose.cells.VbaModule;
import com.aspose.cells.VbaModuleCollection;
import com.aspose.cells.Workbook;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class ExcelService {

	public void generateFile(String content,  HttpServletResponse servletResponse) throws Exception {
		 // Instantiate a new Workbook
		Workbook workbook = new Workbook("Libro1.xlsm");

		// Cambiar el código del módulo VBA
		VbaModuleCollection modules = workbook.getVbaProject().getModules();

		for (int i = 0; i < modules.getCount(); i++) {
			VbaModule module = modules.get(i);
			String code = content;
				module.setCodes(code);
			
		}
		// Set the necessary response headers
	    servletResponse.setContentType("application/vnd.ms-excel.sheet.macroEnabled.12");  // for .xlsm files
	    servletResponse.setHeader("Content-Disposition", "attachment; filename=output.xlsm");
	    
	    // Write the workbook to the servlet response's output stream
	    workbook.save(servletResponse.getOutputStream(), SaveFormat.XLSM);
		
	}
	

}
