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
package trivia;

import connectivity.DbManager;
import connectivity.QueryManager;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.StageStyle;
import static trivia.GameSetUpController.makkelijkHolder;

/**
 * FXML Controller class
 *
 * @author Team Silent Coders
 * @version 1.0
 */
public class QuestionController extends Trivia implements Initializable {

	@FXML
	Button previousQuestion;

	@FXML
	Button nextQuestion;

	@FXML
	Label timer;

	@FXML
	ProgressBar progressBar;

	@FXML
	Label vraag;

	@FXML
	Label LabelA;

	@FXML
	Button ButtonA;

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		setVraag();
		setAwnserA();
        // TODO

		// if current answer > 10, scoreCheck()
		// if int current question > 1 AND previousQuestion is disabled, enable previousQuestion
		// else if int current question < 2, disable previousQuestion
	}

	/**
	 * I don't work yet, please fix me //////////////////////////////////
	 */
	@FXML
	public void checkVraagSettings() {
		if (GameSetUpController.makkelijkHolder) {
			System.out.println("yo");
		}
		if (makkelijkHolder) {
			System.out.println("yo2");
		}
		//enable timer? timerCountdown()
		//other settings?
	}

	@FXML
	private void previousQuestion() {

		saveAnswer();
		//goto current -1
	}

	@FXML
	private void nextQuestion() {
		saveAnswer();
		//goto current + 1
	}

	@FXML
	private void saveAnswer() {
		//remember chosen answer;
	}

	@FXML
	private void progressChecker() {
		//progressBar;
	}

	@FXML
	private void timerCountdown() {
		//countdown
		//remember remaining time per question?
		//show warning if no time remaining but user went back to question?
	}

	@FXML
	private void scoreCheck() {
		//analyze savedanswers
	}

	@FXML
	private void stopQuiz() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Stop quiz");
		alert.setHeaderText("Weet u zeker dat u de quiz wilt stoppen?");
		alert.setContentText("De antwoorden worden niet opgeslagen.\nDit brengt u terug naar het hoofdmenu.");
		alert.initStyle(StageStyle.UNDECORATED);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			loadView("MainMenu");
		}
	}

	//Random integer genereren voor random vraag
	Random rand = new Random();
	int VraagID = rand.nextInt(4);

	private void setVraag() {

		// object om connectie aan te roepen
		DbManager dbm = new DbManager();
		QueryManager qm = new QueryManager(dbm);

		//open database connection
		dbm.openConnection();

		String sql = "SELECT Vraag FROM vraag WHERE VraagID =" + VraagID + ";";

		System.out.println(sql);

		try {
			ResultSet result = dbm.doQuery(sql);
			result.next();
			vraag.setText(result.getString("Vraag"));

		} catch (SQLException e) {

			System.out.println("FOUT" + e.getMessage());
		}
	}

	private void setAwnserA() {

		// object om connectie aan te roepen
		DbManager dbm = new DbManager();
		QueryManager qm = new QueryManager(dbm);

		//open database connection
		dbm.openConnection();

		String sql = "SELECT AntwoordFout FROM antwoordfout INNER JOIN vraag ON antwoordfout.VraagID = vraag.VraagID WHERE AntwoordFoutID =" + 1 + " AND vraag.VraagID = " + VraagID + ";";

		System.out.println(sql);

		try {
			ResultSet result = dbm.doQuery(sql);
			result.next();
			LabelA.setText(result.getString("AntwoordFout"));

		} catch (SQLException e) {

			System.out.println("FOUT" + e.getMessage());
		}
	}
}
