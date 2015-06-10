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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;
import static trivia.AppConfig.*;
import trivia.Trivia;
import static trivia.Trivia.alertDialog;
import static trivia.Trivia.dbPass;
import static trivia.Trivia.dbUrl;
import static trivia.Trivia.dbUser;
import static trivia.Trivia.prefs;
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
	private TextField databaseUserName;

	@FXML
	private TextField databasePassword;

	@FXML
	private Label currentDatabaseLabel;

	@FXML
	private Label messageLabel;

	@FXML
	private Button exportDatabase;

	@FXML
	private Button importDatabase;

	@FXML
	private Button defaultSettings;

	@FXML
	private Button resetButton;

	@FXML
	private Button saveSettings;

	private static final DbManager dbm = new DbManager();

	private List<TextField> databaseFields;
	private static final String protocol = "jdbc:mysql://",
			userDirectoryString = System.getProperty("user.home");
	private String databaseUrl = "", newUrl = "", databaseUser = "", databasePass = "";
	private final File userDirectory = new File(userDirectoryString);

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		exportDatabase.setOnAction(this::exportDatabase);
		importDatabase.setOnAction(this::importDatabase);
		defaultSettings.setOnAction(this::loadView);
		resetButton.setOnAction(this::resetDatabase);
		saveSettings.setOnAction(this::saveSettings);

		databaseFields = Arrays.asList(databaseHost, databaseName,
				databaseUserName, databasePassword);
		for (TextField tf : databaseFields) {
			tf.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable,
						String oldValue, String newValue) {
					disableSaveButton();
				}
			});
		}
		reloadSettings();
	}

	/**
	 * Clears all the fields and deselects all toggles.
	 */
	private void clearFields() {
		Boolean reset;
		reset = true;
		databaseHost.setText("");
		databaseName.setText("");
		databaseUserName.setText("");
		databasePassword.setText("");
		reset = false;
		System.out.println("Fields cleared.");
	}

	private void disableSaveButton() {
		if (databaseHost.getText().isEmpty() || databaseUserName.getText().isEmpty()
				|| databasePassword.getText().isEmpty()) {
			saveSettings.setDisable(true);
		} else {
			saveSettings.setDisable(false);
		}
	}

	private static int execShellCmd(String command) {
		try {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(new String[]{"/bin/bash", "-c", command});
			int exitValue = process.waitFor();
			System.out.println("exit value: " + exitValue); // Value 0 = no error.

			BufferedReader buf = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			String line = "";
			while ((line = buf.readLine()) != null) {
				System.out.println("exec response: " + line);
			}
			return exitValue;
		} catch (IOException | InterruptedException e) {
			System.err.println("Error: " + e.getLocalizedMessage());
		}
		return 255;
	}

	private void exportDatabase(ActionEvent event) {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Database exporteren");
		chooser.setInitialDirectory(userDirectory);
		chooser.setInitialFileName("trivia.sql");
		chooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("SQL", "*.sql"));
		File file = chooser.showSaveDialog(stage);
		try {
			if (file == null) {
				System.out.println("Gebruiker heeft back-up geannuleerd.");
			} else {
				String shellCommand
						= "mysqldump --user=root --password=root --databases trivia > "
						+ file.getPath();
				int exitValue = execShellCmd(shellCommand);
				if (exitValue == 0) {
					System.out.println("Database succesvol geback-upt.");
				} else {
					System.err.println("Error: Foutcode " + exitValue
							+ ". Database back-up mislukt.");
				}
			}
		} catch (NullPointerException e) {
			// User canceled back-up; do nothing.
		}
	}

	private void fillFields() {
		clearFields();
		int findSlash = databaseUrl.substring(13).indexOf("/");
		if (!databaseUrl.equals(DEFAULT_URL)) {
			if (findSlash > 0) {
				databaseHost.setText(databaseUrl.substring(13, 13 + findSlash));
				databaseName.setText(databaseUrl.substring(13 + findSlash + 1));
			} else {
				databaseHost.setText(databaseUrl.substring(13));
				databaseName.setText("");
			}
			databaseUserName.setText(databaseUser);
			databasePassword.setText(databasePass);
		} else {
			databaseHost.setText("");
			databaseName.setText("");
			databaseUserName.setText("");
			databasePassword.setText("");
		}
		disableSaveButton();
	}

	private void getSettings() {
		databaseUrl = prefs.get(dbUrl, DEFAULT_URL);
		databaseUser = prefs.get(dbUser, DEFAULT_USER);
		databasePass = prefs.get(dbPass, DEFAULT_PASS);
	}

	private void importDatabase(ActionEvent event) {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Database exporteren");
		chooser.setInitialDirectory(userDirectory);
		chooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("SQL", "*.sql"));
		File file = chooser.showOpenDialog(stage);
		try {
			if (file == null) {
				System.out.println("Gebruiker heeft herstelling geannuleerd.");
			} else {
				System.out.println(file.getPath());
				String shellCommand
						= "mysql --user=root --password=root --database trivia < '"
						+ file.getPath() + "'";
				int exitValue = execShellCmd(shellCommand);
				if (exitValue == 0) {
					System.out.println("Database succesvol hersteld.");
				} else {
					System.err.println("Error: Foutcode " + exitValue
							+ ". Database herstelling mislukt.");
				}
			}
		} catch (NullPointerException e) {
			// User canceled back-up; do nothing.
		}
	}

	private void resetDatabase(ActionEvent event) {
		prefs.put(dbUrl, DEFAULT_URL);
		prefs.put(dbUser, DEFAULT_USER);
		prefs.put(dbPass, DEFAULT_PASS);
		reloadSettings();
	}

	private void reloadSettings() {
		getSettings();
		fillFields();
		setMessageLabel();
	}

	private void saveSettings(ActionEvent event) {
		Boolean empty = false, breaker = false;
		while (!empty && !breaker) {
			if (databaseHost.getText().isEmpty()) {
				empty = true;
			} else {
				newUrl = databaseHost.getText().startsWith(protocol) ? "" : protocol;
				newUrl += databaseHost.getText();
				if (!databaseName.getText().isEmpty()) {
					newUrl += "/" + databaseName.getText();
				}
			}
			breaker = true;
		}
		if (!empty) {
			prefs.put(dbUrl, newUrl);
			prefs.put(dbUser, databaseUserName.getText());
			prefs.put(dbPass, databasePassword.getText());
			reloadSettings();
		} else {
			alertDialog(Alert.AlertType.ERROR, "Bewaren mislukt", null,
					"De instellingen kunnen niet worden opgeslagen. Neem alles "
					+ "door en probeer het opnieuw.");
		}
	}

	private void setMessageLabel() {
		currentDatabaseLabel.setText(databaseUrl);
		dbm.openConnection();
		messageLabel.setText((serverOffline ? "Database offline. "
				+ (localhostOffline ? "Localhost ook offline." : "Verbonden met localhost.")
				: "Verbonden met database."));
		dbm.closeConnection();
	}
}
