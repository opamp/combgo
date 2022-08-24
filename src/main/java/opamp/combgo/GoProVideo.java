package opamp.combgo;

import java.lang.Integer;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoProVideo {
    private File videofile;
    private String videoType;
    private String videoNumber;
    private String videoChapter;

    GoProVideo(File f) {
        this.videoType = "";
        this.videoNumber = "";
        this.videoChapter = "";
        this.setFile(f);
    }

    private void checkFileData() {
        String filename = this.getName();
        String head2 = filename.substring(0, 2);
        String head4 = filename.substring(0, 4);
        
        if(head2.equals("GH")) {
            this.videoType = "GH";
            this.videoChapter = filename.substring(2, 4);
            this.videoNumber = filename.substring(4, 8);
        }else if(head2.equals("GX")) {
            this.videoType = "GX";
            this.videoChapter = filename.substring(2, 4);
            this.videoNumber = filename.substring(4, 8);
        }else if(head2.equals("GP")) {
            this.videoType = "GP";
            this.videoChapter = filename.substring(2, 4);
            this.videoNumber = filename.substring(4, 8);
        }else if(head4.equals("GOPR")) {
            this.videoType = "GOPR";
            this.videoChapter = "00";
            this.videoNumber = filename.substring(4, 8);
        }
    }

    public void setFile(File f) {
        this.videofile = f;
        if(this.isGoProMp4()) {
            this.checkFileData();
        }
    }
    
    public File getFile() {
        return this.videofile;
    }
    
    public String getName() {
        return this.videofile.getName();
    }
    
    public String getFilePathString() {
        return this.videofile.getAbsolutePath();
    }

    public boolean isGoProMp4() {
        Pattern ptn = Pattern.compile("^(GH(\\w\\w\\d{6}|\\d{4})|GX(\\w\\w\\d{6}|\\d{6})|GOPR\\d{4}|GP\\d{6})\\.(mp|MP)4$");
        Matcher m = ptn.matcher(this.getName());
        return m.find();
    }
    
    public String getType() {
        return this.videoType;
    }

    public String getVideoNumberString() {
        return this.videoNumber;
    }

    public int getVideoNumber() {
        try{
            if(this.videoNumber.length() > 0) {
                return Integer.parseInt(this.videoNumber);
            }else {
                return -1;
            }
        }catch(Exception e) {
            return -1;
        }
    }

    public String getChapterNumberString() {
        return this.videoChapter;
    }

    public int getChapterNumber() {
        try{
            if(this.videoChapter.length() > 0) {
                return Integer.parseInt(this.videoChapter);
            }else {
                return -1;
            }
        }catch(Exception e) {
            return -1;
        }
    }
}
