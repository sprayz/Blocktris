package proyecto.blocktris;

import java.util.Random;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.debugdraw.DebugRenderer;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.ITouchController;
import org.andengine.input.touch.controller.ITouchEventCallback;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.input.touch.controller.MultiTouchController;
import org.andengine.opengl.texture.region.*;
import org.andengine.util.adt.color.Color;

import android.hardware.SensorManager;
import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;



import proyecto.blocktris.logica.EscenaBase;
import proyecto.blocktris.logica.fisica.ObjetoFisico;
import proyecto.blocktris.logica.fisica.piezas.IPieza;
import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaPalo;
import proyecto.blocktris.recursos.ManagerEscenas.TipoEscena;

public class EscenaJuego extends EscenaBase implements IAccelerationListener, IOnSceneTouchListener, IOnAreaTouchListener {

	public static FixtureDef fdef_muro = PhysicsFactory.createFixtureDef(1.0f, 0, 1.0f);
	public Body suelo ; 
	public Body techo; 
	public Body pared_izquierda ; 
	public Body pared_derecha ; 
	PhysicsWorld mundo ; 
	float tamaño_bloque;
	static final int FILAS = 12 ;
	static final int COLUMNAS = 8;
	static final int MAX_MULTITOQUE=8 ;
	
	
	private MouseJoint[] joints;
	
	
	@Override
	public void crearEscena() {
		joints = new MouseJoint[MAX_MULTITOQUE];
		// dejamos  1/10 de el tamaño del bloque  de margen
		tamaño_bloque = camara.getWidth() /  ( COLUMNAS);
		
		tamaño_bloque = tamaño_bloque - 0.2f / COLUMNAS;
		
		motor.registerUpdateHandler(new FPSLogger());
		//fondo
		setBackground(new Background(Color.RED));
		
		//mundo fisico
		mundo= new FixedStepPhysicsWorld(60,new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		//activamos le sensor de rientacion para cambiar la gravedad
		managerRecursos.actividadJuego.getEngine().enableAccelerationSensor(managerRecursos.actividadJuego,this);
		 motor .setTouchController(new MultiTouchController());
		
		//activamos el toque
		setOnSceneTouchListener(this);
		setOnAreaTouchListener(this);
		
		
		//sprite de referencia marcando el centro
		AnimatedSprite caja = new AnimatedSprite(camara.getCenterX(),
				camara.getCenterY(),
				
				managerRecursos.trBloques.deepCopy() ,vbom);
		caja.setSize(10, 10);
		super.attachChild(caja);
		caja.setVisible(true);
		caja.animate(100);
		
		//creammos las paredes
		suelo =PhysicsFactory.createLineBody(mundo, 0, 0, camara.getWidth(), 0, fdef_muro);
		techo=PhysicsFactory.createLineBody(mundo, 0, camara.getHeight(),camara.getWidth() , camara.getHeight(), fdef_muro);
		pared_izquierda=PhysicsFactory.createLineBody(mundo, 0, 0,0, camara.getHeight(), fdef_muro);
		pared_derecha=PhysicsFactory.createLineBody(mundo, camara.getWidth(), 0,camara.getWidth() , camara.getHeight(), fdef_muro);
		
		Random rnd = new Random();
		for(int i =0;i<4;i++){
			PiezaPalo pieza = new PiezaPalo(mundo, camara.getWidth() * rnd.nextFloat(), camara.getHeight()* rnd. nextFloat(), tamaño_bloque, IPieza.FIXTUREDEF_DEFECTO ); 
			pieza.registrarGraficos(this);
			pieza.registrarAreasTactiles(this);
		}
		
		
		
		registerUpdateHandler(mundo); 
	    DebugRenderer debug = new DebugRenderer(mundo, vbom);
	    debug.setDrawBodies(true);
	    debug.setDrawJoints(true);
	    attachChild(debug); 
	}

	@Override
	public void teclaVolverPreionada() {
		// TODO Auto-generated method stub
		System.exit(0);
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
	
	
	
	
	
	
	
	//*******
	//UTILIDAD
	//*******
	
	
	/**
	 * Sacado del ejemplo de AE  demostrando MouseJoint
	 */
	private MouseJoint createMouseJoint(final IEntity entidad, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		final Body body = ((ObjetoFisico)entidad.getUserData()).getCuerpo();
		final MouseJointDef mouseJointDef = new MouseJointDef();

		final Vector2 localPoint = Vector2Pool.obtain((pTouchAreaLocalX - entidad.getWidth() * entidad.getOffsetCenterX()) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
				(pTouchAreaLocalY - entidad.getHeight() * entidad.getOffsetCenterY()) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
		//this.mGroundBody.setTransform(localPoint, 0);

		mouseJointDef.bodyA = suelo;
		mouseJointDef.bodyB = body;
		mouseJointDef.dampingRatio = 0.95f;
		mouseJointDef.frequencyHz = 20f;
		mouseJointDef.maxForce = (200.0f * body.getMass()*4);
		mouseJointDef.collideConnected = true;

		mouseJointDef.target.set(body.getWorldPoint(localPoint));
		Vector2Pool.recycle(localPoint);

		return (MouseJoint) mundo.createJoint(mouseJointDef);
	}
	
	
	
	
	
	
	
	
	
	
	
	//*************
	//EVENTOS
	//*************
	
	@Override
	public void onAccelerationAccuracyChanged(final AccelerationData pAccelerationData) {

	}

	@Override
	public void onAccelerationChanged(final AccelerationData pAccelerationData) {
		final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX(), pAccelerationData.getY());
		mundo.setGravity(gravity);
		Vector2Pool.recycle(gravity);
	}

	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		
		if(pSceneTouchEvent.isActionDown() && pSceneTouchEvent.getPointerID() <MAX_MULTITOQUE) {
			final IEntity entity = (IEntity) pTouchArea;
		
			if(joints[pSceneTouchEvent.getPointerID()]== null) {
				
				joints[pSceneTouchEvent.getPointerID()] = this.createMouseJoint(entity, pTouchAreaLocalX, pTouchAreaLocalY);
			}
			return true;
		}
		return false;
		
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if(this.mundo != null && pSceneTouchEvent.getPointerID() <MAX_MULTITOQUE) {
			switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					
					return true;
				case TouchEvent.ACTION_MOVE:
					if(joints[pSceneTouchEvent.getPointerID()]!= null) {
						final Vector2 vec = Vector2Pool.obtain(pSceneTouchEvent.getX() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, pSceneTouchEvent.getY() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
						joints[pSceneTouchEvent.getPointerID()].setTarget(vec);
						Vector2Pool.recycle(vec);
					}
					return true;
				case TouchEvent.ACTION_UP:
					if(joints[pSceneTouchEvent.getPointerID()] != null) {
						mundo.destroyJoint(joints[pSceneTouchEvent.getPointerID()]);
						joints[pSceneTouchEvent.getPointerID()] = null;
					}
					return true;
			}
			return false;
		}
		return false;
		
	}


	
	

}
