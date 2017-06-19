package com.itextpdf.zugferd.pdfa;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfAConformanceLevel;
import com.itextpdf.kernel.pdf.PdfOutputIntent;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.pdfa.PdfADocument;

public class PdfA3a {

    /** The resulting PDF. */
    public static final String DEST = "results/zugferd/pdfa/quickbrownfox4.pdf";
    /** An image resource. */
    public static final String FOX = "resources/images/fox.bmp";
    /** An image resource. */
    public static final String DOG = "resources/images/dog.bmp";
    /** A path to a color profile. */
    public static final String ICC = "resources/color/sRGB_CS_profile.icm";
    /** A font that will be embedded. */
    public static final String FONT = "resources/fonts/OpenSans-Regular.ttf";

    /**
     * Creates a simple PDF with images and text.
     * @param args no arguments needed.
     * @throws IOException
     * @throws DocumentException 
     */
    static public void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PdfA3a().createPdf(DEST);
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
    	PdfADocument pdfDocument = new PdfADocument(
    			new PdfWriter(dest), PdfAConformanceLevel.PDF_A_3B,
    			new PdfOutputIntent("Custom", "", "http://www.color.org",
        	            "sRGB IEC61966-2.1", new FileInputStream(ICC)));
    	pdfDocument.setDefaultPageSize(PageSize.A4.rotate());
    	pdfDocument.setTagged();
    	Document document = new Document(pdfDocument);
    	PdfFont font = PdfFontFactory.createFont(FONT, true);
        Paragraph p = new Paragraph().setFont(font).setFontSize(20)
        		.add(new Text("The quick brown "))
        		.add(new Image(ImageDataFactory.create(FOX)))
        		.add(new Text(" jumps over the lazy "))
				.add(new Image(ImageDataFactory.create(DOG)));
        document.add(p);
        document.close();
    }

}
