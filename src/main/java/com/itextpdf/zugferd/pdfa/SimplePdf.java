/*
 * Examples written in the context of a book about ZUGFeRD:
 * http://developers.itextpdf.com/content/zugferd-future-invoicing/2-creating-pdfa-files-itext 
 */
package com.itextpdf.zugferd.pdfa;

import java.io.File;
import java.io.IOException;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

/**
 * Creates a simple PDF with text and images.
 */
public class SimplePdf {

    /** The resulting PDF. */
    public static final String DEST = "results/zugferd/pdfa/quickbrownfox1.pdf";
    /** An image resource. */
    public static final String FOX = "resources/images/fox.bmp";
    /** An image resource. */
    public static final String DOG = "resources/images/dog.bmp";

    /**
     * The main method.
     *
     * @param args the arguments
     * @throws IOException Signals that an I/O exception has occurred.
     */
    static public void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new SimplePdf().createPdf(DEST);
    }

    /**
     * Creates the pdf.
     *
     * @param dest the dest
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void createPdf(String dest) throws IOException {
    	// step 1
    	PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
    	pdfDocument.setDefaultPageSize(PageSize.A4.rotate());
    	// step 2
    	Document document = new Document(pdfDocument);
    	// step 3
        document.add(
        	new Paragraph()
        		.setFontSize(20)
        		.add(new Text("The quick brown "))
        		.add(new Image(ImageDataFactory.create(FOX)))
        		.add(new Text(" jumps over the lazy "))
				.add(new Image(ImageDataFactory.create(DOG))));
        // step 4
        document.close();
    }

}
