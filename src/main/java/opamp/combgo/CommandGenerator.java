package opamp.combgo;

import java.util.Map;

public class CommandGenerator {
    private String myname;
    private String formatstr;
    
    CommandGenerator(String name) {
        this.myname = "";
        this.formatstr = "";
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
        String rtn = this.formatstr;
        for(Map.Entry<String, String> elem: vals.entrySet()) {
            String replace_target = "{{" + elem.getKey() + "}}";
            String replace_string = elem.getValue();
            rtn = rtn.replace(replace_target, replace_string);
        }
        return rtn;
    }
}
