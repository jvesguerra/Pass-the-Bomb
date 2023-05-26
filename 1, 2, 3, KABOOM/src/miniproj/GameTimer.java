package miniproj;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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
	private XWing player2;
	private ArrayList<PowerUps> powerups;
	private ArrayList<Obstacles> obstacles;
	private ArrayList<XWing> players;
	private double currentTime = System.nanoTime();
	private int previousSecond = -1;
	private int playerID = 0;

	public static final int PLAYERS = 3;
	public static final double SPAWN_TIME = 5.0d;
	public static final double EXPLOTION_TIME = 5.0d;
	public static final double POWERUP_SPAWN_TIME = 5.0d;

	// NETWORK
	private ReadFromServer rfsRunnable;
	private WriteToServer wtsRunnable;
	private Socket socket;


	public final Image bg = new Image("images/background.jpg",GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT,false,false);

	//GameTimer(GraphicsContext gc, Scene theScene, GameStage gameStage, int playerID){
	GameTimer(GraphicsContext gc, Scene theScene, GameStage gameStage){
		this.gc = gc;
		this.theScene = theScene;
		this.gameStage = gameStage;
		this.powerups = new ArrayList<PowerUps>();
		this.obstacles = new ArrayList<Obstacles>();
		this.players = new ArrayList<XWing>();
		this.handleKeyPressEvent();

		this.connectToServer();
		// initialize xwing
		if(playerID == 1){
			this.xwing = new XWing("XWing",100,250); //initial position is at x=100, y=250
			this.player2 = new XWing("Player 2",200,500);

			this.xwing.setType(1);
			this.player2.setType(0);
		} else if (playerID == 2){
			System.out.println("Test");
			this.player2 = new XWing("Player 2",100,250); //initial position is at x=100, y=250
			this.xwing = new XWing("XWing",200,500);

			this.xwing.setType(0);
			this.player2.setType(1);
		}
		// add players to players array list to remove them
		System.out.println(playerID);
	}

	@Override
	public void handle(long currentNanoTime) {
		double time = (currentNanoTime - currentTime)/1000000000.0;

		if(previousSecond != (int)time && (int)time != 0) {
			// if((int)(time%GameTimer.EXPLOTION_TIME) == 0) {
			// 	this.removePlayer();
			// 	this.assignRandomBomb();
			// }

			// if((int)time%GameTimer.POWERUP_SPAWN_TIME == 0) {
			// 	//this.spawnPowerUps();
			// 	this.spawnObstacles();
			// }
			// this.despawnPowerUps();
			// this.despawnObstacles();

			// if(this.xwing.isInvincible()) {
			// 	this.xwing.setInvincibilityElapsed();
			// }

			// if(this.xwing.isSpeedUp()) {
			// 	this.xwing.setSpeedUpElapsed();
			// }

			// if(this.xwing.isSpeedDown()) {
			// 	this.xwing.setSpeedDownElapsed();
			// }

			// if(this.xwing.isStun()) {
			// 	this.xwing.setStunElapsed();
			// }
		}

		this.gc.clearRect(0, 0, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.gc.drawImage(this.bg, 0, 0);

		this.checkPowerUpsCollision();
		this.checkPlayerCollision();
		this.checkObstaclesCollision();

		this.xwing.render(this.gc);
		this.player2.render(this.gc);

		this.renderPowerUps();
		this.renderObstacles();
		this.gameCheck(time);
		this.drawDetails(time);

		this.xwing.move();

		if(this.previousSecond != (int)time) {
			this.previousSecond = (int)time;
		}
	}

	// NETWORKING	
	// connect to server
	private void connectToServer(){
		try{
			socket = new Socket("localhost",12345);
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			playerID = in.readInt();
			System.out.println("You are player#"+playerID);
			if(playerID == 1){
				System.out.println("Waiting for Player #2 to connect");
			}

			rfsRunnable = new ReadFromServer(in);
			wtsRunnable = new WriteToServer(out);
		}catch(IOException ex){
			System.out.println("IOException from connectToServer()");
		}
	}

	private class ReadFromServer implements Runnable{
		private DataInputStream dataIn;

		public ReadFromServer(DataInputStream in){
			dataIn = in;
			System.out.println("RFS Runnable created");
		}

		public void run(){
			try{
                while(true){
					player2.setDX(dataIn.readInt());
					player2.setDY(dataIn.readInt());
                }
            }catch(IOException ex){
                System.out.println("IOException from RFS run()");
            }
		}
	}

	private class WriteToServer implements Runnable{
		private DataOutputStream dataOut;

		public WriteToServer(DataOutputStream out){
			dataOut = out;
			System.out.println("WTS Runnable created");
		}

		public void run(){
			try{
				while(true){	// sends coordinates
					dataOut.writeInt(xwing.getX());
					dataOut.writeInt(xwing.getY());
					dataOut.flush();
					try{
						Thread.sleep(25);
					}catch(InterruptedException ex){
						System.out.println("InterruptedException from WTS run()");
					}
				}
			}catch(IOException ex){
				System.out.print("IOException from WTS run()");
			}
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

	private void renderPowerUps() {
		for(PowerUps p: this.powerups) {
			p.render(this.gc);
		}
	}

	private void renderObstacles() {
		for(Obstacles p: this.obstacles) {
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

	private void spawnObstacles() {
		Obstacles newObstacle;
		Random r = new Random();
		int x = r.nextInt(GameStage.WINDOW_WIDTH/2); //location is at lesser half of screen
		int y = r.nextInt(GameStage.WINDOW_HEIGHT-Obstacles.OBSTACLE_HEIGHT); //it won't succeed window height

		int type = r.nextInt(2);

		newObstacle = type==0?new Slow(x, y):new Stun(x, y);

		this.obstacles.add(newObstacle);
	}

	private void removePlayer(){
		for(int i=0;i<this.players.size();i++){
			XWing p = this.players.get(i);
			if(p.getType() == 1) {
				this.players.remove(p);
			}
		}
	}

	private void assignRandomBomb(){
		Random r = new Random();
		int i = r.nextInt(this.players.size());
		XWing p = this.players.get(i);
		p.setType(1);
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

	private void despawnObstacles() {
		for(int i=0; i<this.obstacles.size(); i++) {
			Obstacles p = this.obstacles.get(i);
			p.setAvailabilityTimeElapsed();
			if(!p.isAvailable()) {
				this.obstacles.remove(i);
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

	private void checkObstaclesCollision() {
		for(int i=0; i<this.obstacles.size(); i++) {
			Obstacles p = this.obstacles.get(i);
			if(p.isAvailable()) {
				p.checkCollision(this.xwing);
			}else {
				this.obstacles.remove(i);
			}
		}
	}

	private void checkPlayerCollision() {
		for(int i=0; i<this.players.size(); i++) {
			XWing p = this.players.get(i);
			if(p.isAvailable()) {
				p.checkCollision(this.xwing, p);
			}
			else {
				this.players.remove(i);
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
		if(ke==KeyCode.UP){
			this.xwing.setDY(-1*XWing.XWING_SPEED);
			this.xwing.faceUp();
		}

		if(ke==KeyCode.LEFT){
			this.xwing.setDX(-1*XWing.XWING_SPEED);
			this.xwing.faceLeft();
		}

		if(ke==KeyCode.DOWN){
			this.xwing.setDY(XWing.XWING_SPEED);
			this.xwing.faceDown();

		}

		if(ke==KeyCode.RIGHT){
			this.xwing.setDX(XWing.XWING_SPEED);
			this.xwing.faceRight();
		}

		//System.out.println(ke+" key pressed.");
   	}

	//method that will stop the ship's movement; set the ship's DX and DY to 0
	private void stopXWing(KeyCode ke){
		this.xwing.setDX(0);
		this.xwing.setDY(0);
	}
}
