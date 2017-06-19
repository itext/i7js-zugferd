package com.itextpdf.zugferd.pdfa;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

public class TaggedPdf {

    /** The resulting PDF. */
    public static final String DEST = "results/zugferd/pdfa/quickbrownfox2.pdf";
    /** An image resource. */
    public static final String FOX = "resources/images/fox.bmp";
    /** An image resource. */
    public static final String DOG = "resources/images/dog.bmp";

    /**
     * Creates a simple PDF with images and text.
     * @param args no arguments needed.
     * @throws IOException
     * @throws DocumentException 
     */
    static public void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TaggedPdf().createPdf(DEST);
    }
    
    /**
     * Creates a simple PDF with images and text
     * @param dest  the resulting PDF
     * @throws IOException
     * @throws MalformedURLException 
     * @throws FileNotFoundException 
     * @throws DocumentException 
     */
    public void createPdf(String dest) throws IOException {
    	PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
    	pdfDocument.setDefaultPageSize(PageSize.A4.rotate());
    	pdfDocument.setTagged();
    	Document document = new Document(pdfDocument);
        Paragraph p = new Paragraph().setFontSize(20)
        		.add(new Text("The quick brown "))
        		.add(new Image(ImageDataFactory.create(FOX)))
        		.add(new Text(" jumps over the lazy "))
				.add(new Image(ImageDataFactory.create(DOG)));
        document.add(p);
        document.close();
    }

}
