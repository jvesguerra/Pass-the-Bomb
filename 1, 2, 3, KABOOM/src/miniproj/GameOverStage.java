package miniproj;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class GameOverStage {
	public final static String EXIT = "Exit";
	private int num; //win or lose
	private Scene scene;
	private VBox root;
	private Stage stage;
	private Canvas canvas;
	private int score;

	GameOverStage(int ending, int score) {
		this.root = new VBox();
		this.scene = new Scene(root, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT, Color.WHITE);
		this.canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
		this.root.getChildren().add(this.canvas);
		this.num = ending; //from flashGameOver
		this.score = score;
	}

	public void setStage(Stage stage) {
		this.stage = stage;

		if(this.num == 0) { //loser
			this.stage.setTitle("LOSE");
			System.out.println("Lose");
		} else { //winner
			this.stage.setTitle("WIN");
			System.out.println("Win");
		}
		this.setProperties();

		this.stage.setScene(this.scene);
		this.stage.setResizable(false);
		this.stage.show();
	}

	private void setProperties() {
		StackPane root = new StackPane();
		root.getChildren().addAll(this.createCanvas(), this.createVBox());
		this.scene = new Scene(root);
	}

	private Canvas createCanvas() {
		Canvas canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		if(this.num == 0) { //loses the game
			Image bg = new Image("images/game_lose.png",GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT,false,false); //change natin to into picture ng loser
			gc.drawImage(bg, 0, 0);
		} else { //wins the game
			Image bg = new Image("images/game_win.png",GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT,false,false); //change natin to into picture ng winner
			gc.drawImage(bg, 0, 0);
		}

		return canvas;
	}

	private VBox createVBox() {
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(200,10,10,10));
		vbox.setSpacing(8);

		Text scoreText = new Text();
		Button b1 = new Button("Exit");

		scoreText.setText("Score: " + this.score);
		scoreText.setFont(Font.font("Impact", FontWeight.NORMAL, 20));

		if(this.num == 0)scoreText.setFill(Color.BLACK);
		else scoreText.setFill(Color.WHITE);

		vbox.getChildren().add(scoreText);
		vbox.getChildren().add(b1);

		b1.setOnAction(new EventHandler<ActionEvent>() { //exits the whole game
			public void handle(ActionEvent e) {
				System.out.println(GameOverStage.EXIT);
				System.exit(0);
			}
		});

		return vbox;
	}

}
