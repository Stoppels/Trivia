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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import static trivia.controllers.QuestionController.correctAnswersTotal;
import static trivia.controllers.QuestionController.correctlyAnsweredQuestions;
import static trivia.controllers.QuestionController.remainingTimerDuration;
import static trivia.controllers.QuestionController.skippedQuestionsTotal;

/**
 * FXML Controller class
 *
 * @author axel
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
	private Label skippedQuestionsLabel;

	@FXML
	private Label fastestQuestionLabel;

	@FXML
	private Label remainingTimeLabel;

	@FXML
	private Label scoreLabel;

	@FXML
	private Button highScore;

	private int remainingTime = 0;
	private int fastestAnswer = 0;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		highScore.setOnAction(this::loadView);

		correctlyAnsweredLabel.setText(
				correctAnswersTotal.toString() + (correctAnswersTotal < 10 ? " " : ""));
		totalQuestionsLabel.setText(gameLength.toString());
		skippedQuestionsLabel.setText(skippedQuestionsTotal.toString());
		computeScore();
	}

	private void computeScore() {
		double score = 0;

		if (GameSetUpController.difficultyIsEasy) {
			score += (correctAnswersTotal * 8); // 8 points for every correct answer.
		} else if (GameSetUpController.difficultyIsMixed) {
			score += (correctAnswersTotal * 10); // 10 points for every correct answer.			
		} else {
			score += (correctAnswersTotal * 12); // 12 points for every correct answer.
		}
		score *= 0.9; // Subtract 10% of score.
		if (timerSetting) {
			for (Integer i : correctlyAnsweredQuestions) {
				remainingTime += remainingTimerDuration.get(i);
				System.out.println("Added remaining time for question: " + (i + 1));
			}
			for (int i = 0; i < gameLength; i++) {
				System.out.println("Time remaining for question " + (i + 1) + ": "
						+ remainingTimerDuration.get(i).toString());
			}

			score += (remainingTime * 0.2);
			remainingTimeLabel.setText(String.valueOf(remainingTime));
			fastestQuestionLabel.setText("");
		} else {
			remainingTimeTextLabel.setVisible(timerSetting);
			remainingTimeLabel.setVisible(timerSetting);
			fastestQuestionTextLabel.setVisible(timerSetting);
			fastestQuestionLabel.setVisible(timerSetting);
		}
		scoreLabel.setText(String.valueOf((int) score));
	}
}
