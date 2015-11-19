package gnu.rrd;

import java.util.*;
import java.util.Date;
import java.awt.Image;

/**
 * A class that represents an RRD Record.
 *
 * @author Dave Neitz - Sprint E|Solutions, February 2002
 */
public class RRDGraph implements java.io.Serializable {

  private String  filename;
  private String  info;
  private int     xsize;
  private int     ysize;

  /**
   * Constructs an RRD graph data object.
   *
   * @param filename graph filename
   * @param info     graph information
   * @param xsize    size of x axis
   * @param ysize    size of y axis
   */
  public RRDGraph(
    String filename
   ,String info
   ,int    xsize
   ,int    ysize
  ) {
    this.filename = filename;
    this.info     = info;
    this.xsize    = xsize;
    this.ysize    = ysize;
  }

  //---------------------------------------------------------------------------
  //--
  //--                    P U B L I C   M E T H O D S
  //--
  //---------------------------------------------------------------------------
  /**
   * Returns the graph information.
   */
  public String getInfo() { return(info); }
  /**
   * Returns the graph filename.
   */
  public String getFilename() { return(filename); }
  /**
   * Returns the x size.
   */
  public int getXSize() { return(xsize); }
  /**
   * Returns the y size.
   */
  public int getYSize() { return(ysize); }
}
