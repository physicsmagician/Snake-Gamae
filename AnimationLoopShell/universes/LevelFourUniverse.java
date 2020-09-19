import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class LevelFourUniverse extends Universe {
	
	Random random = new Random();
	double appleMinX = 25;
	double appleMinY = 75;
	double appleMaxX = 75;
	double appleMaxY = 125;
	
	AppleSprite apple = new AppleSprite(appleMinX, appleMinY, appleMaxX, appleMaxY, true);
	SnakeSprite snake = new SnakeSprite(350, 350);
	
	public LevelFourUniverse () {
		super();
		background = new BlackBackground();
		activeSprites.add(snake);
		staticSprites.add(apple);
		
		staticSprites.add(new BarrierSprite(0,0, 700, 16, true));	//top
		staticSprites.add(new BarrierSprite(0,684, 700, 700, true)); //bottom
		staticSprites.add(new BarrierSprite(0,0, 16, 700, true)); //left
		staticSprites.add(new BarrierSprite(684,0, 700, 700, true)); //right
		
	}
	
	public boolean centerOnPlayer() {
		return false;
	}		
	
	public void update(KeyboardInput keyboard, long actual_delta_time) {
		
		if (keyboard.keyDownOnce(27)) {
			complete = true;
		}
		
		if (Universe.getTailSprites().size() > 100) {
			Universe.getTailSprites().get(0).setDispose();
		}
		
		int time = Math.round(AnimationFrame.getTime() / 1000);
		int randomFactor = random.nextInt(401) + 51; //max = 150 & min = 100
				
		AppleSprite apple = new AppleSprite(appleMinX + randomFactor, appleMinY + randomFactor, appleMaxX + randomFactor, appleMaxY + randomFactor, true);

		if (SnakeSprite.eatenApple) {
			this.apple.setDispose();
			staticSprites.add(apple);
			this.apple = apple;
		} else {
			if ((time % 7) == 0 && time != 0) {
				apple.setDispose();
			}
		}
		
		updateSprites(keyboard, actual_delta_time);
		disposeSprites();
		
	}
	
}
