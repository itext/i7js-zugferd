/*
 * Part of a set of classes based on a sample database.
 * This example was written by Bruno Lowagie in the context of a book.
 * See http://developers.itextpdf.com/content/zugferd-future-invoicing/3-simple-invoice-database
 */
package com.itextpdf.zugferd.pojo;

import java.util.Date;
import java.util.List;

/**
 * Plain Old Java Object containing info about an Invoice.
 * @author Bruno Lowagie (iText Software)
 */
public class Invoice {
    
    /** The id. */
    protected int id;
    
    /** The customer. */
    protected Customer customer;
    
    /** The total. */
    protected double total;
    
    /** The items. */
    protected List<Item> items;
    
    /** The invoice date. */
    protected Date invoiceDate;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the customer.
     *
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Sets the customer.
     *
     * @param customer the new customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Gets the total.
     *
     * @return the total
     */
    public double getTotal() {
        return total;
    }

    /**
     * Sets the total.
     *
     * @param total the new total
     */
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * Gets the items.
     *
     * @return the items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Sets the items.
     *
     * @param items the new items
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * Gets the invoice date.
     *
     * @return the invoice date
     */
    public Date getInvoiceDate() {
        return invoiceDate;
    }

    /**
     * Sets the invoice date.
     *
     * @param invoiceDate the new invoice date
     */
    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Invoice id: ").append(id).append(" Date: ").append(invoiceDate).append(" Total cost: ").append(total).append("\u20ac\n");
        sb.append("Customer: ").append(customer.toString()).append("\n");
        for (Item item : items) {
            sb.append(item.toString()).append("\n");
        }
        return sb.toString();
    }
}
