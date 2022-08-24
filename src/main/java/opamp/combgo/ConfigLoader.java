package opamp.combgo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ConfigLoader {
    public static File getConfigFilePath() {
        File current_path = new File(System.getProperty("user.dir"));
        if(current_path.isFile()){
            File parentdir = current_path.getParentFile();
            return new File(parentdir.getPath(), "config.txt");
        }else{
            return new File(current_path.getPath(), "config.txt");
        }
    }

    public static CommandGenerator parseLine(String line) {
        String sep = ":";
        String[] splitedstr = line.split(sep);
        if(splitedstr.length < 2) {
            return null;
        }else{
            CommandGenerator result = new CommandGenerator(splitedstr[0]);
            String optionstr = String.join(sep, Arrays.copyOfRange(splitedstr, 1, splitedstr.length));
            result.setFormatString(optionstr);
            return result;
        }
        
    }

    public static List<CommandGenerator> load(File configfile) throws FileNotFoundException, IOException {
        if(!configfile.isFile()) {
            throw new FileNotFoundException(configfile.toString() + " is not found");
        }

        List<CommandGenerator> result = new LinkedList<CommandGenerator>();

        try(BufferedReader reader = new BufferedReader(new FileReader(configfile))) {
            String line = reader.readLine();
            while(line != null) {
                result.add(ConfigLoader.parseLine(line));
                line = reader.readLine();
            }
        }catch(Exception e){
            throw e;
        }
        return result;
    }

    public static List<CommandGenerator> load(InputStream instream) throws IOException {
        List<CommandGenerator> result = new LinkedList<CommandGenerator>();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(instream))) {
            String line = reader.readLine();
            while(line != null) {
                result.add(ConfigLoader.parseLine(line));
                line = reader.readLine();
            }
        }catch(Exception e){
            throw e;
        }
        return result;
    }    
}
