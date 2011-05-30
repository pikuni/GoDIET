/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 1.3.40
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.sysfera.vishnu.api.ims.internal;


public class Process extends EObject{
  private long swigCPtr;

  protected Process(long cPtr, boolean cMemoryOwn) {
    super(VISHNU_IMSJNI.SWIGProcessUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Process obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        VISHNU_IMSJNI.delete_Process(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public Process() {
    this(VISHNU_IMSJNI.new_Process(), true);
  }

  public void _initialize() {
    VISHNU_IMSJNI.Process__initialize(swigCPtr, this);
  }

  
public String getProcessName() {
    return VISHNU_IMSJNI.Process_getProcessName(swigCPtr, this);
  }

  
public void setProcessName(String _processName) {
    VISHNU_IMSJNI.Process_setProcessName(swigCPtr, this, _processName);
  }

  
public String getMachineId() {
    return VISHNU_IMSJNI.Process_getMachineId(swigCPtr, this);
  }

  
public void setMachineId(String _machineId) {
    VISHNU_IMSJNI.Process_setMachineId(swigCPtr, this, _machineId);
  }

  
public String getDietId() {
    return VISHNU_IMSJNI.Process_getDietId(swigCPtr, this);
  }

  
public void setDietId(String _dietId) {
    VISHNU_IMSJNI.Process_setDietId(swigCPtr, this, _dietId);
  }

  
public int getState() {
    return VISHNU_IMSJNI.Process_getState(swigCPtr, this);
  }

  
public void setState(int _state) {
    VISHNU_IMSJNI.Process_setState(swigCPtr, this, _state);
  }

  
public long getTimestamp() {
    return VISHNU_IMSJNI.Process_getTimestamp(swigCPtr, this);
  }

  public void setTimestamp(long _timestamp) {
    VISHNU_IMSJNI.Process_setTimestamp(swigCPtr, this, _timestamp);
  }

  
public String getScript() {
    return VISHNU_IMSJNI.Process_getScript(swigCPtr, this);
  }

  
public void setScript(String _script) {
    VISHNU_IMSJNI.Process_setScript(swigCPtr, this, _script);
  }

  public SWIGTYPE_p_ecorecpp__mapping__any eGet(int _featureID, boolean _resolve) {
    return new SWIGTYPE_p_ecorecpp__mapping__any(VISHNU_IMSJNI.Process_eGet(swigCPtr, this, _featureID, _resolve), true);
  }

  public void eSet(int _featureID, SWIGTYPE_p_ecorecpp__mapping__any _newValue) {
    VISHNU_IMSJNI.Process_eSet(swigCPtr, this, _featureID, SWIGTYPE_p_ecorecpp__mapping__any.getCPtr(_newValue));
  }

  public boolean eIsSet(int _featureID) {
    return VISHNU_IMSJNI.Process_eIsSet(swigCPtr, this, _featureID);
  }

  public void eUnset(int _featureID) {
    VISHNU_IMSJNI.Process_eUnset(swigCPtr, this, _featureID);
  }

  public SWIGTYPE_p_ecore__EClass _eClass() {
    long cPtr = VISHNU_IMSJNI.Process__eClass(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_ecore__EClass(cPtr, false);
  }

}
