package gnu.rrd;

import java.util.*;
import java.util.Date.*;

/**
 * A class that emulates the rrdtool command line application.  This class
 * is used for validating the functionality of the JNI and provides examples
 * on how to code to the interface.
 *
 * @author Dave Neitz - Sprint E|Solutions, February 2002
 */
public class RRDTool {

  //---------------------------------------------------------------------------
  //--
  //--                    P R I V A T E   M E T H O D S
  //--
  //---------------------------------------------------------------------------
  /**
   * Substitute parameter value within key string.
   */
  static private String getInfo(
    Hashtable hash
   ,String    key
   ,String    parm1
   ,String    parm2
  ) throws RRDException  {
    switch (RRDInfo.getType(hash, key, parm1, parm2)) {
      case RRDInfo.DOUBLE_VALUE:
        return("" + RRDInfo.getDouble(hash, key, parm1, parm2));
      case RRDInfo.LONG_VALUE:
        return("" + RRDInfo.getLong(hash, key, parm1, parm2));
      case RRDInfo.STRING_VALUE:
        return(RRDInfo.getString(hash, key, parm1, parm2));
      case RRDInfo.DATE_VALUE:
        return("" + RRDInfo.getDate(hash, key, parm1, parm2));
    }
    return("Value not found");
  }


  //---------------------------------------------------------------------------
  //--
  //--                              M A I N
  //--
  //---------------------------------------------------------------------------
  public static void main(String[] args) {

    String usage = "\n" +
                   "RRDtool 1.0.33  Copyright 1997-2001 by Tobias Oetiker <tobi@oetiker.ch>\n" +
                   "RRD Java Native Interface (JNI) by Dave Neitz Sprint E|Solutions\n\n" +
                   "Usage: java [-cp<PATH>] rrd.RRDTool command command_options\n\n" +
                   "Valid commands: create, update, graph, dump, restore,\n" +
                   "                last, info, fetch, tune, resize\n\n" +
                   "RRDtool is distributed under the Terms of the GNU General\n"+
                   "Public License Version 2. (www.gnu.org/copyleft/gpl.html)\n\n"+
                   "For more information read the RRD manpages\n";
    String unsupported = " is currently unsupported.\n";

    if(args.length < 2) {
      System.out.println(usage);
      System.exit(1);
    }

    String command = args[0];
    String[] opt = new String[args.length-1];
    System.arraycopy(args, 1, opt, 0, args.length-1);

    try {
      if (command.equalsIgnoreCase("create"))         { RRDJNI.create(opt);
      } else if (command.equalsIgnoreCase("update"))  { RRDJNI.update(opt);
      } else if (command.equalsIgnoreCase("graph"))   { 
        RRDGraph graph = RRDJNI.graph(opt);
        System.out.println(graph.getFilename());
        System.out.println(graph.getInfo());
        System.out.println(graph.getXSize()+"x"+graph.getYSize());
      } else if (command.equalsIgnoreCase("dump"))    { RRDJNI.dump(opt);
      } else if (command.equalsIgnoreCase("restore")) { RRDJNI.restore(opt);
      } else if (command.equalsIgnoreCase("last"))    { 
        System.out.println(RRDJNI.last(opt));
      } else if (command.equalsIgnoreCase("info"))    { 
        Hashtable h = RRDJNI.info(opt);
        System.out.println("Return specific values");
        System.out.println("----------------------");
        System.out.println("Filename:                              " + 
                           getInfo(h,RRDInfo.FILENAME,null,null));
        System.out.println("Version:                               " + 
                           getInfo(h,RRDInfo.RRD_VERSION,null,null));
        System.out.println("Step:                                  " + 
                           getInfo(h,RRDInfo.STEP,null,null));
        System.out.println("Last Update:                           " + 
                           getInfo(h,RRDInfo.LAST_UPDATE,null,null));
        System.out.println("rra[0].cf:                             " + 
                           getInfo(h,RRDInfo.RRA_CF,"0",null));
        System.out.println("rra[0].rows:                           " + 
                           getInfo(h,RRDInfo.RRA_ROWS,"0",null));
        System.out.println("rra[0].pdp_per_row:                    " + 
                           getInfo(h,RRDInfo.RRA_PDP_PER_ROW,"0",null));
        System.out.println("rra[0].xff:                            " + 
                           getInfo(h,RRDInfo.RRA_XFF,"0",null));
        System.out.println("rra[0].cdp_prep[0].value:              " + 
                           getInfo(h,RRDInfo.RRA_CDP_PREP_VALUE,"0","0"));
        System.out.println("rra[0].cdp_prep[0].unknown_datapoints: " + 
                           getInfo(h,RRDInfo.RRA_CDP_PREP_UNKNOWN_DATAPOINTS,"0","0"));
        System.out.println("");
        System.out.println("Entire Hashtable");
        System.out.println("----------------");
        Enumeration enum = h.keys();
        while (enum.hasMoreElements()) {
          String  key  = (String)enum.nextElement();
          RRDInfo info = (RRDInfo)h.get(key);
          switch (info.getType()) {
            case RRDInfo.DOUBLE_VALUE:
              System.out.println(key + ": " + info.getDouble());
              break;
            case RRDInfo.LONG_VALUE:
              System.out.println(key + ": " + info.getLong());
              break;
            case RRDInfo.STRING_VALUE:
              System.out.println(key + ": " + info.getString());
              break;
            case RRDInfo.DATE_VALUE:
              System.out.println(key + ": " + info.getDate());
              break;
          }
        }
      } else if (command.equalsIgnoreCase("fetch"))   { 
	Vector v = RRDJNI.fetch(opt);
	Enumeration enum = v.elements();
	while (enum.hasMoreElements()) {
	  RRDRec rec  = (RRDRec)enum.nextElement();
	  String out  = rec.getDate() + ": ";
	  Vector data = rec.getData();
	  Enumeration edata = data.elements();
	  while (edata.hasMoreElements()) {
	    double d = ((Double)(edata.nextElement())).doubleValue();
	    out = out + d + " ";
	  }
          System.out.println(out);
	}           
      } else if (command.equalsIgnoreCase("tune"))    { RRDJNI.tune(opt);
      } else if (command.equalsIgnoreCase("resize"))  { RRDJNI.resize(opt);
      } else {
        System.out.println(usage);
        System.exit(1);
      }
    } catch (RRDException e) {
      System.out.println(e.toString());
      System.exit(1);
    }
    System.exit(0);
  }
}
