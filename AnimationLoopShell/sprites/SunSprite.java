
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class SunSprite extends StaticSprite{
	
	private static Image image = null;

	public SunSprite(double minX, double minY, double maxX, double maxY, boolean showImage) {
		
		if (image == null) {
			try {
				image = ImageIO.read(new File("res/sun.png"));
				
			}
			catch (IOException e) {
				System.out.println(e.toString());
			}		
		}
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
		
	}
	
	public Image getImage() {
		return image;
	}
	

}