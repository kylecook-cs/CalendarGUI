package project;

import java.io.*;
import java.util.*;

/**
 * This class is creates a Controller object to manipulate our Calendar.
 * 
 * @author Kyle Cook
 */

public class Controller {
	
    private static Controller controller;
    private ArrayList<Event> eventsArray;
    private Calendar calendar;

    /**
     * This method constructs an EventController object that stores events into an array
     * and then uses that array within our Calendar
     */
    private Controller() {
        eventsArray = new ArrayList<>();
        calendar = Calendar.getInstance();
    }

    /**
     * This method creates a new controller used to update the Calendar
     * 
     * @return controller the new controller being used to update the Calendar
     */
    public static Controller getInstance() {
        if(controller == null) {
            controller = new Controller();
        }
        return controller;
    }

    /**
     * This method returns the current Calendar
     *
     * @return the current Calendar
     */
    public Calendar getCalendar() {
        return this.calendar;
    }

    /**
     * This method updates the current Calendar's Month
     *
     * @param monthVal the value the month will be updated with
     */
    public void updateMonth(int monthVal) {
        this.calendar.add(Calendar.MONTH, monthVal);
    }

    /**
     * This method updates the current Calendar's Day
     *
     * @param dayVal the value the day will be updated with
     */
    public void updateDay(int value) {
        this.calendar.add(Calendar.DAY_OF_MONTH, value);
    }

    /**
     * This method sets the calendar to a new day
     *
     * @param newDay the day to set the Calendar too
     */
    public void setDay(int newDay) {
        this.calendar.set(Calendar.DAY_OF_MONTH, newDay);
    }

    /**
     * This method sets the Calendar to Today's date
     * 
     */
    public void setTodayCalendar() {
        this.calendar = Calendar.getInstance();
    }

    /**
     * This method adds an Event to the arrayList if there is no time conflict
     *
     * @param eventToAdd the event to add to the arrayList
     * @return Boolean the value for whether the event was added (true) or not (false)
     */
    public boolean addEvent(Event eventToAdd) {
        for(Event e: eventsArray) {
            if(e.checkConflict(eventToAdd)) {
                return false;
            }
        }
        this.eventsArray.add(eventToAdd);
        return true;
    }

    /**
     * This method determines which events fall under a specific day and creates a list of all these Events
     *
     * @return dayEvents the arrayList of Events that happen on a specific day
     */
    public ArrayList<Event> getDayEvents() {
        ArrayList<Event> dayEvents = new ArrayList<>();

        for(int i = 0; i < eventsArray.size(); i++) {
            if( (eventsArray.get(i).getCal().get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) &&
                    (eventsArray.get(i).getCal().get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) &&
                    (eventsArray.get(i).getCal().get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH))) {
                dayEvents.add(eventsArray.get(i));
            }
        }
        return dayEvents;
    }

    /**
     * This method determines which events fall under a specific month and creates a list of all these Events
     *
     * @return monthEvents the arrayList of Events that happen within a specific month
     */
    public ArrayList<Event> getMonthEvents() {
        ArrayList<Event> monthEvents = new ArrayList<>();

        for(int i = 0; i < eventsArray.size(); i++) {
            if( (eventsArray.get(i).getCal().get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) &&
                    (eventsArray.get(i).getCal().get(Calendar.MONTH) == calendar.get(Calendar.MONTH))) {
                monthEvents.add(eventsArray.get(i));
            }
        }
        return monthEvents;
    }

    /**
     * This method determines which events fall under a specific week and creates a list of all these Events
     *
     * @return dayEvents the arrayList of Events that happen within a specific week
     */
    public ArrayList<Event> getWeekEvents() {
        ArrayList<Event> weekEvents = new ArrayList<>();

        for(int i = 0; i < eventsArray.size(); i++) {
            if( (eventsArray.get(i).getCal().get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) &&
                    (eventsArray.get(i).getCal().get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) &&
                    (eventsArray.get(i).getCal().get(Calendar.WEEK_OF_MONTH) == calendar.get(Calendar.WEEK_OF_MONTH))) {
                weekEvents.add(eventsArray.get(i));
            }
        }
        return weekEvents;
    }

    /**
     * This method determines which events fall under a specific time interval and creates a list of all these Events
     *
     * @param start the start date of the time interval
     * @param end the end date of the time interval
     * @return intervalEvents the arrayList of Events that happen within a specific time interval
     */
    public ArrayList<Event> getIntervalEvents(Calendar start, Calendar end) {
    	
        ArrayList<Event> intervalEvents = new ArrayList<>();

        for(int i = 0; i < eventsArray.size(); i++) {
            Calendar cal = eventsArray.get(i).getCal();

            if(checkDates(start, cal) || checkDates(end, cal) || (cal.before(end) && cal.after(start))) {
                intervalEvents.add(eventsArray.get(i));
            }
        }
        return intervalEvents;
    }

    /**
     * This method checks if 2 dates from 2 different Calendars are the same
     *
     * @param cal1 the first calendar to check against
     * @param cal2 the second calendar to check against
     * @return Boolean the value if there are equal(true) or not(false)
     */
    private boolean checkDates(Calendar first, Calendar second) {
        return (first.get(Calendar.DAY_OF_MONTH) == second.get(Calendar.DAY_OF_MONTH))
                && (first.get(Calendar.MONTH) == second.get(Calendar.MONTH))
                && (first.get(Calendar.YEAR) == second.get(Calendar.YEAR));
    }

    /**
     * This method reads in a file with Event data, parses the data into desired format
     * 
     * @param input the file to be read in
     * @return Boolean the value of whether the file was read in correct(true) or not(false)
     */
    public boolean readFile(File input) {
        char[] weekDays = {'S','M','T','W','H','F','A'};
        try {
            BufferedReader br = new BufferedReader(new FileReader(input));
            String line;

            while((line = br.readLine()) != null) {
                String[] data = line.split(";");

                String name = data[0];
                int year = Integer.parseInt(data[1]);
                int monthStart = Integer.parseInt(data[2]);
                int monthEnd = Integer.parseInt(data[3]);
                String days = data[4];
                int hourStart = Integer.parseInt(data[5]);
                int hourEnd = Integer.parseInt(data[6]);

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthStart - 1);
                cal.set(Calendar.DAY_OF_MONTH, 1);

                do {
                    char now = weekDays[cal.get(Calendar.DAY_OF_WEEK) - 1];
                    if(days.contains(String.valueOf(now))) {

                        Calendar eventsCal = Calendar.getInstance();
                        eventsCal.set(Calendar.YEAR, year);
                        eventsCal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
                        eventsCal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
                        addEvent(new Event(name, eventsCal, hourStart, hourEnd));
                    }
                    cal.add(Calendar.DAY_OF_MONTH, 1);

                } while((cal.get(Calendar.MONTH) != monthEnd-1));
            }
            br.close();
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
}