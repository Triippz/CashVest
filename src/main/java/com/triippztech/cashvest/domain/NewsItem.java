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

import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;


import static com.triippztech.cashvest.utilities.AlertUtil.pushAlert;


public class NewsItem {
    @Getter
    @Setter
    private ImageView articleImage;
    @Getter
    @Setter
    private Hyperlink articleLink;
    @Getter
    @Setter
    private Label summary;
    @Setter
    @Getter
    private VBox vBox;

    public NewsItem(ImageView articleImage, Hyperlink articleLink, Label summary, VBox vBox) {
        this.articleImage = articleImage;
        this.articleLink = articleLink;
        this.summary = summary;
        this.vBox = vBox;
    }

    public NewsItem() {
    }

    public static NewsItem create(String imageUrl, String linkUrl, String header, String summary) {
        NewsItem newsItem = new NewsItem();
        Image image = new Image(imageUrl, true);
        newsItem.articleImage = new ImageView(image);
        newsItem.articleImage.setFitHeight(100);
        newsItem.articleImage.setFitWidth(100);

        newsItem.articleLink = new Hyperlink(header);
        newsItem.summary = new Label(summary);

        newsItem.vBox = new VBox();
        newsItem.vBox.getChildren().addAll(newsItem.articleLink, newsItem.summary);

        newsItem.articleLink.setOnAction(t -> {
            pushAlert(
                    "Unable to load webpage",
                    "This feature is coming soon",
                    "There is an existing SSL bug within the JRE, which may rear its head " +
                            "during a webview launch, so we are leaving it out for now.",
                    Alert.AlertType.INFORMATION,
                    null
            );
        });
        return newsItem;
    }

}
