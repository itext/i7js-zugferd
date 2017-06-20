/*
 * This example was written by Bruno Lowagie in the context of a book.
 * See http://developers.itextpdf.com/content/zugferd-future-invoicing/3-simple-invoice-database
 */
package com.itextpdf.zugferd;

import java.sql.SQLException;
import java.util.List;

import com.itextpdf.zugferd.pojo.Invoice;
import com.itextpdf.zugferd.pojo.PojoFactory;

/**
 * A simple example to test the database.
 */
public class DatabaseTest {
    
    /**
     * The main method.
     *
     * @param args the arguments
     * @throws SQLException the SQL exception
     */
    public static void main(String[] args) throws SQLException {
        PojoFactory factory = PojoFactory.getInstance();
        List<Invoice> invoices = factory.getInvoices();
        for (Invoice invoice : invoices)
            System.out.println(invoice.toString());
        factory.close();
    }
}
