package miniproj;

import javafx.scene.image.Image;

public abstract class Obstacles extends Sprite {
	public final static int OBSTACLE_WIDTH = 30;
	public final static int OBSTACLE_HEIGHT = 30;
	public static final int OBSTACLE_DESPAWN_TIME = 5;
	private int availabityTimeElapsed;

	public Obstacles(int x, int y, Image img){
		super(x,y,img);
		this.availabityTimeElapsed = 0;
	}

	public boolean isAvailable() {
		return this.getVisible();
	}

	void setAvailabilityTimeElapsed() {
		this.availabityTimeElapsed += 1;
		if(this.availabityTimeElapsed == OBSTACLE_DESPAWN_TIME) {
			this.despawn();
		}
	}

	void despawn() {
		this.setVisible(false);
	}

	abstract void checkCollision(XWing xwing);
}
