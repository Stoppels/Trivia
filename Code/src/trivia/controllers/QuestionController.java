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

import trivia.connectivity.DbManager;
import trivia.connectivity.QueryManager;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import trivia.Trivia;
import static trivia.Trivia.alertDialog;

/**
 * FXML Controller class
 *
 * @author Team Silent Coders
 * @version 1.0
 */
public class QuestionController extends Trivia implements Initializable {

	// Object to call connection
	DbManager dbm = new DbManager();
	// Object to call QueryManager class
	QueryManager qm = new QueryManager(dbm);

	private static final Integer STARTTIME = 30;
	private Timeline timeline;
	private final IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);

//	Random rand = new Random();
//	int questionId = rand.nextInt(4) + 1;
/*
	 SELECT questionId
	 FROM question
	 WHERE questionId = 1
	 AND Loaded = false
	 ORDER BY RAND()
	 LIMIT 1;

	 UPDATE question
	 SET Loaded = true
	 WHERE questionId = 1;
	 */
	@FXML
	ToggleGroup toggleGroup;

	@FXML
	ToggleButton buttonA;

	@FXML
	ToggleButton buttonB;

	@FXML
	ToggleButton buttonC;

	@FXML
	ToggleButton buttonD;

	@FXML
	Label questionProgress;

	@FXML
	Label timer;

	@FXML
	ProgressBar progressBar;

	@FXML
	Label question;

	@FXML
	Hyperlink labelA;

	@FXML
	Hyperlink labelB;

	@FXML
	Hyperlink labelC;

	@FXML
	Hyperlink labelD;

	@FXML
	Button previousQuestion;

	@FXML
	Button nextQuestion;

	@FXML
	Button mainMenu;

	@FXML
	Button nameWindow;

	private List<ToggleButton> answerButtons;
	private final char[] chosenAnswers = new char[GameSetUpController.gameLength];
	private final int wrongAnswerId1 = 1, wrongAnswerId2 = 2, wrongAnswerId3 = 3;

	public static int[] selectedQuestions;
	private int questionId = 1;
	private int questionFetcher = 0;
	private int questionNumber = 1;

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */
	@Override

	public void initialize(URL url, ResourceBundle rb) {

		loadGameSettings();

		// Perform action for all items in lists
		answerButtons = Arrays.asList(buttonA, buttonB, buttonC, buttonD);
		for (ToggleButton a : answerButtons) {
			a.setOnAction(this::handleAnswerSelection);
		}
		List<Hyperlink> answerLabels = Arrays.asList(labelA, labelB, labelC, labelD);
		for (Hyperlink a : answerLabels) {
			a.setOnAction(this::handleAnswerSelection);
		}

		// Set actions for all buttons
		nextQuestion.setOnAction((event) -> this.nextQuestion(event, true));
		previousQuestion.setOnAction((event) -> this.nextQuestion(event, false));
		mainMenu.setOnAction((event) -> {
			timeline.stop();
			this.stopQuiz(event);
		});

		// Update question progress label, start at 1
		questionProgress.setText(questionNumber + " / " + GameSetUpController.gameLength);

		// Sets the question
		dbm.openConnection();
		question.setText(qm.setQuestion(questionId));

		//sets the wrong answer
		qm.setWrongAnswer(wrongAnswerId1, labelA, questionId);
		qm.setWrongAnswer(wrongAnswerId2, labelB, questionId);
		qm.setWrongAnswer(wrongAnswerId3, labelC, questionId);
		qm.setRightAnswer(labelD, questionId);

		// TODO
		// if current answer > 10, scoreCheck()
		// if int current question > 1 AND previousQuestion is disabled, enable previousQuestion
		// else if int current question < 2, disable previousQuestion
	}

	/**
	 *
	 */
	@FXML
	public void autoPlay() {
		// Bind the timerLabel text property to the timeSeconds property
		timer.textProperty().bind(timeSeconds.divide(100).asString());
		progressBar.progressProperty().bind(
				timeSeconds.divide(STARTTIME * 100.0).subtract(1).multiply(-1));
		timer.setTextFill(Color.WHITE);

		// Countdown from STARTTIME to zero
		if (timeline != null) {
			timeline.stop();
		}
		timeSeconds.set((STARTTIME + 1) * 100);
		timeline = new Timeline();
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.seconds(STARTTIME + 1),
						new KeyValue(timeSeconds, 0)));
		timeline.playFromStart();

		// When completed counting down, execute method openHoofdmenu().
		timeline.setOnFinished((ActionEvent event) -> {
			// Simulate button action
			nextQuestion.fire();
		});
	}

	private void handleAnswerSelection(ActionEvent event) {
		String buttonName = ((Control) event.getSource()).getId();
		System.out.println("QuestionController answer: "
				+ buttonName);
		switch (buttonName) {
			case "buttonA":
			case "labelA":
				buttonA.setSelected(true);
				chosenAnswers[questionNumber - 1] = 'A';
				break;
			case "buttonB":
			case "labelB":
				buttonB.setSelected(true);
				chosenAnswers[questionNumber - 1] = 'B';
				break;
			case "buttonC":
			case "labelC":
				buttonC.setSelected(true);
				chosenAnswers[questionNumber - 1] = 'C';
				break;
			case "buttonD":
			case "labelD":
				buttonD.setSelected(true);
				chosenAnswers[questionNumber - 1] = 'D';
				break;
		}
	}

	/**
	 * This method loads the game settings for the new game.
	 */
	@FXML
	public void loadGameSettings() {
		selectedQuestions = qm.selectQuestions(GameSetUpController.gameLength);
		questionId = selectedQuestions[questionFetcher];

		if (GameSetUpController.timerHolder) {
			progressBar.setVisible(true);
			autoPlay();
		} else {
			timer.setText("");
			progressBar.setVisible(false);
		}
	}

	@FXML
	private void nextQuestion(ActionEvent event, Boolean nextQuestion) {
		if (nextQuestion) {
			if (questionNumber + 1 > GameSetUpController.gameLength) {
				mainMenu.fire();
			} else {
				questionFetcher++;
				questionId = selectedQuestions[questionFetcher];
				questionNumber++;
			}
		} else {
			if (questionNumber <= 1) {
				alertDialog(AlertType.ERROR, "Vorige vraag", "",
						"Dit is de eerste vraag, er is geen vorige vraag.", StageStyle.UNDECORATED);
				return;
			} else {
				questionFetcher--;
				questionId = selectedQuestions[questionFetcher];
				questionNumber++;
			}
		}
		questionProgress.setText(questionNumber + " / " + GameSetUpController.gameLength);
		question.setText(qm.setQuestion(questionId));
		qm.setWrongAnswer(wrongAnswerId1, labelA, questionId);
		qm.setWrongAnswer(wrongAnswerId2, labelB, questionId);
		qm.setWrongAnswer(wrongAnswerId3, labelC, questionId);
		qm.setRightAnswer(labelD, questionId);

		//if () {
		//} else {
		for (ToggleButton a : answerButtons) {
			a.setSelected(false);
		}
		if (GameSetUpController.timerHolder) {
			autoPlay();
		}
		//}
		//saveAnswer();
		//goto current + 1
	}

	@FXML
	private void stopQuiz(ActionEvent event) {
		if (alertDialog(AlertType.CONFIRMATION, "Stop quiz", "Weet u zeker dat u"
				+ " de quiz wilt stoppen?", "De antwoorden worden niet opgeslagen."
				+ "\nDit brengt u terug naar het hoofdmenu.", StageStyle.UNDECORATED)) {
			// TO DO: WIPE SAVED ANSWERS
			int i = 1;
			for (char c : chosenAnswers) {
				System.out.println("Answer to question " + i + ": " + c);
				i++;
			}
			timeline.stop();
			loadView(event);
		}
	}
}
