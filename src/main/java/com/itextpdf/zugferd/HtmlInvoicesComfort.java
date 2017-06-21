/*
 * This example was written by Bruno Lowagie in the context of a book.
 * See http://developers.itextpdf.com/content/zugferd-future-invoicing/6-creating-html-invoices
 */
package com.itextpdf.zugferd;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.sql.SQLException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.SAXException;

import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.zugferd.data.InvoiceData;
import com.itextpdf.zugferd.exceptions.DataIncompleteException;
import com.itextpdf.zugferd.exceptions.InvalidCodeException;
import com.itextpdf.zugferd.pojo.Invoice;
import com.itextpdf.zugferd.pojo.PojoFactory;
import com.itextpdf.zugferd.profiles.IComfortProfile;

/**
 * Creates invoices in the HTML format
 *
 * @author Bruno Lowagie
 */
public class HtmlInvoicesComfort {
    
    /** The Constant DEST. */
    public static final String DEST = "results/zugferd/html/comfort%05d.html";
    
    /** The Constant XSL. */
    public static final String XSL = "resources/zugferd/invoice.xsl";
    
    /** The Constant CSS. */
    public static final String CSS = "resources/zugferd/invoice.css";
    
    /** The Constant LOGO. */
    public static final String LOGO = "resources/zugferd/logo.png";
    
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
        File css = new File(CSS);
        copyFile(css, new File(file.getParentFile(), css.getName()));
        File logo = new File(LOGO);
        copyFile(logo, new File(file.getParentFile(), logo.getName()));
        HtmlInvoicesComfort app = new HtmlInvoicesComfort();
        PojoFactory factory = PojoFactory.getInstance();
        List<Invoice> invoices = factory.getInvoices();
        for (Invoice invoice : invoices) {
            app.createHtml(invoice, new FileWriter(String.format(DEST, invoice.getId())));
        }
        factory.close();
    }
    
    /**
     * Creates the html.
     *
     * @param invoice the invoice
     * @param writer the writer
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ParserConfigurationException the parser configuration exception
     * @throws SAXException the SAX exception
     * @throws DataIncompleteException the data incomplete exception
     * @throws InvalidCodeException the invalid code exception
     * @throws TransformerException the transformer exception
     */
    public void createHtml(Invoice invoice, Writer writer)
    	throws IOException, ParserConfigurationException, SAXException,
    	DataIncompleteException, InvalidCodeException, TransformerException {
        IComfortProfile comfort = new InvoiceData().createComfortProfileData(invoice);
        InvoiceDOM dom = new InvoiceDOM(comfort);
        StreamSource xml = new StreamSource(new ByteArrayInputStream(dom.toXML()));
        StreamSource xsl = new StreamSource(new File(XSL));
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xsl);
        transformer.transform(xml, new StreamResult(writer));
        writer.flush();
        writer.close();
    }
    
    /**
     * Copies a file.
     *
     * @param source the source
     * @param dest the dest
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void copyFile(File source, File dest) throws IOException {
        InputStream input = new FileInputStream(source);
        OutputStream output = new FileOutputStream(dest);
        byte[] buf = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buf)) > 0) {
            output.write(buf, 0, bytesRead);
        }
        input.close();
        output.close();
    }
}
