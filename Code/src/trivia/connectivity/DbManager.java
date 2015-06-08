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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
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
 *
 * @author Team Silent Coders
 * @version 1.0
 */
public class DbManager {

	public static final String JDBC_EXCEPTION = "JDBC Exception: ";
	public static final String SQL_EXCEPTION = "SQL Exception: ";

	public Connection connection;
	public ResultSet result;

//	public PreparedStatement pStatement;
//	public ResultSet rs = getResultSet(pStatement, stmParameters);
	/**
	 * Opens database connection.
	 */
	public void openConnection() {
		String url = "", user = "", pass = "";
		boolean error = true;
		try {
			Class.forName("com.mysql.jdbc.Driver");

			// User set default database.
			prefs.put(dbUrl, DEFAULT_URL);
			prefs.put(dbUser, DEFAULT_USER);
			prefs.put(dbPass, DEFAULT_PASS);
			url = prefs.get(dbUrl, DEFAULT_URL);
			user = prefs.get(dbUser, DEFAULT_USER);
			pass = prefs.get(dbPass, DEFAULT_PASS);
			
			// TSC test server 1
//			url = "jdbc:mysql://oege.ie.hva.nl:3306/zshayann001";
//			user = "shayann001";
//			pass = "hT5vz8pZ8W+mCP";
			System.out.println("Initiating connection with database.");

			// Open connection
			connection = DriverManager.getConnection(url, user, pass);
			error = false;
		} catch (ClassNotFoundException e) {
			System.err.println(JDBC_EXCEPTION + e.getLocalizedMessage());
			error = true;
		} catch (SQLException e) {
			System.err.println(SQL_EXCEPTION + e.getLocalizedMessage());
			error = true;
		} finally {
			if (error) { // Switch to local database if remote database fails.
				try {
					url = DEFAULT_URL;
					user = DEFAULT_USER;
					pass = DEFAULT_PASS;
					connection = DriverManager.getConnection(url, user, pass);
					error = false;
				} catch (SQLException e) {
					System.err.println(SQL_EXCEPTION + e.getLocalizedMessage());
					error = true;
				} finally {
					if (error) {
						alertDialog(Alert.AlertType.ERROR, "Verbinding mislukt", null,
								"De database kan niet worden bereikt. Probeer het "
								+ "later opnieuw", StageStyle.UNDECORATED);
						
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
	 * Closes database connection.
	 */
	public void closeConnection() {
		try {
			connection.close();
			System.out.println("Database connection terminated.");
		} catch (Exception e) {
			System.err.println("Exception: " + e.getLocalizedMessage());
		} catch (Throwable e) {
			System.err.println("Throwable exception: " + e.getLocalizedMessage());
		}
	}

	/**
	 * Executes a SQL-injection proof query with result.
	 *
	 * @param statement
	 * @param parameters
	 */
	public void executeUpdate(PreparedStatement statement, List<String> parameters) {
		try {
			int i = 1;
			for (String pars : parameters) {
				statement.setString(i++, pars);
			}
			statement.executeUpdate();
		} catch (MySQLIntegrityConstraintViolationException e) {
			System.out.println("Error: " + e.getLocalizedMessage());

			alertDialog(Alert.AlertType.ERROR, "Vraag toevoegen", null,
					"Deze vraag bestaat al!", StageStyle.UNDECORATED);

			Trivia.duplicateError = true;
		} catch (SQLException e) {
			System.err.println(SQL_EXCEPTION + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (Throwable e) {
			System.err.println("Throwable exception: " + e.getLocalizedMessage());
		}
	}
	
	public void executeUpdate(PreparedStatement statement) {
		try {
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Executes a SQL-injection proof query with result.
	 *
	 * @param statement
	 * @return ResultSet
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
	 * Executes a SQL-injection proof query with result.
	 *
	 * @param statement
	 * @param parameters
	 * @return ResultSet
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
			System.err.println(SQL_EXCEPTION + e.getLocalizedMessage());
		}
		return result;
	}

}
