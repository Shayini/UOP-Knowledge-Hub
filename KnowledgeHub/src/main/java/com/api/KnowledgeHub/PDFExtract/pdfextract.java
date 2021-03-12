package com.api.KnowledgeHub.PDFExtract;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class pdfextract {

    public static String extract(String filename) throws IOException, TikaException, SAXException {
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        FileInputStream inputstream = new FileInputStream(new File("F:\\windows\\Project\\KnowledgeHub\\upload\\" + filename));
        ParseContext pcontext = new ParseContext();

        //parsing the document using PDF parser
        PDFParser pdfparser = new PDFParser();
        pdfparser.parse(inputstream, handler, metadata,pcontext);

        //getting the content of the document
        String str = handler.toString();
        str = str.replace("\n", " ");
        str = str.replace("\t", " ");
        str = str.toLowerCase();
        str = " " + str;
//        String [] arr = str.split(" ");
//        str = String.join(" ", arr);
        System.out.println("Contents of the PDF :" + str);

        //getting metadata of the document
//        System.out.println("Metadata of the PDF:");
        String[] metadataNames = metadata.names();

//        for(String name : metadataNames) {
//            System.out.println(name+ " : " + metadata.get(name));
//        }

        return str;
    }
}
