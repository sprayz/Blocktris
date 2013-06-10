package proyecto.blocktris;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeMap;

import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.particle.BatchedSpriteParticleSystem;
import org.andengine.entity.particle.emitter.IParticleEmitter;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.emitter.RectangleOutlineParticleEmitter;
import org.andengine.entity.particle.initializer.BlendFunctionParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.RotationParticleModifier;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.primitive.Gradient;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.EntityBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.animator.SlideMenuSceneAnimator;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.debugdraw.DebugRenderer;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.MultiTouchController;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseCubicInOut;

import android.content.Context;
import android.hardware.SensorManager;
import android.opengl.GLES20;
import android.util.Log;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.DestructionListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import proyecto.blocktris.logica.EscenaBase;
import proyecto.blocktris.logica.fisica.ObjetoFisico;
import proyecto.blocktris.logica.fisica.piezas.IPieza;
import proyecto.blocktris.logica.fisica.piezas.PiezaFactory;
import proyecto.blocktris.logica.fisica.piezas.rompibles.*;
import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaBase.Bloque;
import proyecto.blocktris.recursos.EstadoJuego;
import proyecto.blocktris.recursos.ExclStrat;
import proyecto.blocktris.recursos.ManagerEscenas;
import proyecto.blocktris.recursos.ManagerRecursos;
import proyecto.blocktris.recursos.EstadoJuego.EstadoPieza;
import proyecto.blocktris.recursos.ManagerEscenas.TipoEscena;

