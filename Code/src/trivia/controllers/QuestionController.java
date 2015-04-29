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

import trivia.connectivity.DbManager;
import trivia.connectivity.QueryManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import trivia.Trivia;
import static trivia.controllers.GameSetUpController.makkelijkHolder;

/**
 * FXML Controller class
 *
 * @author Team Silent Coders
 * @version 1.0
 */
public class QuestionController extends Trivia implements Initializable {

    // Object to call connection
    DbManager dbm = new DbManager();
    // Object to call QueryManager class
    QueryManager qm = new QueryManager(dbm);

    private static final Integer STARTTIME = 31;
    private Timeline timeline;
    private final IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);

    @FXML
    Button previousQuestion;

    @FXML
    Button nextQuestion;

    @FXML
    Button mainMenu;

    @FXML
    Button buttonA;

    @FXML
    Button buttonB;

    @FXML
    Button buttonC;

    @FXML
    Button buttonD;

    @FXML
    ProgressBar progressBar;

    @FXML
    Label timer;

    @FXML
    Label question;

    @FXML
    Label labelA;

    @FXML
    Label labelB;

    @FXML
    Label labelC;

    @FXML
    Label labelD;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Progressbar();
        autoPlay();

        // sets the question
        dbm.openConnection();
        question.setText(qm.setQuestion());

        //sets the wrong answer
        qm.setWrongAnswer(1, labelA);
        qm.setWrongAnswer(2, labelB);
        qm.setWrongAnswer(3, labelC);
        qm.setRightAnswer(labelD);

        mainMenu.setOnAction(this::stopQuiz);

        // TODO
        // if current answer > 10, scoreCheck()
        // if int current question > 1 AND previousQuestion is disabled, enable previousQuestion
        // else if int current question < 2, disable previousQuestion
    }

    /**
     * I don't work yet, please fix me //////////////////////////////////
     */
    @FXML
    public void checkVraagSettings() {
        if (GameSetUpController.makkelijkHolder) {
            // System.out.println("yo");
        }
        if (makkelijkHolder) {
            // System.out.println("yo2");
        }
        //enable timer? timerCountdown()
        //other settings?
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        System.out.println("QuestionController check: "
                + ((Control) event.getSource()).getId());
        super.handleButtonAction(event);
    }

    @FXML
    private void previousQuestion() {

        question.setText(qm.setQuestion());
        qm.setWrongAnswer(1, labelA);
        qm.setWrongAnswer(2, labelB);
        qm.setWrongAnswer(3, labelC);
        qm.setRightAnswer(labelD);
        saveAnswer();
        //goto current -1
    }

    @FXML
    private void nextQuestion() {
        question.setText(qm.setQuestion());
        qm.setWrongAnswer(1, labelA);
        qm.setWrongAnswer(2, labelB);
        qm.setWrongAnswer(3, labelC);
        qm.setRightAnswer(labelD);

        saveAnswer();
        //goto current + 1
    }

    @FXML
    private void progressChecker() {
        //progressBar;
    }

    @FXML
    private void timerCountdown() {
        //countdown
        //remember remaining time per question?
        //show warning if no time remaining but user went back to question?
    }

    @FXML
    private void saveAnswer() {
        //remember chosen answer;
    }

    @FXML
    private void scoreCheck() {
        //analyze savedanswers
    }

    @FXML
    public void autoPlay() {
        // Bind the timerLabel text property to the timeSeconds property
        timer.textProperty().bind(timeSeconds.asString());
        timer.setTextFill(Color.RED);

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
        timeline.setOnFinished((ActionEvent event) -> {
            // Simulate button action
            nextQuestion.fire();
        });
    }

    public void Button(ActionEvent event) {
        timeline.stop();
        System.out.println("QuestionController check check: "
                + ((Control) event.getSource()).getId());
        loadView(event);
    }

    @FXML

    private void stopQuiz(ActionEvent event) {
        if (alertDialog(AlertType.CONFIRMATION, "Stop quiz", "Weet u zeker dat u"
                + " de quiz wilt stoppen?", "De antwoorden worden niet opgeslagen."
                + "\nDit brengt u terug naar het hoofdmenu.", StageStyle.UNDECORATED)) {
            handleButtonAction(event);
        }
    }

    public void Progressbar() {
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() {
                for (int i = 1; i < 10; i++) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        System.err.println(e.getLocalizedMessage());
                    }
                    System.out.println(i);
                    updateProgress(i, 10);
                }
                return null;
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());

        Thread th = new Thread(task);

        th.setDaemon(true);

        th.start();
    }
}
