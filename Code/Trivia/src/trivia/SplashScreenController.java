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
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author AMS09
 * @version 1.0
 */
public class SplashScreenController extends Trivia implements Initializable {

	@FXML
	private Label timerLabel;
	private static final Integer STARTTIME = 7;
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
	}

	@FXML
	public void autoPlay() {
		// Bind the timerLabel text property to the timeSeconds property
		timerLabel.textProperty().bind(timeSeconds.asString());
		timerLabel.setTextFill(Color.RED);

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
		timeline.setOnFinished((ActionEvent a) -> {
			openMain();
		});
	}

	@FXML
	private void openMain() {
		timeline.stop();
		loadView("MainMenu");
	}

	@FXML
	private void openAdmin() {
		timeline.stop();
		loadView("AdminMenu");

	}

}
