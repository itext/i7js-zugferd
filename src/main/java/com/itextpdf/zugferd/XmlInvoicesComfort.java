/*
 * This example was written by Bruno Lowagie in the context of a book.
 * See http://developers.itextpdf.com/content/zugferd-future-invoicing/4-creating-xml-invoices-itext
 */
package com.itextpdf.zugferd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.zugferd.data.InvoiceData;
import com.itextpdf.zugferd.exceptions.DataIncompleteException;
import com.itextpdf.zugferd.exceptions.InvalidCodeException;
import com.itextpdf.zugferd.pojo.Invoice;
import com.itextpdf.zugferd.pojo.PojoFactory;
import com.itextpdf.zugferd.profiles.IBasicProfile;

/**
 * Creates a set of XML files that represent invoices.
 *
 * @author  Bruno Lowagie
 */
public class XmlInvoicesComfort {
    
    /** The Constant DEST. */
    public static final String DEST = "results/zugferd/xml/comfort%05d.xml";
    
    /**
     * The main method.
     *
     * @param args the arguments
     * @throws SQLException the SQL exception
     * @throws ParserConfigurationException the parser configuration exception
     * @throws SAXException the SAX exception
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws TransformerException the transformer exception
     * @throws DataIncompleteException the data incomplete exception
     * @throws InvalidCodeException the invalid code exception
     */
    public static void main(String[] args) throws SQLException, ParserConfigurationException, SAXException, IOException, TransformerException, DataIncompleteException, InvalidCodeException {
    	LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-html2pdf_typography.xml");
    	File file = new File(DEST);
        file.getParentFile().mkdirs();
        PojoFactory factory = PojoFactory.getInstance();
        List<Invoice> invoices = factory.getInvoices();
        InvoiceData invoiceData = new InvoiceData();
        IBasicProfile comfort;
        InvoiceDOM dom;
        for (Invoice invoice : invoices) {
            comfort = invoiceData.createComfortProfileData(invoice);
            dom = new InvoiceDOM(comfort);
            byte[] xml = dom.toXML();
            FileOutputStream fos = new FileOutputStream(String.format(DEST, invoice.getId()));
            fos.write(xml);
            fos.flush();
            fos.close();
        }
        factory.close();
    }

}
