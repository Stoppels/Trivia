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
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import trivia.Trivia;
import static trivia.Trivia.statement;
import trivia.connectivity.DbManager;

/**
 * FXML Controller class
 *
 * @author Team Silent Coders
 * @version 1.0
 */
public class HighScoreController extends Trivia implements Initializable {

	@FXML
	private Label scoreLabel1;

	@FXML
	private Label scoreLabel2;

	@FXML
	private Label scoreLabel3;

	@FXML
	private Label scoreLabel4;

	@FXML
	private Label scoreLabel5;

	@FXML
	private Label scoreLabel6;

	@FXML
	private Label scoreLabel7;

	@FXML
	private Label scoreLabel8;

	@FXML
	private Label scoreLabel9;

	@FXML
	private Label scoreLabel10;

	@FXML
	private Label nameLabel1;

	@FXML
	private Label nameLabel2;

	@FXML
	private Label nameLabel3;

	@FXML
	private Label nameLabel4;

	@FXML
	private Label nameLabel5;

	@FXML
	private Label nameLabel6;

	@FXML
	private Label nameLabel7;

	@FXML
	private Label nameLabel8;

	@FXML
	private Label nameLabel9;

	@FXML
	private Label nameLabel10;

	@FXML
	private Label dateLabel1;

	@FXML
	private Label dateLabel2;

	@FXML
	private Label dateLabel3;

	@FXML
	private Label dateLabel4;

	@FXML
	private Label dateLabel5;

	@FXML
	private Label dateLabel6;

	@FXML
	private Label dateLabel7;

	@FXML
	private Label dateLabel8;

	@FXML
	private Label dateLabel9;

	@FXML
	private Label dateLabel10;

	@FXML
	public static Label playerStringLabel;

	@FXML
	public static Label playerScoreLabel;
	
	@FXML
	private Button mainMenu;

	private final DbManager dbm = new DbManager();
	private List<Label> highScoreScores, highScoreNames, highScoreDates;
	private String[][] loadedHighScores = new String[10][3];

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */
	@Override

	public void initialize(URL url, ResourceBundle rb) {
		mainMenu.setOnAction(this::loadView);
		highScoreScores = Arrays.asList(scoreLabel1, scoreLabel2, scoreLabel3, scoreLabel4,
				scoreLabel5, scoreLabel6, scoreLabel7, scoreLabel8, scoreLabel9, scoreLabel10);
		highScoreNames = Arrays.asList(nameLabel1, nameLabel2, nameLabel3, nameLabel4,
				nameLabel5, nameLabel6, nameLabel7, nameLabel8, nameLabel9, nameLabel10);
		highScoreDates = Arrays.asList(dateLabel1, dateLabel2, dateLabel3, dateLabel4,
				dateLabel5, dateLabel6, dateLabel7, dateLabel8, dateLabel9, dateLabel10);
		loadHighScore();

//		if (skippedNameEntry) {
//			playerStringLabel.setVisible(false);
//			playerScoreLabel.setVisible(false);
//		}
	}

	public void loadHighScore() {
		try {
			dbm.openConnection();

			statement = dbm.connection.prepareStatement("SELECT HighScore, UserName, Date "
					+ "FROM highscore ORDER BY HighScore DESC LIMIT 10;");
			rs = dbm.getResultSet(statement);

			int i = 0;
			while (rs.next()) { // Save everything.
				for (int j = 1; j < 4; j++) {
					loadedHighScores[i][j - 1] = rs.getString(j);
					System.out.println(rs.getString(j));
				}
				i++;

			}
			i = 0;
			for (Label l1 : highScoreScores) {
				l1.setText(loadedHighScores[i][0]);
				i++;
			}
			i = 0;
			for (Label l2 : highScoreNames) {
				l2.setText(loadedHighScores[i][1]);
				i++;
			}
			i = 0;
			for (Label l3 : highScoreDates) {
				l3.setText(loadedHighScores[i][2]);
				i++;
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getLocalizedMessage());
		} finally {
			dbm.closeConnection();
			rs = null;
			statement = null;
		}
	}

}
