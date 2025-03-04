package org.example.aemetentrega;

import java.io.IOException;

public class BatchExecutor {
    public void executeBatchFile() {
        try {

            String batFilePath = "updateGitHub.bat";


            ProcessBuilder processBuilder = new ProcessBuilder(batFilePath);
            processBuilder.start(); 
            System.out.println("Archivo .bat ejecutado correctamente.");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al ejecutar el archivo .bat.");
        }
    }
}
