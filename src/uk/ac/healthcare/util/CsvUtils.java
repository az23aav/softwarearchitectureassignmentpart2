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

        List<String> headersList = splitCsvLine(lines.get(0));
        String[] headers = headersList.toArray(new String[0]);

        List<Map<String, String>> rows = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            List<String> valuesList = splitCsvLine(lines.get(i));
            String[] values = valuesList.toArray(new String[0]);
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
    private static List<String> splitCsvLine(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                // toggle quotes, but allow escaped quotes ""
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    cur.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                out.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        out.add(cur.toString());
        return out;
    }
}