/*
 * Part of a set of classes based on a sample database.
 * This example was written by Bruno Lowagie in the context of a book.
 * See http://developers.itextpdf.com/content/zugferd-future-invoicing/3-simple-invoice-database
 */
package com.itextpdf.zugferd.pojo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Factory that creates Invoice, Customer, Product, and Item classes.
 * @author Bruno Lowagie (iText Software)
 */
public class PojoFactory {
    
    /** Instance of this PojoFactory that will be reused. */
    protected static PojoFactory factory = null;
    
    /** The connection to the HSQLDB database. */
    protected Connection connection;
    
    /** The customer cache. */
    protected HashMap<Integer, Customer> customerCache = new HashMap<Integer, Customer>();
    
    /** The product cache. */
    protected HashMap<Integer, Product> productCache = new HashMap<Integer, Product>();
    
    /** Prepared statement to get customer data. */
    protected PreparedStatement getCustomer;

    /** Prepared statement to get product data. */
    protected PreparedStatement getProduct;

    /** Prepared statement to get items. */
    protected PreparedStatement getItems;
    
    /**
     * Instantiates a new POJO factory.
     *
     * @throws ClassNotFoundException the class not found exception
     * @throws SQLException the SQL exception
     */
    private PojoFactory() throws ClassNotFoundException, SQLException {
        Class.forName("org.hsqldb.jdbcDriver");
        connection = DriverManager.getConnection(
            "jdbc:hsqldb:resources/db/invoices", "SA", "");
        getCustomer = connection.prepareStatement("SELECT * FROM Customer WHERE id = ?");
        getProduct = connection.prepareStatement("SELECT * FROM Product WHERE id = ?");
        getItems = connection.prepareStatement("SELECT * FROM Item WHERE invoiceid = ?");
    }
    
    /**
     * Gets the single instance of PojoFactory.
     *
     * @return single instance of PojoFactory
     * @throws SQLException the SQL exception
     */
    public static PojoFactory getInstance() throws SQLException {
        if (factory == null || factory.connection.isClosed()) {
            try {
                factory = new PojoFactory();
            } catch (ClassNotFoundException cnfe) {
                throw new SQLException(cnfe.getMessage());
            }
        }
        return factory;
    }
    
    /**
     * Close the database connection.
     *
     * @throws SQLException the SQL exception
     */
    public void close() throws SQLException {
        connection.close();
    }
    
    /**
     * Gets all the {@link Invoice} objects stored in the database.
     *
     * @return the invoices
     * @throws SQLException the SQL exception
     */
    public List<Invoice> getInvoices() throws SQLException {
        List<Invoice> invoices = new ArrayList<Invoice>();
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery("SELECT * FROM Invoice");
        while (rs.next()) {
            invoices.add(getInvoice(rs));
        }
        stm.close();
        return invoices;
    }
    
    /**
     * Creates an {@link Invoice} object from a database result set.
     *
     * @param rs the result set
     * @return the invoice object
     * @throws SQLException the SQL exception
     */
    public Invoice getInvoice(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setId(rs.getInt("id"));
        invoice.setCustomer(getCustomer(rs.getInt("customerid")));
        List<Item> items = getItems(rs.getInt("id"));
        invoice.setItems(items);
        double total = 0;
        for (Item item : items)
            total += item.getCost();
        invoice.setTotal(total);
        invoice.setInvoiceDate(rs.getDate("invoicedate"));
        return invoice;
    }
    
    /**
     * Creates an {@link Item} object from a database result set.
     *
     * @param rs the result set
     * @return the item object
     * @throws SQLException the SQL exception
     */
    public Item getItem(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setItem(rs.getInt("Item"));
        Product product = getProduct(rs.getInt("ProductId"));
        item.setProduct(product);
        item.setQuantity(rs.getInt("Quantity"));
        item.setCost(item.getQuantity() * product.getPrice());
        return item;
    }
    
    /**
     * Gets a {@link Customer} object, given a customer id.
     *
     * @param id the customer id
     * @return the customer object
     * @throws SQLException the SQL exception
     */
    public Customer getCustomer(int id) throws SQLException {
        if (customerCache.containsKey(id))
            return customerCache.get(id);
        getCustomer.setInt(1, id);
        ResultSet rs = getCustomer.executeQuery();
        if (rs.next()) {
            Customer customer = new Customer();
            customer.setId(id);
            customer.setFirstName(rs.getString("FirstName"));
            customer.setLastName(rs.getString("LastName"));
            customer.setStreet(rs.getString("Street"));
            customer.setPostalcode(rs.getString("Postalcode"));
            customer.setCity(rs.getString("City"));
            customer.setCountryId(rs.getString("CountryID"));
            customerCache.put(id, customer);
            return customer;
        }
        return null;
    }
    
    /**
     * Gets a {@link Product} object, given a product id.
     *
     * @param id the product id
     * @return the product object
     * @throws SQLException the SQL exception
     */
    public Product getProduct(int id) throws SQLException {
        if (productCache.containsKey(id))
            return productCache.get(id);
        getProduct.setInt(1, id);
        ResultSet rs = getProduct.executeQuery();
        if (rs.next()) {
            Product product = new Product();
            product.setId(id);
            product.setName(rs.getString("Name"));
            product.setPrice(rs.getDouble("Price"));
            product.setVat(rs.getDouble("Vat"));
            productCache.put(id, product);
            return product;
        }
        return null;
    }
    
    /**
     * Gets a list of {@link Item} objects for a specific invoice.
     *
     * @param invoiceid the invoice id
     * @return the items
     * @throws SQLException the SQL exception
     */
    public List<Item> getItems(int invoiceid) throws SQLException {
        List<Item> items = new ArrayList<Item>();
        getItems.setInt(1, invoiceid);
        ResultSet rs = getItems.executeQuery();
        while (rs.next()) {
            items.add(getItem(rs));
        }
        return items;
    }
}
