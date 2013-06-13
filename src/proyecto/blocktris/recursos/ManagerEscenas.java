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







/**
 * Esta clase contiene las distintas escenas del juego y gestiona  la creación ,destrucción 
 * y sus cambios.
 * 
 * Es un  Singleton, es decir solo se permite una instancia de la misma.
 */
public class ManagerEscenas
{
	
 
	private ManagerEscenas(){};
   
    public EscenaJuego escenaJuego;
   

   public MenuScene escenaMenu;
    
 
   
    private static  ManagerEscenas INSTANCIA= null;
    
   
    private TipoEscena tipoEscenaActual = null;
    
   
    private EscenaBase escenaActual;
    
    
    
    
   
    public enum TipoEscena
    {
        
        /** The escena juego. */
        ESCENA_JUEGO
        
    }
    
   
    
     public void crearEscenaJuego(){
    	 
    	 escenaJuego = new EscenaJuego();
    	 
     }
     
    /**
	 * Cambia la escena.
	 * 
	 * @param escena
	 *             la escena a hacer actual
	 */
    public void setEscena(EscenaBase escena)
    {
    	ManagerRecursos.getInstancia().motor.setScene(escena);
        escenaActual = escena;
    }
   
    /**
	 * Cambia la secena a
	 * 
	 * @param tipoEscena
	 *            el tipo de la escena
	 */
    public void setEscena(TipoEscena tipoEscena)
    { 
        /**
    	 * Gets the escena actual.
    	 * 
    	 * @return the escena actual
    	 */
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
    
    
    public static ManagerEscenas getInstancia()
    {
    	if(INSTANCIA == null){
    		INSTANCIA = new ManagerEscenas();
    	}
        return INSTANCIA;
    }
    
    
    public TipoEscena getTipoEscenaActual()
    {
        return tipoEscenaActual;
    }
   
    public EscenaBase getEscenaActual()
    {
        return escenaActual;
    }
}