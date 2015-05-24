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

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import trivia.Trivia;
import trivia.connectivity.DbManager;

/**
 * FXML Controller class
 *
 * @author Team Silent Coders
 * @version 1.0
 */
public class DefaultSettingsController extends Trivia implements Initializable {

	@FXML
	private ToggleGroup difficultyGroup;

	@FXML
	private ToggleGroup typeGroup;

	@FXML
	private ToggleGroup lengthGroup;

	@FXML
	private ToggleGroup timerGroup;

	@FXML
	private ToggleButton difficultyMixedButton;

	@FXML
	private ToggleButton difficultyHardButton;

	@FXML
	private ToggleButton difficultyEasyButton;

	@FXML
	private ToggleButton typeMixedButton;

	@FXML
	private ToggleButton typeTfButton;

	@FXML
	private ToggleButton typeMcButton;

	@FXML
	private ToggleButton shortLengthButton;

	@FXML
	private ToggleButton mediumLengthButton;

	@FXML
	private ToggleButton longLengthButton;

	@FXML
	private ToggleButton timerToggle;

	@FXML
	private ToggleButton timerToggleNo;

	@FXML
	private CheckBox difficultyModifiability;

	@FXML
	private CheckBox lengthModifiability;

	@FXML
	private CheckBox typeModifiability;

	@FXML
	private CheckBox timerModifiability;

	@FXML
	private Button adminMenu;

	@FXML
	private Button saveSettings;

	// Object to call connection
	private final DbManager dbm = new DbManager();
	static boolean difficultyIsMixed = true, difficultyIsEasy = true;
	private List<ToggleGroup> groupsList;

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		adminMenu.setOnAction(this::loadView);
		saveSettings.setOnAction(this::setSettings);
		getSettings();

		// Can't unselect buttons: one is selected at all times.
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
	}

	/**
	 * This precious makes sure the last saved settings are loaded.
	 */
	private void getSettings() {
		System.out.println("Loading default settings.");

		loadSettings();

		System.out.println("WOWOWOWOW: " + prefs.get(difficultyHolder, "wut"));

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
					shortLengthButton.setText(String.valueOf(Trivia.SHORT_LENGTH));
					break;
				case "mediumLength":
					System.out.println("Default length setting: medium.");
					mediumLengthButton.setSelected(true);
					mediumLengthButton.setText(String.valueOf(Trivia.MEDIUM_LENGTH));
					break;
				case "longLength":
					System.out.println("Default length setting: long.");
					longLengthButton.setSelected(true);
					longLengthButton.setText(String.valueOf(Trivia.LONG_LENGTH));
					break;
				default: // Nothing is selected.
					System.err.println("Something is wrong with the (default) var prefs.");
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
					difficultyModifiability.setSelected(b);
					break;
				case 2:
					System.out.println("Default length modifiability: " + (b ? "on." : "off."));
					lengthModifiability.setSelected(b);
					break;
				case 3:
					System.out.println("Default type modifiability: " + (b ? "on." : "off."));
					typeModifiability.setSelected(b);
					break;
				case 4:
					System.out.println("Default timer modifiability: " + (b ? "on." : "off."));
					timerModifiability.setSelected(b);
					break;
				default:
					System.err.println("Something is wrong with the (default) bool prefs.");
					break;
			}
			i++;
		}
	}

	private void setSettings(ActionEvent event) {
		System.out.println("Setting following default settings.");

		System.out.print("New default difficulty setting: ");
		prefs.remove(difficultyHolder);
		if (difficultyMixedButton.isSelected()) {
			prefs.put(difficultyHolder, "difficultyMixed");
			System.out.println("Mixed.");
		} else if (difficultyEasyButton.isSelected()) {
			prefs.put(difficultyHolder, "difficultyEasy");
			System.out.println("Easy.");
		} else if (difficultyHardButton.isSelected()) {
			prefs.put(difficultyHolder, "difficultyHard");
			System.out.println("Hard.");
		} else {
			System.out.println("\nSomething went wrong when setting a new difficulty.");
			return;
		}
		System.out.println("WOWOWOWOW: " + prefs.get(difficultyHolder, "wut"));

		System.out.print("New default question type: ");
		prefs.remove(typeHolder);
		if (typeMixedButton.isSelected()) {
			prefs.put(typeHolder, "typeMixed");
			System.out.println("Mixed.");
		} else if (typeTfButton.isSelected()) {
			prefs.put(typeHolder, "typeTf");
			System.out.println("short length.");
		} else if (typeMcButton.isSelected()) {
			prefs.put(typeHolder, "typeMc");
			System.out.println("short length.");
		} else {
			System.out.println("\nSomething went wrong when setting a new type.");
			return;
		}

		System.out.print("New default game length: ");
		prefs.remove(lengthHolder);
		if (shortLengthButton.isSelected()) {
			prefs.put(lengthHolder, "shortLength");
			System.out.println("short length.");
		} else if (mediumLengthButton.isSelected()) {
			prefs.put(lengthHolder, "mediumLength");
			System.out.println("medium length.");
		} else if (longLengthButton.isSelected()) {
			prefs.put(lengthHolder, "longLength");
			System.out.println("long length.");
		} else {
			System.out.println("\nSomething went wrong when setting a new length.");
			return;
		}

		prefs.putBoolean(timerHolder, timerToggle.isSelected());
		System.out.println("New default timer setting: "
				+ (timerToggle.isSelected() ? "on." : "off."));
		prefs.putBoolean(difficultyModifier, difficultyModifiability.isSelected());
		System.out.println("New default difficulty modifiability: "
				+ (difficultyModifiability.isSelected() ? "allowed." : "prohibited."));
		prefs.putBoolean(lengthModifier, lengthModifiability.isSelected());
		System.out.println("New default length modifiability: "
				+ (lengthModifiability.isSelected() ? "allowed." : "prohibited."));
		prefs.putBoolean(typeModifier, typeModifiability.isSelected());
		System.out.println("New default type modifiability: "
				+ (typeModifiability.isSelected() ? "allowed." : "prohibited."));
		prefs.putBoolean(timerModifier, timerModifiability.isSelected());
		System.out.println("New default timer modifiability: "
				+ (timerModifiability.isSelected() ? "allowed." : "prohibited."));

		try {
			prefs.sync();
		} catch (BackingStoreException e) {
			System.err.println("Error! Unable to sync after putting prefs: "
					+ e.getLocalizedMessage());
		}
		loadView(event);
	}
}
