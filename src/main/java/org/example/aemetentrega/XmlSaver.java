package org.example.aemetentrega;

import java.io.*;

public class XmlSaver {

    public static void saveXmlFile(InputStream inputStream, String fileName) throws IOException {
        String userHome = System.getProperty("user.home");
        String documentsPath = userHome + File.separator + "Documents" + File.separator + fileName;

        File outputFile = new File(documentsPath);

        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            System.out.println("Archivo guardado en: " + documentsPath);
        } finally {
            inputStream.close();
        }
    }

}
