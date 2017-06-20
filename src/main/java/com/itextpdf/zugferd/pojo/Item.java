/*
 * Part of a set of classes based on a sample database.
 * This example was written by Bruno Lowagie in the context of a book.
 * See http://developers.itextpdf.com/content/zugferd-future-invoicing/3-simple-invoice-database
 */
package com.itextpdf.zugferd.pojo;

/**
 * Plain Old Java Object containing info about an Item.
 * @author Bruno Lowagie (iText Software)
 */
public class Item {
    
    /** The item. */
    protected int item;
    
    /** The product. */
    protected Product product;
    
    /** The quantity. */
    protected int quantity;
    
    /** The cost. */
    protected double cost;

    /**
     * Gets the item.
     *
     * @return the item
     */
    public int getItem() {
        return item;
    }

    /**
     * Sets the item.
     *
     * @param item the new item
     */
    public void setItem(int item) {
        this.item = item;
    }

    /**
     * Gets the product.
     *
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Sets the product.
     *
     * @param product the new product
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * Gets the quantity.
     *
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity.
     *
     * @param quantity the new quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the cost.
     *
     * @return the cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * Sets the cost.
     *
     * @param cost the new cost
     */
    public void setCost(double cost) {
        this.cost = cost;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  #").append(item);
        sb.append(product.toString());
        sb.append("\tQuantity: ").append(quantity);
        sb.append("\tCost: ").append(cost).append("\u20ac");
        return sb.toString();
    }
}
