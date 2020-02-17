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
import com.triippztech.cashvest.service.UserService;
import com.triippztech.cashvest.utilities.SceneRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.triippztech.cashvest.utilities.AlertUtil.pushAlert;

/**
 * The Login controller which is used for the Login View
 * @author Mark Tripoli
 * @version 1.0.0
 * @since 2020-01-30
 */
@Controller
public class LoginController implements Initializable {
    private double xOffset = 0;
    private double yOffset = 0;
    private final UserService userService;
    private final SceneRouter sceneRouter;

    public LoginController(UserService userService, SceneRouter sceneRouter) {
        this.userService = userService;
        this.sceneRouter = sceneRouter;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        makeStageDrageable();
        this.borderPane.setBottom(null);
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
        try {
            if (source == signInButton) {
                validateUser();
            } else if (source == signUpButton) {
                sceneRouter.loadStage(SceneRouter.routeScene(SceneRouter.FXML_ROOT, SceneRouter.Routes.REGISTER));
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

    private void validateUser() {
        userService.authenticateUser(
                userNameField.getText().trim(),
                passwordField.getText().trim(),
                () -> {
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
                () -> setErrorLabel("User with that username or email does not exist"),
                () -> setErrorLabel("Incorrect password entered"));
    }

    private void setErrorLabel(String text) {
        errorLabel.setText(text);
        borderPane.setBottom(errorLabel);
    }

    @FXML
    public VBox leftVBox;
    @FXML
    public JFXButton signUpButton;
    @FXML
    public VBox rightVBox;
    @FXML
    public JFXTextField userNameField;
    @FXML
    public JFXPasswordField passwordField;
    @FXML
    public JFXButton signInButton;
    @FXML
    public Label errorLabel;
    @FXML
    public BorderPane borderPane;

}
