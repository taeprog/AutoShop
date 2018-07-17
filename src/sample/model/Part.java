package sample.model;

import java.util.ArrayList;

public class Part {

    protected int id;
    protected String name;
    protected Double price;
    protected Double byprice;
    protected int count;
    protected String compat;
    protected String type;

    public Part(int id, String name, int count, Double price, Double byprice, String compat, String type){
        this.id = id;
        this.name = name;
        this.count = count;
        this.price = price;
        this.byprice = byprice;
        this.compat = compat;
        this.type = type;
    }

    public Part(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCompat() {
        return compat;
    }

    public void setCompat(String compat) {
        this.compat = compat;
    }

    public Double getByprice() {
        return byprice;
    }

    public void setByprice(Double byprice) {
        this.byprice = byprice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
