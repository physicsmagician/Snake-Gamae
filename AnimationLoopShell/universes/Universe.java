import java.awt.Rectangle;
import java.util.ArrayList;

public abstract class Universe {

	//TODO: these should be private; accessors can implement validation
	protected boolean complete = false;

	protected ActiveSprite player1 = null;
	
	protected double accelarationX = 0; //per second per second
	protected double accelarationY = 600; //per second per second

	protected Background background = null;    
	
	protected ArrayList<ActiveSprite> activeSprites = new ArrayList<ActiveSprite>();
	protected ArrayList<StaticSprite> staticSprites = new ArrayList<StaticSprite>();
	protected static ArrayList<StaticSprite> tailSprites = new ArrayList<StaticSprite>();

	//require a separate list for sprites to be removed to avoid a concurence exception
	private ArrayList<ActiveSprite> disposalList = new ArrayList<ActiveSprite>();
	private ArrayList<StaticSprite> disposalListStatic = new ArrayList<StaticSprite>();
	private ArrayList<StaticSprite> disposalListTails = new ArrayList<StaticSprite>();

	public Universe() {
		activeSprites = new ArrayList<ActiveSprite>();
		staticSprites = new ArrayList<StaticSprite>();
		tailSprites = new ArrayList<StaticSprite>();
	}
	
	
	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public Background getBackground() {
		return background;
	}

	public double getAccelarationX() {
		return accelarationX;
	}

	public double getAccelarationY() {
		return accelarationY;
	}

	protected ActiveSprite getPlayer1() {
		return player1;
	}

	public ArrayList<ActiveSprite> getActiveSprites() {
		return activeSprites;
	}

	public ArrayList<StaticSprite> getStaticSprites() {
		return staticSprites;
	}
	
	public static ArrayList<StaticSprite> getTailSprites() {
		return tailSprites;
	}

	public void setStaticSprites(ArrayList<StaticSprite> staticSprites) {
		this.staticSprites = staticSprites;
	}

	public abstract boolean centerOnPlayer();

	public abstract void update(KeyboardInput keyboard, long actual_delta_time);
	
    protected void updateSprites(KeyboardInput keyboard, long actual_delta_time) {
		for (int i = 0; i < activeSprites.size(); i++) {
			ActiveSprite sprite = activeSprites.get(i);
    		sprite.update(this, keyboard, actual_delta_time);
    	}    	
    }
    
    protected void disposeSprites() {
    
    	//for activeSprites
		for (int i = 0; i < activeSprites.size(); i++) {
			ActiveSprite sprite = activeSprites.get(i);
    		if (sprite.getDispose()) {
    			disposalList.add(sprite);
    		}
    	}
		for (int i = 0; i < disposalList.size(); i++) {
			ActiveSprite sprite = disposalList.get(i);
			activeSprites.remove(sprite);
    	}
		//for staticSprites
		for (int i = 0; i < staticSprites.size(); i++) {
			StaticSprite sprite = staticSprites.get(i);
    		if (sprite.getDispose()) {
    			disposalListStatic.add(sprite);
    		}
    	}
		for (int i = 0; i < disposalListStatic.size(); i++) {
			StaticSprite sprite = disposalListStatic.get(i);
			staticSprites.remove(sprite);
    	}
		
    	//for tailSprites
		for (int i = 0; i < tailSprites.size(); i++) {
			StaticSprite sprite = tailSprites.get(i);
    		if (sprite.getDispose()) {
    			//System.out.println(String.format("%f %f", sprite.minX, sprite.minY));
    			disposalListTails.add(sprite);
    		}
    	}
		for (int i = 0; i < disposalListTails.size(); i++) {
			StaticSprite sprite = disposalListTails.get(i);
			tailSprites.remove(sprite);
    	}
		
		
    	if (disposalList.size() > 0) {
    		disposalList.clear();
    	}
    	if (disposalListStatic.size() > 0 && SnakeSprite.eatenApple) {
    		disposalListStatic.clear();
    	}
    	if (disposalListStatic.size() > 0) {
    		disposalListTails.clear();
    	}
    	
    }	
}
