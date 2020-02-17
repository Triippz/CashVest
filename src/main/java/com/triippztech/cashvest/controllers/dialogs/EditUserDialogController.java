/*
 * Copyright 2020 Mark Tripoli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.triippztech.cashvest.controllers.dialogs;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.triippztech.cashvest.domain.User;
import com.triippztech.cashvest.domain.annotations.PhoneNumber;
import com.triippztech.cashvest.domain.annotations.PhoneNumberConstraint;
import com.triippztech.cashvest.domain.annotations.PhoneNumberValidator;
import com.triippztech.cashvest.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Controller;

import javax.validation.Validation;
import java.net.URL;
import java.util.ResourceBundle;

import static com.triippztech.cashvest.config.Constants.EMAIL_REGEX;

@Controller
public class EditUserDialogController implements Initializable {

    private final UserService userService;
    private User user;

    public EditUserDialogController(UserService userService) {
        this.userService = userService;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user = this.userService.getSignedInUser();
        loadForm();
    }

    @FXML
    public void submitForm(ActionEvent event) {
        if ( formValid() )
            closeStage(event);
    }

    private boolean formValid() {
        String email = this.emailField.getText().trim().toLowerCase();
        String firstName = this.firstNameField.getText().trim();
        String lastName = this.lastNameField.getText().trim();
        String imageUrl = this.imageURLField.getText().trim();
        String phoneNumber = this.phoneNumberField.getText().trim();

        if ( !email.matches(EMAIL_REGEX) ) {
            setErrorLabel("Please enter a valid email.");
            return false;
        }
        if ( !imageUrl.isEmpty() ) {
            String[] schemes = {"http","https"};
            UrlValidator urlValidator = new UrlValidator(schemes);
            if ( !urlValidator.isValid(imageUrl ) ) {
                setErrorLabel("Image URL is invalid");
                return false;
            }
        } else {
            imageUrl = null;
        }

        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setImageUrl(imageUrl);

        if ( !phoneNumber.isEmpty() ) {
            PhoneNumber phoneNumberValid = new PhoneNumber(phoneNumber);
            if ( phoneNumberValid.isValid() )
                user.setPhoneNumber(phoneNumberValid.formatPhoneNumber());
        }
        userService.save(user);
        return true;
    }

    private void loadForm() {
        if ( this.user.getFirstName() != null )
            firstNameField.setText(this.user.getFirstName());
        if ( this.user.getLastName() != null )
            lastNameField.setText(this.user.getLastName());
        if ( this.user.getImageUrl() != null )
            imageURLField.setText(this.user.getImageUrl() );
        if ( this.user.getPhoneNumber() != null ) {
            phoneNumberField.setText(this.user.getPhoneNumber());
        }

        emailField.setText(this.user.getEmail());
    }

    private void setErrorLabel(String error) {
        this.errorLabel.setText(error);
    }

    private void closeStage(ActionEvent event) {
        Node  source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    public BorderPane borderPane;
    @FXML
    public Label errorLabel;
    @FXML
    public JFXTextField emailField;
    @FXML
    public JFXTextField firstNameField;
    @FXML
    public JFXTextField lastNameField;
    @FXML
    public JFXTextField imageURLField;
    @FXML
    public JFXButton submitButton;
    @FXML
    public JFXTextField phoneNumberField;
}
