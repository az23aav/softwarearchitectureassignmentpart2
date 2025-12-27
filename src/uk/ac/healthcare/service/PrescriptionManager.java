package uk.ac.healthcare.service;

import uk.ac.healthcare.model.Prescription;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class PrescriptionManager {

    private static PrescriptionManager instance;

    private PrescriptionManager() {}

    public static synchronized PrescriptionManager getInstance() {
        if (instance == null) {
            instance = new PrescriptionManager();
        }
        return instance;
    }

    public Path exportPrescription(Prescription p, Path outputDir) throws IOException {
        Files.createDirectories(outputDir);

        String fileName = "prescription_" + safe(p.getPrescriptionId()) + ".txt";
        Path outFile = outputDir.resolve(fileName);

        Files.writeString(outFile, p.toFileContent(), StandardCharsets.UTF_8);
        return outFile;
    }

    private String safe(String s) {
        if (s == null) return "UNKNOWN";
        return s.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}

