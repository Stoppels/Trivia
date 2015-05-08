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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    Button addQuestionButton;

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
        addQuestionButton.setOnAction(this::confirmAlertAddQuestion);
        setComboBoxQuestions();
        selectQuestion.setOnAction(e -> System.out.println("User selected : " + selectQuestion.getValue()));//From : https://www.youtube.com/watch?v=Bg5VcIL2IhY
        deleteQuestionButton.setOnAction(this::confirmAlertDeleteQuestion);
    }

    @Override
    public void handleButtonAction(ActionEvent event) {
        System.out.println("ManageQuestionsController check: "
                + ((Control) event.getSource()).getId());
        loadView(event);
    }

    public void confirmAlertDeleteQuestion(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);

        alert.setTitle("Bevestig Vraag Verwijderen");
        alert.setHeaderText("Let op : \nU staat op het punt deze vraag te verwijderen : " + selectQuestion.getValue() );
        alert.setContentText("Klik op 'OK' om te accepteren en deze vraag te verwijderen.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            deleteQuestion();
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }
    
    public void confirmAlertAddQuestion(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);

        alert.setTitle("Bevestig Vraag Toevoegen");
        alert.setHeaderText("Let op : \nU staat op het punt deze vraag toe te voegen : " + addQuestionText.getText()
                + "\nMet het volgende juiste antwoord : " + addCorrectAnswer.getText()
                + "\nEn de volgende onjuiste antwoorden : " + addIncorrectAnswer1.getText() + ", " + addIncorrectAnswer2.getText()
                + ", " + addIncorrectAnswer3.getText() );
        alert.setContentText("Klik op 'OK' om te accepteren en deze vraag toe te voegen.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            addQuestion();
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }
    

    public void setComboBoxQuestions() {

        selectQuestion.setPromptText("Kies een vraag om te verwijderen");

        //make a new list with questions for the combobox
        ArrayList list = new ArrayList();

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/trivia", "root", "");

            Statement st = (Statement) con.createStatement();

            //make the String selectQuestion for every VraagID in the database
            //No idea how to do that, so for now it is < 100
            for (int i = 1; i < 100; i++) {
                String selectQuestion = "SELECT Vraag FROM vraag WHERE VraagID = " + i + " ; ";

                // add the results of the SELECT query to the arraylist list
                ResultSet rs = st.executeQuery(selectQuestion);
                while (rs.next()) {
                    list.add(rs.getString(1));
                }
            }
            //lets see the list for a test
            System.out.println("Dit is de list : " + list);

            //cast arraylist to observable list from http://stackoverflow.com/questions/22191954/javafx-casting-arraylist-to-observablelist
            ObservableList questions = FXCollections.observableArrayList(list);

            //set the items(named : questions) in the ComboBox "selectQuestion"
            selectQuestion.setItems(questions);

        } catch (SQLException ex) {
        }
    }

    public void deleteQuestion() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/trivia", "root", "");

            Statement st = (Statement) con.createStatement();

            // this is the delete query
            String deleteQuestionQuery = "DELETE FROM vraag WHERE Vraag = '" + selectQuestion.getValue() + "' ; ";

            //execute Question delete script  
            st.executeUpdate(deleteQuestionQuery);

            if (st.executeUpdate(deleteQuestionQuery) == 0) {
                System.out.println("Deleting question : " + selectQuestion.getValue());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManageQuestionsController.class.getName()).log(Level.SEVERE, null, ex);
        }

        // if no question selected : there is no question selected to delete Error Dialog
        if (selectQuestion.getValue() == null) {
            System.out.println("There is no question selected to delete !");
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Kan vraag niet verwijderen");
            alert.setContentText("Er is geen vraag geselecteerd om te verwijderen. "
                    + "Selecteer a.u.b. een vraag om deze te verwijderen.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Vraag Succesvol verwijderd");
            alert.showAndWait();
        }

        //this refreshes the ComboBox because there is now an item deleted, so it has to be updated
        setComboBoxQuestions();
    }

    public void addQuestion() {
//        System.out.println("ManageQuestionsController check: "
//                + ((Control) event.getSource()).getId());

        //Collect the Strings with getText from the selected textField
        String question = addQuestionText.getText();
        String correctAnswer = addCorrectAnswer.getText();
        String IncorrectAnswer1 = addIncorrectAnswer1.getText();
        String IncorrectAnswer2 = addIncorrectAnswer2.getText();
        String IncorrectAnswer3 = addIncorrectAnswer3.getText();

        // System check of input
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

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Vraag Succesvol toegevoegd");
        alert.showAndWait();

        //this refreshes the ComboBox because there is now an item added, so it has to be updated
        setComboBoxQuestions();

    }

}
