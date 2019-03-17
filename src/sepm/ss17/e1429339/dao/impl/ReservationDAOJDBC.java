package sepm.ss17.e1429339.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sepm.ss17.e1429339.dao.ReservationDAO;
import sepm.ss17.e1429339.entities.BoxRes;
import sepm.ss17.e1429339.entities.Reservation;
import sepm.ss17.e1429339.exceptions.ReservationDAOException;
import sepm.ss17.e1429339.util.DBUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;


public class ReservationDAOJDBC implements ReservationDAO {

    final static Logger LOGGER = LoggerFactory.getLogger(ReservationDAOJDBC.class);

    private String reservationSearchAllString = "SELECT * FROM reservation WHERE res_isDeleted=FALSE";
    private String reservationSearchString = "SELECT * FROM reservation WHERE res_from <= ? AND res_to >= ? AND res_isDeleted=FALSE";
    private String reservationUpdateString = "UPDATE reservation SET customer=?, res_from=?,res_to=? WHERE res_id=?";

    private String boxResUpdateString = "UPDATE box_res SET price=? WHERE res_id=? AND box_id=?";

    private String reservationCheckAvailableString = "SELECT * FROM reservation r1 NATURAL JOIN box_res WHERE res_from <=? AND res_to >=? AND res_isDeleted=FALSE AND box_id IN (SELECT box_id FROM reservation r2 NATURAL JOIN box_res WHERE  r2.res_id=?);";

    private String boxResCheckAvailableString = "SELECT COUNT(*) AS boxResCount FROM reservation NATURAL JOIN box_res WHERE res_from <=? AND res_to >=? AND box_id=? AND res_isDeleted=FALSE";

    private String reservationGetString = "SELECT * FROM reservation r WHERE r.res_id=?";

    private String reservationInsertString = "INSERT INTO reservation (customer, res_from, res_to, res_isDeleted) VALUES (?,?,?,?)";
    private String boxResInsertString = "INSERT INTO box_res (box_id, res_id, price, horse) VALUES (?,?,?,?)";


    private String reservationDeleteString = "DELETE FROM reservation WHERE res_id=?";
    private String boxResDeleteString = "DELETE FROM box_res WHERE res_id=?";
    private String resPickUpString = "UPDATE reservation SET res_isDeleted=TRUE WHERE res_id=?";

    private String boxReservationString = "SELECT * FROM reservation r NATURAL JOIN box_res WHERE r.res_id=?";

    /**This query is for getting the date of the corresponding box_id*/
    private String dateBoxIdString = "SELECT * FROM reservation NATURAL JOIN box_res WHERE res_from <=? AND res_to >=? AND box_id=?";

    /**This query returns the BoxRes to a specific BoxId and Date range*/
    private String boxReservationIdString = "SELECT * FROM reservation NATURAL JOIN box_res WHERE res_from <=? AND res_to >=? AND box_id=?";

    /**This query deletes the BoxRes with box_id and res_id*/
    private String boxResOnlyDeleteString = "DELETE FROM box_res WHERE res_id=? AND box_id=?";


    @Override
    public Reservation save(Reservation r) throws ReservationDAOException {

        if(r==null || r.getRes_from()==null || r.getRes_to()==null ||
                 r.getRes_isDeleted()==null|| r.getCustomer()==null || r.getRes_from().after(r.getRes_to())
                || r.getCustomer().equals("")){
            throw new IllegalArgumentException();
        }

        boolean free=true;
        List<Reservation> reservationList = checkAvailable(r);
        for (Reservation cr : reservationList){
            if(cr.getRes_id()!=r.getRes_id()){
                free=false;
            }
        }
        if(free) {
            try {
                Reservation temp=new Reservation();
                PreparedStatement ps = DBUtil.getConnection().prepareStatement(reservationInsertString, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, r.getCustomer());
                ps.setDate(2, new java.sql.Date(r.getRes_from().getTime()));
                ps.setDate(3, new java.sql.Date(r.getRes_to().getTime()));
                ps.setBoolean(4, r.getRes_isDeleted());

                ps.executeUpdate();

                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    r.setRes_id(generatedKeys.getLong(1));

                    LOGGER.info("Saving Reservation with id",r.getRes_id());

                    temp=r;
                }

                generatedKeys.close();
                ps.close();
                return temp;
            } catch (SQLException e) {
                LOGGER.error("Error with SQL",e);
                throw new ReservationDAOException();
            }
        }

