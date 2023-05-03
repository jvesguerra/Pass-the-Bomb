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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Options {
	private Scene scene;
	private VBox root;
	private Stage stage;
	private Canvas canvas;
	private int choice;

	public Options() {
		this.root = new VBox();
		this.scene = new Scene(root, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT, Color.WHITE);
		this.canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
		this.root.getChildren().add(this.canvas);
	}

	public void setStage(Stage stage, int choice) {
		this.choice = choice;
		this.stage = stage;

		if(choice == 0) { //how to play
			this.stage.setTitle("How To Play");
		} else { //about
			this.stage.setTitle("About");
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

		if(this.choice == 0) { //how to play
			Image bg = new Image("images/how_to_play.png",GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT,false,false);
			gc.drawImage(bg, 0, 0);
		} else { //about
			Image bg = new Image("images/about.png",GameStage.WINDOW_WIDTH,GameStage.WINDOW_HEIGHT,false,false);
			gc.drawImage(bg, 0, 0);
		}
		return canvas;
	}

	private VBox createVBox() {
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.BOTTOM_RIGHT);
		vbox.setPadding(new Insets(20));
		vbox.setSpacing(8);

		Button b1 = new Button("Back");

		vbox.getChildren().add(b1);

		b1.setOnAction(new EventHandler<ActionEvent>() { //return to menu
			public void handle(ActionEvent e) {
				System.out.println(Menu.BACK);
				Menu menu = new Menu();
				menu.setStage(stage);
			}
		});

		return vbox;
	}

}
