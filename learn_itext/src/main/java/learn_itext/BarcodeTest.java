package learn_itext;

import com.lowagie.text.FontFactory;

import com.lowagie.text.Font;

import com.lowagie.text.pdf.BaseFont;

import com.lowagie.text.Paragraph;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.Barcode39;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BarcodeTest {
  /**
   * Generate a pdf for a Dymo label maker using 3.5"x1.1" label paper
   * 
   * @return pdf document as an array of bytes, ready to be passed back to the
   *         client.
   * @throws Exception
   *           Throw the exception so we can easily tell if the function was
   *           successful or not, and print the error msg to the user if not.
   * 
   */
  public static void main(final String[] args) {
    /*
     * Label print area is 3.5" * 1.1" 1000 = 13.9" 1 = 0.0139" 3.5" ~= 251 1.1"
     * ~= 79
     */
    Rectangle pageArea = new Rectangle(200, 60);

    Document document = new Document(pageArea);
    document.setMargins(2, 2, 2, 2);

    PdfWriter writer = null;
    try {
      writer =
              PdfWriter.getInstance(document, new FileOutputStream(
                      "C:/Users/axp338/Desktop/barcode_test.pdf"));
    } catch (FileNotFoundException | DocumentException e) {
      e.printStackTrace();
    }

    document.open();

    final List<BarcodeInfo> barcodes = new ArrayList<BarcodeInfo>();

    final BarcodeInfo test = new BarcodeInfo("8212-15-09-0072-01", 1, 1);
    barcodes.add(test);

    // Create and populate a page for each barcode
    for (int i = 0; i < barcodes.size(); i++) {
      BarcodeInfo codeInfo = barcodes.get(i);
      // document.newPage();

      // Set the page to the right size (honestly not sure which are
      // necessary but it works)
      // page.setMediaBox(box);
      // page.setBleedBox(box);
      // page.setCropBox(box);
      // page.setTrimBox(box);
      // document.addPage(page);

      String icn = codeInfo.getIcn();
      String mcn = icn.substring(0, 15);
      String counts =
              "Item " + codeInfo.getItemNum() + " of " + codeInfo.getTotalItems() + ", Piece 1 of "
                      + codeInfo.getPieceCount();

      try {
        writeBarcode(document, writer, icn, mcn, counts);
      } catch (IOException e) {
        e.printStackTrace();
      }

      // if (codeInfo.getPieceCount() > 1) {
      // for (int j = 2; j <= codeInfo.getPieceCount(); j++) {
      // document.newPage();
      //
      // counts =
      // "Item " + codeInfo.getItemNum() + " of " + codeInfo.getTotalItems() +
      // ", Piece "
      // + j + " of " + codeInfo.getPieceCount();
      //
      // try {
      // writeBarcode(document, writer, icn, mcn, counts);
      // } catch (IOException e) {
      // e.printStackTrace();
      // }
      // }
      // }
    }

    document.close();

  }

  private static void writeBarcode(final Document document, PdfWriter writer, final String icn,
          final String mcn, final String counts) throws IOException {
    try {
      PdfPTable table = new PdfPTable(1);
      table.setWidthPercentage(100);
      table.setKeepTogether(true);
      table.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
      table.setSplitRows(false);
      
      Barcode39 icnCode = new Barcode39();
      icnCode.setCode(icn);
      icnCode.setAltText(icn);
      icnCode.setBarHeight(13);
      icnCode.setSize(8);
      
      PdfPCell icnCell = new PdfPCell(icnCode.createImageWithBarcode(writer.getDirectContent(), null, null), true);
      icnCell.setBorder(PdfPCell.NO_BORDER);
      table.addCell(icnCell);
      
      PdfPCell countsCell = new PdfPCell();
      countsCell.setBorder(PdfPCell.NO_BORDER);
      Font plainText = FontFactory.getFont(BaseFont.HELVETICA, 6);
      
      Paragraph countsP = new Paragraph(counts, plainText);
      countsP.setAlignment(Paragraph.ALIGN_CENTER);
//      p.setSpacingBefore(0);
//      p.setSpacingAfter(0);
      countsCell.setPadding(0);
      countsCell.addElement(countsP);
      table.addCell(countsCell);
      
      Barcode39 mcnCode = new Barcode39();
      mcnCode.setCode(mcn);
      mcnCode.setAltText(mcn);
      mcnCode.setBarHeight(12);
      mcnCode.setSize(8);

      PdfPCell mcnCell = new PdfPCell(mcnCode.createImageWithBarcode(writer.getDirectContent(), null, null), true);
      mcnCell.setBorder(PdfPCell.NO_BORDER);
      table.addCell(mcnCell);


//      p = new Paragraph();
//      p.setKeepTogether(true);
//      p.add(counts);
//      document.add(p);
//
//      code39 = new Barcode39();
//      code39.setCode(mcn);
//      code39.setAltText(mcn);
//      document.add(code39.createImageWithBarcode(contentBytes, null, null));
      
      document.add(table);
    } catch (DocumentException e) {
      e.printStackTrace();
      return;
    }
  }
}
