package sepm.ss17.e1429339.entities;

/**
 * Created by Felix on 14.03.2017.
 */
public class Box {

    private Integer box_id;
    private String photo;
    private Boolean window;
    private String litter;
    private Boolean inside;
    private Float box_size;
    private String area;
    private Boolean isDeleted;
    private Float daily_rate;

    public Box(String photo, Boolean window, String litter, Boolean inside, Float box_size, String area, Boolean isDeleted, Float daily_rate) {
        this.photo = photo;
        this.window = window;
        this.litter = litter;
        this.inside = inside;
        this.box_size = box_size;
        this.area = area;
        this.isDeleted = isDeleted;
        this.daily_rate = daily_rate;
    }
    public Box(){
    }

    public Integer getBox_id() {
        return box_id;
    }

    public void setBox_id(Integer box_id) {
        this.box_id = box_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Boolean isWindow() {
        return window;
    }

    public void setWindow(Boolean window) {
        this.window = window;
    }

    public String getLitter() {
        return litter;
    }

    public void setLitter(String litter) {
        this.litter = litter;
    }

    public Boolean isInside() {
        return inside;
    }

    public void setInside(Boolean inside) {
        this.inside = inside;
    }

    public Float getBox_size() {
        return box_size;
    }

    public void setBox_size(Float box_size) {
        this.box_size = box_size;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Float getDaily_rate() {
        return daily_rate;
    }

    public void setDaily_rate(Float daily_rate) {
        this.daily_rate = daily_rate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Box)) {
            return false;
        }
        final Box other = (Box) obj;
        if ((this.box_id == null) ? (other.getBox_id() != null) : this.box_id.intValue()!=other.getBox_id().intValue()) {
            return false;
        }
        if ((this.window == null) ? (other.isWindow() != null) : this.window!=other.isWindow()) {
            return false;
        }
        if ((this.photo == null) ? (other.getPhoto() != null) : !this.getPhoto().equals(other.getPhoto())) {
            return false;
        }
        if ((this.litter == null) ? (other.getLitter() != null) : !this.litter.equals(other.getLitter())) {
            return false;
        }
        if ((this.inside == null) ? (other.isInside() != null) : this.inside!=other.isInside()) {
            return false;
        }
        if ((this.box_size == null) ? (other.getBox_size() != null) : this.box_size.floatValue()!=other.getBox_size().floatValue()) {
            return false;
        }
        if ((this.area == null) ? (other.getArea() != null) : !this.area.equals(other.getArea())) {
            return false;
        }
        if ((this.daily_rate == null) ? (other.getDaily_rate() != null) : this.daily_rate.floatValue()!=other.getDaily_rate().floatValue()) {
            return false;
        }
        if ((this.isDeleted == null) ? (other.isDeleted() != null) : this.isDeleted()!=other.isDeleted()) {
            return false;
        }
        return true;
    }

}
