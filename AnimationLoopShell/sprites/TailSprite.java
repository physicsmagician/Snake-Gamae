
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class TailSprite extends StaticSprite {

	private final double VELOCITY = 200;
	
	private static Image image = null;
	
	double velocityX = 0;
	double velocityY = 0; 
	
	public TailSprite(double minX, double minY, double maxX, double maxY) {	
		
		if (image == null) {
			try {
				image = ImageIO.read(new File("res/snake.png"));
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

	@Override
	public Image getImage() {
		return image;
	}

}

