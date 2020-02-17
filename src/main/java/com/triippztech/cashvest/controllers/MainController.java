/*
 * Copyright 2019 Mark Tripoli
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
import com.triippztech.cashvest.JavaFxApplication;
import com.triippztech.cashvest.domain.User;
import com.triippztech.cashvest.service.UserService;
import com.triippztech.cashvest.utilities.SceneRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.triippztech.cashvest.utilities.AlertUtil.pushAlert;

/**
 * The Main controller which is used for the controlling
 * the parent view. Also controls the side menu and handles
 * routing throughout the application
 * @author Mark Tripoli
 * @version 1.0.0
 * @since 2020-01-30
 */
@Controller
public class MainController implements Initializable {


    private double xOffset = 0;
    private double yOffset = 0;
    private boolean isSidebarOpen = true;
    private User user;
    private final UserService userService;
    private final SceneRouter sceneRouter;

    public MainController(UserService userService, SceneRouter sceneRouter) {
        this.userService = userService;
        this.sceneRouter = sceneRouter;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeStageDrageable();
        this.user = userService.getSignedInUser();
        setWelcomeLabel();

        try {
            Parent home = sceneRouter.route(SceneRouter.FXML_ROOT, SceneRouter.Routes.HOME);
            borderPane.setLeft(null);
            borderPane.setCenter(home);
        } catch (NullPointerException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            pushAlert(
                    "Critical Failure Launching CashVest!",
                    "Oops, it's us not you",
                    "There was an issue launching the application. This error has been logged for review. " +
                            "This Application will now close, please re-open and try again.",
                    Alert.AlertType.ERROR, () -> System.exit(-1));
        }

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

    /**
     * Action Event callback to open the side bar
     * @param event Event callback
     */
    @FXML
    private void openSidebar(ActionEvent event) {
        BorderPane border_pane = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        if (isSidebarOpen) {
            border_pane.setLeft(sidebar);
            isSidebarOpen = false;
        } else {
            border_pane.setLeft(null);
            isSidebarOpen = true;
        }
    }

    @FXML
    private void showNotifications()
    {
        // TODO add in logic

    }

    private void setWelcomeLabel() {
        this.sideBarName.setText(user.getFullName());
        this.welcomeLabel.setText("Welcome, " + user.getFullName() + "!");

        if (user.getImageUrl() != null) {
            Image image = new Image(user.getImageUrl(), true);
            image.progressProperty().addListener((observable, oldValue, newValue)
                    -> System.out.println("Progress: " + Math.rint(newValue.doubleValue() * 100) + "%"));
            userImage.setImage(image);
        }

    }

    /**
     * Action event which will route to a specified page
     * @param event Action Event Triggered
     */
    @FXML
    private void routePageClick(ActionEvent event)
    {
        var source = event.getSource();
        Parent parent = null;
        try {
            if (source == alertButton) {
                parent = sceneRouter.route(SceneRouter.FXML_ROOT, SceneRouter.Routes.MANAGE_ALERTS);
            } else if (source == portfolioButton) {
                parent = sceneRouter.route(SceneRouter.FXML_ROOT, SceneRouter.Routes.MANAGE_PORTFOLIOS);
            } else if (source == watchlistButton) {
                parent = sceneRouter.route(SceneRouter.FXML_ROOT, SceneRouter.Routes.MANAGE_WATCHLISTS);
            } else if (source == aboutButton) {
                parent = sceneRouter.route(SceneRouter.FXML_ROOT, SceneRouter.Routes.ABOUT);
            } else if (source == homeButton) {
                parent = sceneRouter.route(SceneRouter.FXML_ROOT, SceneRouter.Routes.HOME);
            }

            if ( parent != null )
                borderPane.setCenter(parent);
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

    @FXML
    public void onSignOut(ActionEvent event) {
        this.userService.signOutUser(this.user, () -> {
            try {
                sceneRouter.loadStage(SceneRouter.routeScene(SceneRouter.FXML_ROOT, SceneRouter.Routes.LOGIN));
            } catch (NullPointerException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                pushAlert(
                        "An Error Occured!",
                        "Oops, it's us not you",
                        "There was an issue during logout. Please try again.",
                        Alert.AlertType.ERROR,
                        null);
            }
        }, () -> {
            pushAlert(
                    "An Error Occured!",
                    "Oops, it's us not you",
                    "There was an issue during logout. Please try again.",
                    Alert.AlertType.ERROR,
                    null);
        });
    }

    @FXML
    public void editUser(ActionEvent event) {
        sceneRouter.loadDialog(SceneRouter.routeScene(SceneRouter.FXML_DIALOG, SceneRouter.Routes.EDIT_USER));
        user = userService.getSignedInUser();
        setWelcomeLabel();
    }

    @FXML
    private JFXButton notificationButton;
    @FXML
    private Button homeButton;
    @FXML
    private Button portfolioButton;
    @FXML
    private Button alertButton;
    @FXML
    private Button watchlistButton;
    @FXML
    private Button aboutButton;
    @FXML
    private VBox sidebar;
    @FXML
    private BorderPane borderPane;
    @FXML
    private HBox menubar;
    @FXML
    private JFXButton menuSettingsButton;
    @FXML
    private Button signOutButton;
    @FXML
    public JFXButton welcomeLabel;
    @FXML
    public JFXButton sideBarName;
    @FXML
    public ImageView userImage;
}
