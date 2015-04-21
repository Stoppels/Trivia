/*
 * The MIT License
 *
 * Copyright 2015 Team Silent Coders.
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

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author AMS09
 * @version 1.0
 */
public class MainMenuController extends Trivia implements Initializable {

	@FXML
	private Label uitlegA;

	@FXML
	private Label uitlegB;

	@FXML
	private Label uitlegC;

	@FXML
	private Label uitlegD;

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}

	@FXML
	private void startGame() {
		loadView("Question");
	}

	@FXML
	private void setUpGame() {
		loadView("GameSetUp");
	}

	@FXML
	private void toggleHelp() {
		List<Label> helpItems = Arrays.asList(uitlegA, uitlegB, uitlegC, uitlegD);

		for (Label a : helpItems) {
			boolean visibility = (a.isVisible() != true);
			a.setVisible(visibility);
		}
	}

	/**
	 * Triggers a confirmation dialog for quitting the app.
	 */
	@FXML
	private void exit() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Afsluiten");
		alert.setHeaderText("Weet u zeker dat u wilt afsluiten?");
		alert.setContentText("Hiermee wordt het programma afgesloten.");
		alert.initStyle(StageStyle.UNDECORATED);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			System.exit(0);
		}
	}

}
