
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public abstract class StaticSprite {

	private static Image image;
	protected double minX = 0;
	protected double minY = 0;
	protected double maxX = 0;
	protected double maxY = 0;
	private boolean dispose = false;

	public StaticSprite() {
	}

	public final void setMinX(double minX) {
		this.minX = minX;
	};
	public final void setMinY(double minY) {
		this.minY = minY;
	}
	
	public final double getMinX() {
		return minX;
	}

	public final double getMaxX() {
		return maxX;
	}

	public final double getMinY() {
		return minY;
	}

	public final double getMaxY() {
		return maxY;
	}

	public final double getHeight() {
		// TODO Auto-generated method stub
		return maxY - minY;
	}

	public final double getWidth() {
		// TODO Auto-generated method stub
		return maxX - minX;
	}
	
	public boolean getDispose() {
		return dispose;
	}
	
	public void setDispose() {
		this.dispose = true;
	}

	public abstract Image getImage();
	
}
