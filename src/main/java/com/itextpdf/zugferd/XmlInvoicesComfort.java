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
 * @author  Bruno Lowagie
 */
public class XmlInvoicesComfort {
    public static final String DEST = "results/zugferd/xml/comfort%05d.xml";
    
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
