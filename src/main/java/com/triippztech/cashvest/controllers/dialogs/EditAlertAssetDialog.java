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
import com.triippztech.cashvest.controllers.ManageAlertsController;
import com.triippztech.cashvest.domain.PriceAlert;
import com.triippztech.cashvest.domain.Stock;
import com.triippztech.cashvest.domain.User;
import com.triippztech.cashvest.domain.enumeration.AlertMethod;
import com.triippztech.cashvest.domain.enumeration.AlertThresholdType;
import com.triippztech.cashvest.service.AlertService;
import com.triippztech.cashvest.service.IEXService;
import com.triippztech.cashvest.service.StockService;
import com.triippztech.cashvest.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.triippztech.cashvest.utilities.AlertUtil.pushAlert;

@Controller
public class EditAlertAssetDialog implements Initializable {

    private final UserService userService;
    private final AlertService alertService;
    private final IEXService iexService;
    private final StockService stockService;

    public EditAlertAssetDialog(UserService userService, AlertService alertService,
                                IEXService iexService, StockService stockService) {
        this.userService = userService;
        this.alertService = alertService;
        this.iexService = iexService;
        this.stockService = stockService;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addContraintListeners();
        addChoiceBoxValues();
        addInitialValutes();
    }

    private void addContraintListeners() {
        // Decimal Values Only
        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                priceField.setText(oldValue);
            }
        });
    }

    private void addChoiceBoxValues() {
        alertThresholdChoiceBox.getItems().setAll( AlertThresholdType.values() );
        alertMethodChoiceBox.getItems().setAll( AlertMethod.values() );

        alertThresholdChoiceBox.setValue(ManageAlertsController.editPriceAlert.getAlertThresholdType());
        alertMethodChoiceBox.setValue(ManageAlertsController.editPriceAlert.getMethod());
    }

    private void addInitialValutes() {
        assetSymbolField.setText(ManageAlertsController.editPriceAlert.getAlertStock().getSymbol());
        priceField.setText(ManageAlertsController.editPriceAlert.getPrice().toPlainString());
    }

    private boolean formValid() {
        if ( priceField.getText().isEmpty() ) {
            pushAlert(
                    "Invalid Form",
                    "",
                    "Must include a price for alert trigger (in USD).",
                    Alert.AlertType.ERROR,
                    null);
            return false;
        }

        BigDecimal bigDecimal;
        try {
            bigDecimal = new BigDecimal(priceField.getText().trim());
        } catch ( NumberFormatException e ) {
            pushAlert(
                    "Invalid Form",
                    "",
                    e.getMessage(),
                    Alert.AlertType.ERROR,
                    null);
            return false;
        }

        if ( !priceValid ( ManageAlertsController.editPriceAlert.getAlertStock().getSymbol(), bigDecimal ) )
            return false;

        User user = userService.getSignedInUser();
        AlertMethod alertMethod = alertMethodChoiceBox.getSelectionModel().getSelectedItem();
        AlertThresholdType alertThresholdType = alertThresholdChoiceBox.getSelectionModel().getSelectedItem();

        if ( alertMethod == AlertMethod.SMS || alertMethod == AlertMethod.ALL_METHODS) {
            if ( user.getPhoneNumber() == null ) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No Phone Number");
                alert.setContentText("You must have a phone number to use SMS");
                alert.showAndWait();
                return false;
            }
        }

        ManageAlertsController.editPriceAlert.setMethod(alertMethod);
        ManageAlertsController.editPriceAlert.setAlertThresholdType(alertThresholdType);
        ManageAlertsController.editPriceAlert.setPrice(bigDecimal);


        if( confirmChoices(
                ManageAlertsController.editPriceAlert.getAlertStock().getSymbol(),
                alertMethod,
                alertThresholdType,
                bigDecimal,
                user)
        ) {
            alertService.save(ManageAlertsController.editPriceAlert);
            return true;
        }
        return false;
    }

    private boolean priceValid(String symbol, BigDecimal price) {
        BigDecimal currentPrice = iexService.getStockPrice(symbol);
        AlertThresholdType alertThresholdType = alertThresholdChoiceBox.getSelectionModel().getSelectedItem();
        switch ( alertThresholdType ) {
            case BELOW:
                if (currentPrice.compareTo(price) < 0)
                {
                    pushAlert(
                            "Invalid Price",
                            "",
                            "Alert price can not be more than current price if threshold is set to BELOW",
                            Alert.AlertType.ERROR,
                            null);
                    return false;
                }
                break;
            case ABOVE:
                if ( currentPrice.compareTo(price) > 0) {
                    pushAlert(
                            "Invalid Price",
                            "",
                            "Alert price can not be less than current price if threshold is set to ABOVE",
                            Alert.AlertType.ERROR,
                            null);
                    return false;
                }
                break;
        }
        return true;
    }

    private boolean confirmChoices(String stock, AlertMethod alertMethod, AlertThresholdType thresholdType,
                                   BigDecimal price, User user) {
        StringBuilder builder = new StringBuilder();
        builder.append("A Price Alert will be set for Asset=").append(stock).append(" when the price is ")
                .append( thresholdType.toString() ).append(" ")
                .append("$").append(price.toPlainString()).append(". ")
                .append("A notification will be sent to you via ").append(alertMethod).append(", to ");
        switch ( alertMethod ) {
            case SMS:
                builder.append( user.getPhoneNumber() ).append(" ");
                break;
            case EMAIL:
                builder.append( user.getEmail() ).append(" ");
                break;
            case PUSH_NOTIFICATION:
                builder.append("Your PC ");
                break;
            default:
                builder.append(user.getEmail()).append(", ").append(user.getPhoneNumber()).append(", ").append("and your PC, ");
                break;
        }

        builder.append("If this alert is triggered.").append("\n\n").append("Is this correct?");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Add New Alert");
        alert.setContentText(builder.toString());
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(okButton, cancelButton);
        Optional<ButtonType> buttonType = alert.showAndWait();
        return buttonType.get() == okButton;
    }

    @FXML
    public void onSubmit(ActionEvent event) {
        if ( formValid() )
            closeStage(event);
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    public BorderPane borderPane;
    @FXML
    public JFXTextField assetSymbolField;
    @FXML
    public ChoiceBox<AlertThresholdType> alertThresholdChoiceBox;
    @FXML
    public ChoiceBox<AlertMethod> alertMethodChoiceBox;
    @FXML
    public JFXTextField priceField;
    @FXML
    public JFXButton editButton;
}
