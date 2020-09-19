import java.util.Random;

public class LevelThreeUniverse extends Universe{

	Random random = new Random();
	double appleMinX = 25;
	double appleMinY = 75;
	double appleMaxX = 75;
	double appleMaxY = 125;
	
	AppleSprite apple = new AppleSprite(appleMinX, appleMinY, appleMaxX, appleMaxY, true);
	SnakeSprite snake = new SnakeSprite(350, 350);
	
	public LevelThreeUniverse() {
		super();
		
		background = new RedPolygonBackground();
		
		activeSprites.add(snake);
		staticSprites.add(apple);
		
		staticSprites.add(new BarrierSprite(0,0, 16, 700, true)); //left
		staticSprites.add(new BarrierSprite(684,0, 700, 700, true)); //right

		staticSprites.add(new BarrierSprite(200, 225, 225, 500, true)); //left
		staticSprites.add(new BarrierSprite(500,225, 525, 500, true)); //right
		

		staticSprites.add(new BarrierSprite(350, 0, 375, 100, true)); //left
		staticSprites.add(new BarrierSprite(350,600, 375, 700, true)); //right
	}
	
	@Override
	public boolean centerOnPlayer() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update(KeyboardInput keyboard, long actual_delta_time) {
		if (keyboard.keyDownOnce(27)) {
			complete = true;
		}
		if (Universe.getTailSprites().size() > 50) {
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