
package com.sysfera.godiet.utils.corba.generated;

/**
* LogCentralToolHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ./goDiet/Utils/CORBA/idl/LogTool.idl
* mercredi 26 janvier 2011 17 h 07 CET
*/


/**
 * methods offered by the core to allow tools to attach
 * and configure filters
 */
public final class LogCentralToolHolder implements org.omg.CORBA.portable.Streamable
{
  public LogCentralTool value = null;

  public LogCentralToolHolder ()
  {
  }

  public LogCentralToolHolder (LogCentralTool initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = LogCentralToolHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    LogCentralToolHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return LogCentralToolHelper.type ();
  }

}