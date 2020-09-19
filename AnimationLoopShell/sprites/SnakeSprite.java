
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class SnakeSprite extends ActiveSprite{

	private double VELOCITY = 200;
	private CollisionDetection collisionDetection;
	
	public static Image image;	
	public static boolean eatenApple = false;
	public static boolean eatenSun = false;
	public static int points = 0;
	public static boolean gameOver = false;
	
	private static double velocityX = 0;
	private static double velocityY = 0; 

	public SnakeSprite(double currentX, double currentY) {
		super();
		
		collisionDetection = new CollisionDetection();

		this.minX = currentX;
		this.minY = currentY;
		
		if (image == null) {
			try {
				image = ImageIO.read(new File("res/snake.png"));
			}
			catch (IOException e) {
				System.out.println(e.toString());
			}		
		}	
		
		if (UniverseFactory.getLevel() > 1) {
			VELOCITY += Math.pow(UniverseFactory.getLevel(), 3);

		}
	}
	
	public Image getImage() {
		return image;
	}

	@Override
	public void update(Universe level, KeyboardInput keyboard, long actual_delta_time) {
		double newX = minX;
		double newY = minY;
		//change the direction of the snake
		//LEFT	
		if (keyboard.keyDownOnce(37)) {

			if (velocityX == VELOCITY) {
				//do nothing
			} 
			else {
				velocityX = -VELOCITY;
				velocityY = 0;
			}
		}
		// RIGHT
		if (keyboard.keyDownOnce(39)) {
			if (velocityX == -VELOCITY) {
				//do nothing
			} 
			else {
				velocityX = VELOCITY;
				velocityY = 0;
			}
		}
		//UP
		if (keyboard.keyDownOnce(38)) {
			if (velocityY == VELOCITY) {
				//do nothing
			} 
			else {
				velocityY = -VELOCITY;	
				velocityX = 0;
			}
		}
		
		// DOWN
		if (keyboard.keyDownOnce(40)) {
			if (velocityY == -VELOCITY) {
				//do nothing
			}
			else {
				velocityY = VELOCITY;
				velocityX = 0;
			}
		}

		newX += actual_delta_time * 0.001 * velocityX;
		newY +=  actual_delta_time * 0.001 * velocityY;
		
		//if snake goes off the screen
		if (newX > 700) {
			newX = 0;
		} else if (newX < 0) {
			newX = 700;
		}
		if (newY > 700) {
			newY = 0;
		} else if (newY < 0) {
			newY = 700;
		}
		
		if (checkCollisionWithBarrier(level, newX, newY) == false) {
			TailSprite segment = new TailSprite(this.minX , this.minY , this.minX + 20, this.minY + 20);
			level.tailSprites.add(segment);
			this.minX = newX;
			this.minY = newY;
		} else {
			gameOver = true;
		}
		
		if (checkCollisionWithApple(level, newX, newY)) {
			TailSprite segment = new TailSprite(this.minX, this.minY, this.minX + 20, this.minY + 20);
			level.tailSprites.add(segment);
		}
		if (checkCollisionWithSun(level, newX, newY)) {
			TailSprite segment = new TailSprite(this.minX, this.minY, this.minX + 20, this.minY + 20);
			level.tailSprites.add(segment);
		}
	 
	}

	private boolean checkCollisionWithBarrier(Universe universe, double x, double y) {

		boolean colliding = false;

		for (StaticSprite staticSprite : universe.getStaticSprites()) {
			if (staticSprite instanceof BarrierSprite ) {
				boolean toLeft = (x + this.width) < staticSprite.getMinX();
				boolean toRight = x > staticSprite.getMaxX();
				boolean collidingX = !( toLeft || toRight);
				boolean above = (y + this.height) < staticSprite.getMinY();
				boolean below = y > staticSprite.getMaxY();
				boolean collidingY = !( above || below);
				if (collidingX && collidingY) {
					colliding = true;
					break;
				}			
			}
		}
		return colliding;	
	}
	
	private boolean checkCollisionWithApple(Universe universe, double x, double y) {
		eatenApple = false;
		for (StaticSprite staticSprite : universe.getStaticSprites()) {
			if (staticSprite instanceof AppleSprite) {
				if (staticSprite.getDispose() == false) {
					boolean toLeft = (x + this.width) < staticSprite.getMinX();
					boolean toRight = x > staticSprite.getMaxX();
					boolean collidingX = !( toLeft || toRight);
					boolean above = (y + this.height) < staticSprite.getMinY();
					boolean below = y > staticSprite.getMaxY();
					boolean collidingY = !( above || below);
					if (collidingX && collidingY) {
						eatenApple = true;
						points += 1;
						break;
					}			
					
				}
			}
		}	
		return eatenApple;
		
	}
	private boolean checkCollisionWithSun(Universe universe, double x, double y) {
		eatenSun = false;
		for (StaticSprite staticSprite : universe.getStaticSprites()) {
			if (staticSprite instanceof SunSprite) {
				if (staticSprite.getDispose() == false) {
					boolean toLeft = (x + this.width) < staticSprite.getMinX();
					boolean toRight = x > staticSprite.getMaxX();
					boolean collidingX = !( toLeft || toRight);
					boolean above = (y + this.height) < staticSprite.getMinY();
					boolean below = y > staticSprite.getMaxY();
					boolean collidingY = !( above || below);
					if (collidingX && collidingY) {
						eatenSun = true;
						points += 1;
						break;
					}			
					
				}
			}
		}	
		return eatenSun;
		
	}
	
	public static double getCentreX() {
		return minX + (width + 2);
	};
	
	public static double getCurrentX() {
		return minX;
	}
	
	public static double getCurrentY() {
		return minY;
	}
	
	public static boolean getGameOver() {
		return gameOver;
	}
	
	public static double getVelocityX() {
		return velocityX;
	}
	
	public static double getVelocityY() {
		return velocityY;
	}
	
	public static int getPoints() {
		return points;
	}
}