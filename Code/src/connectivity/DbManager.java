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
package connectivity;

import java.sql.*;

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

			String url = "jdbc:mysql://localhost/Trivia";
			String user = "root", pass = "root";
			System.out.println("Connected");

			// Open connection
			connection = DriverManager.getConnection(url, user, pass);
		} catch (ClassNotFoundException e) {
			System.err.println(JDBC_EXCEPTION + e);
		} catch (java.sql.SQLException e) {
			System.err.println(SQL_EXCEPTION + e);
		}
	}

	/**
	 * Close database connection
	 */
	public void closeConnection() {
		try {
			connection.close();
		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
		}
	}

	/**
	 * Executes a query without result.
	 *
	 * @param query, the SQl query
	 */
	public void executeQuery(String query) {
		try {
			Statement statement = connection.createStatement();
			statement.executeQuery(query);
		} catch (java.sql.SQLException e) {
			System.err.println(SQL_EXCEPTION + e);
		}
	}

	/**
	 * Executes a query with result.
	 *
	 * @param query, the SQL query
	 * @return
	 */
	public ResultSet doQuery(String query) {
		ResultSet result = null;
		try {
			Statement statement = connection.createStatement();
			result = statement.executeQuery(query);
		} catch (java.sql.SQLException e) {
			System.err.println(SQL_EXCEPTION + e);
		}
		return result;
	}

	/**
	 * Executes a query with result.
	 *
	 * @param query, the SQL query
	 * @return generated keys
	 */
	public ResultSet insertQuery(String query) {
		ResultSet result = null;
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			result = statement.getGeneratedKeys();
		} catch (java.sql.SQLException e) {
			System.err.println(SQL_EXCEPTION + e);
		}
		return result;
	}

}
