package ui;

import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class FentryPanel extends JPanel {

    private BudgetApp budgetApp;
    private Label dateLabel;
    private JXDatePicker datePicker;
    private JComboBox expenseChoice;
    private Label label;
    private Label dollar;
    private JTextField field;
    private Label labelCategory;
    private JComboBox categoryChoice;
    private Label note;
    private Label recurrence;
    private JComboBox recurrenceChoice;
    private Label dateLabel2;
    private JXDatePicker datePicker2;
    private JButton button;
    private JTextField noteField;

    public FentryPanel(BudgetApp budgetApp) {
        super();
        this.budgetApp = budgetApp;
    }

    public void setUpFentryPanel() {
        instantiateComponents();

        setUpHelper();
        note.setBounds(100, 170, 60, 30);
        noteField = new JTextField(5);
        noteField.setBounds(100, 200, 100, 30);
        recurrence.setBounds(100,240, 220,30);
        recurrenceChoice.setBounds(160,270,100,30);
        recurrenceChoice.setLightWeightPopupEnabled(false);
        dateLabel2.setBounds(100,310,200,30);
        datePicker2.setBounds(160,340,100,30);
        datePicker2.setLightWeightPopupEnabled(false);
        button.setBounds(300, 400, 100, 30);

        addAllComponents(noteField);
        setLayout(null);
        button.addActionListener(e -> guiMakeFentry());
    }

    private void guiMakeFentry() {
        Date date = datePicker.getDate();
        if (!field.getText().isEmpty() && date != null) {
            Double amount = Double.parseDouble(field.getText());
            if (expenseChoice.getSelectedItem() == "Expense") {
                amount = -amount;
            }
            String category = (String) categoryChoice.getSelectedItem();
            int recurrence = recurrenceChoice.getSelectedIndex() + 1;
            Date endDate = datePicker2.getDate();
            if (recurrence == 1 || endDate != null) {
                budgetApp.addFutureEntry(amount, noteField.getText(), category, recurrence, date, endDate);
                removeAll();
                repaint();
                setUpFentryPanel();
                revalidate();
            }
        }
    }

    private void setUpHelper() {
        dateLabel.setBounds(100,30,200,30);
        datePicker.setBounds(160,60,100,30);
        datePicker.setLightWeightPopupEnabled(false);
        expenseChoice.setLightWeightPopupEnabled(false);
        expenseChoice.setBounds(240, 130, 100, 30);
        label.setBounds(100, 100, 50, 30);
        dollar.setBounds(80, 130, 20, 30);
        field.setBounds(100, 130, 100, 30);
        labelCategory.setBounds(240,170,200,30);
        categoryChoice.setBounds(240, 200, 100, 30);
        categoryChoice.setLightWeightPopupEnabled(false);
    }

    private void addAllComponents(JTextField noteField) {
        add(dateLabel);
        add(datePicker);
        add(expenseChoice);
        add(label);
        add(dollar);
        add(field);
        add(note);
        add(noteField);
        add(labelCategory);
        add(categoryChoice);
        add(recurrence);
        add(recurrenceChoice);
        add(dateLabel2);
        add(datePicker2);
        add(button);
    }

    private void instantiateComponents() {
        dateLabel = new Label("When is this entry scheduled for?");
        datePicker = new JXDatePicker();
        String[] expenseType = {"Expense","Income"};
        expenseChoice = new JComboBox(expenseType);
        label = new Label("Amount:");
        dollar = new Label("$");
        field = new JTextField(5);
        labelCategory = new Label("Choose Category");
        Object[] categories = budgetApp.getCategories().keySet().toArray();
        categoryChoice = new JComboBox(categories);
        note = new Label("Note:");
        recurrence = new Label("Repeat Entry?");
        String[] recurrenceType = {"No", "Daily", "Weekly", "Monthly", "Yearly"};
        recurrenceChoice = new JComboBox(recurrenceType);
        dateLabel2 = new Label("If yes, repeat until what date?");
        datePicker2 = new JXDatePicker();
        button = new JButton("Submit");
    }

}
