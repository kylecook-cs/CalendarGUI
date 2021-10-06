package project;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * This class is creates the GUI part of the project, Allows users to see and interact with a Calendar GUI
 * 
 * @author Kyle Cook
 */

public class Viewer extends JFrame implements ActionListener
{

	private static final long serialVersionUID = 1L;
	private int currentDaySelected;
    private Controller controller;

    private final String[] monthArray = new String[] {  "January", "Feburary", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};

    private JPanel content;
    private JButton[][] dateNum;
    private JButton todayB;
    private JButton monthBackB;
    private JButton monthForwardB;
    private JTextArea eventData;
    private JButton dayViewerB;
    private JButton weekViewerB;
    private JButton monthViewerB;
    private JButton agendaViewerB;
    private JButton fileB;
    private JLabel dateTitle;
    private JButton createB;
    private JButton dayBackB;
    private JButton dayForwardB;

    /**
     * This method constructs a Viewer object used to hold the content for the GUI
     * 
     */
    public Viewer()
    {
        super();
        setTitle("PARK Calendar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 880, 550);
        content = new JPanel();
        content.setBorder(new EmptyBorder(5, 5, 5, 5));
        content.setLayout(null);
        setContentPane(content);

        guiComponents();
        listeners();

        this.controller = Controller.getInstance();
        this.currentDaySelected = controller.getCalendar().get(Calendar.DAY_OF_MONTH);
        monthDisplay(); 
    }

    /**
     * This is an Override method from ActionListener
     * It is invoked when an action is performed
     * 
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        JButton b = (JButton) e.getSource();
        setDayColor();
        b.setForeground(Color.WHITE);
        b.setBackground(Color.DARK_GRAY);
        this.currentDaySelected = Integer.parseInt(b.getText());
        controller.setDay(currentDaySelected);
    }

    /**
     * This method sets the days on the Calendar to black
     * 
     */
    private void setDayColor()
    {
        for(int i = 0; i < dateNum.length; i++)
        {
            for(JButton button: dateNum[i])
            {
                button.setForeground(Color.DARK_GRAY);

                button.setBackground(Color.WHITE);
            }
        }
    }

    /**
     * This method displays the month
     * 
     */
    public void monthDisplay() {
        Calendar cal = controller.getCalendar();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                dateNum[i][j].setVisible(false);
            }
        }

        dateTitle.setText(monthArray[cal.get(Calendar.MONTH)]+ " " +cal.get(Calendar.YEAR));
        int total = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        int row = 0;
        int column = cal.get(Calendar.DAY_OF_WEEK) - 1;
        int day = 1;

