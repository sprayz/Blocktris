/*
 *  @author Pablo Morillas Lozano
 */
package proyecto.blocktris.recursos;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

// TODO: Auto-generated Javadoc
/**
 * The Class ExclStrat.
 */
public class ExclStrat implements ExclusionStrategy {

    /** The c. */
    private Class<?> c;
    
    /** The field name. */
    private String fieldName;
    
    /**
	 * Instantiates a new excl strat.
	 * 
	 * @param fqfn
	 *            the fqfn
	 * @throws SecurityException
	 *             the security exception
	 * @throws NoSuchFieldException
	 *             the no such field exception
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
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