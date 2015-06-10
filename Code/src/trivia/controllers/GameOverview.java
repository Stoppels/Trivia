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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.stage.StageStyle;
import static trivia.Trivia.statement;
import trivia.connectivity.DbManager;
import static trivia.controllers.GameSetUpController.difficultyIsEasy;
import static trivia.controllers.GameSetUpController.difficultyIsMixed;
import static trivia.controllers.QuestionController.correctAnswersTotal;
import static trivia.controllers.QuestionController.correctlyAnsweredQuestions;
import static trivia.controllers.QuestionController.remainingTimerDuration;
import static trivia.controllers.QuestionController.skippedQuestionsTotal;

/**
 * FXML Controller class
 *
 * @author Team Silent Coders
 * @version 1.0
 */
public class GameOverview extends trivia.Trivia implements Initializable {

	@FXML
	private Label remainingTimeTextLabel;

	@FXML
	private Label fastestQuestionTextLabel;

	@FXML
	private Label correctlyAnsweredLabel;

	@FXML
	private Label totalQuestionsLabel;

	@FXML
	private Label totalWrongAnswers;

	@FXML
	private Label totalScoreAnswers;

	@FXML
	private Label skippedQuestionsLabel;

	@FXML
	private Label fastestQuestionLabel;

	@FXML
	private Label remainingTimeLabel;

	@FXML
	private Label scoreLabel;

	@FXML
	private Button highScore;

	private final DbManager dbm = new DbManager();

	private int remainingTime = 0;
	private int fastestAnswer = 0;
	private String fastestAnswerString = "";
	private Double score = 0.0;
	private List parameters;
	public static Boolean skippedNameEntry = false;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		highScore.setOnAction(this::enterHighScore);

		correctlyAnsweredLabel.setText(
				correctAnswersTotal.toString() + (correctAnswersTotal < 10 ? " " : ""));
		totalQuestionsLabel.setText(gameLength.toString());
		totalWrongAnswers.setText(
				String.valueOf(gameLength - correctAnswersTotal - skippedQuestionsTotal));
		skippedQuestionsLabel.setText(skippedQuestionsTotal.toString());
		computeScore();
	}

	private void computeScore() {
		int wrongAnswerAmount = gameLength - correctAnswersTotal - skippedQuestionsTotal;

		if (difficultyIsMixed) { // Difficulty is mixed.
			score += (correctAnswersTotal * 10); // 10 points for every correct answer.
			score += (wrongAnswerAmount * 2.5); // 2.5 points for every incorrect answer.
			score += (skippedQuestionsTotal); // 1 point for every skipped answer.
		} else if (difficultyIsEasy) { // Difficulty is easy.
			score += (correctAnswersTotal * 8); // 8 points for every correct answer.			
			score += (wrongAnswerAmount * 2); // 2 points for every incorrect answer.
			score += (skippedQuestionsTotal * 0.5); // 0.5 point for every skipped answer.
		} else if (!difficultyIsEasy) { // Difficulty is hard.
			score += (correctAnswersTotal * 12); // 12 points for every correct answer.
			score += (wrongAnswerAmount * 3); // 3 points for every incorrect answer.
			score += (skippedQuestionsTotal * 1.5); // 1.5 points for every skipped answer.
		} else {
			System.err.println("Error: Something went wrong with computeScore().");
		}
		score *= 0.9; // Subtract 10% of score.
		totalScoreAnswers.setText(String.valueOf(score.intValue()));
		System.out.println("Points for answers: " + score);
		if (timerSetting) {
			for (Integer i : correctlyAnsweredQuestions) {
				int timeValue = remainingTimerDuration.get(i);
				remainingTime += timeValue;
				System.out.println("Added remaining time for question: " + (i + 1));

				if (timeValue > fastestAnswer) {
					fastestAnswer = timeValue;
					fastestAnswerString = QuestionController.loadedStrings[i][0];
				}
			}
			for (int i = 0; i < gameLength; i++) {
				System.out.println("Time remaining for question " + (i + 1) + ": "
						+ remainingTimerDuration.get(i).toString());
			}
			score += (remainingTime * 0.2);
			remainingTimeLabel.setText(String.valueOf(remainingTime));
			fastestQuestionLabel.setText(
					fastestAnswerString.equals("") ? "Geen" : fastestAnswerString);
		} else {
			remainingTimeTextLabel.setVisible(timerSetting);
			remainingTimeLabel.setVisible(timerSetting);
			fastestQuestionTextLabel.setVisible(timerSetting);
			fastestQuestionLabel.setVisible(timerSetting);
		}
		scoreLabel.setText(String.valueOf(score.intValue()));
	}

	private void enterHighScore(ActionEvent event) {
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle("Voer naam in");
		dialog.setHeaderText(null);
		dialog.setContentText("Vul hier uw naam in of sla deze stap over.");
		dialog.initStyle(StageStyle.UNDECORATED);

		try {
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				Timestamp timestamp = new Timestamp(new java.util.Date().getTime());
				try {
					dbm.openConnection();
					statement = dbm.connection.prepareStatement(
							"INSERT INTO highscore VALUES(?, ?, ?);");
					parameters = Arrays.asList(score.intValue(), result.get(), timestamp);
					dbm.insertHighScore(statement, parameters);
				} catch (SQLException e) {
					System.err.println("Error: " + e.getLocalizedMessage());
				} finally {
					dbm.closeConnection();
					statement = null;
					parameters = null;
					loadView(event);
				}
				System.out.println(result.get());
			} else {
				skippedNameEntry = true;
				loadView(event);
			}
		} catch (NullPointerException e) {
			System.err.print("Error: " + e.getLocalizedMessage());
			loadView(event);
		}
	}
}
