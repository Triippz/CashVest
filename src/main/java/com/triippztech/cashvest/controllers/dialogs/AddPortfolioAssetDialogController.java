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
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.triippztech.cashvest.controllers.ManagePortfoliosController;
import com.triippztech.cashvest.domain.Stock;
import com.triippztech.cashvest.service.IEXService;
import com.triippztech.cashvest.service.PortfolioService;
import com.triippztech.cashvest.service.StockService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;
import pl.zankowski.iextrading4j.api.stocks.Company;
import pl.zankowski.iextrading4j.api.stocks.Logo;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class AddPortfolioAssetDialogController implements Initializable {

    private final IEXService iexService;
    private final PortfolioService portfolioService;
    private final StockService stockService;

    private Company selectedCompany;

    public AddPortfolioAssetDialogController(IEXService iexService, PortfolioService portfolioService,
                                             StockService stockService) {
        this.iexService = iexService;
        this.portfolioService = portfolioService;
        this.stockService = stockService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void onSubmit(ActionEvent event) {
        if ( event.getSource() == searchButton )
            assetSearch();
        if ( event.getSource() == addButton ) {
            if ( formValid() )
                closeStage(event);
        }
    }

    private boolean formValid() {
        Stock stock = new Stock();
        stock.setSymbol(selectedCompany.getSymbol());
        stock.setName(selectedCompany.getCompanyName());
        stock.setExchange(selectedCompany.getExchange());

        stock = stockService.create(stock);
        ManagePortfoliosController.selectedPortfolio.addStock(stock);
        ManagePortfoliosController.selectedPortfolio =
                portfolioService.save(ManagePortfoliosController.selectedPortfolio);

        return true;
    }

    private void assetSearch() {
        if ( imageView.getImage() != null )
            imageView.setImage(null);

        selectedCompany = iexService.getAsset( searchTextField.getText().trim() );
        if ( selectedCompany == null ) {
            responseVBox.setVisible(true);
            descriptionTextArea.setVisible(false);
            assetNameLabel.setText("Unknown Asset: " + searchTextField.getText().trim().toUpperCase());
            assetNameLabel.setStyle("-fx-text-fill: RED");
            return;
        }

        Logo logo = iexService.getCompanyLogo(selectedCompany.getSymbol());
        Image image = new Image(logo.getUrl(), true);

        responseVBox.setVisible(true);
        descriptionTextArea.setVisible(true);
        assetNameLabel.setVisible(true);
        addButton.setVisible(true);

        assetNameLabel.setText(selectedCompany.getCompanyName() + " (" + selectedCompany.getSymbol() + ")");
        assetNameLabel.setStyle("-fx-text-fill: WHITE");
        descriptionTextArea.setText(selectedCompany.getDescription());
        imageView.setImage(image);
    }


    private void closeStage(ActionEvent event) {
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    public BorderPane borderPane;
    @FXML
    public JFXTextField searchTextField;
    @FXML
    public JFXButton searchButton;
    @FXML
    public VBox responseVBox;
    @FXML
    public Label assetNameLabel;
    @FXML
    public JFXTextArea descriptionTextArea;
    @FXML
    public JFXButton addButton;
    @FXML
    public ImageView imageView;

}
