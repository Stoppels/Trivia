/*
 * The MIT License
 *
 * Copyright 2015 Team Silent Coders.
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
package trivia;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import static trivia.SpelOpzettenController.makkelijkHolder;
import static trivia.Trivia.*;

/**
 * FXML Controller class
 *
 * @author Yassinee
 */
public class VraagController implements Initializable {

	@FXML
	ProgressBar progressBar;

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}

	/**
	 * I don't work yet, please fix me //////////////////////////////////
	 */
	@FXML
	public void checkVraagSettings() {
		if (SpelOpzettenController.makkelijkHolder) {
			System.out.println("yo");
		}
		if (makkelijkHolder) {
			System.out.println("yo2");
		}
	}

	@FXML
	private void progressChecker() {
		//progressBar;
	}

	@FXML
	private void stopQuiz() {
		try {
//		  Alert alert = new Alert(AlertType.CONFIRMATION);
//        alert.setTitle("Stop quiz");
//        alert.setHeaderText("Weet u zeker dat u de quiz wilt stoppen?");
//        alert.setContentText("De antwoorden worden niet opgeslagen.\nDit brengt u terug naar het hoofdmenu.");
//        alert.initStyle(StageStyle.UNDECORATED);
//
//        Optional<ButtonType> result = alert.showAndWait();
//        if (result.get() == ButtonType.OK) {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Hoofdmenu.fxml"));
			Parent root = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.show();
			prevStage.close();
			setPrevStage(stage);
//        }
		} catch (IOException ex) {
			Logger.getLogger(VraagController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
