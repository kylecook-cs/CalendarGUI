package project;

import java.util.*;

/**
 * This class is creates the Event object that is used in our Calendar GUI
 * 
 * @author Kyle Cook
 */

public class Event {
    private String name;
    private Calendar cal;
    private int start;
    private int end;


    /**
     * This method constructs an Event object used in the Calendar GUI
     *
     * @param name the name of the Event
     * @param cal the Calendar the Event is present in
     * @param start the starting hour of the event
     * @param end the end hour of the event
     */
    public Event(String name, Calendar cal, int start, int end) {
        this.name = name;
        this.cal = cal;
        this.start = start;
        this.end = end;

        
    }

    /**
     * This method checks if 2 events have time conflicts
     *
     * @param checkEvent the Event to be checked for time conflicts with another
     * @return Boolean the value of whether there is a conflict(true) or not(false)
     */
    public boolean checkConflict(Event checkEvent) {
        Calendar checkCal = checkEvent.getCal();
        if(checkCal.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH) 
                && (checkCal.get(Calendar.MONTH) == cal.get(Calendar.MONTH))       
                && (checkCal.get(Calendar.YEAR) == cal.get(Calendar.YEAR))) {
            if((start >= checkEvent.getStart() && start < checkEvent.getEnd()) || 
                    (end > checkEvent.getStart() && end <= checkEvent.getEnd()) ||   
                    (checkEvent.getStart() >= start && checkEvent.getStart() < end) || 
                    (checkEvent.getEnd() > start && checkEvent.getEnd() <= end)) {
                return true; 
            }
        }
        return false; 
    }

    /**
     * This method returns an Event's name
     * 
     * @return name the name of the Event
     */
    public String getName() {
        return name;
    }

    /**
     * This method sets an Event's new name
     * 
     * @return newName the new name of the Event
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * This method returns the Calendar of an Event
     * 
     * @return cal the Calendar used for the Event
     */
    public Calendar getCal() {
        return cal;
    }

    /**
     * This method sets a new Calendar for an Event
     * 
     * @param newCal the Calendar being set for an Event
     */
    public void setCal(Calendar newCal) {
        this.cal = newCal;
    }

    /**
     * This method returns an Event's starting hour
     *  
     * @return start the Event's start hour
     */
    public int getStart() {
        return start;
    }

    /**
     * This method sets an Event's starting hour
     *  
     * @param newStart the Event's new start hour
     */
    public void setStart(int newStart)
    {
        this.start = newStart;
    }

    /**
     * This method returns an Event's ending hour
     *  
     * @return end the Event's end hour
     */
    public int getEnd()
    {
        return end;
    }

    /**
     * This method sets an Event's ending hour
     * 
     * @param newEnd the Event's new end hour
     */
    public void setEnd(int newEnd)
    {
        this.end = newEnd;
    }

    @Override
    public String toString()
    {
        int month = cal.get(Calendar.MONTH);
        month += 1;
        return "Event: " + name + "  " + cal.get(Calendar.YEAR) +
                "/"+ month +"/" + cal.get(Calendar.DAY_OF_MONTH) + " " +
                "Time: ("+ start + "-" + end+ ")";
    }
}
