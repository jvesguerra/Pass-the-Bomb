package game;

import javafx.application.Application;
import javafx.stage.Stage;
import miniproj.Menu;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage){ //will start from main menu (new game, how to play and about)
		Menu menu = new Menu();
		menu.setStage(stage);
	}

}
