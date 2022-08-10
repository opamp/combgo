package combgo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.Comparator;

public class VideoList {
    private String videoNumber;
    private List<GoProVideo> filelist;
    
    VideoList() {
        this.videoNumber = "0000";
        this.filelist = new LinkedList<GoProVideo>();
    }

    public void put(GoProVideo videofile) {
        filelist.add(videofile);
    }

    public void setVideoNumber(String numberstr) {
        this.videoNumber = numberstr;
    }

    public String getVideoNumber() {
        return this.videoNumber;
    }

    public int length() {
        return filelist.size();
    }

    public void sort() {
        this.filelist.sort(Comparator.comparingInt(GoProVideo::getChapterNumber));
    }

    public List<GoProVideo> getGoProVideoList() {
        return this.filelist;
    }
    
    public File generateListFile(File output) {
        try(FileWriter writer = new FileWriter(output)) {
            for(GoProVideo vfile : this.filelist) {
                writer.write("file " + vfile.getFilePathString() + "\n");
            }
        }catch(IOException e) {
            return null;
        }
        return output;
    }

    public static List<VideoList> makeVideoLists(File target) {
        LinkedList<VideoList> rtn = new LinkedList<VideoList>();
        if(!target.isDirectory()){
            return rtn;
        }

        List<File> allfiles = Arrays.asList(target.listFiles());
        List<GoProVideo> goprofiles = allfiles.stream() 
            .map(mp4file -> new GoProVideo(mp4file)) 
            .filter(mp4file -> mp4file.isGoProMp4()) 
            .collect(Collectors.toList());
        Map<String, List<GoProVideo>> videoNumbers = goprofiles
            .stream()
            .collect(Collectors.groupingBy(GoProVideo::getVideoNumberString));

        for(Map.Entry<String, List<GoProVideo>> elem : videoNumbers.entrySet()) {
            VideoList lst = new VideoList();
            lst.setVideoNumber(elem.getKey());
            elem.getValue().stream().forEach(vfile -> lst.put(vfile));
            lst.sort();
            rtn.add(lst);
        }
        
        return rtn;
    }
}
