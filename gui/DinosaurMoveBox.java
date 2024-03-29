package it.polimi.dinosaursisland.gui;

import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DinosaurMoveBox extends JFrame {
    private JPanel jPanel = new JPanel(new FlowLayout());
    private JComboBox dinoList;
    private JTextField xField = new JTextField(2);
    private JTextField yField = new JTextField(2);
    private JLabel xLabel = new JLabel("X:");
    private JLabel yLabel = new JLabel("Y:");
    private JButton confirmButton = new JButton("OK");
    private ClientGui cg;
    private String string;

    public DinosaurMoveBox(ClientGui clientGui, String dinosaurs){
        cg = clientGui;
        this.add(jPanel);
        dinosaurs = dinosaurs.replace("@listaDinosauri,", "");
        String dinos[] = dinosaurs.split(",");
        dinoList = new JComboBox(dinos);
        jPanel.add(dinoList);
        jPanel.add(xLabel);
        jPanel.add(xField);
        jPanel.add(yLabel);
        jPanel.add(yField);
        jPanel.add(confirmButton);
        jPanel.add(confirmButton);

        confirmButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                string = "@muoviDinosauro,".concat(cg.returnToken()).concat(",").concat((String) dinoList.getSelectedItem()).concat(",{").concat(xField.getText()).concat(",").concat(yField.getText()).concat("}");
                cg.sendCommandString(string);
                cg.sendCommandString("@vistaLocale,".concat(cg.returnToken()).concat(",").concat((String) dinoList.getSelectedItem()));
                cg.sendCommandString("@mappaGenerale,".concat(cg.returnToken()));
                hideIt();
            }
        });
    }

    public void showIt(){
        this.setLocation(GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint());
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    public void hideIt(){
        this.setVisible(false);
    }
}
