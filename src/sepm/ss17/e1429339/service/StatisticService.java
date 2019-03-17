package sepm.ss17.e1429339.service;

import sepm.ss17.e1429339.dao.ReservationDAO;
import sepm.ss17.e1429339.dao.BoxDAO;
import sepm.ss17.e1429339.entities.Box;
import sepm.ss17.e1429339.exceptions.ServiceException;
import sepm.ss17.e1429339.exceptions.StatisticServiceValidationException;

import java.util.Date;
import java.util.List;


public interface StatisticService {


    /**
     * This method sets the BoxDAO for the statisticservice
     * @param boxDAO for setting the boxDAO
     */
    public void setBoxDAO(BoxDAO boxDAO);

    /**
     * This method sets the reservationDAO for the statisticservice
     * @param reservationDAO for setting the reservationDAO
     */
    public void setReservationDAO(ReservationDAO reservationDAO);

    /**
     * This method returns the number of days where a specific box is reserved in a specific time range
     * @param box_id is the id of the box
     * @param from is the startdate
     * @param to is the enddate
     * @return the number of days on which the box is reserved in the time range
     * @throws ServiceException if an exception in the persistence layer occurs
     * @throws StatisticServiceValidationException if parameter are invalid
     */
    public int getNumberOfDays(int box_id, Date from, Date to) throws ServiceException, StatisticServiceValidationException;

    /**
     * This method returns the number of days where all boxes are reserved in a specific time range
     * @param from is the startdate
     * @param to is the enddate
     * @return number of all reservations
     * @throws ServiceException if an exception in the persistence layer occurs
     * @throws StatisticServiceValidationException if parameter are invalid
     */
    public int getNumberOfAllDays(Date from, Date to) throws ServiceException, StatisticServiceValidationException;


    /**
     * This method returns a list of all boxes
     * @return a list of all boxes
     * @throws ServiceException if parameter are invalid
     */
    public List<Box> searchall() throws ServiceException;


    /**
     * This method returns an array with the frequencies of the days from the box_id in the timerange
     * @param box_id the box id
     * @param from the startdate
     * @param to the enddate
     * @return an array with frequencies of a single box, index: 0=Monday;1=Tuesday;2=Wednesday;3=Thursday;4=Friday;5=Saturday;6=Sunday;
     * @throws ServiceException if an exception in the persistence layer occurs
     * @throws StatisticServiceValidationException if parameter are invalid
     */
    public int[] getFrequencys(int box_id, Date from, Date to) throws ServiceException, StatisticServiceValidationException;


    /**
     * This method returns an array with the frequencies of the days from all boxes in the timerange
     * @param from the startdate
     * @param to the enddate
     * @return an array with frequencies of all boxes, index: 0=Monday;1=Tuesday;2=Wednesday;3=Thursday;4=Friday;5=Saturday;6=Sunday;
     * @throws ServiceException if an exception in the persistence layer occurs
     * @throws StatisticServiceValidationException if parameter are invalid
     */
    public int[] getAllFrequencys(Date from, Date to) throws ServiceException, StatisticServiceValidationException;


    /**
     * This method returns the sum value-price adjustment for a specific weekday - for the best or worst box - adds adjustment in percentage or value
     * @param day the day: 0=Monday;1=Tuesday;2=Wednesday;3=Thursday;4=Friday;5=Saturday;6=Sunday;
     * @param from the startdate
     * @param to the enddate
     * @param adjustment the adjustment of the price
     * @param best if true, the best box is searched, if false the worst
     * @param percentage if true, adjustement in percentage otherwise a value
     * @return a float array with the old sum and new sum - index:0 = oldPrice; index:1 = newPrice
     * @throws ServiceException if an exception in the persistence layer occurs
     * @throws StatisticServiceValidationException if parameter are invalid
     */
    public float[] priceAdjustmentWeekday(int day, Date from, Date to, float adjustment, boolean best, boolean percentage) throws ServiceException, StatisticServiceValidationException;


    /**
     * This method returns the sum value-price adjustment for daterange - for the best or worst box - adds adjustment in percentage or value
     * @param from the startdate
     * @param to the enddate
     * @param adjustment the adjustment of the price
     * @param best if true, adjustement in percentage otherwise a value
     * @param percentage if true, adjustement in percentage otherwise a value
     * @return a float array with the old sum and new sum - index:0 = oldPrice; index:1 = newPrice
     * @throws ServiceException if an exception in the persistence layer occurs
     * @throws StatisticServiceValidationException if parameter are invalid
     */
    public float[] priceAdjustmentRange(Date from, Date to, float adjustment, boolean best, boolean percentage) throws ServiceException, StatisticServiceValidationException;

    /**
     * This method returns the worst Box-id to the given date range; if several boxes are worst, than a random gets picked
     * @param from the startdate
     * @param to the enddate
     * @return the boxid of the worst booked Box in the time range
     * @throws ServiceException if a excpetion in the persistence layer occures
     * @throws StatisticServiceValidationException if parameter are invalid
     */
    public int getWorstBoxRange(Date from, Date to) throws ServiceException, StatisticServiceValidationException;

    /**
     * This method returns the best Box-id to the date range; if several boxes are the best, than a random gets picked
     * @param from the startdate
     * @param to the enddate
     * @return the boxid of the best box
     * @throws ServiceException if a excpetion in the persistence layer occures
     * @throws StatisticServiceValidationException if parameter are invalid
     */
    public int getBestBoxRange(Date from, Date to) throws ServiceException, StatisticServiceValidationException;


    /**
     * This method returns the worst Box-id to the given weekday; if several boxes are worst, than a random gets picked
     * @param day 0=Monday;1=Tuesday;2=Wednesday;3=Thursday;4=Friday;5=Saturday;6=Sunday;
     * @param from the startdate
     * @param to the enddate
     * @return returns the box id of the worst box on the given weekday
     * @throws ServiceException if a excpetion in the persistence layer occures
     * @throws StatisticServiceValidationException if parameter are invalid
     */
    public int getWorstBoxWeekday(int day, Date from, Date to) throws ServiceException, StatisticServiceValidationException;

    /**
     * This method returns the best Box-id to the given weekday; if several boxes are the best, than a random gets picked
     * @param day an array with frequencies of all boxes, index: 0=Monday;1=Tuesday;2=Wednesday;3=Thursday;4=Friday;5=Saturday;6=Sunday;
     * @param from the startdate
     * @param to the enddate
     * @return the box id of the best box on given weekday
     * @throws ServiceException if a excpetion in the persistence layer occures
     * @throws StatisticServiceValidationException if parameter are invalid
     */
    public int getBestBoxWeekday(int day, Date from, Date to) throws ServiceException, StatisticServiceValidationException;



}
