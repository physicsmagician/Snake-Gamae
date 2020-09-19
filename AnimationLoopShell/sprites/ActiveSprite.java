import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public abstract class ActiveSprite {

	protected static double minX = 0;
	protected static double minY = 0;
	protected static int width = 20;
	protected int height = 20;
	private boolean dispose = false;

	public ActiveSprite() {
	}

	public final void setMinX(double currentX) {
		this.minX = currentX;
	};
	public final void setMinY(double currentY) {
		this.minY = currentY;
	}
	public final double getMinX() {
		return minX;
	}

	public final double getMaxX() {
		return minX + width;
	}

	public final double getMinY() {
		return minY;
	}

	public final double getMaxY() {
		return minY + height;
	}

	public final long getHeight() {
		// TODO Auto-generated method stub
		return height;
	}

	public final long getWidth() {
		// TODO Auto-generated method stub
		return width;
	}

	public double getCenterX() {
		return minX + (width / 2);
	};
	
	public double getCenterY() {
		return minY + (height / 2);
	};
				
	public boolean getDispose() {
		return dispose;
	}

	public void setDispose() {
		this.dispose = true;
	}

	public abstract void update(Universe level, KeyboardInput keyboard, long actual_delta_time);
	public abstract Image getImage();
	
}
