package miniproj;

import java.util.Random;
import javafx.scene.image.Image;

public class Enemy extends Sprite {
	public static final int MAX_ENEMY_SPEED = 5;

	//normal enemy
	public final static Image TIE_IMAGE_LEFT = new Image("images/tie_2.png",Enemy.ENEMY_SIZE,Enemy.ENEMY_SIZE,false,false);
	public final static Image TIE_IMAGE_RIGHT = new Image("images/tie.png",Enemy.ENEMY_SIZE,Enemy.ENEMY_SIZE,false,false);
	public final static int ENEMY_SIZE = 60;

	//boss enemy
	public final static Image DEATHSTAR_IMAGE = new Image("images/deathstar.png",Enemy.DEATHSTAR_SIZE,Enemy.DEATHSTAR_SIZE,false,false);
	public final static int DEATHSTAR_LIFE = 3000;
	public final static int DEATHSTAR_SIZE = 150;

	private boolean alive;

	//attribute that will determine if an enemy will initially move to the right
	private boolean moveRight;
	private int speed;
	private int type;
	private int life;


	public Enemy(int x, int y, int type){
		super(x,y, type==0?Enemy.TIE_IMAGE_LEFT:Enemy.DEATHSTAR_IMAGE); //GameTimer will determine the type
		this.alive = true;
		Random r = new Random();
		this.speed = r.nextInt(Enemy.MAX_ENEMY_SPEED)+1; //speed is from 1-5
		this.moveRight = false; //initially moving to left
		this.type = type;
		if(type == 1) {
			this.life = Enemy.DEATHSTAR_LIFE;
		}
	}

	//getter
	public boolean isAlive() {
		return this.alive;
	}

	public int enemyType() {
		return this.type;
	}

	public int getBossLife() {
		return this.life;
	}

	//method that changes the x position of the enemy
	void move(){
		if(this.moveRight == true && this.x <= GameStage.WINDOW_WIDTH-this.width) { //continue moving to the right
			this.x += this.speed;
		} else if(this.moveRight == true && this.x >= GameStage.WINDOW_WIDTH-this.width) { //will move to the left (since it moves to right and reached the window width)
			this.moveRight = !this.moveRight;
			if(this.type==0) this.setImage(TIE_IMAGE_LEFT);
		} else if(this.moveRight == false && this.x >= 0){ //continue moving to the left
			this.x -= this.speed;
		} else {
			this.moveRight = !this.moveRight; //will move to the right (since it moves to left and reached the window width)
			if(this.type==0) this.setImage(TIE_IMAGE_RIGHT);
		}
	}

	void die() {
		this.alive = false;
		this.setVisible(false);
	}

	void decreaseBossLife(int damage) {
		this.life-=damage;
	}

	//for collisions
	void checkCollision(XWing xwing, int type) {
		for(int i=0; i<xwing.getBullets().size(); i++) {
			if(this.collidesWith(xwing.getBullets().get(i))) {
				if(type == 0) { //normal
					this.die();
					xwing.setScore(); //will automatically add score
				} else {
					System.out.println("Death Star Life: " + this.getBossLife());
					this.decreaseBossLife(xwing.getStrength());
					if(this.getBossLife() <= 0) {
						this.die();
						xwing.setScore(); //will automatically add score
					}
					System.out.println("Death Star Life: " + this.getBossLife());
				}
				xwing.getBullets().get(i).setVisible(false);
			}
		}
		if(this.collidesWith(xwing) && !xwing.isInvincible()) {
			if(type == 0) { //normal
				this.die();
			}
			xwing.decreaseStrength(type); //will decrease strength depending on the enemy (normal-0 or boss-1)
			xwing.hasBomb();
		}
	}
}
