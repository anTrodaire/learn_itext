package learn_itext.fill_pdfs;
//package ca.gc.cbsa.pacific.decs.actions.printables.documents;
//
//import ca.gc.cbsa.pacific.decs.abstracts.DECSAction;
//import ca.gc.cbsa.pacific.decs.actions.printables.util.PDFUtil;
//import ca.gc.cbsa.pacific.decs.hbos.UserHBO;
//import ca.gc.cbsa.pacific.decs.hbos.places.PortHBO;
//import ca.gc.cbsa.pacific.decs.hbos.places.WarehouseHBO;
//import ca.gc.cbsa.pacific.decs.hbos.records.ClientHBO;
//import ca.gc.cbsa.pacific.decs.hbos.records.DispositionHBO;
//import ca.gc.cbsa.pacific.decs.hbos.records.ItemHBO;
//import ca.gc.cbsa.pacific.decs.hbos.records.LatestDispositionHBO;
//import ca.gc.cbsa.pacific.decs.hbos.records.MasterHBO;
//import ca.gc.cbsa.pacific.decs.util.ControlNumRegexes;
//import ca.gc.cbsa.pacific.decs.util.FormatDateTime;
//import ca.gc.cbsa.pacific.decs.util.Logging;
//import ca.gc.cbsa.pacific.decs.util.SessionManager;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.common.PDRectangle;
//import org.apache.pdfbox.pdmodel.font.PDFont;
//import org.apache.pdfbox.pdmodel.font.PDType1Font;
//import org.apache.struts2.ServletActionContext;
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.InputStream;
//import java.math.BigDecimal;
//import java.util.Calendar;
//
//public class E44 extends DECSAction {
//
//	private static final String	fileName			= "E44.pdf";
//	private static final String	FILEPATH			= ServletActionContext.getServletContext().getRealPath("/WEB-INF/PDFs/e44.pdf");
//	private static Logger		LOGGER				= LogManager.getLogger(E44.class);
//	private static final long	serialVersionUID	= 1L;
//
//	private String				controlNum;
//
//	private InputStream			response;
//
//	/**
//	 * Generate a E44 for items previously marked by the user.
//	 */
//	@Override
//	public String execute() {
//		String msg = SUCCESS;
//		Session session = null;
//		Transaction tx = null;
//
//		try {
//			session = SessionManager.getSession();
//			tx = session.beginTransaction();
//
//			ItemHBO item = null;
//
//			if (this.controlNum == null) {
//				this.controlNum = (String) this.userSession.get("mcn");
//			}
//
//			// master, latest, c1, c2, warehouse
//			if (this.controlNum.matches(ControlNumRegexes.MCN_REGEX)) {
//				item = (ItemHBO) session.createQuery(
//						"SELECT item FROM ItemHBO item " +
//								"INNER JOIN FETCH item.master AS master " +
//								"INNER JOIN FETCH master.principalOfficer AS officer " +
//								"INNER JOIN FETCH master.client1 " +
//								"INNER JOIN FETCH master.client2 " +
//								"INNER JOIN FETCH master.warehouse " +
//								"INNER JOIN FETCH officer.port " +
//								"WHERE master.mcn = :mcn")
//						.setString("mcn", this.controlNum)
//						.setMaxResults(1)
//						.list()
//						.get(0);
//			} else if (this.controlNum.toLowerCase().matches(ControlNumRegexes.UCL_REGEX)) {
//				final DispositionHBO tempDisp = (DispositionHBO) session.createQuery(
//						"SELECT disposition FROM DispositionHBO disposition " +
//								"INNER JOIN FETCH disposition.item AS item " +
//								"INNER JOIN FETCH item.master AS master " +
//								"INNER JOIN FETCH master.principalOfficer AS officer " +
//								"INNER JOIN FETCH master.client1 " +
//								"INNER JOIN FETCH master.client2 " +
//								"INNER JOIN FETCH master.warehouse " +
//								"INNER JOIN FETCH officer.port " +
//								"WHERE LOWER(disposition.fileNum) = :fileNum")
//						.setString("fileNum", this.controlNum)
//						.setMaxResults(1)
//						.list()
//						.get(0);
//				item = tempDisp.getItem();
//			} else if (this.controlNum.matches(ControlNumRegexes.ICN_REGEX)) {
//				item = (ItemHBO) session.createQuery(
//						"SELECT item FROM ItemHBO item " +
//								"INNER JOIN FETCH item.master AS master " +
//								"INNER JOIN FETCH master.principalOfficer AS officer " +
//								"INNER JOIN FETCH master.client1 " +
//								"INNER JOIN FETCH master.client2 " +
//								"INNER JOIN FETCH master.warehouse " +
//								"INNER JOIN FETCH officer.port " +
//								"WHERE item.icn = :icn")
//						.setString("icn", this.controlNum)
//						.uniqueResult();
//			}
//
//			if (item != null) {
//				final MasterHBO master = item.getMaster();
//
//				final LatestDispositionHBO latestPairing = (LatestDispositionHBO) session.createQuery(
//						"SELECT latest FROM LatestDispositionHBO latest " +
//								"INNER JOIN FETCH latest.disposition " +
//								"INNER JOIN latest.item AS item " +
//								"WHERE item.icn = :icn")
//						.setString("icn", item.getIcn())
//						.uniqueResult();
//
//				final DispositionHBO disp = latestPairing.getDisposition();
//
//				final UserHBO principalOfficer = master.getPrincipalOfficer();
//
//				final PortHBO originalPort = principalOfficer.getPort();
//
//				final String masterTimestamp = FormatDateTime.formatDate(master.getTimestamp());
//
//				final PDDocument document = PDDocument.load(new File(FILEPATH));
//
//				final ClientHBO client = whichClient(master, document);
//
//				if (client != null) {
//					final String clientInfo = client.getFirstLastName() + "\n" + client.getStreet()
//							+ "\n" + client.getCity() + ", " + client.getProvince() + ", "
//							+ client.getPostalCode();
//
//					PDFUtil.setField("CLIENT_INFO", document, clientInfo);
//				}
//
//				final String ccn = master.getCcn();
//				if ((disp.getFileNum() != null) && (ccn != null) && (ccn.length() > 4)) {
//					PDFUtil.setField("UCL_NUM", document, master.getCcn().substring(0, 4) + "E44-" + (originalPort.getPortCode() / 10) + "-"
//							+ disp.getFileNum());
//					PDFUtil.setField("BARCODE", document, "*" + disp.getFileNum().toUpperCase() + "*");
//				}
//				PDFUtil.setField("CBSA_OFFICE", document, originalPort.getOfficeName());
//				PDFUtil.setField("DATE_1", document, masterTimestamp);
//
//				if ((this.user.getPhone() != null) && (this.user.getPhone().length() > 1)) {
//					PDFUtil.setField("TELEPHONE_1", document, principalOfficer.getPhone());
//					PDFUtil.setField("TELEPHONE_2", document, principalOfficer.getPhone());
//				} else {
//					PDFUtil.setField("TELEPHONE_1", document, originalPort.getPhone());
//					PDFUtil.setField("TELEPHONE_2", document, originalPort.getPhone());
//				}
//
//				PDFUtil.setField("OFFICER_NAME", document, principalOfficer.getFormattedName());
//
//				final Calendar arrivalCal = master.getArrivalDate();
//
//				if (arrivalCal != null) {
//					final String arrivalDate = arrivalCal.get(Calendar.YEAR) + "-" +
//							String.format("%02d", (arrivalCal.get(Calendar.MONTH) + 1)) + "-" +
//							String.format("%02d", arrivalCal.get(Calendar.DATE));
//
//					PDFUtil.setField("ARRIVAL_1", document, arrivalDate);
//				}
//
//				PDFUtil.setField("CCN_1", document, master.getCcn()); // "original document/document original"
//				PDFUtil.setField("PIECES_1", document, item.getPieceCount());
//				PDFUtil.setField("VALUE_1", document, item.getItemValue().compareTo(new BigDecimal(-1)) == 0 ? "N/A" : ("$" + String.format("%.2f", item.getItemValue())));
//
//				final PDRectangle rectangle = PDFUtil.getFieldArea(document, "DESCRIPTION_1");
//				final float boxWidth = rectangle.getWidth();
//				final PDFont font = PDType1Font.HELVETICA;
//				final int fontSize = 11;
//
//				final String[] description = item.getDescription().split(" ");
//				final StringBuilder formattedDescription = new StringBuilder();
//				StringBuilder currentTextLine = new StringBuilder();
//				String testString;
//
//				for (final String word : description) {
//					testString = currentTextLine.toString() + " " + word;
//					if (((font.getStringWidth(testString) / 1000) * fontSize) < boxWidth) {
//						if (currentTextLine.length() == 0) {
//							currentTextLine.append(word);
//						} else {
//							currentTextLine.append(" " + word);
//						}
//					} else {
//						formattedDescription.append(currentTextLine.toString() + "\n");
//						currentTextLine = new StringBuilder(word);
//					}
//				}
//				formattedDescription.append(currentTextLine);
//				// formattedDescription.deleteCharAt(0);
//
//				PDFUtil.setField("DESCRIPTION_1", document, formattedDescription.toString());
//
//				if (master.getWarehouse().getWarehouseID() != 0) {
//					final WarehouseHBO warehouse = master.getWarehouse();
//					final String warehouseLocation =
//							warehouse.getName() + "\n" +
//									warehouse.getStreet() + ", " + warehouse.getCity() + ", " +
//									warehouse.getProvince() + ", " + warehouse.getPostalCode();
//					PDFUtil.setField("LOCATED_AT", document, warehouseLocation);
//				}
//				PDFUtil.setField("WEIGHT", document, master.getWeight());
//
//				final ByteArrayOutputStream out = new ByteArrayOutputStream();
//				document.save(out);
//				document.close();
//
//				final InputStream pdf = new ByteArrayInputStream(out.toByteArray());
//				this.userSession.put("pdf", pdf);
//				this.userSession.put("pdfName", fileName);
//			} else {
//				msg = ERROR;
//			}
//		} catch (final Exception e) {
//			if ((tx != null) && tx.isActive()) {
//				tx.rollback();
//			}
//			Logging.error(LOGGER, e, this.user);
//			msg = ERROR;
//		} finally {
//			if ((session != null) && session.isOpen()) {
//				session.close();
//			}
//		}
//		this.response = new ByteArrayInputStream(msg.getBytes());
//		return msg;
//	}
//
//	public String getControlNum() {
//		return this.controlNum;
//	}
//
//	public InputStream getResponse() {
//		return this.response;
//	}
//
//	public void setControlNum(final String controlNum) {
//		this.controlNum = controlNum;
//	}
//
//	private static ClientHBO whichClient(final MasterHBO master, final PDDocument document) {
//		final ClientHBO c1 = master.getClient1();
//		final ClientHBO c2 = master.getClient2();
//
//		if (c1.getClientType().equals("Importer")) {
//			return c1;
//		} else if (c2.getClientType().equals("Importer")) {
//			return c2;
//		}
//
//		return null;
//	}
//}
