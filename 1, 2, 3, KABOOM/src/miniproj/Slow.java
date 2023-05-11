package miniproj;

import javafx.scene.image.Image;

public class Slow extends Obstacles{
	public final static Image SLOW_IMAGE = new Image("images/water.png",Obstacles.OBSTACLE_WIDTH,Obstacles.OBSTACLE_HEIGHT,false,false);
	public final static int SLOW_DURATION = 3;

	public Slow(int x, int y) {
		super(x, y, Slow.SLOW_IMAGE);
	}

	void checkCollision(XWing xwing) {
		if(this.collidesWith(xwing)) {
			this.despawn();
			xwing.decreaseSpeed(Slow.SLOW_DURATION);
		}
	}
}
