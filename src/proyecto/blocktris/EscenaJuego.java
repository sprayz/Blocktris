package proyecto.blocktris;

import java.util.ArrayList;
import java.util.Random;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.particle.BatchedSpriteParticleSystem;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.BaseParticleEmitter;
import org.andengine.entity.particle.emitter.IParticleEmitter;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.AlphaParticleInitializer;
import org.andengine.entity.particle.initializer.BlendFunctionParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
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
import org.andengine.util.adt.array.ArrayUtils;
import org.andengine.util.adt.color.Color;
import org.andengine.util.adt.list.ListUtils;

import android.hardware.SensorManager;
import android.opengl.GLES20;
import android.util.Log;
import android.view.MotionEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;



import proyecto.blocktris.logica.EscenaBase;
import proyecto.blocktris.logica.fisica.ObjetoFisico;
import proyecto.blocktris.logica.fisica.piezas.IPieza;
import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaBase.Bloque;
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
	static final int FILAS =    12 ;
	static final int COLUMNAS = 8;
	static final int MAX_MULTITOQUE=8 ;
	
	private ParticleSystem[] particulas;
	private MouseJoint[] joints;
	
	
	@Override
	public void crearEscena() {
		joints = new MouseJoint[MAX_MULTITOQUE];
		particulas = new ParticleSystem[MAX_MULTITOQUE];
		// dejamos  1/10 de el tamaño del bloque  de margen
		tamaño_bloque = camara.getWidth() /  ( COLUMNAS+0.2f);
		

		
		motor.registerUpdateHandler(new FPSLogger());
		//fondo
		setBackground(new Background(Color.RED));
		
		//mundo fisico
		mundo= new FixedStepPhysicsWorld(60,new Vector2(0, -SensorManager.GRAVITY_EARTH), false);
		//activamos le sensor de rientacion para cambiar la gravedad
		//managerRecursos.actividadJuego.getEngine().enableAccelerationSensor(managerRecursos.actividadJuego,this);
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
		techo=PhysicsFactory.createLineBody(mundo, 0, camara.getHeight()/2,camara.getWidth() , camara.getHeight()/2, fdef_muro);
		pared_izquierda=PhysicsFactory.createLineBody(mundo, 0, 0,0, camara.getHeight()*1.5f, fdef_muro);
		pared_derecha=PhysicsFactory.createLineBody(mundo, camara.getWidth(), 0,camara.getWidth() , camara.getHeight()*1.5f, fdef_muro);
		
		
		Random rnd = new Random();
	
		for(int i =0;i<2;i++){
			PiezaPalo pieza = new PiezaPalo(mundo, camara.getWidth() * rnd.nextFloat(), camara.getHeight()* rnd. nextFloat()+0.5f, tamaño_bloque, IPieza.FIXTUREDEF_DEFECTO ); 
			pieza.getCuerpo().setBullet(true);
			
			
	
			pieza.registrarGraficos(this);
			pieza.registrarAreasTactiles(this);
		}
		
		
		mundo.setContactListener(new ContactListener() {
			
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
				
				
				Body cuerpoPieza;
				//si alguno de los cuerpos colicionando es el techo...
				if((contact.getFixtureA().getBody()==techo ) ||
						(contact.getFixtureB().getBody()==techo)){
					//chulo ehh?
					//sacamos como cuerpo de la pieza aquel que no sea el techo.
					cuerpoPieza = contact.getFixtureB().getBody() == techo? contact.getFixtureA().getBody():contact.getFixtureB().getBody();
					
				
					//Log.d("COLISION", "NORMAL Y = " +contact.getWorldManifold().getNormal().y );
					//  no colisionamos
					contact.setEnabled(false);
					
					
					/*
					 * la alternativa a este método es  usar el vector normal, es decir la  dirección en  que  el
					 * motor aplicaría la fuerza para compensar   dos cuerpos incrustados
					 *  el problema es que  cambia de sentido a mitad  de  cada fixtura(lógicamente)
					 *  
					 *  y esto causa que  se reactive al colisión y  que el motor repela los dos cuerpos que 
					 *  , de repente ,están incrustados  y colisionando.
					 *  esto hace un efecto  elástico que se incrementa cuantas mas fixturas tenga la  pieza
					 *  La consecuencia es que  el cuerpo sure una aceleración considerable al atraversar el techo.
					 *  
					 * 
					 * 
					 */
					//por cada punto de colision
					for(Vector2 p : contact.getWorldManifold().getPoints()){
						// si resulta que la velocidad lineal  es positiva ( va hacia arriba)
						
						if(cuerpoPieza.getLinearVelocityFromWorldPoint(p).y > 0 ) {
							//activamos la colision y  salimos
							contact.setEnabled(true);
							return;
								
						}
						
					}
					
				}
					
				
				
				
			}
			
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void endContact(Contact contact) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beginContact(Contact contact) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
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
		

		/*
		 * realmente el MouseJoint solo usa  un cuerpo(el bodyB) pero  es obligatorio suministrarle otro
		 *  por lo que  tradicionalmente se usa el del suelo ( que suele estar siempre presente)
		 * 
		 */
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
					IParticleEmitter pe = new PointParticleEmitter(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
					particulas[pSceneTouchEvent.getPointerID()] = new SpriteParticleSystem(pe, 20, 20, 360, 
						     managerRecursos .trBloques, vbom);
						
					particulas[pSceneTouchEvent.getPointerID()].addParticleInitializer(new ColorParticleInitializer<Sprite>(100, 0, 0));
					particulas[pSceneTouchEvent.getPointerID()].addParticleInitializer(new AlphaParticleInitializer<Sprite>(0));
					particulas[pSceneTouchEvent.getPointerID()].addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
					particulas[pSceneTouchEvent.getPointerID()].addParticleInitializer(new VelocityParticleInitializer<Sprite>(-20, 20, -20, -10));
					particulas[pSceneTouchEvent.getPointerID()].addParticleInitializer(new RotationParticleInitializer<Sprite>(0.0f, 360.0f));
					particulas[pSceneTouchEvent.getPointerID()].addParticleInitializer(new ExpireParticleInitializer<Sprite>(6));

					particulas[pSceneTouchEvent.getPointerID()].addParticleModifier(new ScaleParticleModifier<Sprite>(0, 5, 1.0f, 2.0f));
					particulas[pSceneTouchEvent.getPointerID()].addParticleModifier(new ColorParticleModifier<Sprite>(40, 6, 1, 1, 0.5f, 1, 0, 1));
					particulas[pSceneTouchEvent.getPointerID()].addParticleModifier(new AlphaParticleModifier<Sprite>(0, 1, 0, 10));
					particulas[pSceneTouchEvent.getPointerID()].addParticleModifier(new AlphaParticleModifier<Sprite>(5, 60, 1, 0));
					EscenaJuego.this.attachChild(particulas[pSceneTouchEvent.getPointerID()]);
					return true;
				case TouchEvent.ACTION_MOVE:
					((BaseParticleEmitter) particulas[pSceneTouchEvent.getPointerID()].getParticleEmitter()).setCenter(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
					if(joints[pSceneTouchEvent.getPointerID()]!= null) {
						final Vector2 vec = Vector2Pool.obtain(pSceneTouchEvent.getX() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, pSceneTouchEvent.getY() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
						joints[pSceneTouchEvent.getPointerID()].setTarget(vec);
						//joints[pSceneTouchEvent.getPointerID()].getBodyB().applyAngularImpulse(0.5f);
						Vector2Pool.recycle(vec);
					}
					return true;
				case TouchEvent.ACTION_UP:
					if(joints[pSceneTouchEvent.getPointerID()] != null) {
						mundo.destroyJoint(joints[pSceneTouchEvent.getPointerID()]);
						joints[pSceneTouchEvent.getPointerID()] = null;
					}
					particulas[pSceneTouchEvent.getPointerID()].detachSelf();
					return true;
			}
			return false;
		}
		return false;
		
	}


	
	

}
