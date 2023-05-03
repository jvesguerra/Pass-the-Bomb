package miniproj;

import javafx.scene.image.Image;

public abstract class Bomb extends Sprite {
	public final static int BOMB_WIDTH = 100;
	public final static int BOMB_HEIGHT = 100;
	public static final int POWERUP_DESPAWN_TIME = 5;
	private int availabityTimeElapsed;

	public Bomb(int x, int y, Image img){
		super(x,y,img);
		this.availabityTimeElapsed = 0;
	}

	public boolean isAvailable() {
		return this.getVisible();
	}

	void setAvailabilityTimeElapsed() {
		this.availabityTimeElapsed += 1;
		if(this.availabityTimeElapsed == POWERUP_DESPAWN_TIME) {
			this.despawn();
		}
	}

	void despawn() {
		this.setVisible(false);
	}

	abstract void checkCollision(XWing xwing);
}
