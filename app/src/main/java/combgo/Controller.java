package combgo;

import java.io.File;
import java.net.URL;
import java.net.URI;
import java.lang.Runtime;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
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

    protected void ContentsDisable(boolean setval) {
        targetpathSelect.setDisable(setval);
        ffmpegpathSelect.setDisable(setval);
        startBtn.setDisable(setval);
        exitBtn.setDisable(setval);
        ffmpegpathText.setDisable(setval);
        targetpathText.setDisable(setval);
        presetCombo.setDisable(setval);
        conCheck.setDisable(setval);
        removeorigCheck.setDisable(setval);
        removedustCheck.setDisable(setval);
    }

    @FXML
    public void onClickStart(ActionEvent event) {
        boolean conversion = this.conCheck.isSelected();
        boolean removeorig = this.removeorigCheck.isSelected();
        boolean removedust = this.removedustCheck.isSelected();

        if(removedust) {
            File target = new File(this.targetpathText.getText());
            Pattern ptn = Pattern.compile(".+\\.(mp|MP)4$");
            for(File f : Arrays.asList(target.listFiles())) {
                if(!ptn.matcher(f.getName()).find()) {
                    f.delete();
                }
            }
        }        

        if(conversion) {
            this.ContentsDisable(true);
            HashMap<String, String> cmd_vals = new HashMap<String, String>();
            cmd_vals.put("FFMPEG", this.ffmpegpathText.getText());
            List<VideoList> vlists = VideoList.makeVideoLists(new File(this.targetpathText.getText()));
            List<String> cmdlst = new LinkedList<String>();
            List<File> tmp_vlists = new LinkedList<File>();

            for(VideoList vlist : vlists) {
                File tmp_vlist = vlist.generateListFile(new File(this.targetpathText.getText(), vlist.getVideoNumber() + "-combgo.txt"));
                File output = new File(this.targetpathText.getText(), vlist.getVideoNumber());
            
                if(tmp_vlist != null) {
                    cmd_vals.put("INPUTLIST", tmp_vlist.getAbsolutePath());
                    cmd_vals.put("OUTPUT", output.getAbsolutePath());
                    cmdlst.add(current.command(cmd_vals));
                    tmp_vlists.add(tmp_vlist);
                }else {
                    // error (skip)
                }
            }

            Task<Integer> cmd_task = new Task<Integer>() {
                @Override protected Integer call() throws Exception {
                    for(String cmd : cmdlst) {
                        try {
                            System.out.println("Run -> " + cmd);
                            Runtime runtime = Runtime.getRuntime();
                            Process proc = runtime.exec(cmd);
                            proc.waitFor();
                            proc.destroy();
                        }catch(Exception e) {
                            System.err.println("Failed to run " + cmd);
                        }
                    }

                    for(File vlist : tmp_vlists) {
                        vlist.delete();
                    }

                    if(removeorig) {
                        File target = new File(targetpathText.getText());
                        Pattern ptn = Pattern.compile("^(GH(\\w\\w\\d{6}|\\d{4})|GX(\\w\\w\\d{6}|\\d{6})|GOPR\\d{4}|GP\\d{6})\\.(mp|MP)4$");
                        for(File f : Arrays.asList(target.listFiles())) {
                            if(ptn.matcher(f.getName()).find()) {
                                f.delete();
                            }
                        }
                    }

                    ContentsDisable(false);
                    return 0;
                }
            };

            Thread t = new Thread(cmd_task);
            t.setDaemon(true);
            t.start();

        }

        // enable all btn and textfield.
    }

    @FXML
    public void onClickExit(ActionEvent event) {
        Platform.exit();
    }
    
}
