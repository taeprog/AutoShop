package sample.model;

public class Vendor {
    private int id;
    private String name;

    public Vendor(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Vendor() {
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

}
