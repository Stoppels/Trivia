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
import java.util.ArrayList;
import java.util.List;

public class QueryManager {

	private final DbManager dbmanager;

	public QueryManager(DbManager dbmanager) {
		this.dbmanager = dbmanager;
	}

	public void insertBaggage() {

		String sql = "INSERT INTO Trivia.Vraag VALUES(2, 'Hoe heette het vorige project?') ";

		System.out.println(sql);
		int luggage_id = 0;
		try {
			ResultSet result = dbmanager.insertQuery(sql);
			result.next();
			luggage_id = result.getInt(1);
			System.out.println(luggage_id);

		} catch (SQLException e) {

			System.out.println("FOUT" + e.getMessage());
		}

	}

	public void setVraag() throws SQLException {

		try {
			String sql = "SELECT Vraag FROM vraag WHERE VraagID = 1;";

			System.out.println(sql);

			ResultSet result = dbmanager.doQuery(sql);
			result.next();

		} catch (SQLException e) {

			System.out.println("FOUT" + e.getMessage());
		}

	}
}
