/*
 *  @author Pablo Morillas Lozano
 */
package proyecto.blocktris.recursos;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;

import proyecto.blocktris.EscenaJuego;
import proyecto.blocktris.EscenaMenu;
import proyecto.blocktris.logica.EscenaBase;






// TODO: Auto-generated Javadoc
/**
 * The Class ManagerEscenas.
 */
public class ManagerEscenas
{
 

    /** The escena juego. */
    public EscenaJuego escenaJuego;
   
   /** The escena menu. */
   public MenuScene escenaMenu;
    
 
    /** The instancia. */
    private static  ManagerEscenas INSTANCIA= null;
    
    /** The tipo escena actual. */
    private TipoEscena tipoEscenaActual = null;
    
    /** The escena actual. */
    private EscenaBase escenaActual;
    
    
    
    
    /**
	 * The Enum TipoEscena.
	 */
    public enum TipoEscena
    {
        
        /** The escena juego. */
        ESCENA_JUEGO
        
    }
    
   
    
     /**
		 * Crear escena juego.
		 */
     public void crearEscenaJuego(){
    	 
    	 escenaJuego = new EscenaJuego();
    	 
     }
     
    /**
	 * Sets the escena.
	 * 
	 * @param escena
	 *            the new escena
	 */
    public void setEscena(EscenaBase escena)
    {
    	ManagerRecursos.getInstancia().motor.setScene(escena);
        escenaActual = escena;
    }
   
    /**
	 * Sets the escena.
	 * 
	 * @param tipoEscena
	 *            the new escena
	 */
    public void setEscena(TipoEscena tipoEscena)
    {
        switch (tipoEscena)
        {
            
            case ESCENA_JUEGO:
                setEscena(escenaJuego);
                tipoEscenaActual =tipoEscena;
              break;
              
            
            default:
                break;
        }
    }
    
    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------
    
    /**
	 * Gets the instancia.
	 * 
	 * @return the instancia
	 */
    public static ManagerEscenas getInstancia()
    {
    	if(INSTANCIA == null){
    		INSTANCIA = new ManagerEscenas();
    	}
        return INSTANCIA;
    }
    
    /**
	 * Gets the tipo escena actual.
	 * 
	 * @return the tipo escena actual
	 */
    public TipoEscena getTipoEscenaActual()
    {
        return tipoEscenaActual;
    }
    
    /**
	 * Gets the escena actual.
	 * 
	 * @return the escena actual
	 */
    public EscenaBase getEscenaActual()
    {
        return escenaActual;
    }
}