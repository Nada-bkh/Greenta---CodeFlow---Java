package com.example.greenta.Services;

import com.example.greenta.Models.Charity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ExcelService {
    public void ExcelCreator(List<Charity> charities, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Charity Data");

            // Add the titles of the columns
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Name of Charity");
            headerRow.createCell(1).setCellValue("Location");
            headerRow.createCell(2).setCellValue("Amount Donated");
            headerRow.createCell(3).setCellValue("Last Date");

            // Add data to the Excel file
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            int rowNum = 1; // Start from the second row since the first row is for headers
            for (Charity charity : charities) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(charity.getName_of_charity());
                row.createCell(1).setCellValue(charity.getLocation());
                row.createCell(2).setCellValue(charity.getAmount_donated());
                row.createCell(3).setCellValue(dateFormat.format(charity.getLast_date()));
            }

            // Write the Excel file to disk
            FileOutputStream fileOut = new FileOutputStream(filePath);
            workbook.write(fileOut);
            fileOut.close();

            System.out.println("Excel file created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
