package proyecto.blocktris;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.particle.BatchedSpriteParticleSystem;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.BaseParticleEmitter;
import org.andengine.entity.particle.emitter.IParticleEmitter;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.AlphaParticleInitializer;
import org.andengine.entity.particle.initializer.BlendFunctionParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.IParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.primitive.Gradient;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.EntityBackground;
import org.andengine.entity.scene.background.IBackground;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.sprite.batch.SpriteGroup;
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
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;



import proyecto.blocktris.logica.EscenaBase;
import proyecto.blocktris.logica.fisica.ObjetoFisico;
import proyecto.blocktris.logica.fisica.piezas.IPieza;
import proyecto.blocktris.logica.fisica.piezas.PiezaFactory;
import proyecto.blocktris.logica.fisica.piezas.rompibles.*;
import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaBase.Bloque;
import proyecto.blocktris.recursos.ManagerEscenas.TipoEscena;

public class EscenaJuego extends EscenaBase implements IAccelerationListener, IOnSceneTouchListener, IOnAreaTouchListener, ITimerCallback {

	public static float MARGENES = 20;
	public static FixtureDef fdef_muro = PhysicsFactory.createFixtureDef(1.0f, 0, 1.0f);
	public Body suelo ; 
	public Body techo; 
	public Body pared_izquierda ; 
	public Body pared_derecha ; 
	PhysicsWorld mundo ; 
	float tamaño_bloque;
	 public static final int FILAS =    9 ;
	public static final int COLUMNAS = 6;
	public static final int MAX_MULTITOQUE=8 ;
	
	public static final float intervaloPieza = 6f ;
	private Entity capaBaja ;
	private Entity capaAlta;
	private ParticleSystem<Sprite>[] particulas;
	private MouseJoint[] joints;
	private Gradient degradadoFondo;
	private Background fondo;
	
	
	private TimerHandler timer;
	/*
	 * ESTADO DEL  JUEGO
	 */
	protected ArrayList<IPieza> piezas;
	protected IPieza ultimaPieza;
	protected ArrayList<Bloque> bloquesLinea;
	protected float puntuacion;
	protected float intervalo;
	protected boolean ganador;
	
