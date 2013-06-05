package proyecto.blocktris.recursos;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class ExclStrat implements ExclusionStrategy {

    private Class<?> c;
    private String fieldName;
    public ExclStrat(String fqfn) throws SecurityException, NoSuchFieldException, ClassNotFoundException
    {
        this.c = Class.forName(fqfn.substring(0, fqfn.lastIndexOf(".")));
        this.fieldName = fqfn.substring(fqfn.lastIndexOf(".")+1);
    }
    public boolean shouldSkipClass(Class<?> arg0) {
        return false;
    }

    public boolean shouldSkipField(FieldAttributes f) {

        return (f.getDeclaringClass() == c && f.getName().equals(fieldName));
    }

}