package sample.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CarsModel {

    private DataBaseWorker db;

    public CarsModel() {
        db = DataBaseWorker.context();
    }

    public ArrayList<Vendor> getVendors() {
        ArrayList<Vendor> ret = new ArrayList<>();
        try {
            ResultSet res = db.getList("SELECT * FROM vendors ORDER BY id DESC");
            while (res.next())
            {
                ret.add(new Vendor(res.getInt("id"), res.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public ArrayList<Car> getCars(int vendorId){
        ArrayList<Car> ret = new ArrayList<>();
        try {
            ResultSet res = db.getList("SELECT * FROM models WHERE vendor_id="+vendorId);
            while (res.next())
            {
                Car car = new Car();
                car.setId(res.getInt("id"));
                car.setModel(res.getString("name"));
                ret.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
