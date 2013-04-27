package proyecto.blocktris.logica;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.app.Activity;

import proyecto.blocktris.recursos.*;
import proyecto.blocktris.recursos.ManagerEscenas.TipoEscena;

public abstract class EscenaBase extends Scene
{
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    
    protected Engine motor;
    protected Activity actividadJuego;
    protected ManagerRecursos managerRecursos;
    protected VertexBufferObjectManager vbom;
    protected Camera camara;
    
    //---------------------------------------------
    // CONSTRUCTOR
    //---------------------------------------------
    
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
    // ABSTRACTION
    //---------------------------------------------
    
    public abstract void crearEscena();
    
    public abstract void teclaVolverPreionada();
    
    public abstract TipoEscena getTipoEscena();
    
    public abstract void deshacerEscena();
}