package utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CSVFileUtil {

    private static void writeContent(String filePath, List<String[]> rows) {
        try (PrintWriter writer = new PrintWriter(new File(filePath))) {
            for (String[] row : rows) {
                writer.println(String.join(",", row));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Create a new CSV file (if not exists)
    public static void createCSVFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                System.out.println("File already exists: " + filePath);
            }
            Files.createFile(path);
            System.out.println("File created: " + filePath);
        } catch (IOException e) {
            System.err.println("Error creating file: " + e.getMessage());
        }
    }

    // Delete the CSV file
    public static void deleteCSVFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                System.out.println("File does not exist: " + filePath);
            }
            Files.delete(path);
            System.out.println("File deleted: " + filePath);
        } catch (IOException e) {
            System.err.println("Error deleting file: " + e.getMessage());
        }
    }

    // Read content from CSV file
    public static List<String[]> readContentFromCSV(String filePath) {
        List<String[]> content = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                content.add(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return content;
    }

    // Write content to CSV file (append mode)
    public static void writeContentToCSV(String filePath, List<String[]> data) {
        if (data == null || data.isEmpty()) {
            System.err.println("No data provided to write.");
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, true))) {
            writer.writeAll(data);
            System.out.println("Data written successfully to: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    // Read a specific row from the CSV file, starting 0 index
    public static String[] readRowFromCSV(String filePath, int rowIndex) {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            int currentRow = 0;
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (currentRow == rowIndex) {
                    return line;
                }
                currentRow++;
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
        } catch (IOException | CsvValidationException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
        return null;
    }

    // Read multiple specific rows from the CSV file
    public static List<String[]> readRowsFromCSV(String filePath, List<Integer> rowIndices) {
        List<String[]> result = new ArrayList<>();
        Set<Integer> rowSet = new HashSet<>(rowIndices); // to avoid duplicates and fast lookup
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            int currentRow = 0;
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (rowSet.contains(currentRow)) {
                    result.add(line);
                }
                currentRow++;
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
        } catch (IOException | CsvValidationException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
        return result;
    }

    // Read a specific column from the CSV file by header name
    public static List<String> readColumnFromCSV(String filePath, String column) {
        List<String> columnData = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] header = reader.readNext();
            if (header == null) return columnData;
            int columnIndex = -1;
            for (int i = 0; i < header.length; i++) {
                if (header[i].equalsIgnoreCase(column.trim())) {
                    columnIndex = i;
                    break;
                }
            }

            if (columnIndex == -1) {
                System.err.println("Column not found: " + column);
                return columnData;
            }

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (columnIndex < line.length) {
                    columnData.add(line[columnIndex]);
                } else {
                    columnData.add(""); // Handle missing value
                }
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println("Error reading CSV column: " + e.getMessage());
        }
        return columnData;
    }

    // Read multiple columns from the CSV file by header names
    public static List<List<String>> readColumnsFromCSV(String filePath, List<String> columns) {
        List<List<String>> columnsData = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            columnsData.add(new ArrayList<>());
        }
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] header = reader.readNext();
            if (header == null) return columnsData;
            List<Integer> columnIndexes = new ArrayList<>();
            for (String col : columns) {
                int idx = -1;
                for (int i = 0; i < header.length; i++) {
                    if (header[i].equalsIgnoreCase(col.trim())) {
                        idx = i;
                        break;
                    }
                }
                columnIndexes.add(idx);
            }
            for (int i = 0; i < columnIndexes.size(); i++) {
                if (columnIndexes.get(i) == -1) {
                    System.err.println("Column not found: " + columns.get(i));
                }
            }
            String[] line;
            while ((line = reader.readNext()) != null) {
                for (int i = 0; i < columnIndexes.size(); i++) {
                    int colIdx = columnIndexes.get(i);
                    if (colIdx >= 0 && colIdx < line.length) {
                        columnsData.get(i).add(line[colIdx]);
                    } else {
                        columnsData.get(i).add(""); // Handle missing value
                    }
                }
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println("Error reading CSV columns: " + e.getMessage());
        }
        return columnsData;
    }

    // Read a specific column from the CSV file by header name, with configurable header row
    // headerRowNum - 1
    public static List<String> readColumnFromCSV(String filePath, String column, int headerRow) {
        List<String> columnData = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            // Skip rows before headerRow
            for (int i = 0; i < headerRow; i++) {
                if (reader.readNext() == null) {
                    // File ended before header row
                    return columnData;
                }
            }

            String[] header = reader.readNext();
            if (header == null) return columnData;

            int columnIndex = -1;
            for (int i = 0; i < header.length; i++) {
                if (header[i].equalsIgnoreCase(column.trim())) {
                    columnIndex = i;
                    break;
                }
            }

            if (columnIndex == -1) {
                System.err.println("Column not found: " + column);
                return columnData;
            }

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (columnIndex < line.length) {
                    columnData.add(line[columnIndex]);
                } else {
                    columnData.add(""); // Handle missing value
                }
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println("Error reading CSV column: " + e.getMessage());
        }
        return columnData;
    }

    // Read multiple columns from the CSV file by header names, with configurable header row
    public static List<List<String>> readColumnsFromCSV(String filePath, List<String> columns, int headerRow) {
        List<List<String>> columnsData = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            columnsData.add(new ArrayList<>());
        }
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            // Skip rows before headerRow
            for (int i = 0; i < headerRow; i++) {
                if (reader.readNext() == null) {
                    // File ended before header row
                    return columnsData;
                }
            }

            String[] header = reader.readNext();
            if (header == null) return columnsData;

            List<Integer> columnIndexes = new ArrayList<>();
            for (String col : columns) {
                int idx = -1;
                for (int i = 0; i < header.length; i++) {
                    if (header[i].equalsIgnoreCase(col.trim())) {
                        idx = i;
                        break;
                    }
                }
                columnIndexes.add(idx);
            }

            for (int i = 0; i < columnIndexes.size(); i++) {
                if (columnIndexes.get(i) == -1) {
                    System.err.println("Column not found: " + columns.get(i));
                }
            }

            String[] line;
            while ((line = reader.readNext()) != null) {
                for (int i = 0; i < columnIndexes.size(); i++) {
                    int colIdx = columnIndexes.get(i);
                    if (colIdx >= 0 && colIdx < line.length) {
                        columnsData.get(i).add(line[colIdx]);
                    } else {
                        columnsData.get(i).add(""); // Handle missing value
                    }
                }
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println("Error reading CSV columns: " + e.getMessage());
        }
        return columnsData;
    }

    // Append
    public static void writeColumnToCSV(String filePath, String column, List<String> data) {
        File file = new File(filePath);
        List<String[]> allRows = readContentFromCSV(filePath);
        List<String> headers = new ArrayList<>();

        if (!file.exists()) {
            headers.add(column);
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println(column);
                for (String value : data) {
                    writer.println(value);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try (PrintWriter writer = new PrintWriter(file)) {
                if (!allRows.isEmpty()) {
                    headers.addAll(Arrays.asList(allRows.get(0)));
                    headers.add(column);
                    writer.println(String.join(",", headers));

                    for (int i = 0; i < Math.max(data.size(), allRows.size() - 1); i++) {
                        String[] row = i < allRows.size() - 1 ? allRows.get(i + 1) : new String[headers.size() - 1];
                        List<String> newRow = new ArrayList<>(Arrays.asList(row));
                        while (newRow.size() < headers.size() - 1) newRow.add("");
                        newRow.add(i < data.size() ? data.get(i) : "");
                        writer.println(String.join(",", newRow));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Append
    public static void writeColumnsToCSV(String filePath, List<String> columns, List<List<String>> data) {
        try (PrintWriter writer = new PrintWriter(new File(filePath))) {
            writer.println(String.join(",", columns));
            for (int i = 0; i < data.get(0).size(); i++) {
                List<String> row = new ArrayList<>();
                for (List<String> columnData : data) {
                    row.add(i < columnData.size() ? columnData.get(i) : "");
                }
                writer.println(String.join(",", row));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateColumnToCSV(String filePath, String column, List<String> data) {
        List<String[]> rows = readContentFromCSV(filePath);
        if (rows.isEmpty()) return;

        String[] headers = rows.get(0);
        List<String[]> updatedRows = new ArrayList<>();
        int colIndex = -1;

        // Determine if column exists
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].equals(column)) {
                colIndex = i;
                break;
            }
        }

        if (colIndex == -1) {
            // Add new column
            headers = Arrays.copyOf(headers, headers.length + 1);
            headers[headers.length - 1] = column;
            colIndex = headers.length - 1;
        }

        updatedRows.add(headers);

        int maxRows = Math.max(rows.size() - 1, data.size());
        for (int i = 0; i < maxRows; i++) {
            String[] baseRow = i + 1 < rows.size() ? rows.get(i + 1) : new String[headers.length];
            String[] newRow = Arrays.copyOf(baseRow, headers.length);
            newRow[colIndex] = i < data.size() ? data.get(i) : "";
            updatedRows.add(newRow);
        }

        writeContent(filePath, updatedRows);
    }

    public static void updateColumnsToCSV(String filePath, List<String> columns, List<List<String>> data) {
        List<String[]> rows = readContentFromCSV(filePath);
        if (rows.isEmpty()) return;

        String[] headers = rows.get(0);
        Map<String, Integer> headerMap = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            headerMap.put(headers[i], i);
        }

        // Update header
        List<String> updatedHeaderList = new ArrayList<>(Arrays.asList(headers));
        List<Integer> targetIndices = new ArrayList<>();

        for (String col : columns) {
            if (!headerMap.containsKey(col)) {
                updatedHeaderList.add(col);
                targetIndices.add(updatedHeaderList.size() - 1);
            } else {
                targetIndices.add(headerMap.get(col));
            }
        }

        int totalCols = updatedHeaderList.size();
        int maxRows = Math.max(rows.size() - 1, data.get(0).size());
        List<String[]> updatedRows = new ArrayList<>();
        updatedRows.add(updatedHeaderList.toArray(new String[0]));

        for (int i = 0; i < maxRows; i++) {
            String[] baseRow = i + 1 < rows.size() ? rows.get(i + 1) : new String[totalCols];
            String[] newRow = Arrays.copyOf(baseRow, totalCols);
            for (int j = 0; j < columns.size(); j++) {
                int index = targetIndices.get(j);
                if (index >= newRow.length) {
                    newRow = Arrays.copyOf(newRow, index + 1);
                }
                newRow[index] = i < data.get(j).size() ? data.get(j).get(i) : "";
            }
            updatedRows.add(newRow);
        }

        writeContent(filePath, updatedRows);
    }

    public static void updateColumnToCSV(String filePath, String column, List<String> data, int headerRow) {
        List<String[]> rows = readContentFromCSV(filePath);
        if (rows.isEmpty() || headerRow >= rows.size()) return;

        // Get headers from the specified header row
        String[] headers = rows.get(headerRow);
        List<String[]> updatedRows = new ArrayList<>();

        int colIndex = -1;
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].equals(column)) {
                colIndex = i;
                break;
            }
        }

        if (colIndex == -1) {
            // Add new column header
            headers = Arrays.copyOf(headers, headers.length + 1);
            headers[headers.length - 1] = column;
            colIndex = headers.length - 1;
        }

        // Copy all rows before headerRow unchanged
        for (int i = 0; i < headerRow; i++) {
            updatedRows.add(rows.get(i));
        }

        // Add updated header row
        updatedRows.add(headers);

        int maxRows = Math.max(rows.size() - (headerRow + 1), data.size());

        for (int i = 0; i < maxRows; i++) {
            int currentRowIndex = i + headerRow + 1;
            String[] baseRow = currentRowIndex < rows.size() ? rows.get(currentRowIndex) : new String[headers.length];
            String[] newRow = Arrays.copyOf(baseRow, headers.length);

            newRow[colIndex] = i < data.size() ? data.get(i) : "";
            updatedRows.add(newRow);
        }

        writeContent(filePath, updatedRows);
    }

    public static void updateColumnsToCSV(String filePath, List<String> columns, List<List<String>> data, int headerRow) {
        List<String[]> rows = readContentFromCSV(filePath);
        if (rows.isEmpty() || headerRow >= rows.size()) return;

        String[] headers = rows.get(headerRow);
        Map<String, Integer> headerMap = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            headerMap.put(headers[i], i);
        }

        List<String> updatedHeaderList = new ArrayList<>(Arrays.asList(headers));
        List<Integer> targetIndices = new ArrayList<>();

        for (String col : columns) {
            if (!headerMap.containsKey(col)) {
                updatedHeaderList.add(col);
                targetIndices.add(updatedHeaderList.size() - 1);
            } else {
                targetIndices.add(headerMap.get(col));
            }
        }

        int totalCols = updatedHeaderList.size();
        int maxRows = Math.max(rows.size() - (headerRow + 1), data.get(0).size());
        List<String[]> updatedRows = new ArrayList<>();

        // Add all rows before headerRow unchanged
        for (int i = 0; i < headerRow; i++) {
            updatedRows.add(rows.get(i));
        }

        // Add updated header row
        updatedRows.add(updatedHeaderList.toArray(new String[0]));

        for (int i = 0; i < maxRows; i++) {
            int currentRowIndex = i + headerRow + 1;
            String[] baseRow = currentRowIndex < rows.size() ? rows.get(currentRowIndex) : new String[totalCols];
            String[] newRow = Arrays.copyOf(baseRow, totalCols);

            for (int j = 0; j < columns.size(); j++) {
                int index = targetIndices.get(j);
                if (index >= newRow.length) {
                    newRow = Arrays.copyOf(newRow, index + 1);
                }
                newRow[index] = i < data.get(j).size() ? data.get(j).get(i) : "";
            }
            updatedRows.add(newRow);
        }

        writeContent(filePath, updatedRows);
    }

    public static List<String[]> filterRowsFromCSV(String filePath, String columnName, String columnValue) {
        List<String[]> filteredRows = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] header = reader.readNext();
            if (header == null) return filteredRows;

            int colIndex = -1;
            for (int i = 0; i < header.length; i++) {
                if (header[i].equalsIgnoreCase(columnName.trim())) {
                    colIndex = i;
                    break;
                }
            }

            if (colIndex == -1) {
                System.err.println("Column not found: " + columnName);
                return filteredRows;
            }

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (colIndex < line.length && columnValue.equalsIgnoreCase(line[colIndex].trim())) {
                    filteredRows.add(line);
                }
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println("Error filtering CSV rows: " + e.getMessage());
        }
        return filteredRows;
    }

    public static List<String[]> filterRowsFromCSV(String filePath, List<String> columnsName, List<String> columnsValue) {
        List<String[]> filteredRows = new ArrayList<>();
        if (columnsName == null || columnsValue == null || columnsName.size() != columnsValue.size()) {
            System.err.println("Column names and values must be non-null and have the same size.");
            return filteredRows;
        }

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] header = reader.readNext();
            if (header == null) return filteredRows;

            Map<String, Integer> colIndexMap = new HashMap<>();
            for (int i = 0; i < header.length; i++) {
                colIndexMap.put(header[i].trim(), i);
            }

            // Validate all column names exist
            for (String colName : columnsName) {
                if (!colIndexMap.containsKey(colName.trim())) {
                    System.err.println("Column not found: " + colName);
                    return filteredRows;
                }
            }

            String[] line;
            while ((line = reader.readNext()) != null) {
                boolean matches = true;
                for (int i = 0; i < columnsName.size(); i++) {
                    String col = columnsName.get(i).trim();
                    String val = columnsValue.get(i).trim();
                    int colIndex = colIndexMap.get(col);
                    if (colIndex >= line.length || !val.equalsIgnoreCase(line[colIndex].trim())) {
                        matches = false;
                        break;
                    }
                }
                if (matches) {
                    filteredRows.add(line);
                }
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println("Error filtering CSV rows: " + e.getMessage());
        }
        return filteredRows;
    }

    public static List<List<String>> filterRowsFromCSV(String filePath, String columnName, String columnValue, List<String> columnsToIncludeInRow) {
        List<List<String>> result = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] header = reader.readNext();
            if (header == null) return result;

            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < header.length; i++) {
                headerMap.put(header[i].trim(), i);
            }

            if (!headerMap.containsKey(columnName)) {
                System.err.println("Filter column not found: " + columnName);
                return result;
            }

            List<Integer> includedIndices = new ArrayList<>();
            for (String col : columnsToIncludeInRow) {
                if (headerMap.containsKey(col)) {
                    includedIndices.add(headerMap.get(col));
                } else {
                    System.err.println("Included column not found: " + col);
                    includedIndices.add(-1);
                }
            }

            int filterIndex = headerMap.get(columnName);

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (filterIndex < line.length && columnValue.equalsIgnoreCase(line[filterIndex].trim())) {
                    List<String> row = new ArrayList<>();
                    for (int idx : includedIndices) {
                        row.add(idx >= 0 && idx < line.length ? line[idx] : "");
                    }
                    result.add(row);
                }
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println("Error filtering rows from CSV: " + e.getMessage());
        }

        return result;
    }

    public static List<List<String>> filterRowsFromCSV(String filePath, List<String> columnNames, List<String> columnValues, List<String> columnsToIncludeInRow) {
        List<List<String>> result = new ArrayList<>();

        if (columnNames == null || columnValues == null || columnNames.size() != columnValues.size()) {
            System.err.println("Filter column names and values must be non-null and the same size.");
            return result;
        }

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] header = reader.readNext();
            if (header == null) return result;

            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < header.length; i++) {
                headerMap.put(header[i].trim(), i);
            }

            List<Integer> filterIndices = new ArrayList<>();
            for (String col : columnNames) {
                if (headerMap.containsKey(col)) {
                    filterIndices.add(headerMap.get(col));
                } else {
                    System.err.println("Filter column not found: " + col);
                    return result;
                }
            }

            List<Integer> includedIndices = new ArrayList<>();
            for (String col : columnsToIncludeInRow) {
                if (headerMap.containsKey(col)) {
                    includedIndices.add(headerMap.get(col));
                } else {
                    System.err.println("Included column not found: " + col);
                    includedIndices.add(-1);
                }
            }

            String[] line;
            while ((line = reader.readNext()) != null) {
                boolean matches = true;
                for (int i = 0; i < filterIndices.size(); i++) {
                    int idx = filterIndices.get(i);
                    String expected = columnValues.get(i).trim();
                    if (idx >= line.length || !expected.equalsIgnoreCase(line[idx].trim())) {
                        matches = false;
                        break;
                    }
                }

                if (matches) {
                    List<String> row = new ArrayList<>();
                    for (int idx : includedIndices) {
                        row.add(idx >= 0 && idx < line.length ? line[idx] : "");
                    }
                    result.add(row);
                }
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println("Error filtering rows from CSV: " + e.getMessage());
        }

        return result;
    }

    public static List<String> filterColumnFromCSV(String filePath, String columnName, String columnValue) {
        List<String> result = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] header = reader.readNext();
            if (header == null) return result;

            Map<String, Integer> colMap = new HashMap<>();
            for (int i = 0; i < header.length; i++) {
                colMap.put(header[i].trim(), i);
            }

            if (!colMap.containsKey(columnName)) {
                System.err.println("Column not found: " + columnName);
                return result;
            }

            int matchIndex = colMap.get(columnName);

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (matchIndex < line.length && columnValue.equalsIgnoreCase(line[matchIndex].trim())) {
                    result.add(line[matchIndex]);
                }
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println("Error filtering CSV column: " + e.getMessage());
        }
        return result;
    }

    public static List<List<String>> filterColumnsFromCSV(String filePath, List<String> columnsName, List<String> columnsValue) {
        List<List<String>> result = new ArrayList<>();
        for (int i = 0; i < columnsName.size(); i++) {
            result.add(new ArrayList<>());
        }

        if (columnsName == null || columnsValue == null || columnsName.size() != columnsValue.size()) {
            System.err.println("Column names and values must be non-null and have the same size.");
            return result;
        }

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] header = reader.readNext();
            if (header == null) return result;

            Map<String, Integer> colMap = new HashMap<>();
            for (int i = 0; i < header.length; i++) {
                colMap.put(header[i].trim(), i);
            }

            for (String col : columnsName) {
                if (!colMap.containsKey(col.trim())) {
                    System.err.println("Column not found: " + col);
                    return result;
                }
            }

            String[] line;
            while ((line = reader.readNext()) != null) {
                boolean match = true;
                for (int i = 0; i < columnsName.size(); i++) {
                    String col = columnsName.get(i).trim();
                    String val = columnsValue.get(i).trim();
                    int index = colMap.get(col);
                    if (index >= line.length || !val.equalsIgnoreCase(line[index].trim())) {
                        match = false;
                        break;
                    }
                }

                if (match) {
                    for (int i = 0; i < columnsName.size(); i++) {
                        int index = colMap.get(columnsName.get(i).trim());
                        result.get(i).add(index < line.length ? line[index] : "");
                    }
                }
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println("Error filtering CSV columns: " + e.getMessage());
        }

        return result;
    }
}
