/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.guiforxmlsearcher;

import com.mai.guiforxmlsearcher.operations_type_scene.SerachByTagBahavior;
import com.mai.guiforxmlsearcher.operations_type_scene.ValueOfListView;
import com.mai.guiforxmlsearcher.utils.OperationsType;
import com.mai.guiforxmlsearcher.text_area_appender.TextAreaAppender;
import com.mai.guiforxmlsearcher.utils.XMLUtils;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import org.apache.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author Sergey
 */
public class MainFXMLController implements Initializable {

    final static Logger logger = Logger.getLogger(MainFXMLController.class);
    @FXML
    private ComboBox<OperationsType> operationComboBox;
    @FXML
    private Label workPathName;
    @FXML
    private Label savePathName;
    @FXML
    private Button choiceWorkPathButton;
    @FXML
    private TextArea logTextArea;
    @FXML
    private ListView valuesListView;
    @FXML
    private ComboBox atributeComboBox;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button startButton;

    private SerachByTagBahavior serachByTagBahavior;
    private File workDirectory;
    private File saveDirectory;

    @FXML
    private void handleChoiceWorkPathAction(ActionEvent event) {
        DirectoryChooser fileChooser = new DirectoryChooser();
        workDirectory = fileChooser.showDialog(((Node) event.getTarget()).getScene().getWindow());
        if (workDirectory == null) {
            workPathName.setText("No Directory selected");
        } else {
            workPathName.setText(workDirectory.getAbsolutePath());
        }
        if (workDirectory == null) {
            logger.info("Укажите рабочую дирректорию!");
            return;
        }
        XMLUtils.getAllAtributeAndValues(workDirectory, progressBar, serachByTagBahavior);
//        logger.info("Найдено " + atrMap.size() + " различных атрибутов");
//        serachByTagBahavior.refrashElement(atrMap);
    }

    @FXML
    private void handleChoiceSavePathAction(ActionEvent event) {
        DirectoryChooser fileChooser = new DirectoryChooser();
        saveDirectory = fileChooser.showDialog(((Node) event.getTarget()).getScene().getWindow());
        if (saveDirectory == null) {
            savePathName.setText("No Directory selected");
        } else {
            savePathName.setText(workDirectory.getAbsolutePath());
        }
        if (workDirectory == null) {
            logger.info("Укажите рабочую дирректорию!");
            return;
        }
    }

    @FXML
    private void startOperation(ActionEvent event) {
        logicStartButton();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initData();
        initListeners();
    }

    private void initData() {
        operationComboBox.getItems().addAll(OperationsType.values());
        operationComboBox.setValue(OperationsType.SEARCH_BY_TAG);
        TextAreaAppender.setTextArea(logTextArea);

        serachByTagBahavior = new SerachByTagBahavior(valuesListView, atributeComboBox);
    }

    private void initListeners() {
        operationComboBox.valueProperty().addListener(new ChangeListener<OperationsType>() {
            @Override
            public void changed(ObservableValue<? extends OperationsType> observable, OperationsType oldValue, OperationsType newValue) {
                if (workDirectory == null) {
                    logger.info("Укажите рабочую дирректорию!");
                }
                XMLUtils.getAllAtributeAndValues(workDirectory, progressBar, serachByTagBahavior);
            }
        });
    }

    private void logicStartButton() {
        if (operationComboBox.getValue() == OperationsType.SEARCH_BY_TAG) {
            if (saveDirectory == null) {
                logger.info("Укажите дирректорию для сохранения!");
                return;
            }
            ValueOfListView valueOfListView = (ValueOfListView) valuesListView.getSelectionModel().getSelectedItem();
            if (valueOfListView == null) {
                logger.info("Выберете в списке значений атрибута необходимое!");
                return;
            }
            XMLUtils.copyFiles(valueOfListView.getFiles(), saveDirectory.getAbsolutePath(), progressBar);
        }
    }

}
