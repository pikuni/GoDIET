/*@GODIET_LICENSE*/
/*
 * ComputeCollection.java
 *
 * Created on June 17, 2004, 10:56 AM
 */

package goDiet.Model;

import java.util.Iterator;
import java.util.Vector;
/**
 *
 * @author  hdail
 */
public class ComputeCollection {
    /** Config-related items.  Never change if jobCount > 0 */
    private String name;
    private StorageResource storageResource = null;
    private java.util.Vector computeResources;    
    //private String envPath = null;
    //private String envLdLibraryPath = null;
    private Vector envVars = new Vector();
    
    /** Runtime-related items. */
    private int jobCount = 0;
    
    /** Creates a new instance of ComputeCollection */
    public ComputeCollection(String name) {
        this.name = name;
        this.computeResources = new java.util.Vector();
    }
    
    /** Config-related methods.  Do not use setXX() if getJobCount() > 0 */
    
    public String getName(){
        return this.name;
    }
    
    public void addStorageResource(StorageResource storageRes){
        this.storageResource = storageRes;
    }
    public StorageResource getStorageResource(){
        return this.storageResource;
    }

    public void addComputeResource(ComputeResource newComp){    
        if( this.getComputeResource(newComp.getName()) != null){
            System.err.println("Resource Collection: Compute Resource " + 
                newComp.getName() + " already included. Addition refused.");
            return;
        }
        this.computeResources.add(newComp);
    }  
       
    public int getComputeResourceCount(){
        return this.computeResources.size();
    }
    public java.util.Vector getComputeResources(){return computeResources;}
    
    public ComputeResource getComputeResource(String name) {
        ComputeResource resource = null;
        for( Iterator it = computeResources.iterator(); it.hasNext();) {
            resource = (ComputeResource) it.next();
            if(name.equals(resource.getName())) {
                return resource;
            }
        }
        return null;
    }
    
    /*public void setEnvPath(String path){
        this.envPath = path;
    }
    public String getEnvPath(){
        return this.envPath;
    }

    public void setEnvLdLibraryPath(String ldLibPath){
        this.envLdLibraryPath = ldLibPath;
    }
    public String getEnvLdLibraryPath(){
        return this.envLdLibraryPath;
    }*/
     public Vector getEnvVars() {
        return envVars;
    }

    public void setEnvVars(Vector envVars) {
        this.envVars = envVars;
    }
//    /** Run-time related methods. */
//    /** This method should be called only by compute resources belonging 
//     * to this collection */
//    public void incJobCount(){
//        this.jobCount++;
//    }
//    /** This method should be called only by compute resources belonging 
//     * to this collection */
//    public void decJobCount(){
//        this.jobCount--;
//    }
//    public int getJobCount(){
//        return this.jobCount;
//    }

   
}
