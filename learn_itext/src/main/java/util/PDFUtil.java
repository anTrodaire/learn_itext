package util;
//package ca.gc.cbsa.pacific.decs.actions.printables.util;
//
//import org.apache.pdfbox.cos.COSArray;
//import org.apache.pdfbox.cos.COSDictionary;
//import org.apache.pdfbox.cos.COSName;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
//import org.apache.pdfbox.pdmodel.common.PDRectangle;
//import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
//import org.apache.pdfbox.pdmodel.interactive.form.PDField;
//
//import java.io.IOException;
//
//public class PDFUtil {
//  public static PDRectangle getFieldArea(final PDDocument document, final String fieldName)
//          throws IOException {
//    final PDDocumentCatalog docCatalog = document.getDocumentCatalog();
//    final PDAcroForm acroForm = docCatalog.getAcroForm();
//    final PDField field = acroForm.getField(fieldName);
//
//    final COSDictionary fieldDict = field.getCOSObject();
//    final COSArray fieldAreaArray = (COSArray) fieldDict.getDictionaryObject(COSName.RECT);
//    final PDRectangle rectangle = new PDRectangle(fieldAreaArray);
//    return rectangle;
//  }
//
//  public static void setField(final String name, final PDDocument document, final Integer value)
//          throws IOException {
//    if (value != null) {
//      final PDDocumentCatalog docCatalog = document.getDocumentCatalog();
//      final PDAcroForm acroForm = docCatalog.getAcroForm();
//      final PDField field = acroForm.getField(name);
//      if (field != null) {
//        field.setValue(Integer.toString(value));
//      } else {
//        System.err.println("No field found with name:" + name);
//      }
//    }
//  }
//
//  public static void setField(final String name, final PDDocument document, final String value)
//          throws IOException {
//    if (value != null) {
//      final PDDocumentCatalog docCatalog = document.getDocumentCatalog();
//      final PDAcroForm acroForm = docCatalog.getAcroForm();
//      final PDField field = acroForm.getField(name);
//      if (field != null) {
//        field.setValue(value);
//      } else {
//        System.err.println("No field found with name:" + name);
//      }
//    }
//  }
//}
