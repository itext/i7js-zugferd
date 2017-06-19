package com.itextpdf.zugferd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfOutputIntent;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.zugferd.data.InvoiceData;
import com.itextpdf.zugferd.exceptions.DataIncompleteException;
import com.itextpdf.zugferd.exceptions.InvalidCodeException;
import com.itextpdf.zugferd.pojo.Invoice;
import com.itextpdf.zugferd.pojo.Item;
import com.itextpdf.zugferd.pojo.PojoFactory;
import com.itextpdf.zugferd.pojo.Product;
import com.itextpdf.zugferd.profiles.IBasicProfile;

/**
 * Reads invoice data from a test database and creates ZUGFeRD invoices
 * (Basic profile).
 * @author Bruno Lowagie
 */
public class PdfInvoicesBasic {
    public static final String DEST = "results/zugferd/pdf/basic%05d.pdf";
    public static final String ICC = "resources/color/sRGB_CS_profile.icm";
    public static final String REGULAR = "resources/fonts/OpenSans-Regular.ttf";
    public static final String BOLD = "resources/fonts/OpenSans-Bold.ttf";
    public static final String INTENT = "resources/zugferd/sRGB_CS_profile.icm";
    public static final String NEWLINE = "\n";
    
    public static void main(String[] args) throws IOException, ParserConfigurationException, SQLException, SAXException, TransformerException, ParseException, DataIncompleteException, InvalidCodeException {
    	LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-html2pdf_typography.xml");
    	File file = new File(DEST);
        file.getParentFile().mkdirs();
        PdfInvoicesBasic app = new PdfInvoicesBasic();
        PojoFactory factory = PojoFactory.getInstance();
        List<Invoice> invoices = factory.getInvoices();
        for (Invoice invoice : invoices) {
            app.createPdf(invoice);
        }
        factory.close();
    }
    
    public PdfInvoicesBasic() throws IOException {
    }
    
