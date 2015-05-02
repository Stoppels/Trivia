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

import com.mysql.jdbc.Statement;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.stage.StageStyle;
import trivia.Trivia;
import static trivia.Trivia.alertDialog;

/**
 * FXML Controller class
 *
 * @author Team Silent Coders
 * @version 1.0
 */
public class ManageQuestionsController extends Trivia implements Initializable {

    @FXML
    Button mainMenu;

    @FXML
    Button addQuestion;

    @FXML
    TextField addQuestionText;

    @FXML
    TextField addCorrectAnswer;

    @FXML
    TextField addIncorrectAnswer1;

    @FXML
    TextField addIncorrectAnswer2;

    @FXML
    TextField addIncorrectAnswer3;

    @FXML
    ComboBox selectQuestion;

    @FXML
    Button deleteQuestionButton;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainMenu.setOnAction(this::handleButtonAction);
        addQuestion.setOnAction(this::addQuestion);

    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        System.out.println("ManageQuestionsController check: "
                + ((Control) event.getSource()).getId());
        loadView(event);
    }

    public void addQuestion(ActionEvent event) {
        System.out.println("ManageQuestionsController check: "
                + ((Control) event.getSource()).getId());

        //Collect the Strings with getText from the selected textField
        String question = addQuestionText.getText();
        String correctAnswer = addCorrectAnswer.getText();
        String IncorrectAnswer1 = addIncorrectAnswer1.getText();
        String IncorrectAnswer2 = addIncorrectAnswer2.getText();
        String IncorrectAnswer3 = addIncorrectAnswer3.getText();

        // System control of input
        System.out.println("Added question : " + question);
        System.out.println("Added Correct Answer : " + correctAnswer);
        System.out.println("Added Incorrect Answer 1 : " + IncorrectAnswer1);
        System.out.println("Added Incorrect Answer 2 : " + IncorrectAnswer2);
        System.out.println("Added Incorrect Answer 3 : " + IncorrectAnswer3);

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/trivia", "root", "");

            Statement st = (Statement) con.createStatement();

            String insertQuestion = "INSERT INTO vraag VALUES(NULL, '" + question + "' ) ; ";

            //execute Question insert script  
            st.executeUpdate(insertQuestion);

            try {
                //Get the VraagID from the query
                String query = "SELECT VraagID FROM vraag WHERE Vraag = '" + question + "' ;";

                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    String VraagID = rs.getString("VraagID");
                    System.out.println(VraagID);

                    // create the sql insert scripts strings
                    String insertCorrectAnswer = "INSERT INTO antwoordgoed VALUES(" + VraagID + ", '" + correctAnswer + "' , " + VraagID + " ) ;";
                    String insertIncorrectAnswer1 = "INSERT INTO antwoordfout VALUES(1, '" + IncorrectAnswer1 + "' , " + VraagID + " ) ;";
                    String insertIncorrectAnswer2 = "INSERT INTO antwoordfout VALUES(2, '" + IncorrectAnswer2 + "' , " + VraagID + " ) ;";
                    String insertIncorrectAnswer3 = "INSERT INTO antwoordfout VALUES(3, '" + IncorrectAnswer3 + "' , " + VraagID + " ) ;";
                    //execute the scripts 
                    st.executeUpdate(insertCorrectAnswer);
                    st.executeUpdate(insertIncorrectAnswer1);
                    st.executeUpdate(insertIncorrectAnswer2);
                    st.executeUpdate(insertIncorrectAnswer3);
                }
            } catch (SQLException ex) {
            } finally {
                if (st != null) {
                    st.close();
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(ManageQuestionsController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //after everything is put into the database, clear the textFields
        addQuestionText.setText("");
        addCorrectAnswer.setText("");
        addIncorrectAnswer1.setText("");
        addIncorrectAnswer2.setText("");
        addIncorrectAnswer3.setText("");

        alertDialog(Alert.AlertType.CONFIRMATION, "Vraag toevoegen geslaagd",
                "Het uploaden van de vraag in de database is succesvol",
                "U kunt een nieuwe vraag invoeren of afsluiten", StageStyle.UNDECORATED);

    }

}
