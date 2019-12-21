package ui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.IOException;



public class Gui extends JFrame {
    private BudgetApp budgetApp;
    private JPanel container;
    private JPanel homePanel;
    private EntryPanel entryPanel;
    private FentryPanel fentryPanel;
    private JPanel printPanel;
    private JPanel categoryPanel;
    private JPanel bmPanel;
    private CardLayout cardLayout;

    private JButton entryButton;
    private JButton printButton;
    private JButton exitButton;
    private JButton fentryButton;
    private JButton categoryButton;
    private JButton bmButton;
    private JButton homeButton;

    public Gui(BudgetApp budgetApp) {
        super("mainFrame");
        this.budgetApp = budgetApp;
        entryPanel = new EntryPanel(budgetApp);
        fentryPanel = new FentryPanel(budgetApp);
    }

    public void mainPanel() throws IOException {
        setUpPanels();

        container.add(homePanel, "homePanel");
        container.add(entryPanel, "entryPanel");
        container.add(fentryPanel, "fentryPanel");
        container.add(printPanel, "printPanel");
        container.add(categoryPanel, "categoryPanel");
        container.add(bmPanel, "bmPanel");

        cardLayout.show(container, "homePanel");

        JPanel buttons = new JPanel();
        setUpButtonsPanel(buttons);

        setLayout(new BorderLayout());
        add(buttons, BorderLayout.WEST);
        add(container);
        pack();
        setSize(900,600);
        setVisible(true);
    }

    private void setUpPanels() throws IOException {
        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);
        homePanel = new JPanel();
        printPanel = new JPanel();
        categoryPanel = new JPanel();
        bmPanel = new JPanel();

        budgetApp.printHomePage(homePanel);
        homePanel.setLayout(null);
        entryPanel.setUpEntryPanel();
        fentryPanel.setUpFentryPanel();
        setUpCategoryPanel(categoryPanel);
        setUpBmPanel(bmPanel);
    }




    private void setUpCategoryPanel(JPanel categoryPanel) {
        Label label = new Label("Name of the new Category:");
        label.setBounds(140, 50, 600, 30);
        JTextField field = new JTextField(5);
        field.setBounds(160, 100, 100, 30);
        JButton button = new JButton("Submit");
        button.setBounds(300, 400, 100, 30);
        categoryPanel.add(label);
        categoryPanel.add(field);
        categoryPanel.add(button);
        categoryPanel.setLayout(null);
        button.addActionListener(e -> guiMakeCategory(field.getText()));
    }

    private void guiMakeCategory(String field) {
        if (!field.isEmpty()) {
            budgetApp.addNewCategory(field);
            categoryPanel.removeAll();
            categoryPanel.repaint();
            setUpCategoryPanel(categoryPanel);
            categoryPanel.revalidate();
        }
    }

    private void setUpBmPanel(JPanel bmPanel) {
        Label label = new Label("After going past what balance value would you like to be additionally notified?");
        label.setBounds(10, 50, 600, 30);
        Label dollar = new Label("$");
        dollar.setBounds(140, 100, 20, 30);
        JTextField field = new JTextField(5);
        field.setBounds(160, 100, 100, 30);
        JButton button = new JButton("Submit");
        button.setBounds(300, 400, 100, 30);
        bmPanel.add(label);
        bmPanel.add(dollar);
        bmPanel.add(field);
        bmPanel.add(button);
        bmPanel.setLayout(null);
        button.addActionListener(e -> guiMakeBalanceMonitor(field));
    }

    private void guiMakeBalanceMonitor(JTextField field) {
        if (!field.getText().isEmpty()) {
            budgetApp.addNewBalanceMonitor(field.getText());
            bmPanel.removeAll();
            bmPanel.repaint();
            setUpBmPanel(bmPanel);
            bmPanel.revalidate();
        }
    }

    private void setUpButtonsPanel(JPanel buttons) {
        entryButton = new JButton("add new Entry");
        entryButton.addActionListener(e -> cardLayout.show(container, "entryPanel"));
        printButton = new JButton("Print financial report");
        printButton.addActionListener(e -> printButtonAction());
        exitButton = new JButton("Save and exit");
        exitButton.addActionListener(e -> closeGui());
        fentryButton = new JButton("add new Future Entry");
        fentryButton.addActionListener(e -> cardLayout.show(container, "fentryPanel"));
        categoryButton = new JButton("Add new category");
        categoryButton.addActionListener(e -> cardLayout.show(container, "categoryPanel"));
        bmButton = new JButton("Add new balance monitor");
        bmButton.addActionListener(e -> cardLayout.show(container, "bmPanel"));
        homeButton = new JButton("Home");
        homeButton.addActionListener(e -> cardLayout.show(container, "homePanel"));

        buttons.setLayout(new GridLayout(7,1));
        buttons.setSize(300,600);
        addButtons(buttons);
    }

    private void addButtons(JPanel buttons) {
        buttons.add(homeButton);
        buttons.add(entryButton);
        buttons.add(printButton);
        buttons.add(fentryButton);
        buttons.add(categoryButton);
        buttons.add(bmButton);
        buttons.add(exitButton);
    }

    private void printButtonAction() {
        cardLayout.show(container, "printPanel");
        printPanel.removeAll();
        printPanel.repaint();
        budgetApp.printFinancialReport(printPanel);
        printPanel.setLayout(null);
    }

    private void closeGui() {
        budgetApp.save();
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

}
