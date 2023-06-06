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
	private int pid;

	private ArrayList<Bullet> bullets;
	private int speedDownElapsed;
	private int speedDownDuration;
	private boolean speedDown;
	private boolean stun;
	private int stunElapsed;
	private int stunDuration;

	private final static Image PLAYER_RIGHT = new Image("images/players/yellow_right.png",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);
	public final static Image PLAYER_LEFT = new Image("images/players/yellow_left.png",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);
	public final static Image PLAYER_DOWN = new Image("images/players/yellow_down.png",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);
	public final static Image PLAYER_UP = new Image("images/players/yellow_up.png",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);
	public final static Image HAS_BOMB = new Image("images/bomb_object.png",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);
	private final static Image XWING_INVINCIBLE_IMAGE = new Image("images/x_wing_invincible.png",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);

	private final static Image PLAYER2_RIGHT = new Image("images/players/red_right.png",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);
	public final static Image PLAYER2_LEFT = new Image("images/players/red_left.png",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);
	public final static Image PLAYER2_DOWN = new Image("images/players/red_down.png",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);
	public final static Image PLAYER2_UP = new Image("images/players/red_up.png",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);

	private final static Image PLAYER3_RIGHT = new Image("images/players/green_right.png",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);
	public final static Image PLAYER3_LEFT = new Image("images/players/green_left.png",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);
	public final static Image PLAYER3_DOWN = new Image("images/players/green_down.png",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);
	public final static Image PLAYER3_UP = new Image("images/players/green_up.png",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);

	private final static Image PLAYER4_RIGHT = new Image("images/players/blue_right.png",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);
	public final static Image PLAYER4_LEFT = new Image("images/players/blue_left.png",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);
	public final static Image PLAYER4_DOWN = new Image("images/players/blue_down.png",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);
	public final static Image PLAYER4_UP = new Image("images/players/blue_up.png",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);

	public static final int XWING_X_POS = 100;
	public static final int XWING_Y_POS = 250;
	public final static double speed = 3;
	public final static int XWING_SIZE = 50;
	public final static double SPEED_INT = 5;

	public static double XWING_SPEED = 3;


	public XWing(String name, int x, int y, int pid){
		super(x,y,XWing.PLAYER_LEFT);
		this.name = name;
		Random r = new Random();
		this.strength = r.nextInt(51)+100; //strength starts from 100-150
		this.alive = true;
		this.invincibility = false;
		this.speedup = false;
		this.speedDown = false;
		this.stun = false;
		this.moveRight = false;
		this.pid = pid;

		this.bullets = new ArrayList<Bullet>();
	}

	public boolean isInvincible() {
		return this.invincibility;
	}

	public boolean isSpeedUp() {
		return this.speedup;
	}

	public boolean isSpeedDown() {
		return this.speedDown;
	}

	public boolean isStun() {
		return this.stun;
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
			if(this.pid == 1){
				this.setImage(PLAYER_LEFT);
			}else if(this.pid == 2){
				this.setImage(PLAYER2_LEFT);
			}else if(this.pid == 3){
				this.setImage(PLAYER3_LEFT);
			}else{
				this.setImage(PLAYER4_LEFT);
			}
			
		}
	}

	
	void die(){
    	this.alive = false;
    }

	void faceRight() {
		if(this.type == 0){
			System.out.println("FACE RIGHT");
			this.setImage(PLAYER_RIGHT);
		}
	}

	void faceLeft() {
		if(this.type == 0){
			System.out.println("FACE LEFT");
			this.setImage(PLAYER_LEFT);
		}
	}

	void faceDown() {
		if(this.type == 0){
			System.out.println("FACE DOWN");
			this.setImage(PLAYER_DOWN);
		}
	}

	void faceUp() {
		if(this.type == 0){
			System.out.println("FACE UP");
			this.setImage(PLAYER_UP);
		}
	}

	//PLAYER 2 RENDERS
		void faceRight2() {
			if(this.type == 0){
				this.setImage(PLAYER2_RIGHT);
			}
		}

		void faceLeft2() {
			if(this.type == 0){
				this.setImage(PLAYER2_LEFT);
			}
		}

		void faceDown2() {
			if(this.type == 0){
				this.setImage(PLAYER2_DOWN);
			}
		}

		void faceUp2() {
			if(this.type == 0){
				this.setImage(PLAYER2_UP);
			}
		}

	//PLAYER 3 RENDERS
		void faceRight3() {
			if(this.type == 0){
				this.setImage(PLAYER3_RIGHT);
			}
		}

		void faceLeft3() {
			if(this.type == 0){
				this.setImage(PLAYER3_LEFT);
			}
		}

		void faceDown3() {
			if(this.type == 0){
				this.setImage(PLAYER3_DOWN);
			}
		}

		void faceUp3() {
			if(this.type == 0){
				this.setImage(PLAYER3_UP);
			}
		}

	//PLAYER 4 RENDERS
		void faceRight4() {
			if(this.type == 0){
				this.setImage(PLAYER4_RIGHT);
			}
		}

		void faceLeft4() {
			if(this.type == 0){
				this.setImage(PLAYER4_LEFT);
			}
		}

		void faceDown4() {
			if(this.type == 0){
				this.setImage(PLAYER4_DOWN);
			}
		}

		void faceUp4() {
			if(this.type == 0){
				this.setImage(PLAYER4_UP);
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

	// SPEED UP
	void addSpeed(int speedupDuration) {
		this.speedup = true;
		XWing.XWING_SPEED += SPEED_INT;
		this.speedupElapsed = 0;
		this.setImage(XWING_INVINCIBLE_IMAGE);
		this.speedupDuration = speedupDuration;
	}

	void setSpeedUpElapsed() {
		this.speedupElapsed += 1;
		if(this.speedupElapsed == this.speedupDuration) {
			this.speedup = false;
			XWing.XWING_SPEED -= SPEED_INT;
			this.setImage(PLAYER_RIGHT);
		}
	}

	// SLOW DOWN
	void decreaseSpeed(int speedDownDuration) {
		this.speedDown = true;
		XWing.XWING_SPEED -= 2;
		this.speedDownElapsed = 0;
		this.setImage(XWING_INVINCIBLE_IMAGE);
		this.speedDownDuration = speedDownDuration;
	}

	void setSpeedDownElapsed() {
		this.speedDownElapsed += 1;
		if(this.speedDownElapsed == this.speedDownDuration) {
			this.speedDown = false;
			XWing.XWING_SPEED += 2;
			this.setImage(PLAYER_RIGHT);
		}
	}

	// STUN
	void stun(int stunDuration) {
		this.stun = true;
		XWing.XWING_SPEED -= 3;
		this.stunElapsed = 0;
		this.setImage(XWING_INVINCIBLE_IMAGE);
		this.stunDuration = stunDuration;
	}

	void setStunElapsed() {
		this.stunElapsed += 1;
		if(this.stunElapsed == this.stunDuration) {
			this.stun = false;
			XWing.XWING_SPEED += 3;
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
		if(xwing.collidesWith(dest)) {
			if(xwing.getType() == 0 && dest.getType() == 1){
				xwing.setType(1);
				dest.setType(0);
			}

			else if(xwing.getType() == 1 && dest.getType() == 0){
				xwing.setType(0);
				dest.setType(1);
			}
		}
	}

	//method that makes ai move
	void moveAi(){
		if(this.moveRight == true && this.x <= GameStage.WINDOW_WIDTH-this.width) { //continue moving to the right
			this.x += XWing.speed;
		} else if(this.moveRight == true && this.x >= GameStage.WINDOW_WIDTH-this.width) { //will move to the left (since it moves to right and reached the window width)
			this.moveRight = !this.moveRight;
			if(this.type==0) this.setImage(PLAYER_LEFT);
		} else if(this.moveRight == false && this.x >= 0){ //continue moving to the left
			this.x -= XWing.speed;
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