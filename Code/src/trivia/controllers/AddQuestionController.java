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
	private String difficultySetter = "",
			question = "", correctAnswer = "", incorrectAnswer1 = "",
			incorrectAnswer2 = "", incorrectAnswer3 = "";
	public List<String> addStrings;
	public List<TextField> addFields;
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
		addFields = Arrays.asList(addQuestionText, addCorrectAnswer,
				addIncorrectAnswer1, addIncorrectAnswer2, addIncorrectAnswer3);

		adminMenu.setOnAction(this::loadView);
		addQuestionButton.setOnAction(this::confirmAlertAddQuestion);

		// Can't unselect entire ToggleGroup: keep one selected at all times.
		difficultyGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> ov,
					Toggle toggle, Toggle new_toggle) {
				if (!reset && new_toggle == null) {
					toggle.setSelected(true);
					disableAddButton();
				} else if (reset && new_toggle == null) {
					disableAddButton();
				} else if (new_toggle != null) {
					disableAddButton();
				}
			}
		});
		addQuestionButton.setDisable(true);
		for (TextField tf : addFields) {
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
		difficultySetter = difficultyGroup.getSelectedToggle().
				toString().contains("Easy") ? "Easy" : "Hard";
		// Collect the Strings with getText from the selected textField.
		int i = -1;
		for (TextField tf : addFields) {
			// Make sure all strings start with an uppercase letter.
			System.out.println("Initiated at row: " + ++i);
			if (Character.isLetter(tf.getText().charAt(0))) {
				addStrings.set(i, Character.toUpperCase(tf.getText().charAt(0))
						+ tf.getText().substring(1));
			} else {
				addStrings.set(i, tf.getText());
			}
		}

		try {
			dbm.openConnection();
			statement = dbm.connection
					.prepareStatement("INSERT INTO question VALUES(NULL, ?, ?);");
			updateParameters = Arrays.asList(addStrings.get(0), difficultySetter);
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

			addFields.get(0).requestFocus();
			duplicateError = false;
			return;
		}
		System.out.println("Adding question: " + addStrings.get(0) + "\n"
				+ "Adding Correct Answer: " + addStrings.get(1) + "\n"
				+ "Adding Incorrect Answer 1: " + addStrings.get(2) + "\n"
				+ "Adding Incorrect Answer 2: " + addStrings.get(3) + "\n"
				+ "Adding Incorrect Answer 3: " + addStrings.get(4) + "\n"
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
			updateParameters = Arrays.asList(questionId, addStrings.get(1), questionId);
			dbm.executeUpdate(statement, updateParameters);

			statement = dbm.connection.prepareStatement(
					"INSERT INTO wronganswer VALUES(1, ?, ?), (2, ?, ?), (3, ?, ?);");
			updateParameters = Arrays.asList(addStrings.get(2), questionId,
					addStrings.get(3), questionId, addStrings.get(4), questionId);
			dbm.executeUpdate(statement, updateParameters);

			// If we get to here, everything is stored in the database; clear fields.
			clearFields();

			alertDialog(Alert.AlertType.INFORMATION, "Vraag toevoegen", null,
					"De vraag is succesvol toegevoegd!", StageStyle.UNDECORATED);
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
		for (TextField tf : addFields) {
			tf.setText("");
		}
		difficultyGroup.selectToggle(null);
		addFields.get(0).requestFocus();
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
			if (addFields.get(0).getText().isEmpty() || addFields.get(1).getText().isEmpty()
					|| addFields.get(2).getText().isEmpty()
					|| addFields.get(3).getText().isEmpty()
					|| addFields.get(4).getText().isEmpty()
					|| (!difficultyEasy.isSelected() && !difficultyHard.isSelected())) {
				alertDialog(Alert.AlertType.ERROR, "Invoerveld leeg", null, "Elk tekstveld moet "
						+ "zijn ingevuld en een moeilijkheidsgraad gekozen.", StageStyle.UNDECORATED);
			} else if (addFields.get(0).getText().equals(addFields.get(1).getText())
					|| addFields.get(0).getText().equals(addFields.get(2).getText())
					|| addFields.get(0).getText().equals(addFields.get(3).getText())
					|| addFields.get(0).getText().equals(addFields.get(4).getText())
					|| addFields.get(1).getText().equals(addFields.get(2).getText())
					|| addFields.get(1).getText().equals(addFields.get(3).getText())
					|| addFields.get(1).getText().equals(addFields.get(4).getText())
					|| addFields.get(2).getText().equals(addFields.get(3).getText())
					|| addFields.get(2).getText().equals(addFields.get(4).getText())
					|| addFields.get(3).getText().equals(addFields.get(4).getText())) {
				alertDialog(Alert.AlertType.ERROR, "Dubbele waarde", null, "Elk tekstveld moet "
						+ "een unieke invoer bevatten.", StageStyle.UNDECORATED);
			} else if (alertDialog(Alert.AlertType.CONFIRMATION, "Vraag toevoegen",
					"Weet u zeker dat u deze vraag wilt toevoegen?",
					"De vraag: " + addFields.get(0).getText()
					+ "\nMet het juiste antwoord: " + addFields.get(1).getText()
					+ "\nEn de onjuiste antwoorden:\n– " + addFields.get(2).getText()
					+ "\n– " + addFields.get(3).getText() + "\n– " + addFields.get(4).getText(),
					StageStyle.UNDECORATED)) {
				addQuestion();
			}
		} catch (NoSuchElementException e) {
			System.err.println(e.getLocalizedMessage());
			// No need to handle exception
		}
	}

	private void disableAddButton() {
		if (addFields.get(0).getText().isEmpty() || addFields.get(1).getText().isEmpty()
				|| addFields.get(2).getText().isEmpty() || addFields.get(3).getText().isEmpty()
				|| addFields.get(4).getText().isEmpty()
				|| (!difficultyEasy.isSelected() && !difficultyHard.isSelected())) {
			addQuestionButton.setDisable(true);
		} else {
			addQuestionButton.setDisable(false);
		}
	}

}
