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
import trivia.connectivity.DbManager;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
	Button previousQuestionButton;

	@FXML
	Button nextQuestionButton;

	@FXML
	Button mainMenu;

	@FXML
	Button nameWindow;

	// Object to call connection
	private final DbManager dbm = new DbManager();
	// Object to store query results
	private ResultSet result = null;
	private String queryText = "";

	// Length of time for each question
	private static final Integer STARTTIME = 30;
	private Timeline timeline;
	private final IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);

	private int questionNumber = 1;

	private ArrayList<Integer> remainingTimerDuration = new ArrayList<Integer>();

	private List<ToggleButton> answerButtons;
	private List<Hyperlink> answerLabels;
	private final String[][] loadedStrings = new String[GameSetUpController.gameLength][7];
	final String[][] chosenAnswers = new String[GameSetUpController.gameLength][2];
	final String[] correctAnswers = new String[GameSetUpController.gameLength];

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
		answerLabels = Arrays.asList(labelA, labelB, labelC, labelD);
		for (ToggleButton a : answerButtons) {
			a.setOnAction(this::handleAnswerSelection);
		}
		for (Hyperlink a : answerLabels) {
			a.setOnAction(this::handleAnswerSelection);
		}
		// Use Str empty as: user did not pick an answer, prevents empty ResultSet errors
		for (int i = 0; i < chosenAnswers.length; i++) {
			chosenAnswers[i][0] = "";
			chosenAnswers[i][1] = "E";
		}

		// Set actions for all buttons
		nextQuestionButton.setOnAction((event) -> this.nextQuestion(event, true));
		previousQuestionButton.setOnAction((event) -> this.nextQuestion(event, false));
		mainMenu.setOnAction((event) -> {
			timeline.stop();
			this.stopQuiz(event);
		});

		// Update question progress label, start at 1 and therefore disable previousQuestionButton
		questionProgress.setText(questionNumber + " / " + GameSetUpController.gameLength);
		previousQuestionButton.setDisable(true);

		// Sets the question and answers
		questionProgress.setText(questionNumber + " / " + GameSetUpController.gameLength);
		setLabels(questionNumber - 1);
	}

	/**
	 * This method controls the timer.
	 */
	@FXML
	private void autoPlay() {
		int originalRunTester = remainingTimerDuration.get(questionNumber - 1);

		if (originalRunTester <= 1) { // If time's up, player may not change answers.
			System.out.print("Timer empty");
			for (ToggleButton tb : answerButtons) { // Disables the answer buttons.
				tb.setDisable(true);
			}
			for (Hyperlink h : answerLabels) { // Disables the answer labels.
				h.setDisable(true);
			}
			System.out.println("Timer ran out. Editing chosen answers disabled.");
		}

		// Bind the timerLabel text property to the timeSeconds property
		timer.textProperty().bind(timeSeconds.divide(100).asString());
		progressBar.progressProperty().bind(
				timeSeconds.divide(remainingTimerDuration.get(questionNumber - 1)
						* 100.0).subtract(1).multiply(-1));

		// Countdown from STARTTIME to zero
		if (timeline != null) {
			timeline.stop();
		}
		timeSeconds.set((remainingTimerDuration.get(questionNumber - 1) + 1) * 100);
		timeline = new Timeline();
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.seconds(remainingTimerDuration.get(
										questionNumber - 1) + 1), new KeyValue(timeSeconds, 0)));
		timeline.playFromStart();

		// When completed counting down, execute block contents.
		timeline.setOnFinished((ActionEvent event) -> {
			if (originalRunTester == STARTTIME) {
				nextQuestionButton.fire(); // Simulate button action, auto-continue to next Q.
			} else { // If time's up, player may not change answers.
				for (ToggleButton a : answerButtons) { // Disables the answer buttons.
					a.setDisable(true);
				}
				for (Hyperlink a : answerLabels) { // Disables the answer labels.
					a.setDisable(true);
				}
				System.out.println("Timer ran out. Editing chosen answers disabled.");
			}
		});
	}

	/**
	 * This method remembers which answers were chosen by player.
	 *
	 * @param event
	 */
	private void handleAnswerSelection(ActionEvent event) {
		String buttonName = ((Control) event.getSource()).getId();
		System.out.println("User selected answer via Object: "
				+ buttonName + ".");
		for (Hyperlink h : answerLabels) { // For each answer label.
			h.setVisited(false);
			h.setFocusTraversable(false);
		}
		switch (buttonName) { // This method & switch case are pretty self-explanatory.
			case "buttonA":
			case "labelA":
				buttonA.setSelected(true);
				chosenAnswers[questionNumber - 1][0] = labelA.getText();
				chosenAnswers[questionNumber - 1][1] = "A";
				break;
			case "buttonB":
			case "labelB":
				buttonB.setSelected(true);
				chosenAnswers[questionNumber - 1][0] = labelB.getText();
				chosenAnswers[questionNumber - 1][1] = "B";
				break;
			case "buttonC":
			case "labelC":
				buttonC.setSelected(true);
				chosenAnswers[questionNumber - 1][0] = labelC.getText();
				chosenAnswers[questionNumber - 1][1] = "C";
				break;
			case "buttonD":
			case "labelD":
				buttonD.setSelected(true);
				chosenAnswers[questionNumber - 1][0] = labelD.getText();
				chosenAnswers[questionNumber - 1][1] = "D";
				break;
			default:
				chosenAnswers[questionNumber - 1][0] = ""; // Empty Str = no answer chosen.
				chosenAnswers[questionNumber - 1][1] = "E";
				break;
		}
	}

	/**
	 * This method loads the game settings for the new game.
	 */
	public void loadGameSettings() {
		int questionsIndex = -1;
		dbm.openConnection();
		try {
			result = null;
			queryText = "SELECT q.Question, r.RightAnswer, w.WrongAnswer, w2.WrongAnswer, "
					+ "w3.WrongAnswer, q.Difficulty, q.QuestionId FROM question q "
					+ "inner join rightanswer r ON q.QuestionId = r.QuestionId "
					+ "inner join wronganswer w ON w.QuestionId = q.QuestionId "
					+ "inner join wronganswer w2 ON w2.QuestionId = q.QuestionId "
					+ "inner join wronganswer w3 ON w3.QuestionId = q.QuestionId "
					+ "WHERE w.WrongAnswerId = 1 AND w2.WrongAnswerId = 2 AND "
					+ "w3.WrongAnswerId = 3 ORDER BY RAND() LIMIT "
					+ GameSetUpController.gameLength + ";";
			result = dbm.doQuery(queryText); // Get everything above.
			while (result.next()) { // Save everything.
				System.out.print("Loaded details for {"
						+ ++questionsIndex + "}"); // Loads next question.
				for (int i = 0; i <= 6; i++) {
					loadedStrings[questionsIndex][i] = result.getString(i + 1);
					System.out.print("[" + i + "]");
					if (i == 6) {
						System.out.println("");
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			dbm.closeConnection();
			result = null;
			queryText = "";
		}
		// Print loaded questions.
		for (int i = 0; i < GameSetUpController.gameLength; i++) {
			System.out.print("\nLoaded data #" + (i + 1) + ": ");
			for (int j = 0; j <= 6; j++) { // Print stored data for each question.
				if (j > 0) {
					System.out.print(" | ");
				}
				System.out.print(loadedStrings[i][j]);
			}
		}
		// Store all correct answers.
		for (int i = 0; i < GameSetUpController.gameLength; i++) {
			correctAnswers[i] = loadedStrings[i][1];
		}
		System.out.print("\nStored correct answers for all questions.");
		// Randomize answers order for every question.
		for (int i = 0; i < loadedStrings.length; i++) {
			String[] temp = new String[4];
			System.out.print("\nQuestion " + (i + 1) + " prior to randomization: ");
			for (int j = 1; j < 5; j++) {
				if (j > 1) {
					System.out.print(" | ");
				}
				System.out.print(loadedStrings[i][j]);
				temp[j - 1] = loadedStrings[i][j];
			}
			Collections.shuffle(Arrays.asList(temp));
			System.out.print("\nQuestion " + (i + 1) + " after randomization: ");
			for (int j = 0; j < 4; j++) {
				loadedStrings[i][j + 1] = temp[j];
				if (j > 0) {
					System.out.print(" | ");
				}
				System.out.print(loadedStrings[i][j + 1]);
			}
		}

		if (GameSetUpController.timerHolder) { // Timer enabled
			progressBar.setVisible(true);
			remainingTimerDuration = new ArrayList<Integer>();
			for (int i = 0; i < GameSetUpController.gameLength; i++) {
				remainingTimerDuration.add(i, timeSeconds.getValue());
				System.out.println("Time remaining for question " + (i + 1) + ": "
						+ remainingTimerDuration.get(i).toString());
			}
			autoPlay();
		} else { // Timer disabled
			timer.setText("");
			progressBar.setVisible(false);
		}
	}

	/**
	 * This method handles loading the previous and next question. If the
	 * boolean is true, the following question will be loaded, otherwise the
	 * previous. If there is no following question, the ScoreOverview is
	 * triggered.
	 *
	 * @param event
	 * @param nextQuestion
	 */
	private void nextQuestion(ActionEvent event, Boolean nextQuestion) {
		String temp = nextQuestion ? "next" : "previous";
		System.out.println("User hit " + temp + "QuestionButton.");
		remainingTimerDuration.set(questionNumber - 1, parseInt(timer.getText()));
		for (int i = 0; i < GameSetUpController.gameLength; i++) {
			System.out.println("Time remaining for question " + (i + 1) + ": "
					+ remainingTimerDuration.get(i).toString());
		}

		// Makes sure players can pick or change answers
		for (ToggleButton tb : answerButtons) {
			tb.setDisable(false);
		}
		for (Hyperlink h : answerLabels) {
			h.setDisable(false);
		}

		if (nextQuestion) { // nextQuestionButton is pressed
			if (questionNumber + 1 > GameSetUpController.gameLength) { // There is no next Q.
				timeline.stop();
				for (int i = 0; i < chosenAnswers.length; i++) { // Print chosen answers.
					System.out.println("Answer to question " + i + ": " + chosenAnswers[i][1]);
				}
				loadView(event); // --> go to ScoreOverview
			} else {
				questionNumber++;
				previousQuestionButton.setDisable(false);
				if (questionNumber + 1 > GameSetUpController.gameLength) {
					nextQuestionButton.setText("Bekijk score");
				}
			}
		} else { // previousQuestionButton is pressed
			if (questionNumber <= 2) { // If curr question is 1 or 2, disable previousQB
				previousQuestionButton.setDisable(true);
			}
			questionNumber--;
			nextQuestionButton.setText("Volgende"); // In case we're near the last
		}
		// Set question progress (e.g. 1/10), set question and answers
		questionProgress.setText(questionNumber + " / " + GameSetUpController.gameLength);
		setLabels(questionNumber - 1);
		setChosenAnswers(questionNumber - 1);

		if (GameSetUpController.timerHolder) {
			autoPlay();
		}
	}

	/**
	 * Selects the answer player chose earlier for current question, if any.
	 *
	 * @param i
	 */
	private void setChosenAnswers(int i) {
		switch (chosenAnswers[i][1]) {
			case "A":
				buttonA.setSelected(true);
				break;
			case "B":
				buttonB.setSelected(true);
				break;
			case "C":
				buttonC.setSelected(true);
				break;
			case "D":
				buttonD.setSelected(true);
				break;
			default:
				for (ToggleButton a : answerButtons) {
					a.setSelected(false);
				}
				// Use char 'E' as: player did not pick an answer
				// Prevents empty result set errors
				chosenAnswers[i][1] = "E";
				break;
		}
	}

	/**
	 * This method updates the question and answer labels.
	 *
	 * @param storedDataRow
	 */
	private void setLabels(int storedDataRow) {
		int storedDataNumber = 0;
		question.setText(loadedStrings[storedDataRow][storedDataNumber]);
		for (Hyperlink h : answerLabels) {
			storedDataNumber++;
			h.setText(loadedStrings[storedDataRow][storedDataNumber]);
		}
	}

	@FXML
	private void stopQuiz(ActionEvent event) {
		if (alertDialog(AlertType.CONFIRMATION, "Stop quiz", "Weet u zeker dat u"
				+ " de quiz wilt stoppen?", "De antwoorden worden niet opgeslagen."
				+ "\nDit brengt u terug naar het hoofdmenu.", StageStyle.UNDECORATED)) {
			// TO DO: WIPE SAVED ANSWERS
			timeline.stop();
			loadView(event);
		} else { // Player cancels the method, if timer's enabled, continue countdown
			if (GameSetUpController.timerHolder) {
				timeline.play();
			}
		}
	}
}
