package tests;

import org.testng.annotations.Test;
import utils.CSVFileUtil;

import java.util.Arrays;
import java.util.List;

public class CSVTest {

    @Test(enabled = false)
    public void createNewCSVTest(){
        CSVFileUtil.createCSVFile(System.getProperty("user.dir")+"/test data/created csv/createdFile.csv");
    }

    @Test(enabled = false)
    public void deleteNewCSVTest(){
        CSVFileUtil.deleteCSVFile(System.getProperty("user.dir")+"/test data/createdFile.csv");
    }

    @Test(enabled = false)
    public void readDataCSVTest(){
        List<String[]> data = CSVFileUtil.readContentFromCSV(System.getProperty("user.dir")+"/test data/csv files/SB-1505202514_P1005 1.csv");
        for(String[] row:data){
            System.out.println(Arrays.toString(row));
        }
    }

    @Test(enabled = false)
    public void writeDataCSVTest(){
        CSVFileUtil.createCSVFile(System.getProperty("user.dir")+"/test data/created csv/createdFile.csv");
        List<String[]> dataToWrite = CSVFileUtil.readContentFromCSV(System.getProperty("user.dir")+"/test data/csv files/SB-1505202514_P1005 1.csv");
        CSVFileUtil.writeContentToCSV(System.getProperty("user.dir")+"/test data/created csv/createdFile.csv",dataToWrite);
        List<String[]> dataToRead = CSVFileUtil.readContentFromCSV(System.getProperty("user.dir")+"/test data/created csv/createdFile.csv");
        for(String[] row:dataToRead){
            System.out.println(Arrays.toString(row));
        }
    }

    @Test(enabled = false)
    public void readSpecificRow(){
        String[] row = CSVFileUtil.readRowFromCSV(System.getProperty("user.dir")+"/test data/created csv/createdFile.csv",251);
        System.out.println(Arrays.toString(row));
    }

    @Test(enabled = false)
    public void readSpecificRows(){
        List<Integer> rowIndices = Arrays.asList(0,10,300);
        List<String[]> rows = CSVFileUtil.readRowsFromCSV(System.getProperty("user.dir")+"/test data/created csv/createdFile.csv",rowIndices);
        for(String[] row:rows){
            System.out.println(Arrays.toString(row));
        }
    }

    @Test(enabled = false)
    public void readSepificCol(){
        List<String> colData = CSVFileUtil.readColumnFromCSV(System.getProperty("user.dir")+"/test data/csv files/SB-1505202514_P1005 2.csv","Well");
        for(String cell:colData){
            System.out.println(cell);
        }
    }

    @Test(enabled = false)
    public void readSepificCols(){
        String filePath = System.getProperty("user.dir")+"/test data/csv files/SB-1505202514_P1005 2.csv";
        List<String> colHeaders = Arrays.asList("Well","Well Position","Target");
        List<List<String>> selectedCols = CSVFileUtil.readColumnsFromCSV(filePath, colHeaders);
        for (List<String> row : selectedCols) {
            for (String cell : row) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }
    }

    @Test(enabled = false)
    public void writeSepificCol(){
        List<String> colDataToWrite = CSVFileUtil.readColumnFromCSV(System.getProperty("user.dir")+"/test data/csv files/SB-1505202514_P1005 2.csv","Well");
        CSVFileUtil.writeColumnToCSV(System.getProperty("user.dir")+"/test data/csv files/SB-1505202514_P1005 3.csv","Well",colDataToWrite);
    }

    @Test(enabled = false)
    public void updateSepificCol(){
        List<String> colDataToWrite = CSVFileUtil.readColumnFromCSV(System.getProperty("user.dir")+"/test data/csv files/SB-1505202514_P1005 2.csv","Well");
        CSVFileUtil.updateColumnToCSV(System.getProperty("user.dir")+"/test data/csv files/SB-1505202514_P1005 3.csv","Well",colDataToWrite);
    }

    @Test(enabled = false)
    public void updateSepificCols(){
        List<String> colsToRead = Arrays.asList("Well","Well Position","Sample Name");
        List<List<String>> colsDataToWrite = CSVFileUtil.readColumnsFromCSV(System.getProperty("user.dir")+"/test data/csv files/SB-1505202514_P1005 2.csv",colsToRead);
        CSVFileUtil.updateColumnsToCSV(System.getProperty("user.dir")+"/test data/csv files/SB-1505202514_P1005 3.csv",colsToRead,colsDataToWrite);
    }

    @Test(enabled = false)
    public void updateSepificColsByHeader(){
        List<String> colsToRead = Arrays.asList("Well","Well Position","Sample Name");
        List<List<String>> colsDataToWrite = CSVFileUtil.readColumnsFromCSV(System.getProperty("user.dir")+"/test data/csv files/SB-1505202514_P1005 1.csv",colsToRead,6);
        CSVFileUtil.updateColumnsToCSV(System.getProperty("user.dir")+"/test data/csv files/SB-1505202514_P1005 11.csv",colsToRead,colsDataToWrite,14);
    }

    @Test(enabled = false)
    public void filterByRowsValue(){
        List<String[]> rows = CSVFileUtil.filterRowsFromCSV(System.getProperty("user.dir")+"/test data/csv files/SB-1505202514_P1005 2.csv","Sample","TRA100291");
        for(String[] row:rows){
            System.out.println(Arrays.toString(row));
        }
    }

    @Test(enabled = false)
    public void filterByColValue(){
        List<String> rows = CSVFileUtil.filterColumnFromCSV(System.getProperty("user.dir")+"/test data/csv files/SB-1505202514_P1005 2.csv","Sample","TRA100291");
        for(String row:rows){
            System.out.println(row);
        }
    }

    @Test(enabled = true)
    public void filterByRowsValueInclude() {
        List<String> colsToIncludeInRows = Arrays.asList("Sample", "Target", "Cq");
        String filePath = System.getProperty("user.dir") + "/test data/csv files/SB-1505202514_P1005 11.csv";
        List<List<String>> rows = CSVFileUtil.filterRowsFromCSV(
                filePath,
                "Sample",
                "TRA100291",
                colsToIncludeInRows
        );
        for (List<String> row : rows) {
            System.out.println(row);
        }
    }


}
