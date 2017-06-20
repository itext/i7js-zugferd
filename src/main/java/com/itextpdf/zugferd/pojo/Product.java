/*
 * Part of a set of classes based on a sample database.
 * This example was written by Bruno Lowagie in the context of a book.
 * See http://developers.itextpdf.com/content/zugferd-future-invoicing/3-simple-invoice-database
 */
package com.itextpdf.zugferd.pojo;

/**
 * Plain Old Java Object containing info about a Product.
 * @author Bruno Lowagie (iText Software)
 */
public class Product {
    
    /** The id. */
    protected int id;
    
    /** The name. */
    protected String name;
    
    /** The price. */
    protected double price;
    
    /** The vat. */
    protected double vat;

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
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the price.
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price.
     *
     * @param price the new price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the vat.
     *
     * @return the vat
     */
    public double getVat() {
        return vat;
    }

    /**
     * Sets the vat.
     *
     * @param vat the new vat
     */
    public void setVat(double vat) {
        this.vat = vat;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t(").append(id).append(")\t").append(name).append("\t").append(price).append("\u20ac\tvat ").append(vat).append("%");
        return sb.toString();
    }
}
