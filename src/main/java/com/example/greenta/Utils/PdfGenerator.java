package com.example.greenta.Utils;

import com.example.greenta.Models.Epreuve;
import com.example.greenta.Models.Question;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class PdfGenerator {
    public static Document createPDF(String dest){
        PdfWriter writer;
        PdfDocument pdf;
        Document document = null;
        try {
            writer = new PdfWriter(dest);
            pdf = new PdfDocument(writer);
            document = new Document(pdf, PageSize.A4);
            document.setMargins(10, 20, 20, 20);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return document;
    }

    public static Table generateQuestionsTablePDF(List<Question> questions) {
        Table table = new Table(new float[]{7,3}).useAllAvailableWidth();
        PdfFont bold = null;
        try {
            bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Cell headerCell = new Cell(1, 2)
                .add(new Paragraph("Question Details").setFont(bold))
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(10)
                .setBackgroundColor(new DeviceRgb(140, 221, 8));
        table.addCell(headerCell);

        table.addCell(new Cell().add(new Paragraph("Question").setFont(bold)));
        table.addCell(new Cell().add(new Paragraph("Note").setFont(bold)));



        for (Question q : questions) {
            table.addCell(q.getQuestion());
            table.addCell(String.valueOf(q.getNote()));
        }

        return table;
    }
    public static void addEpreuveDetails(Document document, Epreuve epreuve){
        PdfFont font;
        try {
            font=PdfFontFactory.createFont(StandardFonts.HELVETICA);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Paragraph noteParagraph=new Paragraph("Note: "+epreuve.getNote())
                .setFont(font)
                .setFontSize(12);
        document.add(noteParagraph);
        Paragraph dateParagraph=new Paragraph("Date: "+epreuve.getDate_p())
                .setFont(font)
                .setFontSize(12);
        document.add(dateParagraph);
        String message="";
        if(epreuve.getNote()>0){
            message="Bravo! Pour vos accomplissements exceptionnels, nous vous remettons ce certificat de succès.";
        }else{
            message="Merci pour votre participation!";
        }

        Paragraph messageParagraph=new Paragraph(message)
                .setFont(font)
                .setFontSize(24);
        document.add(messageParagraph);


    }
}
