package miniproj;

import javafx.scene.image.Image;

public class RebelAlliance extends PowerUps{
	public final static Image REBELALLIANCE_IMAGE = new Image("images/rebel_alliance.png",PowerUps.POWERUP_WIDTH,PowerUps.POWERUP_HEIGHT,false,false);
	public final static int POWERUP_INVINCIBILTY_DURATION = 3;
	
	public RebelAlliance(int x, int y) {
		super(x, y, RebelAlliance.REBELALLIANCE_IMAGE);
	}

	void checkCollision(XWing xwing) {
		if(this.collidesWith(xwing)) {
			this.despawn();
			xwing.makeInvincible(RebelAlliance.POWERUP_INVINCIBILTY_DURATION);
		}
	}
}
