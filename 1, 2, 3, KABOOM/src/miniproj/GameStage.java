package miniproj;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameStage {
	public static final int WINDOW_HEIGHT = 720;
	public static final int WINDOW_WIDTH = 1280;
	private Scene scene;
	private Stage stage;
	private Group root;
	private Canvas canvas;
	private GraphicsContext gc;
	private GameTimer gametimer;

	//the class constructor
	public GameStage(int playerID) {
		this.root = new Group();
		this.scene = new Scene(root, GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT,Color.WHITE);
		this.canvas = new Canvas(GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT);
		this.gc = canvas.getGraphicsContext2D();
		//instantiate an animation timer
		this.gametimer = new GameTimer(this.gc,this.scene,this,playerID);
	}

	//method to add the stage elements
	void setStage(Stage stage) {
		this.stage = stage;

		//set stage elements here
		this.root.getChildren().add(canvas);

		this.stage.setTitle("Star Wars Shooting Game");
		this.stage.setScene(this.scene);

		//invoke the start method of the animation timer
		this.gametimer.start();

		this.stage.show();
	}

	void flashGameOver(int ending, int score){
		PauseTransition transition = new PauseTransition(Duration.seconds(1));
		transition.play();

		transition.setOnFinished(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				GameOverStage gameOver = new GameOverStage(ending, score);
				gameOver.setStage(stage);
			}
		});
	}
}

