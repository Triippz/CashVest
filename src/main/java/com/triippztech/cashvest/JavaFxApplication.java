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

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext context;

    public static Stage stage = null;

    /**
     * Any kind of work that does not run on the UI thread should go here
     * Items to prepare the application
     * @throws Exception
     */
    @Override
    public void init() throws Exception {
        ApplicationContextInitializer<GenericApplicationContext> initializer =
                ac -> {
                    ac.registerBean ( Application.class, () -> JavaFxApplication.this  );
                    ac.registerBean ( Parameters.class, this::getParameters );
                    ac.registerBean ( HostServices.class, this::getHostServices );
                };

        this.context = new SpringApplicationBuilder( )
                .sources( CashvestApplication.class )
                .initializers ( initializer )
                .run ( getParameters().getRaw().toArray ( new String[0] ) );
    }

    /**
     * The callback you get when the application is ready
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.context.publishEvent( new StageReadyEvent ( primaryStage ) );
        stage = primaryStage;
    }

    @Override
    public void stop() throws Exception {
        this.context.close();
        Platform.exit();
    }
}

class StageReadyEvent extends ApplicationEvent {

    public Stage getStage() {
        return ( Stage ) getSource();
    }

    public StageReadyEvent( Stage source) {
        super ( source );
    }
}
