
package com.sysfera.godiet.utils.corba.generated;

/**
* log_msg_t.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ./goDiet/Utils/CORBA/idl/LogTypes.idl
* mercredi 26 janvier 2011 17 h 07 CET
*/

public final class log_msg_t implements org.omg.CORBA.portable.IDLEntity
{
  public String componentName = null;

  // origin of message
  public log_time_t time = null;

  // time of message
  public boolean warning = false;

  // true if the message has not been ordered
  public String tag = null;

  // type of message
  public String msg = null;

  public log_msg_t ()
  {
  } // ctor

  public log_msg_t (String _componentName, log_time_t _time, boolean _warning, String _tag, String _msg)
  {
    componentName = _componentName;
    time = _time;
    warning = _warning;
    tag = _tag;
    msg = _msg;
  } // ctor

} // class log_msg_t