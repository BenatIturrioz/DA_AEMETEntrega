package org.example.aemetentrega;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;

public class XMLTransformerXPath
{
    public static void main(String[] args) {
        try {
            File xmlFile = null;

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Selecciona un archivo XML");
            int selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != JFileChooser.APPROVE_OPTION) {
                System.out.println("No se seleccionó ningún archivo. Saliendo...");
            }

            xmlFile = fileChooser.getSelectedFile();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Document newDoc = builder.newDocument();

            Element root = newDoc.createElement("");
            newDoc.appendChild(root);

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();

            XPathExpression expr = xpath.compile("//dia/temperatura");
            NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);


            for (int i = 0; i < nodeList.getLength(); i++) {
                Element newStudent = newDoc.createElement("dia");
                Element newName = newDoc.createElement("temperatura");

                String nameText = nodeList.item(i).getTextContent();

                String moteValue = nameText.toLowerCase().replace(" ", "_");
                newName.setAttribute("mote", moteValue);

                newName.setTextContent(nameText);

                newStudent.appendChild(newName);
                root.appendChild(newStudent);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(newDoc);
            StreamResult result = new StreamResult(new File("output.xml"));
            transformer.transform(source, result);

            System.out.println("Archivo XML transformado correctamente y guardado como 'output.xml'");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
