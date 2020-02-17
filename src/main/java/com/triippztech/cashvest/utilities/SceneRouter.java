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

package com.triippztech.cashvest.utilities;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static com.triippztech.cashvest.JavaFxApplication.stage;

/**
 * The SceneRouter aims to mimic routing used in typical frontend web frameworks.
 * Here we will declare all of our routes (scenes), so users do not have to rewrite
 * custom scene switch logic.
 *
 * @author Mark Tripoli
 */
@Component
public class SceneRouter {

    public static final String FILE_DELIM = File.separator;
    public static final String FXML_ROOT = FILE_DELIM + "fxml" + FILE_DELIM;
    public static final String FXML_DIALOG = FILE_DELIM + "fxml" + FILE_DELIM + "dialogs" + FILE_DELIM;
    private final ApplicationContext context;

    public SceneRouter(ApplicationContext context) {
        this.context = context;
    }


    public enum Routes {
        MAIN("Main.fxml"),
        HOME("Home.fxml"),
        MANAGE_ALERTS("ManageAlert.fxml"),
        MANAGE_PORTFOLIOS("ManagePortfolio.fxml"),
        ABOUT("About.fxml"),
        MANAGE_WATCHLISTS("ManageWatchlist.fxml"),
        LOGIN("Login.fxml"),
        REGISTER("Register.fxml"),
        EDIT_USER("EditUserDialog.fxml"),
        ADD_PORTFOLIO("AddPortfolioDialog.fxml"),
        ADD_PORTFOLIO_ASSET("AddPortfolioAssetDialog.fxml"),
        ADD_WATCHLIST("AddWatchlistDialog.fxml"),
        ADD_WATCHLIST_ASSET("AddWatchlistAssetDialog.fxml"),
        ADD_ALERT_ASSET("AddAlertAssetDialog.fxml"),
        EDIT_ALERT_ASSET("EditAlertAssetDialog.fxml");

        private String route;

        Routes ( String route ) {
            this.route = route;
        }

        public String getRoute() {
            return this.route;
        }
    }

    /**
     * Routes UI to a new scene
     * @param path The file path leading to fxml file
     * @param route The route (scene) to switch to
     */
    @SneakyThrows
    public Parent route(String path, Routes route)  {
        FXMLLoader fxmlLoader = SceneRouter.routeScene(path, route);
        fxmlLoader.setControllerFactory(context::getBean);
        return fxmlLoader.load();
    }

    public static FXMLLoader routeScene(String path, Routes route) throws NullPointerException{
        return new FXMLLoader(SceneRouter.class.getResource( path + route.route ));
    }

    @SneakyThrows
    public void loadStage(FXMLLoader fxmlLoader)  {
        fxmlLoader.setControllerFactory(context::getBean);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene ( root );
        stage.setScene ( scene );
    }

    @SneakyThrows
    public void loadDialog(FXMLLoader fxmlLoader) {
        fxmlLoader.setControllerFactory(context::getBean);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene ( root );
        Stage newStage = new Stage();
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.setResizable(false);
        newStage.setScene ( scene );
        newStage.showAndWait();
    }
}
