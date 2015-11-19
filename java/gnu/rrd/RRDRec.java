package gnu.rrd;

import java.util.*;
import java.util.Date;

/**
 * A class that represents an RRD Record.
 *
 * @author Dave Neitz - Sprint E|Solutions, February 2002
 */
public class RRDRec implements java.io.Serializable {

  private Date   date;
  private Vector data = new Vector();

  /**
   * Constructs an RRD record data object.
   *
   * @param date  date/time of data value
   * @param data  data source elements
   */
  public RRDRec(
    Date   date
   ,Vector data
  ) {
    this.date = date;
    this.data = data;
  }

  //---------------------------------------------------------------------------
  //--
  //--                    P U B L I C   M E T H O D S
  //--
  //---------------------------------------------------------------------------
  /**
   * Returns the date/time;
   */
  public Date getDate() { return(date); }
  /**
   * Returns the data;
   */
  public Vector getData() { return(data); }
  /**
   * Returns the data;
   */
  public double getDataAt(int index) { return(((Double)(data.elementAt(index))).doubleValue()); }
}