        return new Reservation();

    }

    @Override
    public BoxRes saveBoxRes(BoxRes br) throws ReservationDAOException {

        if(br==null || br.getBox_id()==null || br.getPrice()<=0 || br.getBox_id()<0 || br.getRes_id()<0 || br.getHorse().equals("")){
            throw new IllegalArgumentException();
        }

        try{
            PreparedStatement ps = DBUtil.getConnection().prepareStatement(boxResInsertString);
            ps.setInt(1, br.getBox_id());
            ps.setLong(2, br.getRes_id());
            ps.setFloat(3, br.getPrice());
            ps.setString(4, br.getHorse());

            LOGGER.info("Saving BoxRes with Box ID " + br.getBox_id() + " ,ReservationID" + br.getRes_id());

            if(ps.executeUpdate() == 1){
                ps.close();
                return br;
            }else{
                return null;
            }

        } catch (SQLException e) {
            LOGGER.error("Error with SQL",e);
            throw new ReservationDAOException();
        }

    }


    @Override
    public boolean updateBoxRes(BoxRes br) throws ReservationDAOException {

        if(br==null || br.getPrice()<=0 || br.getBox_id()<0 || br.getRes_id()<0
                || br.getBox_id()==null || br.getHorse().equals("")){
            throw new IllegalArgumentException();
        }

        int success;
        try {
            PreparedStatement ps = DBUtil.getConnection().prepareStatement(boxResUpdateString);

            ps.setFloat(1,br.getPrice());
            ps.setLong(2,br.getRes_id());
            ps.setInt(3,br.getBox_id());

            LOGGER.info("Updating BoxRes with Reservation ID "+br.getRes_id()+ "Box ID " + br.getBox_id()+ " price" + br.getPrice());

            success = ps.executeUpdate();


            if(success>=1){
                ps.close();
                return true;
            }else{
                ps.close();
                return false;
            }

        } catch (SQLException e) {
            LOGGER.error("Error with SQL",e);
            throw new ReservationDAOException();
        }
    }

    @Override
    public boolean pickUpRes(Reservation r) throws ReservationDAOException {

        if(r==null || r.getRes_id()<0){
            throw new IllegalArgumentException();
        }
        int success;
        try {
            PreparedStatement ps = DBUtil.getConnection().prepareStatement(resPickUpString);
            ps.setLong(1,r.getRes_id());

            LOGGER.info("Pick up Horses with Reservation ID " + r.getRes_id());

            success = ps.executeUpdate();


            if(success == 1){
                ps.close();
                return true;
            }else{
                ps.close();
                return false;
            }

        } catch (SQLException e) {
            LOGGER.error("Error with SQL",e);
            throw new ReservationDAOException();
        }

    }

    @Override
    public List<Reservation> getReservationToBoxId(int box_id, Date from, Date to) throws ReservationDAOException {

        if(box_id<0 || from.after(to)){
            throw new IllegalArgumentException();
        }

        List<Reservation> listReservation = new LinkedList<Reservation>();
        try {
            PreparedStatement ps = DBUtil.getConnection().prepareStatement(dateBoxIdString);

            ps.setDate(2,new java.sql.Date(from.getTime()));
            ps.setDate(1,new java.sql.Date(to.getTime()));
            ps.setLong(3,box_id);

            LOGGER.info("Returning Reservations to Box-ID" + box_id + " from:" + from + " to:" + to);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Reservation ret = new Reservation(rs.getString(2),new java.util.Date(rs.getDate(3).getTime()),new java.util.Date(rs.getDate(4).getTime()), rs.getBoolean(5));
                ret.setRes_id(rs.getLong(1));
                listReservation.add(ret);
            }

            return listReservation;

        } catch (SQLException e) {
            LOGGER.error("Error with SQL",e);
            throw new ReservationDAOException();
        }
    }

    @Override
    public List<BoxRes> getBoxResId(int box_id, Date from, Date to) throws ReservationDAOException {

        if(box_id<0 || from.after(to) || from==null || to==null){
            throw new IllegalArgumentException();
        }

        List<BoxRes> boxResList = new LinkedList<BoxRes>();
        try {
            PreparedStatement ps = DBUtil.getConnection().prepareStatement(boxReservationIdString);

            ps.setDate(2,new java.sql.Date(from.getTime()));
            ps.setDate(1,new java.sql.Date(to.getTime()));
            ps.setInt(3,box_id);

            LOGGER.info("Returning BoxRes to BoxID:" + box_id + "from: " + from + "to: " + to);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                BoxRes br = new BoxRes(rs.getFloat(7),rs.getString(8));
                br.setBox_id(rs.getInt(6));
                br.setRes_id(rs.getInt(1));
                boxResList.add(br);
            }

            return boxResList;

        } catch (SQLException e) {
            LOGGER.error("Error with SQL",e);
            throw new ReservationDAOException();
        }
    }

    @Override
    public boolean deleteOnlyBoxRes(BoxRes br) throws ReservationDAOException {

        if(br==null || br.getBox_id()==null || br.getBox_id()<0 || br.getPrice()<0){
            throw new IllegalArgumentException();
        }

        int success;
        try {
            PreparedStatement ps1 = DBUtil.getConnection().prepareStatement(boxResOnlyDeleteString);
            ps1.setLong(1,br.getRes_id());
            ps1.setInt(2,br.getBox_id());

            LOGGER.info("Deleting BoxRes with Box_id:" + br.getBox_id() + " Res_id:" + br.getRes_id());

            success = ps1.executeUpdate();

            if(success >= 1){
                ps1.close();
                return true;
            }else{
                ps1.close();
                return false;
            }

        } catch (SQLException e) {
            LOGGER.error("Error with SQL",e);
            throw new ReservationDAOException();
        }
    }

    @Override
    public boolean delete(Reservation r) throws ReservationDAOException {

        if(r==null || r.getRes_id()<0){
            throw new IllegalArgumentException();
        }

        int success;
        try {
            PreparedStatement ps1 = DBUtil.getConnection().prepareStatement(reservationDeleteString);
            PreparedStatement ps2 = DBUtil.getConnection().prepareStatement(boxResDeleteString);
            ps1.setLong(1,r.getRes_id());
            ps2.setLong(1,r.getRes_id());

            LOGGER.info("Deleting Reservation and corresponding BoxRes with ReservationID:" + r.getRes_id());

            success = ps2.executeUpdate();
            success += ps1.executeUpdate();


            if(success > 1){
                ps1.close();
                ps2.close();
                return true;
            }else{
                ps1.close();
                ps2.close();
                return false;
            }

        } catch (SQLException e) {
            LOGGER.error("Error with SQL",e);
            throw new ReservationDAOException();
        }
    }

    @Override
    public boolean update(Reservation r) throws ReservationDAOException {

        if(r==null || r.getRes_from()==null || r.getRes_to()==null ||
                r.getRes_isDeleted()==null|| r.getCustomer()==null || r.getRes_from().after(r.getRes_to()) || r.getRes_id()<0
                || r.getCustomer().equals("")){
            throw new IllegalArgumentException();
        }

        boolean free=true;
        List<Reservation> reservationList = checkAvailable(r);
        for (Reservation cr : reservationList){
            if(cr.getRes_id()!=r.getRes_id()){
                free=false;
            }
        }
        if(free) {
            int success;
            try {
                PreparedStatement ps = DBUtil.getConnection().prepareStatement(reservationUpdateString);

                ps.setString(1,r.getCustomer());
                ps.setDate(2,new java.sql.Date(r.getRes_from().getTime()));
                ps.setDate(3,new java.sql.Date(r.getRes_to().getTime()));
                ps.setLong(4, r.getRes_id());

                LOGGER.info("Updating Reservation with Res ID: " + r.getRes_id());

                success = ps.executeUpdate();

                if(success==1){
                    ps.close();
                    return true;
                }else{
                    ps.close();
                    return false;
                }

            } catch (SQLException e) {
                LOGGER.error("Error with SQL",e);
                throw new ReservationDAOException();
            }
        }
        return false;
    }

    @Override
    public List<Reservation> checkAvailable(Reservation r) throws ReservationDAOException {

        if(r==null || r.getRes_from().after(r.getRes_to()) || r.getRes_id()<0 || r.getRes_from()==null
                || r.getRes_to()==null){
            throw new IllegalArgumentException();
        }

        List<Reservation> listReservation = new LinkedList<Reservation>();
        try {
            PreparedStatement ps = DBUtil.getConnection().prepareStatement(reservationCheckAvailableString);

            ps.setDate(2,new java.sql.Date(r.getRes_from().getTime()));
            ps.setDate(1,new java.sql.Date(r.getRes_to().getTime()));
            ps.setLong(3,r.getRes_id());
            LOGGER.info("Check availability from Reservation ID." + r.getRes_id());
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Reservation ret = new Reservation(rs.getString(2),new java.util.Date(rs.getDate(3).getTime()),new java.util.Date(rs.getDate(4).getTime()), rs.getBoolean(5));
                ret.setRes_id(rs.getLong(1));
                listReservation.add(ret);
            }


            return listReservation;

        } catch (SQLException e) {
            LOGGER.error("Error with SQL",e);
            throw new ReservationDAOException();
        }
    }

    @Override
    public int boxResCheckAvailable(int box_id, Date from, Date to) throws ReservationDAOException {

        if(box_id<0 || from==null || to==null || from.after(to)){
            throw new IllegalArgumentException();
        }

        try {
            PreparedStatement ps = DBUtil.getConnection().prepareStatement(boxResCheckAvailableString);

            ps.setDate(2,new java.sql.Date(from.getTime()));
            ps.setDate(1,new java.sql.Date(to.getTime()));
            ps.setInt(3,box_id);

            LOGGER.info("BoxRes check available with box_id: "+ box_id + " from: "+ from + " to: "+to);

            ResultSet rs = ps.executeQuery();
            int count=0;

            if(rs.next()){
                return rs.getInt(1);
            }

            return 0;

        } catch (SQLException e) {
            LOGGER.error("Error with SQL",e);
            throw new ReservationDAOException();
        }
    }


    @Override
    public List<Reservation> searchall() throws ReservationDAOException {

        List<Reservation> listReservation = new LinkedList<Reservation>();
        try {
            PreparedStatement ps = DBUtil.getConnection().prepareStatement(reservationSearchAllString);

            LOGGER.info("Seacrhing all Reservations");

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Reservation r = new Reservation(rs.getString(2),new java.util.Date(rs.getDate(3).getTime()),new java.util.Date(rs.getDate(4).getTime()), rs.getBoolean(5));
                r.setRes_id(rs.getLong(1));
                listReservation.add(r);
            }
            ps.close();
            rs.close();

            return listReservation;

        } catch (SQLException e) {
            LOGGER.error("Error with SQL",e);
            throw new ReservationDAOException();
        }

    }

    @Override
    public Reservation getReservation(long id) throws ReservationDAOException {

        if(id<0){
            throw new IllegalArgumentException();
        }

        try {
            Reservation r = new Reservation();
            PreparedStatement ps = DBUtil.getConnection().prepareStatement(reservationGetString);
            ps.setLong(1,id);

            LOGGER.info("Seachring for reservation with ID:" + id);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                r.setRes_id(rs.getLong(1));
                r.setCustomer(rs.getString(2));
                r.setRes_from(new java.util.Date(rs.getDate(3).getTime()));
                r.setRes_to(new java.util.Date(rs.getDate(4).getTime()));
                r.setRes_isDeleted(rs.getBoolean(5));
                rs.close();
                ps.close();

                return r;
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            LOGGER.error("Error with SQL",e);
            throw new ReservationDAOException();
        }

        return null;
    }

    @Override
    public List<Reservation> search(Reservation r) throws ReservationDAOException {

        if(r==null || r.getRes_from().after(r.getRes_to()) || r.getRes_from()==null || r.getRes_to()==null){
            throw new IllegalArgumentException();
        }

        List<Reservation> listReservation = new LinkedList<Reservation>();
        try {
            PreparedStatement ps = DBUtil.getConnection().prepareStatement(reservationSearchString);
            ps.setDate(2,new java.sql.Date(r.getRes_from().getTime()));
            ps.setDate(1,new java.sql.Date(r.getRes_to().getTime()));

            LOGGER.info("Seachring reservations from: " + r.getRes_from()+" to:" + r.getRes_to());

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Reservation result = new Reservation(rs.getString(2), new java.util.Date(rs.getDate(3).getTime()), new java.util.Date(rs.getDate(4).getTime()), rs.getBoolean(5));
                result.setRes_id(rs.getLong(1));
                listReservation.add(result);
            }
            ps.close();
            rs.close();

            return listReservation;

        } catch (SQLException e) {
            LOGGER.error("Error with SQL",e);
            throw new ReservationDAOException();
        }

    }

    /**This method returns a List from the Mappingtable BoxRes corresponding to the Reservation r*/
    public List<BoxRes> getBoxReservation(Reservation r) throws ReservationDAOException {

        if(r==null || r.getRes_id()<0){
            throw new IllegalArgumentException();
        }

        List<BoxRes> listBoxRes = new LinkedList<BoxRes>();
        Map<Reservation,List<BoxRes>> mapBoxRes= new TreeMap<Reservation,List<BoxRes>>();

        try {
            PreparedStatement ps = DBUtil.getConnection().prepareStatement(boxReservationString);
            ps.setLong(1,r.getRes_id());

            LOGGER.info("Searching all BoxRes to ReservationID: " + r.getRes_id());

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                BoxRes resultBoxRes = new BoxRes(rs.getFloat(7), rs.getString(8));
                resultBoxRes.setBox_id(rs.getInt(6));
                resultBoxRes.setRes_id(rs.getLong(1));

                listBoxRes.add(resultBoxRes);

            }

            ps.close();
            rs.close();

            return listBoxRes;

        } catch (SQLException e) {
            LOGGER.error("Error with SQL",e);
            throw new ReservationDAOException();
        }

    }

}
