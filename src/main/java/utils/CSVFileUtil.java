package utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CSVFileUtil {

    private static final Logger logger = LogManager.getLogger(CSVFileUtil.class);

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
                logger.info("File already exists: {}", filePath);
            } else {
                Files.createFile(path);
                logger.info("File created: {}", filePath);
            }
        } catch (IOException e) {
            logger.error("Error creating file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create CSV file", e);
        }
    }

    // Delete the CSV file
    public static void deleteCSVFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                logger.info("File does not exist: {}", filePath);
            } else {
                Files.delete(path);
                logger.info("File deleted: {}", filePath);
            }
        } catch (IOException e) {
            logger.error("Error deleting file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete CSV file", e);
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
        } catch (NoSuchFileException | FileNotFoundException e) {
            logger.error("File not found: {}", filePath, e);
            throw new RuntimeException("CSV file not found: " + filePath, e);
        } catch (IOException e) {
            logger.error("Error reading CSV file: {}", e.getMessage(), e);
            throw new RuntimeException("Error reading CSV file: " + filePath, e);
        } catch (CsvValidationException e) {
            logger.error("CSV validation error while reading file: {}", filePath, e);
            throw new RuntimeException("CSV validation failed for file: " + filePath, e);
        }

        return content;
    }

    // Write content to CSV file (append mode)
    public static void writeContentToCSV(String filePath, List<String[]> data) {
        if (data == null || data.isEmpty()) {
            logger.warn("No data provided to write to CSV file: {}", filePath);
            throw new IllegalArgumentException("No data provided to write.");
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, true))) {
            writer.writeAll(data);
            logger.info("Data written successfully to: {}", filePath);
        } catch (IOException e) {
            logger.error("Error writing to CSV file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to write to CSV file: " + filePath, e);
        }
    }

    // Read a specific row from the CSV file, starting at index 0
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
            logger.warn("Row index {} out of bounds in file: {}", rowIndex, filePath);
        } catch (FileNotFoundException e) {
            logger.error("File not found: {}", filePath, e);
            throw new RuntimeException("CSV file not found: " + filePath, e);
        } catch (IOException | CsvValidationException e) {
            logger.error("Error reading CSV file: {}", e.getMessage(), e);
            throw new RuntimeException("Error reading CSV file: " + filePath, e);
        }
        return null;
    }

    // Read multiple specific rows from the CSV file
    public static List<String[]> readRowsFromCSV(String filePath, List<Integer> rowIndices) {
        List<String[]> result = new ArrayList<>();
        Set<Integer> rowSet = new HashSet<>(rowIndices);
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
            logger.error("File not found: {}", filePath, e);
            throw new RuntimeException("CSV file not found: " + filePath, e);
        } catch (IOException | CsvValidationException e) {
            logger.error("Error reading CSV file: {}", e.getMessage(), e);
            throw new RuntimeException("Error reading CSV file: " + filePath, e);
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
                logger.error("Column not found: {}", column);
                throw new IllegalArgumentException("Column not found: " + column);
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
            logger.error("Error reading CSV column from file {}: {}", filePath, e.getMessage(), e);
            throw new RuntimeException("Failed to read column from CSV file: " + filePath, e);
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
                    logger.error("Column not found: {}", columns.get(i));
                    throw new IllegalArgumentException("Column not found: " + columns.get(i));
                }
            }

            String[] line;
            while ((line = reader.readNext()) != null) {
                for (int i = 0; i < columnIndexes.size(); i++) {
                    int colIdx = columnIndexes.get(i);
                    if (colIdx >= 0 && colIdx < line.length) {
                        columnsData.get(i).add(line[colIdx]);
                    } else {
                        columnsData.get(i).add("");
                    }
                }
            }
        } catch (IOException | CsvValidationException e) {
            logger.error("Error reading CSV columns from file {}: {}", filePath, e.getMessage(), e);
            throw new RuntimeException("Failed to read columns from CSV file: " + filePath, e);
        }

        return columnsData;
    }

    // Read a specific column from the CSV file by header name with configurable header row
    public static List<String> readColumnFromCSV(String filePath, String column, int headerRow) {
        List<String> columnData = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            for (int i = 0; i < headerRow; i++) {
                if (reader.readNext() == null) return columnData;
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
                logger.error("Column not found at row {}: {}", headerRow, column);
                throw new IllegalArgumentException("Column not found: " + column);
            }

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (columnIndex < line.length) {
                    columnData.add(line[columnIndex]);
                } else {
                    columnData.add("");
                }
            }
        } catch (IOException | CsvValidationException e) {
            logger.error("Error reading CSV column from file {} at header row {}: {}", filePath, headerRow, e.getMessage(), e);
            throw new RuntimeException("Failed to read column from CSV file: " + filePath, e);
        }

        return columnData;
    }

    // Read multiple columns from the CSV file by header names with configurable header row
    public static List<List<String>> readColumnsFromCSV(String filePath, List<String> columns, int headerRow) {
        List<List<String>> columnsData = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            columnsData.add(new ArrayList<>());
        }

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            for (int i = 0; i < headerRow; i++) {
                if (reader.readNext() == null) return columnsData;
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
                    logger.error("Column not found at row {}: {}", headerRow, columns.get(i));
                    throw new IllegalArgumentException("Column not found: " + columns.get(i));
                }
            }

            String[] line;
            while ((line = reader.readNext()) != null) {
                for (int i = 0; i < columnIndexes.size(); i++) {
                    int colIdx = columnIndexes.get(i);
                    if (colIdx >= 0 && colIdx < line.length) {
                        columnsData.get(i).add(line[colIdx]);
                    } else {
                        columnsData.get(i).add("");
                    }
                }
            }
        } catch (IOException | CsvValidationException e) {
            logger.error("Error reading CSV columns from file {} at header row {}: {}", filePath, headerRow, e.getMessage(), e);
            throw new RuntimeException("Failed to read columns from CSV file: " + filePath, e);
        }

        return columnsData;
    }

    // Append a single column to an existing CSV file or create a new file
    public static void writeColumnToCSV(String filePath, String column, List<String> data) {
        File file = new File(filePath);
        List<String[]> allRows = readContentFromCSV(filePath);
        List<String> headers = new ArrayList<>();

        try (PrintWriter writer = new PrintWriter(file)) {
            if (!file.exists() || allRows.isEmpty()) {
                // File doesn't exist or is empty
                headers.add(column);
                writer.println(column);
                for (String value : data) {
                    writer.println(value);
                }
            } else {
                // File exists and has content
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
            logger.error("Error writing column '{}' to CSV file '{}': {}", column, filePath, e.getMessage(), e);
            throw new RuntimeException("Failed to write column to CSV: " + filePath, e);
        }
    }

    // Append multiple columns to a new or existing CSV file
    public static void writeColumnsToCSV(String filePath, List<String> columns, List<List<String>> data) {
        File file = new File(filePath);

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println(String.join(",", columns));
            int numRows = data.isEmpty() ? 0 : data.get(0).size();

            for (int i = 0; i < numRows; i++) {
                List<String> row = new ArrayList<>();
                for (List<String> columnData : data) {
                    row.add(i < columnData.size() ? columnData.get(i) : "");
                }
                writer.println(String.join(",", row));
            }
        } catch (IOException e) {
            logger.error("Error writing columns to CSV file '{}': {}", filePath, e.getMessage(), e);
            throw new RuntimeException("Failed to write columns to CSV: " + filePath, e);
        }
    }

    // Updates a single column in the CSV file, adding the column if it doesn't exist.
    public static void updateColumnToCSV(String filePath, String column, List<String> data) {
        try {
            List<String[]> rows = readContentFromCSV(filePath);
            if (rows.isEmpty()) return;

            String[] headers = rows.get(0);
            List<String[]> updatedRows = new ArrayList<>();
            int colIndex = -1;

            for (int i = 0; i < headers.length; i++) {
                if (headers[i].equals(column)) {
                    colIndex = i;
                    break;
                }
            }

            if (colIndex == -1) {
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
        } catch (Exception e) {
            logger.error("Failed to update single column in CSV", e);
            throw new RuntimeException("Error updating single column in CSV", e);
        }
    }

    // Updates multiple columns in the CSV file, adding columns if they don't exist.
    public static void updateColumnsToCSV(String filePath, List<String> columns, List<List<String>> data) {
        try {
            List<String[]> rows = readContentFromCSV(filePath);
            if (rows.isEmpty()) return;

            String[] headers = rows.get(0);
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
        } catch (Exception e) {
            logger.error("Failed to update multiple columns in CSV", e);
            throw new RuntimeException("Error updating multiple columns in CSV", e);
        }
    }

    // Updates a single column in the CSV file with a specified header row, adding the column if missing.
    public static void updateColumnToCSV(String filePath, String column, List<String> data, int headerRow) {
        try {
            List<String[]> rows = readContentFromCSV(filePath);
            if (rows.isEmpty() || headerRow >= rows.size()) return;

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
                headers = Arrays.copyOf(headers, headers.length + 1);
                headers[headers.length - 1] = column;
                colIndex = headers.length - 1;
            }

            for (int i = 0; i < headerRow; i++) {
                updatedRows.add(rows.get(i));
            }

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
        } catch (Exception e) {
            logger.error("Failed to update single column in CSV with header row", e);
            throw new RuntimeException("Error updating single column in CSV with header row", e);
        }
    }

    // Updates multiple columns in the CSV file with a specified header row, adding columns if missing.
    public static void updateColumnsToCSV(String filePath, List<String> columns, List<List<String>> data, int headerRow) {
        try {
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

            for (int i = 0; i < headerRow; i++) {
                updatedRows.add(rows.get(i));
            }

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
        } catch (Exception e) {
            logger.error("Failed to update multiple columns in CSV with header row", e);
            throw new RuntimeException("Error updating multiple columns in CSV with header row", e);
        }
    }

    // Filters rows from CSV where a single column matches the given value.
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
                logger.error("Column not found: {}", columnName);
                return filteredRows;
            }

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (colIndex < line.length && columnValue.equalsIgnoreCase(line[colIndex].trim())) {
                    filteredRows.add(line);
                }
            }
        } catch (IOException | CsvValidationException e) {
            logger.error("Error filtering CSV rows for single column", e);
            throw new RuntimeException("Error filtering CSV rows for single column", e);
        }
        return filteredRows;
    }

    // Filters rows from CSV where multiple columns match given values.
    public static List<String[]> filterRowsFromCSV(String filePath, List<String> columnsName, List<String> columnsValue) {
        List<String[]> filteredRows = new ArrayList<>();
        if (columnsName == null || columnsValue == null || columnsName.size() != columnsValue.size()) {
            logger.error("Column names and values must be non-null and have the same size.");
            return filteredRows;
        }

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] header = reader.readNext();
            if (header == null) return filteredRows;

            Map<String, Integer> colIndexMap = new HashMap<>();
            for (int i = 0; i < header.length; i++) {
                colIndexMap.put(header[i].trim(), i);
            }

            for (String colName : columnsName) {
                if (!colIndexMap.containsKey(colName.trim())) {
                    logger.error("Column not found: {}", colName);
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
            logger.error("Error filtering CSV rows for multiple columns", e);
            throw new RuntimeException("Error filtering CSV rows for multiple columns", e);
        }
        return filteredRows;
    }

    // Filters rows by one column and returns specified columns from matching rows.
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
                logger.error("Filter column not found: {}", columnName);
                return result;
            }

            List<Integer> includedIndices = new ArrayList<>();
            for (String col : columnsToIncludeInRow) {
                if (headerMap.containsKey(col)) {
                    includedIndices.add(headerMap.get(col));
                } else {
                    logger.warn("Included column not found: {}", col);
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
            logger.error("Error filtering rows from CSV by single column", e);
            throw new RuntimeException("Error filtering rows from CSV by single column", e);
        }
        return result;
    }

    // Filters rows by multiple columns and returns specified columns from matching rows.
    public static List<List<String>> filterRowsFromCSV(String filePath, List<String> columnNames, List<String> columnValues, List<String> columnsToIncludeInRow) {
        List<List<String>> result = new ArrayList<>();
        if (columnNames == null || columnValues == null || columnNames.size() != columnValues.size()) {
            logger.error("Filter column names and values must be non-null and the same size.");
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
                    logger.error("Filter column not found: {}", col);
                    return result;
                }
            }

            List<Integer> includedIndices = new ArrayList<>();
            for (String col : columnsToIncludeInRow) {
                if (headerMap.containsKey(col)) {
                    includedIndices.add(headerMap.get(col));
                } else {
                    logger.warn("Included column not found: {}", col);
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
            logger.error("Error filtering rows from CSV by multiple columns", e);
            throw new RuntimeException("Error filtering rows from CSV by multiple columns", e);
        }
        return result;
    }

    // Filters a single column from CSV rows where columnName matches columnValue.
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
                logger.error("Column not found: {}", columnName);
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
            logger.error("Error filtering CSV column", e);
            throw new RuntimeException("Error filtering CSV column", e);
        }
        return result;
    }

    // Filters multiple columns from CSV where columnsName match columnsValue and returns matching column data lists.
    public static List<List<String>> filterColumnsFromCSV(String filePath, List<String> columnsName, List<String> columnsValue) {
        List<List<String>> result = new ArrayList<>();
        for (int i = 0; i < columnsName.size(); i++) {
            result.add(new ArrayList<>());
        }

        if (columnsName == null || columnsValue == null || columnsName.size() != columnsValue.size()) {
            logger.error("Column names and values must be non-null and have the same size.");
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
                    logger.error("Column not found: {}", col);
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
            logger.error("Error filtering CSV columns", e);
            throw new RuntimeException("Error filtering CSV columns", e);
        }
        return result;
    }
}
