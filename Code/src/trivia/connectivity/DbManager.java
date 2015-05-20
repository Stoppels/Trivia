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

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.*;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
import static trivia.Trivia.alertDialog;
import trivia.controllers.AddQuestionController;

/**
 *
 * @author Team Silent Coders
 * @version 1.0
 */
public class DbManager {

	public static final String JDBC_EXCEPTION = "JDBC Exception: ";
	public static final String SQL_EXCEPTION = "SQL Exception: ";

	public Connection connection;

	/**
	 * Open database connection
	 */
	public void openConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			String url = "jdbc:mysql://localhost:3306/trivia";
			String user = "root", pass = "root";
			System.out.println("Database connection established.");

			// Open connection
			connection = DriverManager.getConnection(url, user, pass);
		} catch (ClassNotFoundException e) {
			System.err.println(JDBC_EXCEPTION + e.getLocalizedMessage());
		} catch (SQLException e) {
			System.err.println(SQL_EXCEPTION + e.getLocalizedMessage());
		}
	}

	/**
	 * Close database connection
	 */
	public void closeConnection() {
		try {
			connection.close();
			System.out.println("Database connection terminated.");
		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
		}
	}

	/**
	 * Executes a query without returning a result, such as updating and
	 * deleting.
	 *
	 * @param query, the SQl query
	 */
	public void executeUpdate(String query) {
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (MySQLIntegrityConstraintViolationException e) {
			alertDialog(Alert.AlertType.ERROR, "Vraag toevoegen", null,
					"Deze vraag bestaat al!", StageStyle.UNDECORATED);
			AddQuestionController.duplicateError = true;
		} catch (SQLException e) {
			System.err.println(SQL_EXCEPTION + e.getLocalizedMessage());
		}
	}

	/**
	 * Executes a query with result.
	 *
	 * @param query, the SQL query
	 * @return query results
	 */
	public ResultSet doQuery(String query) {
		ResultSet result = null;
		try {
			Statement statement = connection.createStatement();
			result = statement.executeQuery(query);
		} catch (SQLException | NullPointerException e) {
			System.err.println(SQL_EXCEPTION + e.getLocalizedMessage());
		}
		return result;
	}

}