        for(int i = 0; i < total; i++) {
            if(this.currentDaySelected == day) {
                dateNum[row][column].setForeground(Color.WHITE);
                dateNum[row][column].setBackground(Color.DARK_GRAY);
            }

            dateNum[row][column].setText(day + "");
            dateNum[row][column].setVisible(true);
            cal.set(Calendar.DAY_OF_MONTH, day);
           
            day++;
            column++;
            if(column >= 7) {
                column = 0;
                row++;
            }
        }
        cal.set(Calendar.DAY_OF_MONTH, this.currentDaySelected);
    }

    /**
     * This method applies listerners to all the buttons used in the GUI
     */
    private void listeners()
    {
        // todays date
        todayB.addActionListener(action -> { 
            setDayColor();
            controller.setTodayCalendar();
            this.currentDaySelected = controller.getCalendar().get(Calendar.DAY_OF_MONTH);
            monthDisplay();
        });

        // month back 1
        monthBackB.addActionListener(action -> {
            setDayColor();
            if(controller.getCalendar().get(Calendar.DAY_OF_MONTH) == 6) {
            	controller.updateDay(-1);
            }
            controller.updateMonth(-1);
            monthDisplay();

        });

        // month forward 1
        monthForwardB.addActionListener(action -> {
            setDayColor();
            controller.updateMonth(1);
            monthDisplay();
        });

        // day back 1
        dayBackB.addActionListener(action -> {
            setDayColor();
            controller.updateDay(-1);
            this.currentDaySelected = controller.getCalendar().get(Calendar.DAY_OF_MONTH);
            monthDisplay();
        });

        // day forward 1 
        dayForwardB.addActionListener(action -> {
            setDayColor();
            controller.updateDay(1);
            this.currentDaySelected = controller.getCalendar().get(Calendar.DAY_OF_MONTH);
            monthDisplay();
        });

        // create event 
        createB.addActionListener(action ->
        {
            addNewEvent();
        });

        // agenda
        agendaViewerB.addActionListener(action -> {
            agendaDetails();
        });

        // day view
        dayViewerB.addActionListener(action -> {
            displayEventData(controller.getDayEvents());
        });

        // month view
        monthViewerB.addActionListener(action -> {
            displayEventData(controller.getMonthEvents());
        });

        // week view
        weekViewerB.addActionListener(action -> {
            displayEventData(controller.getWeekEvents());
        });

        // file
        fileB.addActionListener(action -> {
            JFileChooser c = new JFileChooser();
            int result = c.showOpenDialog(null);

            if(result == JFileChooser.APPROVE_OPTION) {
                File file = c.getSelectedFile();
                boolean success = controller.readFile(file);
                if(success) {
                    JOptionPane.showMessageDialog(null, "File was uploaded successfully.");
                }
                else {
                    JOptionPane.showMessageDialog(null, "File failed to upload.");
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "File not selected.");
            }
        });
    }

    /**
     * This method displays the Event data of Events within an arrayList
     *
     * @param events the arrayList of Events to be displayed
     */
    private void displayEventData(ArrayList<Event> events) {
        
    	String event = "";
        for(Event e: events) {
            event += e + "\n";
        }
        eventData.setText(event);
    }

    /**
     * This method displays Event data used in the Agenda Viewer
     * 
     */
    private void agendaDetails() {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        do {
            try {
                String[] startDateArray = JOptionPane.showInputDialog("Enter the Start Date (MM/DD/YYYY)").split("/");
                int month = Integer.parseInt(startDateArray[0]);
                if(month > 0 && month <= 12) {
                    startDate.set(Calendar.MONTH, month - 1);
                    int day = Integer.parseInt(startDateArray[1]);

                    if(day > 0 && day <= 31) {
                        startDate.set(Calendar.DAY_OF_MONTH, day);
                        int year = Integer.parseInt(startDateArray[2]);

                        if(year > 0) {
                            startDate.set(Calendar.YEAR, year);
                            break;
                        }
                    }
                }
                JOptionPane.showMessageDialog(null, "Please enter correct date format (MMM/DD/YYYY)");
            }
            catch(Exception e) {
                JOptionPane.showMessageDialog(null, "Please enter correct date format (MMM/DD/YYYY)");
            }
        }
        while(startDate.after(Calendar.getInstance()));

        do {
            try {
                String[] endDateArray = JOptionPane.showInputDialog("Enter Ending Date (MM/DD/YYYY)").split("/");
                int month = Integer.parseInt(endDateArray[0]);
                if(month > 0 && month <= 12) {
                    endDate.set(Calendar.MONTH, month - 1);
                    int day = Integer.parseInt(endDateArray[1]);

                    if(day > 0 && day <= 31) {
                        endDate.set(Calendar.DAY_OF_MONTH, day);
                        int year = Integer.parseInt(endDateArray[2]);

                        if(year > 0) {
                            endDate.set(Calendar.YEAR, year);
                            break;
                        }
                    }
                }

                JOptionPane.showMessageDialog(null, "Please enter correct format of date (MMM/DD/YYYY)");
            }
            catch(Exception e) {
                JOptionPane.showMessageDialog(null, "Please enter correct format of date (MMM/DD/YYYY)");
            }
        }
        while(endDate.after(Calendar.getInstance()));

        displayEventData(controller.getIntervalEvents(startDate, endDate));
    }

    /**
     * This method creates a new Event
     * 
     * @precondition event will be checked for time conflicts, if there is one event will not be created
     * 
     */
    private void addNewEvent() {
        String newEvent = JOptionPane.showInputDialog("Please Enter the Event's Name: ");
        int newStart, newEnd;

        do {
            try {
                newStart = Integer.parseInt(JOptionPane.showInputDialog("Please Enter the Event's Start Hour (0-23) "));
                if(newStart < 0 || newStart > 23)
                {
                    JOptionPane.showMessageDialog(null, "Please choose a valid hour (0 - 23) ");
                }
                else
                {
                    break;
                }
            }
            catch(Exception e) {
                JOptionPane.showMessageDialog(null, "\"Please choose a valid hour (0 - 23) \"");
            }
        }
        while(true);

        do {
            try {
                newEnd = Integer.parseInt(JOptionPane.showInputDialog("Please Enter the Event's End Hour (0-23) "));
                if (newEnd < 0 || newEnd > 23 || newEnd < newStart) {
                    JOptionPane.showMessageDialog(null, "Please choose a valid hour (0 - 23) that is after your Start Hour ");
                }
                else{
                    break;
                }
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null, "Please choose a valid hour (0 - 23)");
            }
        }
        while(true);

        Calendar eventCalendar = Calendar.getInstance();
        eventCalendar.set(Calendar.YEAR, this.controller.getCalendar().get(Calendar.YEAR));
        eventCalendar.set(Calendar.MONTH, this.controller.getCalendar().get(Calendar.MONTH));
        eventCalendar.set(Calendar.DAY_OF_MONTH, this.currentDaySelected);
        Event event = new Event(newEvent, eventCalendar, newStart, newEnd);
        boolean result = controller.addEvent(event);

        if(result) {
            JOptionPane.showMessageDialog(null, "Event " + "''" + newEvent  + "''" +
                    " was added to Calendar successfully.");
        }
        else {
            JOptionPane.showMessageDialog(null, "Event " + "''" + newEvent  + "''" +
                    " failed to be created due to time conflict with another event with the Calendar.");
        }
    }

    /**
     * This method adds all GUI buttons to frame and initializes them
     * 
     */
    private void guiComponents() {
        
    	// today's date
        todayB = new JButton("Today");
        todayB.setForeground(Color.WHITE);
        todayB.setBackground(Color.DARK_GRAY);
        todayB.setBounds(170, 16, 87, 47);
        content.add(todayB);
    	
    	// day back
        dayBackB = new JButton("<");
        dayBackB.setBackground(Color.RED);
        dayBackB.setBounds(110, 16, 45, 47);
        content.add(dayBackB);

        // day forward
        dayForwardB = new JButton(">");
        dayForwardB.setBackground(Color.GREEN);
        dayForwardB.setBounds(270, 16, 45, 47);
        content.add(dayForwardB);
        
        // month back
        monthBackB = new JButton("<");
        monthBackB.setBackground(Color.RED);
        monthBackB.setBounds(20, 70, 45, 38);
        content.add(monthBackB);
        
        // month forward
        monthForwardB = new JButton(">");
        monthForwardB.setBackground(Color.GREEN);
        monthForwardB.setBounds(373, 70, 45, 38);
        content.add(monthForwardB);

        // file
        fileB = new JButton("From File");
        fileB.setBackground(Color.MAGENTA);
        fileB.setForeground(Color.BLACK);
        fileB.setBounds(710, 16, 130, 37);
        content.add(fileB);

        // agenda
        agendaViewerB = new JButton("Agenda");
        agendaViewerB.setBackground(Color.CYAN);
        agendaViewerB.setBounds(750, 70, 90, 47);
        content.add(agendaViewerB);

        // day view
        dayViewerB = new JButton("Day");
        dayViewerB.setBackground(Color.ORANGE);
        dayViewerB.setBounds(440, 70, 80, 47);
        content.add(dayViewerB);
        
        // month view
        monthViewerB = new JButton("Month");
        monthViewerB.setBackground(Color.PINK);
        monthViewerB.setBounds(650, 70, 80, 47);
        content.add(monthViewerB);

        // week view
        weekViewerB = new JButton("Week");
        weekViewerB.setBackground(Color.YELLOW);
        weekViewerB.setBounds(540, 70, 80, 47);
        content.add(weekViewerB);

        // create
        createB = new JButton("Create Event");
        createB.setForeground(Color.BLUE);
        createB.setBounds(20, 440, 398, 38);
        content.add(createB);

        // date title
        dateTitle = new JLabel("July 2020");
        dateTitle.setHorizontalAlignment(SwingConstants.CENTER);
        dateTitle.setFont(new Font("Lucida Grande", Font.BOLD, 30));
        dateTitle.setBounds(77, 70, 278, 37);
        content.add(dateTitle);

        //display area for Event data
        eventData = new JTextArea();
        eventData.setBorder(new LineBorder(new Color(0, 0, 0)));
        eventData.setBounds(440, 120, 232, 363);
        JScrollPane pane = new JScrollPane(eventData);
        pane.setBounds(440, 120, 400, 363);
        content.add(pane);

        String[] weeks = {"SU","M","T","W","TH","F","SA"};
        JLabel[] labels = new JLabel[7];
        int x = 30;
        for(int i = 0; i < labels.length; i++)
        {
            labels[i] = new JLabel(weeks[i]);
            labels[i].setBounds(x, 120, 50, 40);
            labels[i].setHorizontalAlignment(SwingConstants.CENTER);
            labels[i].setBackground(Color.WHITE);
            labels[i].setOpaque(true);
            x += 54;
            content.add(labels[i]);
        }

        labels[0].setBackground(Color.lightGray);
        labels[6].setBackground(Color.lightGray);

        dateNum = new JButton[6][7];
        x = 30;
        int y = 175;
        int count = 1;
        for(int i = 0; i < dateNum.length; i++)
        {
            for(int j = 0; j < dateNum[0].length; j++)
            {
                dateNum[i][j] = new JButton(count+ "");
                count++;
                dateNum[i][j].setBounds(x, y, 50, 40);
                dateNum[i][j].setBackground(Color.WHITE);
                dateNum[i][j].addActionListener(this);
                x += 54;
                content.add(dateNum[i][j]);
            }
            x = 30;
            y += 44;
        }
    }
}
