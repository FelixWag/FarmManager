package sepm.ss17.e1429339.entities;


public class BoxRes {

    private Integer box_id;
    private long res_id;
    private float price;
    private String horse;

    public BoxRes(float price, String horse){
        this.price = price;
        this.horse = horse;
    }

    public BoxRes(){

    }

    public Integer getBox_id() {
        return box_id;
    }

    public void setBox_id(Integer box_id) {
        this.box_id = box_id;
    }

    public long getRes_id() {
        return res_id;
    }

    public void setRes_id(long res_id) {
        this.res_id = res_id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getHorse() {
        return horse;
    }

    public void setHorse(String horse) {
        this.horse = horse;
    }
}
