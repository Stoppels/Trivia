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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import static trivia.AppConfig.LONG_LENGTH;
import static trivia.AppConfig.MEDIUM_LENGTH;
import static trivia.AppConfig.SHORT_LENGTH;
import trivia.Trivia;

/**
 * FXML Controller class
 *
 * @author Team Silent Coders
 * @version 1.0
 */
public class GameSetUpController extends Trivia implements Initializable {

	@FXML
	public ToggleGroup difficultyGroup;

	@FXML
	public ToggleGroup typeGroup;

	@FXML
	public ToggleGroup lengthGroup;

	@FXML
	public ToggleGroup timerGroup;

	@FXML
	private ToggleButton shortLengthButton;

	@FXML
	private ToggleButton mediumLengthButton;

	@FXML
	private ToggleButton longLengthButton;

	@FXML
	private ToggleButton difficultyEasyButton;

	@FXML
	private ToggleButton difficultyHardButton;

	@FXML
	private ToggleButton difficultyMixedButton;

	@FXML
	private ToggleButton typeTfButton;

	@FXML
	private ToggleButton typeMcButton;

	@FXML
	private ToggleButton typeMixedButton;

	@FXML
	private ToggleButton timerToggle;

	@FXML
	private ToggleButton timerToggleNo;

	@FXML
	private Button mainMenu;

	@FXML
	private Button startGame;

	private List<ToggleGroup> groupsList;
	static boolean difficultyIsMixed = true, difficultyIsEasy = true,
			typeIsMixed = true, typeIsTf = true;

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

		// Can't unselect entire ToggleGroup: keep one selected at all times.
		groupsList = Arrays.asList(difficultyGroup, typeGroup, lengthGroup, timerGroup);
		for (ToggleGroup tg : groupsList) {
			tg.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
				@Override
				public void changed(ObservableValue<? extends Toggle> ov,
						Toggle toggle, Toggle new_toggle) {
					if (new_toggle == null) {
						toggle.setSelected(true);
					}
				}
			});
		}

		getSettings();
	}

	/**
	 * This precious makes sure the last saved settings are loaded.
	 */
	private void getSettings() {
		loadSettings();

		for (String s : varHolder) {
			switch (s) {
				case "difficultyMixed":
					System.out.println("Default difficulty setting: mixed.");
					difficultyMixedButton.setSelected(true);
					break;
				case "difficultyEasy":
					System.out.println("Default difficulty setting: easy.");
					difficultyEasyButton.setSelected(true);
					break;
				case "difficultyHard":
					System.out.println("Default difficulty setting: hard.");
					difficultyHardButton.setSelected(true);
					break;
				case "typeMixed":
					System.out.println("Default type setting: mixed.");
					typeMixedButton.setSelected(true);
					break;
				case "typeTf":
					System.out.println("Default type setting: true or false.");
					typeTfButton.setSelected(true);
					break;
				case "typeMc":
					System.out.println("Default type setting: multiple choice.");
					typeMcButton.setSelected(true);
					break;
				case "shortLength":
					System.out.println("Default length setting: short.");
					shortLengthButton.setSelected(true);
					shortLengthButton.setText(String.valueOf(gameLength = SHORT_LENGTH));
					break;
				case "mediumLength":
					System.out.println("Default length setting: medium.");
					mediumLengthButton.setSelected(true);
					mediumLengthButton.setText(String.valueOf(gameLength = MEDIUM_LENGTH));
					break;
				case "longLength":
					System.out.println("Default length setting: long.");
					longLengthButton.setSelected(true);
					longLengthButton.setText(String.valueOf(gameLength = LONG_LENGTH));
					break;
				default: // Nothing is selected.
					if (parseInt(s) >= 10 && parseInt(s) <= 60) {
						System.out.println("Default timer duration: " + s + " seconds.");
					} else {
						System.err.println("Something is wrong with the (default) var prefs. Suspect: " + s);
					}
					break;
			}
		}

		int i = 0;
		for (Boolean b : boolHolder) {
			switch (i) {
				case 0:
					System.out.println("Default timer setting: " + (b ? "on." : "off."));
					timerToggle.setSelected(b);
					timerToggleNo.setSelected(!b);
					break;
				case 1:
					System.out.println("Default difficulty modifiability: " + (b ? "on." : "off."));
					difficultyMixedButton.setDisable(!b);
					difficultyEasyButton.setDisable(!b);
					difficultyHardButton.setDisable(!b);
					break;
				case 2:
					System.out.println("Default type modifiability: " + (b ? "on." : "off."));
					typeMixedButton.setDisable(!b);
					typeTfButton.setDisable(!b);
					typeMcButton.setDisable(!b);
					break;
				case 3:
					System.out.println("Default length modifiability: " + (b ? "on." : "off."));
					shortLengthButton.setDisable(!b);
					mediumLengthButton.setDisable(!b);
					longLengthButton.setDisable(!b);
					break;
				case 4:
					System.out.println("Default timer modifiability: " + (b ? "on." : "off."));
					timerToggle.setDisable(!b);
					timerToggleNo.setDisable(!b);
					break;
				default:
					System.err.println("Something is wrong with the (default) bool prefs.");
					break;
			}
			i++;
		}
	}

	private void handleLengthSelection(ActionEvent event) {
		String selectedButton = ((Control) event.getSource()).getId();
		switch (selectedButton) {
			case "shortLengthButton":
				shortLengthButton.setSelected(true);
				break;
			case "mediumLengthButton":
				mediumLengthButton.setSelected(true);
				break;
			case "longLengthButton":
				longLengthButton.setSelected(true);
				break;
		}
	}

	private void startGame(ActionEvent event) {
		System.out.print("Chosen difficulty setting: ");
		if (difficultyMixedButton.isSelected()) {
			System.out.println("Mixed");
			difficultyIsEasy = false;
			difficultyIsMixed = true;
		} else if (difficultyEasyButton.isSelected()) {
			System.out.println("Easy");
			difficultyIsEasy = true;
			difficultyIsMixed = false;
		} else if (difficultyHardButton.isSelected()) {
			System.out.println("Hard");
			difficultyIsEasy = false;
			difficultyIsMixed = false;
		}

		System.out.print("Chosen question type: ");
		if (typeMixedButton.isSelected()) {
			System.out.println("Mixed");
			typeIsTf = false;
			typeIsMixed = true;
		} else if (typeTfButton.isSelected()) {
			System.out.println("True/False");
			typeIsTf = true;
			typeIsMixed = false;
		} else if (typeMcButton.isSelected()) {
			System.out.println("Multiple Choice");
			typeIsTf = false;
			typeIsMixed = false;
		}

		System.out.print("Chosen game length: ");
		if (shortLengthButton.isSelected()) {
			System.out.println(gameLength = SHORT_LENGTH);
		} else if (mediumLengthButton.isSelected()) {
			System.out.println(gameLength = MEDIUM_LENGTH);
		} else if (longLengthButton.isSelected()) {
			System.out.println(gameLength = LONG_LENGTH);
		}

		System.out.print("Timer is: ");
		if (timerToggle.isSelected()) {
			timerSetting = true;
			System.out.println("Enabled");
		} else {
			timerSetting = false;
			System.out.println("Disabled");
		}
		loadView(event);
	}
}
