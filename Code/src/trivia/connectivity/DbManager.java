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
import java.util.List;
import javafx.scene.control.Alert;
import static trivia.AppConfig.DEFAULT_PASS;
import static trivia.AppConfig.DEFAULT_URL;
import static trivia.AppConfig.DEFAULT_USER;
import trivia.Trivia;
import static trivia.Trivia.alertDialog;
import static trivia.Trivia.dbPass;
import static trivia.Trivia.dbUrl;
import static trivia.Trivia.dbUser;
import static trivia.Trivia.prefs;

/**
 * This class handles interactions with the database.
 *
 * @author Team Silent Coders
 * @version 1.0
 */
public class DbManager {

	public static final String JDBC_EXCEPTION = "JDBC Exception: ";
	public static final String SQL_EXCEPTION = "SQL Exception: ";

	public Connection connection;
	private ResultSet result;
	private Boolean terminalError = false;

	/**
	 * Opens database connection. If the remote connection fails, a fallback
	 * local connection will be initiated. If the local connection also fails,
	 * variable terminalError is set to true and an error message is shown.
	 * Catches any errors and logs them.
	 */
	public void openConnection() {
		String url = "", user = "", pass = "";
		boolean error = true;
		try {
			Class.forName("com.mysql.jdbc.Driver");

			// User set default database.
			url = prefs.get(dbUrl, DEFAULT_URL);
			user = prefs.get(dbUser, DEFAULT_USER);
			pass = prefs.get(dbPass, DEFAULT_PASS);

			System.out.println("Initiating connection with database.");

			// Open connection
			connection = DriverManager.getConnection(url, user, pass);
			error = false;
			Trivia.serverOffline = false;
			terminalError = false;
		} catch (ClassNotFoundException e) {
			System.err.println("Remote " + JDBC_EXCEPTION + e.getLocalizedMessage());
			error = true;
		} catch (SQLException e) {
			System.err.println("Remote " + SQL_EXCEPTION + e.getLocalizedMessage());
			error = true;
		} finally {
			if (error) { // Switch to local database if remote database fails.
				Trivia.serverOffline = true;
				try {
					url = DEFAULT_URL;
					user = DEFAULT_USER;
					pass = DEFAULT_PASS;
					connection = DriverManager.getConnection(url, user, pass);
					error = false;
					Trivia.localhostOffline = false;
					terminalError = false;
				} catch (SQLException e) {
					System.err.println("Local " + SQL_EXCEPTION + e.getLocalizedMessage());
					error = true;
				} finally {
					if (error) {
						Trivia.localhostOffline = true;
						terminalError = true;
						alertDialog(Alert.AlertType.ERROR, "Verbinding mislukt", null,
								"De database kan niet worden bereikt. Probeer het "
								+ "later opnieuw");
					} else {
						System.out.println("Database connection established with local: " + url);
					}
				}
			} else {
				System.out.println("Database connection established with server: " + url);
			}
		}
	}

	/**
	 * Tries closing a database connection. Catches any errors (such as if there
	 * is no open connection) and logs them.
	 */
	public void closeConnection() {
		try {
			connection.close();
			System.out.println("Database connection terminated.");
		} catch (Exception e) {
			System.err.println("Closing connection exception: " + e.getLocalizedMessage());
		} catch (Throwable e) { // We log any Errors (and its subclasses) here.
			System.err.println("Throwable exception: " + e.getLocalizedMessage());
		}
	}

	/**
	 * Executes an SQL-injection proof query without result. May be used for
	 * INSERT statements. Does contain a single or multiple parameters.
	 *
	 * @param statement to be executed.
	 * @param parameters to be inserted in the statement.
	 */
	public void executeUpdate(PreparedStatement statement, List<String> parameters) {
		try {
			int i = 1;
			for (String pars : parameters) {
				statement.setString(i++, pars);
			}
			statement.executeUpdate();
		} catch (MySQLIntegrityConstraintViolationException e) {
			System.err.println("Error: " + e.getLocalizedMessage());

			alertDialog(Alert.AlertType.ERROR, "Vraag toevoegen", null,
					"Deze vraag bestaat al!");

			Trivia.duplicateError = true;
		} catch (SQLException e) {
			System.err.println(SQL_EXCEPTION + e.getLocalizedMessage());
		} catch (Throwable e) {
			System.err.println("Throwable exception: " + e.getLocalizedMessage());
		}
	}

	/**
	 * Executes a SQL-injection proof query with result. May be used for SELECT
	 * statements that return a value. Does not contain any parameters.
	 *
	 * @param statement to be executed.
	 * @return ResultSet of queried statement.
	 */
	public ResultSet getResultSet(PreparedStatement statement) {
		result = null;
		try {
			result = statement.executeQuery();
		} catch (SQLException e) {
			System.err.println(SQL_EXCEPTION + e.getLocalizedMessage());
		}
		return result;
	}

	/**
	 * Executes an SQL-injection proof query with result. May be used for SELECT
	 * statements that return a value. Does contain a single or multiple
	 * parameters.
	 *
	 * @param statement to be executed.
	 * @param parameters to be inserted in the statement.
	 * @return ResultSet of queried statement.
	 */
	public ResultSet getResultSet(PreparedStatement statement, List<String> parameters) {
		result = null;
		try {
			int i = 1;
			for (String pars : parameters) {
				statement.setString(i++, pars);
			}
			result = statement.executeQuery();
		} catch (SQLException e) {
			System.err.println("getResultSet " + SQL_EXCEPTION + e.getLocalizedMessage());
		}
		return result;
	}

	/**
	 * Method for inserting a new high score in the database. The first
	 * parameter consists of the Double value score that the player earned,
	 * casted to int. The second value is the player's name, which can be left
	 * empty for anonymous players. The third value is the time the score was
	 * reached.
	 *
	 * @param statement to be executed.
	 * @param parameters to be inserted in the statement.
	 */
	public void insertHighScore(PreparedStatement statement, List parameters) {
		try {
			statement.setInt(1, (int) parameters.get(0));
			statement.setString(2, parameters.get(1).toString());
			statement.setTimestamp(3, (Timestamp) parameters.get(2));
			statement.execute();
		} catch (SQLException e) {
			System.err.println("insertHighscore " + SQL_EXCEPTION + e.getLocalizedMessage());
		}
	}

	/**
	 * Method tries opening a connection and returns a boolean value indicating
	 * whether it succeeded or not. Uses a try-finally block, because it cannot
	 * handle exceptions that are handled locally in the respective open and
	 * close methods.
	 *
	 * @return whether openConnection() succeeded or not.
	 */
	public Boolean testConnection() {
		try {
			openConnection();
			return terminalError; // True = error.
		} finally { // Can't handle an exception here, we do that in openConnection().
			closeConnection();
		} // Close the connection if there was no error, otherwise handle in closeConnection().
	}

}
