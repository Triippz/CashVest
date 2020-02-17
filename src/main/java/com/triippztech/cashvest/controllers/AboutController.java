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

import com.triippztech.cashvest.utilities.SceneRouter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The About controller which is used for the About View
 * @author Mark Tripoli
 * @version 1.0.0
 * @since 2020-01-30
 */
@Controller
public class AboutController implements Initializable {


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadWebView();
    }

    @SneakyThrows
    private void loadWebView() {
        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();

        URL url = this.getClass().getResource(String.format("%shtml%sabout.html",
                SceneRouter.FILE_DELIM,
                SceneRouter.FILE_DELIM));
        webEngine.load(url.toString());

        borderPane.setCenter(browser);

    }

    @FXML
    public BorderPane borderPane;
}
