package javawork_view;

import javawork_model.DistanceCost;
import javawork_service.DistanceService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * MainGUI show the main interface
 *
 * @author Jiawei Huang
 * 04/10/2021
 */
public class MainGUI extends JFrame {
    private JPanel contentPanel;
    private JScrollPane scrollPane;
    private JTable table;
    private JTextArea textArea;
    private JLabel startName;
    private JLabel endName;
    private JLabel arrow;
    private JComboBox startBox;
    private JComboBox endBox;
    private JButton calculatorButton;
    private JButton saveButton;
    private JPanel panel1;

    /**
     * create a new thread to run the program
     * @param args
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainGUI frame = new MainGUI();
                frame.setVisible(true);
            }
        });
    }

    /**
     * constructor to init interface and related parameters
     */
    public MainGUI() {
        setTitle("Best Route Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setBounds(200,120,1000, 688);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        contentPanel = new JPanel();
        setContentPane(contentPanel);
        contentPanel.setLayout(null);

        //region Show up DataTable
        String head[] = new String[]{"ID", "Start ID", "Start", "End ID", "End", "Distance(km)", "Cost($)"};

        Object data[][] = DistanceService.getData();
        DefaultTableModel defaultTableModel = new DefaultTableModel(data, head) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 5 || column == 6) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        table = new JTable(defaultTableModel);
        table.getTableHeader().setReorderingAllowed(false); //column cannot move
        //hide the first column
        table.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        table.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        table.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(0);
        table.setBorder(BorderFactory.createLineBorder(Color.black)); //set Border color
        table.setGridColor(Color.black); //set Gird Cell color

        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(14, 55, 380, 300);
        contentPanel.add(scrollPane);
        contentPanel.setVisible(true);
        //endregion

        //region save button
        saveButton = new JButton("SAVE");
        saveButton.setBounds(280, 360, 100, 30);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowNum = table.getRowCount();
                List<DistanceCost> list = new ArrayList<DistanceCost>();
                for (int i = 0; i < rowNum; i++) {
                    DistanceCost distanceCost = new DistanceCost();
                    distanceCost.setId(Integer.parseInt(table.getValueAt(i, 0).toString()));
                    distanceCost.setDistance(Float.parseFloat(table.getValueAt(i, 5).toString()));
                    distanceCost.setCost(Float.parseFloat(table.getValueAt(i, 6).toString()));
                    list.add(distanceCost);
                }

                if (DistanceService.insertData(list)) {
                    resetAllLabels();
                    JOptionPane.showMessageDialog(null, "Save Successfully", "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Save Failed, " +
                                    "Please check if the data is complete", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        contentPanel.add(saveButton);
        //endregion

        //region TextArea to display the calculation result
        textArea = new JTextArea("Welcome to use BestRouteCalculator!\n");
        textArea.setBounds(14, 650, 1400, 150);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("myFont", Font.BOLD, 20));
        textArea.setForeground(Color.DARK_GRAY);
        textArea.setBorder(BorderFactory.createLineBorder(Color.blue));
        textArea.setEditable(false);

        contentPanel.add(textArea);
        //endregion

        //region Select two place to calculator
        startName = new JLabel("Departure");
        startName.setBounds(35, 400, 90, 20);
        contentPanel.add(startName);

        startBox = new JComboBox();
        startBox.setBounds(14, 420, 125, 30);
        contentPanel.add(startBox);

        endName = new JLabel("Destination");
        endName.setBounds(270, 400, 90, 20);
        contentPanel.add(endName);

        endBox = new JComboBox();
        endBox.setBounds(250, 420, 125, 30);
        contentPanel.add(endBox);

        for (int i = 1; i <= DistanceService.idToName.size(); i++) {
            String item = i + ". " + DistanceService.idToName.get(i);
            startBox.addItem(item);
            endBox.addItem(item);
        }
        endBox.setSelectedIndex(2);

        arrow = new JLabel("<---------->");
        arrow.setBounds(143, 420, 100, 20);
        contentPanel.add(arrow);
        //endregion

        //region calculator Button
        calculatorButton = new JButton("calculate");
        calculatorButton.setBounds(143, 450, 100, 50);
        contentPanel.add(calculatorButton);

        calculatorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
                int startId = startBox.getSelectedIndex() + 1;
                int endId = endBox.getSelectedIndex() + 1;
                DistanceService distanceService = new DistanceService();
                textArea.append(distanceService.getShortestDistance(startId, endId) + "\r\n\r\n");
                textArea.append(distanceService.getLowestCostPath(startId, endId));
                showDistancePath(distanceService.getShortestId(startId, endId));
                showCostPath(distanceService.getLowestId(startId, endId));
            }
        });
        //endregion

        setDistanceLabel(data);

        JPanel panel = new ImagePanel();
        panel.setBounds(400, 50, 1000, 580);
        contentPanel.add(panel);
    }

    /**
     * set the text about distance and cost on the map
     * @param data two dimensional array containing route information
     */
    public void setDistanceLabel(Object[][] data) {
        int rowNum = table.getRowCount();
        for (int i = 0; i < rowNum; i++) {
            JLabel label = new JLabel();
            label.setText("<html><body>" + data[i][5] + "km<br>$" + data[i][6] + "</body></html>");
            if (i == 0) {
                label.setBounds(1100, 230, 120, 60);
                label.setName("12");
            } else if (i == 1) {
                label.setBounds(1170, 370, 120, 60);
                label.setName("16");
            } else if (i == 2) {
                label.setBounds(1020, 310, 120, 60);
                label.setName("15");
            } else if (i == 3) {
                label.setBounds(1010, 240, 120, 60);
                label.setName("25");
            } else if (i == 4) {
                label.setBounds(800, 220, 120, 60);
                label.setName("23");
            } else if (i == 5) {
                label.setBounds(630, 350, 120, 60);
                label.setName("34");
            } else if (i == 6) {
                label.setBounds(750, 300, 120, 60);
                label.setName("35");
            } else if (i == 7) {
                label.setBounds(790, 380, 120, 60);
                label.setName("45");
            } else if (i == 8) {
                label.setBounds(1050, 390, 120, 60);
                label.setName("56");
            }
            contentPanel.add(label);
        }
    }

    /**
     * display the shortest distance route by changing the font color
     * @param idPath the id String of the shortest route passing places
     */
    public void showDistancePath(String idPath) {
        resetAllLabels();
        List<JLabel> labels = getJLabelObject(contentPanel);
        char start;
        char end;
        for (int i = 0; i < idPath.length() - 1; i++) {
            start = idPath.charAt(i);
            end = idPath.charAt(i+1);
            for(JLabel label : labels) {
                if((label.getName() != null)
                        && ((label.getName().charAt(0) == start && label.getName().charAt(1) == end)
                ||(label.getName().charAt(1) == start && label.getName().charAt(0) == end))) {
                    label.setForeground(Color.green);
                }
            }
        }
    }

    /**
     * display the lowest cost route by changing the font color
     * @param idPath the id String of the lowest route passing places
     */
    public void showCostPath(String idPath) {
        List<JLabel> labels = getJLabelObject(contentPanel);
        char start;
        char end;
        for (int i = 0; i < idPath.length() - 1; i++) {
            start = idPath.charAt(i);
            end = idPath.charAt(i+1);
            for(JLabel label : labels) {
                if((label.getName() != null) && ((label.getName().charAt(0) == start && label.getName().charAt(1) == end)
                        ||(label.getName().charAt(1) == start && label.getName().charAt(0) == end))) {
                    label.setForeground(Color.MAGENTA);
                }
            }
        }
    }

    /**
     * reload the distance and cost text on the map and reset the font color to black
     */
    public void resetAllLabels() {
        Object[][] data = DistanceService.getData();

        List<JLabel> labels = getJLabelObject(contentPanel);
        for(JLabel label : labels) {
            label.setForeground(Color.black);
            if((label.getName() != null)) {
                if(label.getName().equals("12")) {
                    label.setText("<html><body>" + data[0][5] + "km<br>$" + data[0][6] + "</body></html>");
                } else if(label.getName().equals("16")) {
                    label.setText("<html><body>" + data[1][5] + "km<br>$" + data[1][6] + "</body></html>");
                } else if(label.getName().equals("15")) {
                    label.setText("<html><body>" + data[2][5] + "km<br>$" + data[2][6] + "</body></html>");
                } else if(label.getName().equals("25")) {
                    label.setText("<html><body>" + data[3][5] + "km<br>$" + data[4][6] + "</body></html>");
                } else if(label.getName().equals("23")) {
                    label.setText("<html><body>" + data[4][5] + "km<br>$" + data[4][6] + "</body></html>");
                } else if(label.getName().equals("34")) {
                    label.setText("<html><body>" + data[5][5] + "km<br>$" + data[5][6] + "</body></html>");
                } else if(label.getName().equals("35")) {
                    label.setText("<html><body>" + data[6][5] + "km<br>$" + data[6][6] + "</body></html>");
                } else if(label.getName().equals("45")) {
                    label.setText("<html><body>" + data[7][5] + "km<br>$" + data[7][6] + "</body></html>");
                } else if(label.getName().equals("56")) {
                    label.setText("<html><body>" + data[8][5] + "km<br>$" + data[8][6] + "</body></html>");
                }
            }
        }
    }

    /**
     * get all JLabel component
     * @param component
     * @return A list contains all JLabel component
     */
    public List<JLabel> getJLabelObject(Component component) {
        List<JLabel> result = new ArrayList<>();
        if (component instanceof JPanel) {
            for (Component com : ((JPanel) component).getComponents()) {
                List<JLabel> labels = getJLabelObject(com);
                result.addAll(labels);
            }
        } else {
            if (component instanceof JLabel) {
                result.add((JLabel) component);
            }
        }
        return result;
    }

    /**
     * set the map as background
     */
    class ImagePanel extends JPanel {
        public void paint(Graphics g) {
            super.paint(g);
            ImageIcon icon = new ImageIcon("src/image/world-map-route.jpg");
            g.drawImage(icon.getImage(), 0, 0, 1000, 580, this);
        }
    }
}
