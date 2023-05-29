package web.browser.fx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class FXMLDocumentController  implements Initializable{

    @FXML
    private Button back_button;

    @FXML
    private Button forward_button;

    @FXML
    private ProgressBar progressbar;

    @FXML
    private Button reload_button;

    @FXML
    private TextField url_field;

    @FXML
    private WebView webview;

    @FXML
    private Button zoom_in,load;

    @FXML
    private Button zoom_out;
    
    private WebEngine engine;
    private WebHistory history;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        

    }

    private void loadUrl() {
    engine = webview.getEngine();
    String url = url_field.getText();
    
     
    if(url.contains(".com") || url.contains(".net") || url.contains(".org")) {
        if(!url.contains("https://")){
            engine.load("https://" + url);
        }
        else{
        engine.load(url);}
    }
    
    else {  
        String searchQuery = "https://www.google.com/search?q=" + url.replace(" ", "+");
        engine.load(searchQuery);
        
    }
    
    progressbar.progressProperty().bind(engine.getLoadWorker().progressProperty());
    
    engine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
        if (newValue == Worker.State.SUCCEEDED) {
            System.out.println("Page has been loaded");

            history = webview.getEngine().getHistory();
            ObservableList<WebHistory.Entry> entries = history.getEntries();
            url_field.setText(entries.get(history.getCurrentIndex()).getUrl());

            Stage stage = (Stage) url_field.getScene().getWindow();
            stage.setTitle(entries.get(history.getCurrentIndex()).getTitle());
        } else if (newValue == Worker.State.FAILED) {
            System.out.println("Loading failed");
            engine.loadContent("Page Not Found");
        }
    });
}


    @FXML
    private void back() {
        history = webview.getEngine().getHistory();
        history.go(-1);
        ObservableList<WebHistory.Entry> entries = history.getEntries();
        url_field.setText(entries.get(history.getCurrentIndex()).getUrl());
    }

    @FXML
    private void reload() {
        engine.reload();
    }

    @FXML
    private void forward() {
        history = webview.getEngine().getHistory();
        history.go(1);
        ObservableList<WebHistory.Entry> entries = history.getEntries();
        url_field.setText(entries.get(history.getCurrentIndex()).getUrl());
    }

   @FXML
    private void txtEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            loadUrl();
        }
    }


    @FXML
    private void zoomIn() {
        webview.setZoom(webview.getZoom() + 0.10);
    }

    @FXML
    private void zoomOut() {
        webview.setZoom(webview.getZoom() - 0.10);
    }
}



