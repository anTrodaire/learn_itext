package learn_itext.examples;
 
import com.lowagie.text.pdf.Barcode;

import com.lowagie.text.pdf.PdfPCell;

import com.lowagie.text.pdf.BarcodeEAN;

import com.lowagie.text.pdf.PdfPTable;

import com.lowagie.text.pdf.PdfWriter;

import com.lowagie.text.Document;

import com.lowagie.text.DocumentException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Barcodes {
    public static final String DEST = "results/tables/barcode_table.pdf";
 
    public static void main(String[] args) throws IOException,
            com.lowagie.text.DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new Barcodes().createPdf(DEST);
    }
 
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        for (int i = 0; i < 12; i++) {
            table.addCell(createBarcode(writer, String.format("%08d", i)));
        }
        document.add(table);
        document.close();
    }
 
    public static PdfPCell createBarcode(PdfWriter writer, String code) throws DocumentException, IOException {
        BarcodeEAN barcode = new BarcodeEAN();
        barcode.setCodeType(Barcode.EAN8);
        barcode.setCode(code);
        PdfPCell cell = new PdfPCell(barcode.createImageWithBarcode(writer.getDirectContent(), null, null), true);
        cell.setPadding(10);
        return cell;
    }
}