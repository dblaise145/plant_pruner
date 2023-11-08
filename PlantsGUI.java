import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class PlantsGUI {
    // GUI Code
    private JPanel panel;
    private static JTextArea infoArea;
    private static ArrayList<Integer> currentPanelIndex;
    public PlantsGUI(String imageUrl, String info) {
        panel = new JPanel(new BorderLayout());

        try {
            URL url = new URL(imageUrl);
            ImageIcon imageIcon = new ImageIcon(url);
            JLabel imageLabel = new JLabel(imageIcon);
            panel.add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        infoArea = new JTextArea(info);
        infoArea.setWrapStyleWord(true);
        infoArea.setLineWrap(true);
        infoArea.setEditable(false);
        infoArea.setBorder(new EmptyBorder(2, 2, 2, 2)); // Add some padding
        panel.add(infoArea, BorderLayout.PAGE_END);
    }

    public JPanel getPanel() {
        return panel;
    }

    public static void updatePlants(ArrayList<Plant> plants, JPanel cardsPanel) {
        cardsPanel.removeAll();
        for (Plant plant : plants) {
            cardsPanel.add(new PlantsGUI(plant.image, plant.toString()).getPanel());
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
        currentPanelIndex.set(0, 1);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Plants GUI");
                JPanel cardsPanel = new JPanel(new CardLayout());
                currentPanelIndex = new ArrayList<>();
                currentPanelIndex.add(1); 
                ArrayList<Plant> plants = new ArrayList<Plant>();
                Plants.readCSV(plants);
                updatePlants(plants, cardsPanel);

                JButton leftButton = new JButton("<");
                leftButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if(currentPanelIndex.get(0) != 1) {
                            CardLayout cl = (CardLayout) (cardsPanel.getLayout());
                            cl.previous(cardsPanel);
                            currentPanelIndex.set(0, currentPanelIndex.get(0) - 1);
                        }
                    }
                });

                JButton rightButton = new JButton(">");
                rightButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if(currentPanelIndex.get(0) != cardsPanel.getComponentCount()) {
                            CardLayout cl = (CardLayout) (cardsPanel.getLayout());
                            cl.next(cardsPanel);
                            currentPanelIndex.set(0, currentPanelIndex.get(0) + 1);
                        }
                    }
                });

                JButton dateButton = new JButton("Enter New Prune Date");
                dateButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String dateString = JOptionPane.showInputDialog(frame, "Enter date (format: yyyy-MM-dd)");
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        try {
                            LocalDate date = LocalDate.parse(dateString, formatter);
                            Plants.updatePruneInfo(currentPanelIndex.get(0), date);
                            Plants.readCSV(plants);
                            updatePlants(plants, cardsPanel);
                        } catch (DateTimeParseException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                JButton addButton = new JButton("Add New Plant");
                    addButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String nameS = JOptionPane.showInputDialog(frame, "Enter plant scientific name:");
                        String nameC = JOptionPane.showInputDialog(frame, "Enter plant common name:");
                        String imageUrl = JOptionPane.showInputDialog(frame, "Enter image URL:");
                        String pruneInfo = JOptionPane.showInputDialog(frame, "Enter prune info:");
                        LocalDate pruneDate = LocalDate.parse(JOptionPane.showInputDialog(frame, "Enter prune date (format: yyyy-MM-dd):"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        int[] lastP = new int[3];
                        lastP[0] = pruneDate.getYear();
                        lastP[1] = pruneDate.getMonthValue();
                        lastP[2] = pruneDate.getDayOfMonth();
                        Plant newPlant;
                        try {
                            newPlant = new Plant(nameS, nameC, imageUrl, pruneInfo, lastP);
                            Plants.addPlant(newPlant);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        Plants.readCSV(plants);
                        updatePlants(plants, cardsPanel);
                    }
                });

                frame.add(leftButton, BorderLayout.WEST);
                frame.add(rightButton, BorderLayout.EAST);
                frame.add(cardsPanel, BorderLayout.CENTER);
                frame.add(dateButton, BorderLayout.SOUTH);
                frame.add(addButton, BorderLayout.NORTH);

                frame.setSize(600, 600);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}