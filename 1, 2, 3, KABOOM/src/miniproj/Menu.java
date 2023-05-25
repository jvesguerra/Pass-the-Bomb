package miniproj;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;

public class Menu {
	public static final String NEW_GAME = "New Game";
	public static final String HOW_TO_PLAY = "How to Play";
	public static final String ABOUT = "About";
	public static final String BACK = "Back";

	private Socket socket;
	private int playerID;

	private Scene splashScene;
	private Stage stage;
	private HBox root;
	private Canvas canvas;

	public Menu() {
		this.root = new HBox();
		this.splashScene = new Scene(root, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT, Color.WHITE);
		this.canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
		this.root.getChildren().add(this.canvas);
	}

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
		}catch(IOException ex){
			System.out.println("IOException from connectToServer()");
		}
	}

	public void setStage(Stage stage) {
		this.stage = stage;
		stage.setTitle("Star Wars Shooting Game");
		this.initSplash(stage);

		stage.setScene(this.splashScene);
		stage.setResizable(false);
		stage.show();
		
	}

	private void initSplash(Stage stage) {
		StackPane root = new StackPane();
		root.getChildren().addAll(this.createCanvas(), this.createHBox());
		this.splashScene = new Scene(root);
	}

	private Canvas createCanvas() {
		Canvas canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		Image bg = new Image("images/menu.png",GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT,false,false);
		gc.drawImage(bg, 0, 0);
		return canvas;
	}

	private HBox createHBox() {
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.BOTTOM_CENTER);
		hbox.setPadding(new Insets(10,50,85,50));
		hbox.setSpacing(10);

		Button b1 = new Button(Menu.NEW_GAME);
		Button b2 = new Button(Menu.HOW_TO_PLAY);
		Button b3 = new Button(Menu.ABOUT);

		hbox.getChildren().addAll(b1, b2, b3);

		b1.setOnAction(new EventHandler<ActionEvent>() { //new game
			public void handle(ActionEvent e) {
				setGame(stage, 1);
			}
		});
		b2.setOnAction(new EventHandler<ActionEvent>() { //how to play
			public void handle(ActionEvent e) {
				setGame(stage, 2);
			}
		});
		b3.setOnAction(new EventHandler<ActionEvent>() { //about
			public void handle(ActionEvent e) {
				setGame(stage, 3);
			}
		});

		return hbox;
	}

	private void setGame(Stage stage, int screen) {
		stage.setScene(this.splashScene);

		switch(screen) {
			case 1: //start with the game
				//wait for other players after starting
				// check for number of players before proceeding to game stage
				// pass player id in game stage
				this.connectToServer();
				System.out.println(Menu.NEW_GAME);
				GameStage gameStage = new GameStage(this.playerID);
				gameStage.setStage(stage);
				break;
			case 2: //go to how to play screen
				System.out.println(Menu.HOW_TO_PLAY);
				Options option1 = new Options();
				option1.setStage(stage, 0);
				break;
			case 3: //go to about screen
				System.out.println(Menu.ABOUT);
				Options option2 = new Options();
				option2.setStage(stage, 1);
				break;
		}
	}
}
