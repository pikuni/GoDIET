/*@GODIET_LICENSE*/
/*
 * AddElementsEvent.java
 *
 * Created on 13 avril 2004, 15:01
 */

package com.sysfera.godiet.Events;

import com.sysfera.godiet.Model.deprecated.Elements;

/**
 *
 * @author  rbolze
 */
public class AddElementsEvent extends java.awt.AWTEvent {
    
    /** Creates a new instance of AddElementsEvent */
    public AddElementsEvent(Elements source) {
        super(source, 110);
    } 
}
