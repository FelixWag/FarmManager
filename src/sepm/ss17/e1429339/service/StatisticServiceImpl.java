package sepm.ss17.e1429339.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sepm.ss17.e1429339.dao.ReservationDAO;
import sepm.ss17.e1429339.dao.BoxDAO;
import sepm.ss17.e1429339.entities.Box;
import sepm.ss17.e1429339.entities.BoxRes;
import sepm.ss17.e1429339.entities.Reservation;
import sepm.ss17.e1429339.exceptions.*;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

public class StatisticServiceImpl implements StatisticService {

    final static Logger LOGGER = LoggerFactory.getLogger(StatisticServiceImpl.class);

    private ReservationDAO reservationDAO;
    private BoxDAO boxDAO;


    @Override
    public void setBoxDAO(BoxDAO boxDAO) {
        this.boxDAO=boxDAO;
    }

    @Override
    public void setReservationDAO(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }


    @Override
    public int getNumberOfDays(int box_id, Date from, Date to) throws ServiceException, StatisticServiceValidationException {

        if(box_id<0 || from==null || to==null || from.after(to)){
            throw new StatisticServiceValidationException();
        }

        int[] singleDays;
        int sumDays=0;
        singleDays=getFrequencys(box_id,from,to);
        for(int i=0;i<7;i++){
            sumDays += singleDays[i];
        }
        LOGGER.info("Box id" + box_id + "is " + sumDays + " booked");
        return sumDays;
    }

    @Override
    public int getNumberOfAllDays(Date from, Date to) throws ServiceException, StatisticServiceValidationException {

        if(from==null || to==null || from.after(to)){
            throw new StatisticServiceValidationException();
        }
        int[] singleDays;
        int sumDays=0;
        singleDays=getAllFrequencys(from,to);
        for(int i=0;i<7;i++){
            sumDays+=singleDays[i];
        }
        LOGGER.info("All boxes are "+sumDays+" days booked");
        return sumDays;
    }

    @Override
    public List<Box> searchall() throws BoxServiceException {
        try {
            LOGGER.info("Searching all boxes");
            return boxDAO.searchall();
        } catch (BoxDAOException e) {
            LOGGER.error("ERROR",e);
            throw new BoxServiceException();
        }
    }

    @Override
    public int[] getAllFrequencys(Date from, Date to) throws ServiceException, StatisticServiceValidationException {
        if(from==null || to==null || from.after(to)){
            throw new StatisticServiceValidationException();
        }
        int[] frequencys = new int[7];
        List<Box> boxList = searchall();

        for (Box b : boxList) {
            int[] temp;
            temp = getFrequencys(b.getBox_id(), from, to);

            frequencys[0] += temp[0];
            frequencys[1] += temp[1];
            frequencys[2] += temp[2];
            frequencys[3] += temp[3];
            frequencys[4] += temp[4];
            frequencys[5] += temp[5];
            frequencys[6] += temp[6];
        }

        LOGGER.info("All boxes are booked on the weekdays starting Monday x time:"+frequencys[0]+", "
                +frequencys[1]+", "+frequencys[2]+", "+frequencys[3]+", "+frequencys[4]+", "+frequencys[5]+", "+
                frequencys[6]);

        return frequencys;
    }


    /**
     * This method returns the best Box-id to the given weekday; if several boxes are the best, than a random gets picked
     * @param day an array with frequencies of all boxes, index: 0=Monday;1=Tuesday;2=Wednesday;3=Thursday;4=Friday;5=Saturday;6=Sunday;
     * @param from the startdate
     * @param to the enddate
     * @return the box id of the best box on given weekday
     * @throws ServiceException if a excpetion in the persistence layer occures
     * @throws StatisticServiceValidationException if parameter are invalid
     */
    public int getBestBoxWeekday(int day, Date from, Date to) throws ServiceException, StatisticServiceValidationException {

        if(day<0 || day>6 || from==null || to==null || from.after(to)){
            throw new StatisticServiceValidationException();
        }

        int box_id=1;
        List<Box> boxList = searchall();
        int tempBest;
        int totalBest=0;

        for(Box b: boxList){
            tempBest = getFrequencys(b.getBox_id(),from,to)[day];
            if(tempBest>=totalBest){
                totalBest=tempBest;
                box_id=b.getBox_id();
            }
        }

        LOGGER.info("Best Box "+box_id+" of the weekday"+day);
        return box_id;
    }



