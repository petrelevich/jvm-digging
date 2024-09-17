package ru.demo.mainpackage;

import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.List;

public class PDFTableExtractor extends PDFTextStripper {

    public PDFTableExtractor() throws IOException {
        super();
        // Use reflection to set the 'output' field to a dummy writer
        try {
            Field outputField = PDFTextStripper.class.getDeclaredField("output");
            outputField.setAccessible(true);
            outputField.set(this, new NullWriter());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set dummy writer", e);
        }
    }


    @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
//        for (TextPosition text : textPositions) {
//            System.out.println("Text: " + text.getUnicode() + " at position: " + text.getXDirAdj() + ", " + text.getYDirAdj());
//        }
        System.out.println(string);
        // Do not call super.writeString
    }

    // Dummy writer that discards all output
    private static class NullWriter extends Writer {
        @Override
        public void write(char[] cbuf, int off, int len) {
            // Do nothing
        }

        @Override
        public void flush() {
            // Do nothing
        }

        @Override
        public void close() {
            // Do nothing
        }
    }

}