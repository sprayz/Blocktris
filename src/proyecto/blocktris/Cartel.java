package proyecto.blocktris;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.animator.IMenuSceneAnimator;
import org.andengine.entity.text.Text;
import org.andengine.util.TextUtils;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

import android.util.Log;

import proyecto.blocktris.recursos.ManagerRecursos;


public class Cartel extends Entity{
	String texto ;
	float tiempoActivoSegundos;
	IEaseFunction entrada;
	IEaseFunction salida;
	Text cartel;	

	 
	public Cartel(String texto,float tiempoSegundos) {
		super();
		this.entrada = EaseLinear.getInstance();
		this.salida = EaseLinear.getInstance();
		cartel = new Text(this.getX(),this.getY(),
				ManagerRecursos.getInstancia().fGlobal, 
				texto,
				ManagerRecursos.getInstancia().vbom);
		this.attachChild(cartel);
	}




	public Cartel(float pX, float pY,String texto,float tiempoSegundos,IEaseFunction entrada,IEaseFunction salida) {
		super(pX, pY);
		this.entrada = entrada;
		this.salida= salida;
		cartel = new Text(this.getX(),this.getY(),
				ManagerRecursos.getInstancia().fGlobal, 
				texto,
				ManagerRecursos.getInstancia().vbom);
		this.attachChild(cartel);
	}


	public void reiniciarAnimacion(){
		this.resetEntityModifiers();
		
			
		
		
	}


	
	@Override
	public void onAttached() {
		inicializar();
		super.onAttached();
	}
	@Override
	public void onDetached() {
		this.clearEntityModifiers();
		super.onAttached();
	}



	@Override
	public void setPosition(IEntity pOtherEntity) {
		
		super.setPosition(pOtherEntity);
		cartel.setPosition(this);
	}




	@Override
	public void setPosition(float pX, float pY) {
		
		super.setPosition(pX, pY);
		cartel.setPosition(this);
		Log.e("CARTEL X", cartel.getX() + "   " + cartel.getY());
	}




	private void inicializar(){
		
		cartel.setHeight(this.getParent().getHeight()/10);
		cartel.setPosition(this);
	//	Log.e("CARTEL X", cartel.getX() + "   " + cartel.getY());
		
		
		
		
		
	
			this.registerEntityModifier( 
				
				new SequenceEntityModifier(
				new IEntityModifier.IEntityModifierListener() {
			
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				pItem.setVisible(true);
				
			}
		}
		
				
				,new MoveXModifier(tiempoActivoSegundos/2,0,this.getX(),entrada),
				new MoveXModifier(tiempoActivoSegundos/2,this.getX(),
						ManagerRecursos.getInstancia().camara.getWidth(),entrada)));
	}
	

}
