package com.quiroz.mypayments.helper;

import com.quiroz.mypayments.enums.Month;
import com.quiroz.mypayments.exception.NotFoundException;
import com.quiroz.mypayments.models.ExpensiveModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class HelperFile {

    public static boolean hasExcelFormat(MultipartFile file){
        String contentType = file.getContentType();
        if (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    public static Map<String, Set<String>> getSettingsFromFile(InputStream inputStream) {

        Map<String, Set<String>> settingMap = new HashMap<>();

        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        XSSFSheet sheet = workbook.getSheet("Settings");//TODO validate

        int rowNumber = 0;

        for (Row row : sheet) {
            if (rowNumber == 0) {
                rowNumber++;
                continue;
            }
            //Every row has columns, get the column iterator and iterate over them
            Iterator<Cell> cellIterator = row.cellIterator();
            String key = row.getCell(0).getStringCellValue();
            String value = row.getCell(1).getStringCellValue();
            if (!settingMap.containsKey(key)) {
                Set<String> elements = new HashSet<>();
                elements.add(value);
                settingMap.put(key, elements);
            } else {
                Set<String> elements = settingMap.get(key);
                elements.add(value);
                settingMap.put(key, elements);
            }

        }
        return settingMap;
    }

    public static List<ExpensiveModel> getExpensesByMonth(Month month, InputStream inputStream) {
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        XSSFSheet sheet = getSheetByMonthAndValidate(workbook, month);

        List<ExpensiveModel> expenses = new LinkedList<>();
        int rowNumber = 0;
        for (Row row : sheet) {
            rowNumber++;
            if(rowNumber > 21) {
                int colNumber = 0;
                ExpensiveModel expense = new ExpensiveModel();
                for (Cell cell : row) {

                    if (colNumber <= 6) {
                        System.out.println(cell);

                        switch (cell.getColumnIndex()) {
                            case 0 -> expense.setId((int)(cell.getNumericCellValue()));
                            case 1 -> expense.setCategory(cell.getStringCellValue());
                            case 2 -> expense.setSubCategory(cell.getStringCellValue());
                            case 3 -> expense.setDescription(cell.getStringCellValue());
                            case 4 ->
                                expense.setAmount(BigDecimal.valueOf(cell.getNumericCellValue()));
                            case 5 -> expense.setDate(cell.getLocalDateTimeCellValue());
                            case 6 -> expense.setMonth(cell.getStringCellValue());
                            default -> throw new IllegalStateException("Unexpected value: " + cell.getColumnIndex());
                        }

                    }
                    colNumber++;
                }
                expenses.add(expense);
            }

        }

        return expenses;
    }

    public static Map<String, BigDecimal> getIncomeByMonth(Month month,
                                                           InputStream in){
        Map<String, BigDecimal> incomeMap = new HashMap<>();
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(in);
        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
        XSSFSheet sheet = getSheetByMonthAndValidate(workbook, month);

        int rowNumber = 0;
        for (Row row : sheet) {
            rowNumber++;
            if(rowNumber >= 9 && rowNumber <= 10) {
                String key = row.getCell(0).getStringCellValue();
                BigDecimal value = BigDecimal.valueOf(row.getCell(1).getNumericCellValue());
                System.out.println(key + "= " + value);
                incomeMap.put(key, value);
            }
        }
        return incomeMap;

    }

    private static XSSFSheet getSheetByMonthAndValidate(XSSFWorkbook workbook, Month month){
        XSSFSheet sheet = workbook.getSheet(month.getEnglishName());

        if(null == sheet) {
            throw new NotFoundException(String.format("Sheet not found for the month: %s", month.getEnglishName()));
        }
        return sheet;
    }

}
