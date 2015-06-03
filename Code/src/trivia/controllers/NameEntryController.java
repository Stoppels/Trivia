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

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author axel
 */
public class NameEntryController extends trivia.Trivia implements Initializable {

    @FXML
    private Button Q;
    @FXML
    private Button A;
    @FXML
    private Button M;
    @FXML
    private Button key1;
    @FXML
    private Button key2;
    @FXML
    private Button key3;
    @FXML
    private Button key4;
    @FXML
    private Button key5;
    @FXML
    private Button key6;
    @FXML
    private Button key7;
    @FXML
    private Button key8;
    @FXML
    private Button key9;
    @FXML
    private Button key0;
    @FXML
    private Button Z;
    @FXML
    private Button W;
    @FXML
    private Button E;
    @FXML
    private Button R;
    @FXML
    private Button T;
    @FXML
    private Button Y;
    @FXML
    private Button U;
    @FXML
    private Button I;
    @FXML
    private Button O;
    @FXML
    private Button P;
    @FXML
    private Button X;
    @FXML
    private Button S;
    @FXML
    private Button D;
    @FXML
    private Button F;
    @FXML
    private Button G;
    @FXML
    private Button H;
    @FXML
    private Button J;
    @FXML
    private Button K;
    @FXML
    private Button L;
    @FXML
    private Button N;
    @FXML
    private Button Space;
    @FXML
    private Button C;
    @FXML
    private Button V;
    @FXML
    private Button B;
    @FXML
    private Label NameLabel;
    @FXML
    private TextField InputName;
    @FXML
    private Button next;
    @FXML
    private Button Backspace;

    

    String newtext;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        next.setOnAction((event) -> this.loadView(event));
        

    }

    @FXML
    public String clickOnButton(ActionEvent event) {
        Space.setText(" ");
        String text = ((Button) event.getSource()).getText();
        String oldtext = InputName.getText();
        newtext = oldtext + text;
        InputName.setText(newtext);
        return newtext;
    }

    @FXML
    public void backspace(ActionEvent event) {

        String news = "";

        if (InputName.getText().length() > 0) {     

            StringBuilder strB = new StringBuilder(InputName.getText());

            strB.deleteCharAt(InputName.getText().length() - 1);

            news = strB.toString();
        }
        InputName.setText(news);

    }
}
