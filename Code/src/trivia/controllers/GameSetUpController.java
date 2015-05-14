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
package trivia.controllers;

import static java.lang.Integer.parseInt;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ToggleButton;
import trivia.Trivia;

/**
 * FXML Controller class
 *
 * @author Team Silent Coders
 * @version 1.0
 */
public class GameSetUpController extends Trivia implements Initializable {

	@FXML
	ToggleButton shortLength;

	@FXML
	ToggleButton mediumLength;

	@FXML
	ToggleButton longLength;

	@FXML
	ToggleButton xxlLength;

	@FXML
	ToggleButton difficultyEasy;

	@FXML
	ToggleButton difficultyHard;

	@FXML
	ToggleButton difficultyMixed;

	@FXML
	ToggleButton typeTf;

	@FXML
	ToggleButton typeMc;

	@FXML
	ToggleButton typeMixed;

	@FXML
	ToggleButton timerToggle;

	@FXML
	Button mainMenu;

	@FXML
	Button startGame;

	private List<ToggleButton> lengthButtons;
	static int gameLength = 10;
	public static boolean difficultyIsMixed = true;
	public static boolean difficultyIsEasy = true;
	static boolean tfHolder = true;
	static boolean mcHolder = true;
	static boolean timerHolder = true;

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		mainMenu.setOnAction(this::loadView);
		startGame.setOnAction(this::startGame);

		// Perform action for all items in list
		lengthButtons = Arrays.asList(shortLength, mediumLength, longLength, xxlLength);
		for (ToggleButton a : lengthButtons) {
			a.setOnAction(this::handleLengthSelection);
		}
	}

	private void handleLengthSelection(ActionEvent event) {
		String selectedButton = ((Control) event.getSource()).getId();
		switch (selectedButton) {
			case "shortLength":
				shortLength.setSelected(true);
				gameLength = parseInt(shortLength.getText());
				break;
			case "mediumLength":
				mediumLength.setSelected(true);
				gameLength = parseInt(mediumLength.getText());
				break;
			case "longLength":
				longLength.setSelected(true);
				gameLength = parseInt(longLength.getText());
				break;
			case "xxlLength":
				xxlLength.setSelected(true);
				gameLength = parseInt(xxlLength.getText());
				break;
			default:
				gameLength = 10;
		}
	}

	@FXML
	private void startGame(ActionEvent event) {
		System.out.print("Difficulty setting: ");
		if (difficultyMixed.isSelected()) {
			System.out.println("Mixed");
			difficultyIsMixed = true;
		} else if (difficultyEasy.isSelected()) {
			System.out.println("Easy");
			difficultyIsMixed = false;
		} else if (difficultyHard.isSelected()) {
			System.out.println("Hard");
			difficultyIsMixed = false;
		}

		System.out.print("Question type: ");
		if (typeMixed.isSelected()) {
			System.out.println("Mixed");
		} else if (typeTf.isSelected()) {
			System.out.println("True/false");
		} else if (typeMc.isSelected()) {
			System.out.println("Four choices");
		}

		System.out.print("Timer is: ");
		if (timerToggle.isSelected()) {
			timerHolder = true;
			System.out.println("Enabled");
		} else {
			timerHolder = false;
			System.out.println("Disabled");
		}
		loadView(event);
	}
}
