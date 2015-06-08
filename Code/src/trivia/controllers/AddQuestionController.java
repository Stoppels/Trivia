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
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
	private Button addQuestionButton;

	private final DbManager dbm = new DbManager();
	private String typeSetter = "", difficultySetter = "",
			question = "", correctAnswer = "", incorrectAnswer1 = "",
			incorrectAnswer2 = "", incorrectAnswer3 = "", tempStr1 = "", tempStr2 = "";
	public List<String> addStrings;
	public List<TextField> addMcFields, addTfFields, selectedFields;
	private Boolean reset = false;

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		addStrings = Arrays.asList(question, correctAnswer,
				incorrectAnswer1, incorrectAnswer2, incorrectAnswer3);
		addMcFields = Arrays.asList(addQuestionText, addCorrectAnswer,
				addIncorrectAnswer1, addIncorrectAnswer2, addIncorrectAnswer3);
		addTfFields = Arrays.asList(addQuestionText, addCorrectAnswer, addIncorrectAnswer1);

		adminMenu.setOnAction(this::loadView);
		addQuestionButton.setOnAction(this::confirmAlertAddQuestion);
		setMessageLabel();

		typeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> ov,
					Toggle toggle, Toggle new_toggle) {
				if (!reset && new_toggle == null) {
					// Can't unselect entire ToggleGroup: keep one selected at all times.
					toggle.setSelected(true);
					disableAddButton();
				} else if (reset && new_toggle == null) {
					disableAddButton();
				} else if (new_toggle != null) {
					disableAddButton();
					difficultyEasy.setDisable(false);
					difficultyHard.setDisable(false);
					if (new_toggle == typeTrueFalseButton
							&& (difficultyEasy.isSelected() || difficultyHard.isSelected())) {
						addIncorrectAnswer2.setDisable(true);
						addIncorrectAnswer3.setDisable(true);
						tempStr1 = addIncorrectAnswer2.getText();
						tempStr2 = addIncorrectAnswer3.getText();
						addIncorrectAnswer2.setText("");
						addIncorrectAnswer3.setText("");
					} else if (new_toggle == typeMultipleChoiceButton
							&& (difficultyEasy.isSelected() || difficultyHard.isSelected())) {
						addIncorrectAnswer2.setDisable(false);
						addIncorrectAnswer3.setDisable(false);
						addIncorrectAnswer2.setText(tempStr1);
						addIncorrectAnswer3.setText(tempStr2);
					}
				}
			}
		});
		difficultyGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> ov,
					Toggle toggle, Toggle new_toggle) {
				if (!reset && new_toggle == null) {
					// Can't unselect entire ToggleGroup: keep one selected at all times.
					toggle.setSelected(true);
					disableAddButton();
				} else if (reset && new_toggle == null) {
					disableAddButton();
				} else if (new_toggle != null) {
					disableAddButton();
					if (typeTrueFalseButton.isSelected() || typeMultipleChoiceButton.isSelected()) {
						addQuestionText.setDisable(false);
						addCorrectAnswer.setDisable(false);
						addIncorrectAnswer1.setDisable(false);
						if (typeTrueFalseButton.isSelected()) {
							addIncorrectAnswer2.setDisable(true);
							addIncorrectAnswer3.setDisable(true);
						} else if (typeMultipleChoiceButton.isSelected()) {
							addIncorrectAnswer2.setDisable(false);
							addIncorrectAnswer3.setDisable(false);
						}
					}
				}
			}
		});
		for (TextField tf : addMcFields) {
			tf.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable,
						String oldValue, String newValue) {
					disableAddButton();
				}
			});
		}
	}

	/**
	 * This method handles adding new questions to the database.
	 */
	private void addQuestion() {
		// If the Toggle's properties contains "Easy" set as Easy, otherwise Hard.
		typeSetter = typeGroup.getSelectedToggle().toString().
				contains("TrueFalse") ? "TrueFalse" : "MultipleChoice";
		System.out.println(typeSetter);
		// If the Toggle's properties contains "Easy" set as Easy, otherwise Hard.
		difficultySetter = difficultyGroup.getSelectedToggle().
				toString().contains("Easy") ? "Easy" : "Hard";
		// Collect the Strings with getText from the selected textField.
		int i = -1;
		selectedFields = typeSetter.contains("TrueFalse") ? addTfFields : addMcFields;
		for (TextField tf : selectedFields) {
			// Make sure all strings start with an uppercase letter.
			System.out.println("Initiated at row: " + ++i);
			// Change all first characters to uppercase if they're letters.
			if (!tf.getText().isEmpty() && Character.isLetter(tf.getText().charAt(0))) {
				addStrings.set(i, Character.toUpperCase(tf.getText().charAt(0))
						+ tf.getText().substring(1));
			} else {
				addStrings.set(i, tf.getText());
			}
			if (!addStrings.get(0).endsWith("?")) {
				addStrings.set(0, addStrings.get(0).concat("?"));
			}
		}

		try {
			dbm.openConnection();
			statement = dbm.connection
					.prepareStatement("INSERT INTO question VALUES(NULL, ?, ?, ?);");
			updateParameters = Arrays.asList(addStrings.get(0), typeSetter, difficultySetter);
			dbm.executeUpdate(statement, updateParameters);
		} catch (SQLException e) {
			System.err.println("Error: " + e.getLocalizedMessage());
		}

		// System check of input.
		if (duplicateError) {
			dbm.closeConnection();
			rs = null;
			statement = null;
			updateParameters = null;

			addMcFields.get(0).requestFocus();
			duplicateError = false;
			return;
		}
		System.out.println("Adding question: " + addStrings.get(0) + "\n"
				+ "Adding Correct Answer: " + addStrings.get(1) + "\n"
				+ "Adding Incorrect Answer 1: " + addStrings.get(2));
		if (typeMultipleChoiceButton.isSelected()) {
			System.out.println("Adding Incorrect Answer 2: " + addStrings.get(3) + "\n"
					+ "Adding Incorrect Answer 3: " + addStrings.get(4));
		}
		System.out.println("Setting question type as: " + typeSetter + "\n"
				+ "Setting question difficulty: " + difficultySetter);

		try {
			//Get the QuestionId from the ResultSet.
			statement = dbm.connection.prepareStatement(
					"SELECT QuestionId FROM question WHERE Question = ?;");
			updateParameters = Arrays.asList(addStrings.get(0));
			rs = dbm.getResultSet(statement, updateParameters);
			rs.next();
			String questionId;
			System.out.println(questionId = rs.getString("QuestionId"));

			statement = dbm.connection.prepareStatement(
					"INSERT INTO rightanswer VALUES(?, ?, ?);");
			updateParameters = Arrays.asList(questionId, questionId, addStrings.get(1));
			dbm.executeUpdate(statement, updateParameters);

			statement = dbm.connection.prepareStatement(
					"INSERT INTO wronganswer VALUES(?, 1, ?), (?, 2, ?), (?, 3, ?);");
			updateParameters = Arrays.asList(questionId, addStrings.get(2),
					questionId, addStrings.get(3), questionId, addStrings.get(4));
			dbm.executeUpdate(statement, updateParameters);

			// If we get to here, everything is stored in the database; clear fields.
			clearFields();

			alertDialog(Alert.AlertType.INFORMATION, "Vraag toevoegen", null,
					"De vraag is succesvol toegevoegd!", StageStyle.UNDECORATED);
			setMessageLabel();
		} catch (SQLException e) {
			System.err.println("Error: " + e.getLocalizedMessage());
		} finally {
			dbm.closeConnection();
			rs = null;
			statement = null;
			updateParameters = null;
		}
	}

	private void clearFields() {
		reset = true;
		for (TextField tf : addMcFields) {
			tf.setText("");
			tf.setDisable(true);
		}
		typeGroup.selectToggle(null);
		difficultyGroup.selectToggle(null);
		selectedFields = null;
		tempStr1 = "";
		tempStr2 = "";
		addMcFields.get(0).requestFocus();
		reset = false;
	}

	/**
	 * This method shows an alert dialog asking for confirmation for adding the
	 * entered question and its answers.
	 *
	 * @param event
	 */
	private void confirmAlertAddQuestion(ActionEvent event) {
		try {
			if ((typeTrueFalseButton.isSelected() && (addMcFields.get(0).getText().isEmpty()
					|| addMcFields.get(1).getText().isEmpty()
					|| addMcFields.get(2).getText().isEmpty())) || (typeMultipleChoiceButton.isSelected()
					&& (addMcFields.get(0).getText().isEmpty()
					|| addMcFields.get(1).getText().isEmpty()
					|| addMcFields.get(2).getText().isEmpty()
					|| addMcFields.get(3).getText().isEmpty()
					|| addMcFields.get(4).getText().isEmpty()))) {
				alertDialog(Alert.AlertType.ERROR, "Invoerveld leeg", null, "Elk "
						+ "tekstveld moet zijn ingevuld en een moeilijkheidsgraad"
						+ " gekozen.", StageStyle.UNDECORATED);
			} else if ((typeTrueFalseButton.isSelected()
					&& (addMcFields.get(0).getText().equals(addMcFields.get(1).getText())
					|| addMcFields.get(0).getText().equals(addMcFields.get(2).getText())
					|| addMcFields.get(1).getText().equals(addMcFields.get(2).getText())))
					|| (typeMultipleChoiceButton.isSelected()
					&& (addMcFields.get(0).getText().equals(addMcFields.get(1).getText())
					|| addMcFields.get(0).getText().equals(addMcFields.get(2).getText())
					|| addMcFields.get(0).getText().equals(addMcFields.get(3).getText())
					|| addMcFields.get(0).getText().equals(addMcFields.get(4).getText())
					|| addMcFields.get(1).getText().equals(addMcFields.get(2).getText())
					|| addMcFields.get(1).getText().equals(addMcFields.get(3).getText())
					|| addMcFields.get(1).getText().equals(addMcFields.get(4).getText())
					|| addMcFields.get(2).getText().equals(addMcFields.get(3).getText())
					|| addMcFields.get(2).getText().equals(addMcFields.get(4).getText())
					|| addMcFields.get(3).getText().equals(addMcFields.get(4).getText())))) {
				alertDialog(Alert.AlertType.ERROR, "Dubbele waarde", null, "Elk tekstveld"
						+ " moet een unieke invoer bevatten.", StageStyle.UNDECORATED);
			} else if (typeMultipleChoiceButton.isSelected()) {
				if (alertDialog(Alert.AlertType.CONFIRMATION, "Vraag toevoegen",
						"Weet u zeker dat u deze vraag wilt toevoegen?",
						"De vraag: " + addMcFields.get(0).getText() + "\n"
						+ "\nMet het juiste antwoord: " + addMcFields.get(1).getText()
						+ "\nEn de onjuiste antwoorden:\n– " + addMcFields.get(2).getText()
						+ "\n– " + addMcFields.get(3).getText() + "\n– "
						+ addMcFields.get(4).getText(), StageStyle.UNDECORATED)) {
					addQuestion();
				}
			} else if (typeTrueFalseButton.isSelected()) {
				if (alertDialog(Alert.AlertType.CONFIRMATION, "Vraag toevoegen",
						"Weet u zeker dat u deze vraag wilt toevoegen?",
						"De vraag: " + addMcFields.get(0).getText() + "\n"
						+ "\nMet het juiste antwoord: " + addMcFields.get(1).getText()
						+ "\n\nEn het onjuiste antwoord: " + addMcFields.get(2).getText(),
						StageStyle.UNDECORATED)) {
					addQuestion();
				}
			}
		} catch (NoSuchElementException e) {
			System.err.println(e.getLocalizedMessage());
			// No need to handle exception
		}
	}

	/**
	 * Method that disables the addQuestionButton if any of the necessary fields
	 * is empty.
	 */
	private void disableAddButton() {
		if ((typeTrueFalseButton.isSelected() && (addMcFields.get(0).getText().isEmpty()
				|| addMcFields.get(1).getText().isEmpty() || addMcFields.get(2).getText().isEmpty()))
				|| (typeMultipleChoiceButton.isSelected() && (addMcFields.get(0).getText().isEmpty()
				|| addMcFields.get(1).getText().isEmpty() || addMcFields.get(2).getText().isEmpty()
				|| addMcFields.get(3).getText().isEmpty() || addMcFields.get(4).getText().isEmpty()))) {
			addQuestionButton.setDisable(true);
		} else {
			addQuestionButton.setDisable(false);
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
}
