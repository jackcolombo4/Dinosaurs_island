package it.polimi.dinosaursisland.mappa;

import java.util.ArrayList;

public class WaterBox extends Box {
	public WaterBox(int x, int y){
		super.setLocation(x, y);
		super.walkable = false;			
	}
	public WaterBox(Box box){
        if(box!=null){
            super.setLocation(box);
        }
	}		
}