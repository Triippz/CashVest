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

import com.jfoenix.controls.JFXListView;
import com.triippztech.cashvest.domain.NewsItem;
import com.triippztech.cashvest.domain.Portfolio;
import com.triippztech.cashvest.domain.TopPerformerItem;
import com.triippztech.cashvest.domain.User;
import com.triippztech.cashvest.service.IEXService;
import com.triippztech.cashvest.service.PortfolioService;
import com.triippztech.cashvest.service.UserService;
import com.triippztech.cashvest.widgets.ProcessingDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import org.controlsfx.dialog.ProgressDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import pl.zankowski.iextrading4j.api.stocks.Quote;
import pl.zankowski.iextrading4j.api.stocks.v1.News;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The Home controller which is used for the Home View
 * @author Mark Tripoli
 * @version 1.0.0
 * @since 2020-01-30
 */
@Controller
public class HomeController implements Initializable {

    private final Logger log = LoggerFactory.getLogger(HomeController.class);

    private final IEXService iexService;
    private final UserService userService;
    private final PortfolioService portfolioService;

    private Task copyWorker;

    public HomeController(IEXService iexService, UserService userService, PortfolioService portfolioService) {
        this.iexService = iexService;
        this.userService = userService;
        this.portfolioService = portfolioService;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        progressDialogue();

    }

    private void progressDialogue(){
        copyWorker = initializePages();
        ProcessingDialog.processingDialog(copyWorker);
    }

    public Task initializePages() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                initNewsTable();
                initTickerList();
                initPositionsTable();
                return true;
            }
        };
    }

    private void initNewsTable() {
        try {
            List<News> news = iexService.getNews();
            List<NewsItem> newsItems = new ArrayList<>();

            for (News item : news)
                newsItems.add(NewsItem.create(item.getImage(), item.getUrl(), item.getHeadline(), item.getSummary()));

            ObservableList<NewsItem> data = FXCollections.observableList(newsItems);

            /* initialize and specify table column */
            TableColumn<NewsItem, ImageView> firstColumn = new TableColumn<>("Images");
            firstColumn.setCellValueFactory(new PropertyValueFactory<>("articleImage"));
            firstColumn.prefWidthProperty().bind(newsTable.widthProperty().multiply(0.2));
            firstColumn.getStyleClass().add("table-column");

            TableColumn<NewsItem, Hyperlink> secondColumn = new TableColumn<>("Headline");
            secondColumn.setCellValueFactory(new PropertyValueFactory<>("articleLink"));
            secondColumn.prefWidthProperty().bind(newsTable.widthProperty().multiply(0.8));
            secondColumn.getStyleClass().add("table-column");

            /* add column to the tableview and set its items */
            newsTable.getColumns().add(firstColumn);
            newsTable.getColumns().add(secondColumn);
            newsTable.setItems(data);
            newsTable.setSelectionModel(null);
            newsTable.getStyleClass().add("noheader");
        } catch (Exception e) {
            log.error("Error receiving news from IEX: " + e.getMessage());
            newsTable.setPlaceholder(new Label("Error retrieving news feed"));
        }
    }

    private void initPositionsTable() {
        User user = this.userService.getSignedInUser();
        if ( user.getPortfolios() == null ) {
            this.positionsTable.setPlaceholder(new Label("No Positions"));
        }

        for ( Portfolio portfolio : user.getPortfolios() ) {
            portfolio.setTotalValue(portfolioService.getPortfolioValue(portfolio));
        }


        ObservableList<Portfolio> data = FXCollections.observableList ( new ArrayList<>( user.getPortfolios() ) );
        /* initialize and specify table column */
        portfolioNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        portfolioNameCol.getStyleClass().add("table-column-portfolios-name");

        totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("totalValue"));
        totalPriceCol.getStyleClass().add("table-column-portfolios-value");

        /* add column to the tableview and set its items */
        positionsTable.setItems(data);
        positionsTable.setSelectionModel(null);
        positionsTable.getStyleClass().add("noheader");
    }

    private void initTickerList() {
        final List<Quote> quotes = iexService.getTopPerformers();
        for ( Quote quote : quotes )
            if ( quote.getChangePercent() != null )
                topListView.getItems().add(TopPerformerItem.create(quote));
        topListView.setStyle("-fx-background-color: #0A014F");
    }

    @FXML
    public BorderPane centerBorderPane;
    @FXML
    public TableView<Portfolio> positionsTable;
    @FXML
    public TableColumn<Portfolio, String> portfolioNameCol;
    @FXML
    public TableColumn<Portfolio, Long> numSharesCol;
    @FXML
    public TableColumn<Portfolio, BigDecimal> totalPriceCol;
    @FXML
    public TableView<NewsItem> newsTable;
    @FXML
    public JFXListView<VBox> topListView;

}
