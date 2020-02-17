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
import com.triippztech.cashvest.domain.Stock;
import com.triippztech.cashvest.domain.User;
import com.triippztech.cashvest.domain.Watchlist;
import com.triippztech.cashvest.service.IEXService;
import com.triippztech.cashvest.service.StockService;
import com.triippztech.cashvest.service.UserService;
import com.triippztech.cashvest.service.WatchlistService;
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
import pl.zankowski.iextrading4j.api.stocks.Quote;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

// TODO FIX MANYTOMANY DELETIONS
/**
 * The Watchlist controller which is used for the Watchlist View
 * and managing and handing off actions to the WatchlistService
 * @author Mark Tripoli
 * @version 1.0.0
 * @since 2020-01-30
 */
@Controller
public class ManageWatchlistController implements Initializable {

    private final WatchlistService watchlistService;
    private final StockService stockService;
    private final UserService userService;
    private final IEXService iexService;
    private final SceneRouter sceneRouter;

    private Task copyWorker;
    private User user;

    public static Watchlist selectedWatchlist;


    public ManageWatchlistController(WatchlistService watchlistService, StockService stockService,
                                     UserService userService, IEXService iexService, SceneRouter sceneRouter) {
        this.watchlistService = watchlistService;
        this.stockService = stockService;
        this.userService = userService;
        this.iexService = iexService;
        this.sceneRouter = sceneRouter;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        copyWorker = initializePages();
        ProcessingDialog.processingDialog(copyWorker);
        addListeners();
    }

    /**
     * Returns a task to load all relevent components
     * which will display a loading dialog
     * @return
     */
    public Task initializePages() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                initAll();
                return true;
            }
        };
    }

    /**
     * Adds a listener to the watchlist choicebox for user selection
     */
    private void addListeners() {
        watchlistsChoiceBox.getSelectionModel()
                .selectedIndexProperty()
                .addListener((observable, oldValue, newValue) -> {
                    watchlistsChoiceBox.getSelectionModel().select(newValue.intValue());
                    changeWatchlistName();
                    loadTable();
                });
    }

    /**
     * Initializes all components
     */
    private void initAll() {
        loadUser();
        initializeWatchlistChoices();
        loadTable();
        changeWatchlistName();
    }

    /**
     * Gets the currently logged in user
     */
    private void loadUser() {
        this.user = userService.getSignedInUser();
    }

    /**
     * Load initial values into choicebox
     */
    private void initializeWatchlistChoices() {
        ObservableList<Watchlist> options = FXCollections.observableArrayList(this.user.getWatchlists());
        watchlistsChoiceBox.setItems(options);
        watchlistsChoiceBox.getSelectionModel().selectFirst();
    }

    /**
     * Loads the table full of stock information
     */
    private void loadTable() {
        List<Stock> stockList = new ArrayList<>(this.watchlistsChoiceBox.getSelectionModel().getSelectedItem().getWatchlistStocks());
        for ( Stock stock : stockList ) {
            Quote quote = iexService.getQuoteForAsset(stock.getSymbol());
            stock.setValue ( quote.getLatestPrice() );
            stock.setPercentChange( quote.getChangePercent() );
        }

        ObservableList<Stock> data = FXCollections.observableList(stockList);

        /* initialize and specify table column */
        assetSymbolCol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        assetSymbolCol.getStyleClass().add("table-column");

        assetNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assetNameCol.getStyleClass().add("table-column");

        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueCol.getStyleClass().add("table-column");

        percChangeCol.setCellValueFactory(new PropertyValueFactory<>("percentChange"));
        percChangeCol.getStyleClass().add("table-column");

        addTableDeleteButtons();

        /* add column to the tableview and set its items */
        watchlistTabel.setItems(data);
        watchlistTabel.setSelectionModel(null);
    }

    /**
     * Changes the watchlist lable to the currently selected watchlist
     */
    private void changeWatchlistName() {
        watchlistNameLabel.setText (watchlistsChoiceBox.getSelectionModel().getSelectedItem().getListName() );
    }

    /**
     * Adds a delete button to each table cell with a callback for when hit
     */
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

    /**
     * Deletes a particular stock from the selected portfolio
     * @param stock Stock to delete
     */
    private void deleteStockFromPortfolio(Stock stock) {
        Watchlist watchlist = watchlistsChoiceBox.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove Stock");
        alert.setContentText(
                String.format("Would you really like to remove: %S from %S?", stock.getSymbol(), watchlist)
        );
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(okButton, cancelButton);
        alert.showAndWait().ifPresent( buttonType -> {
            if ( buttonType == okButton ) {
                watchlistService.save(watchlist.removeStock(stock));

                try {
                    loadUser();
                    this.watchlistsChoiceBox.getItems().clear();
                    initializeWatchlistChoices();
                } catch (NullPointerException ignored){}
            }
        });
    }


    @FXML
    public void onClick(ActionEvent event) {
        if ( event.getSource() == addWatchlistBtn ) {
            sceneRouter.loadDialog(SceneRouter.routeScene(SceneRouter.FXML_DIALOG, SceneRouter.Routes.ADD_WATCHLIST));
        } else if ( event.getSource() == addAssetBtn ) {
            selectedWatchlist = watchlistsChoiceBox.getSelectionModel().getSelectedItem();
            sceneRouter.loadDialog(SceneRouter.routeScene(SceneRouter.FXML_DIALOG, SceneRouter.Routes.ADD_WATCHLIST_ASSET));
        }

        /**
         * Terrible work around, since we attached a selection listener to the box,
         * when we clear the selections, it will trigger an event. The selection == null
         * thorwing an NPE. So to handle the exception just throw this in for now.
         */
        try {
            loadUser();
            this.watchlistsChoiceBox.getItems().clear();
            initializeWatchlistChoices();
        } catch (NullPointerException ignored){}
    }

    @FXML
    public BorderPane borderPane;
    @FXML
    public JFXButton addWatchlistBtn;
    @FXML
    public Label watchlistNameLabel;
    @FXML
    public TableView<Stock> watchlistTabel;
    @FXML
    public TableColumn<Stock, String> assetSymbolCol;
    @FXML
    public TableColumn<Stock, String> assetNameCol;
    @FXML
    public TableColumn<Stock, BigDecimal> valueCol;
    @FXML
    public TableColumn<Stock, BigDecimal> percChangeCol;
    @FXML
    public TableColumn<Stock, Void> deleteCol;
    @FXML
    public ChoiceBox<Watchlist> watchlistsChoiceBox;
    @FXML
    public JFXButton addAssetBtn;

}
