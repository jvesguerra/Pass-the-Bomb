package miniproj;

import java.util.ArrayList;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GameTimer extends AnimationTimer{

	private GraphicsContext gc;
	private Scene theScene;
	private GameStage gameStage;
	private XWing xwing;
	private ArrayList<Enemy> enemies;
	private ArrayList<PowerUps> powerups;
	private ArrayList<Bomb> bombs;


	public static final int PLAYERS = 3; //also the initial enemies spawned
	//public static final int NEW_SPAWN_ENEMIES = 3;

	private double currentTime = System.nanoTime();

	private int previousSecond = -1;
	public static final double SPAWN_TIME = 5.0d;
	public static final double POWERUP_SPAWN_TIME = 10.0d;
	//public static final double BOSS_SPAWN_TIME = 30.0d;


	public final Image bg = new Image("images/background.jpg",GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT,false,false);

	GameTimer(GraphicsContext gc, Scene theScene, GameStage gameStage){
		this.gc = gc;
		this.theScene = theScene;
		this.gameStage = gameStage;
		this.xwing = new XWing("XWing",XWing.XWING_X_POS,XWing.XWING_Y_POS); //initial position is at x=100, y=250
		this.enemies = new ArrayList<Enemy>();
		this.powerups = new ArrayList<PowerUps>();
		this.bombs = new ArrayList<Bomb>();
		this.spawnEnemies();
		this.spawnBomb();
		this.handleKeyPressEvent();
	}

	@Override
	public void handle(long currentNanoTime) {
		double time = (currentNanoTime - currentTime)/1000000000.0;

		if(previousSecond != (int)time && (int)time != 0) {
//			if((int)(time%GameTimer.SPAWN_TIME) == 0) {
//				this.spawnNewEnemies();
//			}
//			if((int)time == (int)GameTimer.BOSS_SPAWN_TIME) {
//				this.spawnBossEnemy();
//			}
			if((int)time%GameTimer.POWERUP_SPAWN_TIME == 0) {
				this.spawnPowerUps();
			}

			this.despawnPowerUps();

			if(this.xwing.isInvincible()) {
				this.xwing.setInvincibilityElapsed();
			}
		}

//		System.out.println((int)time); //practice checker

		this.gc.clearRect(0, 0, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);

		this.gc.drawImage(this.bg, 0, 0);

		this.xwing.move();
		this.moveBullets();
		this.moveEnemies();
		this.checkPowerUpsCollision();

		this.xwing.render(this.gc);
		this.renderEnemies();
		this.renderBullets();
		this.renderPowerUps();
		this.renderBomb();

		this.gameCheck(time);
		this.drawDetails(time);

		if(this.previousSecond != (int)time) {
			this.previousSecond = (int)time;
		}
	}

	private void drawDetails(double t) {
		int time = (int) t; //typecasting a double to int

		String gameDetails;
		int strength = this.xwing.getStrength();
		gameDetails = "Time: 0" + (time>=60?(time/60):0) + (time%60<=9?":0":":") + time%60 + "	Score: " + this.xwing.getScore() + "	Strength: " + (strength<=0?0:strength);

		this.gc.fillText(gameDetails, 450, 50);
		this.gc.setFont(Font.font("Impact", FontWeight.NORMAL, 20));
		this.gc.setFill(Color.WHITE);
	}

	private void gameCheck(double t) {
		int time = (int) t; //typecasting a double to int

		if(!this.xwing.isAlive()) { //the player loses the game (xwing died)
			this.gameStage.flashGameOver(0, this.xwing.getScore());
			this.stop();
		} else if(time >= 60) { //the player wins the game
			this.gameStage.flashGameOver(1, this.xwing.getScore());
			this.stop();
		}
	}

	//method that will render/draw the enemies to the canvas
	private void renderEnemies() {
		for (Enemy e : this.enemies){
			e.render(this.gc);
		}
	}

	//method that will render/draw the bullets to the canvas
	private void renderBullets() {
		for(Bullet b : this.xwing.getBullets()) {
			b.render(this.gc);
		}
	}

	private void renderBomb() {
		for(Bomb p: this.bombs) {
			p.render(this.gc);
		}
	}

	private void spawnBomb() {
		Bomb newBomb;
		Random r = new Random();
		int x = r.nextInt(GameStage.WINDOW_WIDTH/2); //location is at lesser half of screen
		int y = r.nextInt(GameStage.WINDOW_HEIGHT-Bomb.BOMB_HEIGHT); //it won't succeed window height

		int type = r.nextInt(2);
		newBomb = new BombObject(x,y);

		this.bombs.add(newBomb);
	}


	private void renderPowerUps() {
		for(PowerUps p: this.powerups) {
			p.render(this.gc);
		}
	}

	private void spawnPowerUps() {
		PowerUps newPowerUp;
		Random r = new Random();
		int x = r.nextInt(GameStage.WINDOW_WIDTH/2); //location is at lesser half of screen
		int y = r.nextInt(GameStage.WINDOW_HEIGHT-PowerUps.POWERUP_HEIGHT); //it won't succeed window height

		int type = r.nextInt(2);

		newPowerUp = type==0?new Orb(x, y):new RebelAlliance(x, y);

		this.powerups.add(newPowerUp);
	}

	private void despawnPowerUps() {
		for(int i=0; i<this.powerups.size(); i++) {
			PowerUps p = this.powerups.get(i);
			p.setAvailabilityTimeElapsed();
			if(!p.isAvailable()) {
				this.powerups.remove(i);
			}
		}
	}

	private void checkPowerUpsCollision() {
		for(int i=0; i<this.powerups.size(); i++) {
			PowerUps p = this.powerups.get(i);
			if(p.isAvailable()) {
				p.checkCollision(this.xwing);
			}else {
				this.powerups.remove(i);
			}
		}
	}

	//method that will spawn/instantiate seven enemies at a random x,y location
	private void spawnEnemies(){ //initial
		Random r = new Random();
		for(int i=0;i<GameTimer.PLAYERS;i++){
			int x = r.nextInt(GameStage.WINDOW_WIDTH/2)+400; //location is at greater half of screen
			int y = r.nextInt(GameStage.WINDOW_HEIGHT-Enemy.ENEMY_SIZE); //it won't succeed window height
			this.enemies.add(new Enemy(x, y, 0));
		}
	}

//	private void spawnNewEnemies() { //add enemies at certain interval
//		Random r = new Random();
//		for(int i=0;i<GameTimer.NEW_SPAWN_ENEMIES;i++){
//			int x = r.nextInt(GameStage.WINDOW_WIDTH/2)+400; //location is at greater half of screen
//			int y = r.nextInt(GameStage.WINDOW_HEIGHT-Enemy.ENEMY_SIZE); //it won't succeed window height
//			this.enemies.add(new Enemy(x, y, 0));
//		}
//	}

//	private void spawnBossEnemy() { //add boss at 30 seconds
//		Random r = new Random();
//		int x = r.nextInt(GameStage.WINDOW_WIDTH/2)+400; //location is at greater half of screen
//		int y = r.nextInt(GameStage.WINDOW_HEIGHT-Enemy.DEATHSTAR_SIZE); //it won't succeed window height
//		this.enemies.add(new Enemy(x, y, 1));
//	}

	//method that will move the bullets shot by a ship
	private void moveBullets(){
		//create a local arraylist of Bullets for the bullets 'shot' by the ship
		ArrayList<Bullet> bList = this.xwing.getBullets();

		//Loop through the bullet list and check whether a bullet is still visible.
		for(int i = 0; i < bList.size(); i++){
			Bullet b = bList.get(i);
			if(b.getVisible() == true) {
				b.move();
			} else {
				bList.remove(i);
			}
		}
	}

	//method that will move the enemies
	private void moveEnemies(){
		//Loop through the enemies arraylist
		for(int i = 0; i < this.enemies.size(); i++){
			Enemy e = this.enemies.get(i);
			if(e.isAlive()) {
				e.move();
				e.checkCollision(this.xwing, e.enemyType());
			} else {
				this.enemies.remove(i);
			}
		}
	}

	//method that will listen and handle the key press events
	private void handleKeyPressEvent() {
		this.theScene.setOnKeyPressed(new EventHandler<KeyEvent>(){
			public void handle(KeyEvent e){
            	KeyCode code = e.getCode();
                moveXWing(code);
			}
		});

		this.theScene.setOnKeyReleased(new EventHandler<KeyEvent>(){
		            public void handle(KeyEvent e){
		            	KeyCode code = e.getCode();
		                stopXWing(code);
		            }
		        });
    }

	//method that will move the XWing depending on the key pressed
	private void moveXWing(KeyCode ke) { //should move 5 pixels
		if(ke==KeyCode.UP) this.xwing.setDY(-1*XWing.XWING_SPEED);

		if(ke==KeyCode.LEFT) this.xwing.setDX(-1*XWing.XWING_SPEED);

		if(ke==KeyCode.DOWN) this.xwing.setDY(XWing.XWING_SPEED);

		if(ke==KeyCode.RIGHT) this.xwing.setDX(XWing.XWING_SPEED);

		if(ke==KeyCode.SPACE) this.xwing.shoot();

		System.out.println(ke+" key pressed.");
   	}

	//method that will stop the ship's movement; set the ship's DX and DY to 0
	private void stopXWing(KeyCode ke){
		this.xwing.setDX(0);
		this.xwing.setDY(0);
	}
}
