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

	private ArrayList<Bullet> bullets;
	private final static Image XWING_IMAGE = new Image("images/x_wing.png",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);
	private final static Image XWING_INVINCIBLE_IMAGE = new Image("images/x_wing_invincible.png",XWing.XWING_SIZE,XWing.XWING_SIZE,false,false);
	private final static int XWING_SIZE = 50;

	public static final int XWING_X_POS = 100;
	public static final int XWING_Y_POS = 250;

	public static final int XWING_SPEED = 10;

	private int score = 0;

	public XWing(String name, int x, int y){
		super(x,y,XWing.XWING_IMAGE);
		this.name = name;
		Random r = new Random();
		this.strength = r.nextInt(51)+100; //strength starts from 100-150
		this.alive = true;
		this.invincibility = false;
		this.bullets = new ArrayList<Bullet>();
		System.out.println("Strength: " + this.strength); //practice checker
	}

	public boolean isInvincible() {
		return this.invincibility;
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

	public int getStrength() {
		return this.strength;
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
			this.setImage(XWING_IMAGE);
		}
	}

	void die(){
    	this.alive = false;
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

	//method called if up/down/left/right arrow key is pressed.
	public void move() {
		if((this.x+this.dx >= 0 && this.y+this.dy >= 0) && (this.x+this.dx <= GameStage.WINDOW_WIDTH-this.width && this.y+this.dy <= GameStage.WINDOW_HEIGHT-this.height)) {
			this.x += this.dx;
			this.y += this.dy;
		}
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

	void setScore() {
		this.score+=1;
	}
}