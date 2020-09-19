import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class LevelFiveUniverse extends Universe {
	
	Random random = new Random();
	double appleMinX = 25;
	double appleMinY = 75;
	double appleMaxX = 75;
	double appleMaxY = 125;
	
	AppleSprite apple = new AppleSprite(appleMinX, appleMinY, appleMaxX, appleMaxY, true);
	SnakeSprite snake = new SnakeSprite(350, 350);
	
	public LevelFiveUniverse () {
		super();
		
		background = new PurpleBackground();
		activeSprites.add(snake);
		staticSprites.add(apple);
		
		//gap is 150px wide
		staticSprites.add(new BarrierSprite(0,0, 16, 300, true));	//top left corner (verticle)
		staticSprites.add(new BarrierSprite(0, 0, 300, 16, true)); //top left corner (horizontal)
		
		staticSprites.add(new BarrierSprite(684,0, 700, 300, true)); //top right corner (verticle)
		staticSprites.add(new BarrierSprite(450,0, 700, 16, true)); //top right corner (horizontal)
		
		staticSprites.add(new BarrierSprite(0,450, 16, 700, true)); //bottom left corner (verticle)
		staticSprites.add(new BarrierSprite(0, 684, 300, 700, true)); //bottom left corner (horizontal)
		
		staticSprites.add(new BarrierSprite(684, 450, 700, 700, true)); //bottom right corner (verticle)
		staticSprites.add(new BarrierSprite(450, 684, 700, 700, true)); //bottom right corner (horizontal)
		
		//box
		staticSprites.add(new BarrierSprite(250, 200, 450, 216, true)); //top
		//staticSprites.add(new BarrierSprite(250, 200, 266, 500, true)); //left
		//staticSprites.add(new BarrierSprite(450, 200, 466, 516, true)); //right
		staticSprites.add(new BarrierSprite(250, 500, 450, 516, true)); //bottom
		
	}
	
	public boolean centerOnPlayer() {
		return false;
	}		
	
	public void update(KeyboardInput keyboard, long actual_delta_time) {
		
		if (keyboard.keyDownOnce(27)) {
			complete = true;
		}
		
		if (Universe.getTailSprites().size() > 150) {
			Universe.getTailSprites().get(0).setDispose();
		}
		
		eatenApple();
			
		updateSprites(keyboard, actual_delta_time);
		disposeSprites();
		
		if (SnakeSprite.getPoints() == 5) {
			UniverseFactory.getNextUniverse();
		}
		
	}
	public void eatenApple() {
		if (SnakeSprite.eatenApple) {
			int randomFactor = random.nextInt(500) + 11; 
			AppleSprite apple = new AppleSprite(appleMinX + randomFactor, appleMinY + randomFactor, 
					appleMaxX + randomFactor, appleMaxY + randomFactor, true);
		
			this.apple.setDispose();
			staticSprites.add(apple);
			this.apple = apple;
			}
	}	
}