    public void createPdf(Invoice invoice) throws ParserConfigurationException, SAXException, TransformerException, IOException, ParseException, DataIncompleteException, InvalidCodeException {
        String dest = String.format(DEST, invoice.getId());
        InvoiceData invoiceData = new InvoiceData();
        IBasicProfile basic = invoiceData.createBasicProfileData(invoice);
        InvoiceDOM dom = new InvoiceDOM(basic);
        

    	ZugferdDocument pdfDocument = new ZugferdDocument(
    			new PdfWriter(dest), ZugferdConformanceLevel.ZUGFeRDBasic,
    			new PdfOutputIntent("Custom", "", "http://www.color.org",
        	            "sRGB IEC61966-2.1", new FileInputStream(ICC)));
        pdfDocument.addFileAttachment(
        		"ZUGFeRD invoice", dom.toXML(), "ZUGFeRD-invoice.xml",
        		PdfName.ApplicationXml, new PdfDictionary(), PdfName.Alternative);
    	Document document = new Document(pdfDocument)
    			.setFont(PdfFontFactory.createFont(REGULAR, true))
    			.setFontSize(12);
    	
    	// Alternative font
    	PdfFont bold = PdfFontFactory.createFont(BOLD, true);
    	
        // header
    	document.add(
    		new Paragraph(String.format("%s %s", basic.getName(), basic.getId()))
    			.setFontSize(14)
    			.setTextAlignment(TextAlignment.RIGHT));
        document.add(
        	new Paragraph(convertDate(basic.getDateTime(), "MMM dd, yyyy"))
    			.setTextAlignment(TextAlignment.RIGHT));
        
        // Address seller / buyer
        Table table = new Table(new UnitValue[]{
        		new UnitValue(UnitValue.PERCENT, 50),
        		new UnitValue(UnitValue.PERCENT, 50)})
        		.setWidthPercent(100);
        table.addCell(getPartyAddress("From:",
                basic.getSellerName(),
                basic.getSellerLineOne(),
                basic.getSellerLineTwo(),
                basic.getSellerCountryID(),
                basic.getSellerPostcode(),
                basic.getSellerCityName()));
        table.addCell(getPartyAddress("To:",
                basic.getBuyerName(),
                basic.getBuyerLineOne(),
                basic.getBuyerLineTwo(),
                basic.getBuyerCountryID(),
                basic.getBuyerPostcode(),
                basic.getBuyerCityName()));
        table.addCell(getPartyTax(basic.getSellerTaxRegistrationID(),
                basic.getSellerTaxRegistrationSchemeID(), bold));
        table.addCell(getPartyTax(basic.getBuyerTaxRegistrationID(),
                basic.getBuyerTaxRegistrationSchemeID(), bold));
        document.add(table);
        
        // line items
        table = new Table(new UnitValue[]{
        		new UnitValue(UnitValue.PERCENT, 43.75f),
        		new UnitValue(UnitValue.PERCENT, 12.5f),
        		new UnitValue(UnitValue.PERCENT, 6.25f),
        		new UnitValue(UnitValue.PERCENT, 12.5f),
        		new UnitValue(UnitValue.PERCENT, 12.5f),
        		new UnitValue(UnitValue.PERCENT, 12.5f)})
        		.setWidthPercent(100)
				.setMarginTop(10).setMarginBottom(10);
        table.addHeaderCell(new Cell()
        		.add("Item:").setFont(bold));
        table.addHeaderCell(new Cell()
        		.add("Price:").setFont(bold));
        table.addHeaderCell(new Cell()
        		.add("Qty:").setFont(bold));
        table.addHeaderCell(new Cell()
        		.add("Subtotal:").setFont(bold));
        table.addHeaderCell(new Cell()
        		.add("VAT:").setFont(bold));
        table.addHeaderCell(new Cell()
        		.add("Total:").setFont(bold));

        Product product;
        for (Item item : invoice.getItems()) {
            product = item.getProduct();
            table.addCell(new Cell()
                	.add(product.getName()));
            table.addCell(new Cell()
                	.add(InvoiceData.format2dec(InvoiceData.round(product.getPrice())))
                	.setTextAlignment(TextAlignment.RIGHT));
            table.addCell(new Cell()
                	.add(String.valueOf(item.getQuantity()))
                	.setTextAlignment(TextAlignment.RIGHT));
            table.addCell(new Cell()
                	.add(InvoiceData.format2dec(InvoiceData.round(item.getCost())))
                	.setTextAlignment(TextAlignment.RIGHT));
            table.addCell(new Cell()
                	.add(InvoiceData.format2dec(InvoiceData.round(product.getVat())))
                	.setTextAlignment(TextAlignment.RIGHT));
            table.addCell(new Cell()
                	.add( InvoiceData.format2dec(InvoiceData.round(item.getCost() + ((item.getCost() * product.getVat()) / 100))))
                	.setTextAlignment(TextAlignment.RIGHT));
        }
        document.add(table);

        // grand totals
        document.add(getTotalsTable(
                basic.getTaxBasisTotalAmount(), basic.getTaxTotalAmount(), basic.getGrandTotalAmount(), basic.getGrandTotalAmountCurrencyID(),
                basic.getTaxTypeCode(), basic.getTaxApplicablePercent(),
                basic.getTaxBasisAmount(), basic.getTaxCalculatedAmount(), basic.getTaxCalculatedAmountCurrencyID(), bold));
        

        // payment info
        document.add(getPaymentInfo(basic.getPaymentReference(), basic.getPaymentMeansPayeeFinancialInstitutionBIC(), basic.getPaymentMeansPayeeAccountIBAN()));
        
        document.close();
    }
    /*
    
    
    public Paragraph getPaymentInfo(String ref, String[] bic, String[] iban) {
        Paragraph p = new Paragraph(String.format(
                "Please wire the amount due to our bank account using the following reference: %s",
                ref), font12);
        int n = bic.length;
        for (int i = 0; i < n; i++) {
            p.add(Chunk.NEWLINE);
            p.add(String.format("BIC: %s - IBAN: %s", bic[i], iban[i]));
        }
        return p;
    }*/

