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

package com.triippztech.cashvest.widgets;


import javafx.concurrent.Task;
import javafx.stage.StageStyle;
import org.controlsfx.dialog.ProgressDialog;

public class ProcessingDialog {

    private Task copyWorker;

    public static void processingDialog(Task copyWorker) {
        ProcessingDialog progressDialog = new ProcessingDialog();
        progressDialog.copyWorker = copyWorker;

        ProgressDialog dialog = new ProgressDialog(copyWorker);
        dialog.initStyle(StageStyle.TRANSPARENT);

        dialog.setGraphic(null);
        dialog.setTitle("Loading . . .");
        dialog.setContentText("Loading . . .");
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        dialog.initStyle(StageStyle.UTILITY);
        new Thread(copyWorker).start();
        dialog.showAndWait();
    }
}
