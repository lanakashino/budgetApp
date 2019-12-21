package ui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class EntryPanel extends JPanel {

    private BudgetApp budgetApp;
    private JComboBox expenseChoice;
    private Label label;
    private Label dollar;
    private JTextField field;
    private Label labelCategory;
    private JComboBox categoryChoice;
    private Label note;
    private JTextField noteField;
    private JButton button;

    public EntryPanel(BudgetApp budgetApp) {
        super();
        this.budgetApp = budgetApp;
    }

    public void setUpEntryPanel() {
        setUpHelper();

        Object[] categories = budgetApp.getCategories().keySet().toArray();
        categoryChoice = new JComboBox(categories);
        categoryChoice.setBounds(240, 200, 100, 30);
        categoryChoice.setLightWeightPopupEnabled(false);

        note = new Label("Note:");
        note.setBounds(100, 170, 60, 30);
        noteField = new JTextField(5);
        noteField.setBounds(100, 200, 100, 30);
        button = new JButton("Submit");
        button.setBounds(300, 400, 100, 30);

        addAllComponents();

        setLayout(null);
        button.addActionListener(e -> guiMakeEntry());
    }

    private void setUpHelper() {
        String[] expenseType = {"Expense","Income"};
        expenseChoice = new JComboBox(expenseType);
        expenseChoice.setBounds(240, 130, 100, 30);
        expenseChoice.setLightWeightPopupEnabled(false);
        label = new Label("Amount:");
        label.setBounds(100, 100, 600, 30);
        dollar = new Label("$");
        dollar.setBounds(80, 130, 20, 30);
        field = new JTextField(5);
        field.setBounds(100, 130, 100, 30);
        labelCategory = new Label("Choose Category:");
        labelCategory.setBounds(240,170,200,30);
    }

    private void addAllComponents() {
        add(expenseChoice);
        add(label);
        add(dollar);
        add(field);
        add(note);
        add(noteField);
        add(labelCategory);
        add(categoryChoice);
        add(button);
    }

    private void guiMakeEntry() {
        if (!field.getText().isEmpty()) {
            Double amount = Double.parseDouble(field.getText());
            if (expenseChoice.getSelectedItem() == "Expense") {
                amount = -amount;
            }
            String category = (String) categoryChoice.getSelectedItem();
            budgetApp.addBasicEntry(amount, noteField.getText(), LocalDate.now(), category);
            removeAll();
            setUpEntryPanel();
            revalidate();
            repaint();
        }
    }

}
