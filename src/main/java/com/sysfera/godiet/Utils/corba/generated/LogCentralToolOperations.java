
package com.sysfera.godiet.Utils.corba.generated;

/**
* LogCentralToolOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ./goDiet/Utils/CORBA/idl/LogTool.idl
* mercredi 26 janvier 2011 17 h 07 CET
*/


/**
 * methods offered by the core to allow tools to attach
 * and configure filters
 */
public interface LogCentralToolOperations 
{

  /**
     * Do nothing but usefull to be sure that the object is reachable
     */
  void test ();

  /**
     * Connect a Tool with its toolName, which must be unique among all
     * tools. The return value indicates the success of the connection.
     * If ALREADYEXISTS is returned, the tool could not be attached, as
     * the specified toolName already exists. In this case the tool must
     * reconnect with another name before specifying any filters. If the 
     * tool sends an empty toolName, the LogCentral will provide a unique
     * toolName and pass it back to the tool.
     */
  short connectTool (org.omg.CORBA.StringHolder toolName, ToolMsgReceiver msgReceiver);

  /**
     * Disconnects a connected tool from the monitor. No further 
     * filterconfigurations should be sent after this call. The 
     * toolMsgReceiver will not be used by the monitor any more 
     * after this call. Returns NOTCONNECTED if the calling tool
     * was not connected.
     */
  short disconnectTool (String toolName);

  /**
     * Returns a list of possible tags. This is just a convenience
     * functions and returns the values that are specified in a
     * configuration file. If the file is not up to date, the 
     * application may generate more tags than defined in this
     * list.
     */
  String[] getDefinedTags ();

  /**
     * Returns a list of actually connected Components. This is just
     * a convenience function, as the whole state of the system will
     * be sent to the tool right after connection (in the form of
     * messages)
     */
  String[] getDefinedComponents ();

  /**
     * Create a filter for this tool on the monitor. Messages matching
     * this filter will be forwarded to the tool. The filter will be
     * identified by its name, which is a part of filter_t. A tool
     * can have as much filters as it wants. Returns ALREADYEXISTS if
     * another filter with this name is already registered.
     */
  short addFilter (String toolName, filter_t filter);

  /**
     * Removes a existing filter from the tools filterList. The filter
     * will be identified by its name in the filter_t. If the specified
     * filter does not exist, NOTEXISTS is returned.
     */
  short removeFilter (String toolName, String filterName);

  /**
     * Removes all defined filters.
     */
  short flushAllFilters (String toolName);
} // interface LogCentralToolOperations