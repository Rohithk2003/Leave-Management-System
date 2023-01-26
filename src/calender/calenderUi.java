package calender;

import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatLabel;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import imageData.imageData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

class newButton extends JButton {
    private final int dayValue;
    private final int monthValue;
    FlatLabel presentLabel;

    newButton(String text, int dayValue, int monthValue, Color c) {
        super(text);
        this.dayValue = dayValue;
        this.monthValue = monthValue;
        this.setFocusable(false);
        this.setLayout(null);
        this.setHorizontalAlignment(SwingConstants.RIGHT);
        this.setVerticalAlignment(SwingConstants.TOP);
        this.setBackground(c);
        this.setFont(UIManager.getFont("h2.regular.font"));
        this.setLayout(null);
    }


    public boolean autoValidateVisibility(Date[][] leaveDates, String[][] completeData) {

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date check = format.parse(2023 + "-" + monthValue + "-" + dayValue);
            int index = 0;
            for (Date[] i : leaveDates) {
                if (check.compareTo(i[0]) >= 0 && check.compareTo(i[1]) <= 0 && !completeData[index][2].equals("Rejected")) {
                    if (completeData[index][2].equals("Under Review")) {
                        presentLabel.setBackground(Color.blue);
                    }
                    if (completeData[index][2].equals("Accepted")) {
                        presentLabel.setBackground(Color.green);
                    }

                    return true;
                }
                index++;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setVisiblity(boolean status) {
        presentLabel.setVisible(status);
    }

    public void initInnerLabel(int no) {
        presentLabel = new FlatLabel();
        presentLabel.setBounds(1, 40, 3000, 20);
        presentLabel.setFont(UIManager.getFont("large.font"));
        presentLabel.setOpaque(true);
        presentLabel.setVisible(true);
        this.add(presentLabel);
    }
}

public class calenderUi implements ActionListener {
    newButton[] buttons;
    int firstMissingDays, lastMissingDays, firstDay, lastDay, noOfRows, monthValue;
    FlatButton rightBtn;
    FlatButton leftBtn;
    String[] months = new String[12];
    JPanel parentComPanel;
    int[] previousMonthsStartDate;
    FlatLabel currentMonthYear;
    Date[][] leaveDates;
    String[][] completeData;

    public calenderUi(JPanel parentComponent, int currentMonthIndex, Date[][] leaveDates, String[][] completeData) {
        FlatMacDarkLaf.setup();
        parentComPanel = parentComponent;
        this.completeData = completeData;
        previousMonthsStartDate = new int[]{31, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30};
        this.monthValue = currentMonthIndex;
        this.leaveDates = leaveDates;
        if (monthValue > 11) {
            monthValue = 0;
        } else if (monthValue < 0) {
            monthValue = 11;
        }
        calenderDataLoader();

        JPanel topCalenderUiPanel = new JPanel();
        topCalenderUiPanel.setLayout(null);
        topCalenderUiPanel.setPreferredSize(new Dimension(500, 80));

        currentMonthYear = new FlatLabel();
        currentMonthYear.setText(months[monthValue].toUpperCase() + " 2023");
        currentMonthYear.setHorizontalAlignment(SwingConstants.CENTER);
        currentMonthYear.setFont(new Font("Segoe Ui", Font.PLAIN, 33));


        rightBtn = new FlatButton();
        rightBtn.setIcon(imageData.rightIcon);
        rightBtn.setFocusable(false);
        rightBtn.setButtonType(FlatButton.ButtonType.roundRect);
        rightBtn.addActionListener(this);

        leftBtn = new FlatButton();
        leftBtn.setIcon(imageData.leftIcon);
        leftBtn.setFocusable(false);
        leftBtn.setButtonType(FlatButton.ButtonType.roundRect);
        leftBtn.addActionListener(this);

        JLayeredPane panel = new JLayeredPane();
        panel.setLayout(new GridLayout(noOfRows, 7, -1, 0));

        buttons = new newButton[35 + lastMissingDays];
        JLabel[] days = new JLabel[7];
        String[] daysString = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

        for (int i = 0; i < 7; i++) {
            days[i] = new JLabel(daysString[i]);
            days[i].setFont(UIManager.getFont("h1.font"));
            days[i].setFocusable(false);
            days[i].setEnabled(false);
            days[i].setHorizontalAlignment(JLabel.CENTER);
            panel.add(days[i]);

        }
        int buttonIndexValue;
        int val = previousMonthsStartDate[monthValue];
        for (buttonIndexValue = 0; buttonIndexValue < firstMissingDays; buttonIndexValue++) {
            int value = val - firstMissingDays + 1;
            buttons[buttonIndexValue] = new newButton(Integer.toString(value), value, monthValue, new Color(102, 102, 102));
            buttons[buttonIndexValue].initInnerLabel(buttonIndexValue);

            buttons[buttonIndexValue].setVisiblity(buttons[buttonIndexValue].autoValidateVisibility(leaveDates, completeData));
            panel.add(buttons[buttonIndexValue]);
            val++;
        }
        int i;
        for (i = firstDay; i < lastDay + 1; i++, buttonIndexValue++) {

            String buttonValue = Integer.toString(i);
            buttons[buttonIndexValue] = new newButton(buttonValue, i, this.monthValue + 1, new Color(61, 61, 61));
            buttons[buttonIndexValue].initInnerLabel(buttonIndexValue);
            buttons[buttonIndexValue].setVisiblity(buttons[buttonIndexValue].autoValidateVisibility(leaveDates, completeData));
            panel.add(buttons[buttonIndexValue]);

        }
        for (int j = 1; j <= lastMissingDays; j++, i++) {
            buttons[i] = new newButton(Integer.toString(j), j, monthValue + 2, new Color(94, 94, 94));
            buttons[i].initInnerLabel(i);
            buttons[i].setVisiblity(buttons[i].autoValidateVisibility(leaveDates, completeData));
            panel.add(buttons[i]);
        }
        panel.setBounds(30, 87, parentComponent.getWidth() - 50, 650);
        currentMonthYear.setBounds(0, 20, parentComponent.getWidth(), 40);
        rightBtn.setBounds(940, 20, 50, 50);
        leftBtn.setBounds(50, 20, 50, 50);


        JButton acceptedButton = new JButton();
        acceptedButton.setText("Accepted");
        acceptedButton.setBackground(Color.green);
        acceptedButton.setHorizontalAlignment(FlatLabel.CENTER);
        acceptedButton.setForeground(Color.black);
        acceptedButton.setFocusable(false);
        acceptedButton.setBounds(280, 30, 90, 30);
        acceptedButton.setOpaque(true);

        JButton underReviewButton = new JButton();
        underReviewButton.setText("Under Review");
        underReviewButton.setBackground(Color.blue);
        underReviewButton.setFocusable(false);
        underReviewButton.setHorizontalAlignment(FlatLabel.CENTER);
        underReviewButton.setForeground(Color.black);
        underReviewButton.setBounds(120, 30, 120, 30);
        underReviewButton.setOpaque(true);

        topCalenderUiPanel.add(leftBtn);
        topCalenderUiPanel.add(currentMonthYear);
        topCalenderUiPanel.add(acceptedButton);
        topCalenderUiPanel.add(underReviewButton);
        topCalenderUiPanel.add(rightBtn);

        parentComponent.add(topCalenderUiPanel, BorderLayout.NORTH);
        parentComponent.add(panel, BorderLayout.CENTER);
    }

    public void calenderDataLoader() {
        try {
            File dataFile = new File("src\\calender\\data.txt");
            Scanner reader = new Scanner(dataFile);
            String data;
            String[] splitData;
            int i = 0;
            while (reader.hasNext()) {
                data = reader.nextLine();
                splitData = data.split(" ");
                months[i] = splitData[0];
                i++;
                if (splitData[0].equals(months[monthValue])) {
                    noOfRows = Integer.parseInt(splitData[1]);
                    firstMissingDays = Integer.parseInt(splitData[2]);
                    lastMissingDays = Integer.parseInt(splitData[3]);
                    firstDay = Integer.parseInt(splitData[4]);
                    lastDay = Integer.parseInt(splitData[5]);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == rightBtn) {
            parentComPanel.removeAll();
            new calenderUi(parentComPanel, monthValue + 1, leaveDates, completeData);
            parentComPanel.repaint();
            parentComPanel.revalidate();
        }
        if (e.getSource() == leftBtn) {
            parentComPanel.removeAll();
            new calenderUi(parentComPanel, monthValue - 1, leaveDates, completeData);
            parentComPanel.repaint();
            parentComPanel.repaint();
            parentComPanel.revalidate();
        }
    }


}
