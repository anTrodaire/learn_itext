package learn_itext.barcodes;

import com.lowagie.text.Rectangle;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.Barcode39;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

public class BarcodeDev {

  private static final String DEST = "C:/Users/axp338/Desktop/basic_barcode.pdf";

  public static void main(String[] args) {
    /*
     * // * Label print area is 3.5" * 1.1" 1000 = 13.9" 1 = 0.0139" 3.5" ~= 251
     * // * 1.1" ~= 79 //
     */
    Rectangle pageArea = new Rectangle(250, 80);

    Document doc = new Document(pageArea);
    PdfWriter writer = null;
    try {
      writer = PdfWriter.getInstance(doc, new FileOutputStream(DEST));
    } catch (FileNotFoundException | DocumentException e) {
      e.printStackTrace();
      return;
    }

    doc.open();

    PdfContentByte contentBytes = writer.getDirectContent();

    Barcode39 code39 = new Barcode39();
    code39.setCode("8211-17-02-0001");
    code39.setAltText("8211-17-02-0001");
    try {
      doc.add(code39.createImageWithBarcode(contentBytes, null, null));
    } catch (DocumentException e) {
      e.printStackTrace();
      return;
    }

    doc.close();
  }
}
