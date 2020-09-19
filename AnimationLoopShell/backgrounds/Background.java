import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Background {

	public abstract Tile getTile(int col, int row);
	
	public abstract int getCol(int x);
	
	public abstract int getRow(int y);
	
}