    /**
     * This method returns the worst Box-id to the given weekday; if several boxes are worst, than a random gets picked
     * @param day 0=Monday;1=Tuesday;2=Wednesday;3=Thursday;4=Friday;5=Saturday;6=Sunday;
     * @param from the startdate
     * @param to the enddate
     * @return returns the box id of the worst box on the given weekday
     * @throws ServiceException if a excpetion in the persistence layer occures
     * @throws StatisticServiceValidationException if parameter are invalid
     */
    public int getWorstBoxWeekday(int day, Date from, Date to) throws ServiceException, StatisticServiceValidationException {

        if(day<0 || day>6 || from==null || to==null || from.after(to)){
            throw new StatisticServiceValidationException();
        }

        int box_id=1;
        List<Box> boxList = searchall();
        int tempWorst;
        int totalWorst=Integer.MAX_VALUE;

        try {
            for (Box b : boxList) {
                if (boxDAO.getNumberOfReservations(b.getBox_id(),from,to) >= 1) {
                    tempWorst = getFrequencys(b.getBox_id(), from, to)[day];
                    if (tempWorst <= totalWorst) {
                        totalWorst = tempWorst;
                        box_id = b.getBox_id();
                    }
                }
            }
        }catch (BoxDAOException e){
            LOGGER.error("ERROR",e);
            throw new BoxServiceException();
        }

        LOGGER.info("Worst Box "+box_id+" of the weekday"+day);
        return box_id;
    }

    /**
     * This method returns the best Box-id to the date range; if several boxes are the best, than a random gets picked
     * @param from the startdate
     * @param to the enddate
     * @return the boxid of the best box
     * @throws ServiceException if a excpetion in the persistence layer occures
     * @throws StatisticServiceValidationException if parameter are invalid
     */
    public int getBestBoxRange(Date from, Date to) throws ServiceException, StatisticServiceValidationException {

        if(from==null || to==null || from.after(to)){
            throw new StatisticServiceValidationException();
        }

        int box_id=1;
        List<Box> boxList = searchall();
        int totalBest=0;
        /**TempDays is for getting all the days*/
        int[] tempDays;

        for(Box b: boxList){
            tempDays=getFrequencys(b.getBox_id(),from,to);
            int tempBest=0;
            /**Now iterate over the Array to get the sum of the days*/
            for(int i=0;i<7;i++){
                tempBest += tempDays[i];
            }
            if(tempBest>=totalBest){
                totalBest=tempBest;
                box_id=b.getBox_id();
            }
        }

        LOGGER.info("Best Box "+box_id+" in the timerange from:"+from+" to:"+to);
        return box_id;
    }


    /**
     * This method returns the worst Box-id to the given date range; if several boxes are worst, than a random gets picked
     * @param from the startdate
     * @param to the enddate
     * @return the boxid of the worst booked Box in the time range
     * @throws ServiceException if a excpetion in the persistence layer occures
     * @throws StatisticServiceValidationException if parameter are invalid
     */
    public int getWorstBoxRange(Date from, Date to) throws ServiceException, StatisticServiceValidationException {

        if(from==null || to==null || from.after(to)){
            throw new StatisticServiceValidationException();
        }

        int box_id=1;
        List<Box> boxList = searchall();
        int totalWorst=Integer.MAX_VALUE;
        /**TempDays is for getting all the days*/
        int[] tempDays;

        try {
            for (Box b : boxList) {
                if (boxDAO.getNumberOfReservations(b.getBox_id(),from,to) >= 1) {
                    tempDays = getFrequencys(b.getBox_id(), from, to);
                    int tempWorst = 0;
                    /**Now iterate over the Array to get the sum of the days*/
                    for (int i = 0; i < 7; i++) {
                        tempWorst += tempDays[i];
                    }
                    if (tempWorst <= totalWorst) {
                        totalWorst = tempWorst;
                        box_id = b.getBox_id();
                    }
                }
            }
        }catch (BoxDAOException e){
            LOGGER.error("ERROR",e);
            throw new BoxServiceException();
        }

        LOGGER.info("Best Box "+box_id+" in the timerange from:"+from+" to:"+to);
        return box_id;
    }

