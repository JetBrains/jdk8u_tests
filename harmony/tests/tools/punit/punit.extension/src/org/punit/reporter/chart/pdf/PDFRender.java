/* (C) Copyright 2007, by Andrew Zhang */

package org.punit.reporter.chart.pdf;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.FileOutputStream;

import org.jfree.chart.JFreeChart;
import org.punit.exception.IOException;
import org.punit.message.Messages;
import org.punit.reporter.chart.ChartRender;
import org.punit.runner.Runner;
import org.punit.util.ReporterUtil;

import com.lowagie.text.Anchor;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ImgTemplate;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public class PDFRender implements ChartRender {

    private static final long serialVersionUID = 4028654555382925636L;

    private int _height;

    private int _width;
   
    private transient Document _document;

    private transient PdfWriter _writer;
    

    public PDFRender() {
        this(PDFConstants.DEFAULT_HEIGHT, PDFConstants.DEFAULT_WIDTH);
    }
    
    public PDFRender(int height, int width) {
        _height = height;
        _width = width;
    }

    public void onRunnerStart(Class<?> clazz, Runner runner) {
   		String fileName = ReporterUtil.defaultFileName(clazz, runner)
					+ PDFConstants.POSTFIX;
   		fileName = ReporterUtil.generateFileName(fileName, runner);
        initPDFDocument(fileName);
        renderPreface();
    }

    private void initPDFDocument(String fileName) {
        _document = new Document();
        try {
            _writer = PdfWriter.getInstance(_document, new FileOutputStream(
                    fileName));
        } catch (Exception e) {
            throw new IOException(e);
        }
        _document.open();
    }

    public void renderChart(JFreeChart chart, String prefixFileName) {
        PdfTemplate template = _writer.getDirectContent().createTemplate(_width, _height);
        Graphics2D graphics = template.createGraphics(_width, _height, new DefaultFontMapper());
        Rectangle area = new Rectangle(0, 0, _width, _height);
        chart.draw(graphics, area);
        graphics.dispose();
        try {
            _document.add(new ImgTemplate(template));
        } catch (BadElementException e) {
            throw new IOException(e);
        } catch (DocumentException e) {
            throw new IOException(e);
        }
        _document.newPage();
    }

    public void onRunnerEnd(Class<?> clazz, Runner runner) {
        _document.close();
        _writer.close();
    }

    private void renderPreface() {
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Chunk(Messages.getString("PDFRender.01"), PDFConstants.H1_FONT)); //$NON-NLS-1$
        paragraph.add(new Chunk("\n\n\n", PDFConstants.H1_FONT)); //$NON-NLS-1$
        paragraph.add(new Chunk(Messages.getString("PDFRender.02"), PDFConstants.H1_FONT));//$NON-NLS-1$
        paragraph.add(new Chunk("\n\n\n", PDFConstants.H1_FONT)); //$NON-NLS-1$
        paragraph.add(url(PDFConstants.WEBSITE));
        try {
            _document.add(paragraph);
        } catch (DocumentException e) {
            throw new IOException(e);
        }
        _document.newPage();
    }

    private Element url(String url) {
        Anchor anchor = new Anchor(new Chunk(url));
        anchor.setReference(url);
        return anchor;
    }
}
