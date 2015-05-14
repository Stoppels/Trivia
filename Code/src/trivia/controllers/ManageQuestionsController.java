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
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
public class ManageQuestionsController extends Trivia implements Initializable {

	@FXML
	Button adminMenu;

	@FXML
	Button addQuestionButton;

	@FXML
	TextField addQuestionText;

	@FXML
	TextField addCorrectAnswer;

	@FXML
	TextField addIncorrectAnswer1;

	@FXML
	TextField addIncorrectAnswer2;

	@FXML
	TextField addIncorrectAnswer3;

	@FXML
	ComboBox selectQuestion;

	@FXML
	Button deleteQuestionButton;

	@FXML
	ToggleGroup difficultyGroup;

	@FXML
	ToggleButton difficultyEasy;

	@FXML
	ToggleButton difficultyHard;

	DbManager dbm = new DbManager();
	ResultSet rs = null;
	String queryText = "";
	public static String difficultySetter = "";

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
		setComboBoxQuestions();
		// When something is selected the ComboBox (an event) -> print out the selection.
		selectQuestion.setOnAction(e -> System.out.println("User selected : "
				+ selectQuestion.getValue()));
		deleteQuestionButton.setOnAction(this::confirmDeleteQuestion);
	}

	/**
	 * This method shows an alert dialog asking for confirmation for adding the
	 * entered question and its answers.
	 *
	 * @param event
	 */
	public void confirmAlertAddQuestion(ActionEvent event) {
		try {
			if (addQuestionText.getText().equals("") || addQuestionText.getText().equals("")
					|| addIncorrectAnswer1.getText().equals("")
					|| addIncorrectAnswer2.getText().equals("")
					|| addIncorrectAnswer3.getText().equals("") || (!difficultyEasy.isSelected() && !difficultyHard.isSelected())) {
				alertDialog(Alert.AlertType.ERROR, "Tekstveld leeg", null, "Elk tekstveld moet "
						+ "zijn ingevuld.", StageStyle.UNDECORATED);
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
	 * This method shows an alert dialog asking for confirmation for deleting
	 * the selected question.
	 *
	 * @param event
	 */
	public void confirmDeleteQuestion(ActionEvent event) {
		if (selectQuestion.getValue() == null) {
			alertDialog(Alert.AlertType.ERROR, "Vraag verwijderen",
					"Verwijdering gestopt",
					"Er is geen vraag geselecteerd om te verwijderen."
					+ "\nSelecteer een vraag om deze te verwijderen.", StageStyle.UNDECORATED);
		} else if (alertDialog(AlertType.CONFIRMATION, "Vraag verwijderen", "Weet u zeker dat u "
				+ "deze vraag wilt verwijderen?", "De vraag: " + selectQuestion.getValue(),
				StageStyle.UNDECORATED)) {
			deleteQuestion();
		}
		// Else -> User selected a question, but decided to cancel deletion.
	}

	/**
	 * This method handles adding new questions to the database.
	 */
	public void addQuestion() {
		// If the Toggle's properties contains "Easy" set as Easy, otherwise Hard
		difficultySetter = difficultyGroup.getSelectedToggle().
				toString().contains("Easy") ? "Easy" : "Hard";
		// Collect the Strings with getText from the selected textField
		String question = addQuestionText.getText();
		String correctAnswer = addCorrectAnswer.getText();
		String incorrectAnswer1 = addIncorrectAnswer1.getText();
		String incorrectAnswer2 = addIncorrectAnswer2.getText();
		String incorrectAnswer3 = addIncorrectAnswer3.getText();

		// System check of input
		System.out.println("Adding question: " + question + "\n"
				+ "Adding Correct Answer: " + correctAnswer + "\n"
				+ "Adding Incorrect Answer 1: " + incorrectAnswer1 + "\n"
				+ "Adding Incorrect Answer 2: " + incorrectAnswer2 + "\n"
				+ "Adding Incorrect Answer 3: " + incorrectAnswer3 + "\n"
				+ "Setting question difficulty: " + difficultySetter);

		dbm.openConnection();
		queryText = "INSERT INTO question VALUES(NULL, '" + question + "', '" + difficultySetter + "');";

		dbm.executeUpdate(queryText);
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

			// If we get to here, everything is stored in the database
			addQuestionText.setText("");
			addCorrectAnswer.setText("");
			addIncorrectAnswer1.setText("");
			addIncorrectAnswer2.setText("");
			addIncorrectAnswer3.setText("");
			difficultyGroup.selectToggle(null);
			addQuestionText.requestFocus();

			alertDialog(Alert.AlertType.INFORMATION, "Vraag toevoegen", null,
					"De vraag is succesvol toegevoegd!", StageStyle.UNDECORATED);
			// Updates the ComboBox to reflect the changes
			setComboBoxQuestions();
		} catch (SQLException e) {
			System.err.println("Error: " + e.getLocalizedMessage());
		} finally {
			dbm.closeConnection();
			rs = null;
			queryText = "";
		}
	}

	/**
	 * This method handles deleting questions from the database.
	 */
	public void deleteQuestion() {
		try {
			// If no question selected: there is no question selected to delete Error Dialog
			dbm.openConnection();
			String countRows = "SELECT COUNT(*) FROM question";
			rs = dbm.doQuery(countRows);

			rs.next();
			int priorToUpdate = parseInt(rs.getString(1));
			System.out.println("Questions prior to deletion: " + priorToUpdate);

			queryText = "DELETE FROM question WHERE Question = '" + selectQuestion.getValue() + "';";
			dbm.executeUpdate(queryText);

			rs = dbm.doQuery(countRows);
			rs.next();
			System.out.println("Questions after deletion: " + parseInt(rs.getString(1)));

			if ((priorToUpdate - 1) == parseInt(rs.getString(1))) {
				System.out.println("Deleting question: " + selectQuestion.getValue());
				alertDialog(Alert.AlertType.INFORMATION, "Vraag verwijderen", null,
						"De vraag is succesvol verwijderd!", StageStyle.UNDECORATED);
			}
		} catch (SQLException e) {
			e.getLocalizedMessage();
		} finally {
			dbm.closeConnection();
			rs = null;
			queryText = "";
		}
		// Updates the ComboBox to reflect the changes
		setComboBoxQuestions();
	}

	/**
	 * This populates the ComboBox with all the questions in the database.
	 */
	public void setComboBoxQuestions() {
		//make a new list with questions for the combobox
		ArrayList list = new ArrayList();

		dbm.openConnection();
		int questionsTotal = 0;
		try {
			queryText = "SELECT QuestionId FROM question ORDER by QuestionId DESC LIMIT 1";
			rs = dbm.doQuery(queryText);
			while (rs.next()) {
				questionsTotal = parseInt(rs.getString(1));
			}
			for (int i = 0; i <= questionsTotal; i++) {
				queryText = "SELECT Question FROM question WHERE QuestionId = " + i + ";";

				// add the results of the SELECT query to the arraylist list
				rs = dbm.doQuery(queryText);
				while (rs.next()) {
					list.add(rs.getString(1));
				}
			}
			// Showcase the list as a test
			System.out.println("List of questions: " + list + "\n"
					+ "Number of questions: " + questionsTotal);

			//cast arraylist to observable list from
			//http://stackoverflow.com/questions/22191954/javafx-casting-arraylist-to-observablelist
			ObservableList questions = FXCollections.observableArrayList(list);

			//set the items (named as questions) in the ComboBox "selectQuestion"
			selectQuestion.setItems(questions);
		} catch (SQLException | NumberFormatException e) {
			System.err.println("Error: " + e.getLocalizedMessage());
		} finally {
			dbm.closeConnection();
			rs = null;
			queryText = "";
		}
	}

}
