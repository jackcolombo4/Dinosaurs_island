package it.polimi.dinosaursisland.Gui;

import java.awt.*;
import javax.swing.*;
import it.polimi.dinosaursisland.mappa.*;
import it.polimi.dinosaursisland.dinosauri.*;
import it.polimi.dinosaursisland.partita.*;
import java.util.ArrayList;

public class MapFrame extends JFrame{
    private JPanel mapPanel = new JPanel();
    private JFrame mapFrame = new JFrame("Mappa!");
    private Map map;
    private String mapString;

    public MapFrame(Map map){
        this.map = map;
        initComponents();

    }
    public MapFrame(String string){
        mapString = string;
        initComponentsString(mapString);

    }
    public void initComponents(){
        mapPanel.setLayout(new GridLayout(40, 40));
        mapFrame.add(mapPanel);
        for (int i = 39; i >= 0; i--){
            for (int j = 0; j < 40; j++){
                if (map.ReturnBox(i, j) instanceof WaterBox){
                    mapPanel.add(new JLabel(new ImageIcon("water.gif")));
                }
                if (map.ReturnBox(i, j) instanceof GroundBox){
                    Dinosaur dino = (((GroundBox) map.ReturnBox(i, j)).isDinosaurPres());
                    if ((((GroundBox) map.ReturnBox(i, j)).isCarrionPres()) != null && ((((GroundBox) map.ReturnBox(i, j)).isDinosaurPres()) == null)){
                        mapPanel.add(new JLabel(new ImageIcon("carrion.gif")));
                    }
                    if ((((GroundBox) map.ReturnBox(i, j)).isVegetationPres()) != null && ((((GroundBox) map.ReturnBox(i, j)).isDinosaurPres()) == null)){
                        mapPanel.add(new JLabel(new ImageIcon("vegetation.gif")));
                    }

                    if (dino != null){
                        mapPanel.add(new JLabel(new ImageIcon("dinosaur.gif")));
                    }
                    if (((((GroundBox) map.ReturnBox(i, j)).isCarrionPres()) == null) && ((((GroundBox) map.ReturnBox(i, j)).isVegetationPres()) == null) && (((GroundBox) map.ReturnBox(i, j)).isDinosaurPres()) == null){
                        mapPanel.add(new JLabel(new ImageIcon("ground.gif")));
                    }
                }
            }
        }
        mapFrame.pack();
        mapFrame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        mapFrame.setVisible(true);
    }

    public String generateString(String string){
        string = string.replace("@mappaGenerale,{40,40},", "");
        string = string.replace("[a]", "a");
        string = string.replace("[t]", "t");
        string = string.replace("[v]", "v");
        string = string.replace("[b]", "b");
        string = string.replace(",", "");
        string = string.replace(";", "");
        return string;
    }

    public void initComponentsString(String string){
        string = generateString(string);
        mapPanel.setLayout(new GridLayout(40, 40));
        mapFrame.add(mapPanel);
        for (int i = 39; i >= 0; i--){
            for (int j = 0; j < 40; j++){
                if (!(string.charAt(i*40 + j) == 'b')){
                    if (string.charAt(i*40 + j) == 'v'){
                        mapPanel.add(new JLabel(new ImageIcon("vegetation.gif")));
                    }
                    if (string.charAt(i*40 + j) == 't'){
                        mapPanel.add(new JLabel(new ImageIcon("ground.gif")));
                    }
                    if (string.charAt(i*40 + j) == 'a'){
                        mapPanel.add(new JLabel(new ImageIcon("water.gif")));
                    }

                } else {
                    mapPanel.add(new JLabel(new ImageIcon("notVisible.gif")));
                }
            }
        }
        mapFrame.pack();
        mapFrame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        mapFrame.setVisible(true);
    }

    public void setVisibility(Type type){
        mapPanel.removeAll();
        for (int i = 39; i >= 0; i--){
            for (int j = 0; j < 40; j++){
                if (type != null && type.isInFullVisual(map.ReturnBox(i, j))){
                    if (map.ReturnBox(i, j) instanceof WaterBox){
                        mapPanel.add(new JLabel(new ImageIcon("water.gif")));
                    }
                    if (map.ReturnBox(i, j) instanceof GroundBox){
                        Dinosaur dino = (((GroundBox) map.ReturnBox(i, j)).isDinosaurPres());
                        if ((((GroundBox) map.ReturnBox(i, j)).isCarrionPres()) != null && ((((GroundBox) map.ReturnBox(i, j)).isDinosaurPres()) == null)){
                            mapPanel.add(new JLabel(new ImageIcon("carrion.gif")));
                        }
                        if ((((GroundBox) map.ReturnBox(i, j)).isVegetationPres()) != null && ((((GroundBox) map.ReturnBox(i, j)).isDinosaurPres()) == null)){
                            mapPanel.add(new JLabel(new ImageIcon("vegetation.gif")));
                        }
                        if (dino != null){
                            mapPanel.add(new JLabel(new ImageIcon(dino.returnPlayer().returnGif())));
                        }
                        if (((((GroundBox) map.ReturnBox(i, j)).isCarrionPres()) == null) && ((((GroundBox) map.ReturnBox(i, j)).isVegetationPres()) == null) && (((GroundBox) map.ReturnBox(i, j)).isDinosaurPres()) == null){
                            mapPanel.add(new JLabel(new ImageIcon("ground.gif")));
                        }
                    }
                } else {
                    mapPanel.add(new JLabel(new ImageIcon("notVisible.gif")));
                }
            }
        }
        mapPanel.invalidate();
        mapPanel.validate();
        mapPanel.repaint();
    }

    public void rePaint(){
        mapPanel.removeAll();
        for (int i = 39; i >= 0; i--){
            for (int j = 0; j < 40; j++){
                if (map.ReturnBox(i, j) instanceof WaterBox){
                    mapPanel.add(new JLabel(new ImageIcon("water.gif")));
                }
                if (map.ReturnBox(i, j) instanceof GroundBox){
                    if ((((GroundBox) map.ReturnBox(i, j)).isCarrionPres()) != null && ((((GroundBox) map.ReturnBox(i, j)).isDinosaurPres()) == null)){
                        mapPanel.add(new JLabel(new ImageIcon("carrion.gif")));
                    }
                    if ((((GroundBox) map.ReturnBox(i, j)).isVegetationPres()) != null && ((((GroundBox) map.ReturnBox(i, j)).isDinosaurPres()) == null)){
                        mapPanel.add(new JLabel(new ImageIcon("vegetation.gif")));
                    }
                    if ((((GroundBox) map.ReturnBox(i, j)).isDinosaurPres()) != null){
                        mapPanel.add(new JLabel(new ImageIcon("dinosaur.gif")));
                    }
                    if (((((GroundBox) map.ReturnBox(i, j)).isCarrionPres()) == null) && ((((GroundBox) map.ReturnBox(i, j)).isVegetationPres()) == null) && (((GroundBox) map.ReturnBox(i, j)).isDinosaurPres()) == null){
                        mapPanel.add(new JLabel(new ImageIcon("ground.gif")));
                    }
                }
            }
        }
        mapPanel.invalidate();
        mapPanel.validate();
        mapPanel.repaint();
    }
}