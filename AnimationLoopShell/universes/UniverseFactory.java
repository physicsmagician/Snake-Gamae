
public class UniverseFactory {

	private static int level = 0;
	
	public static int getLevel() {
		return level;
	}

	public static void setLevel(int level) {
		UniverseFactory.level = level;
	}

	public static Universe getNextUniverse() {
		level++;

		if (level == 1) {
			return new LevelOneUniverse();
		}
		else if (level == 2) {
			return new LevelTwoUniverse();
		}
		else if (level == 3) {
			return new LevelThreeUniverse();
		} 
		else if (level == 4) {
			return new LevelFourUniverse();
		} 
		else if (level == 5) {
			return new LevelFiveUniverse();
		} 
		else if (level == 6) {
			return new LevelSixUniverse();
		} 
		else {
			return null;
		}

	}
	
}
