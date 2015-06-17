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
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import static trivia.AppConfig.LONG_LENGTH;
import static trivia.AppConfig.MEDIUM_LENGTH;
import static trivia.AppConfig.SHORT_LENGTH;
import trivia.Trivia;
import static trivia.Trivia.alertDialog;

/**
 * This class handles the quiz-section (question view) of the application.
 *
 * @author Team Silent Coders
 * @version 1.0
 */
public class QuestionController extends Trivia implements Initializable {

	@FXML
	private GridPane gridPane;

	@FXML
	private ToggleButton buttonA;

	@FXML
	private ToggleButton buttonB;

	@FXML
	private ToggleButton buttonC;

	@FXML
	private ToggleButton buttonD;

	@FXML
	private Label questionProgress;

	@FXML
	private Label timer;

	@FXML
	private ProgressBar progressBar;

	@FXML
	private Label questionLabel;

	@FXML
	private Hyperlink labelA;

	@FXML
	private Hyperlink labelB;

	@FXML
	private Hyperlink labelC;

	@FXML
	private Hyperlink labelD;

	@FXML
	private Label checkMarkA;

	@FXML
	private Label checkMarkB;

	@FXML
	private Label checkMarkC;

	@FXML
	private Label checkMarkD;

	@FXML
	private Button previousQuestionButton;

	@FXML
	private Button nextQuestionButton;

	@FXML
	private Button mainMenu;

	@FXML
	private Button viewScore;

	// Object to call connection
	private final DbManager dbm = new DbManager();

	private Timeline timeline;
	private final IntegerProperty timeSeconds = new SimpleIntegerProperty(setTime);

	private int questionNumber = 1;

	public static ArrayList<Integer> remainingTimerDuration = new ArrayList<Integer>();
	public static List<Integer> correctlyAnsweredQuestions = new ArrayList<Integer>();
	public static Integer correctAnswersTotal = 0;
	public static Integer skippedQuestionsTotal = 0;

	private List<ToggleButton> answerButtons;
	private List<Hyperlink> answerLabels;
	public static String[][] loadedStrings = new String[gameLength][8];
	public String[][] chosenAnswers = new String[gameLength][2];
	public String[][] correctAnswers = new String[gameLength][2];
	private Boolean gameIsFinished = false;
	private Boolean reviewedAnswers = false;

	private final Color BLACK = Color.web("#000"); // Black
	private final Color GREEN = Color.web("#008000"); // Green
	private final Color LIGHTER_GREEN = Color.web("#00F72C"); // Free Speech Green
	private final Color RED = Color.web("CE2029"); // Fire Engine Red
	private final String css = "/resources/stylesheets/ReviewStyles.css";

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */
	@Override

	public void initialize(URL url, ResourceBundle rb) {
		loadGameSettings();

		// Perform action for all items in lists.
		answerButtons = Arrays.asList(buttonA, buttonB, buttonC, buttonD);
		answerLabels = Arrays.asList(labelA, labelB, labelC, labelD);
		for (ToggleButton a : answerButtons) {
			a.setOnAction(this::handleAnswerSelection);
		}
		for (Hyperlink a : answerLabels) {
			a.setOnAction(this::handleAnswerSelection);
		}
		// Use Str empty as: player did not pick an answer, prevents empty ResultSet errors.
		for (int i = 0; i < chosenAnswers.length; i++) {
			chosenAnswers[i][0] = "";
			chosenAnswers[i][1] = "E";
		}

		// Set actions for all buttons.
		nextQuestionButton.setOnAction((event) -> this.nextQuestion(event, true));
		previousQuestionButton.setOnAction((event) -> this.nextQuestion(event, false));
		mainMenu.setOnAction(this::stopQuiz);
		viewScore.setOnAction((event) -> {
			if (alertDialog(AlertType.CONFIRMATION, "Score bekijken", null,
					"Als u doorgaat ziet u uw score en kunt u de vragen "
					+ "niet meer bekijken.")) {
				this.loadView(event);
			}
		});

		// Update question progress label, start at 1 and therefore disable previousQuestionButton.
		questionProgress.setText(questionNumber + " / " + gameLength);
		previousQuestionButton.setDisable(true);

		// Sets the question and answers.
		questionProgress.setText(questionNumber + " / " + gameLength);
		setLabels(questionNumber - 1);
	}

