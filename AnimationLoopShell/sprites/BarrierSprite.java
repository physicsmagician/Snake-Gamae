import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BarrierSprite extends StaticSprite {

	private Image image = null;
	
	public BarrierSprite(double minX, double minY, double maxX, double maxY, boolean showImage) {
		
		if (image == null && showImage) {
			try {
				image = ImageIO.read(new File("res/barrier.png"));
				
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
