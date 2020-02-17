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
import com.triippztech.cashvest.domain.Portfolio;
import com.triippztech.cashvest.domain.User;
import com.triippztech.cashvest.service.PortfolioService;
import com.triippztech.cashvest.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;


@Controller
public class AddPortfolioDialogController implements Initializable {

    private final UserService userService;
    private final PortfolioService portfolioService;

    public AddPortfolioDialogController(UserService userService, PortfolioService portfolioService) {
        this.userService = userService;
        this.portfolioService = portfolioService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void onSubmit(ActionEvent event) {
        if ( formValid() )
            closeStage(event);
    }

    private boolean formValid() {
        String name = portfolioNameField.getText().trim();
        String description = portfolioDescriptionField.getText().trim();

        if ( name.isEmpty() ) {
            errorLabel.setText("Name cannot be blank");
            errorLabel.setVisible(true);
            return false;
        }

        User user = userService.getSignedInUser();
        Portfolio portfolio = new Portfolio();
        portfolio.setPortfolioUser(user);
        portfolio.setDescription(description);
        portfolio.setName(name);

        try {
            portfolioService.createPortfolio(portfolio);
        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
            return false;
        }
        return true;
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    public BorderPane borderPane;
    @FXML
    public JFXButton createPortfolioBtn;
    @FXML
    public JFXTextField portfolioNameField;
    @FXML
    public JFXTextField portfolioDescriptionField;
    @FXML
    public Label errorLabel;
}
