/*
 * This example was written by Bruno Lowagie in the context of a book.
 * See http://developers.itextpdf.com/content/zugferd-future-invoicing/7-creating-pdf-invoices-comfort
 */
package com.itextpdf.zugferd;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import org.xml.sax.SAXException;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfOutputIntent;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.zugferd.data.InvoiceData;
import com.itextpdf.zugferd.exceptions.DataIncompleteException;
import com.itextpdf.zugferd.exceptions.InvalidCodeException;
import com.itextpdf.zugferd.pojo.Invoice;
import com.itextpdf.zugferd.pojo.PojoFactory;
import com.itextpdf.zugferd.profiles.IComfortProfile;

/**
 * Creates ZUGFeRD invoices using the Comfort profile.
 * 
 * @author Bruno Lowagie
 */
public class PdfInvoicesComfort {
	
    /** The pattern for the destination files. */
    public static final String DEST = "results/zugferd/pdf/comfort%05d.pdf";
    
    /** The path to the XSL file. */
    public static final String XSL = "resources/zugferd/invoice.xsl";
    
    /** The path to the output intent file. */
    public static final String INTENT = "resources/color/sRGB_CS_profile.icm";
    
    /** The converter properties. */
    public static ConverterProperties properties;
    
    /**
     * The main method.
     *
     * @param args the arguments
     * @throws SQLException the SQL exception
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ParserConfigurationException the parser configuration exception
     * @throws SAXException the SAX exception
     * @throws DataIncompleteException the data incomplete exception
     * @throws InvalidCodeException the invalid code exception
     * @throws TransformerException the transformer exception
     */
    public static void main(String[] args)
    	throws SQLException, IOException,
    	ParserConfigurationException, SAXException, TransformerException,
    	DataIncompleteException, InvalidCodeException {
        LicenseKey.loadLicenseFile(
        	System.getenv("ITEXT7_LICENSEKEY")
        	+ "/itextkey-html2pdf_typography.xml");
       	File file = new File(DEST);
        file.getParentFile().mkdirs();
        PdfInvoicesComfort app = new PdfInvoicesComfort();
        PojoFactory factory = PojoFactory.getInstance();
        List<Invoice> invoices = factory.getInvoices();
        for (Invoice invoice : invoices) {
        	app.createPdf(invoice, new FileOutputStream(String.format(DEST, invoice.getId())));
        }
        factory.close();
    }
    
    /**
     * Creates a PDF invoice.
     *
     * @param invoice the invoice
     * @param fos the fos
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ParserConfigurationException the parser configuration exception
     * @throws SAXException the SAX exception
     * @throws DataIncompleteException the data incomplete exception
     * @throws InvalidCodeException the invalid code exception
     * @throws TransformerException the transformer exception
     */
    public void createPdf(Invoice invoice, FileOutputStream fos)
    	throws IOException, ParserConfigurationException,
    	SAXException, TransformerException,
    	DataIncompleteException, InvalidCodeException {
        IComfortProfile comfort =
        	new InvoiceData().createComfortProfileData(invoice);
        InvoiceDOM dom = new InvoiceDOM(comfort);
        
        StreamSource xml = new StreamSource(
        		new ByteArrayInputStream(dom.toXML()));
        StreamSource xsl = new StreamSource(
        		new File(XSL));
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xsl);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer htmlWriter = new OutputStreamWriter(baos);
        transformer.transform(xml, new StreamResult(htmlWriter));
        htmlWriter.flush();
        htmlWriter.close();
        byte[] html = baos.toByteArray();
        
        ZugferdDocument pdfDocument = new ZugferdDocument(
        	new PdfWriter(fos), ZugferdConformanceLevel.ZUGFeRDComfort,
        	new PdfOutputIntent("Custom", "", "http://www.color.org",
        	    "sRGB IEC61966-2.1", new FileInputStream(INTENT)));
        pdfDocument.addFileAttachment("ZUGFeRD invoice", PdfFileSpec.createEmbeddedFileSpec(pdfDocument,
                dom.toXML(), "ZUGFeRD invoice", "ZUGFeRD-invoice.xml",
        		PdfName.ApplicationXml, new PdfDictionary(), PdfName.Alternative));
        pdfDocument.setTagged();
        HtmlConverter.convertToPdf(
        		new ByteArrayInputStream(html), pdfDocument, getProperties());
    }
    
    /**
     * Gets the converter properties.
     *
     * @return the properties
     */
    public ConverterProperties getProperties() {
    	if (properties == null) {
		    properties = new ConverterProperties()
				.setBaseUri("resources/zugferd/");
    	}
    	return properties;
    }
}
