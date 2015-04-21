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
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Toggle;

/**
 * FXML Controller class
 *
 * @author AMS09
 * @version 1.0
 */
public class GameSetUpController extends Trivia implements Initializable {

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

	static boolean makkelijkHolder;
	static boolean waarvalsHolder;
	static boolean meerkeuzeHolder;
	static boolean timerHolder;

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
	private void openMain() {
		loadView("MainMenu");
	}

	@FXML
	private void startGame() {
		loadView("Question");

		if (moeilijkheidNormaal.isSelected()) {
			System.out.println("normaal");
			makkelijkHolder = true;
		} else if (moeilijkheidMoeilijk.isSelected()) {
			System.out.println("moeilijk");
			makkelijkHolder = false;
		}

		if (waarvalsvragenToggleAan.isSelected()) {
			System.out.println("waar");
		}
		if (meerkeuzevragenToggleAan.isSelected()) {
			System.out.println("meerkeuze");
		}
		if (timerToggleAan.isSelected()) {
			System.out.println("timer");
		}
	}
}
