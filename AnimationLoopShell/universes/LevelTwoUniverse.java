import java.util.Random;

public class LevelTwoUniverse extends Universe{

	Random random = new Random();
	double appleMinX = 25;
	double appleMinY = 75;
	double appleMaxX = 75;
	double appleMaxY = 125;
	
	AppleSprite apple = new AppleSprite(appleMinX, appleMinY, appleMaxX, appleMaxY, true);
	SnakeSprite snake = new SnakeSprite(350, 350);
	
	public LevelTwoUniverse() {
		super();
		
		background = new WhiteSandBackground();

		activeSprites.add(snake);
		staticSprites.add(apple);
		staticSprites.add(new BarrierSprite(0,0, 700, 16, true));	//top
		staticSprites.add(new BarrierSprite(0,684, 700, 700, true)); //bottom
		staticSprites.add(new BarrierSprite(200, 225, 225, 500, true)); //left
		staticSprites.add(new BarrierSprite(500,225, 525, 500, true)); //right
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
		
		if (Universe.getTailSprites().size() > 15) {
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
