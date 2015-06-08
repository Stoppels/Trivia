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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import trivia.Trivia;
import trivia.connectivity.DbManager;

/**
 *
 * @author Nick Shayan
 * @version 1.0
 */
public class AdvancedSettingsController extends Trivia implements Initializable {

	@FXML
	private TextField databaseHost;

	@FXML
	private TextField databaseName;

	@FXML
	private Button defaultSettings;

	@FXML
	private Button saveSettings;

	@FXML
	private TextField databasePassword;

	@FXML
	private TextField databasePort;

	@FXML
	private TextField databaseUserName;

	private static final DbManager dbm = new DbManager();

	private static String ip = "localhost";
	private static String port = "3306";
	private static String database = "trivia";
	private static String user = "root";
	private static String pass = "root";
	private static String path = System.getProperty("user.home") + "/Desktop/";
	private static String dumpCommand = "mysqldump";

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		defaultSettings.setOnAction(this::loadView);
		saveSettings.setOnAction((ActionEvent event) -> this.mysqldump());
	}

	public static void export(ActionEvent event) {

		try {
			dbm.openConnection();
			Runtime.getRuntime().exec("mysqldump -u root -proot trivia > " + path);
		} catch (IOException ex) {
			Logger.getLogger(AdvancedSettingsController.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			dbm.closeConnection();
		}
	}

	public static void execShellCmd(String cmd) {
		try {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(new String[]{"/bin/bash", "-c", cmd});
			int exitValue = process.waitFor();
			System.out.println("exit value: " + exitValue);
			BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = "";
			while ((line = buf.readLine()) != null) {
				System.out.println("exec response: " + line);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void mysqldump() {
		String shellCommand
				= "mysqldump --user=root --password=root --databases trivia > "
				+ path + "trivia.sql";
		execShellCmd(shellCommand);
	}
	
	private void importDatabase() {
		String shellCommand
				= "mysqldump --user=root --password=root --databases trivia > "
				+ path + "trivia.sql";
		execShellCmd(shellCommand);
	}
}