	/**
	 * This method controls the timer.
	 */
	@FXML
	private void autoPlay() {
		int originalRunTester = remainingTimerDuration.get(questionNumber - 1);

		if (originalRunTester <= 1) { // If time's up, player may not change answers.
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

		// Countdown from setTime to zero
		if (timeline != null) {
			timeline.stop();
		}
		timeSeconds.set((remainingTimerDuration.get(questionNumber - 1) + 1) * 100);
		if (!gameIsFinished) {
			timeline = new Timeline();
			timeline.getKeyFrames().add(
					new KeyFrame(Duration.seconds(
									remainingTimerDuration.get(questionNumber - 1) + 1),
							new KeyValue(timeSeconds, 0)));
			timeline.playFromStart();
		}

		// When completed counting down, execute block contents.
		timeline.setOnFinished((ActionEvent event) -> {
			if (originalRunTester == setTime) {
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

	private void clearStrings() {
		loadedStrings = new String[gameLength][8];
		chosenAnswers = new String[gameLength][2];
		correctAnswers = new String[gameLength][2];
	}

	/**
	 * Selects the answer player chose earlier for current question, if any.
	 *
	 * @param i
	 */
	private void getChosenAnswers(int i) {
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
	 * This method saves which answers were chosen by player.
	 *
	 * @param event
	 */
	private void handleAnswerSelection(ActionEvent event) {
		String buttonName = ((Control) event.getSource()).getId();
		System.out.println("Player selected answer via Object: "
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
		clearStrings();
		int questionsIndex = -1;

		String type = " AND GameType = '", difficulty = " AND Difficulty = '";

		type += GameSetUpController.typeIsTf ? "TrueFalse'" : "MultipleChoice'";
		difficulty += GameSetUpController.difficultyIsEasy ? "Easy'" : "Hard'";

		if (GameSetUpController.typeIsMixed) {
			type = "";
		}
		if (GameSetUpController.difficultyIsMixed) {
			difficulty = "";
		}

		try {
			dbm.openConnection();
			String queryText = ("SELECT q.Question, r.RightAnswer, w.WrongAnswer, w2.WrongAnswer, "
					+ "w3.WrongAnswer, q.GameType, q.Difficulty, q.QuestionId FROM question q "
					+ "INNER JOIN rightanswer r ON q.QuestionId = r.QuestionId "
					+ "INNER JOIN wronganswer w ON w.QuestionId = q.QuestionId "
					+ "INNER JOIN wronganswer w2 ON w2.QuestionId = q.QuestionId "
					+ "INNER JOIN wronganswer w3 ON w3.QuestionId = q.QuestionId "
					+ "WHERE w.WrongAnswerId = 1 AND w2.WrongAnswerId = 2 AND "
					+ "w3.WrongAnswerId = 3" + type + difficulty + " ORDER BY RAND() LIMIT ?;");
			statement = dbm.connection.prepareStatement(queryText);
			// Get everything above.
			statement.setInt(1, gameLength);
			rs = statement.executeQuery(); // Get everything.
			while (rs.next()) { // Save everything.
				System.out.print("Loaded details for {"
						+ ++questionsIndex + "}"); // Loads next question.
				for (int i = 0; i <= 7; i++) {
					loadedStrings[questionsIndex][i] = rs.getString(i + 1);
					System.out.print("[" + i + "]");
					if (i == 7) {
						System.out.println("");
					}
				}
			}
		} catch (SQLException | NullPointerException e) {
			System.err.println("Error: " + e.getLocalizedMessage());
			return;
		} finally {
			dbm.closeConnection();
			rs = null;
			statement = null;
			updateParameters = null;
		}
		// Print loaded questions.
		for (int i = 0; i < gameLength; i++) {
			System.out.print("\nLoaded data #" + (i + 1) + ": ");
			for (int j = 0; j <= 7; j++) { // Print stored data for each question.
				if (j > 0) {
					System.out.print(" | ");
				}
				System.out.print(loadedStrings[i][j]);
			}
		}
		// Store all correct answer strings.
		storeCorrectAnswerStrings();
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
			Collections.shuffle(Arrays.asList(temp)); // Randomization takes place.
			System.out.print("\nQuestion " + (i + 1) + " after randomization: ");
			for (int j = 0; j < 4; j++) {
				loadedStrings[i][j + 1] = temp[j];
				if (j > 0) {
					System.out.print(" | ");
				}
				System.out.print(loadedStrings[i][j + 1]);
			}
			System.out.println("");
		}
		// Store all correct answer labels.
		storeCorrectAnswerLabels();

		if (timerSetting) { // Timer enabled.
			progressBar.setVisible(true);
			remainingTimerDuration = new ArrayList<Integer>();
			for (int i = 0; i < gameLength; i++) {
				remainingTimerDuration.add(i, timeSeconds.getValue());
				System.out.println("Time remaining for question " + (i + 1) + ": "
						+ remainingTimerDuration.get(i).toString());
			}
			autoPlay();
		} else { // Timer disabled.
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
		System.out.println(temp + "QuestionButton was fired.");
		if (timerSetting && !gameIsFinished) { // Updates timer for each question.
			remainingTimerDuration.set(questionNumber - 1, parseInt(timer.getText()));
			// Print a detailed overview.
//			for (int i = 0; i < gameLength; i++) {
//				System.out.println("Time remaining for question " + (i + 1) + ": "
//						+ remainingTimerDuration.get(i).toString());
//			}
//			for (int i = 0; i < chosenAnswers.length; i++) { // Print chosen answers.
//				System.out.println("Answer to question " + (i + 1) + ": "
//						+ chosenAnswers[i][1]);
//			}
		} else if (gameIsFinished) { // Game ended, reviewing answers.
//			timer.setVisible(false);
			progressBar.setVisible(false);
		}

		// Makes sure players can pick or change answers, unless the game's finished.
		for (ToggleButton tb : answerButtons) {
			tb.setDisable(gameIsFinished);
		}
		for (Hyperlink h : answerLabels) {
			h.setDisable(gameIsFinished);
		}
		if (gameIsFinished) { // Show answers as normal, even though they're disabled.
			for (ToggleButton tb : answerButtons) {
				tb.setOpacity(1.0);
			}
			for (Hyperlink h : answerLabels) {
				h.setOpacity(1.0);
			}
		}

		if (nextQuestion) { // nextQuestionButton is pressed.
			if (questionNumber + 1 > gameLength) { // There is no next question (this is the last).
				if (timerSetting) {
					timeline.stop();
				}
				if (gameIsFinished && reviewedAnswers) {
					if (alertDialog(AlertType.CONFIRMATION, "Score bekijken", null,
							"Als u doorgaat ziet u uw score en kunt u de vragen "
							+ "niet meer bekijken.")) {
						loadView(event); // --> go to ScoreOverview.
						return; // End of method.
					}
					return; // Player canceled, player can still view previous answers.
				} else if (gameIsFinished) { // Only supposed to reach this once, use return.
					questionNumber = 1;
					reviewedAnswers = true;
					for (int i = 0; i < gameLength; i++) {
						System.out.println("Correct answer " + (i + 1) + ": "
								+ correctAnswers[i][1] + " | " + correctAnswers[i][0]);
					}
					return;
				}
				if (alertDialog(AlertType.CONFIRMATION, "Quiz beëindigen", null,
						"Als u doorgaat wordt het spel beëindigd en kunt u de"
						+ " juiste antwoorden inzien.")) {
					for (int i = 0; i < chosenAnswers.length; i++) { // Print chosen answers.
						System.out.println("Answer to question " + (i + 1) + ": "
								+ chosenAnswers[i][1]);
						if (chosenAnswers[i][1].equals("E")) {
							// Player skipped question.
							skippedQuestionsTotal++;
						}
					}
					gameIsFinished = true; // Only supposed to reach this once, use return.
					viewScore.setVisible(true);
					nextQuestionButton.setText("Volgende");
					nextQuestion(event, true);
				}
			} else { // Next question is not the last question.
				questionNumber++;
				previousQuestionButton.setDisable(false);
				if (questionNumber + 1 > gameLength && !gameIsFinished) {
					nextQuestionButton.setText("Bekijk vragen");
				} else if (questionNumber + 1 > gameLength && gameIsFinished && reviewedAnswers) {
					nextQuestionButton.setText("Bekijk score");
				}
			}
		} else { // previousQuestionButton is pressed.
			if (questionNumber <= 2) { // If curr question is 1 or 2, disable previousQB.
				previousQuestionButton.setDisable(true);
			}
			questionNumber--;
			if (questionNumber + 2 > gameLength && !(questionNumber + 1 > gameLength)) {
				nextQuestionButton.setText("Volgende"); // In case we're only near the last.
			}
		}
		// Set question progress (e.g. 1/15), set question and answers.
		questionProgress.setText(questionNumber + " / " + gameLength);
		setLabels(questionNumber - 1);
		getChosenAnswers(questionNumber - 1);

		if (timerSetting) {
			autoPlay();
		}
		if (gameIsFinished) {
			showAnswers();
		}
	}

	/**
	 * This method restores the default settings on game ending.
	 */
	public static void resetGameSettings() {
		loadSettings();
		for (String s : varHolder) {
			switch (s) {
				case "difficultyMixed":
					System.out.println("Reset difficulty setting: mixed.");
					GameSetUpController.difficultyIsMixed = true;
					break;
				case "difficultyEasy":
					System.out.println("Reset difficulty setting: easy.");
					GameSetUpController.difficultyIsEasy = true;
					GameSetUpController.difficultyIsMixed = false;
					break;
				case "difficultyHard":
					System.out.println("Reset difficulty setting: hard.");
					GameSetUpController.difficultyIsEasy = false;
					GameSetUpController.difficultyIsMixed = false;
					break;
				case "typeMixed":
					System.out.println("Reset type setting: mixed.");
					GameSetUpController.typeIsMixed = true;
					break;
				case "typeTf":
					System.out.println("Reset type setting: true or false.");
					GameSetUpController.typeIsTf = true;
					GameSetUpController.typeIsMixed = false;
					break;
				case "typeMc":
					System.out.println("Reset type setting: multiple choice.");
					GameSetUpController.typeIsTf = false;
					GameSetUpController.typeIsMixed = false;
					break;
				case "shortLength":
					System.out.println("Reset length setting: short.");
					gameLength = SHORT_LENGTH;
					break;
				case "mediumLength":
					System.out.println("Reset length setting: medium.");
					gameLength = MEDIUM_LENGTH;
					break;
				case "longLength":
					System.out.println("Reset length setting: long.");
					gameLength = LONG_LENGTH;
					break;
				default: // Nothing is selected or it's a number.
					if (parseInt(s) >= 10 && parseInt(s) <= 60) {
						System.out.println("Reset timer duration: " + s + " seconds.");
					} else {
						System.err.println("Something is wrong with the (default) "
								+ "var prefs. Suspect: " + s);
					}
					break;
			}
		}

		timerSetting = boolHolder[0];
		System.out.println("Reset timer setting: " + (timerSetting ? "on." : "off."));
	}

	/**
	 * This method updates the question and answer labels.
	 *
	 * @param storedDataRow
	 */
	private void setLabels(int storedDataRow) {
		int storedDataNumber = 0;
		questionLabel.setText(loadedStrings[storedDataRow][storedDataNumber]);
		for (Hyperlink h : answerLabels) {
			storedDataNumber++;
			h.setText(loadedStrings[storedDataRow][storedDataNumber]);
		}
	}

	private void showAnswers() {
		for (int i = 0; i < gameLength; i++) {
			if (chosenAnswers[i][0].equals(
					correctAnswers[i][0])) {
				correctAnswersTotal++;
				correctlyAnsweredQuestions.add(i);
			}
		}
		if (chosenAnswers[questionNumber - 1][0].equals(correctAnswers[questionNumber - 1][0])) {
			switch (chosenAnswers[questionNumber - 1][1]) {
				case "A":
					checkMarkA.setStyle("-fx-text-fill: #00F72C");
					checkMarkA.setText("√");
					checkMarkB.setText("");
					checkMarkC.setText("");
					checkMarkD.setText("");
					break;
				case "B":
					checkMarkB.setStyle("-fx-text-fill: #00F72C");
					checkMarkA.setText("");
					checkMarkB.setText("√");
					checkMarkC.setText("");
					checkMarkD.setText("");
					break;
				case "C":
					checkMarkC.setStyle("-fx-text-fill: #00F72C");
					checkMarkA.setText("");
					checkMarkB.setText("");
					checkMarkC.setText("√");
					checkMarkD.setText("");
					break;
				case "D":
					checkMarkD.setStyle("-fx-text-fill: #00F72C");
					checkMarkA.setText("");
					checkMarkB.setText("");
					checkMarkC.setText("");
					checkMarkD.setText("√");
					break;
				default:
					checkMarkA.setText("");
					checkMarkB.setText("");
					checkMarkC.setText("");
					checkMarkD.setText("");
					break;
			}
		} else {
			switch (chosenAnswers[questionNumber - 1][1]) {
				case "A":
					checkMarkA.setStyle("-fx-text-fill: #CE2029");
					checkMarkA.setText("X");
					checkMarkB.setText("");
					checkMarkC.setText("");
					checkMarkD.setText("");
					break;
				case "B":
					checkMarkB.setStyle("-fx-text-fill: #CE2029");
					checkMarkA.setText("");
					checkMarkB.setText("X");
					checkMarkC.setText("");
					checkMarkD.setText("");
					break;
				case "C":
					checkMarkC.setStyle("-fx-text-fill: #CE2029");
					checkMarkA.setText("");
					checkMarkB.setText("");
					checkMarkC.setText("X");
					checkMarkD.setText("");
					break;
				case "D":
					checkMarkD.setStyle("-fx-text-fill: #CE2029");
					checkMarkA.setText("");
					checkMarkB.setText("");
					checkMarkC.setText("");
					checkMarkD.setText("X");
					break;
				default:
					checkMarkA.setText("");
					checkMarkB.setText("");
					checkMarkC.setText("");
					checkMarkD.setText("");
					break;
			}
		}
		switch (correctAnswers[questionNumber - 1][1]) {
			case "A":
				labelA.setTextFill(GREEN);
				buttonA.getStylesheets().add(css);
				labelB.setTextFill(BLACK);
				buttonB.getStylesheets().clear();
				labelC.setTextFill(BLACK);
				buttonC.getStylesheets().clear();
				labelD.setTextFill(BLACK);
				buttonD.getStylesheets().clear();
				break;
			case "B":
				labelA.setTextFill(BLACK);
				buttonA.getStylesheets().clear();
				labelB.setTextFill(GREEN);
				buttonB.getStylesheets().add(css);
				labelC.setTextFill(BLACK);
				buttonC.getStylesheets().clear();
				labelD.setTextFill(BLACK);
				buttonD.getStylesheets().clear();
				break;
			case "C":
				labelA.setTextFill(BLACK);
				buttonA.getStylesheets().clear();
				labelB.setTextFill(BLACK);
				buttonB.getStylesheets().clear();
				labelC.setTextFill(GREEN);
				buttonC.getStylesheets().add(css);
				labelD.setTextFill(BLACK);
				buttonD.getStylesheets().clear();
				break;
			case "D":
				labelA.setTextFill(BLACK);
				buttonA.getStylesheets().clear();
				labelB.setTextFill(BLACK);
				buttonB.getStylesheets().clear();
				labelC.setTextFill(BLACK);
				buttonC.getStylesheets().clear();
				labelD.setTextFill(GREEN);
				buttonD.getStylesheets().add(css);
				break;
			default:
				labelA.setTextFill(BLACK);
				buttonA.getStylesheets().clear();
				labelB.setTextFill(BLACK);
				buttonB.getStylesheets().clear();
				labelC.setTextFill(BLACK);
				buttonC.getStylesheets().clear();
				labelD.setTextFill(BLACK);
				buttonD.getStylesheets().clear();
				System.out.println("There was no correct answer saved.");
		}

	}

	@FXML
	private void stopQuiz(ActionEvent event) {
		if (timerSetting) {
			timeline.stop();
		}
		if (alertDialog(AlertType.CONFIRMATION, "Stop quiz", "Weet u zeker dat u"
				+ " de quiz wilt stoppen?", "De antwoorden worden niet opgeslagen."
				+ "\nDit brengt u terug naar het hoofdmenu.")) {
			resetGameSettings();
			loadView(event);
		} else { // Player cancels the method, if timer's enabled, continue countdown.
			if (timerSetting && !gameIsFinished) {
				timeline.play();
			}
		}
	}

	/**
	 * Method stores the correct answer string.
	 */
	private void storeCorrectAnswerStrings() {
		for (int i = 0; i < gameLength; i++) {
			correctAnswers[i][0] = loadedStrings[i][1];
		}
		System.out.print("\nStored correct answers for all questions.");
	}

	/**
	 * Method stores the label containing the correct answer after
	 * randomization.
	 *
	 * @param answer
	 */
	private void storeCorrectAnswerLabels() {
		for (int i = 0; i < loadedStrings.length; i++) {
			for (int j = 1; j < 5; j++) {
				if (loadedStrings[i][j].equals(correctAnswers[i][0])) {
					switch (j) { // Find and remember which label holds the correct answer.
						case 1:
							correctAnswers[i][1] = "A";
							break;
						case 2:
							correctAnswers[i][1] = "B";
							break;
						case 3:
							correctAnswers[i][1] = "C";
							break;
						case 4:
							correctAnswers[i][1] = "D";
							break;
						default:
							System.out.println("Something went wrong when storing answers.");
							break;
					}
					break;
				}
			}
		}
	}
}
