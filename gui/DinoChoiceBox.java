package it.polimi.dinosaursisland.gui;

import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DinoChoiceBox extends JFrame {
    private JPanel jPanel = new JPanel();
    private JComboBox dinoList;
    private JButton confirmButton = new JButton("OK");
    final private ClientGui cg;
    final private String cm;

    public DinoChoiceBox(ClientGui clientGui, String string, String dinosaurs){
        cm = string;
        cg = clientGui;
        dinosaurs = dinosaurs.replace("@listaDinosauri,", "");
        String dinos[] = dinosaurs.split(",");
        dinoList = new JComboBox(dinos);
        this.add(jPanel);
        jPanel.add(dinoList);
        jPanel.add(confirmButton);
        confirmButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if (cg.returnToken() != null && cm.equals("@statoDinosauro")){
                    String string = cm.concat(",").concat(cg.returnToken()).concat(",").concat((String) dinoList.getSelectedItem());
                    cg.sendCommandString(string);
                }

                if (cg.returnToken() != null && cm.equals("@vistaLocale")){
                    String string = cm.concat(",").concat(cg.returnToken()).concat(",").concat((String) dinoList.getSelectedItem());
                    cg.sendCommandString(string);
                }

                if (cg.returnToken() != null && cm.equals("@deponiUovo")){
                    String string = cm.concat(",").concat(cg.returnToken()).concat(",").concat((String) dinoList.getSelectedItem());
                    cg.sendCommandString(string);
                }

                if (cg.returnToken() != null && cm.equals("@cresciDinosauro")){
                    String string = cm.concat(",").concat(cg.returnToken()).concat(",").concat((String) dinoList.getSelectedItem());
                    cg.sendCommandString(string);
                    string = "@mappaGenerale,".concat(cg.returnToken());
                    cg.sendCommandString(string);
                    string = "@vistaLocale,".concat(cg.returnToken().concat(",").concat((String) dinoList.getSelectedItem()));
                    cg.sendCommandString(string);
                }
                hideIt();
            }
        });
    }
    
    public void showIt(){
        this.setTitle("Lista Dinosauri");
        this.setLocation(GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint());
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    public void hideIt(){

        this.setVisible(false);
    }
}