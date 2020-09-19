import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class HieroglyphsBackground extends Background {

    private Image hieroglyphs;
    private int backgroundWidth = 0;
    private int backgroundHeight = 0;

    public HieroglyphsBackground() {
    	try {
    		this.hieroglyphs = ImageIO.read(new File("res/hieroglyphs.jpg"));
    		backgroundWidth = hieroglyphs.getWidth(null);
    		backgroundHeight = hieroglyphs.getHeight(null);
    		
    	}
    	catch (IOException e) {
    		//System.out.println(e.toString());
    	}		
    }
	
	public Tile getTile(int col, int row) {
		//row is an index of tiles, with 0 being the at the origin
		//col is an index of tiles, with 0 being the at the origin
		int x = (col * backgroundWidth);
		int y = (row * backgroundHeight);
		Tile newTile = null;
		
		newTile = new Tile(hieroglyphs, x, y, backgroundWidth, backgroundHeight, false);

		return newTile;
	}
	
	public int getCol(int x) {
		//which col is x sitting at?
		int col = 0;
		if (backgroundWidth != 0) {
			col = (x / backgroundWidth);
			if (x < 0) {
				return col - 1;
			}
			else {
				return col;
			}
		}
		else {
			return 0;
		}
	}
	
	public int getRow(int y) {
		//which row is y sitting at?
		int row = 0;
		
		if (backgroundHeight != 0) {
			row = (y / backgroundHeight);
			if (y < 0) {
				return row - 1;
			}
			else {
				return row;
			}
		}
		else {
			return 0;
		}
	}
	
}


