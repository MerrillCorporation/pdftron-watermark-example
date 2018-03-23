package com.pdftron.watermarking.demo;

import com.pdftron.common.PDFNetException;
import com.pdftron.pdf.ColorPt;
import com.pdftron.pdf.Font;
import com.pdftron.pdf.PDFDoc;
import com.pdftron.pdf.PDFNet;
import com.pdftron.pdf.PageSet;
import com.pdftron.pdf.Stamper;
import com.pdftron.sdf.SDFDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.pdftron.pdf.Stamper.e_horizontal_center;
import static com.pdftron.pdf.Stamper.e_vertical_center;

@Service
public class WatermarkService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WatermarkService.class);

    private DownloadService downloadService;

    public WatermarkService(DownloadService downloadService) {
        this.downloadService = downloadService;
        PDFNet.initialize();
    }

    public void watermarkFile(OutputStream output, String fileName) {
        LOGGER.info("sourceFileSize={}", downloadService.getFileSize(fileName));
        InputStream unwatermarkedPdfStream = downloadService.getFile(fileName);
        PDFDoc pdfDoc = makePdfDoc(unwatermarkedPdfStream);
        Stamper stamper = makeStamper(pdfDoc);
        PageSet pageSet = makePageSet(pdfDoc);
        stamper.stampText(pdfDoc, "English | 日本語 | 中文 | ", pageSet);

        save(pdfDoc, output);
    }
    
    private PDFDoc makePdfDoc(InputStream stream) {
        try {
            PDFDoc doc = new PDFDoc(stream);
            return doc;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load source file", e);
        } 
    }
    
    private Stamper makeStamper(PDFDoc doc) {
        Stamper stamper = new Stamper(Stamper.e_absolute_size, 0, 0);
        stamper.setOpacity(0.18);
        stamper.setRotation(-50);
        stamper.setAsBackground(false);
        stamper.showsOnPrint(true);
        stamper.setAlignment(e_horizontal_center, e_vertical_center);
        stamper.setSize(Stamper.e_font_size, 24, -1);
        stamper.setPosition(50, 50);

        
        stamper.setFont(makeFont(doc, "arialunicode.ttf"));

        ColorPt textFontColor = makeColor();
        stamper.setFontColor(textFontColor);
        stamper.setAsAnnotation(true);

        return stamper;
    }
    
    private Font makeFont(PDFDoc doc, String fontFileName) {
        InputStream fontStream = getFontAsStream("arialunicode.ttf");
        try {
            return Font.createCIDTrueTypeFont(doc, fontStream, true, true);
        } catch (PDFNetException e) {
            throw new RuntimeException("Failed to create font", e);
        }
    }
    
    private InputStream getFontAsStream(String fontFileName) {
        LOGGER.info("fontFileSize={}", downloadService.getFileSize(fontFileName));
        return downloadService.getFile(fontFileName);
    }
    
    private ColorPt makeColor() {
        try {
            ColorPt textFontColor = new ColorPt();
            textFontColor.setColorantNum(53565);
            return textFontColor;
        } catch (PDFNetException e) {
            throw new RuntimeException("failed to make color", e);
        }
    }

    private PageSet makePageSet(PDFDoc pdfDoc) {
        try {
            return new PageSet(1, pdfDoc.getPageCount(), PageSet.e_all);
        } catch (PDFNetException e) {
            throw new RuntimeException("Failed to make pageset", e);
        }
    }

    private void save(PDFDoc pdfDoc, OutputStream output) {
        try {
            pdfDoc.save(output, SDFDoc.e_linearized, null);
        } catch (PDFNetException | IOException e) {
            throw new RuntimeException("Failed to save pdf", e);
        }
    }
}
