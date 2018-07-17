package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PartsModel {
    private DataBaseWorker db;

    public PartsModel() {
        db = DataBaseWorker.context();
    }

    public ArrayList<Part> getList() {
        ArrayList<Part> ret = new ArrayList<>();

        try{
             ResultSet res = db.getList("SELECT * FROM parts WHERE count>0");
            while (res.next())
            {
                Part part = new Part();
                part.setId(res.getInt("id"));
                part.setName(res.getString("name"));
                part.setCount(res.getInt("count"));
                part.setCompat(res.getString("compat"));
                part.setByprice(res.getDouble("byprice"));
                part.setPrice(res.getDouble("price"));
                ret.add(part);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public ArrayList<Part> getNeedToBuy() {
        ArrayList<Part> ret = new ArrayList<>();

        try{
            ResultSet res = db.getList("SELECT * FROM parts WHERE count<=1");
            while (res.next())
            {
                Part part = new Part();
                part.setId(res.getInt("id"));
                part.setName(res.getString("name"));
                part.setCount(res.getInt("count"));
                part.setCompat(res.getString("compat"));
                part.setByprice(res.getDouble("byprice"));
                part.setPrice(res.getDouble("price"));
                ret.add(part);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
    //public ArrayList<Part> search(String name) {
       // return search(name, null);
    //}

    public ArrayList<Part> search(String name, String vendor, String model) {
        ArrayList<Part> ret = new ArrayList<>();
        String nameValue = name.toLowerCase();
        String vendorValue = (vendor!=null)?vendor:"";
        try{
            ResultSet res =db.getList("SELECT * FROM parts WHERE search LIKE '%"+nameValue+"%' AND count >0 AND"
                    +" (compat LIKE '%"+vendorValue+"%' OR search LIKE '%"+vendorValue.toLowerCase()+"%')");
            while (res.next())
            {
                Part part = new Part();
                part.setId(res.getInt("id"));
                part.setName(res.getString("name"));
                part.setCount(res.getInt("count"));
                part.setCompat(res.getString("compat"));
                part.setPrice(res.getDouble("price"));
                ret.add(part);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public ArrayList<Check> getCheckList(){
        ArrayList<Check> ret = new ArrayList<>();
        try{
            ResultSet res =db.getList("SELECT SUM(count*price) as 'summ', datetime FROM sold GROUP BY datetime ORDER BY id DESC");
            while (res.next())
            {
                Check check = new Check();
                check.setDateTime(res.getString("datetime"));
                check.setSum(res.getDouble("summ"));
                ret.add(check);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public ArrayList<Part> getFullCheck(String datetime){
        ArrayList<Part> ret = new ArrayList<>();

        try{
            ResultSet res = db.getList("SELECT parts.name AS name, sold.count AS count, sold.price AS price FROM sold, parts  WHERE datetime='"+datetime+"' AND parts.id = part_id");
            while (res.next())
            {
                Part part = new Part();
                part.setName(res.getString("name"));
                part.setCount(res.getInt("count"));
                part.setPrice(res.getDouble("price"));
                ret.add(part);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }


    public void sell(ArrayList<Part> cart) {
        SimpleDateFormat sdfdate = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        for(int i=0; i<cart.size(); i++){
            try {
                db.execQuery("UPDATE parts SET count=count-"+cart.get(i).getCount()+" WHERE id="+cart.get(i).getId());

                db.execQuery("INSERT INTO sold (part_id, count, price, datetime) VALUES ("+
                        cart.get(i).getId()+", "+cart.get(i).getCount()+", "+cart.get(i).getPrice()+", '"+
                        sdfdate.format(Calendar.getInstance().getTime())+"')");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    public HashMap<String, Part> getNamesMap(String names) {
        HashMap<String, Part> ret = new HashMap<>();
        try {
            ResultSet res = db.getList("SELECT * FROM parts WHERE search in ("+names+")");
            while (res.next())
            {
                Part part = new Part();
                part.setId(res.getInt("id"));
                part.setName(res.getString("name"));
                part.setCount(res.getInt("count"));
                part.setPrice(res.getDouble("price"));
                part.setCompat(res.getString("compat"));
                part.setByprice(res.getDouble("byprice"));
                ret.put(res.getString("search").toLowerCase(), part);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void addParts(ArrayList<Part> parts) {
        String partsNames = "";
        for(int i=0; i<parts.size(); i++) {
            try{
                partsNames += "'"+parts.get(i).getName().toLowerCase()+((parts.get(i).getCompat() != null)?parts.get(i).getCompat().toLowerCase():"")+"',";
            }catch(Exception e){
                e.printStackTrace();
            }

        }
        partsNames = partsNames.substring(0, partsNames.length()-1);
        HashMap<String, Part> exists = this.getNamesMap(partsNames);
        for(int i=0; i<parts.size(); i++) {
            String partname = parts.get(i).getName().toLowerCase()+((parts.get(i).getCompat() != null)?parts.get(i).getCompat().toLowerCase():"");
            if(exists.containsKey(partname)) {
                try {
                    db.execQuery("UPDATE parts SET byprice="
                            +parts.get(i).getByprice()
                            +", price="+parts.get(i).getPrice()
                            +", count="+(exists.get(partname).getCount()+parts.get(i).getCount())
                            +" WHERE id="+exists.get(partname).getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    db.execQuery("INSERT INTO parts (name, count, price, byprice, search, compat) VALUES ('"+
                            parts.get(i).getName()+"', "+parts.get(i).getCount()+", "+
                            parts.get(i).getPrice()+", "+parts.get(i).getByprice()+", '"+partname+"', '"+
                            ((parts.get(i).getCompat() != null)?parts.get(i).getCompat().toLowerCase():"")+"')");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
