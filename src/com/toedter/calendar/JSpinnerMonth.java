/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toedter.calendar;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Fabián
 */
public class JSpinnerMonth implements ItemListener,
        ChangeListener {

    private static final long serialVersionUID = -2028361332231218527L;

    /**
     * true, if the month chooser has a spinner component
     */
    protected boolean hasSpinner;

    private Locale locale;

    private int month;

    private int oldSpinnerValue = 0;

    // needed for comparison
    private JDayChooser dayChooser;

    private JYearChooser yearChooser;

    private JComboBox comboBox;

    private JSpinner spinner;

    private boolean initialized;

    private boolean localInitialize;

    public JSpinnerMonth() {
        spinner = new JSpinner();
        this.hasSpinner = true;

        comboBox = new JComboBox();
        comboBox.addItemListener(this);

        // comboBox.addPopupMenuListener(this);
        locale = Locale.getDefault();
        initNames();

        spinner.setEditor(comboBox);
        spinner.addChangeListener(this);

        initialized = true;
        setMonth(Calendar.getInstance().get(Calendar.MONTH));
    }

    /**
     * Initializes the locale specific month names.
     */
    public void initNames() {
        localInitialize = true;

        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
        String[] monthNames = dateFormatSymbols.getMonths();

        if (comboBox.getItemCount() == 12) {
            comboBox.removeAllItems();
        }

        for (int i = 0; i < 12; i++) {
            comboBox.addItem(monthNames[i]);
        }

        localInitialize = false;
        comboBox.setSelectedIndex(month);
    }

    /**
     * Is invoked if the state of the spinner changes.
     *
     * @param e the change event.
     */
    public void stateChanged(ChangeEvent e) {
        SpinnerNumberModel model = (SpinnerNumberModel) ((JSpinner) e
                .getSource()).getModel();
        int value = model.getNumber().intValue();
        boolean increase = (value > oldSpinnerValue) ? true : false;
        oldSpinnerValue = value;

        int month = getMonth();

        if (increase) {
            month += 1;

            if (month == 12) {
                month = 0;

                if (yearChooser != null) {
                    int year = yearChooser.getYear();
                    year += 1;
                    yearChooser.setYear(year);
                }
            }
        } else {
            month -= 1;

            if (month == -1) {
                month = 11;

                if (yearChooser != null) {
                    int year = yearChooser.getYear();
                    year -= 1;
                    yearChooser.setYear(year);
                }
            }
        }

        setMonth(month);
    }

    /**
     * The ItemListener for the months.
     *
     * @param e the item event
     */
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            int index = comboBox.getSelectedIndex();

            if ((index >= 0) && (index != month)) {
                setMonth(index, false);
            }
        }
    }

    /**
     * Sets the month attribute of the JMonthChooser object. Fires a property
     * change "month".
     *
     * @param newMonth the new month value
     * @param select true, if the month should be selcted in the combo box.
     */
    private void setMonth(int newMonth, boolean select) {
        if (!initialized || localInitialize) {
            return;
        }

        int oldMonth = month;
        month = newMonth;

        if (select) {
            comboBox.setSelectedIndex(month);
        }

        if (dayChooser != null) {
            dayChooser.setMonth(month);
        }

        spinner.firePropertyChange("month", oldMonth, month);
    }

    /**
     * Sets the month. This is a bound property. Valuse are valid between 0
     * (January) and 11 (December). A value < 0 will be treated as 0, a value >
     * 11 will be treated as 11.
     *
     * @param newMonth the new month value
     *
     * @see #getMonth
     */
    public void setMonth(int newMonth) {
        if (newMonth < 0 || newMonth == Integer.MIN_VALUE) {
            setMonth(0, true);
        } else if (newMonth > 11) {
            setMonth(11, true);
        } else {
            setMonth(newMonth, true);
        }
    }

    /**
     * Returns the month.
     *
     * @return the month value
     */
    public int getMonth() {
        return month;
    }

    /**
     * Convenience method set a day chooser.
     *
     * @param dayChooser the day chooser
     */
    public void setDayChooser(JDayChooser dayChooser) {
        this.dayChooser = dayChooser;
    }

    /**
     * Convenience method set a year chooser. If set, the spin for the month
     * buttons will spin the year as well
     *
     * @param yearChooser the new yearChooser value
     */
    public void setYearChooser(JYearChooser yearChooser) {
        this.yearChooser = yearChooser;
    }

    /**
     * Returns the locale.
     *
     * @return the locale value
     *
     * @see #setLocale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Set the locale and initializes the new month names.
     *
     * @param l the new locale value
     *
     * @see #getLocale
     */
    public void setLocale(Locale l) {
        if (!initialized) {
            spinner.setLocale(l);
        } else {
            locale = l;
            initNames();
        }
    }

    /**
     * Enable or disable the JMonthChooser.
     *
     * @param enabled the new enabled value
     */
    public void setEnabled(boolean enabled) {
        spinner.setEnabled(enabled);
        comboBox.setEnabled(enabled);

        if (spinner != null) {
            spinner.setEnabled(enabled);
        }
    }

    /**
     * Returns the month chooser's comboBox text area (which allow the focus to
     * be set to it).
     *
     * @return the combo box
     */
    public Component getComboBox() {
        return this.comboBox;
    }

    /**
     * Returns the month chooser's comboBox bar (which allow the focus to be set
     * to it).
     *
     * @return Component the spinner or null, if the month chooser has no
     * spinner
     */
    public Component getSpinner() {
        // Returns <null> if there is no spinner.
        return spinner;
    }

    /**
     * Returns the type of spinner the month chooser is using.
     *
     * @return true, if the month chooser has a spinner
     */
    public boolean hasSpinner() {
        return hasSpinner;
    }

    /**
     * Sets the font for this component.
     *
     * @param font the desired <code>Font</code> for this component
     */
    public void setFont(Font font) {
        if (comboBox != null) {
            comboBox.setFont(font);
        }
        spinner.setFont(font);
    }

    /**
     * Creates a JFrame with a JMonthChooser inside and can be used for testing.
     *
     * @param s The command line arguments
     */
    public static void main(String[] s) {
        JFrame frame = new JFrame("MonthChooser");
        frame.getContentPane().add(new JMonthChooserSpinner());
        frame.pack();
        frame.setVisible(true);
    }
}
