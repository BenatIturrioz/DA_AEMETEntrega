package org.example.aemetentrega;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class UrlFindFinder {

    public static void obtainDocument() throws ParserConfigurationException, NoSuchAlgorithmException, IOException, KeyManagementException, SAXException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = null;

        String url = "https://www.aemet.es/xml/municipios/localidad_20076.xml";

        ArrayList<Object> a = getFiles(true, url);

        InputStream iXmlFile = (InputStream) a.get(0);
        File fXmlFile = (File) a.get(1);

        if (url != null) {
            document = dBuilder.parse(iXmlFile);
        } else {
            document = dBuilder.parse(fXmlFile);
        }

        NodeList nodesList;
        try {
            nodesList = XpathHelper.get(document, "");
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }

        if (iXmlFile != null) {
            XmlSaver.saveXmlFile(iXmlFile, "aemet.xml");
            System.out.println("aemet.xml saved");
        } else {
            System.out.println("No se pudo descargar el archivo XML.");
        }

    }

    public static ArrayList<Object> getFiles(boolean aukeratu, String url)
            throws KeyManagementException, NoSuchAlgorithmException, MalformedURLException, IOException {
        File fXmlFile = null;
        InputStream iXmlFile = null;

        if (url != null) {
            aukeratu = false;
            MyUrlConnection.disableSSLCertificateValidation();
            iXmlFile = MyUrlConnection.getFileFromURL(url);
        }

        if (aukeratu) {
            fXmlFile = FileChoser.chooseWindow();
        } else {
            fXmlFile = FileChoser.getFileFromRoute();
        }

        ArrayList<Object> a = new ArrayList();
        a.add(iXmlFile);
        a.add(fXmlFile);

        return a;
    }

}
