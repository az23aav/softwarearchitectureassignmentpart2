package uk.ac.healthcare.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CsvUtils {

    public static List<String> readLines(String filePath) {
        try {
            return Files.readAllLines(Path.of(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file: " + filePath, e);
        }
    }

    public static List<Map<String, String>> read(Path csv) throws IOException {
        List<String> lines = Files.readAllLines(csv);
        if (lines.isEmpty()) return List.of();

        String[] headers = lines.get(0).split(",", -1);

        List<Map<String, String>> rows = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            String[] values = lines.get(i).split(",", -1);
            Map<String, String> row = new LinkedHashMap<>();

            for (int j = 0; j < headers.length; j++) {
                String key = headers[j].trim();
                String val = (j < values.length) ? values[j].trim() : "";
                row.put(key, val);
            }
            rows.add(row);
        }
        return rows;
    }
}