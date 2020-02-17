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
import com.triippztech.cashvest.domain.PriceAlert;
import com.triippztech.cashvest.domain.Stock;
import com.triippztech.cashvest.domain.User;
import com.triippztech.cashvest.domain.enumeration.AlertMethod;
import com.triippztech.cashvest.domain.enumeration.AlertThresholdType;
import com.triippztech.cashvest.service.AlertService;
import com.triippztech.cashvest.service.IEXService;
import com.triippztech.cashvest.service.StockService;
import com.triippztech.cashvest.service.UserService;
import com.triippztech.cashvest.utilities.SceneRouter;
import com.triippztech.cashvest.widgets.ProcessingDialog;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import org.springframework.stereotype.Controller;
import pl.zankowski.iextrading4j.api.stocks.Quote;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

/**
 * The Alerts controller which is used for the Alerts View
 * Controls handing out actions to the alert service
 * @author Mark Tripoli
 * @version 1.0.0
 * @since 2020-01-30
 */
@Controller
public class ManageAlertsController implements Initializable {

    private final AlertService alertService;
    private final StockService stockService;
    private final UserService userService;
    private final IEXService iexService;
    private final SceneRouter sceneRouter;

    private Task copyWorker;
    private User user;
    public static PriceAlert editPriceAlert;

    public ManageAlertsController(AlertService alertService, StockService stockService, UserService userService,
                                  IEXService iexService, SceneRouter sceneRouter) {
        this.alertService = alertService;
        this.stockService = stockService;
        this.userService = userService;
        this.iexService = iexService;
        this.sceneRouter = sceneRouter;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        copyWorker = initializePages();
        ProcessingDialog.processingDialog(copyWorker);
    }

    public Task initializePages() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                initAll();
                return true;
            }
        };
    }

    private void initAll() {
        loadUser();
        loadTable();
    }

    private void loadUser() {
        this.user = userService.getSignedInUser();
    }


    private void loadTable() {
        alertTable.getItems().clear();

        List<PriceAlert> priceAlerts = new ArrayList<>(this.user.getPriceAlerts());
        for ( PriceAlert priceAlert : priceAlerts ) {
            Quote quote = iexService.getQuoteForAsset(priceAlert.getAlertStock().getSymbol());
            priceAlert.setCurrentPrice ( quote.getLatestPrice() );
        }

        ObservableList<PriceAlert> data = FXCollections.observableList(priceAlerts);

        /* initialize and specify table column */
        assetSymbolCol.setCellValueFactory(((Callback<TableColumn.CellDataFeatures<PriceAlert, String>, ObservableValue<String>>) param -> {
            Stock stock = param.getValue().getAlertStock();
            return new SimpleStringProperty(stock.getSymbol());
        }));
        assetSymbolCol.getStyleClass().add("table-column");

        methodCol.setCellValueFactory(new PropertyValueFactory<>("method"));
        methodCol.getStyleClass().add("table-column");

        thresholdCol.setCellValueFactory(new PropertyValueFactory<>("alertThresholdType"));
        thresholdCol.getStyleClass().add("table-column");

        triggerPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        triggerPriceCol.getStyleClass().add("table-column");

        currentPriceCol.setCellValueFactory(new PropertyValueFactory<>("currentPrice"));
        currentPriceCol.getStyleClass().add("table-column");

        addTableEditButtons();
        addTableDeleteButtons();

        /* add column to the tableview and set its items */
        alertTable.setItems(data);
        alertTable.setSelectionModel(null);
    }

    private void addTableEditButtons() {
        Callback<TableColumn<PriceAlert, Void>, TableCell<PriceAlert, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<PriceAlert, Void> call(final TableColumn<PriceAlert, Void> param) {
                return new TableCell<>() {
                    private final JFXButton btn = new JFXButton("Edit");
                    {
                        btn.setStyle("-fx-text-fill: ORANGE");
                        btn.setOnAction((ActionEvent event) -> {
                            editPriceAlert = getTableView().getItems().get(getIndex());
                            sceneRouter.loadDialog(SceneRouter.routeScene(SceneRouter.FXML_DIALOG, SceneRouter.Routes.EDIT_ALERT_ASSET));
                            editPriceAlert = null;

                            try {
                                loadUser();
                                loadTable();
                            } catch (NullPointerException ignored){}
                        });
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
        editCol.setCellFactory(cellFactory);
    }

    private void addTableDeleteButtons() {
        Callback<TableColumn<PriceAlert, Void>, TableCell<PriceAlert, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<PriceAlert, Void> call(final TableColumn<PriceAlert, Void> param) {
                return new TableCell<>() {
                    private final JFXButton btn = new JFXButton("Delete");

                    {
                        btn.setStyle("-fx-text-fill: RED");
                        btn.setOnAction((ActionEvent event) -> {
                            PriceAlert priceAlert = getTableView().getItems().get(getIndex());
                            deleteAlertFromUser(priceAlert);
                        });
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
        deleteCol.setCellFactory(cellFactory);
    }

    private void deleteAlertFromUser(PriceAlert priceAlert) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove Stock");
        alert.setContentText(
                String.format("Would you really like to delete alert for: %S?", priceAlert.getAlertStock().getSymbol())
        );
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(okButton, cancelButton);
        alert.showAndWait().ifPresent( buttonType -> {
            if ( buttonType == okButton ) {
                user.removePriceAlert(priceAlert);
                userService.save(user);

                try {
                    loadUser();
                    loadTable();
                } catch (NullPointerException ignored){}
            }
        });
    }


    @FXML
    public void onClick(ActionEvent event) {
        if ( event.getSource() == addAlertBtn ) {
            sceneRouter.loadDialog(SceneRouter.routeScene(SceneRouter.FXML_DIALOG, SceneRouter.Routes.ADD_ALERT_ASSET));
        }

        /**
         * Terrible work around, since we attached a selection listener to the box,
         * when we clear the selections, it will trigger an event. The selection == null
         * thorwing an NPE. So to handle the exception just throw this in for now.
         */
        try {
            loadUser();
            loadTable();
        } catch (NullPointerException ignored){}
    }

    @FXML
    public BorderPane borderPane;
    @FXML
    public TableView<PriceAlert> alertTable;
    @FXML
    public TableColumn<PriceAlert, String> assetSymbolCol;
    @FXML
    public TableColumn<PriceAlert, AlertMethod> methodCol;
    @FXML
    public TableColumn<PriceAlert, AlertThresholdType> thresholdCol;
    @FXML
    public TableColumn<PriceAlert, BigDecimal> triggerPriceCol;
    @FXML
    public TableColumn<PriceAlert, BigDecimal> currentPriceCol;
    @FXML
    public TableColumn<PriceAlert, Void> editCol;
    @FXML
    public TableColumn<PriceAlert, Void> deleteCol;
    @FXML
    public JFXButton addAlertBtn;
}
