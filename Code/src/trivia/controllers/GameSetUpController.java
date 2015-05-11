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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Toggle;
import trivia.Trivia;

/**
 * FXML Controller class
 *
 * @author Team Silent Coders
 * @version 1.0
 */
public class GameSetUpController extends Trivia implements Initializable {

	@FXML
	Button mainMenu;

	@FXML
	Button startGame;

	@FXML
	Toggle moeilijkheidNormaal;

	@FXML
	Toggle moeilijkheidMoeilijk;

	@FXML
	Toggle waarvalsvragenToggleAan;

	@FXML
	Toggle meerkeuzevragenToggleAan;

	@FXML
	Toggle timerToggleAan;

	static int gameLength = 10;
	static boolean makkelijkHolder;
	static boolean waarvalsHolder;
	static boolean meerkeuzeHolder;
	static boolean timerHolder = true;

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		mainMenu.setOnAction(this::handleButtonAction);
		startGame.setOnAction(this::startGame);
	}

	@Override
	public void handleButtonAction(ActionEvent event) {
		System.out.println("GameSetUpController check: "
				+ ((Control) event.getSource()).getId());
		loadView(event);
	}

	@FXML
	private void startGame(ActionEvent event) {
//		loadView("Question");

		if (moeilijkheidNormaal.isSelected()) {
			System.out.println("normaal");
			makkelijkHolder = true;
		} else if (moeilijkheidMoeilijk.isSelected()) {
			System.out.println("moeilijk");
			makkelijkHolder = false;
		}

		if (waarvalsvragenToggleAan.isSelected()) {
			System.out.println("Waar");
		} else {
			System.out.println("Onwaar");
		}
		if (meerkeuzevragenToggleAan.isSelected()) {
			System.out.println("Meerkeuze");
		} else {
			System.out.println("True/false");
		}
		if (timerToggleAan.isSelected()) {
			timerHolder = true;
			System.out.println("Set timer");
		} else {
			timerHolder = false;
			System.out.println("Disable timer");
		}
		handleButtonAction(event);
	}
}
