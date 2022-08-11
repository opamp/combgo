package combgo;

import java.util.Map;

public class CommandGenerator {
    private String myname;
    private String formatstr;
    private String defaultvalue;
    
    CommandGenerator(String name) {
        this.myname = "";
        this.formatstr = "";
        this.defaultvalue = "";
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

    public void setDefaultValue(String val) {
        this.defaultvalue = val;
    }

    public String getDefaultValue() {
        return this.defaultvalue;
    }

    public boolean isReady() {
        if(myname.length() > 0 && formatstr.length() > 0) {
            return true;
        }else {
            return false;
        }
    }

    public void setFormatString(String optstr) {
        this.formatstr = optstr;
    }

    public String getFormatString() {
        return this.formatstr;
    }

    public String command(Map<String, String> vals) {
        return "Temporary return";
    }
}
