package main.screens;

import main.scoresRecord.Player;
import main.scoresRecord.Scores;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class HighScoreScreen extends JPanel {

    private JButton backButton;
    private DefaultTableModel model;


    public HighScoreScreen(CardLayout cardLayout, JPanel cardPanel) {
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(173, 216, 230));

        // Title Label
        JLabel titleLabel = new JLabel("TOP 10 SCORE", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK); // Title color
        titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0)); // Add some padding around the title
        this.add(titleLabel, BorderLayout.NORTH);

        List<Player> playerList = Scores.getTopScores();
        // Top 10 Scores List
        String[] columnNames = {"Player Name", "Score"};
        model = new DefaultTableModel(columnNames, 0); // Start with empty model


        JTable scoreTable = new JTable(model);
        scoreTable.setFillsViewportHeight(true);
        scoreTable.setRowHeight(40); // Adjust row height for better readability

        // Center text in cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        scoreTable.setDefaultRenderer(Object.class, centerRenderer);
        scoreTable.setBackground(new Color(204, 255, 204));


        // Adjust column widths
        TableColumnModel columnModel = scoreTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(200); // Player Name column
        columnModel.getColumn(1).setPreferredWidth(100); // Score column


        // Set table header font and alignment
        JTableHeader header = scoreTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setBackground(new Color(34, 139, 34));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        JScrollPane scrollPane = new JScrollPane(scoreTable);

        // Create a panel to set padding color
        JPanel paddedPanel = new JPanel();
        paddedPanel.setBackground(new Color(173, 216, 230));
        paddedPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding around the table
        paddedPanel.add(scrollPane);

        // Add Components
        this.add(paddedPanel, BorderLayout.CENTER);

        // Back Button
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 12)); // Smaller font
        backButton.setBackground(new Color(173, 216, 230));
        backButton.setForeground(Color.BLACK); // Button text color
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(150, 30)); // Smaller button size

        // Panel to hold the back button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(173, 216, 230));
        buttonPanel.add(backButton);

        backButton.addActionListener(new BackButtonListener(cardLayout, cardPanel));
        this.add(buttonPanel, BorderLayout.SOUTH);
        backButton.setBackground(Color.DARK_GRAY);
        backButton.setForeground(Color.WHITE);

        //Space after the back
        JPanel spacer = new JPanel();
        spacer.setPreferredSize(new Dimension(0, 180));
        buttonPanel.add(spacer);
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            refreshScores();
        }
    }

    private void refreshScores(){
        List<Player> playerList = Scores.getTopScores();
        model.setRowCount(0); // Clear existing rows in the table model
        Object[][] data = convertListToTable(playerList);
        for (Object[] row : data) {
            model.addRow(row); // Add rows to the model
        }
    }
    private Object[][] convertListToTable(List<Player> playerList){
        Object[][]  data = new Object[playerList.size()][2];
        for (int i = 0; i< playerList.size(); i++){
            data[i][0] = playerList.get(i).getName();
            data[i][1] = playerList.get(i).getScore();
        }
        return data;
    }

    private class BackButtonListener implements ActionListener {
        private CardLayout cardLayout;
        private JPanel cardPanel;

        public BackButtonListener(CardLayout cardLayout, JPanel cardPanel) {
            this.cardLayout = cardLayout;
            this.cardPanel = cardPanel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            cardLayout.show(cardPanel, "MainScreen");
        }
    }
}
