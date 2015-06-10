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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.StageStyle;
import trivia.Trivia;
import static trivia.Trivia.alertDialog;
import static trivia.Trivia.rs;
import static trivia.Trivia.statement;
import static trivia.Trivia.updateParameters;
import trivia.connectivity.DbManager;
import static trivia.controllers.AddQuestionController.duplicateError;

/**
 * FXML Controller class
 *
 * @author Team Silent Coders
 * @version 1.0
 */
public class ManageQuestionsController extends Trivia implements Initializable {

	@FXML
	private ComboBox selectQuestion;

	@FXML
	private TextField editQuestionText;

	@FXML
	private TextField editCorrectAnswer;

	@FXML
	private TextField editIncorrectAnswer1;

	@FXML
	private TextField editIncorrectAnswer2;

	@FXML
	private TextField editIncorrectAnswer3;

	@FXML
	private ToggleGroup typeGroup;

	@FXML
	private ToggleButton typeTrueFalseButton;

	@FXML
	private ToggleButton typeMultipleChoiceButton;

	@FXML
	private ToggleGroup difficultyGroup;

	@FXML
	private ToggleButton difficultyEasy;

	@FXML
	private ToggleButton difficultyHard;

	@FXML
	private Label messageLabel;

	@FXML
	private Button adminMenu;

	@FXML
	private Button deleteQuestionButton;

	@FXML
	private Button editQuestionButton;

	private final DbManager dbm = new DbManager();
	private String typeSetter = "", difficultySetter = "",
			questionText = "", correctAnswer = "", incorrectAnswer1 = "",
			incorrectAnswer2 = "", incorrectAnswer3 = "", tempStr1 = "", tempStr2 = "";
	private int currentQuestion = 0;
	private List<String> editStrings;
	private List<TextField> answerFields, editMcFields, editTfFields, selectedFields;
	private Boolean reset = false;

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
		editMcFields = Arrays.asList(editQuestionText, editCorrectAnswer,
				editIncorrectAnswer1, editIncorrectAnswer2, editIncorrectAnswer3);
		editTfFields = Arrays.asList(editQuestionText, editCorrectAnswer,
				editIncorrectAnswer1);
		editStrings = Arrays.asList(questionText, correctAnswer,
				incorrectAnswer1, incorrectAnswer2, incorrectAnswer3);
		selectedFields = editMcFields;

		adminMenu.setOnAction(this::loadView);
		editQuestionButton.setOnAction(this::confirmAlertEditQuestion);
		setComboBoxQuestions();
		// When something is selected in the ComboBox (= an event) -> print out the selection.
		selectQuestion.setOnAction(this::fillFields);
		deleteQuestionButton.setOnAction(this::confirmDeleteQuestion);
		setMessageLabel();

		typeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> ov,
					Toggle toggle, Toggle new_toggle) {
				if (!reset && new_toggle == null) {
					// Can't unselect entire ToggleGroup: keep one selected at all times.
					toggle.setSelected(true);
					disableEditButton();
				} else if (reset && new_toggle == null) {
					disableEditButton();
				} else if (new_toggle != null) {
					disableEditButton();
					difficultyEasy.setDisable(false);
					difficultyHard.setDisable(false);
					if (new_toggle == typeTrueFalseButton
							&& (difficultyEasy.isSelected() || difficultyHard.isSelected())) {
						editIncorrectAnswer2.setDisable(true);
						editIncorrectAnswer3.setDisable(true);
						tempStr1 = editIncorrectAnswer2.getText();
						tempStr2 = editIncorrectAnswer3.getText();
						editIncorrectAnswer2.setText("");
						editIncorrectAnswer3.setText("");
					} else if (new_toggle == typeMultipleChoiceButton
							&& (difficultyEasy.isSelected() || difficultyHard.isSelected())) {
						editIncorrectAnswer2.setDisable(false);
						editIncorrectAnswer3.setDisable(false);
						editIncorrectAnswer2.setText(tempStr1);
						editIncorrectAnswer3.setText(tempStr2);
					}
				}
			}
		});
		// Can't unselect entire ToggleGroup: keep one selected at all times.
		difficultyGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> ov,
					Toggle toggle, Toggle new_toggle) {
				if (!reset && new_toggle == null) {
					// Can't unselect entire ToggleGroup: keep one selected at all times.
					toggle.setSelected(true);
					disableEditButton();
				} else if (reset && new_toggle == null) {
					disableEditButton();
				} else if (new_toggle != null) {
					disableEditButton();
				}
			}
		});
		editQuestionButton.setDisable(true);
		for (TextField tf : editMcFields) {
			tf.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable,
						String oldValue, String newValue) {
					disableEditButton();
				}
			});
		}
		deleteQuestionButton.setDisable(true);
		selectQuestion.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observable,
							String OldValue, String newValue) {
						if (newValue == null) {
							deleteQuestionButton.setDisable(true);
						} else {
							deleteQuestionButton.setDisable(false);
						}
					}
				});
	}

	/**
	 * Clears all the fields and deselects all toggles.
	 */
	private void clearFields() {
		reset = true;
		for (TextField tf : editMcFields) {
			tf.setText("");
		}
		typeGroup.selectToggle(null);
		difficultyGroup.selectToggle(null);
		typeSetter = "";
		difficultySetter = "";
		selectedFields = null;
		tempStr1 = "";
		tempStr2 = "";
		editQuestionButton.setDisable(true);
		editMcFields.get(0).requestFocus();
		reset = false;
		System.out.println("Fields cleared.");
	}

	/**
	 * This method shows an alert dialog asking for confirmation for saving the
	 * edited question and its answers.
	 *
	 * @param event
	 */
	private void confirmAlertEditQuestion(ActionEvent event) {
		try {
			if ((typeTrueFalseButton.isSelected() && (editMcFields.get(0).getText().isEmpty()
					|| editMcFields.get(1).getText().isEmpty()
					|| editMcFields.get(2).getText().isEmpty()))
					|| (typeMultipleChoiceButton.isSelected()
					&& (editMcFields.get(0).getText().isEmpty()
					|| editMcFields.get(1).getText().isEmpty()
					|| editMcFields.get(2).getText().isEmpty()
					|| editMcFields.get(3).getText().isEmpty()
					|| editMcFields.get(4).getText().isEmpty()))) {
				alertDialog(Alert.AlertType.ERROR, "Tekstveld leeg", null, "Elk tekstveld moet"
						+ " zijn ingevuld en een moeilijkheidsgraad moet zijn gekozen.");
			} else if ((typeTrueFalseButton.isSelected()
					&& (editMcFields.get(0).getText().equals(editMcFields.get(1).getText())
					|| editMcFields.get(0).getText().equals(editMcFields.get(2).getText())
					|| editMcFields.get(1).getText().equals(editMcFields.get(2).getText())))
					|| (typeMultipleChoiceButton.isSelected()
					&& (editMcFields.get(0).getText().equals(editMcFields.get(1).getText())
					|| editMcFields.get(0).getText().equals(editMcFields.get(2).getText())
					|| editMcFields.get(0).getText().equals(editMcFields.get(3).getText())
					|| editMcFields.get(0).getText().equals(editMcFields.get(4).getText())
					|| editMcFields.get(1).getText().equals(editMcFields.get(2).getText())
					|| editMcFields.get(1).getText().equals(editMcFields.get(3).getText())
					|| editMcFields.get(1).getText().equals(editMcFields.get(4).getText())
					|| editMcFields.get(2).getText().equals(editMcFields.get(3).getText())
					|| editMcFields.get(2).getText().equals(editMcFields.get(4).getText())
					|| editMcFields.get(3).getText().equals(editMcFields.get(4).getText())))) {
				alertDialog(Alert.AlertType.ERROR, "Dubbele waarde", null, "Elk tekstveld moet "
						+ "een unieke invoer bevatten.");
			} else if (typeMultipleChoiceButton.isSelected()) {
				if (alertDialog(Alert.AlertType.CONFIRMATION, "Vraag toevoegen",
						"Weet u zeker dat u deze vraag wilt toevoegen?",
						"De vraag: " + editMcFields.get(0).getText() + "\n"
						+ "\nMet het juiste antwoord: " + editMcFields.get(1).getText()
						+ "\nEn de onjuiste antwoorden:\n– " + editMcFields.get(2).getText()
						+ "\n– " + editMcFields.get(3).getText() + "\n– "
						+ editMcFields.get(4).getText())) {
					editQuestion();
				}
			} else if (typeTrueFalseButton.isSelected()) {
				if (alertDialog(Alert.AlertType.CONFIRMATION, "Vraag toevoegen",
						"Weet u zeker dat u deze vraag wilt toevoegen?",
						"De vraag: " + editMcFields.get(0).getText() + "\n"
						+ "\nMet het juiste antwoord: " + editMcFields.get(1).getText()
						+ "\n\nEn het onjuiste antwoord: " + editMcFields.get(2).getText())) {
					editQuestion();
				}
			}
		} catch (NoSuchElementException e) {
			// No need to handle exception. Alert already does.
		}
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
					+ "\nSelecteer een vraag om deze te verwijderen.");
		} else if (alertDialog(AlertType.CONFIRMATION, "Vraag verwijderen", "Weet u zeker dat u"
				+ " deze vraag wilt verwijderen?", "De vraag: " + selectQuestion.getValue())) {
			deleteQuestion();
		}
		// Else -> Player selected a question, but decided to cancel deletion.
	}

	/**
	 * This method handles deleting questions from the database.
	 */
	private void deleteQuestion() {
		dbm.openConnection();
		try {
			// If no question selected: there is no question selected to delete Error Dialog.
			String countRows = "SELECT COUNT(*) FROM question";
			statement = dbm.connection.prepareStatement(countRows);
			updateParameters = Arrays.asList();
			rs = dbm.getResultSet(statement, updateParameters);

			rs.next();
			int priorToUpdate = rs.getInt(1);
			System.out.println("Questions prior to deletion: " + priorToUpdate);
			statement = dbm.connection
					.prepareStatement("DELETE FROM question WHERE Question = ?;");
			updateParameters = Arrays.asList(selectQuestion.getValue().toString());
			dbm.executeUpdate(statement, updateParameters);

			statement = dbm.connection.prepareStatement(countRows);
			updateParameters = Arrays.asList();
			rs = dbm.getResultSet(statement, updateParameters);
			rs.next();
			System.out.println("Questions after deletion: " + rs.getInt(1));

			if ((priorToUpdate - 1) == rs.getInt(1)) {
				System.out.println("Deleted question: " + selectQuestion.getValue());
				alertDialog(Alert.AlertType.INFORMATION, "Vraag verwijderen", null,
						"De vraag is succesvol verwijderd!");
			}
		} catch (SQLException e) {
			e.getLocalizedMessage();
		} finally {
			dbm.closeConnection();
			rs = null;
			statement = null;
			updateParameters = null;
		}
		// Updates the ComboBox to reflect the changes.
		setComboBoxQuestions();

		// If we get to here, the question entry and its answers have been deleted.
		clearFields();
		setMessageLabel();
	}

	private void disableEditButton() {
		if ((typeTrueFalseButton.isSelected() && (editMcFields.get(0).getText().isEmpty()
				|| editMcFields.get(1).getText().isEmpty()
				|| editMcFields.get(2).getText().isEmpty()))
				|| (typeMultipleChoiceButton.isSelected()
				&& (editMcFields.get(0).getText().isEmpty()
				|| editMcFields.get(1).getText().isEmpty()
				|| editMcFields.get(2).getText().isEmpty()
				|| editMcFields.get(3).getText().isEmpty()
				|| editMcFields.get(4).getText().isEmpty()))
				|| (editMcFields.get(0).getText().equals(editStrings.get(0))
				&& editMcFields.get(1).getText().equals(editStrings.get(1))
				&& editMcFields.get(2).getText().equals(editStrings.get(2))
				&& editMcFields.get(3).getText().equals(editStrings.get(3))
				&& editMcFields.get(4).getText().equals(editStrings.get(4))
				&& (typeGroup.getSelectedToggle().toString().contains(typeSetter))
				&& (difficultyGroup.getSelectedToggle().toString().contains(difficultySetter)))) {
			editQuestionButton.setDisable(true);
		} else {
			editQuestionButton.setDisable(false);
		}
	}

	/**
	 * This method handles adding new questions to the database.
	 */
	private void editQuestion() {
		storeStrings();

		System.out.println("QuestionId: " + currentQuestion + "\n"
				+ "Saving question as: " + editStrings.get(0) + "\n"
				+ "Saving Correct Answer as: " + editStrings.get(1) + "\n"
				+ "Saving Incorrect Answer 1 as: " + editStrings.get(2) + "\n"
				+ "Saving Incorrect Answer 2 as: " + editStrings.get(3) + "\n"
				+ "Saving Incorrect Answer 3 as: " + editStrings.get(4) + "\n"
				+ "Saving question type as: " + typeSetter + "\n"
				+ "Saving question difficulty as: " + difficultySetter);

		try {
			dbm.openConnection();
			// Create the insert script strings and execute it.
			statement = dbm.connection.prepareStatement(
					"UPDATE question SET Question = ?, GameType = ?,"
					+ " Difficulty = ? WHERE QuestionId = ?;");
			updateParameters = Arrays.asList(editStrings.get(0), typeSetter,
					difficultySetter, String.valueOf(currentQuestion));
			dbm.executeUpdate(statement, updateParameters);

			statement = dbm.connection.prepareStatement(
					"UPDATE rightanswer SET RightAnswer = ? WHERE QuestionId = ?;");
			updateParameters = Arrays.asList(editStrings.get(1), String.valueOf(currentQuestion));
			dbm.executeUpdate(statement, updateParameters);

			statement = dbm.connection.prepareStatement("UPDATE wronganswer set "
					+ "WrongAnswer = ? WHERE QuestionId = ? AND WrongAnswerId = 1");
			updateParameters = Arrays.asList(editStrings.get(2), String.valueOf(currentQuestion));
			dbm.executeUpdate(statement, updateParameters);

			statement = dbm.connection.prepareStatement("UPDATE wronganswer set "
					+ "WrongAnswer = ? WHERE QuestionId = ? AND WrongAnswerId = 2");
			updateParameters = Arrays.asList(editStrings.get(3), String.valueOf(currentQuestion));
			dbm.executeUpdate(statement, updateParameters);

			statement = dbm.connection.prepareStatement("UPDATE wronganswer set "
					+ "WrongAnswer = ? WHERE QuestionId = ? AND WrongAnswerId = 3");
			updateParameters = Arrays.asList(editStrings.get(4), String.valueOf(currentQuestion));
			dbm.executeUpdate(statement, updateParameters);
		} catch (SQLException e) {
			System.out.println("Error: " + e.getLocalizedMessage());
		} finally {
			dbm.closeConnection();
			statement = null;
			updateParameters = null;
		}
		// System check of input.
		if (duplicateError) { // <<—————— DOUBLE CHECK WHETHER THIS EVEN WORKS.
			rs = null;
			statement = null;
			updateParameters = null;

			editMcFields.get(0).requestFocus();
			duplicateError = false;
			return;
		}

		// Updates the ComboBox to reflect the changes.
		setComboBoxQuestions();
		selectQuestion.setValue(null);

		// If we get to here, the question entry and its answers have been updated.
		clearFields();
		alertDialog(Alert.AlertType.INFORMATION, "Vraag bewerken", null,
				"De vraag is succesvol aangepast!");
	}

	/**
	 * Populates all fields and the right toggle if a question has been
	 * selected.
	 */
	private void fillFields(Event event) {
		clearFields();
		try {
			if (selectQuestion.getValue() == null) { // If no question has been selected.
				selectedFields = editMcFields;
				toggleFields(true); // Disable all fields and ToggleButtons.
				return; // And don't try to fill/select them (aborts this method).
			}
		} catch (NullPointerException e) {
			System.err.println("Error: " + e.getLocalizedMessage()); // Hurr durr I exception.
		}

		int i = 1;
		try {
			dbm.openConnection();
			statement = dbm.connection.prepareStatement(
					"SELECT r.RightAnswer, w.WrongAnswer, w2.WrongAnswer, "
					+ "w3.WrongAnswer, q.GameType, q.Difficulty, q.QuestionId FROM question q "
					+ "INNER JOIN rightanswer r ON q.QuestionId = r.QuestionId "
					+ "INNER JOIN wronganswer w ON w.QuestionId = q.QuestionId "
					+ "INNER JOIN wronganswer w2 ON w2.QuestionId = q.QuestionId "
					+ "INNER JOIN wronganswer w3 ON w3.QuestionId = q.QuestionId "
					+ "WHERE w.WrongAnswerId = 1 AND w2.WrongAnswerId = 2 AND "
					+ "w3.WrongAnswerId = 3 AND Question = ?;");
			System.out.println("Player selected question: " + (updateParameters
					= Arrays.asList(selectQuestion.getValue().toString())).get(0));

			rs = dbm.getResultSet(statement, updateParameters); // Get everything.
			rs.next();
			editMcFields.get(0).setText(updateParameters.get(0)); // Fill the question field.

			for (TextField tf : answerFields) { // Fill the answer fields.
				tf.setText(rs.getString(i++));
			}
			if (rs.getString(i).contains("TrueFalse")) { // Select the right type toggle.
				typeTrueFalseButton.setSelected(true);
			} else if (rs.getString(i).contains("MultipleChoice")) {
				typeMultipleChoiceButton.setSelected(true);
			}
			if (rs.getString(++i).contains("Easy")) { // Select the right difficulty toggle.
				difficultyEasy.setSelected(true);
			} else if (rs.getString(i).contains("Hard")) {
				difficultyHard.setSelected(true);
			}

			currentQuestion = rs.getInt(i + 1); // Store QuestionId in case of edit.
			storeStrings();
		} catch (SQLException e) {
			System.err.println("Error: " + e.getLocalizedMessage());
			toggleFields(true); // Something went wrong, disable all fields.
		} finally {
			dbm.closeConnection();
			rs = null;
			statement = null;
			updateParameters = null;
		}
		toggleFields(false); // Enable the necessary fields.
		disableEditButton();
	}

	/**
	 * This populates the ComboBox with all the questions in the database.
	 */
	private void setComboBoxQuestions() {
		// Make a new list with questions for the combobox.
		ArrayList list = new ArrayList();

		int questionsTotal = 0;
		try {
			dbm.openConnection();
			statement = dbm.connection.prepareStatement(
					"SELECT QuestionId FROM question ORDER by QuestionId DESC LIMIT 1;");
			rs = dbm.getResultSet(statement);
			while (rs.next()) {
				questionsTotal = rs.getInt(1);
			}
			for (int i = 0; i <= questionsTotal; i++) {
				statement = dbm.connection.prepareStatement(
						"SELECT Question FROM question WHERE QuestionId = ?;");
				updateParameters = Arrays.asList(String.valueOf(i));

				// Add the results of the 'query' to the ArrayList list.
				rs = dbm.getResultSet(statement, updateParameters);
				while (rs.next()) {
					list.add(rs.getString(1));
				}
			}
			// Test the list by showcasing it.
//			System.out.println("List of questions: " + list + "\n"
//					+ "Highest questionId: " + questionsTotal);

			// Cast ArrayList to ObservableList from
			// stackoverflow.com/questions/22191954/javafx-casting-arraylist-to-observablelist
			Collections.sort(list);
			ObservableList questions = FXCollections.observableArrayList(list);

			// Set the questions in the ComboBox selectQuestion
			selectQuestion.setItems(questions);
		} catch (SQLException | NumberFormatException e) {
			System.err.println("Error: " + e.getLocalizedMessage());
		} finally {
			dbm.closeConnection();
			rs = null;
			statement = null;
			updateParameters = null;
		}
	}

	private void setMessageLabel() {
		try {
			dbm.openConnection();
			statement = dbm.connection
					.prepareStatement("SELECT COUNT(*) FROM question;");
			rs = dbm.getResultSet(statement);
			rs.next();
			messageLabel.setText("Aantal vragen: " + rs.getString(1));
		} catch (SQLException e) {
			System.err.println("Error: " + e.getLocalizedMessage());
		} finally {
			dbm.closeConnection();
			rs = null;
			statement = null;
		}
	}

	private void storeStrings() {
		// If the Toggle's properties contain "TF", set as TF, otherwise MC.
		typeSetter = typeGroup.getSelectedToggle().toString().
				contains("TrueFalse") ? "TrueFalse" : "MultipleChoice";

		// If the Toggle's properties contain "Easy" set as Easy, otherwise Hard.
		difficultySetter = difficultyGroup.getSelectedToggle().
				toString().contains("Easy") ? "Easy" : "Hard";

		// Collect the Strings with getText from the selected textField.
		int i = -1;
		selectedFields = typeSetter.contains("TrueFalse") ? editTfFields : editMcFields;
		for (TextField tf : selectedFields) {
			// Make sure all strings start with an uppercase letter.
			System.out.println("Initiated at row: " + ++i);
			if (!tf.getText().isEmpty() && Character.isLetter(tf.getText().charAt(0))) {
				Character.toUpperCase(tf.getText().charAt(0));
				editStrings.set(i, Character.toUpperCase(tf.getText().charAt(0))
						+ tf.getText().substring(1));
			} else {
				editStrings.set(i, tf.getText());
			}
		}
		if (selectedFields == editTfFields) { // Also store the §§correct strings if empty.
			editStrings.set(3, "");
			editStrings.set(4, "");
		}
		if (!editStrings.get(0).endsWith("?")) { // Make sure all questions end with ?.
			editStrings.set(0, editStrings.get(0).concat("?"));
		}
	}

	/**
	 * If no question is selected in ComboBox, disable all fields & vice versa.
	 *
	 * @param b
	 */
	private void toggleFields(Boolean b) {
		for (TextField tf : selectedFields) {
			tf.setDisable(b);
		}
		if (selectedFields == editTfFields && b == false) {
			editIncorrectAnswer2.setDisable(!b);
			editIncorrectAnswer3.setDisable(!b);
		}
		for (Toggle t : typeGroup.getToggles()) {
			((ToggleButton) t).setDisable(b);
		}
		for (Toggle t : difficultyGroup.getToggles()) {
			((ToggleButton) t).setDisable(b);
		}
		selectQuestion.requestFocus();
	}

}
