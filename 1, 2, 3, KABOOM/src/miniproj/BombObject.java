package miniproj;

import javafx.scene.image.Image;

public class BombObject extends Bomb{
	public final static Image BOMB_IMAGE = new Image("images/bomb_object.png",Bomb.BOMB_WIDTH,Bomb.BOMB_HEIGHT,false,false);

	public BombObject(int x, int y) {
		super(x, y, BombObject.BOMB_IMAGE);
	}

	void checkCollision(XWing xwing) {
		if(this.collidesWith(xwing)) {
			this.despawn();
			xwing.increaseStrength();
		}
	}
}
