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
import com.triippztech.cashvest.domain.Portfolio;
import com.triippztech.cashvest.domain.Stock;
import com.triippztech.cashvest.domain.User;
import com.triippztech.cashvest.service.IEXService;
import com.triippztech.cashvest.service.PortfolioService;
import com.triippztech.cashvest.service.StockService;
import com.triippztech.cashvest.service.UserService;
import com.triippztech.cashvest.utilities.SceneRouter;
import com.triippztech.cashvest.widgets.ProcessingDialog;
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

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The Portfolio controller which is used for the Portolio View
 * and managing and handing off actions to the PortfolioService
 * @author Mark Tripoli
 * @version 1.0.0
 * @since 2020-01-30
 */
@Controller
public class ManagePortfoliosController implements Initializable {

    private final PortfolioService portfolioService;
    private final StockService stockService;
    private final UserService userService;
    private final IEXService iexService;
    private final SceneRouter sceneRouter;

    private Task copyWorker;
    private User user;

    public static Portfolio selectedPortfolio;

    public ManagePortfoliosController(PortfolioService portfolioService, StockService stockService, UserService userService,
                                      IEXService iexService, SceneRouter sceneRouter) {
        this.portfolioService = portfolioService;
        this.stockService = stockService;
        this.userService = userService;
        this.iexService = iexService;
        this.sceneRouter = sceneRouter;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        copyWorker = initializePages();
        ProcessingDialog.processingDialog(copyWorker);
        addListeners();
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

    private void addListeners() {
        portfoliosChoiceBox.getSelectionModel()
                .selectedIndexProperty()
                .addListener((observable, oldValue, newValue) -> {
                    portfoliosChoiceBox.getSelectionModel().select(newValue.intValue());
                    changeTotalValue();
                    changePortfolioName();
                    loadTable();
                });
    }

    private void initAll() {
        loadUser();
        initializePortfolioChoices();
        changeTotalValue();
        loadTable();
        changePortfolioName();
    }

    private void loadUser() {
        this.user = userService.getSignedInUser();
    }

    private void initializePortfolioChoices() {
        ObservableList<Portfolio> options = FXCollections.observableArrayList(this.user.getPortfolios());
        portfoliosChoiceBox.setItems(options);
        portfoliosChoiceBox.getSelectionModel().selectFirst();
    }

    private void loadTable() {
        List<Stock> stockList = new ArrayList<>(this.portfoliosChoiceBox.getSelectionModel().getSelectedItem().getPortfolioStocks());
        for ( Stock stock : stockList ) {
            stock.setValue ( iexService.getStockPrice(stock.getSymbol()) );
        }

        ObservableList<Stock> data = FXCollections.observableList(stockList);

        /* initialize and specify table column */
        assetSymbolCol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        assetSymbolCol.getStyleClass().add("table-column");

        assetNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assetNameCol.getStyleClass().add("table-column");

        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueCol.getStyleClass().add("table-column");

        valueCol.getStyleClass().add("table-column");

        addTableDeleteButtons();
        editCol.getStyleClass().add("table-column");

        /* add column to the tableview and set its items */
        portfolioTable.setItems(data);
        portfolioTable.setSelectionModel(null);
    }

    private void changeTotalValue() throws NullPointerException{
        Portfolio selectedItem = portfoliosChoiceBox.getSelectionModel().getSelectedItem();
        BigDecimal totalVal = portfolioService.getPortfolioValue ( selectedItem );
        totalValueLabel.setText (  "$" + totalVal.toPlainString() );
    }

    private void changePortfolioName() {
        portfolioNameCol.setText (portfoliosChoiceBox.getSelectionModel().getSelectedItem().getName() );
    }

    private void addTableEditButtons() {

    }

    private void addTableDeleteButtons() {
        Callback<TableColumn<Stock, Void>, TableCell<Stock, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Stock, Void> call(final TableColumn<Stock, Void> param) {
                return new TableCell<>() {

                    private final JFXButton btn = new JFXButton("Delete");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Stock stock = getTableView().getItems().get(getIndex());
                            deleteStockFromPortfolio(stock);
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

    private void deleteStockFromPortfolio(Stock stock) {
        Portfolio portfolio = portfoliosChoiceBox.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove Stock");
        alert.setContentText(
                String.format("Would you really like to remove: %S from %S?", stock.getSymbol(), portfolio)
        );
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(okButton, cancelButton);
        alert.showAndWait().ifPresent( buttonType -> {
            if ( buttonType == okButton ) {
                    Portfolio newPortfolio = portfolio.removeStock(stock);
                    portfolioService.save(newPortfolio);
                try {
                    loadUser();
                    this.portfoliosChoiceBox.getItems().clear();
                    initializePortfolioChoices();
                } catch (NullPointerException ignored){}
            }
        });
    }


    @FXML
    public void onClick(ActionEvent event) {
        if ( event.getSource() == addPortfolioBtn ) {
            sceneRouter.loadDialog(SceneRouter.routeScene(SceneRouter.FXML_DIALOG, SceneRouter.Routes.ADD_PORTFOLIO));
        } else if ( event.getSource() == addAssetBtn ) {
            selectedPortfolio = portfoliosChoiceBox.getSelectionModel().getSelectedItem();
            sceneRouter.loadDialog(SceneRouter.routeScene(SceneRouter.FXML_DIALOG, SceneRouter.Routes.ADD_PORTFOLIO_ASSET));
        }

        /**
         * Terrible work around, since we attached a selection listener to the box,
         * when we clear the selections, it will trigger an event. The selection == null
         * thorwing an NPE. So to handle the exception just throw this in for now.
         */
        try {
            loadUser();
            this.portfoliosChoiceBox.getItems().clear();
            initializePortfolioChoices();
        } catch (NullPointerException ignored){}
    }


    @FXML
    public JFXButton addPortfolioBtn;
    @FXML
    public BorderPane borderPane;
    @FXML
    public TableView<Stock> portfolioTable;
    @FXML
    public TableColumn<Stock, String> assetSymbolCol;
    @FXML
    public TableColumn<Stock, String>  assetNameCol;
    @FXML
    public TableColumn<Stock, BigDecimal>  valueCol;
    @FXML
    public TableColumn<Stock, Void> editCol;
    @FXML
    public TableColumn<Stock, Void> deleteCol;
    @FXML
    public ChoiceBox<Portfolio> portfoliosChoiceBox;
    @FXML
    public JFXButton addAssetBtn;
    @FXML
    public Label totalValueLabel;
    @FXML
    public Label portfolioNameCol;
}
