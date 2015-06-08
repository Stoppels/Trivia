/*
 * The MIT License
 *
 * Copyright 2015 Team Silent Coders.
 * Application developed for Amsterdam University of Applied Sciences and Amsta.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package trivia;

import javafx.scene.image.Image;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import static trivia.AppConfig.*;

/**
 * Application main class
 *
 * @author Team Silent Coders
 * @version 1.0
 */
public class Trivia extends Application {

	// Preference holders.
	public static Preferences prefs;
	public static String difficultyHolder = "1", typeHolder = "2", lengthHolder = "3",
			timerHolder = "4", difficultyModifier = "5", lengthModifier = "6",
			typeModifier = "7", timerModifier = "8", timerLength = "9",
			dbUrl = "10", dbUser = "11", dbPass = "12";
	public static String[] varHolder = new String[4];
	public static Boolean[] boolHolder = new Boolean[5];

	// Length of time for each question.
	public static final Integer START_TIME = 30;
	public static Integer setTime = 0;

	public static Boolean timerSetting = TIMER_DEFAULT;
	public static Integer gameLength = DEFAULT_LENGTH;

	// For use in any class.
	public String viewName = "";
	public static ResultSet rs = null;
	public static PreparedStatement statement;
	public static List<String> updateParameters;
	public static Boolean duplicateError = false;
	public static Stage stage = null;
	public static Scene scene = null;
	public static Rectangle2D screenBounds;

	@Override
	public void start(Stage startStage) {
		//streamSettings(); // Shushing out stream & saving err.
		try {
			screenBounds = Screen.getPrimary().getVisualBounds(); // Get screen size.

			stage = startStage;
			stage.getIcons().add(new Image("resources/images/logo.png")); // App icon.
			Parent root = FXMLLoader.load(getClass().
					getResource("/trivia/views/SplashScreen.fxml"));
			root.setId("pane");
			scene = new Scene(root);
			scene.getStylesheets().addAll(this.getClass().
					getResource("/resources/stylesheets/Styles.css").toExternalForm());

			stage.setScene(scene);
			stage.setTitle(APPLICATION_NAME);
			stage.setMinHeight(MIN_HEIGHT);
			stage.setMinWidth(MIN_WIDTH);
//			stage.setFullScreen(true);
			stage.setWidth(screenBounds.getWidth());
			stage.setHeight(screenBounds.getHeight());
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setFullScreenExitHint("");
			stage.show();
			prefs = Preferences.userRoot().node(this.getClass().getName());
			setTime = Integer.parseInt(prefs.get(timerLength, START_TIME.toString()));
		} catch (IOException e) {
			System.err.println("Error: " + e.getLocalizedMessage());
		}
	}

	/**
	 * Allows for quick and clean creation of a boolean AlertDialog.
	 *
	 * @param type
	 * @param title
	 * @param header
	 * @param content
	 * @param style
	 * @return boolean value
	 */
	public static boolean alertDialog(AlertType type, String title,
			String header, String content, StageStyle style) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.initStyle(style);

		// Does user choose button OK or not?
		Optional<ButtonType> result = alert.showAndWait();
		return result.get() == ButtonType.OK;
	}

	/**
	 * This precious makes sure the last saved settings are loaded.
	 */
	public static void loadSettings() {
		System.out.println("Loading default settings.");

		varHolder[0] = prefs.get(difficultyHolder, "difficultyMixed");
		varHolder[1] = prefs.get(typeHolder, "typeMc");
		varHolder[2] = prefs.get(lengthHolder, "shortLength");
		varHolder[3] = prefs.get(timerLength, START_TIME.toString());
		boolHolder[0] = prefs.getBoolean(timerHolder, true);
		boolHolder[1] = prefs.getBoolean(difficultyModifier, false);
		boolHolder[2] = prefs.getBoolean(typeModifier, false);
		boolHolder[3] = prefs.getBoolean(lengthModifier, false);
		boolHolder[4] = prefs.getBoolean(timerModifier, true);
	}

	/**
	 * Handles switching between views.
	 *
	 * @param event
	 */
	public void loadView(ActionEvent event) {
		boolean error = false;

		// Fetch the FX:ID of the button and find out which button it was.
		viewName = ((Control) event.getSource()).getId();

		// Depending on the target view, do something.
		switch (viewName) {
			case "mainMenu":
				viewName = "MainMenu";
				break;
			case "adminMenu":
			case "saveSettings":
				viewName = "AdminMenu";
				break;
			case "startGame":
				viewName = "Question";
				break;
			case "nextQuestionButton":
			case "viewScore":
				viewName = "GameOverview";
				break;
			case "highScore":
//				viewName = "HighScore"; <——————————————— Boys, afmaken?
				viewName = "MainMenu";
				break;
			case "gameSetUp":
				viewName = "GameSetUp";
				break;
			case "defaultSettings":
				viewName = "DefaultSettings";
				break;
			case "addQuestion":
				viewName = "AddQuestion";
				break;
			case "manageQuestions":
				viewName = "ManageQuestions";
				break;
			case "advancedSettings":
				viewName = "AdvancedSettings";
				break;
			default:
				System.err.println("View " + viewName + " not found.");
				error = true;
				break;
		}

		// Was a view chosen? If true continue, otherwise do nothing.
		if (!error) {
			System.out.println("Opening: " + viewName);

			try {
				Parent root = FXMLLoader.load(getClass().getResource("/trivia/views/"
						+ viewName + ".fxml"));
//				stage.setFullScreen(true);
				scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
				root.setId("pane");
				scene.getStylesheets().addAll(this.getClass().
						getResource("/resources/stylesheets/Styles.css").toExternalForm());
				stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
				stage.setScene(scene);
				stage.setTitle(APPLICATION_NAME + " " + viewName);
				stage.show();
			} catch (LoadException e) {
				System.out.print("LoadException with file: " + e.getLocalizedMessage());
				e.printStackTrace();
			} catch (IOException e) {
				System.out.print("Error (loadView): " + e.getLocalizedMessage());
			}
		}
	}

	private void streamSettings() {
		// Uncomment on product shipment to shush console debug messages.
		System.out.close();

		try { // Logs errors in a tmp file.
			Path path = Paths.get(System.getProperty("java.io.tmpdir"), "Trivia Error Logs.txt");
			PrintStream printer = new PrintStream(path.toFile());
			System.setErr(printer);
		} catch (IOException e) {
			System.err.println("Error: " + e.getLocalizedMessage());

		}

	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