public class EscenaJuego extends EscenaBase implements IAccelerationListener,
		IOnSceneTouchListener, IOnAreaTouchListener, ITimerCallback,
		IEscenaTetris, IOnMenuItemClickListener {

	public static final String ARCHIVO_ESTADO = "estado.dat";
	public static float MARGENES = 20;
	public static float ANGULO_MAX = 10;
	public static double SEN_ANGULO_MIN = Math.sin(Math.toRadians(ANGULO_MAX));
	public static double SEN_ANGULO_MAX = Math.sin(Math.toRadians(-ANGULO_MAX));
	public static FixtureDef fdef_muro = PhysicsFactory.createFixtureDef(1.0f,
			0, 0.5f);
	public Body suelo;
	public Body techo;
	public Body pared_izquierda;
	public Body pared_derecha;
	PhysicsWorld mundo;
	float tamaño_bloque;

	public static final int FILAS = 12;
	public static final int COLUMNAS = 9;
	public static final int MAX_BLOQUES = FILAS * COLUMNAS;
	public static final int MAX_MULTITOQUE = 8;

	
	public static final int PUNTOS_LINEA = 100;
	public static final int MULTIPLICADOR_LINEA =2 ;
	public  float  tiempoUltimaLinea;
	private int  lineasConsecutivas;
	public static  float MAX_TIEMPOLINEA = 100;
	public static final float intervaloPonerPieza = 2f;
	public static final float intervaloComprobarLinea = 1f;
	private Entity capaBaja;
	private Entity capaAlta;
	private BatchedSpriteParticleSystem[] particulasPuntero;
	private MouseJoint[] joints;
	private Gradient degradadoFondo;
	private Background fondo;

	private TimerHandler timerPieza;
	private TimerHandler timerLinea;
	/*
	 * ESTADO DEL JUEGO
	 */
	protected boolean acabada = false;
	protected EstadoJuego estadoGuardado;
	protected ArrayList<IPieza> piezasEscena;

	private Text cartelPuntos;
	private boolean primerCargado = true;
	protected IPieza ultimaPieza;

	protected int puntuacion;

	@Override
	public void crearEscena() {
		/*
		 * VARIABLES
		 */

		//
		piezasEscena = new ArrayList<IPieza>();
		estadoGuardado = new EstadoJuego();
		
		joints = new MouseJoint[MAX_MULTITOQUE];

		timerLinea = new TimerHandler(intervaloComprobarLinea, false, this);
		timerPieza = new TimerHandler(intervaloPonerPieza, false, this);
		// FONDO

		degradadoFondo = new Gradient(camara.getWidth() / 2,
				camara.getHeight() / 2, camara.getWidth(), camara.getHeight(),
				vbom);
		degradadoFondo.setFromColor(0.0f, 0.5f, 1.0f);
		degradadoFondo.setToColor(1, 1, 1);
		degradadoFondo.setGradientFitToBounds(true);
		degradadoFondo.setGradientDitherEnabled(true);

		
		fondo = new EntityBackground(degradadoFondo);

		this.setBackgroundEnabled(true);
		this.setBackground(fondo);

		
		
		
		cartelPuntos = new Text(camara.getWidth()/2f,camara.getHeight()*(8f/9f), 
				managerRecursos.fGlobal, "", "XXXXXXXXX".length(), new TextOptions(HorizontalAlign.CENTER ),vbom);
		
		//cartelPuntos.setHeight(camara.getHeight()/9f);
		// CAPAS
		capaBaja = new Entity();
		capaAlta = new Entity();
		this.attachChild(capaBaja);
		this.attachChild(capaAlta);

		
		capaAlta.attachChild(cartelPuntos);
		cartelPuntos.setAlpha(0.8f);
		
		
		// CAJA

		// dejamos un 20% de el tamaño del bloque de margen
		tamaño_bloque = camara.getWidth() / COLUMNAS;
		tamaño_bloque = tamaño_bloque - ((tamaño_bloque / 4f / COLUMNAS));

		inicializarSistemasParticulas();
		motor.registerUpdateHandler(new FPSLogger());

		// FISICA
		// gravedad hacia abajo :-]
		mundo = new FixedStepPhysicsWorld(60, new Vector2(0,
				-SensorManager.GRAVITY_EARTH), true, 20, 16);
		mundo.setContinuousPhysics(true);
		// activamos le sensor de rientacion para cambiar la gravedad
		// managerRecursos.actividadJuego.getEngine().enableAccelerationSensor(managerRecursos.actividadJuego,this);
		motor.setTouchController(new MultiTouchController());

		// activamos el toque
		setOnSceneTouchListener(this);
		setOnAreaTouchListener(this);

		// sprite de referencia marcando el centro
		AnimatedSprite caja = new AnimatedSprite(camara.getCenterX(),
				camara.getCenterY(),

				managerRecursos.trBloques.deepCopy(), vbom);
		caja.setSize(10, 10);
		this.capaBaja.attachChild(caja);
		caja.setVisible(true);
		caja.animate(100);

		// PAREDES
		suelo = PhysicsFactory.createLineBody(mundo, 0, 0 + MARGENES,
				camara.getWidth(), 0 + MARGENES, fdef_muro);
		techo = PhysicsFactory.createLineBody(mundo, 0,
				camara.getHeight() * 1.5f, camara.getWidth(),
				camara.getHeight() * 1.5f, fdef_muro);
		pared_izquierda = PhysicsFactory.createLineBody(mundo, 0, 0 - MARGENES,
				0, camara.getHeight() * 2f - MARGENES, fdef_muro);
		pared_derecha = PhysicsFactory.createLineBody(mundo, camara.getWidth(),
				0 + MARGENES, camara.getWidth(), camara.getHeight() * 2f
						- MARGENES, fdef_muro);

		/*
		 * aquí filtramos las colisiones para permitir que las piezas entren en
		 * la caja
		 * 
		 * es decir, puedan atravesar el techo para descender pero no para
		 * ascender (^_^)
		 */
	
		
		
		

		mundo.setContactListener(new ContactListener() {

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub

				Body cuerpoPieza;
				// si alguno de los cuerpos colicionando es el techo...
				if ((contact.getFixtureA().getBody() == techo)
						|| (contact.getFixtureB().getBody() == techo)) {
					// chulo ehh?
					// sacamos como cuerpo de la pieza aquel que no sea el
					// techo.
					cuerpoPieza = contact.getFixtureB().getBody() == techo ? contact
							.getFixtureA().getBody() : contact.getFixtureB()
							.getBody();

					// Log.d("COLISION", "NORMAL Y = "
					// +contact.getWorldManifold().getNormal().y );
					// no colisionamos
					contact.setEnabled(false);

					/*
					 * la alternativa a este método es usar el vector normal, es
					 * decir la dirección en que el motor aplicaría la fuerza
					 * para compensar dos fixturas incrustados el problema es
					 * que cambia de sentido a mitad de cada fixtura
					 * 
					 * y esto causa que se reactive al colisión y que el motor
					 * repela los dos cuerpos que , de repente ,están
					 * incrustados y colisionando. esto hace un efecto elástico
					 * que se incrementa cuantas mas fixturas tenga la pieza La
					 * consecuencia es que el cuerpo sufre una aceleración
					 * considerable al atraversar el techo.
					 */
					// por cada punto de colision
					for (Vector2 p : contact.getWorldManifold().getPoints()) {
						// si resulta que la velocidad lineal es positiva ( va
						// hacia arriba)
						if (cuerpoPieza.getLinearVelocityFromWorldPoint(p).y > 0) {
							// activamos la colision y salimos
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

		this.registerUpdateHandler(timerLinea);
		this.registerUpdateHandler(timerPieza);

		
		
		
		registerUpdateHandler(mundo);
		
		/*DebugRenderer debug = new DebugRenderer(mundo, vbom);
		debug.setDrawBodies(true);
	debug.setDrawJoints(true);
	attachChild(debug);*/
	}

	/*
	 * EVENTOS
	 */

	public void guardarEstado() {

		estadoGuardado = new EstadoJuego();
		estadoGuardado.puntuacion = puntuacion;
		estadoGuardado.acabada = acabada;
		for (IPieza p : piezasEscena) {

			estadoGuardado.piezas.add(EstadoJuego.EstadoPieza.empaquetar(p));
		}

		try {

			FileOutputStream fileOut = actividadJuego.openFileOutput(
					ARCHIVO_ESTADO, Context.MODE_PRIVATE);
			/*
			 * 
			 * Evito serializar el componente Shape de los FixtureDef que
			 * contiene los vértices porque: 1.Está fuertemente atado a la
			 * librería nativa. 2.No es necesario para recrear la escena. 3.Está
			 * diseñado para ser creado y accedido solo por las funciónes de la
			 * librería nativa. En consecuencia es poco más que una referencia a
			 * una dirección de memoria en el espacio de la librería.
			 */

			Gson gson = new GsonBuilder().setExclusionStrategies(
					new ExclStrat(
							"com.badlogic.gdx.physics.box2d.FixtureDef.shape"))
					.create();
			BufferedOutputStream bos = new BufferedOutputStream(fileOut);

			bos.write(gson.toJson(estadoGuardado).getBytes());
			Log.w("SERIALIZANDO", " GUARDANDO ESTADO");
			bos.flush();
			bos.close();
			fileOut.close();

			estadoGuardado = null;
		} catch (IOException f) {
			Log.e("SERIALIZADO", f.getMessage());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void cargarEstado() {

		estadoGuardado = null;

		FileInputStream fileIn = null;

		StringBuilder sb = new StringBuilder();

		BufferedReader reader = null;

		try {
			fileIn = actividadJuego.openFileInput(ARCHIVO_ESTADO);
			reader = new BufferedReader(new InputStreamReader(fileIn, "UTF-8"));
			String linea = null;
			while ((linea = reader.readLine()) != null) {
				sb.append(linea).append("\n");

			}

			String resultado = sb.toString();
			Gson gson = new Gson();
			BufferedInputStream bis = new BufferedInputStream(fileIn);

			Log.w("DESSERIALIZANDO", " CARGANDO ESTADO");
			estadoGuardado = gson.fromJson(resultado, EstadoJuego.class);

		} catch (Exception i) {
			Log.e("DESERIALIZADO", i.getMessage());
		} finally {
			if (reader != null) {

				try {
					reader.close();
				} catch (IOException e) {

				}
			}

		}

		reiniciarEscena();
		if (estadoGuardado != null) {
			piezasEscena.clear();
			puntuacion = estadoGuardado.puntuacion;
			acabada = estadoGuardado.acabada;
			for (EstadoPieza ep : estadoGuardado.piezas) {
				IPieza pieza;
				pieza = EstadoJuego.EstadoPieza.desempaquetar(mundo, ep);
				pieza.registrarAreasTactiles(this);
				pieza.registrarGraficos(this.capaBaja);
				piezasEscena.add(pieza);

			}
			estadoGuardado.piezas.clear();
		}

		/*
		 * Actualizamos manualmente los PhysicsConnector para que posicionen
		 * correctamente los graficos respecto a los cuerpos, sin esperar al
		 * siguiente fotograma
		 */
		
		for (PhysicsConnector pc : mundo.getPhysicsConnectorManager()) {

			pc.onUpdate(0);

		}

	}

	public void finalizarPartida(boolean ganado) {
		if (!onFinalizarPartida(ganado))
			return;
		reiniciarEscena();
		this.unregisterUpdateHandler(timerLinea);
		this.unregisterUpdateHandler(timerPieza);
		puntuacion = 0;
		acabada = true;
		onPartidaFinalizada();

	}

	public void iniciarPartida() {
		acabada = false;
		if (!onIniciarPartida())
			return;

		this.registerUpdateHandler(timerLinea);
		this.registerUpdateHandler(timerPieza);

		onPartidaIniciada();
	}

	@Override
	public void reiniciarEscena() {
		for (IPieza p : piezasEscena) {
			p.destruirPieza();
		}
		System.gc();
		piezasEscena.clear();

		Log.d("REINICIO", "CUERPOS: " + mundo.getBodyCount());
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

	// *******
	// UTILIDAD
	// *******

	/**
	 * Esta función inicializa un sistema de partículas por cada puntero posible
	 */
	private void inicializarSistemasParticulas() {
		particulasPuntero = new BatchedSpriteParticleSystem[MAX_MULTITOQUE];

		for (int i = 0; i < particulasPuntero.length; i++) {

			IParticleEmitter pe = new RectangleOutlineParticleEmitter(
					tamaño_bloque / 2, tamaño_bloque / 2, tamaño_bloque * 0.9f,
					tamaño_bloque * 0.9f);

			particulasPuntero[i] = new BatchedSpriteParticleSystem(pe, 100,
					250, 500, managerRecursos.trAnimBrillo.getTextureRegion(1),
					vbom);

			// efectos para cada partícula
			particulasPuntero[i]
					.addParticleInitializer(new ColorParticleInitializer<UncoloredSprite>(
							1, 1, 0));
			// particulas[i].addParticleInitializer(new );
			particulasPuntero[i]
					.addParticleInitializer(new BlendFunctionParticleInitializer<UncoloredSprite>(
							GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
			// particulas[i].addParticleInitializer(new
			// VelocityParticleInitializer<Sprite>(120, 0, 12, 0));
			particulasPuntero[i]
					.addParticleInitializer(new RotationParticleInitializer<UncoloredSprite>(
							0.0f, 360.0f));

			// si no ponemos tiempo de expiracion las particulas solo se retiran
			// cuando llegan
			// al máximo
			particulasPuntero[i]
					.addParticleInitializer(new ExpireParticleInitializer<UncoloredSprite>(
							0.4f));

			particulasPuntero[i]
					.addParticleModifier(new ScaleParticleModifier<UncoloredSprite>(
							0, 0.4f, 0.5f, 0.5f));
			particulasPuntero[i]
					.addParticleModifier(new ColorParticleModifier<UncoloredSprite>(
							0.0f, 0.4f, 1, 1, 0.5f, 1, 0, 1));
			// particulas[i].addParticleModifier(new
			// AlphaParticleModifier<Sprite>(0, 0.2f, 0, 1 ));
			// particulas[i].addParticleModifier(new
			// AlphaParticleModifier<Sprite>(0.5f,1 , 1, 0));
			// no queremos que estén activados desde el principio
			particulasPuntero[i].setParticlesSpawnEnabled(false);

		}
	}

	/**
	 * Sacado del ejemplo de AE demostrando MouseJoint
	 */
	private MouseJoint createMouseJoint(final IEntity entidad,
			final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		final Body body = ((ObjetoFisico<?>) entidad.getUserData()).getCuerpo();
		final MouseJointDef mouseJointDef = new MouseJointDef();

		final float[] coordsEscena = entidad
				.convertLocalCoordinatesToSceneCoordinates(pTouchAreaLocalX,
						pTouchAreaLocalY);
		final Vector2 localPoint = Vector2Pool.obtain(
				(coordsEscena[0] - (entidad.getWidth() * entidad
						.getOffsetCenterX()))
						/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
				(coordsEscena[1] - (entidad.getHeight() * entidad
						.getOffsetCenterY()))
						/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);

		/*
		 * realmente el MouseJoint solo usa un cuerpo(el bodyB) pero es
		 * obligatorio suministrarle otro por lo que tradicionalmente se usa el
		 * del suelo ( que suele estar siempre presente)
		 */
		mouseJointDef.bodyA = suelo;
		mouseJointDef.bodyB = body;
		mouseJointDef.dampingRatio = 0.00f;
		mouseJointDef.frequencyHz = 20f;
		mouseJointDef.maxForce = (200 * body.getMass() * 4);
		mouseJointDef.collideConnected = true;

		mouseJointDef.target.set(localPoint);
		Vector2Pool.recycle(localPoint);

		return (MouseJoint) mundo.createJoint(mouseJointDef);
	}

	public void comprobarLineas() {
		/*
		 * El motor (Box2d) no garantiza que el orden en el que se reportan las
		 * intersecciones sea el de proximidad al origen del rayo. Por lo tanto
		 * hay que ordenar en base al parametro fraction, que representa con un
		 * decimal de 0.0f a 1.0f en que fraccion de la distancia del origen al
		 * objetivo nos encontramos al colisionar.
		 */

		// TreeMap para ordenar las fixturas por su proximidad al origen según
		// insertamos
		final TreeMap<Float, Bloque> bloquesLinea = new TreeMap<Float, Bloque>();

		for (int linea = 0; linea < FILAS; linea++) {

			bloquesLinea.clear();
			Vector2 p1;
			Vector2 p2;
			// tiramos una linea que atraviese la caja sin tocar los muros
			p1 = Vector2Pool.obtain(
					0 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, (linea
							* tamaño_bloque + (tamaño_bloque / 2) + MARGENES)
							/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);

			p2 = Vector2Pool.obtain((camara.getWidth())
					/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, p1.y);

			/*
			 * Line lin = new
			 * Line(p1.x*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
			 * p1.y*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT
			 * ,p2.x*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT
			 * ,p2.y*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT ,vbom);
			 * 
			 * this.capaAlta.attachChild(lin); Log.d("RAYCAST","RAYO LINEA");
			 * //tiramos el rayo
			 */
			mundo.rayCast(new RayCastCallback() {

				@Override
				public float reportRayFixture(Fixture fixture, Vector2 point,
						Vector2 normal, float fraction) {
					// cada vez que encuentre una fixture
					// si no pertenece a un cuerpo estático(muro)

					// Log.d("RAYCAST","RAYO bloque " + bloquesLinea.size());
				
					if (!(fixture.getBody().getType() == BodyType.StaticBody)) {
						Vector2 velocidad = fixture.getBody()
								.getLinearVelocity();

						if (Math.abs(velocidad.x) < 0.2
								&& Math.abs(velocidad.y) < 0.2) {

							// y además se encuentra alineado con los ejes( con
							// un margen de 10 grados arriba o abajo)
							double diferencia = Math.abs(Math.toDegrees(fixture
									.getBody().getAngle()) % 90);
							if (diferencia < 5
									|| Math.abs(diferencia - 90) < 5) {
								// lo añadimos a la linea

								bloquesLinea.put(fraction,
										(Bloque) fixture.getUserData());

							}
						}

					}
					// continuamos hasta el final aunque hayamos encontrado algo

					return 1;
				}
			}, p1, p2);
			// si hemos encontrado COLUMNAS bloques alineados tenemos una línea
			// completa
/*
			float cont = 1.0f;
			for (Bloque b : bloquesLinea.values()) {

				b.getGrafico().setAlpha(cont);
				cont -= 0.08;
			}
			*/
			if (bloquesLinea.size() >= COLUMNAS
					&& onQuitarLinea(bloquesLinea.values())) {

				/*
				 * Por cada bloque en la linea
				 */

				// Las piezas que hemos tocado
				HashSet<IPieza> piezasTocadas = new HashSet<IPieza>();

				for (Bloque b : bloquesLinea.values()) {

					
					// si el evento dice que este bloque no se toca no lo
					// destruimos
					
					if (!onQuitarBloque(b))
						continue;
					Log.w("LINEA", "Quitando bloque:"+b );
					// saco la pieza y la añado a la colección
					IPieza pieza = (IPieza) b.getPadre();
					
					piezasTocadas.add(pieza);
				pieza.quitarBloque(b);
					

					if (pieza.getBloques().isEmpty()) {
						
						pieza.destruirPieza();
						piezasEscena.remove(pieza);
						Log.e("LINEA", "quitando de tocadas con bloques:" + pieza.getBloques().size());
						piezasTocadas.remove(pieza);

					}else{
						
						
					}
					
				}
				Log.e("LINEA", "piezas tocadas tiene:" + piezasTocadas.size());
				for (IPieza tocada : piezasTocadas) {
					Log.e("LINEA", "Delenlazando:" + tocada);
					for (IPieza p : tocada.Desenlazar()) {
						
						p.registrarAreasTactiles(this);
						p.registrarGraficos(this.capaBaja);
						piezasEscena.add(p);

					}

				}

			}

			Vector2Pool.recycle(p1);
			Vector2Pool.recycle(p2);

		}
	}

	// *************
	// EVENTOS
	// *************

	@Override
	public void onAccelerationAccuracyChanged(
			final AccelerationData pAccelerationData) {

	}

	@Override
	public void onAccelerationChanged(final AccelerationData pAccelerationData) {
		final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX(),
				pAccelerationData.getY());
		mundo.setGravity(gravity);
		Vector2Pool.recycle(gravity);
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			final ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {

		if (pSceneTouchEvent.isActionDown()
				&& pSceneTouchEvent.getPointerID() < MAX_MULTITOQUE) {
			final IEntity entity = (IEntity) pTouchArea;
			if (joints[pSceneTouchEvent.getPointerID()] == null) {
				final Bloque bloque = (Bloque) entity.getUserData();
				final IPieza pieza = (IPieza) bloque.getPadre();

				//entity.attachChild(particulasPuntero[pSceneTouchEvent
				//		.getPointerID()]);
				joints[pSceneTouchEvent.getPointerID()] = this
						.createMouseJoint(entity, pTouchAreaLocalX,
								pTouchAreaLocalY);
				Log.e("COGIENDO PIEZA", ""+pieza);
			}

			return false;
		}
		return false;

	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {

		if (pSceneTouchEvent.getPointerID() == 0) {

			// degradadoFondo.setGradientAngle( (float) (180/ Math.PI *
			// Math.atan2(degradadoFondo.getX() - pSceneTouchEvent.getX(),
			// pSceneTouchEvent.getY() - degradadoFondo.getY())));
			degradadoFondo.setGradientVector(
					pSceneTouchEvent.getX() - camara.getWidth() / 2,
					pSceneTouchEvent.getY() - camara.getHeight() / 2);
			// degradadoFondo.setGradientVector(pSceneTouchEvent.getX(),
			// pSceneTouchEvent.getY());
		}
		if (this.mundo != null
				&& pSceneTouchEvent.getPointerID() < MAX_MULTITOQUE) {

			switch (pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_DOWN:
				//particulasPuntero[pSceneTouchEvent.getPointerID()]
				//		.setParticlesSpawnEnabled(true);
				// ((BaseParticleEmitter)
				// particulas[pSceneTouchEvent.getPointerID()].getParticleEmitter()).setCenter(pSceneTouchEvent.getX(),
				// pSceneTouchEvent.getY());
				return true;
			case TouchEvent.ACTION_MOVE:

				// ((BaseParticleEmitter)
				// particulas[pSceneTouchEvent.getPointerID()].getParticleEmitter()).setCenter(pSceneTouchEvent.getX(),
				// pSceneTouchEvent.getY());
				if (joints[pSceneTouchEvent.getPointerID()] != null) {
					final Vector2 vec = Vector2Pool
							.obtain(pSceneTouchEvent.getX()
									/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
									pSceneTouchEvent.getY()
											/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
					joints[pSceneTouchEvent.getPointerID()].setTarget(vec);
					// joints[pSceneTouchEvent.getPointerID()].getBodyB().applyAngularImpulse(4.5f);

					Vector2Pool.recycle(vec);
				}
				return true;
			case TouchEvent.ACTION_UP:
				if (joints[pSceneTouchEvent.getPointerID()] != null  &&  joints[pSceneTouchEvent.getPointerID()].getBodyA().isActive() ) {

					
					IPieza pieza = (IPieza) joints[pSceneTouchEvent.getPointerID()].getBodyB().getUserData();
					Log.e("SOLTANDO PIEZA", ""+pieza);
					/*
					 * !!
					 * 
					 * Básicamente ocurre que al destruir un cuerpo también se
					 * destruyen sus enlaces(joints) pero cómo la extensión de
					 * box2d no es mucho mas que unos bindings cutres no
					 * gestiona los objetos nativos de la manera deseable(
					 * invlidándolos también)
					 * 
					 * en resumen si se acaba de destruir una pieza mientras
					 * estaba sujeta, al soltar el dedo estoy intentando
					 * destruir un enlace(joint) que solo existe en el lado
					 * Java. Por lo tanto cuando las llamadas JNI hacen su magia
					 * intentan acceder a un puntero inválido y dan sigsev
					 * (segmentation fault) a nivel de libc.
					 * 
					 * Probablemente intentando hacer un doble free() (dado que
					 * la memoria de la estructura enlace ya fue liberada cuando
					 * se destruyó el cuerpo.
					 * 
					 * //confirmado
					 */
					
					if (joints[pSceneTouchEvent.getPointerID()].isActive()) {
						mundo.destroyJoint(joints[pSceneTouchEvent
								.getPointerID()]);
					}
					joints[pSceneTouchEvent.getPointerID()] = null;
					//particulasPuntero[pSceneTouchEvent.getPointerID()]
				//			.detachSelf();
				}
				//particulasPuntero[pSceneTouchEvent.getPointerID()]
				//		.setParticlesSpawnEnabled(false);
				return true;
			}
			return false;
		}
		return false;

	}

	// ha pasado el tiempo de un timer
	@Override
	public void onTimePassed(final TimerHandler pTimerHandler) {

		if (pTimerHandler == timerLinea) {

			motor.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					comprobarLineas();
					pTimerHandler.reset();

				}
			});

		}
		if (pTimerHandler == timerPieza) {

			motor.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					onPonerPieza();
					pTimerHandler.reset();

				}
			});

		}

	}

	@Override
	public boolean onIniciarPartida() {

		Log.w("FLUJO", "PARTIDA INICIANDO PARTIDA");
		return true;
	}

	@Override
	public void onPartidaIniciada() {
		Log.w("FLUJO", "PARTIDA INICIADA");
	}

	@Override
	public boolean onFinalizarPartida(boolean ganado) {

		return true;
	}

	@Override
	public void onPartidaFinalizada() {

	}

	@Override
	public boolean onQuitarLinea(Collection<Bloque> bloques) {
		/*
		
		//si entramos en el tiempo de lineas consecutivas
		if(motor.getSecondsElapsedTotal() -  tiempoUltimaLinea < MAX_TIEMPOLINEA){
			lineasConsecutivas ++;
			
			
			
		}else{
			lineasConsecutivas =0;
		}
		
		puntuacion=  puntuacion +  PUNTOS_LINEA * (MULTIPLICADOR_LINEA * lineasConsecutivas==0?1:lineasConsecutivas);
		managerRecursos.sonidoLinea.play();
		motor.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				cartelPuntos.setText(Integer.toString(puntuacion));
				
			}
		});
		
		
		tiempoUltimaLinea= motor.getSecondsElapsedTotal();
		
		*/
		return true;
	}

	public void onLineaQuitada() {
	};

	@Override
	public boolean onQuitarBloque(final Bloque bloque) {

		// creamos un sistema de partículas para cada bloque que se elimina

		PointParticleEmitter pe = new PointParticleEmitter(
				tamaño_bloque * 0.5f, tamaño_bloque * 0.5f);

		// ponemos comom color el color del bloque que se esta quitando

		// el 100 es necesario apra que empiece a emitir inmediatamente aunque
		// solo halla un sprite
		BatchedSpriteParticleSystem ps = new BatchedSpriteParticleSystem(pe, 1,
				100, 1, managerRecursos.trBloques.getTextureRegion(bloque
						.getGrafico().getCurrentTileIndex()), vbom);

		ps.addParticleInitializer(new BlendFunctionParticleInitializer<UncoloredSprite>(
				GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));

		// si no ponemos tiempo de expiracion las particulas solo se retiran
		// cuando llegan
		// al máximo
		ps.addParticleInitializer(new ExpireParticleInitializer<UncoloredSprite>(
				1.3f));

		// desgraciadamente no se puede especificar el tamaño absoluto de cada
		// sprite
		// por lo tanto hay que sacar la proporción entre el tamaño del sprite
		// del bloque y el tamaño original de la textura
		// dado que el parametro scale del sprite no se ajusta si asignamos
		// directamente el tamaño(que es el caso)
		// y por lo tanto es 1.0f permanentemente
		ps.addParticleModifier(new ScaleParticleModifier<UncoloredSprite>(0,
				1.0f, 0.5f, bloque.getGrafico().getHeight()
						/ managerRecursos.trBloques.getHeight() * 2));
		ps.addParticleModifier(new AlphaParticleModifier<UncoloredSprite>(0,
				1.0f, 1, 0));
		ps.addParticleModifier(new RotationParticleModifier<UncoloredSprite>(0,
				1.6f, 0, 360));

		final float[] coords = new float[2];
		bloque.getGrafico().getSceneCenterCoordinates(coords);
		pe.setCenterX(coords[0]);

		pe.setCenterY(coords[1]);

		this.capaAlta.attachChild(ps);
		ps.setParticlesSpawnEnabled(true);

		// la añadimos un modificador de retraso (haha)
		// cuando termina, se autodesengancha y se queda pendiente de GC
		// lo ideal sería reutilizar los objetos, pero va a haber muy pocos
		ps.registerEntityModifier(new DelayModifier(1.3f,
				new IEntityModifier.IEntityModifierListener() {

					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier,
							IEntity pItem) {
					}

					@Override
					public void onModifierFinished(
							IModifier<IEntity> pModifier, final IEntity pItem) {
						motor.runOnUpdateThread(new Runnable() {

							@Override
							public void run() {
								// pasado el tiempo nos desenganchamos de la
								// escena
								((BatchedSpriteParticleSystem) pItem)
										.detachSelf();

							}
						});

					}
				}));
		return true;

	}

	public void onPonerPieza() {
motor.runOnUpdateThread( new Runnable() {
	
	@Override
	public void run() {
		cartelPuntos.setText(Integer.toString(puntuacion));
		
	}
});
		
		puntuacion++;
		IPieza pieza = new PiezaPalo(mundo,
				camara.getWidth() / 2, camara.getHeight() * 2f, tamaño_bloque,
				IPieza.FIXTUREDEF_DEFECTO, PiezaBase.BODYDEF_DEFECTO);
		pieza.registrarAreasTactiles(this);
		pieza.registrarGraficos(this.capaBaja);

		piezasEscena.add(pieza);
		// contabilizamos los bloques que hay en escena
		float bloques = 0;
		for (IPieza p : piezasEscena) {
			bloques += p.getBloques().size();

		}

		if (bloques >= MAX_BLOQUES) {

			acabada = true;
			pausarEscena();

		} else if (bloques >= MAX_BLOQUES / 1.3) {

			managerRecursos.sonidoAlarma.play();
		}

		Log.d("PIEZA ", "PUESTA PIEZA : " + pieza.getTipo().toString());

	}

	@Override
	public void teclaMenuPresionada() {

		pausarEscena();

	}

	@Override
	public void teclaVolverPreionada() {
		pausarEscena();
	}

	@Override
	public void pausarEscena() {
		Log.w("FLUJO", "ESCENA JUEGO PAUSADA");
		if(	managerRecursos.musicaFondo.isPlaying())
			managerRecursos.musicaFondo.pause();
		guardarEstado();

		/*
		 * 
		 * Encargamos el abrir el menú a la siguiente actualización/fotograma
		 */

		motor.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {

				pausado = true;
				EscenaJuego.this.setChildScene(new EscenaMenu(camara, acabada,
						EscenaJuego.this), false, true, true);
			}
		}, false);

	}

	@Override
	public void reanudarEscena() {
		Log.w("FLUJO", "ESCENA JUEGO REANUDADA");
		
			
		
		motor.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				cargarEstado();

				pausado = false;

			}
		});

		// forzamos una actualización para que el estado se cargue
		// inmediatamente

		/*
		 * Esto es necesario porque cargar estado modifica la
		 */
		try {
			motor.onUpdate(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * 
		 * Si es el primer cargado
		 */
		if (primerCargado) {
			primerCargado = false;
			pausarEscena();
		}

	}

	@Override
	public void onEscenaPausada() {

	}

	@Override
	public void onEscenaReanudada() {

	}

	@Override
	public boolean isPartidaAcabada() {

		return acabada;
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {

		switch (pMenuItem.getID()) {

		case EscenaMenu.ID_CONTINUAR:
			pMenuScene.back(true);
			
			managerRecursos.musicaFondo.play();
			return false;
		case EscenaMenu.ID_MULTIJUGADOR:
			ActividadBluetooth
					.lanzar(ManagerRecursos.getInstancia().actividadJuego);
			return true;

		case EscenaMenu.ID_NUEVAPARTIDA:
			finalizarPartida(false);
			iniciarPartida();
			pMenuScene.back(true);
			
			managerRecursos.musicaFondo.seekTo(0);
			managerRecursos.musicaFondo.resume();
			return true;
		case EscenaMenu.ID_INSTRUCCIONES:
			ActividadBluetooth
					.lanzar(ManagerRecursos.getInstancia().actividadJuego);
			return true;
		case EscenaMenu.ID_SALIR:
			System.exit(0);
			return true;

		default:

		}

		return false;
	}

}
