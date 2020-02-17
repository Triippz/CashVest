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

package com.triippztech.cashvest.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.triippztech.cashvest.JavaFxApplication;
import com.triippztech.cashvest.domain.User;
import com.triippztech.cashvest.service.UserService;
import com.triippztech.cashvest.utilities.SceneRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.triippztech.cashvest.config.Constants.EMAIL_REGEX;
import static com.triippztech.cashvest.utilities.AlertUtil.pushAlert;

/**
 * The Registration controller which is used for the Registration View
 * and managing and handing off actions to the UserService
 * @author Mark Tripoli
 * @version 1.0.0
 * @since 2020-01-30
 */
@Controller
public class RegisterController implements Initializable {
    private double xOffset = 0;
    private double yOffset = 0;
    private final UserService userService;
    private final SceneRouter sceneRouter;

    public RegisterController(UserService userService, SceneRouter sceneRouter) {
        this.userService = userService;
        this.sceneRouter = sceneRouter;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        makeStageDrageable();
        borderPane.setBottom(null);
    }


    /**
     * Make the entire application draggable by holding down the left click button and dragging
     */
    private void makeStageDrageable() {
        borderPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        borderPane.setOnMouseDragged(event -> {
            JavaFxApplication.stage.setX(event.getScreenX() - xOffset);
            JavaFxApplication.stage.setY(event.getScreenY() - yOffset);
            JavaFxApplication.stage.setOpacity(0.7f);
        });
        borderPane.setOnDragDone((e) -> {
            JavaFxApplication.stage.setOpacity(1.0f);
        });
        borderPane.setOnMouseReleased((e) -> {
            JavaFxApplication.stage.setOpacity(1.0f);
        });
    }

    @FXML
    private void onClick(ActionEvent event) {
        var source = event.getSource();
        FXMLLoader loader = null;
        try {
            if (source == registerButton) {
                loader = SceneRouter.routeScene(SceneRouter.FXML_ROOT, SceneRouter.Routes.MAIN);
                validateUser();
            } else if (source == signInButton) {
                loader = SceneRouter.routeScene(SceneRouter.FXML_ROOT, SceneRouter.Routes.LOGIN);
            }
            if ( loader != null ){
                sceneRouter.loadStage(loader);
            }
        } catch (NullPointerException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            pushAlert(
                    "An Error Occured!",
                    "Oops, it's us not you",
                    "There was an issue opening the specified page, if this error persists, contact the author",
                    Alert.AlertType.ERROR,
                    null);
        }
    }

    /**
     * Simple form validation
     */
    @SneakyThrows
    private void validateUser() {
        User user = new User();
        String login = userNameField.getText().trim();
        String password1 = passwordField.getText().trim();
        String password2 = passwordFieldRepeat.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();

        if ( login.isEmpty() )
        {
            setErrorLabel("Login field must be filled");
            return;
        }
        if ( !email.matches(EMAIL_REGEX) )
        {
            setErrorLabel(email + " is not a valid email address");
            return;
        }
        if ( password1.equals(password2)) {
            user.setLogin(login);
            user.setPassword(password1);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            userService.registerAndAuthenticateUser(user, () -> {
                try {
                    sceneRouter.loadStage(SceneRouter.routeScene(SceneRouter.FXML_ROOT, SceneRouter.Routes.MAIN));
                } catch (NullPointerException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                    pushAlert(
                            "An Error Occured!",
                            "Oops, it's us not you",
                            "There was an issue opening the specified page, if this error persists, contact the author",
                            Alert.AlertType.ERROR,
                            null);
                }
            },
                    () -> setErrorLabel("Unable to create account, Username already exists"),
                    ()-> setErrorLabel("Unable to create account, email already exists")
            );
        } else {
            setErrorLabel("Passwords do not match");
            return;
        }
    }

    /**
     * Sets the error lable during form validation
     * @param error Error Message
     */
    private void setErrorLabel(String error) {
        errorLabel.setText(error);
        borderPane.setBottom(errorLabel);
    }


    @FXML
    public BorderPane borderPane;
    @FXML
    public VBox leftVBox;
    @FXML
    public JFXButton signInButton;
    @FXML
    public VBox rightVBox;
    @FXML
    public JFXTextField userNameField;
    @FXML
    public JFXPasswordField passwordField;
    @FXML
    public JFXPasswordField passwordFieldRepeat;
    @FXML
    public JFXTextField firstNameField;
    @FXML
    public JFXTextField lastNameField;
    @FXML
    public JFXTextField emailField;
    @FXML
    public JFXButton registerButton;
    @FXML
    public Label errorLabel;
}
