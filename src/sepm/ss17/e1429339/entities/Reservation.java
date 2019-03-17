package sepm.ss17.e1429339.entities;


import java.time.LocalDate;
import java.util.Date;

public class Reservation {

    private long res_id;
    private String customer;
    private Date res_from;
    private Date res_to;
    private boolean res_isDeleted;


    public Reservation(String customer, Date res_from, Date res_to, boolean res_isDeleted){
        this.customer=customer;
        this.res_from=res_from;
        this.res_to=res_to;
        this.res_isDeleted=res_isDeleted;
    }

    public Reservation(){
    }


    public long getRes_id() {
        return res_id;
    }

    public void setRes_id(long res_id) {
        this.res_id = res_id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Date getRes_from() {return res_from;}

    public void setRes_from(Date res_from) {
        this.res_from = res_from;
    }

    public Date getRes_to() {
        return res_to;
    }

    public void setRes_to(Date res_to) {
        this.res_to = res_to;
    }

    public Boolean getRes_isDeleted() {
        return res_isDeleted;
    }

    public void setRes_isDeleted(Boolean res_isDeleted) {
        this.res_isDeleted = res_isDeleted;
    }

}