    @Override
    public float[] priceAdjustmentWeekday(int day,Date from, Date to, float adjustment, boolean best, boolean percentage) throws ServiceException, StatisticServiceValidationException {

        if(day<0 || day>6 || from==null || to==null || from.after(to) || !Float.toString(adjustment).matches("\\-?\\d*\\.?\\d{1,2}")){
            throw new StatisticServiceValidationException();
        }

        int box_id;
        if(best) {
            box_id = getBestBoxWeekday(day, from, to);
        }else {
            box_id = getWorstBoxWeekday(day,from,to);
        }

        float [] oldAndNew=new float[2];
        float oldSum=0.0f;
        float newSum=0.0f;
        try {
            List<Reservation> reservationList = reservationDAO.getReservationToBoxId(box_id, from, to);

            List<BoxRes> boxResList = reservationDAO.getBoxResId(box_id, from, to);
            /**Compute the old sum*/
            for (BoxRes br : boxResList) {
                oldSum += br.getPrice();
            }

            /**Compute new price*/
            for (BoxRes br : boxResList) {
                float newPrice = 0.0f;
                Reservation r = reservationDAO.getReservation(br.getRes_id());
                /**To get the old daily rate*/
                long days = DAYS.between(r.getRes_from().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), r.getRes_to().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) + 1;
                float daily_rate = br.getPrice() / days;

                if (!percentage) {
                    daily_rate += adjustment;
                } else {
                    daily_rate *= 1 + (adjustment / 100);
                }
                newPrice = days * daily_rate;
                newSum += newPrice;
            }
        }catch (ReservationDAOException e){
            LOGGER.error("ERROR",e);
            throw new ReservationServiceException();
        }

        oldAndNew[0]=oldSum;
        oldAndNew[1]=newSum;

        LOGGER.info("Specific day - Old price:" + oldAndNew[0] + " new price: " + oldAndNew[1]);

