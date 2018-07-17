package sample.model;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class ExcelWorker {


    public static void exportParts(ArrayList<Part> parts, String path, String name){
        if(!path.matches("/*\\.txt/")) {
            path +=".xlsx";
        }
        XSSFWorkbook book = new XSSFWorkbook();
        XSSFSheet sheet = book.createSheet(name);
        int rownum = 0;
        Row row;
        Cell cell;
        row = sheet.createRow(rownum);
        cell = row.createCell(0);
        cell.setCellValue("Название");
        cell = row.createCell(1);
        cell.setCellValue("Автомобили с которыми совместимо");
        cell = row.createCell(2);
        cell.setCellValue("Количество");
        cell = row.createCell(3);
        cell.setCellValue("Цена закупки");
        cell = row.createCell(4);
        cell.setCellValue("Сумма закупки");
        cell = row.createCell(5);
        cell.setCellValue("Цена продажи");
        cell = row.createCell(6);
        cell.setCellValue("Сумма продажи");
        rownum++;
        for(int i =0; i<parts.size(); i++, rownum++){
            row = sheet.createRow(rownum);
            cell = row.createCell(0);
            cell.setCellValue(parts.get(i).getName());
            cell = row.createCell(1);
            cell.setCellValue(parts.get(i).getCompat());
            cell = row.createCell(2);
            cell.setCellValue(parts.get(i).getCount());
            cell = row.createCell(3);
            cell.setCellValue(parts.get(i).getByprice());
            cell = row.createCell(4);
            cell.setCellValue(parts.get(i).getCount()*parts.get(i).getByprice());
            cell = row.createCell(5);
            cell.setCellValue(parts.get(i).getPrice());
            cell = row.createCell(6);
            cell.setCellValue(parts.get(i).getCount()*parts.get(i).getPrice());
        }
        try {
            FileOutputStream out = new FileOutputStream(new File(path));
            book.write(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Part> parse(String name) {
        ArrayList<Part> result = new ArrayList<>();
        InputStream in = null;
        XSSFWorkbook wb = null;
        try{
            in = new FileInputStream(name);
            wb = new XSSFWorkbook(in);
        }catch(IOException e){
            e.printStackTrace();
        }

        Sheet sheet = wb.getSheetAt(0);
        Iterator<Row> it = sheet.iterator();
        while (it.hasNext())
        {
            Row row = it.next();
            if(row.getRowNum() == 0) continue;
            if(row.getCell(0)==null) continue;
            Iterator<Cell> cells = row.iterator();
            Part tmp = new Part();
            while (cells.hasNext()) {
                Cell cell = cells.next();
                String data = "";
                switch (cell.getCellType()){
                    case Cell.CELL_TYPE_STRING: data=cell.getStringCellValue(); break;
                    case Cell.CELL_TYPE_NUMERIC: data=cell.getNumericCellValue()+""; break;
                }
                switch (cell.getColumnIndex()) {
                    case 0:
                        tmp.setName(data);
                        break;
                    case 1:
                        tmp.setCompat(data);
                        break;
                    case 2:
                        tmp.setCount(Double.valueOf(data).intValue());
                        break;
                    case 3:
                        tmp.setByprice(Double.valueOf(data));
                        break;
                    case 5:
                        tmp.setPrice(Double.valueOf(data));
                        break;
                    default: continue;
                }
            }
            result.add(tmp);
        }
        return result;
    }
}
