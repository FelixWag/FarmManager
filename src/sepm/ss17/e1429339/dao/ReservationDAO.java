package sepm.ss17.e1429339.dao;

import sepm.ss17.e1429339.entities.BoxRes;
import sepm.ss17.e1429339.entities.Reservation;
import sepm.ss17.e1429339.exceptions.ReservationDAOException;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface ReservationDAO {

    /**
     * Stores a reservation in the database
     * @param r the reservation which should be saved
     * @return the reservation which is stored in the database
     * @throws ReservationDAOException if SQL-Exception occurs
     */
    public Reservation save(Reservation r) throws ReservationDAOException;

    /**
     * Deletes a given reservation
     * @param r the reservation which should be deleted
     * @return if the deletion was successful
     * @throws ReservationDAOException if SQL-Exception occurs
     */
    public boolean delete(Reservation r) throws ReservationDAOException;

    /**
     * Searches all reservations
     * @return a list of all reservations
     * @throws ReservationDAOException if SQL-Exception occurs
     */
    public List<Reservation> searchall() throws ReservationDAOException;

    /**
     * Searches the reservation with the given id
     * @param id the Reservation id
     * @return the reservation which is searched
     * @throws ReservationDAOException if SQL-Exception occurs
     */
    public Reservation getReservation(long id) throws ReservationDAOException;

    /**
     * Updates a box in the database with the given values of the reservation
     * @param r the reservation with the values
     * @return if the update was successful
     * @throws ReservationDAOException if SQL-Exception occurs
     */
    public boolean update(Reservation r) throws ReservationDAOException;

    /**
     * Searches all boxes in the given time range of the reservation
     * @param r the reservation with the time range
     * @return a list of all reservations in the time range
     * @throws ReservationDAOException if SQL-Exception occurs
     */
    public List<Reservation> search(Reservation r) throws ReservationDAOException;

    /**
     * Checks for a given reservation if the boxes in the reservation are free at the time of the Reservation
     * @param r the reservation
     * @return a list of all reservations with the boxes of the parameter-reservation
     * @throws ReservationDAOException if SQL-Exception occurs
     */
    public List<Reservation> checkAvailable(Reservation r) throws ReservationDAOException;

    /**
     * Checks if a box is reserved in the given time range
     * @param box_id the box ID
     * @param from the startdate
     * @param to the enddate
     * @return the number of reservations in the given range
     * @throws ReservationDAOException if SQL-Exception occurs
     */
    public int boxResCheckAvailable(int box_id, Date from, Date to) throws ReservationDAOException;

    /**
     * This method returns a List from the Mappingtable BoxRes corresponding to the Reservation r
     * @param r the Reservation
     * @return a list of BoxReservation to the reservation r
     * @throws ReservationDAOException if SQL-Exception occurs
     */
    public List<BoxRes> getBoxReservation(Reservation r) throws ReservationDAOException;

    /**
     * This method inserts a BoxReservation into the table box_res
     * @param br the BoxReservation which should be inserted
     * @return the inserted BoxReservation
     * @throws ReservationDAOException if SQL-Exception occurs
     */
    public BoxRes saveBoxRes(BoxRes br) throws ReservationDAOException;

    /**
     * This method updates a price of a BoxReservation
     * @param br the BoxReservation with the new price
     * @return if the update was successful
     * @throws ReservationDAOException if SQL-Exception occurs
     */
    public boolean updateBoxRes(BoxRes br) throws ReservationDAOException;

    /**
     * This method sets Reservation isDeleted to true
     * @param r the reservation
     * @return if the update was successful
     * @throws ReservationDAOException if SQL-Exception occurs
     */
    public boolean pickUpRes(Reservation r) throws ReservationDAOException;

    /**
     * This Method gets the reservation to the Box_id in the time interval
     * @param box_id the box ID
     * @param from the startdate
     * @param to the enddate
     * @return a list of Reservations
     * @throws ReservationDAOException if SQL-Exception occurs
     */
    public List<Reservation> getReservationToBoxId(int box_id, Date from, Date to) throws ReservationDAOException;

    /**
     * This method returns a List of BoxReservation to the specific boxID in the given date range
     * @param box_id the box ID
     * @param from startdate
     * @param to enddate
     * @return A list of all BoxReservations
     * @throws ReservationDAOException if SQL-Exception occurs
     */
    public List<BoxRes> getBoxResId(int box_id, Date from, Date to) throws ReservationDAOException;


    /**
     * This method deletes a BoxReservation to the corresponding Reservation and Id
     * @param br the BoxReservation which should be deleted
     * @return if the deletion was successful
     * @throws ReservationDAOException if SQL-Exception occurs
     */
    public boolean deleteOnlyBoxRes(BoxRes br) throws ReservationDAOException;

}
