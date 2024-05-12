package gui.other;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import entity.MovieScheduleSeat;
import entity.Order;
import entity.OrderDetail;

public class CreateMovieTickets {
	private static String createLine(int doDai, char kyTu) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < doDai; i++) {
			sb.append(kyTu);
		}
		sb.append("\n");
		return sb.toString();
	}

	public static void createTicket(List<OrderDetail> odList, ArrayList<MovieScheduleSeat> seatList, Order order) {
		DecimalFormat df = new DecimalFormat("$#.00");
		String title = "Ticket\n";
		int padding = (80 - title.length()) / 2;
		String centeredTitle = String.format("%" + (padding + title.length()) + "s", title);

		String cinema = "\nMy Cinema\n";
		String address = "123 Street, Manchester By The Sea,  Masachusetts, USA\n";
		String line = createLine(36, '=');

		String customerinfo = "Customer Infomation:\n";
		String cusid = order.getCustomer().getCustomerID();
		String name = order.getCustomer().getFullName() + "\n";
		String phoneNumber = order.getCustomer().getPhoneNumber() + "\n";
		String mail = order.getCustomer().getEmail() + "\n";

		String movieid = order.getSchedule().getMovie().getMovieID();
		String movie = order.getSchedule().getMovie().getMovieName() + "\n";
		String time = order.getSchedule().getScreeningTime().toLocalTime() + " ~ "
				+ order.getSchedule().getEndTime().toLocalTime() + "\n";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d yyyy", Locale.ENGLISH);
		String date = order.getSchedule().getScreeningTime().format(formatter) + "\n";
		String scheduleid = order.getSchedule().getScheduleID();
		String room = "Room: " + order.getSchedule().getRoom() + "\n";
		String qs = "Quantity Seat: " + order.getQuantityTicket() + "\n";
		String seat = "Seat: ";
		for (int i = 0; i < seatList.size() - 1; i++) {
			seat += seatList.get(i) + ", ";
		}
		seat += seatList.get(seatList.size() - 1) + "\n";
		String emp = "Staff: " + order.getEmployee().getFullName() + "\n";

		String orderID = order.getOrderID();
		String ticketPrice = "Ticket Price:";
		String ticketPriceValue = df.format(order.getSchedule().getPerSeatPrice() * order.getQuantityTicket());
		List<String> productIDList = new ArrayList<String>();
		for (int i = 0; i < odList.size(); i++) {
			productIDList.add(odList.get(i).getProduct().getProductID());
		}
		List<String> productNameList = new ArrayList<String>();
		for (int i = 0; i < odList.size(); i++) {
			productNameList.add(odList.get(i).getProduct().getProductName());
		}
		List<String> lineTotalList = new ArrayList<String>();
		for (int i = 0; i < odList.size(); i++) {
			lineTotalList.add(df.format(odList.get(i).getTotal()));
		}
		String transactionTime = order.getOrderDate().format(DateTimeFormatter.ofPattern("HH:mm - yyyy-MM-dd")) + "\n";
		String qrContent = orderID + "; " + cusid + "; " + movieid + "; " + scheduleid;
		for (String pid : productIDList) {
			qrContent += "; " + pid;
		}

		// String defaultFolderPath = "T:\\JavaProject\\Cinema-Java-Project\\data\\";
		String defaultFolderPath = "data/";

		Document document = new Document(new Rectangle(333, 999));
		String fileName = orderID + "ticket.pdf";

		try {
			PdfWriter.getInstance(document, new FileOutputStream(defaultFolderPath + fileName));
			document.open();

			Font font = new Font(Font.FontFamily.COURIER, 12);
			Font fontProduct = new Font(Font.FontFamily.COURIER, 11);
			Font fontBold = new Font(Font.FontFamily.COURIER, 12, Font.BOLD);

			Paragraph titleParagraph = new Paragraph(centeredTitle, new Font(Font.FontFamily.COURIER, 25, Font.BOLD));
			titleParagraph.setAlignment(Element.ALIGN_CENTER);
			document.add(titleParagraph);

			Paragraph cinemaTitle = new Paragraph(cinema, fontBold);
			cinemaTitle.setAlignment(Element.ALIGN_LEFT);
			document.add(cinemaTitle);
			Paragraph cinemaParagraph = new Paragraph(address + line, font);
			cinemaParagraph.setAlignment(Element.ALIGN_LEFT);
			document.add(cinemaParagraph);

			Paragraph customerInfoTitle = new Paragraph(customerinfo, fontBold);
			customerInfoTitle.setAlignment(Element.ALIGN_LEFT);
			document.add(customerInfoTitle);
			Paragraph customerInfoParagraph = new Paragraph(name + phoneNumber + mail + line, font);
			customerInfoParagraph.setAlignment(Element.ALIGN_LEFT);
			document.add(customerInfoParagraph);

			Paragraph movieInfoTitle = new Paragraph(movie, new Font(Font.FontFamily.COURIER, 14, Font.BOLD));
			movieInfoTitle.setAlignment(Element.ALIGN_LEFT);
			document.add(movieInfoTitle);
			Paragraph movieParagraph = new Paragraph("Time: " + time + date + room + qs + seat + emp + line, font);
			movieParagraph.setAlignment(Element.ALIGN_LEFT);
			document.add(movieParagraph);

			PdfPTable billTable = new PdfPTable(new float[] { 0.75f, 0.25f });
			billTable.setWidthPercentage(100);
			List<PdfPCell> cellList = new ArrayList<PdfPCell>();

			cellList.add(new PdfPCell(new Phrase(ticketPrice, font)));
			cellList.add(new PdfPCell(new Phrase(ticketPriceValue, font)));
			int index = 0;
			for (String productName : productNameList) {
				cellList.add(new PdfPCell(new Phrase(productName, fontProduct)));
				cellList.add(new PdfPCell(new Phrase(lineTotalList.get(index), fontProduct)));
				index++;
			}
			cellList.add(new PdfPCell(new Phrase("Total:", fontBold)));
			cellList.add(new PdfPCell(new Phrase(df.format(order.getTotal()), fontBold)));

			boolean[] isOdd = new boolean[1];
			cellList.forEach(cell -> {
				if (!isOdd[0]) {
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				} else {
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				}
				cell.setBorder(Rectangle.NO_BORDER);
				billTable.addCell(cell);
				isOdd[0] = !isOdd[0];
			});

			billTable.setHorizontalAlignment(Element.ALIGN_CENTER);

			document.add(billTable);

			Paragraph trasactionParagraph = new Paragraph(transactionTime, font);
			trasactionParagraph.setAlignment(Element.ALIGN_RIGHT);
			document.add(trasactionParagraph);
			document.add(new Paragraph("\n"));

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Map<EncodeHintType, Object> hintMap = new HashMap<>();
			hintMap.put(EncodeHintType.MARGIN, 1);
			BitMatrix qrCodeMatrix = new MultiFormatWriter().encode(qrContent, BarcodeFormat.QR_CODE, 400, 400,
					hintMap);
			MatrixToImageWriter.writeToStream(qrCodeMatrix, "PNG", baos);
			Image qrCodeImage = Image.getInstance(baos.toByteArray());

			PdfPTable table = new PdfPTable(1);
			table.setWidthPercentage(50);
			PdfPCell cell = new PdfPCell(qrCodeImage, true);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			document.add(table);

			document.close();
			System.out.println("Ticket information and QR code have been written");
			try {
				if (Desktop.isDesktopSupported()) {
					File pdfFile = new File(defaultFolderPath + fileName);
					Desktop.getDesktop().open(pdfFile);
				} else {
					System.out.println("Desktop is not supported.");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (DocumentException | IOException | WriterException e) {
			e.printStackTrace();
		}
	}
}