package sepm.ss17.e1429339.service;


import sepm.ss17.e1429339.dao.BoxDAO;
import sepm.ss17.e1429339.dao.ReservationDAO;
import sepm.ss17.e1429339.entities.BoxRes;
import sepm.ss17.e1429339.entities.Reservation;
import sepm.ss17.e1429339.exceptions.ReservationServiceException;
import sepm.ss17.e1429339.exceptions.ReservationServiceValidationException;
import sepm.ss17.e1429339.exceptions.ServiceException;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ReservationService {

    /**
     * Adds an reservation to the database
     * @param r the reservation
     * @return returns the reservation which is added
     * @throws ReservationServiceException if an exception in the persistence layer occurs
     * @throws ReservationServiceValidationException if parameter are invalid
     */
    public Reservation addReservation(Reservation r) throws ReservationServiceException, ReservationServiceValidationException;

    /**
     * This method deletes an reservation
     * @param r the reservation
     * @return if the deletion was successful
     * @throws ReservationServiceException if an exception in the persistence layer occurs
     * @throws ReservationServiceValidationException if parameter are invalid
     */
    public boolean deleteReservation(Reservation r) throws ReservationServiceException, ReservationServiceValidationException;

    /**
     * This method searches the Reservation to the given day
     * @param id the Reservation id
     * @return the reservation
     * @throws ReservationServiceException if an exception in the persistence layer occurs
     * @throws ReservationServiceValidationException if parameter are invalid
     */
    public Reservation getReservation(long id) throws ReservationServiceException, ReservationServiceValidationException;

    /**
     * This method updates an reservation
     * @param r Reservation
     * @return if the update was successful
     * @throws ReservationServiceException if an exception in the persistence layer occurs
     * @throws ReservationServiceValidationException if parameter are invalid
     */
    public boolean updateReservation(Reservation r) throws ReservationServiceException, ReservationServiceValidationException;

    /**
     * This method sets the ReservationDAO for the reservationservice
     * @param reservationDAO for setting the boxDAO
     */
    public void setReservationDAO(ReservationDAO reservationDAO);

    /**
     * Searches all reservations
     * @return a list of all reservations
     * @throws ReservationServiceException if an exception in the persistence layer occurs
     */
    public List<Reservation> searchall() throws ReservationServiceException;

    /**
     * Searches all Reservation which are in the time range of the reservation
     * @param r the Reservation
     * @return a list of all Reservations
     * @throws ReservationServiceException if an exception in the persistence layer occurs
     * @throws ReservationServiceValidationException if parameter are invalid
     */
    public List<Reservation> search(Reservation r) throws ReservationServiceException, ReservationServiceValidationException;

    /**
     * Returns a list of Box-Reservations for the corresponding Reservation
     * @param r the reservation
     * @return a list of Box Reservations
     * @throws ReservationServiceException if an exception in the persistence layer occurs
     * @throws ReservationServiceValidationException if parameter are invalid
     */
    public List<BoxRes> getBoxReservation(Reservation r) throws ReservationServiceException, ReservationServiceValidationException;


    /**
     * Adds an BoxRes Object to the box_res table
     * @param br the Box-Reservation
     * @return the Box-Reservation which is inserted
     * @throws ReservationServiceException if an exception  in the persistence layer occurs
     * @throws ReservationServiceValidationException if parameter are invalid
     */
    public BoxRes addBoxRes(BoxRes br) throws ReservationServiceException, ReservationServiceValidationException;


    /**
     * Checks if a box is available in a time range
     * @param box_id the box id
     * @param from the startdate
     * @param to the enddate
     * @return the number of reservations for this box in the time range
     * @throws ReservationServiceException if an exception in the persistence layer occurs
     * @throws ReservationServiceValidationException if parameter are invalid
     */
    public int boxResCheckAvailable(int box_id, Date from, Date to) throws ReservationServiceException, ReservationServiceValidationException;


    /**
     * Computes the price of a box in the time range
     * @param box_id the box id
     * @param from the startdate
     * @param to the enddate
     * @return the price in the time range
     * @throws ServiceException if a exception in the persistence layer occurs
     * @throws ReservationServiceValidationException if parameter are invalid
     */
    public float computePrice(int box_id, LocalDate from, LocalDate to) throws ServiceException, ReservationServiceValidationException;


    /**
     * Updates the price of the Table box_res
     * @param br the Box-Reservation which should be updated
     * @return if the upated suceeded
     * @throws ReservationServiceException if an exception in the persistence layer occurs
     * @throws ReservationServiceValidationException if parameter are invalid
     */
    public boolean updateBoxRes(BoxRes br) throws ReservationServiceException, ReservationServiceValidationException;


    /**
     * Method to pick up a horse; the bill is paid
     * @param r the reservation which should be paid
     * @return if the update was successful
     * @throws ReservationServiceException if an exception in the persistence layer occurs
     * @throws ReservationServiceValidationException if parameter are invalid
     */
    public boolean pickUpHorse(Reservation r) throws ReservationServiceException, ReservationServiceValidationException;


    /**
     * Method to delete a BoxRes
     * @param br the Box-Reservation which should be deleted
     * @return if the deletion was successful
     * @throws ReservationServiceException if an exception  in the persistence layer occurs
     * @throws ReservationServiceValidationException if parameter are invalid
     */
    public boolean deleteBoxRes(BoxRes br) throws ReservationServiceException, ReservationServiceValidationException;

    /**
     * This method sets the BoxDAO for the reservationservice
     * @param boxDAO for setting the boxDAO
     */
    public void setBoxDAO(BoxDAO boxDAO);

}
