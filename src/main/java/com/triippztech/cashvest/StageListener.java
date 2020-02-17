/*
 * Copyright 2019 Mark Tripoli
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

package com.triippztech.cashvest;

import com.triippztech.cashvest.service.UserService;
import com.triippztech.cashvest.utilities.SceneRouter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StageListener implements ApplicationListener<StageReadyEvent> {

    private final String appTitle;
    private final Resource fxml;
    private final ApplicationContext context;
    private final UserService userService;
    private final SceneRouter sceneRouter;

    StageListener(
            @Value("${spring.application.ui.title}") String appTitle,
            @Value("classpath:/fxml/Login.fxml") Resource resource,
            ApplicationContext context, UserService userService, SceneRouter sceneRouter) {
        this.appTitle = appTitle;
        this.fxml = resource;
        this.context = context;
        this.userService = userService;
        this.sceneRouter = sceneRouter;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent stageReadyEvent) {
        this.userService.userLoggedIn(
                () -> {
                    try {
                        openStage(stageReadyEvent,  SceneRouter.Routes.MAIN);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                () -> {
                    try {
                        openStage(stageReadyEvent, SceneRouter.Routes.LOGIN);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public void openStage(StageReadyEvent stageReadyEvent, SceneRouter.Routes route) throws IOException {
        Stage stage = stageReadyEvent.getStage();
        FXMLLoader fxmlLoader = SceneRouter.routeScene(SceneRouter.FXML_ROOT, route);
        fxmlLoader.setControllerFactory(context::getBean);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(this.appTitle);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
}
