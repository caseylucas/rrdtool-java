package gnu.rrd;

import java.util.*;
import java.util.Date;

/**
 * A class that represents an RRD Info Record.
 *
 * @author Dave Neitz - Sprint E|Solutions, February 2002
 */
public class RRDInfo implements java.io.Serializable {

  public static final int    DOUBLE_VALUE  = 0;
  public static final int    LONG_VALUE    = 1;
  public static final int    STRING_VALUE  = 2;
  public static final int    DATE_VALUE    = 3;

  public static final String FILENAME                        = "filename";
  public static final String RRD_VERSION                     = "rrd_version";
  public static final String STEP                            = "step";
  public static final String LAST_UPDATE                     = "last_update";
  public static final String DS_TYPE                         = "ds[&1].type";
  public static final String DS_MINIMAL_HEARTBEAT            = "ds[&1].minimal_heartbeat";
  public static final String DS_MIN                          = "ds[&1].min";
  public static final String DS_MAX                          = "ds[&1].max";
  public static final String DS_LAST_DS                      = "ds[&1].last_ds";
  public static final String DS_VALUE                        = "ds[&1].value";
  public static final String DS_UNKNOWN_SEC                  = "ds[&1].unknown_sec";
  public static final String RRA_CF                          = "rra[&1].cf";
  public static final String RRA_ROWS                        = "rra[&1].rows";
  public static final String RRA_PDP_PER_ROW                 = "rra[&1].pdp_per_row";
  public static final String RRA_XFF                         = "rra[&1].xff";
  public static final String RRA_CDP_PREP_VALUE              = "rra[&1].cdp_prep[&2].value";
  public static final String RRA_CDP_PREP_UNKNOWN_DATAPOINTS = "rra[&1].cdp_prep[&2].unknown_datapoints";

  private int    type;
  private Object value;

  /**
   * Constructs an RRD info object.
   *
   * @param type  info type
   * @param value  double value
   */
  public RRDInfo(double value) { 
    this.type  = DOUBLE_VALUE;
    this.value = new Double(value);
  }

  /**
   * Constructs an RRD info object.
   *
   * @param type  info type
   * @param value long value
   */
  public RRDInfo(long value) { 
    this.type  = LONG_VALUE;
    this.value = new Long(value);
  }

  /**
   * Constructs an RRD info object.
   *
   * @param type  info type
   * @param value string value
   */
  public RRDInfo(String value) { 
    this.type  = STRING_VALUE;
    this.value = value;
  }

  /**
   * Constructs an RRD info object.
   *
   * @param type  info type
   * @param value date value
   */
  public RRDInfo(Date value) {
    this.type  = DATE_VALUE;
    this.value = value;
  }


  //---------------------------------------------------------------------------
  //--
  //--                    P U B L I C   M E T H O D S
  //--
  //---------------------------------------------------------------------------
  /**
   * Returns the info type;
   */
  public int getType() { return(type); }
  /**
   * Returns the double value;
   */
  public double getDouble() { return(((Double)value).doubleValue()); }
  /**
   * Returns the long value
   */
  public long getLong() { return(((Long)value).longValue()); }
  /**
   * Returns the string value
   */
  public String getString() { return((String)value); }

  /**
   * Returns the date value
   */
  public Date getDate() { return((Date)value); }

  /**
   * Fetch a value type from the Info Hashtable
   *
   * @param info    info hashtable
   * @param key     hashtable key
   * @param parm    parameter
   */
  static public int getType(
    Hashtable info
   ,String    key
   ,String    parm1
   ,String    parm2
  ) { 
    if (parm1 != null) key = substituteString(key, "&1", parm1);
    if (parm2 != null) key = substituteString(key, "&2", parm2);
    RRDInfo i = (RRDInfo)info.get(key);
    return i.getType();
  }

  /**
   * Fetch a double value from the Info Hashtable
   *
   * @param info    info hashtable
   * @param key     hashtable key
   * @param parm    parameter
   */
  static public double getDouble(
    Hashtable info
   ,String    key
   ,String    parm1
   ,String    parm2
  ) throws RRDException { 
    if (getType(info, key, parm1, parm2) != DOUBLE_VALUE)
      throw new RRDException("Invalid type, data value is not a double value.");
    if (parm1 != null) key = substituteString(key, "&1", parm1);
    if (parm2 != null) key = substituteString(key, "&2", parm2);
    RRDInfo i = (RRDInfo)info.get(key);
    return i.getDouble();
  }

  /**
   * Fetch a long value from the Info Hashtable
   *
   * @param info    info hashtable
   * @param key     hashtable key
   * @param parm    parameter
   */
  static public long getLong(
    Hashtable info
   ,String    key
   ,String    parm1
   ,String    parm2
  ) throws RRDException { 
    if (getType(info, key, parm1, parm2) != LONG_VALUE)
      throw new RRDException("Invalid type, data value is not a long value.");
    if (parm1 != null) key = substituteString(key, "&1", parm1);
    if (parm2 != null) key = substituteString(key, "&2", parm2);
    RRDInfo i = (RRDInfo)info.get(key);
    return i.getLong();
  }

  /**
   * Fetch a string value from the Info Hashtable
   *
   * @param info    info hashtable
   * @param key     hashtable key
   * @param parm    parameter
   */
  static public String getString(
    Hashtable info
   ,String    key
   ,String    parm1
   ,String    parm2
  ) throws RRDException { 
    if (getType(info, key, parm1, parm2) != STRING_VALUE)
      throw new RRDException("Invalid type, data value is not a string value.");
    if (parm1 != null) key = substituteString(key, "&1", parm1);
    if (parm2 != null) key = substituteString(key, "&2", parm2);
    RRDInfo i = (RRDInfo)info.get(key);
    return i.getString();
  }

  /**
   * Fetch a date value from the Info Hashtable
   *
   * @param info    info hashtable
   * @param key     hashtable key
   * @param parm    parameter
   */
  static public Date getDate(
    Hashtable info
   ,String    key
   ,String    parm1
   ,String    parm2
  ) throws RRDException { 
    if (getType(info, key, parm1, parm2) != DATE_VALUE)
      throw new RRDException("Invalid type, data value is not a date value.");
    if (parm1 != null) key = substituteString(key, "&1", parm1);
    if (parm2 != null) key = substituteString(key, "&2", parm2);
    RRDInfo i = (RRDInfo)info.get(key);
    return i.getDate();
  }

  //---------------------------------------------------------------------------
  //--
  //--                    P R I V A T E   M E T H O D S
  //--
  //---------------------------------------------------------------------------
  /**
   * Substitute parameter value within key string.
   */
  static private String substituteString(String src, String sub, String val) {
    String rtn = "";
    int pos, lsub, lsrc;
    if((pos = src.indexOf(sub)) < 0) { return(src); }
    lsrc = src.length();
    lsub = sub.length();
    if(pos == 0) {
      rtn = val + src.substring(lsub);
    } else
    if(pos+lsub == lsrc) {
      rtn = src.substring(0,pos) + val;
    } else {
      rtn = src.substring(0,pos) + val + src.substring(pos+lsub);
    }
    return rtn;
  }

}
