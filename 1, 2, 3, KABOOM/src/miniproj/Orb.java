package miniproj;

import javafx.scene.image.Image;

public class Orb extends PowerUps{
	public final static Image ORB_IMAGE = new Image("images/orb.png",PowerUps.POWERUP_WIDTH,PowerUps.POWERUP_HEIGHT,false,false);
	public final static int POWERUP_SPEED_DURATION = 3;

	public Orb(int x, int y) {
		super(x, y, Orb.ORB_IMAGE);
	}

	void checkCollision(XWing xwing) {
		if(this.collidesWith(xwing)) {
			this.despawn();
			xwing.addSpeed(Orb.POWERUP_SPEED_DURATION);
		}
	}
}
