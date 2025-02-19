package org.example.aemet;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class XmlDriveUploader {
    public static void main(String[] args) {
        JFrame frame = new JFrame("XML Downloader & Uploader");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        JButton downloadButton = new JButton("Download XML");
        JButton modifyButton = new JButton("Modify XML");

        downloadButton.addActionListener(e -> downloadXML("https://www.aemet.es/xml/municipios/localidad_20076.xml", "file.xml"));
        modifyButton.addActionListener(e -> modifyXML("file.xml"));

        frame.add(downloadButton);
        frame.add(modifyButton);
        frame.setVisible(true);
    }

    private static void downloadXML(String urlString, String fileName) {
        try (InputStream in = new URL(urlString).openStream()) {
            Files.copy(in, Paths.get(fileName));
            JOptionPane.showMessageDialog(null, "XML Downloaded Successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Download Failed!");
        }
    }

    private static void modifyXML(String filePath) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filePath));

            NodeList nodeList = document.getElementsByTagName("nombre");
            if (nodeList.getLength() > 0) {
                Node node = nodeList.item(0);
                node.setTextContent("Modified Name");
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(document), new StreamResult(new File(filePath)));

            JOptionPane.showMessageDialog(null, "XML Modified Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Modification Failed!");
        }
    }
}
