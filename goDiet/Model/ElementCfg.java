/*@GODIET_LICENSE*
/*
 * 
 */

package Model;

import goDiet.Model.Option;
import java.util.Vector;

/**
 * This Class is use in order to generate the config_file.cfg
 * of the Element
 * @author rbolze
 */
public class ElementCfg {    
    private String cfgFileName;
    private Vector options;
    
    public ElementCfg(String cfgFileName){        
        this.cfgFileName=cfgFileName;
        this.options = new Vector();        
    }
    public String addOption(Option o){
        String message = "";
        if (options.contains(o)){
            message+="WARNING : you have redefined one option :\n";            
            int index = options.indexOf(o);
            Option old = (Option)options.get(index);
            message+="WARNING : "+old.getName() +" = "+old.getValue()+"\n";
            message+="WARNING : replaced by :\n";
            message+="WARNING : "+o.getName() +" = "+o.getValue();
            options.remove(index);
            options.insertElementAt(o,index);
        }
        this.options.add(o);
        return message;
    }

    public String setCfgFileName(String cfgFileName) {
        return this.cfgFileName=cfgFileName;
    }
    
    public String getCfgFileName() {
        return cfgFileName;
    }
    
    public Vector getOptions() {
        return options;
    }
    public Option getOption(String name){
        Option o = new Option();    
        if (!options.isEmpty()){
            int index = options.indexOf(name);
            o =(Option)options.get(index);
        }
        return o;
    }
    
}
