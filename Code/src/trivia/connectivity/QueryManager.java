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
package trivia.connectivity;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Hyperlink;
import trivia.controllers.GameSetUpController;

/**
 *
 * @author Team Silent Coders
 * @version 1.0
 */
public class QueryManager {

	// Create a database manager object for handling the database connection
	private final DbManager dbm;
	private ResultSet result = null;
	private String sql = "";

	/**
	 *
	 * @param dbm
	 */
	public QueryManager(DbManager dbm) {
		this.dbm = dbm;
	}

	/**
	 * Selects a randomized set of questions, based on the gameLength setting.
	 *
	 * @param questionsCount
	 * @return
	 */
	public int[] selectQuestions(int questionsCount) {
		dbm.openConnection();
		int[] questionsHolder = new int[questionsCount];
		int i = 1;
		sql = "SELECT QuestionId FROM question ORDER BY RAND() LIMIT " + questionsCount + ";";
		result = dbm.doQuery(sql);
		try {
			while (result.next()) {
				System.out.println("Fetched QuestionId: " + result.getInt(1));
				questionsHolder[i - 1] = result.getInt(1);
				i++;
			}
			return questionsHolder;
		} catch (SQLException e) {
			e.getLocalizedMessage();
			return null;
		} finally {
			dbm.closeConnection();
			result = null;
			sql = "";
		}
	}

	/**
	 * Fetches the Question from the database, based on the difficulty setting.
	 *
	 * @param QuestionId
	 * @return the string of the query
	 */
	public String setQuestion(int QuestionId) {
		String difficulty = " AND Difficulty = '";
		difficulty += GameSetUpController.difficultyIsEasy ? "Easy'" : "Hard'";
		if (GameSetUpController.difficultyIsMixed) {
			difficulty = "";
		}

		dbm.openConnection();
		sql = "SELECT Question FROM question WHERE QuestionId = " + QuestionId
				+ difficulty + ";";
		System.out.println(sql);
		String returnString = null;

		try {
			result = dbm.doQuery(sql);
			result.next();
			returnString = result.getString("Question");
		} catch (SQLException e) {
			System.err.println("Error: " + e.getLocalizedMessage());
		} finally {
			dbm.closeConnection();
			result = null;
			sql = "";
		}
		return returnString;
	}

	/**
	 * Fetches the RightAnswer from the database and sets it as the labelNumber
	 * text.
	 *
	 * @param labelNumber
	 * @param QuestionId
	 */
	public void setRightAnswer(Hyperlink labelNumber, int QuestionId) {

		QueryManager qm = new QueryManager(dbm);

		// Open database connection
		dbm.openConnection();
		sql = "SELECT RightAnswer FROM rightanswer WHERE QuestionId = " + QuestionId + ";";
		System.out.println(sql);

		try {
			result = dbm.doQuery(sql);
			result.next();
			labelNumber.setText(result.getString("RightAnswer"));
		} catch (SQLException e) {
			System.err.println("Error: " + e.getLocalizedMessage());
		} finally {
			dbm.closeConnection();
			result = null;
			sql = "";
		}
	}

	/**
	 * Fetches the WrongAnswer from the database and sets it as the labelNumber
	 * text.
	 *
	 * @param wrongAnswerId
	 * @param labelNumber
	 * @param QuestionId
	 */
	public void setWrongAnswer(int wrongAnswerId, Hyperlink labelNumber, int QuestionId) {

		// Open database connection
		dbm.openConnection();
		sql = "SELECT WrongAnswer FROM wronganswer INNER JOIN question ON"
				+ " wronganswer.QuestionId = question.QuestionId WHERE wrongAnswerId = "
				+ wrongAnswerId + " AND question.QuestionId = " + QuestionId + ";";
		System.out.println(sql);

		try {
			result = dbm.doQuery(sql);
			result.next();
			labelNumber.setText(result.getString("WrongAnswer"));
		} catch (SQLException e) {
			System.err.println("Error: " + e.getLocalizedMessage());
		} finally {
			dbm.closeConnection();
			result = null;
			sql = "";
		}
	}
}
