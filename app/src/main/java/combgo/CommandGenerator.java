package combgo;

import java.util.Map;

public class CommandGenerator {
    private String myname;
    private String optionstr;
    
    CommandGenerator(String name) {
        this.myname = "";
        this.optionstr = "";
        this.setName(name);
    }

    public void setName(String name) {
        if(name.length() > 0) {
            this.myname = name;
        }else{
            throw new IllegalArgumentException("The argument 'name' must not be empty");
        }
    }

    public String getName() {
        return this.myname;
    }

    public boolean isReady() {
        if(myname.length() > 0 && optionstr.length() > 0) {
            return true;
        }else {
            return false;
        }
    }

    public void setOptionString(String optstr) {
        this.optionstr = optstr;
    }

    public String getOptionString(String optstr) {
        return this.optionstr;
    }

    public String command(Map<String, String> vals) {
        return "Temporary return";
    }
}
