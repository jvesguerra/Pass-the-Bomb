package miniproj;

import java.util.ArrayList;
import java.util.Random;
import javafx.scene.image.Image;

public class XWing extends Sprite{
	private String name;
	private int strength;
	private boolean alive;
	private boolean invincibility;
	private int invincibilityElapsed;
	private int invincibilityDuration;
	private boolean speedup;
	private int speedupElapsed;
	private int speedupDuration;
	private int type = 0; // 0 has a bomb; 1 no bomb
	private int score = 0;
	private boolean moveRight;
	private int speed;

	private ArrayList<Bullet> bullets;

	private final static Image PLAYER_RIGHT = new Image("images/move_right.png",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);
	public final static Image PLAYER_LEFT = new Image("images/move_left.png",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);
	public final static Image PLAYER_DOWN = new Image("images/move_down.png",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);
	public final static Image PLAYER_UP = new Image("images/move_up.png",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);
	public final static Image HAS_BOMB = new Image("images/karina_2.jpeg",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);
	private final static Image XWING_INVINCIBLE_IMAGE = new Image("images/x_wing_invincible.png",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);

	public static final int XWING_X_POS = 100;
	public static final int XWING_Y_POS = 250;
	public static final int XWING_SPEED = 3;
	public final static int XWING_SIZE = 50;

	public XWing(String name, int x, int y){
		super(x,y,XWing.PLAYER_LEFT);
		this.name = name;
		Random r = new Random();
		this.strength = r.nextInt(51)+100; //strength starts from 100-150
		this.alive = true;
		this.invincibility = false;
		this.speedup = false;
		this.moveRight = false;

		this.speed = XWING_SPEED;
		this.bullets = new ArrayList<Bullet>();
	}

	public boolean isInvincible() {
		return this.invincibility;
	}

	public boolean isSpeed() {
		return this.speedup;
	}

	public boolean isAlive(){
		if(this.alive) return true;
		return false;
	}

	public String getName(){
		return this.name;
	}

	public int getScore() {
		return this.score;
	}

	public int getType() {
		return this.type;
	}

	public int getStrength() {
		return this.strength;
	}

	void setScore() {
		this.score+=1;
	}

	void setType(int num) {
		this.type = num;
		if(this.type == 1){
			this.setImage(HAS_BOMB);
		}else{
			this.setImage(PLAYER_LEFT);
		}
	}

	void die(){
    	this.alive = false;
    }

	void faceRight() {
		if(this.type == 0){
			this.setImage(PLAYER_RIGHT);
		}
	}

	void faceLeft() {
		if(this.type == 0){
			this.setImage(PLAYER_LEFT);
		}
	}

	void faceDown() {
		if(this.type == 0){
			this.setImage(PLAYER_DOWN);
		}
	}

	void faceUp() {
		if(this.type == 0){
			this.setImage(PLAYER_UP);
		}
	}

	void makeInvincible(int invincibilityDuration) {
		this.invincibility = true;
		this.invincibilityElapsed = 0;
		this.setImage(XWING_INVINCIBLE_IMAGE);
		this.invincibilityDuration = invincibilityDuration;
	}

	void setInvincibilityElapsed() {
		this.invincibilityElapsed += 1;
		if(this.invincibilityElapsed == this.invincibilityDuration) {
			this.invincibility = false;
			this.setImage(PLAYER_RIGHT);
		}
	}

	void addSpeed(int speedupDuration) {
		this.speedup = true;
		this.speed += 20;
		System.out.println(this.speed);
		this.speedupElapsed = 0;
		this.setImage(XWING_INVINCIBLE_IMAGE);
		this.speedupDuration = speedupDuration;
	}

	void setSpeedElapsed() {
		this.speedupElapsed += 1;
		if(this.speedupElapsed == this.speedupDuration) {
			this.speedup = false;
			this.setImage(PLAYER_RIGHT);
		}
	}


	//method called if up/down/left/right arrow key is pressed.
	public void move() {
		if((this.x+this.dx >= 0 && this.y+this.dy >= 0) && (this.x+this.dx <= GameStage.WINDOW_WIDTH-this.width && this.y+this.dy <= GameStage.WINDOW_HEIGHT-this.height)) {
			this.x += this.dx;
			this.y += this.dy;
		}
	}

	public boolean isAvailable() {
		return this.getVisible();
	}

	void despawn() {
		this.setVisible(false);
	}

	void checkCollision(XWing xwing, XWing dest) {
		if(this.collidesWith(xwing)) {
			xwing.makeInvincible(5);
			dest.makeInvincible(5);

			if(xwing.getType() == 0){
				xwing.setType(1);
				dest.setType(0);
			}else{
				xwing.setType(0);
				dest.setType(1);
			}


		}
	}

	//method that makes ai move
	void moveAi(){
		if(this.moveRight == true && this.x <= GameStage.WINDOW_WIDTH-this.width) { //continue moving to the right
			this.x += this.speed;
		} else if(this.moveRight == true && this.x >= GameStage.WINDOW_WIDTH-this.width) { //will move to the left (since it moves to right and reached the window width)
			this.moveRight = !this.moveRight;
			if(this.type==0) this.setImage(PLAYER_LEFT);
		} else if(this.moveRight == false && this.x >= 0){ //continue moving to the left
			this.x -= this.speed;
		} else {
			this.moveRight = !this.moveRight; //will move to the right (since it moves to left and reached the window width)
			if(this.type==0) this.setImage(PLAYER_RIGHT);
		}
	}

	//method that will get the bullets 'shot' by the XWing
	public ArrayList<Bullet> getBullets(){
		return this.bullets;
	}

	//method called if spacebar is pressed
	public void shoot(){
		//compute for the x and y initial position of the bullet
		int x = (int) (this.x + this.width+20);
		int y = (int) (this.y + this.height/2);
		this.bullets.add(new Bullet(x, y));
    }

	void decreaseStrength(int type) { //hit
		if(type == 0) { //normal enemy hit the XWing
			this.strength-=30;
		} else { //boss enemy hit the XWing
			this.strength-=50;
			this.makeInvincible(1);
		}

		//strength printer
		if(this.getStrength() <= 0) {
			System.out.println("Strength: 0");
			this.die();
		} else {
			System.out.println("Strength: " + this.strength);
		}

	}

	void increaseStrength() { //collected an orb
		this.strength+=50;
		System.out.println("Strength: " + this.strength);
	}
}