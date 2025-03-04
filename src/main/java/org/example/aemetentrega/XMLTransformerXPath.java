package org.example.aemetentrega;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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

public class XMLTransformerXPath {

    public static void transformXML(Document doc) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document newDoc = builder.newDocument();
            Element root = newDoc.createElement("datos");
            newDoc.appendChild(root);

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();
            XPathExpression expr = xpath.compile("//dia[prob_precipitacion and temperatura]"); // Seleccionamos todos los días con prob_precipitacion y temperatura
            NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element diaElement = (Element) nodeList.item(i);
                Element newDia = newDoc.createElement("dia");

                // Extraemos la fecha del día
                String fecha = diaElement.getAttribute("fecha");
                Element newFecha = newDoc.createElement("fecha");
                newFecha.setTextContent(fecha);
                newDia.appendChild(newFecha);

                // Extraemos y calculamos la media de la probabilidad de precipitación
                NodeList probNodes = diaElement.getElementsByTagName("prob_precipitacion");
                if (probNodes.getLength() > 0) {
                    double probSum = 0;
                    int probCount = 0; // Para contar los valores válidos de prob_precipitacion
                    for (int j = 0; j < probNodes.getLength(); j++) {
                        String probValue = probNodes.item(j).getTextContent().trim();
                        if (!probValue.isEmpty()) {
                            try {
                                probSum += Double.parseDouble(probValue);
                                probCount++;
                            } catch (NumberFormatException e) {
                                // Si el valor no es válido, lo ignoramos
                                System.out.println("Invalid prob_precipitacion value: " + probValue);
                            }
                        }
                    }
                    if (probCount > 0) {
                        double probAverage = probSum / probCount;
                        Element newProb = newDoc.createElement("prob_precipitacion_media");
                        newProb.setTextContent(String.valueOf(probAverage));
                        newDia.appendChild(newProb);
                    }
                }

                // Extraemos y calculamos la media de la temperatura por hora
                NodeList tempNodes = diaElement.getElementsByTagName("temperatura");
                if (tempNodes.getLength() > 0) {
                    double tempSum = 0;
                    int tempCount = 0; // Para contar los valores válidos de temperatura
                    NodeList temperaturaHoras = ((Element) tempNodes.item(0)).getElementsByTagName("dato");
                    for (int j = 0; j < temperaturaHoras.getLength(); j++) {
                        String tempValue = temperaturaHoras.item(j).getTextContent().trim();
                        if (!tempValue.isEmpty()) {
                            try {
                                tempSum += Double.parseDouble(tempValue);
                                tempCount++;
                            } catch (NumberFormatException e) {
                                // Si el valor no es válido, lo ignoramos
                                System.out.println("Invalid temperatura value: " + tempValue);
                            }
                        }
                    }
                    if (tempCount > 0) {
                        double tempAverage = tempSum / tempCount;
                        Element newTemp = newDoc.createElement("temperatura_media");
                        newTemp.setTextContent(String.valueOf(tempAverage));
                        newDia.appendChild(newTemp);
                    }
                }

                // Añadimos el nuevo día al documento raíz
                root.appendChild(newDia);
            }

            // Guardamos el nuevo documento XML con la información transformada
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(newDoc);
            StreamResult result = new StreamResult(new File("../../EguraldiaXML/output.xml"));
            transformer.transform(source, result);

            System.out.println("Archivo XML transformado correctamente y guardado como 'output.xml'");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
