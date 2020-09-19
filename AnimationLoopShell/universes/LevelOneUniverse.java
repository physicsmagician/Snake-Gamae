import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class LevelOneUniverse extends Universe {
	
	Random random = new Random();
	
	double appleMinX = 25;
	double appleMinY = 75;
	double appleMaxX = 75;
	double appleMaxY = 125;
	
	AppleSprite apple = new AppleSprite(appleMinX, appleMinY, appleMaxX, appleMaxY, true);
	SnakeSprite snake = new SnakeSprite(350, 350);
	
	public LevelOneUniverse () {
		super();
		
		activeSprites.add(snake);
		staticSprites.add(apple);
	}
	
	public boolean centerOnPlayer() {
		return false;
	}		
	
	public void update(KeyboardInput keyboard, long actual_delta_time) {
		
		if (keyboard.keyDownOnce(27)) {
			complete = true;
		}
		
		if (Universe.getTailSprites().size() > 6) {
			Universe.getTailSprites().get(0).setDispose();
		}
		eatenApple();
		updateSprites(keyboard, actual_delta_time);
		disposeSprites();
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
