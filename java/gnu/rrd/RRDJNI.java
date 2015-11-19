package gnu.rrd;

import java.io.*;
import java.util.*;
import java.awt.*;

/**
 * A class that provides RRD tool functionality via JNI.
 * <p>
 * @author Dave Neitz - Sprint E|Solutions, February 2002
 */
public class RRDJNI {

  //---------------------------------------------------------------------------
  //--
  //--                    P U B L I C   M E T H O D S
  //--
  //---------------------------------------------------------------------------

  /**
   * Create a new RRD.
   * <p>
   * @param argv argument list
   * <p>
   * filename.rrd [--start|-b start time]
   *              [--step|-s step]
   *              [DS:ds-name:DST:heartbeat:min:max] [RRA:CF:xff:steps:rows]
   * <p>
   * @exception RRDException when an error occurs.
   */
  public native static void create(
    String argv[]
  ) throws RRDException;

  /**
   * Update an RRD.
   * <p>
   * @param argv argument list
   * <p>
   * filename.rrd --template|-t ds-name:ds-name:...
   *              time|N:value[:value...]
   *              [ time:value[:value...] ..]
   * <p>
   * @exception RRDException when an error occurs.
   */
  public native static void update(
    String argv[]
  ) throws RRDException;

  /**
   * Generate a graph from one or several RRD.
   * <p>
   * @param argv argument list
   * <p>
   * filename [-s|--start seconds] [-e|--end seconds]
   *          [-x|--x-grid x-axis grid and label]
   *          [--alt-y-grid]
   *          [-y|--y-grid y-axis grid and label]
   *          [-v|--vertical-label string] [-w|--width pixels]
   *          [-h|--height pixels] [-o|--logarithmic]
   *          [-u|--upper-limit value] [-z|--lazy]
   *          [-l|--lower-limit value] [-r|--rigid]
   *          [--alt-autoscale]
   *          [--alt-autoscale-max]
   *          [--units-exponent value]
   *          [--step seconds]
   *          [-f|--imginfo printfstr]
   *          [-a|--imgformat GIF|PNG]
   *          [-c|--color COLORTAG#rrggbb] [-t|--title string]
   *          [DEF:vname=rrd:ds-name:CF]
   *          [CDEF:vname=rpn-expression]
   *          [PRINT:vname:CF:format]
   *          [GPRINT:vname:CF:format]
   *          [HRULE:value#rrggbb[:legend]]
   *          [VRULE:value#rrggbb[:legend]]
   *          [LINE{1|2|3}:vname[#rrggbb[:legend]]]
   *          [AREA:vname[#rrggbb[:legend]]]
   *          [STACK:vname[#rrggbb[:legend]]]
   * <p>
   * @return the graph
   * @exception RRDException when an error occurs.
   */
  public native static RRDGraph graph(
    String argv[]
  ) throws RRDException;

  /**
   * Dump an RRD file to an XML file.
   * <p>
   * @param argv argument list
   * <p>
   * filename.rrd >filename.xml
   * <p>
   * @exception RRDException when an error occurs.
   */
  public native static void dump(
    String argv[]
  ) throws RRDException;

  /**
   * Restore an RRD file from its XML form.
   * <p>
   * @param argv argument list
   * <p>
   * [--range-check|-r] filename.xml filename.rrd
   * <p>
   * @exception RRDException when an error occurs.
   */
  public native static void restore(
    String argv[]
  ) throws RRDException;

  /**
   * Show the last update time for an RRD.
   * <p>
   * @param argv argument list
   * <p>
   * filename.rrd
   * <p>
   * @exception RRDException when an error occurs.
   */
  public native static Date last(
    String argv[]
  ) throws RRDException;

  /**
   * Returns the configuration and status of an RRD.
   * <p>
   * @param argv argument list
   * <p>
   * filename.rrd
   * <p>
   * @exception RRDException when an error occurs.
   */
  public native static Hashtable info(
    String argv[]
  ) throws RRDException;

  /**
   * Fetch data out of an RRD.
   * <p>
   * @param argv argument list
   * <p>
   * filename.rrd CF
   *              [--resolution|-r resolution]
   *              [--start|-s start] [--end|-e end]
   * <p>
   * @return a list of RRDRec record objects
   * @exception RRDException when an error occurs.
   */
  public native static Vector fetch(
    String argv[]
  ) throws RRDException;

  /**
   * Modify some basic properties of an RRD.
   * <p>
   * @param argv argument list
   * <p>
   * filename.rrd [--heartbeat|-h ds-name:heartbeat]
   *              [--data-source-type|-d ds-name:DST]
   *              [--data-source-rename|-r old-name:new-name]
   *              [--minimum|-i ds-name:min] [--maximum|-a ds-name:max]
   * <p>
   * @exception RRDException when an error occurs.
   */
  public native static void tune(
    String argv[]
  ) throws RRDException;

  /**
   * Alter the lenght of one of the RRAs in an RRD.
   * <p>
   * @param argv argument list
   * <p>
   *  filename.rrd rranum GROW|SHRINK rows
   * <p>
   * @exception RRDException when an error occurs.
   */
  public native static void resize(
    String argv[]
  ) throws RRDException;


  //---------------------------------------------------------------------------
  //--
  //--                       I N I T I A T O R S
  //--
  //---------------------------------------------------------------------------
  // loads the JNI library when the class is loaded...
  static { System.loadLibrary("rrdjni"); }
}
  
    
  
