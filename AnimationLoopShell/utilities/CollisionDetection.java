import java.util.ArrayList;

public class CollisionDetection {

	private OneDimensionBounce bounce = new OneDimensionBounce();	
	private double bounceFactorX = 1;
	private double bounceFactorY = 1;

	public double getBounceFactorX() {
		return bounceFactorX;
	}

	public void setBounceFactorX(double bounceFactorX) {
		this.bounceFactorX = bounceFactorX;
	}

	public double getBounceFactorY() {
		return bounceFactorY;
	}

	public void setBounceFactorY(double bounceFactorY) {
		this.bounceFactorY = bounceFactorY;
	}

	public static boolean overlaps(double a_left, double a_top, double a_right, double a_bottom, double b_left, double b_top, double b_right, double b_bottom) {
		boolean a_to_left_of_b = (a_right < b_left); //case 1: right edge of A is to the left of left edge of B
		boolean a_to_right_of_b = (a_left > b_right); //case 2: left edge of A is to the right of right edge of B
		boolean horizontal_overlap = !(a_to_left_of_b || a_to_right_of_b);

		boolean a_above_b = (a_bottom > b_top); //case 1: bottom edge of A is above top edge of B
		boolean a_below_b = (a_top  < b_bottom); //case 2: top edge of A is below bottom edge of B
		boolean vertical_overlap = !(a_above_b || a_below_b);

		return (horizontal_overlap && vertical_overlap);
	}

	public static boolean inside(double a_left, double a_top, double a_right, double a_bottom, double b_left, double b_top, double b_right, double b_bottom) {
		boolean x_inside = ((b_left <= a_left) && (a_right <= b_right));
		boolean y_inside = ((b_bottom <= a_bottom) && (b_top >= a_top));
		if (x_inside && y_inside) {
			return true;
		}
		else {
			return false;	    	
		}
	}

	public static boolean covers (double a_left, double a_top, double a_right, double a_bottom, double b_left, double b_top, double b_right, double b_bottom) {
		return inside(b_left, b_top, b_right, b_bottom, a_left, a_top, a_right, a_bottom);
	}

	/**
	 * @param planeBounce: will contain the results of the bounce calculation; cannot be null
	 */
	public void calculatePlaneBounce(TwoDimensionBounce planeBounce, ActiveSprite sprite, ArrayList<StaticSprite> barriers, double velocityX, double velocityY, long actual_delta_time ) {

		if (planeBounce == null) {
			planeBounce = new TwoDimensionBounce();
		}

		//calculate new position assuming there are no changes in direction
		double movementX = (velocityX * actual_delta_time * 0.001);
		double movementY = (velocityY * actual_delta_time * 0.001);

		planeBounce.newVelocityX = velocityX;
		planeBounce.newVelocityY = velocityY;
		planeBounce.newX = sprite.minX + movementX;
		planeBounce.newY = sprite.minY + movementY;
		planeBounce.didBounce = false;

		for (StaticSprite barrier : barriers) {
			//colliding with top edge of barrier?
			//?moving down (can only collide if sprite is moving down)
			if (movementY > 0) {
				//?is the sprite to the left || right of the barrier? (can only collide if this is not the case) 
				if (!( (sprite.getMinX() >= barrier.getMaxX()) || (sprite.getMaxX() <= barrier.getMinX()))) {
					calculateLineBounce(bounce, sprite.getMaxY(), movementY, barrier.getMinY(), bounceFactorY);
					if (bounce.didBounce) {
						planeBounce.newY = bounce.newLocaton - sprite.height;
						//cannot use the returned velocity as it actually (in this case) represents movement for the given delta time
						planeBounce.newVelocityY = (velocityY * bounceFactorY * -1);
						planeBounce.didBounce = true;
					}
				}
			}

			//colliding with bottom edge of barrier?
			//?moving up (can only collide if sprite is moving up)
			if (movementY < 0) {
				//is the sprite to the left || right of the barrier? (can only collide if this is not the case) 
				if (! ((sprite.getMinX() >= barrier.getMaxX()) || (sprite.getMaxX() <= barrier.getMinX()))) {
					calculateLineBounce(bounce, sprite.getMinY(), movementY, barrier.getMaxY(), bounceFactorY);
					if (bounce.didBounce) {
						planeBounce.newY = bounce.newLocaton;
						planeBounce.newVelocityY = (velocityY * bounceFactorY * -1);
						planeBounce.didBounce = true;
					}
				}
			}

			//colliding with left edge of barrier?
			//?moving right (can only collide if sprite is moving to right)
			if (movementX > 0) {
				//?is the sprite above || below the barrier? (can only collide if this is not the case) 
				if (!( (sprite.getMinY() >= barrier.getMaxY()) || (sprite.getMaxY() <= barrier.getMinY()))) {
					calculateLineBounce(bounce, sprite.getMaxX(), movementX, barrier.getMinX(), bounceFactorX);
					if (bounce.didBounce) {
						planeBounce.newX = bounce.newLocaton - sprite.width;
						planeBounce.newVelocityX = (velocityX * bounceFactorX * -1);
						planeBounce.didBounce = true;
					}
				}
			}

			//colliding with right edge of barrier?
			//?moving left (can only collide if sprite is moving to left)
			if (movementX < 0) {
				//?is the sprite above || below the barrier? (can only collide if this is not the case) 
				if (!( (sprite.getMinY() >= barrier.getMaxY()) || (sprite.getMaxY() <= barrier.getMinY()))) {
					calculateLineBounce(bounce, sprite.getMinX(), movementX, barrier.getMaxX(), bounceFactorX);
					if (bounce.didBounce) {
						planeBounce.newX = bounce.newLocaton;
						planeBounce.newVelocityX = (velocityX * bounceFactorX * -1);
						planeBounce.didBounce = true;
					}
				}
			}
		}
	}

	/**
	 * @param lineBounce: will contain the results of the bounce calculation; cannot be null
	 */
	public void calculateLineBounce(OneDimensionBounce lineBounce, double location, double velocity, double boundary, double bounceFactor) {

		double distanceToBoundary = 0;
		if (lineBounce == null) {
			lineBounce = new OneDimensionBounce();
		}

		if ( (velocity > 0) && (location <= boundary) && ((location + velocity) >= boundary)) {
			distanceToBoundary = (boundary - location);
			lineBounce.newLocaton = boundary - ( (velocity - distanceToBoundary) * bounceFactor);
			lineBounce.newVelocity = (velocity * bounceFactor * -1);
			lineBounce.didBounce = true;
		}
		else if (velocity < 0 && (location >= boundary) && ((location + velocity) <= boundary)) {
			distanceToBoundary = (location - boundary);
			lineBounce.newLocaton = boundary - ( (velocity + distanceToBoundary) * bounceFactor);
			lineBounce.newVelocity = (velocity * bounceFactor * -1);
			lineBounce.didBounce = true;
		}
		else {
			lineBounce.newLocaton = location + velocity;
			lineBounce.newVelocity = velocity;
			lineBounce.didBounce = false;
		}

	}	
}
