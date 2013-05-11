package proyecto.blocktris;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.*;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.*;
import org.andengine.entity.scene.menu.item.decorator.*;
import org.andengine.util.adt.color.Color;

import android.util.Log;
import android.widget.Toast;

import proyecto.blocktris.logica.EscenaBase;
import proyecto.blocktris.recursos.ManagerEscenas;
import proyecto.blocktris.recursos.ManagerEscenas.TipoEscena;

public class EscenaMenu extends EscenaBase implements IOnMenuItemClickListener{


	@Override
	public void crearEscena() {
		  MenuScene menuScene;
		// TODO Auto-generated method stub
		setBackground(new Background(Color.BLUE));
		 
		  menuScene = new MenuScene(camara);
		  final IMenuItem botonSingle = new ScaleMenuItemDecorator(new TextMenuItem(1, managerRecursos.fGlobal , "Single", vbom) , 1.1f, 1);
		  final IMenuItem botonMulti = new ScaleMenuItemDecorator(new TextMenuItem(2, managerRecursos.fGlobal , "Multi", vbom) , 1.1f, 1);
		  final IMenuItem botonPuntuaciones = new ScaleMenuItemDecorator(new TextMenuItem(3, managerRecursos.fGlobal , "Salir", vbom) , 1.1f, 1);
		  final IMenuItem botonSalir = new ScaleMenuItemDecorator(new TextMenuItem(4, managerRecursos.fGlobal , "Salir", vbom) , 1.1f, 1);
			 
		
		 
		  menuScene.addMenuItem(botonSingle);
		menuScene.addMenuItem(botonMulti);
		menuScene.addMenuItem(botonPuntuaciones);
		menuScene.addMenuItem(botonSalir);
		  menuScene.buildAnimations();
		 
		  menuScene.setBackgroundEnabled(false);
		  menuScene.setOnMenuItemClickListener(this);
		  this.setChildScene(menuScene);
		  
		  Log.d("MENU", "MEMNU CREADO");
	}

	@Override
	public void teclaVolverPreionada() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TipoEscena getTipoEscena() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deshacerEscena() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		Log.d("MENU", "CLICK REGISTRADO");
		
		
		switch(pMenuItem.getID()){
		
		case 1:
			ManagerEscenas.getInstancia().setEscena(TipoEscena.ESCENA_JUEGO );
			return true;
		case 2:
			return true;
		default:
		
		}
		
		
		
		
		
		return false;
	}

}
