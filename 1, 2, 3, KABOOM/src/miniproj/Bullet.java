package miniproj;

import javafx.scene.image.Image;

public class Bullet extends Sprite {
	public final int BULLET_SPEED = 10; //
	public final static Image BULLET_IMAGE = new Image("images/laser.png",Bullet.BULLET_WIDTH,Bullet.BULLET_HEIGHT,false,false);
	public final static int BULLET_WIDTH = 30;
	public final static int BULLET_HEIGHT = 15;

	public Bullet(int x, int y){
		super(x,y,Bullet.BULLET_IMAGE);
	}

	//method that will move/change the x position of the bullet
	void move(){
		this.x += this.BULLET_SPEED;
		if(this.x >= GameStage.WINDOW_WIDTH){
			this.setVisible(false);
		}
	}
}