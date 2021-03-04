package sample;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

public class Content {
    private static String addressesOfMails;
    private static LinkedList<String> groupMails = new LinkedList<>();

    //chon file chua du lieu de doc
    private static Workbook getWorkbook(InputStream inputStream, String excelFilePath) throws IOException {
        Workbook workbook = null;
        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }
        return workbook;
    }

    public static LinkedList<String> readFile(String path) throws Exception {
        //make data reset after read new exel file
        addressesOfMails = "";
        groupMails.clear();

        int count = 0; // count the number of emails
        //get file from path
        InputStream inputStream = new FileInputStream(new File(path));

        //get workbook( file xlsx chua emails)
        Workbook workbook = getWorkbook(inputStream, path);

        //get sheet
        Sheet sheet = workbook.getSheetAt(0); // lay du lieu o trang tinh thu 1(index: 0)

        //get all rows
        Iterator<Row> iterator = sheet.iterator();
            while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                while (cellIterator.hasNext()){
                    Cell cell = cellIterator.next();

                    if(cell.getCellType() == CellType.STRING){
                        if((cell.getStringCellValue().contains("@") && cell.getStringCellValue().contains(".com")) ||
                                (cell.getStringCellValue().contains("@") && cell.getStringCellValue().contains(".vn"))) { // kiem tra xem co la dia chi mail hay khong
                            count++;
                            addressesOfMails = addressesOfMails + cell.getStringCellValue() + ",";
                            if(count % 90 == 0) {
                                addressesOfMails = addressesOfMails.substring(0, addressesOfMails.length() - 1);
                                addressesOfMails = addressesOfMails + ",,";
                            }
                        }
                    }
                }
            }
        addressesOfMails = addressesOfMails.substring(0, addressesOfMails.length() - 1); // cat phan tu , cuoi chuoi
        workbook.close();
        inputStream.close();

        groupMails.addAll(Arrays.asList(addressesOfMails.split(",,")));
        return groupMails;
    }
}