    public String convertDate(Date d, String newFormat) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(newFormat);
        return sdf.format(d);
    }
    
    public Cell getPartyAddress(String who, String name, String line1, String line2, String countryID, String postcode, String city) {
        Cell cell = new Cell()
        		.setBorder(Border.NO_BORDER)
        		.add(who).add(name).add(line1).add(line2)
        		.add(String.format("%s-%s %s", countryID, postcode, city));
        return cell;
    }
    
    public Cell getPartyTax(String[] taxId, String[] taxSchema, PdfFont bold) {
        Cell cell = new Cell().setBorder(Border.NO_BORDER).setFontSize(10)
        .add(new Paragraph("Tax ID(s):").setFont(bold));
        if (taxId.length == 0) {
            cell.add("Not applicable");
        }
        else {
            int n = taxId.length;
            for (int i = 0; i < n; i++) {
                cell.add(String.format("%s: %s", taxSchema[i], taxId[i]));
            }
        }
        return cell;
    }
    
    public Table getTotalsTable(String tBase, String tTax, String tTotal, String tCurrency,
            String[] type, String[] percentage, String base[], String tax[], String currency[],
            PdfFont bold) {
        Table table = new Table(new UnitValue[]{
        		new UnitValue(UnitValue.PERCENT, 8.33f),
        		new UnitValue(UnitValue.PERCENT, 8.33f),
        		new UnitValue(UnitValue.PERCENT, 25f),
        		new UnitValue(UnitValue.PERCENT, 25f),
        		new UnitValue(UnitValue.PERCENT, 25f),
        		new UnitValue(UnitValue.PERCENT, 8.34f)})
        	.setWidthPercent(100);
        table.addCell(new Cell().add("TAX:").setFont(bold));
        table.addCell(new Cell().add("%").setFont(bold).setTextAlignment(TextAlignment.RIGHT));
        table.addCell(new Cell().add("Base amount:").setFont(bold));
        table.addCell(new Cell().add("Tax amount:").setFont(bold));
        table.addCell(new Cell().add("Total:").setFont(bold));
        table.addCell(new Cell().add("Curr.:").setFont(bold));
        int n = type.length;
        for (int i = 0; i < n; i++) {
            table.addCell(new Cell().add(type[i]).setTextAlignment(TextAlignment.RIGHT));
            table.addCell(new Cell().add(percentage[i]).setTextAlignment(TextAlignment.RIGHT));
            table.addCell(new Cell().add(base[i]).setTextAlignment(TextAlignment.RIGHT));
            table.addCell(new Cell().add(tax[i]).setTextAlignment(TextAlignment.RIGHT));
            double total = Double.parseDouble(base[i]) + Double.parseDouble(tax[i]);
            table.addCell(new Cell().add(InvoiceData.format2dec(InvoiceData.round(total)))
            		.setTextAlignment(TextAlignment.RIGHT));
            table.addCell(new Cell().add(currency[i]));
        }
        table.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(tBase).setTextAlignment(TextAlignment.RIGHT));
        table.addCell(new Cell().add(tTax).setTextAlignment(TextAlignment.RIGHT));
        table.addCell(new Cell().add(tTotal).setTextAlignment(TextAlignment.RIGHT));
        table.addCell(new Cell().add(tCurrency));
        return table;
    }
    
    public Paragraph getPaymentInfo(String ref, String[] bic, String[] iban) {
        Paragraph p = new Paragraph(String.format(
                "Please wire the amount due to our bank account using the following reference: %s",
                ref));
        int n = bic.length;
        for (int i = 0; i < n; i++) {
            p.add(String.format("BIC: %s - IBAN: %s", bic[i], iban[i]));
        }
        return p;
    }
}
