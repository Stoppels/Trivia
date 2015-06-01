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
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import static trivia.AppConfig.APPLICATION_COPYRIGHT;
import trivia.Trivia;

/**
 * FXML Controller class
 *
 * @author Team Silent Coders
 * @version 1.0
 */
public class SplashScreenController extends Trivia implements Initializable {

	@FXML
	private Button mainMenu;

	@FXML
	private Button adminMenu;

	@FXML
	private Label timerLabel;

	@FXML
	private Label copyrightLabel;

	private static final Integer STARTTIME = 6;
	private Timeline timeline;
	private final IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		autoPlay();
		mainMenu.setOnAction((event) -> {
			timeline.stop();
			this.loadView(event);
		});
		adminMenu.setOnAction((event) -> {
			timeline.stop();
			this.loadView(event);
		});
		copyrightLabel.setText(APPLICATION_COPYRIGHT);
	}

	@FXML
	public void autoPlay() {
		// Bind the timerLabel text property to the timeSeconds property
		timerLabel.textProperty().bind(timeSeconds.asString());

		// Countdown from STARTTIME to zero
		if (timeline != null) {
			timeline.stop();
		}
		timeSeconds.set(STARTTIME);
		timeline = new Timeline();
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.seconds(STARTTIME + 1),
						new KeyValue(timeSeconds, 0)));
		timeline.playFromStart();

		// When completed counting down, execute method openHoofdmenu().
		timeline.setOnFinished((ActionEvent event) -> {
			// Simulate button action
			mainMenu.fire();
		});
	}
}
