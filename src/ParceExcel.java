import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class ParceExcel {


    public static void main(String[] args)
    {
        String ret = parse(System.getProperty("user.home")+ File.separator+"Запчасти.xls");
        System.out.println(ret);
    }

    public static String parse(String name) {
        String result = "";
        InputStream in = null;
        HSSFWorkbook wb = null;
        try{
            in = new FileInputStream(name);
            wb = new HSSFWorkbook(in);
        }catch(IOException e){
            e.printStackTrace();
        }

        Sheet sheet = wb.getSheetAt(0);
        Iterator<Row> it = sheet.iterator();
        while (it.hasNext())
        {
            Row row = it.next();
            Iterator<Cell> cells = row.iterator();
            while (cells.hasNext()) {
                Cell cell = cells.next();
                cell.getColumnIndex();
                switch (cell.getCellType()){
                    case Cell.CELL_TYPE_STRING: result+=cell.getStringCellValue()+" "; break;
                    case Cell.CELL_TYPE_NUMERIC: result+=cell.getNumericCellValue()+" "; break;
                }
            }
            result+="\n";
        }
        return result;
    }
}
