package combgo;

import java.io.File;
import java.net.URL;
import java.net.URI;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.util.List;
import java.util.HashMap;
import java.util.stream.Stream;
import java.util.stream.Collectors;


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
    private TextField presetreview;

    @FXML
    private ComboBox<String> presetCombo;

    @FXML
    private CheckBox conCheck;

    @FXML
    private CheckBox removeorigCheck;

    @FXML
    private CheckBox removedustCheck;

    private List<CommandGenerator> presets;

    private CommandGenerator current;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize contents
        this.ffmpegpathText.setText("ffmpeg");
        this.presetCombo.valueProperty().addListener(new ChangeListener<String>() {
                @Override public void changed(ObservableValue ov, String old_value, String new_value) {
                    List<CommandGenerator> result = presets.stream().filter(item -> item.getName().equals(new_value)).collect(Collectors.toList());
                    if(!result.isEmpty()) {
                        current = result.get(0);
                        presetreview.setText(current.getFormatString());
                    }
                }
            });
        
        // Initialize preset combobox values
        try {
            List<CommandGenerator> defaultpresets = ConfigLoader.load(new File(new URI(this.getClass().getResource("/defaultpresets.txt").toString())));
            File userconfig = ConfigLoader.getConfigFilePath();
            if(userconfig.isFile()){
                List<CommandGenerator> userpresets = ConfigLoader.load(ConfigLoader.getConfigFilePath());
                this.presets = Stream.concat(defaultpresets.stream(), userpresets.stream()).collect(Collectors.toList());
            }else{
                this.presets = defaultpresets;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        if(this.presets.isEmpty()){
            System.err.println("No presets are loaded");
            System.exit(1);
        }
        this.presetCombo.getItems().setAll(this.presets.stream().map(preset -> preset.getName()).collect(Collectors.toList()));
        this.presetCombo.setValue(this.presets.get(0).getName());
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
        HashMap<String, String> cmd_vals = new HashMap<String, String>();
        cmd_vals.put("FFMPEG", this.ffmpegpathText.getText());
        List<VideoList> vlists = VideoList.makeVideoLists(new File(this.targetpathText.getText()));

        for(VideoList vlist : vlists) {
            File tmp_vlist = vlist.generateListFile(new File(this.targetpathText.getText(), vlist.getVideoNumber() + ".txt"));
            if(tmp_vlist != null) {
                cmd_vals.put("INPUTLIST", tmp_vlist.getAbsolutePath());
                String cmd = current.command(cmd_vals);
            }else {
                // error (skip)
            }
        }
    }

    @FXML
    public void onClickExit(ActionEvent event) {
        Platform.exit();
    }
    
}
