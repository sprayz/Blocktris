/*
 *  @author Pablo Morillas Lozano
 */
package proyecto.blocktris.logica;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.app.Activity;

import proyecto.blocktris.recursos.*;
import proyecto.blocktris.recursos.ManagerEscenas.TipoEscena;

// TODO: Auto-generated Javadoc
/**
 * The Class EscenaBase.
 */
public abstract class EscenaBase extends Scene
{
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    
    /** The motor. */
    protected Engine motor;
    
    /** The actividad juego. */
    protected Activity actividadJuego;
    
    /** The manager recursos. */
    protected ManagerRecursos managerRecursos;
    
    /** The vbom. */
    protected VertexBufferObjectManager vbom;
    
    /** The camara. */
    protected Camera camara;
    
    /** The pausado. */
    protected  boolean pausado =false;
    //---------------------------------------------
    // CONSTRUCTOR
    //---------------------------------------------
    
    /**
	 * Checks if is pausado.
	 * 
	 * @return true, if is pausado
	 */
    public boolean isPausado() {
		return pausado;
	}

	/**
	 * Sets the pausado.
	 * 
	 * @param pausado
	 *            the new pausado
	 */
	public void setPausado(boolean pausado) {
		this.pausado = pausado;
	}

	/**
	 * Instantiates a new escena base.
	 */
	public EscenaBase()
    {
        this.managerRecursos = ManagerRecursos.getInstancia();
        this.motor = managerRecursos.motor;
        this.actividadJuego= managerRecursos.actividadJuego;
        this.vbom = managerRecursos.vbom;
        this.camara = managerRecursos.camara;
        crearEscena();
    }
    
    //---------------------------------------------
    // ABSTRACCION
    //---------------------------------------------
   
    /**
	 * Crear escena.
	 */
    public abstract void crearEscena();
    
    /**
	 * Reiniciar escena.
	 */
    public abstract void reiniciarEscena();
    
    /**
	 * Pausar escena.
	 */
    public abstract void pausarEscena();
    
    /**
	 * Reanudar escena.
	 */
    public abstract void  reanudarEscena();
    
    /**
	 * Tecla volver preionada.
	 */
    public abstract void teclaVolverPresionada();
    
    /**
	 * Tecla menu presionada.
	 */
    public abstract void teclaMenuPresionada();
    
    
    /**
	 * Deshacer escena.
	 */
    public abstract void deshacerEscena();
}