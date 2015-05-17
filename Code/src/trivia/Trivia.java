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

import java.io.IOException;
import java.util.Optional;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Application main class
 *
 * @author Team Silent Coders
 * @version 1.0
 */
public class Trivia extends Application {

	@Override
	public void start(Stage stage) {
		//System.out.close(); // <- Uncomment on product shipment to shush console debug messages.
		try {
			Parent root = FXMLLoader.load(getClass().
					getResource("/trivia/views/SplashScreen.fxml"));
			Scene scene = new Scene(root);

			root.setId("pane");
			scene.getStylesheets().addAll(this.getClass().
					getResource("/resources/stylesheets/Styles.css").toExternalForm());

			stage.setScene(scene);
			stage.setFullScreenExitHint("");
			//stage.setFullScreen(true);
			stage.show();
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
	 * Handles switching between views.
	 *
	 * @param event
	 */
	public void loadView(ActionEvent event) {
		boolean error = false;

		// Fetch the FX:ID of the button and find out which button it was.
		String viewName = ((Control) event.getSource()).getId();

		// Depending on the target view, do something.
		switch (viewName) {
			case "mainMenu":
				viewName = "MainMenu";
				break;
			case "adminMenu":
				viewName = "AdminMenu";
				break;
			case "startGame":
				viewName = "Question";
				break;
			case "addQuestion":
				viewName = "AddQuestion";
				break;
			case "manageQuestions":
				viewName = "ManageQuestions";
				break;
			case "gameSetUp":
				viewName = "GameSetUp";
				break;
//			case "openSettings":
//				viewName = "OpenSettings";
//				break;
//			case "nameEntry":
//				viewName = "NameEntry";
//				break;
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
				Scene scene = new Scene(root);
				root.setId("pane");
				scene.getStylesheets().addAll(this.getClass().
						getResource("/resources/stylesheets/Styles.css").toExternalForm());

				Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
				stage.setScene(scene);
				//stage.setFullScreen(true);
				stage.show();
			} catch (IOException e) {
				System.err.println(e.getLocalizedMessage() + "\n"
						+ e.getLocalizedMessage());
			}
		}
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
