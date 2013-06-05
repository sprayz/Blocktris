package proyecto.blocktris;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.*;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
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

public class EscenaMenu extends MenuScene implements IOnMenuItemClickListener{

	
	final static int ACTIVIDAD_BLUETOOTH = 6;

	public EscenaMenu(Camera camara){
		super(camara);
		
		// TODO Auto-generated method stub
		//setBackground(new Background(Color.BLUE));
		setBackgroundEnabled(true);
	
		  
		  
		  
		  final IMenuItem botonSingle = new ScaleMenuItemDecorator(new TextMenuItem(1, ManagerRecursos.getInstancia().fGlobal , ManagerRecursos.getInstancia().actividadJuego.getText(R.string.menu_continuar), ManagerRecursos.getInstancia().vbom) , 1.1f, 1);
		  final IMenuItem botonMulti = new ScaleMenuItemDecorator(new TextMenuItem(2, ManagerRecursos.getInstancia().fGlobal , ManagerRecursos.getInstancia().actividadJuego.getText(R.string.menu_multijugador), ManagerRecursos.getInstancia().vbom) , 1.1f, 1);
		  final IMenuItem botonNuevaPartida = new ScaleMenuItemDecorator(new TextMenuItem(3, ManagerRecursos.getInstancia().fGlobal , ManagerRecursos.getInstancia().actividadJuego.getText(R.string.menu_nuevaPartida), ManagerRecursos.getInstancia().vbom) , 1.1f, 1);
		  final IMenuItem botonInstrucciones = new ScaleMenuItemDecorator(new TextMenuItem(4, ManagerRecursos.getInstancia().fGlobal , ManagerRecursos.getInstancia().actividadJuego.getText(R.string.menu_instrucciones), ManagerRecursos.getInstancia().vbom) , 1.1f, 1);
		  final IMenuItem botonSalir = new ScaleMenuItemDecorator(new TextMenuItem(5, ManagerRecursos.getInstancia().fGlobal , ManagerRecursos.getInstancia().actividadJuego.getText(R.string.menu_salir), ManagerRecursos.getInstancia().vbom) , 1.1f, 1);
			 
		
		 
		  addMenuItem(botonSingle);
		addMenuItem(botonMulti);
		addMenuItem(botonNuevaPartida);
		addMenuItem(botonInstrucciones);
		addMenuItem(botonSalir);
		buildAnimations();
		 
		setBackgroundEnabled(false);
		 setOnMenuItemClickListener(this);
	
		  
		  Log.d("MENU", "MEMNU CREADO");
	}

	
	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		Log.d("MENU", "CLICK REGISTRADO");
		
		
		switch(pMenuItem.getID()){
	
		case 1:
			this.back();
			
			return false;
		case 2:
			ActividadBluetooth.lanzar(ManagerRecursos.getInstancia().actividadJuego);
			return true;
		
		case 3:
			ManagerEscenas.getInstancia().setEscena(TipoEscena.ESCENA_JUEGO );
			return true;
		case 4:
			ActividadBluetooth.lanzar(ManagerRecursos.getInstancia().actividadJuego);
			return true;
		case 5:
			this.back();
			return true;
		
		

		default:
		
		}
		
		
		
		
		
		return false;
	}

	
	

}
