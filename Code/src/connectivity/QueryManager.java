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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import javafx.scene.control.Label;

/**
 *
 * @author Team Silent Coders
 * @version 1.0
 */
public class QueryManager {

	// object databaseManager for databse connection
	DbManager dbm = new DbManager();

	// Generate random integer in order to fetch random question from database
	Random rand = new Random();
	int VraagID = rand.nextInt(4);

	private final DbManager dbmanager;

	public QueryManager(DbManager dbmanager) {
		this.dbmanager = dbmanager;
	}

	/**
	 *
	 */
	public void insert() {

		String sql = "INSERT INTO Trivia.Vraag VALUES("
				+ "2, 'Hoe heette het vorige project?') ";

		System.out.println(sql);
		int id = 0;
		try {
			ResultSet result = dbmanager.insertQuery(sql);
			result.next();
			id = result.getInt(1);
			System.out.println(id);
		} catch (SQLException e) {
			System.err.println("Query error: " + e.getLocalizedMessage());
		}
	}

	/**
	 *
	 * @return the string of the query
	 */
	public String setQuestion() {
		dbm.openConnection();
		String sql = "SELECT Vraag FROM vraag WHERE VraagID =" + VraagID + ";";
		System.out.println(sql);
		String returnString = null;
		try {
			ResultSet result = dbm.doQuery(sql);
			result.next();
			returnString = result.getString("Vraag");
		} catch (SQLException e) {
			System.err.println("Error: " + e.getLocalizedMessage());
		}
		return returnString;
	}

	/**
	 *
	 * @param antwoordfoutID
	 * @param labelNumber
	 */
	public void setWrongAnswer(int antwoordfoutID, Label labelNumber) {

		// Open database connection
		dbm.openConnection();
		String sqlAnswer = "SELECT AntwoordFout FROM antwoordfout INNER JOIN vraag ON"
				+ " antwoordfout.VraagID = vraag.VraagID WHERE AntwoordFoutID ="
				+ antwoordfoutID + " AND vraag.VraagID = " + VraagID + ";";
		System.out.println(sqlAnswer);

		try {
			ResultSet result = dbm.doQuery(sqlAnswer);
			result.next();
			labelNumber.setText(result.getString("AntwoordFout"));
		} catch (SQLException e) {
			System.err.println("FOUT" + e.getLocalizedMessage());
		}
	}

	/**
	 *
	 * @param labelNumber
	 */
	public void setRightAnswer(Label labelNumber) {

		QueryManager qm = new QueryManager(dbm);

		// Open database connection
		dbm.openConnection();

		String sqlAnswer = "SELECT AntwoordGoed FROM antwoordgoed WHERE VraagID = " + VraagID + ";";
		System.out.println(sqlAnswer);

		try {
			ResultSet result = dbm.doQuery(sqlAnswer);
			result.next();
			labelNumber.setText(result.getString("AntwoordGoed"));
		} catch (SQLException e) {
			System.err.println("FOUT" + e.getLocalizedMessage());
		}
	}

}
