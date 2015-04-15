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
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import static trivia.Trivia.*;

/**
 * FXML Controller class
 *
 * @author Rehman
 */
public class SplashscreenController implements Initializable {

    @FXML
    private Label timerLabel;
    private static final Integer STARTTIME = 5;
    private Timeline timeline;
    private final IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        autoPlay();
    }

    @FXML
    public void autoPlay() {
        // Bind the timerLabel text property to the timeSeconds property
        timerLabel.textProperty().bind(timeSeconds.asString());
        timerLabel.setTextFill(Color.RED);

        // Countdown from STARTTIME to zero
        if (timeline != null) {
            timeline.stop();
        }
        timeSeconds.set(STARTTIME);
        timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(STARTTIME + 1),
                        new KeyValue(timeSeconds, 0)));
        timeline.playFromStart();

        // When completed counting down, execute method openHoofdmenu().
        timeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                openHoofdmenu();
            }
        });
        timeline.setCycleCount(1);
    }

    @FXML
    private void openHoofdmenu() {
        timeline.stop();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Hoofdmenu.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            prevStage.close();
            setPrevStage(stage);
        } catch (IOException ex) {
            Logger.getLogger(SplashscreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void openAdminmenu() {
        timeline.stop();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/AdminHoofdmenu.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            prevStage.close();
            setPrevStage(stage);
        } catch (IOException ex) {
            Logger.getLogger(SplashscreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