	@Override
	public void crearEscena() {
		/*
		 * VARIABLES
		 * 
		 */
		piezas= new ArrayList<IPieza>();
		joints = new MouseJoint[MAX_MULTITOQUE];
		bloquesLinea= new ArrayList<Bloque>();
		timer =  new TimerHandler(intervaloPieza,true,this);
		//FONDO
		
		degradadoFondo  = new Gradient(camara.getWidth()/2,camara.getHeight()/2,camara.getWidth(), camara.getHeight(), vbom);
		degradadoFondo.setFromColor(0.1f, 0.2f, 0.5f);
		degradadoFondo.setToColor(1, 1, 1);
		degradadoFondo.setGradientFitToBounds(true);
		degradadoFondo.setGradientDitherEnabled(true);
		
		fondo = new EntityBackground(degradadoFondo);
		
		this.setBackgroundEnabled(true);
		this.setBackground(fondo);
	
		
		//CAPAS
		capaBaja= new Entity();
		capaAlta= new Entity();
		this.attachChild(capaBaja);
		this.attachChild(capaAlta);
		
		//CAJA
		
		// dejamos  1/10 de el tamaño del bloque  de margen
		tamaño_bloque = (camara.getWidth() - MARGENES*2) /  ( COLUMNAS+0.25f);
		
		
		inicializarSistemasParticulas();
		motor.registerUpdateHandler(new FPSLogger());
		
		//FISICA
		//gravedad hacia abajo :-]
		mundo= new FixedStepPhysicsWorld(60,new Vector2(0, -SensorManager.GRAVITY_EARTH),true,20,16);
		mundo.setContinuousPhysics(true);
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
		this.capaBaja.attachChild(caja);
		caja.setVisible(true);
		caja.animate(100);
		
		//PAREDES
		suelo =PhysicsFactory.createLineBody(mundo, 0, 0+MARGENES, camara.getWidth(), 0+MARGENES, fdef_muro);
		techo=PhysicsFactory.createLineBody(mundo, 0, camara.getHeight()-MARGENES,camara.getWidth() , camara.getHeight()-MARGENES, fdef_muro);
		pared_izquierda=PhysicsFactory.createLineBody(mundo, 0, 0-MARGENES,0, camara.getHeight()*1.5f-MARGENES, fdef_muro);
		pared_derecha=PhysicsFactory.createLineBody(mundo, camara.getWidth(), 0+MARGENES,camara.getWidth(), camara.getHeight()*1.5f-MARGENES, fdef_muro);
		

		
		
		/*
		 * aquí filtramos las colisiones para permitir que las  piezas entren en la caja
		 * 
		 * es decir, puedan atravesar el techo para descender pero no para ascender (^_^)
		 */
		
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
					cuerpoPieza = contact.getFixtureB().getBody() == techo
							? contact.getFixtureA().getBody():contact.getFixtureB().getBody();
					
				
					//Log.d("COLISION", "NORMAL Y = " +contact.getWorldManifold().getNormal().y );
					//  no colisionamos
					contact.setEnabled(false);
					
					
					/*
					 * la alternativa a este método es  usar el vector normal, es decir la  dirección en  que  el
					 * motor aplicaría la fuerza para compensar   dos fixturas incrustados
					 *  el problema es que  cambia de sentido a mitad  de  cada fixtura
					 *  
					 *  y esto causa que  se reactive al colisión y  que el motor repela los dos cuerpos que 
					 *  , de repente ,están incrustados  y colisionando.
					 *  esto hace un efecto  elástico que se incrementa cuantas mas fixturas tenga la  pieza
					 *  La consecuencia es que  el cuerpo sufre una aceleración considerable al atraversar el techo.
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
	    reiniciarEscena();
	    iniciarPartida();
	}
	
	/*
	 * EVENTOS
	 * 
	 */
	/*
	public boolean onIniciarPartida(){return true;}
	public void onPartidaIniciada(){}
	public boolean onFinalizarPartida(){return true;}
	public void  onPartidaFinalizada(){}
	public  void onQuitarLinea(Collection<Bloque> bloques){}
	public void onLineaQuitada(){};
	public void onPausado(){};
	public void onReanudado(){};
	*/
	
	public boolean onIniciarPartida(){return true;}
	public void onPartidaIniciada(){}
	public boolean onFinalizarPartida(){return true;}
	public void  onPartidaFinalizada(){}
	public  void onQuitarLinea(Collection<Bloque> bloques){}
	public void onLineaQuitada(){};
	public void onPausado(){};
	public void onReanudado(){};
	
	
	
	public void iniciarPartida(){
		if(!onIniciarPartida())
			return;
		
		
		motor.registerUpdateHandler(timer);
		Random rnd = new Random();
		for(int i =0;i<3;i++){
			IPieza pieza = PiezaFactory.piezaAleatoria(mundo, camara.getWidth() * rnd.nextFloat(), camara.getHeight()* rnd. nextFloat()+0.5f, tamaño_bloque, IPieza.FIXTUREDEF_DEFECTO,PiezaBase.BODYDEF_DEFECTO ); 

		pieza.getCuerpo().setBullet(true);
		
		
		/*
		for(IPieza p : pieza2.quitarBloqueDesenlazar(pieza2.getBloques().get(2))){
			p.registrarAreasTactiles(this);
			p.registrarGraficos(this.capaBaja);
			p.getCuerpo().setBullet(true);
		}
		*/
		
			pieza.registrarGraficos(this.capaBaja);
			pieza.registrarAreasTactiles(this);
			piezas.add(pieza);
			onPartidaIniciada();
			
		 
		}
		
		onPartidaIniciada();
	}
	
	@Override
	public void reiniciarEscena() {
		for(IPieza p : piezas ){
			p.destruirPieza();
		}
		System.gc();
		piezas.clear();
		
		iniciarPartida();
		
		Log.d("REINICIO", "CUERPOS: " + mundo.getBodyCount());
	}
	
	

	@Override
	public void teclaVolverPreionada() {
		// TODO Auto-generated method stub
		
		managerRecursos.actividadJuego.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				reiniciarEscena();
			
			}
		});
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
	 * Esta función inicializa un  sistema de partículas por cada puntero posible
	 */
	private void inicializarSistemasParticulas(){
		particulas = new SpriteParticleSystem[MAX_MULTITOQUE];
		
		
		for(int i =0; i< particulas.length;i++ ){
			
			IParticleEmitter pe = new PointParticleEmitter(0,0);
			
		 
		 particulas[i]=	new SpriteParticleSystem(pe, 100, 100, 300, 
				     managerRecursos .trAnimBrillo.getTextureRegion(1), vbom);
				
		
		//efectos para cada partícula
		 particulas[i].addParticleInitializer(new ColorParticleInitializer<Sprite>(1,1, 0));
	//	 particulas[i].addParticleInitializer(new AlphaParticleInitializer<Sprite>(1));
		 particulas[i].addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
		 particulas[i].addParticleInitializer(new VelocityParticleInitializer<Sprite>(-2, 2, -2, -1));
		 particulas[i].addParticleInitializer(new RotationParticleInitializer<Sprite>(0.0f, 360.0f));
		 
		 //si no ponemos tiempo de expiracion las particulas solo se retiran cuando llegan
		 //al máximo
		 particulas[i].addParticleInitializer(new ExpireParticleInitializer<Sprite>(1));

		 particulas[i].addParticleModifier(new ScaleParticleModifier<Sprite>(0, 0.25f, 1f, 1.5f));
		 particulas[i].addParticleModifier(new ColorParticleModifier<Sprite>(0.5f, 1, 1, 1, 0.5f, 1, 0, 1));
		// particulas[i].addParticleModifier(new AlphaParticleModifier<Sprite>(0, 0.2f, 0, 1 ));
		// particulas[i].addParticleModifier(new AlphaParticleModifier<Sprite>(0.5f,1 , 1, 0));
		// no queremos que estén  activados desde el principio
		 particulas[i].setParticlesSpawnEnabled(false);
		// los añadimos a la escena
		 this.capaAlta.attachChild( particulas[i]);
		}
		
	}
	
	
	
	
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
		mouseJointDef.dampingRatio = 0.00f;
		mouseJointDef.frequencyHz = 20f;
		mouseJointDef.maxForce = (200* body.getMass()*4);
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
			final ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		
		if(pSceneTouchEvent.isActionDown() && pSceneTouchEvent.getPointerID() <MAX_MULTITOQUE) {
			final IEntity entity = (IEntity) pTouchArea;
			if(joints[pSceneTouchEvent.getPointerID()]== null) {
				final IPieza pieza = (IPieza) ((ObjetoFisico)entity.getUserData()).getPadre();
				
				joints[pSceneTouchEvent.getPointerID()] = this.createMouseJoint(entity, pTouchAreaLocalX, pTouchAreaLocalY);
			}
			
			
			return false;
		}
		return false;
		
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		
		
		
		
		
		
		
		
		if(pSceneTouchEvent.getPointerID()==1){
			degradadoFondo.setGradientVector(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
			degradadoFondo.setGradientAngle( (float) (180/ Math.PI * Math.atan2(degradadoFondo.getX() - pSceneTouchEvent.getX(), pSceneTouchEvent.getY() - degradadoFondo.getY())));
		}
		if(this.mundo != null && pSceneTouchEvent.getPointerID() <MAX_MULTITOQUE) {
			
			switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					 particulas[pSceneTouchEvent.getPointerID()].setParticlesSpawnEnabled(true);
					// ((BaseParticleEmitter) particulas[pSceneTouchEvent.getPointerID()].getParticleEmitter()).setCenter(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
					return true;
				case TouchEvent.ACTION_MOVE:
					//((BaseParticleEmitter) particulas[pSceneTouchEvent.getPointerID()].getParticleEmitter()).setCenter(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
					if(joints[pSceneTouchEvent.getPointerID()]!= null) {
						final Vector2 vec = Vector2Pool.obtain(pSceneTouchEvent.getX() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, pSceneTouchEvent.getY() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
						joints[pSceneTouchEvent.getPointerID()].setTarget(vec);
						//joints[pSceneTouchEvent.getPointerID()].getBodyB().applyAngularImpulse(4.5f);
						Vector2Pool.recycle(vec);
					}
					return true;
				case TouchEvent.ACTION_UP:
					if(joints[pSceneTouchEvent.getPointerID()] != null) {
						mundo.destroyJoint(joints[pSceneTouchEvent.getPointerID()]);
						joints[pSceneTouchEvent.getPointerID()] = null;
					}
					particulas[pSceneTouchEvent.getPointerID()].setParticlesSpawnEnabled(false);
					return true;
			}
			return false;
		}
		return false;
		
	}

	//ha pasado el tiempo  de un timer
	@Override
	public void onTimePassed(TimerHandler pTimerHandler) {
		
		
		
		bloquesLinea.clear();
		for(int linea =0;linea<1;linea++){
			Vector2 p1;
			Vector2 p2;
			//tiramos una linea  que atraviese la caja sin tocar los muros
			p1 = Vector2Pool.obtain(0 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
					(linea*tamaño_bloque+MARGENES+(tamaño_bloque/2))/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
			
			p2 = Vector2Pool.obtain((camara.getWidth() ) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
					p1.y);
			//this.capaAlta.attachChild(new Line(p1.x,p1.y,p2.x,p2.y,vbom));
			
			mundo.rayCast(new RayCastCallback() {
				
				@Override
				public float reportRayFixture(Fixture fixture, Vector2 point,
						Vector2 normal, float fraction) {
					if( !(fixture.getBody().getType()==BodyType.StaticBody) &&
							Math.toDegrees(fixture.getBody().getAngle()) <10 && Math.toDegrees(fixture.getBody().getAngle()) >-10 ){
						bloquesLinea.add((Bloque) fixture.getUserData());
					}
					return -1;
				}
			},p1 ,p2  );
			
			for(Bloque b : bloquesLinea){
				b.getGrafico().animate(10);
				
			}
		Vector2Pool.recycle(p1);
		Vector2Pool.recycle(p2);
		}
		
		
		
	}

	


	
	

}