        return oldAndNew;
    }

    @Override
    public float[] priceAdjustmentRange(Date from, Date to, float adjustment, boolean best, boolean percentage) throws ServiceException, StatisticServiceValidationException {
        if(from==null || to==null || from.after(to) || !Float.toString(adjustment).matches("\\-?\\d*\\.?\\d{1,2}")){
            throw new StatisticServiceValidationException();
        }
        int box_id;
        if(best) {
            box_id = getBestBoxRange(from, to);
        }else {
            box_id = getWorstBoxRange(from,to);
        }
        float [] oldAndNew=new float[2];
        float oldSum=0.0f;
        float newSum=0.0f;
        try {
            List<Reservation> reservationList = reservationDAO.getReservationToBoxId(box_id, from, to);


            List<BoxRes> boxResList = reservationDAO.getBoxResId(box_id, from, to);
            /**Compute the old sum*/
            for (BoxRes br : boxResList) {
                float oldPrice = 0.0f;
                Reservation r = reservationDAO.getReservation(br.getRes_id());
                /**To get the old daily rate*/
                long days = DAYS.between(r.getRes_from().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), r.getRes_to().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) + 1;
                float daily_rate = br.getPrice() / days;

                /**Now check if the time range of the reservation is out of the given range*/
                Date tempFrom;
                Date tempTo;
                if (r.getRes_from().before(from)) {
                    tempFrom = from;
                } else {
                    tempFrom = r.getRes_from();
                }
                if (r.getRes_to().after(to)) {
                    tempTo = to;
                } else {
                    tempTo = r.getRes_to();
                }

                long actualDays = DAYS.between(tempFrom.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), tempTo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) + 1;

                oldPrice = actualDays * daily_rate;
                oldSum += oldPrice;
            }

            /**Compute new price*/
            for (BoxRes br : boxResList) {
                float newPrice = 0.0f;
                Reservation r = reservationDAO.getReservation(br.getRes_id());
                /**To get the old daily rate*/
                long days = DAYS.between(r.getRes_from().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), r.getRes_to().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) + 1;
                float daily_rate = br.getPrice() / days;

                /**Now check if the time range of the reservation is out of the given range*/
                Date tempFrom;
                Date tempTo;
                if (r.getRes_from().before(from)) {
                    tempFrom = from;
                } else {
                    tempFrom = r.getRes_from();
                }
                if (r.getRes_to().after(to)) {
                    tempTo = to;
                } else {
                    tempTo = r.getRes_to();
                }

                /**Compute the acutal days in the time range*/
                long actualDays = DAYS.between(tempFrom.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), tempTo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()) + 1;

                if (!percentage) {
                    daily_rate += adjustment;
                } else {
                    daily_rate *= 1 + (adjustment / 100);
                }
                newPrice = actualDays * daily_rate;
                newSum += newPrice;
            }
        }catch (ReservationDAOException e){
            LOGGER.error("ERROR",e);
            throw new ReservationServiceException();
        }

        oldAndNew[0]=oldSum;
        oldAndNew[1]=newSum;

        LOGGER.info("Specific time range - Old price:" + oldAndNew[0] + " new price: " + oldAndNew[1]);

        return oldAndNew;
    }

    @Override
    public int[] getFrequencys(int box_id, Date from, Date to) throws ServiceException, StatisticServiceValidationException {

        if(box_id<0 || from.after(to) || from==null || to==null){
            throw new StatisticServiceValidationException();
        }

        int[] frequencys = new int[7];
        try {
            List<Reservation> listRes;
            listRes = reservationDAO.getReservationToBoxId(box_id, from, to);

            int monday, tuesday, wednesday, thursday, friday, saturday, sunday;
            monday = tuesday = wednesday = thursday = friday = saturday = sunday = 0;

            for (Reservation r : listRes) {

                Date tempFrom;
                Date tempTo;
                Calendar startCal = Calendar.getInstance();
                Calendar endCal = Calendar.getInstance();

                /**Now check if the time range of the reservation is out of the given range*/
                if (r.getRes_from().before(from)) {
                    tempFrom = from;
                } else {
                    tempFrom = r.getRes_from();
                }
                if (r.getRes_to().after(to)) {
                    tempTo = to;
                } else {
                    tempTo = r.getRes_to();
                }


                startCal.setTime(tempFrom);

                endCal.setTime(tempTo);

                /**Count weekdays*/
                do {
                    if (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                        monday++;
                    }
                    if (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
                        tuesday++;
                    }
                    if (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
                        wednesday++;
                    }
                    if (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
                        thursday++;
                    }
                    if (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                        friday++;
                    }
                    if (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                        saturday++;
                    }
                    if (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        sunday++;
                    }
                    startCal.add(Calendar.DAY_OF_MONTH, 1);
                } while (startCal.getTimeInMillis() <= endCal.getTimeInMillis());
                frequencys[0] = monday;
                frequencys[1] = tuesday;
                frequencys[2] = wednesday;
                frequencys[3] = thursday;
                frequencys[4] = friday;
                frequencys[5] = saturday;
                frequencys[6] = sunday;
            }
        }catch (ReservationDAOException e){
            LOGGER.error("ERROR",e);
            throw new ReservationServiceException();
        }

        LOGGER.info("Box: "+box_id+"is booked on the weekdays starting Monday x time:"+frequencys[0]+", "
                +frequencys[1]+", "+frequencys[2]+", "+frequencys[3]+", "+frequencys[4]+", "+frequencys[5]+", "+
                frequencys[6]);

        return frequencys;
    }


}
