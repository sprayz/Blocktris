/*
 *  @author Pablo Morillas Lozano
 */
package proyecto.blocktris;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.*;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.animator.AlphaMenuSceneAnimator;
import org.andengine.entity.scene.menu.item.*;
import org.andengine.entity.scene.menu.item.decorator.*;
import org.andengine.util.adt.color.Color;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import proyecto.blocktris.logica.EscenaBase;
import proyecto.blocktris.recursos.ManagerEscenas;
import proyecto.blocktris.recursos.ManagerEscenas.TipoEscena;
import proyecto.blocktris.recursos.ManagerRecursos;

/*
 * 
 * 
 * f
 */

/**
 * Clase que representa el menú del juego.
 */
public class EscenaMenu extends MenuScene {

	
	/** código de retorno para identificar el resultado  */
	 final static int ACTIVIDAD_BLUETOOTH = 6;
	 
 	/** El boton continuar. */
 	IMenuItem botonContinuar = null;
	  
  	/** El boton puntuaciones. */
  	IMenuItem botonPuntuaciones;
	  
  	/** El boton nueva partida. */
  	IMenuItem botonNuevaPartida;
	  
  	/** El boton instrucciones. */
  	IMenuItem botonInstrucciones;
	  
  	/** El boton salir. */
  	IMenuItem botonSalir;
	 
	 /** Menu Continuar ID_CONTINUAR. */
 	public static final int ID_CONTINUAR=0;
	 
 	/** Menu nuevapartida */
 	public static final int ID_NUEVAPARTIDA=1;
	 
 	/** Menu puntuaciones */
 	public static final int ID_PUNTUACIONES=2;
	 
 	/** Menu instrucciones */
 	public static final int ID_INSTRUCCIONES=3;
	 
 	/** Menu salir. */
 	public static final int ID_SALIR=4;
	 
	/**
	 * Instancia escena menu.
	 * 
	 * @param camara
	 *            la camara
	 * @param juegoAcabado
	 *            identifica deba mostrar el boton continuar o no.
	 * @param listener
	 *            el listener  al que mandar los eventos
	 */
	public EscenaMenu(Camera camara,boolean juegoAcabado,IOnMenuItemClickListener listener ){
		super(camara);
		 this.setOnMenuItemClickListener(listener);
		/*
		 * Aparentemente es imposible hacer una escena semitransparente o aplicar el mismo efecto a su  fondo
		 * 
		 * Tanto la clase Background  cómo scene   heredan de entity pero ignoran
		 * el método setAlpha(float)
		 * 
		 * La manera recomendada es crear manualmente  un fondo  con un gráfico que ocupe toda la escena
		 *  y ponerle a este ultimo su transaparencia.
		 *  
		 *  NOTA: Puede ser  inceficiente  en escenas con muchos graficos por encima del fondo ...
		 */
		Rectangle fondo = new Rectangle(camara.getCenterX(),
										camara.getCenterY(),
										camara.getWidth(),
										camara.getHeight(),
										ManagerRecursos.getInstancia().vbom	);
	
		fondo.setColor(0, 0, 0, 0.3f);
		this.attachChild(fondo);
		// entrada con transaparencia
		this.setMenuSceneAnimator(new AlphaMenuSceneAnimator() ) ;
		

		  //botones
		
		   botonPuntuaciones = new ScaleMenuItemDecorator(new TextMenuItem(ID_PUNTUACIONES, ManagerRecursos.getInstancia().fGlobal , ManagerRecursos.getInstancia().actividadJuego.getText(R.string.menu_puntuaciones), ManagerRecursos.getInstancia().vbom) , 1.1f, 1);
		  botonNuevaPartida = new ScaleMenuItemDecorator(new TextMenuItem(ID_NUEVAPARTIDA, ManagerRecursos.getInstancia().fGlobal , ManagerRecursos.getInstancia().actividadJuego.getText(R.string.menu_nuevaPartida), ManagerRecursos.getInstancia().vbom) , 1.1f, 1);
		  botonInstrucciones = new ScaleMenuItemDecorator(new TextMenuItem(ID_INSTRUCCIONES, ManagerRecursos.getInstancia().fGlobal , ManagerRecursos.getInstancia().actividadJuego.getText(R.string.menu_instrucciones), ManagerRecursos.getInstancia().vbom) , 1.1f, 1);
		   botonSalir = new ScaleMenuItemDecorator(new TextMenuItem(ID_SALIR, ManagerRecursos.getInstancia().fGlobal , ManagerRecursos.getInstancia().actividadJuego.getText(R.string.menu_salir), ManagerRecursos.getInstancia().vbom) , 1.1f, 1);
			 
		if(!juegoAcabado){
		   botonContinuar = new ScaleMenuItemDecorator(new TextMenuItem(ID_CONTINUAR, ManagerRecursos.getInstancia().fGlobal , ManagerRecursos.getInstancia().actividadJuego.getText(R.string.menu_continuar), ManagerRecursos.getInstancia().vbom) , 1.1f, 1);
		  addMenuItem(botonContinuar);
		}
		  addMenuItem(botonNuevaPartida);
		  addMenuItem(botonPuntuaciones);
		addMenuItem(botonInstrucciones);
		addMenuItem(botonSalir);
		buildAnimations();
		 
		//sin fondo
		setBackgroundEnabled(false);
		
		 
	
		  
		 
	}

	


	
	

}
