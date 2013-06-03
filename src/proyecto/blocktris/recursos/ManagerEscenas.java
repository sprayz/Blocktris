package proyecto.blocktris.recursos;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;

import proyecto.blocktris.EscenaJuego;
import proyecto.blocktris.EscenaMenu;
import proyecto.blocktris.logica.EscenaBase;






public class ManagerEscenas
{
 

    public EscenaJuego escenaJuego;
   public EscenaBase escenaMenu;
    
 
    private static  ManagerEscenas INSTANCIA= null;
    
    private TipoEscena tipoEscenaActual = null;
    
    private EscenaBase escenaActual;
    
    
    
    
    public enum TipoEscena
    {
        ESCENA_JUEGO,
        ESCENA_MENU
        
    }
    
   
    
     public void crearEscenaJuego(){
    	 
    	 escenaJuego = new EscenaJuego();
    	 
     }
     public void crearEscenaMenu(){
    	 
    	 escenaMenu = new EscenaMenu();
    	 
     }
  
    public void setEscena(EscenaBase escena)
    {
    	ManagerRecursos.getInstancia().motor.setScene(escena);
        escenaActual = escena;
    }
   
    public void setEscena(TipoEscena tipoEscena)
    {
        switch (tipoEscena)
        {
            
            case ESCENA_JUEGO:
                setEscena(escenaJuego);
                tipoEscenaActual =tipoEscena;
              break;
              
            case ESCENA_MENU:
                setEscena(escenaMenu);
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