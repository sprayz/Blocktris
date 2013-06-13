/*
 *  @author Nishant
 */
package proyecto.blocktris.recursos;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;


/**
 * 
 * Clase que define una estrategia de exclusión para  Gson
 * 
 * @author Nishant
 * http://stackoverflow.com/a/4803346
 * 
 */
public class ExclStrat implements ExclusionStrategy {

    
    private Class<?> c;
    
   
    private String fieldName;
    

    public ExclStrat(String fqfn) throws SecurityException, NoSuchFieldException, ClassNotFoundException
    {
        this.c = Class.forName(fqfn.substring(0, fqfn.lastIndexOf(".")));
        this.fieldName = fqfn.substring(fqfn.lastIndexOf(".")+1);
    }
    
    /* (non-Javadoc)
     * @see com.google.gson.ExclusionStrategy#shouldSkipClass(java.lang.Class)
     */
    public boolean shouldSkipClass(Class<?> arg0) {
        return false;
    }

    /* (non-Javadoc)
     * @see com.google.gson.ExclusionStrategy#shouldSkipField(com.google.gson.FieldAttributes)
     */
    public boolean shouldSkipField(FieldAttributes f) {

        return (f.getDeclaringClass() == c && f.getName().equals(fieldName));
    }

}