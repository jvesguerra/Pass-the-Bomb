package miniproj;

import javafx.scene.image.Image;

public class Stun extends Obstacles{
	public final static Image STUN_IMAGE = new Image("images/powerup_stun.png",Obstacles.OBSTACLE_WIDTH,Obstacles.OBSTACLE_HEIGHT,false,false);
	public final static int STUN_DURATION = 5;

	public Stun(int x, int y) {
		super(x, y, Stun.STUN_IMAGE);
	}

	void checkCollision(XWing xwing) {
		if(this.collidesWith(xwing)) {
			this.despawn();
			xwing.stun(Stun.STUN_DURATION);
		}
	}
}
