package combgo;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import javafx.event.ActionEvent;


public class Controller implements Initializable {
    @FXML
    private Button targetpathSelect;

    @FXML
    private Button ffmpegpathSelect;

    @FXML
    private Button startBtn;

    @FXML
    private Button exitBtn;

    @FXML
    private Label ffmpegpathMsg;

    @FXML
    private Label targetpathMsg;

    @FXML
    private TextField ffmpegpathText;

    @FXML
    private TextField targetpathText;

    @FXML
    private ComboBox<String> presetCombo;

    @FXML
    private CheckBox conCheck;

    @FXML
    private CheckBox removeorigCheck;

    @FXML
    private CheckBox removedustCheck;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.ffmpegpathText.setText("ffmpeg");
    }

    @FXML
    public void onClickSelectFFmpegPath(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select a ffmpeg execution file");
        File ffmpeg = chooser.showOpenDialog(null);
        if(ffmpeg != null) {
            this.ffmpegpathText.setText(ffmpeg.getPath());
        }
    }

    @FXML
    public void onClickSelectTargetPath(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select a target directory");
        File target = chooser.showDialog(null);
        if(target != null){
            this.targetpathText.setText(target.getPath());
        }
    }
    
    @FXML
    public void onClickStart(ActionEvent event) {
        System.out.println("Start");
    }

    @FXML
    public void onClickExit(ActionEvent event) {
        System.out.println("End");
    }
    
}
