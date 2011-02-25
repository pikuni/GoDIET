/*@GODIET_LICENSE*/
/*
 * LogCentralCommController.java
 *
 * Created on 27 june 2004
 */

package com.sysfera.godiet.Controller;

import java.util.ArrayList;
import java.util.List;

import com.sysfera.godiet.Events.LogStateChange;
import com.sysfera.godiet.Model.deprecated.OmniNames;
import com.sysfera.godiet.Utils.corba.generated.log_msg_t;
import com.sysfera.godiet.Utils.deprecated.LogCentralConnection;

/**
 *
 * @author  hdail
 */
public class LogCentralCommController extends java.util.Observable 
                                      implements Runnable {
    public static final int LOG_CONNECT_NONE = 0;
    public static final int LOG_CONNECT_ACTIVE = 1;
    public static final int LOG_CONNECT_CONFUSED = 2;

    private ConsoleController consoleCtrl;
    private DietPlatformController modelCtrl;
    private LogCentralConnection connection;
    private java.lang.Thread               logCommThread;
    private int lcConnState = LOG_CONNECT_NONE;
    private List<log_msg_t> pendingMsgQueue;

    public LogCentralCommController(ConsoleController consoleController, 
                                    DietPlatformController modelController){
        this.consoleCtrl    = consoleController;
        //this.consoleCtrl.addObserver(this); // do we want to know when launch?
        this.modelCtrl      = modelController;
        this.lcConnState    = LOG_CONNECT_NONE;
        pendingMsgQueue     = new ArrayList<log_msg_t>();
    }
    
    /** Returns true if connect was successful, false otherwise */
    public boolean connectLogService(OmniNames omni){
        boolean connectSuccess = false;
        
        String omniNamesPort = "" + omni.getPort();
        connection = new LogCentralConnection("GoDIET", omni.getContact(),
            omniNamesPort, this, consoleCtrl);
        connectSuccess = connection.connect();
        
        if(connectSuccess){
            this.logCommThread       = new java.lang.Thread(this); 
            this.logCommThread.start();
            consoleCtrl.printOutput("Connected to LogCentral.", 2);
            return true;
        } else {
            consoleCtrl.printError("Failed to connect to LogCentral.", 1);
            return false;
        }
    }
    
    public void queueMsg(log_msg_t msg){
        synchronized(pendingMsgQueue){
            this.pendingMsgQueue.add(msg);
        }
        synchronized(this){
            notifyAll();
        }
    }
    
    public log_msg_t deQueueMsg(){
        synchronized(pendingMsgQueue){
            if(this.pendingMsgQueue.size() > 0){
                return ((log_msg_t)this.pendingMsgQueue.remove(0));
            } else {
                return null;
            }
        }
    }
    
    public void disconnect(){
        this.connection.disconnect();
        //this.logCommThread.stop();    
        // TODO: figure out how to disconnect / reconnect properly
    }
    
    public void run() {
        consoleCtrl.printOutput("LogCentralCommController thread starting up.",2);
        log_msg_t msg;
        while(true) { 
            try {
                synchronized(this){
                    this.wait();
                }
            } catch (InterruptedException x){
                x.printStackTrace();
            }
            while( (msg = deQueueMsg()) != null){
                String name = msg.componentName;
                String tag = msg.tag;
                String text = msg.msg;
                String elementType = null;
                int newState;
                
                if(tag.compareTo("IN") == 0){
                    newState = com.sysfera.godiet.Defaults.LOG_STATE_RUNNING;
                    consoleCtrl.printOutput("Forwarding message: " + name + " tag: " 
                        + tag + " text: " + text, 2);
                } else {
                    newState = com.sysfera.godiet.Defaults.LOG_STATE_CONFUSED;
                    consoleCtrl.printOutput("Ignoring message: " + name + " tag: " 
                        + tag + " text: " + text, 2);
                }
                
                if(newState == com.sysfera.godiet.Defaults.LOG_STATE_RUNNING){
                    // Tokens are: elementType parent machineName
                    java.util.StringTokenizer strTok = 
                        new java.util.StringTokenizer(text.toString()," ");
                    elementType = strTok.nextToken();
                    
                    setChanged();
                    notifyObservers(new LogStateChange(
                        this,name,elementType,newState));
                    clearChanged(); 
                }
            }
        }
    }
}