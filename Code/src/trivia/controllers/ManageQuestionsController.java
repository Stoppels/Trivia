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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
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
	ComboBox selectQuestion;

	@FXML
	TextField editQuestionText;

	@FXML
	TextField editCorrectAnswer;

	@FXML
	TextField editIncorrectAnswer1;

	@FXML
	TextField editIncorrectAnswer2;

	@FXML
	TextField editIncorrectAnswer3;

	@FXML
	ToggleGroup difficultyGroup;

	@FXML
	ToggleButton difficultyEasy;

	@FXML
	ToggleButton difficultyHard;

	@FXML
	Button adminMenu;

	@FXML
	Button deleteQuestionButton;

	@FXML
	Button editQuestionButton;

	private final DbManager dbm = new DbManager();
	private ResultSet result = null;
	private String queryText = "", difficultySetter = "";
	private int currentQuestion = 0;
	private List<TextField> answerFields;

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		answerFields = Arrays.asList(editCorrectAnswer, editIncorrectAnswer1,
				editIncorrectAnswer2, editIncorrectAnswer3);

		adminMenu.setOnAction(this::loadView);
		editQuestionButton.setOnAction(this::confirmAlertEditQuestion);
		setComboBoxQuestions();
		// When something is selected the ComboBox (an event) -> print out the selection.
		selectQuestion.setOnAction(this::fillFields);
		deleteQuestionButton.setOnAction(this::confirmDeleteQuestion);
	}

	/**
	 * Clears all the fields and deselects all toggles.
	 */
	private void clearFields() {
		editQuestionText.setText("");
		for (TextField t : answerFields) {
			t.setText("");
		}
		difficultyGroup.selectToggle(null);
		editQuestionText.requestFocus();
	}

	/**
	 * This method shows an alert dialog asking for confirmation for deleting
	 * the selected question.
	 *
	 * @param event
	 */
	private void confirmDeleteQuestion(ActionEvent event) {
		if (selectQuestion.getValue() == null) {
			alertDialog(Alert.AlertType.ERROR, "Vraag verwijderen",
					"Verwijdering gestopt",
					"Er is geen vraag geselecteerd om te verwijderen."
					+ "\nSelecteer een vraag om deze te verwijderen.", StageStyle.UNDECORATED);
		} else if (alertDialog(AlertType.CONFIRMATION, "Vraag verwijderen", "Weet u zeker dat u"
				+ " deze vraag wilt verwijderen?", "De vraag: " + selectQuestion.getValue(),
				StageStyle.UNDECORATED)) {
			deleteQuestion();
		}
		// Else -> Player selected a question, but decided to cancel deletion.
	}

	/**
	 * This method shows an alert dialog asking for confirmation for saving the
	 * edited question and its answers.
	 *
	 * @param event
	 */
	private void confirmAlertEditQuestion(ActionEvent event) {
		try {
			if (editQuestionText.getText().equals("") || editQuestionText.getText().equals("")
					|| editIncorrectAnswer1.getText().equals("")
					|| editIncorrectAnswer2.getText().equals("")
					|| editIncorrectAnswer3.getText().equals("")
					|| (!difficultyEasy.isSelected() && !difficultyHard.isSelected())) {
				alertDialog(Alert.AlertType.ERROR, "Tekstveld leeg", null, "Elk tekstveld moet"
						+ " zijn ingevuld en een moeilijkheidsgraad moet zijn gekozen.",
						StageStyle.UNDECORATED);
			} else if (alertDialog(Alert.AlertType.CONFIRMATION, "Vraag wijzigen",
					"Weet u zeker dat u de wijzigingen wilt opslaan?",
					"De vraag: " + editQuestionText.getText()
					+ "\nMet het juiste antwoord: " + editCorrectAnswer.getText()
					+ "\nEn de onjuiste antwoorden:\n– " + editIncorrectAnswer1.getText()
					+ "\n– " + editIncorrectAnswer2.getText() + "\n– "
					+ editIncorrectAnswer3.getText(), StageStyle.UNDECORATED)) {
				editQuestion();
			}
		} catch (NoSuchElementException e) {
			// No need to handle exception. Alert already does.
		}
	}

	/**
	 * This method handles deleting questions from the database.
	 */
	private void deleteQuestion() {
		dbm.openConnection();
		try {
			// If no question selected: there is no question selected to delete Error Dialog.
			String countRows = "SELECT COUNT(*) FROM question";
			result = dbm.doQuery(countRows);

			result.next();
			int priorToUpdate = result.getInt(1);
			System.out.println("Questions prior to deletion: " + priorToUpdate);

			queryText = "DELETE FROM question WHERE Question = '"
					+ selectQuestion.getValue() + "';";
			dbm.executeUpdate(queryText);

			result = dbm.doQuery(countRows);
			result.next();
			System.out.println("Questions after deletion: " + result.getInt(1));

			if ((priorToUpdate - 1) == result.getInt(1)) {
				System.out.println("Deleted question: " + selectQuestion.getValue());
				alertDialog(Alert.AlertType.INFORMATION, "Vraag verwijderen", null,
						"De vraag is succesvol verwijderd!", StageStyle.UNDECORATED);
			}
		} catch (SQLException e) {
			e.getLocalizedMessage();
		} finally {
			dbm.closeConnection();
			result = null;
			queryText = "";
		}
		// Updates the ComboBox to reflect the changes.
		setComboBoxQuestions();

		// If we get to here, the question entry and its answers have been deleted.
		clearFields();
	}

	/**
	 * Populates all fields and the right toggle if a question has been
	 * selected.
	 */
	private void fillFields(Event event) {
		try {
			if (selectQuestion.getValue() == null) { // If no question has been selected
				toggleFields(true); // Disable all fields and ToggleButtons
				return; // And don't try to fill/select them (aborts this method)
			} else {
				toggleFields(false); // Otherwise enable the fields and execute this method
			}
		} catch (NullPointerException e) {
			System.err.println("Error: " + e.getLocalizedMessage()); // Hurr durr I exception
		}

		String question = selectQuestion.getValue().toString();
		int i = 1;
		System.out.println("Player selected question: " + question);
		dbm.openConnection();
		try {
			queryText = "SELECT r.RightAnswer, w.WrongAnswer, w2.WrongAnswer, "
					+ "w3.WrongAnswer, q.Difficulty, q.QuestionId FROM question q "
					+ "inner join rightanswer r ON q.QuestionId = r.QuestionId "
					+ "inner join wronganswer w ON w.QuestionId = q.QuestionId "
					+ "inner join wronganswer w2 ON w2.QuestionId = q.QuestionId "
					+ "inner join wronganswer w3 ON w3.QuestionId = q.QuestionId "
					+ "WHERE w.WrongAnswerId = 1 AND w2.WrongAnswerId = 2 AND "
					+ "w3.WrongAnswerId = 3 AND Question = '" + question + "';";
			result = dbm.doQuery(queryText); // Get everything
			result.next();
			editQuestionText.setText(question); // Fill the question field
			for (TextField t : answerFields) { // Fill the answer fields
				t.setText(result.getString(i));
				i++;
			}
			if (result.getString(i).contains("Easy")) { // Select the right difficulty toggle
				difficultyEasy.setSelected(true);
			} else if (result.getString(i).contains("Hard")) {
				difficultyHard.setSelected(true);
			}
			currentQuestion = result.getInt(i + 1); // Store QuestionId in case of edit
		} catch (SQLException e) {
			System.err.println("Error: " + e.getLocalizedMessage());
		} finally {
			dbm.closeConnection();
			result = null;
			queryText = "";
		}
	}

	/**
	 * This method handles adding new questions to the database.
	 */
	private void editQuestion() {
		// If the Toggle's properties contains "Easy" set as Easy, otherwise Hard
		difficultySetter = difficultyGroup.getSelectedToggle().
				toString().contains("Easy") ? "Easy" : "Hard";
		// Collect the Strings with getText from the selected textField
		String question = editQuestionText.getText(),
				correctAnswer = editCorrectAnswer.getText(),
				incorrectAnswer1 = editIncorrectAnswer1.getText(),
				incorrectAnswer2 = editIncorrectAnswer2.getText(),
				incorrectAnswer3 = editIncorrectAnswer3.getText();

		// Make sure the question starts with an uppercase letter
		question = Character.toUpperCase(question.charAt(0)) + question.substring(1);

		// System check of input
		System.out.println("Changing question to: " + question + "\n"
				+ "Changing Correct Answer to: " + correctAnswer + "\n"
				+ "Changing Incorrect Answer 1 to: " + incorrectAnswer1 + "\n"
				+ "Changing Incorrect Answer 2 to: " + incorrectAnswer2 + "\n"
				+ "Changing Incorrect Answer 3 to: " + incorrectAnswer3 + "\n"
				+ "Changing question difficulty to: " + difficultySetter);

		System.out.println("QuestionId: " + currentQuestion);
		String[] newEntries = new String[]{"UPDATE question SET Question = '" + question
			+ "', Difficulty = '" + difficultySetter + "' WHERE QuestionId = "
			+ currentQuestion + ";", "UPDATE rightanswer SET RightAnswer = '"
			+ correctAnswer + "' WHERE QuestionId = " + currentQuestion + ";",
			"UPDATE wronganswer set WrongAnswer = '" + incorrectAnswer1
			+ "' WHERE QuestionId = " + currentQuestion + " AND WrongAnswerId = 1",
			"UPDATE wronganswer set WrongAnswer = '" + incorrectAnswer2
			+ "' WHERE QuestionId = " + currentQuestion + " AND WrongAnswerId = 2",
			"UPDATE wronganswer set WrongAnswer = '" + incorrectAnswer2
			+ "' WHERE QuestionId = " + currentQuestion + " AND WrongAnswerId = 3"};

		dbm.openConnection();
		for (String s : newEntries) { // Update all entries.
			dbm.executeUpdate(s);
		}
		dbm.closeConnection();
		// Updates the ComboBox to reflect the changes.
		setComboBoxQuestions();
		selectQuestion.setValue(null);
		// If we get to here, the question entry and its answers have been deleted.
		clearFields();
		alertDialog(Alert.AlertType.INFORMATION, "Vraag bewerken", null,
				"De vraag is succesvol aangepast!", StageStyle.UNDECORATED);
	}

	/**
	 * This populates the ComboBox with all the questions in the database.
	 */
	private void setComboBoxQuestions() {
		//make a new list with questions for the combobox
		ArrayList list = new ArrayList();

		dbm.openConnection();
		int questionsTotal = 0;
		try {
			queryText = "SELECT QuestionId FROM question ORDER by QuestionId DESC LIMIT 1";
			result = dbm.doQuery(queryText);
			while (result.next()) {
				questionsTotal = result.getInt(1);
			}
			for (int i = 0; i <= questionsTotal; i++) {
				queryText = "SELECT Question FROM question WHERE QuestionId = " + i + ";";

				// Add the results of the query to the ArrayList list
				result = dbm.doQuery(queryText);
				while (result.next()) {
					list.add(result.getString(1));
				}
			}
			// Test the list by showcasing it
			System.out.println("List of questions: " + list + "\n"
					+ "Number of questions: " + questionsTotal);

			// Cast arraylist to observable list from
			// stackoverflow.com/questions/22191954/javafx-casting-arraylist-to-observablelist
			Collections.sort(list);
			ObservableList questions = FXCollections.observableArrayList(list);

			// Set the questions in the ComboBox selectQuestion
			selectQuestion.setItems(questions);
		} catch (SQLException | NumberFormatException e) {
			System.err.println("Error: " + e.getLocalizedMessage());
		} finally {
			dbm.closeConnection();
			result = null;
			queryText = "";
		}
	}

	/**
	 * If no question is selected in ComboBox, disable all fields & vice versa.
	 *
	 * @param b
	 */
	private void toggleFields(Boolean b) {
		selectQuestion.requestFocus();
		editQuestionText.setDisable(b);
		for (TextField t : answerFields) {
			t.setDisable(b);
		}
		for (Toggle t : difficultyGroup.getToggles()) {
			((ToggleButton) t).setDisable(b);
		}
	}

}
