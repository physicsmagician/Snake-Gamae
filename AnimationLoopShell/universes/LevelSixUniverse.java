import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

/** EXPLANATION OF LEVEL ///
 * Apep is the ancient egyptian God who represents chaos.
 * He is the enemy of Ra, the sun God, so in this level he is eating the Sun.
 * This is a bonus level.
 */
public class LevelSixUniverse extends Universe {
	
	Random random = new Random();
	double sunMinX = 75;
	double sunMinY = 75;
	double sunMaxX = 125;
	double sunMaxY = 125;	
	SunSprite sun = new SunSprite(sunMinX, sunMinY, sunMaxX, sunMaxY, true);
	SnakeSprite snake = new SnakeSprite(350, 350);
	
	public LevelSixUniverse () {
		super();
		
		background = new HieroglyphsBackground();
		
		activeSprites.add(snake);
		staticSprites.add(sun);
		
		staticSprites.add(new BarrierSprite(0,0, 700, 40, true));	//top
		staticSprites.add(new BarrierSprite(0,660, 700, 700, true)); //bottom
		staticSprites.add(new BarrierSprite(0,0, 40, 700, true)); //left
		staticSprites.add(new BarrierSprite(660,0, 700, 700, true)); //right
		
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
		
		int time = Math.round(AnimationFrame.getTime() / 1000);
		int randomFactor = random.nextInt(401) + 51; //max = 150 & min = 100
				
		SunSprite sun = new SunSprite(sunMinX + randomFactor, sunMinY + randomFactor, sunMaxX + randomFactor, sunMaxY + randomFactor, true);

		if (SnakeSprite.eatenSun) {
			this.sun.setDispose();
			staticSprites.add(sun);
			this.sun = sun;
		} else {
			if ((time % 7) == 0 && time != 0) {
				sun.setDispose();
			}
		}
		
		updateSprites(keyboard, actual_delta_time);
		disposeSprites();
		
	}
	
}
