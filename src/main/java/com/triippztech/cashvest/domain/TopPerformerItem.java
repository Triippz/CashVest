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

package com.triippztech.cashvest.domain;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import lombok.Getter;
import lombok.Setter;
import pl.zankowski.iextrading4j.api.stocks.Quote;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TopPerformerItem {
    @Getter
    @Setter
    private JFXButton button;
    @Getter
    @Setter
    private BigDecimal price;
    @Getter
    @Setter
    private String symbol;
    @Getter
    @Setter
    private BigDecimal percentChange;
    @Getter
    @Setter
    private Boolean isPositive = false;

    public static VBox create(Quote quote) {
        TopPerformerItem topPerformerItem = new TopPerformerItem();

        topPerformerItem.setSymbol(quote.getSymbol());
        topPerformerItem.setPercentChange(quote.getChangePercent().setScale(2, RoundingMode.CEILING));
        topPerformerItem.setPrice(quote.getLatestPrice().setScale(2, RoundingMode.CEILING));

        if ( topPerformerItem.getPercentChange().compareTo(new BigDecimal("0")) >= 0 )
            topPerformerItem.setIsPositive(true);

        VBox hBox = new VBox();
        Label priceLabel = new Label();
        Label percentChangeLabel = new Label();
        Label symbolLabel = new Label();

        priceLabel.setText("$" + topPerformerItem.getPrice().toPlainString());
        symbolLabel.setText(topPerformerItem.getSymbol());

        symbolLabel.setStyle("-fx-text-fill: white");
        if ( topPerformerItem.isPositive ) {
            percentChangeLabel.setText(topPerformerItem.getPercentChange().toPlainString() + "%");
            priceLabel.setStyle("-fx-text-fill: green");
            percentChangeLabel.setStyle("-fx-text-fill: green");
        } else {
            percentChangeLabel.setText(topPerformerItem.getPercentChange().toPlainString() + "%");
            priceLabel.setStyle("-fx-text-fill: red");
            percentChangeLabel.setStyle("-fx-text-fill: red");
        }

        hBox.getChildren().addAll(symbolLabel, priceLabel, percentChangeLabel);
        hBox.setStyle("-fx-background-color: #0A014F");
        return hBox;
    }


//    public static JFXButton create(Quote quote) {
//        TopPerformerItem topPerformerItem = new TopPerformerItem();
//
//        topPerformerItem.setSymbol(quote.getSymbol());
//        topPerformerItem.setPercentChange(quote.getChangePercent());
//        topPerformerItem.setPrice(quote.getLatestPrice());
//
//        if ( topPerformerItem.getPercentChange().compareTo(new BigDecimal("0")) >= 0 )
//            topPerformerItem.setIsPositive(true);
//
//        JFXButton button = new JFXButton();
//        HBox hBox = new HBox();
//        Label priceLabel = new Label();
//        Label percentChangeLabel = new Label();
//        Label symbolLabel = new Label();
//
//        priceLabel.setText("$" + topPerformerItem.getPrice().toPlainString());
//        symbolLabel.setText(topPerformerItem.getSymbol());
//
//        symbolLabel.setTextFill(Paint.valueOf("WHITE"));
//        if ( topPerformerItem.isPositive ) {
//            percentChangeLabel.setText(topPerformerItem.getPercentChange().toPlainString() + "%");
//            priceLabel.setTextFill(Paint.valueOf("GREEN"));
//            percentChangeLabel.setTextFill(Paint.valueOf("GREEN"));
//        } else {
//            percentChangeLabel.setText("- " + topPerformerItem.getPercentChange().toPlainString() + "%");
//            priceLabel.setTextFill(Paint.valueOf("RED"));
//            percentChangeLabel.setTextFill(Paint.valueOf("RED"));
//        }
//
//        percentChangeLabel.setStyle("-fx-background-color: #0A014F");
//        priceLabel.setStyle("-fx-background-color: #0A014F");
//        symbolLabel.setStyle("-fx-background-color: #0A014F");
//
//        hBox.getChildren().addAll(symbolLabel, priceLabel, percentChangeLabel);
//        hBox.setStyle("-fx-background-color: #0A014F");
//
//        button.setStyle("-fx-background-color: #0A014F");
//        button.setGraphic(hBox);
//        button.setWrapText(true);
//        return button;
//    }

}
