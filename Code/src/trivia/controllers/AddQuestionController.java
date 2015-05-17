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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.StageStyle;
import trivia.Trivia;
import static trivia.Trivia.alertDialog;
import trivia.connectivity.DbManager;

/**
 * FXML Controller class
 *
 * @author Team Silent Coders
 * @version 1.0
 */
public class AddQuestionController extends Trivia implements Initializable {

	@FXML
	private TextField addCorrectAnswer;

	@FXML
	private TextField addQuestionText;

	@FXML
	private TextField addIncorrectAnswer1;

	@FXML
	private TextField addIncorrectAnswer2;

	@FXML
	private TextField addIncorrectAnswer3;

	@FXML
	private ToggleGroup difficultyGroup;

	@FXML
	private ToggleButton difficultyEasy;

	@FXML
	private ToggleButton difficultyHard;

	@FXML
	private Button adminMenu;

	@FXML
	private Button addQuestionButton;

	private final DbManager dbm = new DbManager();
	private ResultSet rs = null;
	private String queryText = "",
			difficultySetter = "";
	public static Boolean duplicateError = false;

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		adminMenu.setOnAction(this::loadView);
		addQuestionButton.setOnAction(this::confirmAlertAddQuestion);
	}

	/**
	 * This method shows an alert dialog asking for confirmation for adding the
	 * entered question and its answers.
	 *
	 * @param event
	 */
	private void confirmAlertAddQuestion(ActionEvent event) {
		try {
			if (addQuestionText.getText().equals("") || addQuestionText.getText().equals("")
					|| addIncorrectAnswer1.getText().equals("")
					|| addIncorrectAnswer2.getText().equals("")
					|| addIncorrectAnswer3.getText().equals("") || (!difficultyEasy.isSelected() && !difficultyHard.isSelected())) {
				alertDialog(Alert.AlertType.ERROR, "Tekstveld leeg", null, "Elk tekstveld moet "
						+ "zijn ingevuld en een moeilijkheidsgraad gekozen.", StageStyle.UNDECORATED);
			} else if (alertDialog(Alert.AlertType.CONFIRMATION, "Vraag toevoegen",
					"Weet u zeker dat u deze vraag wilt toevoegen?",
					"De vraag: " + addQuestionText.getText()
					+ "\nMet het juiste antwoord: " + addCorrectAnswer.getText()
					+ "\nEn de onjuiste antwoorden:\n– " + addIncorrectAnswer1.getText()
					+ "\n– " + addIncorrectAnswer2.getText() + "\n– " + addIncorrectAnswer3.getText(),
					StageStyle.UNDECORATED)) {
				addQuestion();
			}
		} catch (NoSuchElementException e) {
			// No need to handle exception
		}
	}

	/**
	 * This method handles adding new questions to the database.
	 */
	private void addQuestion() {
		// If the Toggle's properties contains "Easy" set as Easy, otherwise Hard
		difficultySetter = difficultyGroup.getSelectedToggle().
				toString().contains("Easy") ? "Easy" : "Hard";
		// Collect the Strings with getText from the selected textField
		String question = addQuestionText.getText(),
				correctAnswer = addCorrectAnswer.getText(),
				incorrectAnswer1 = addIncorrectAnswer1.getText(),
				incorrectAnswer2 = addIncorrectAnswer2.getText(),
				incorrectAnswer3 = addIncorrectAnswer3.getText();

		question = Character.toUpperCase(question.charAt(0)) + question.substring(1);
		// System check of input

		dbm.openConnection();
		queryText = "INSERT INTO question VALUES(NULL, '" + question + "', '" + difficultySetter + "');";

		dbm.executeUpdate(queryText);
		if (duplicateError) {
			dbm.closeConnection();
			addQuestionText.requestFocus();
			duplicateError = false;
			return;
		}
		System.out.println("Adding question: " + question + "\n"
				+ "Adding Correct Answer: " + correctAnswer + "\n"
				+ "Adding Incorrect Answer 1: " + incorrectAnswer1 + "\n"
				+ "Adding Incorrect Answer 2: " + incorrectAnswer2 + "\n"
				+ "Adding Incorrect Answer 3: " + incorrectAnswer3 + "\n"
				+ "Setting question difficulty: " + difficultySetter);

		try {
			//Get the QuestionId from the query
			queryText = "SELECT QuestionId FROM question WHERE Question = '" + question + "';";

			rs = dbm.doQuery(queryText);
			rs.next();
			String questionId = rs.getString("QuestionId");
			System.out.println("QuestionId: " + questionId);

			// Create the SQL insert scripts strings
			String insertCorrectAnswer = "INSERT INTO rightanswer VALUES("
					+ questionId + ", '" + correctAnswer + "', " + questionId + ");";
			String insertIncorrectAnswers = "INSERT INTO `wronganswer` VALUES"
					+ "(1, '" + incorrectAnswer1 + "', " + questionId + "),"
					+ "(2, '" + incorrectAnswer2 + "', " + questionId + "),"
					+ "(3, '" + incorrectAnswer3 + "', " + questionId + ");";

			// Execute the script
			dbm.executeUpdate(insertCorrectAnswer);
			dbm.executeUpdate(insertIncorrectAnswers);

			// If we get to here, everything is stored in the database; clear fields
			addQuestionText.setText("");
			addCorrectAnswer.setText("");
			addIncorrectAnswer1.setText("");
			addIncorrectAnswer2.setText("");
			addIncorrectAnswer3.setText("");
			difficultyGroup.selectToggle(null);
			addQuestionText.requestFocus();

			alertDialog(Alert.AlertType.INFORMATION, "Vraag toevoegen", null,
					"De vraag is succesvol toegevoegd!", StageStyle.UNDECORATED);

		} catch (SQLException e) {
			System.err.println("Error: " + e.getLocalizedMessage());
		} finally {
			dbm.closeConnection();
			rs = null;
			queryText = "";
		}
	}

}
