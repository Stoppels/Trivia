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
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import trivia.Trivia;

/**
 * FXML Controller class
 *
 * @author Team Silent Coders
 * @version 1.0
 */
public class DefaultSettingsController extends Trivia implements Initializable {

	@FXML
	public ToggleButton difficultyHard;

	@FXML
	public ToggleButton typeTf;

	@FXML
	public CheckBox lengthModifiability;

	@FXML
	public ToggleGroup typeGroup;

	@FXML
	private Button mainMenu;

	@FXML
	public ToggleGroup lengthGroup;

	@FXML
	public ToggleButton shortLength;

	@FXML
	public ToggleGroup difficultyGroup;

	@FXML
	private Button saveSettings;

	@FXML
	public ToggleGroup timerGroup;

	@FXML
	public CheckBox difficultyModifiability;

	@FXML
	public ToggleButton typeMixed;

	@FXML
	public CheckBox timerModifiability;

	@FXML
	public ToggleButton mediumLength;

	@FXML
	public ToggleButton difficultyEasy;

	@FXML
	public CheckBox typeModifiability;

	@FXML
	public ToggleButton longLength;

	@FXML
	public ToggleButton timerToggle;

	@FXML
	public ToggleButton difficultyMixed;

	@FXML
	public ToggleButton typeMc;

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		mainMenu.setOnAction(this::loadView);
		saveSettings.setOnAction(this::saveSettings);
	}

	private void saveSettings(Event event) {

	}
}
