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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import static trivia.Trivia.*;

/**
 * FXML Controller class
 *
 * @author Rehman
 */
public class HoofdmenuController implements Initializable {

    @FXML
    private Label uitlegA;

    @FXML
    private Label uitlegB;

    @FXML
    private Label uitlegC;

    @FXML
    private Label uitlegD;

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

    @FXML
    private void zetSpelOp() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/SpelOpzetten.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            prevStage.close();
            setPrevStage(stage);
        } catch (IOException ex) {
            Logger.getLogger(HoofdmenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void startSpel() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Vraag.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            prevStage.close();
            setPrevStage(stage);
        } catch (IOException ex) {
            Logger.getLogger(HoofdmenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void toggleHelp() {
        List<Label> helpItems = Arrays.asList(uitlegA, uitlegB, uitlegC, uitlegD);

        for (Label a : helpItems) {
            if (!a.isVisible()) {
                a.setVisible(true);
            } else {
                a.setVisible(false);
            }
        }
    }

    /**
     * Triggers a confirmation dialog for quitting the app.
     */
    @FXML
    private void sluitAf() {
        Action response = Dialogs.create().owner(prevStage)
                .title("Afsluiten")
                .message("Weet u zeker dat u wilt afsluiten?")
                .actions(Dialog.ACTION_OK, Dialog.ACTION_CANCEL)
                .showWarning();

        if (response == Dialog.ACTION_OK) {
            System.exit(0);
        }
    }

}
