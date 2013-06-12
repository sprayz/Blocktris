/*
 *  @author Pablo Morillas Lozano
 */
package proyecto.blocktris;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.TreeMap;

import org.andengine.engine.Engine.EngineLock;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
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
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.MultiTouchController;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.dialog.StringInputDialogBuilder;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.call.Callback;
import org.andengine.util.modifier.IModifier;

import proyecto.blocktris.logica.EscenaBase;
import proyecto.blocktris.logica.fisica.ObjetoFisico;
import proyecto.blocktris.logica.fisica.piezas.IPieza;
import proyecto.blocktris.logica.fisica.piezas.PiezaFactory;
import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaBase;
import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaBase.Bloque;
import proyecto.blocktris.recursos.BDPuntuaciones;
import proyecto.blocktris.recursos.EstadoJuego;
import proyecto.blocktris.recursos.EstadoJuego.EstadoPieza;
import proyecto.blocktris.recursos.ExclStrat;
import proyecto.blocktris.recursos.ManagerRecursos;
import proyecto.blocktris.recursos.Puntuacion;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.hardware.SensorManager;
import android.opengl.GLES20;
import android.util.Log;

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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/**
 * Escena principal del juego.
 * 
 * 
 * @author Pablo Morillas Lozano
 *
 */
public class EscenaJuego extends EscenaBase implements 
		IOnSceneTouchListener, IOnAreaTouchListener, ITimerCallback,
		 IOnMenuItemClickListener {

	/** el archivo en el alamacenamiento privado donde guardamos el estado. */
	private static final String ARCHIVO_ESTADO = "estado.dat";
	
	/** Márgen inferior.
	 * No combiene que los usuarion con dedos de morcilla  
	 * presionen por error la linea inferior de botones :-p. */
	private static float MARGENES = 20;
	
	/** Angulo  máximo  que puede tener un bloque respecto a los ejes para tomarlo en cuenta
	 *  en el escaneo de líneas */
	private static float ANGULO_MAX = 10;
	
	/** Las propiedades de los muros(densidad,elasticidad,fricción).
	 * @see FixtureDef */
	private static FixtureDef fdef_muro = PhysicsFactory.createFixtureDef(1.0f,
			0, 0.5f);
	
	/** El suelo. */
	private Body suelo;
	
	/** El techo. */
	private Body techo;
	
	/** La pared_izquierda. */
	private Body pared_izquierda;
	
	/** La pared_derecha. */
	private Body pared_derecha;
	
	/** El mundo. */
	PhysicsWorld mundo;
	
	/** El tamaño_bloque. */
	float tamaño_bloque;

	/** el número de filas. */
	private static final int FILAS = 10;
	
	/** el número de columnas. */
	private static final int COLUMNAS = 8;
	
	/** La cantidad de bloques  máximos en el mundo. */
	private static final int MAX_BLOQUES = FILAS * COLUMNAS;
	
	/** El máximo numero de  punteros que tratamos. */
	private static final int MAX_MULTITOQUE = 8;
	
	/** A partir de estos bloques se alerta al usuario de que  se está 
	 * llenando la escena y que se acerca el final(MUAHAHAHA! :D) de la partida. */
	private static final int LIMITE_BLOQUES_ALARMA =(int) (MAX_BLOQUES / 1.3);
	
	/** Los puntos que da cada línea */
	private static final int PUNTOS_LINEA = 10;
	
	/** Cada línea consecutiva aplica este multiplicador a sus puntos. */
	private static final int MULTIPLICADOR_LINEA =2 ;
	
	/** El intervalo de  tiempo  en el que   las líneas se consideran consecutivas. */
	private static  float MAX_TIEMPOLINEA = 5;
	
	/**  El intervalo de  tiempo  entre  pieza y pieza . */
	private static final float intervaloPonerPieza = 5f;
	
	/** El intervalo entre escaneos buscando líneas. */
	private static final float intervaloComprobarLinea = 1f;

	/** Velocidad  máxima  que puede llevar un bloque para tomarlo en cuenta
	 *  en el escaneo de líneas. */
	protected static final float VELOCIDAD_MAX = 0.2f;
	
	/** La capa gráfica baja.*/
	private Entity capaBaja;
	
	/** La capa gráfica alta.*/
	private Entity capaAlta;
	
	/** Los sitemas de partículas para cada puntero */
	private BatchedSpriteParticleSystem[] particulasPuntero;
	
	/** Los enlaces  de ratón  para cada puntero (para coger las piezas con el dedo) */
	private MouseJoint[] joints;
	
	/** El degradado de fondo */
	private Gradient degradadoFondo;
	
	/** El objeto de fondo. */
	private Background fondo;

	/** El  contador del intervalo  de piezas. */
	private TimerHandler timerPieza;
	
	/** El  contador del intervalo  de  el escaneo de líneas */
	private TimerHandler timerLinea;
	/*
	 * ESTADO DEL JUEGO
	 */
	/** El tiempo ( en  segundos, relativo al inicio del motor)  en el que 
	 * se registró la última línea. */
	private  float  tiempoUltimaLinea;
	
	/** Las líneas consecutivas que llevamos */
	private int  lineasConsecutivas;
	
	/** Bandera para determinar si se acabó la pertida. */
	private boolean acabada = false;
	
	/** Contenedor para serializar el estado del juego.  */
	private EstadoJuego estadoGuardado;
	
	/** Piezas que se encuentran actualmente en la escena. */
	private ArrayList<IPieza> piezasEscena;

	/** El cartel de la puntuación */
	private Text cartelPuntos;
	
	/** Bander apara determinar si es la primera vez que estamos cargando. */
	private boolean primerCargado = true;
	
	
	/** Puntuación  de la partida actual */
	private int puntuacion;

	/* (non-Javadoc)
	 * @see proyecto.blocktris.logica.EscenaBase#crearEscena()
	 */
	@Override
	public void crearEscena() {
		/*
		 * Inicialización
		 */

		
		piezasEscena = new ArrayList<IPieza>();
		estadoGuardado = new EstadoJuego();
		
		//enlaces para el "ratón" 
		joints = new MouseJoint[MAX_MULTITOQUE];

		
		timerLinea = new TimerHandler(intervaloComprobarLinea, false, this);
		timerPieza = new TimerHandler(intervaloPonerPieza, false, this);
		// FONDO

		degradadoFondo = new Gradient(camara.getWidth() / 2,
				camara.getHeight() / 2, camara.getWidth(), camara.getHeight(),
				vbom);
		degradadoFondo.setFromColor(0.0f, 0.5f, 1.0f);
		degradadoFondo.setToColor(1, 1, 1);
		//  el degradado se ajusta a las dimensiones del contenedor  aunque 
		//modifiquemos suparámetros
		degradadoFondo.setGradientFitToBounds(true);
	
		
		fondo = new EntityBackground(degradadoFondo);

		this.setBackgroundEnabled(true);
		this.setBackground(fondo);

		
		
		//Texto de los puntos
		cartelPuntos = new Text(camara.getWidth()/2f,camara.getHeight()*(8f/9f), 
				managerRecursos.fGlobal, "0", "XXXXXXXXX".length(), new TextOptions(HorizontalAlign.CENTER ),vbom);
		
		//cartelPuntos.setHeight(camara.getHeight()/9f);
		// CAPAS
		capaBaja = new Entity();
		capaAlta = new Entity();
		this.attachChild(capaBaja);
		this.attachChild(capaAlta);

		
		capaAlta.attachChild(cartelPuntos);
		//semitransparencia
		cartelPuntos.setAlpha(0.8f);
		
		
		

		// dejamos cierta holgura para poder maniobrar mejor las piezas
		tamaño_bloque = camara.getWidth() / COLUMNAS;
		tamaño_bloque = tamaño_bloque - ((tamaño_bloque / 4f / COLUMNAS));

		
		inicializarSistemasParticulas();
		
		//añade  al Catlog los FPS cada 5 sec
		motor.registerUpdateHandler(new FPSLogger());

		// FISICA
		
		
		mundo = new PhysicsWorld( new Vector2(0,-SensorManager.GRAVITY_EARTH), // gravedad hacia abajo :-]
				true, //los cuerpos que alcancen estabilidad y no esten colisionando se pueden sacar de la simulación
				20, //iteraciones de velocidad
				16); //iteraciones de colisión
			/*Cuantas más iteraciones por actualización  más precisa es la simulación pero mas recursos lleva
			 * alcanzar  los mismos FPS
			 */
		
		
		// activamos el Multitoque
		motor.setTouchController(new MultiTouchController());
		setOnSceneTouchListener(this);
		setOnAreaTouchListener(this);
	
		
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

					
					// no colisionamos
					contact.setEnabled(false);

					/*
					 * la alternativa a este método es usar el vector normal, es
					 * decir la dirección en que el motor aplicaría la fuerza
					 * para compensar dos fixturas incrustadas el problema es
					 * que cambia de sentido a mitad de cada fixtura
					 * 
					 * y esto causa que se reactive la colisión y que el motor
					 * repela los dos cuerpos que , de repente ,están
					 * incrustados y colisionando. esto hace un efecto elástico
					 * que se incrementa cuantas mas fixturas tenga la pieza La
					 * consecuencia es que el cuerpo sufre una aceleración
					 * considerable al atraversar el techo.
					 * 
					 * Las piezas  caen cómo meteoritos y  siembran destrucción en cualquier estructura que 
					 * el jugador pudiera haber organizado
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
			public void postSolve(Contact contact, ContactImpulse impulse) {}

			@Override
			public void endContact(Contact contact) {}

			@Override
			public void beginContact(Contact contact) {}
		});
		
		this.registerUpdateHandler(timerLinea);
		this.registerUpdateHandler(timerPieza);
		registerUpdateHandler(mundo);
		
		
	}


	/**
	 * Este método serializa el estado del juego {@link proyecto.bloqcktris.recursos.EstadoJuego}
	 * a un archivo en formato JSON.
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
		
			BufferedOutputStream bos = new BufferedOutputStream(fileOut);

					
					
				

			bos.write(estadoGuardado.serializarJSON().getBytes());
			//Log.w("SERIALIZANDO", " GUARDANDO ESTADO");
			bos.flush();
			bos.close();
			fileOut.close();

			estadoGuardado = null;
		} catch (IOException f) {
			Log.e("SERIALIZADO", f.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cargar estado.
	 */
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
			
			estadoGuardado = EstadoJuego.deserializarJSON(resultado);

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
		 * 
		 */
		
		for (PhysicsConnector pc : mundo.getPhysicsConnectorManager()) {

			pc.onUpdate(0);

		}

	}

	/**
	 * Finaliza la partida actual.
	 */
	public void finalizarPartida() {
		if(managerRecursos.musicaFondo.isPlaying())
			managerRecursos.musicaFondo.stop();
		
		reiniciarEscena();
		this.unregisterUpdateHandler(timerLinea);
		this.unregisterUpdateHandler(timerPieza);
		puntuacion = 0;
		acabada = true;
		}

	/**
	 * Inicia  una nueva partida
	 */
	public void iniciarPartida() {
		managerRecursos.musicaFondo.play();
		acabada = false;
	
		this.registerUpdateHandler(timerLinea);
		this.registerUpdateHandler(timerPieza);

	
	}

	/* (non-Javadoc)
	 * @see proyecto.blocktris.logica.EscenaBase#reiniciarEscena()
	 */
	@Override
	public void reiniciarEscena() {
		for (IPieza p : piezasEscena) {
			p.destruirPieza();
		}
		System.gc();
		

	}

	/* (non-Javadoc)
	 * @see proyecto.blocktris.logica.EscenaBase#deshacerEscena()
	 */
	@Override
	public void deshacerEscena() {
	

	}

	// *******
	// UTILIDAD
	// *******

	/**
	 * Esta función inicializa un sistema de partículas por cada puntero
	 * posible.
	 */
	private void inicializarSistemasParticulas() {
		particulasPuntero = new BatchedSpriteParticleSystem[MAX_MULTITOQUE];

		for (int i = 0; i < particulasPuntero.length; i++) {

			//queremos que  el rectángulo sea ligeramente más pequeño para que  las partículas  sobresalgan
			//demasiado de  el bloque
			IParticleEmitter pe = new RectangleOutlineParticleEmitter(
					tamaño_bloque / 2, tamaño_bloque / 2, tamaño_bloque * 0.9f,
					tamaño_bloque * 0.9f);

			particulasPuntero[i] = new BatchedSpriteParticleSystem(pe, 10,
					20, 4, managerRecursos.trAnimBrillo.getTextureRegion(1),
					vbom);

			// efectos para cada partícula
			particulasPuntero[i]
					.addParticleInitializer(new ColorParticleInitializer<UncoloredSprite>(
							1, 1, 0));
			particulasPuntero[i]
					.addParticleInitializer(new BlendFunctionParticleInitializer<UncoloredSprite>(
							GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
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
			// no queremos que estén activados desde el principio
			particulasPuntero[i].setParticlesSpawnEnabled(false);

		}
	}

	/**
	 * Sacado del ejemplo de AE demostrando MouseJoint.
	 * 
	 * Esta función crea  un {@link MouseJoint}.
	 * 
	 * @param entidad
	 *            the entidad
	 * @param pTouchAreaLocalX
	 *            the touch area local x
	 * @param pTouchAreaLocalY
	 *            the touch area local y
	 * @return the mouse joint
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
		mouseJointDef.collideConnected = true; //si desactivamos la colision las piezas atraviesarán el suelo :-)

		mouseJointDef.target.set(localPoint);
		Vector2Pool.recycle(localPoint);

		return (MouseJoint) mundo.createJoint(mouseJointDef);
	}

	
	
	
	/**
	 * Destruye los cuerpos de las piezas de la escena que estén marcados inactivos
	 * ( no siendo simulados).
	 * 
	 * Este método se ha de ejecutar en un {@link IUpdateHandler}.
	 * NO , repito , NO  es posible ejecutarse dentro de un Runnable en el 
	 * hilo de actualización  sin causar problemas de sincronización en la extensión box2d
	 * y errores en la librería nativa
	 * 
	 */
	private void purgarPiezas(){	
		
				for(ListIterator<IPieza> pi = piezasEscena.listIterator();
						pi.hasNext();){
					
					IPieza p = pi.next();
					
					if(!p.getCuerpo().isActive() ){
						mundo.destroyBody(p.getCuerpo());
						pi.remove();
					}
					
				}
			
		
	}
	
	
	/**
	 * Este método  escanea la escena  buscando y tratando bloques alineados.
	 * 
	 * Este método realiza cambios en el estado de la escena y debería, por lo tanto,
	 * ejecutarse en el hilo de actualización.
	 * 
	 * @see BaseGameActivity#runOnUpdateThread(Runnable, boolean)
	 */
	public void comprobarLineas() {
		/*
		 * El motor físico (Box2d) no garantiza que el orden en el que se reportan las
		 * intersecciones sea el de proximidad al origen del rayo. Por lo tanto
		 * hay que ordenar en base al parametro fraction, que representa con un
		 * decimal de 0.0f a 1.0f en que fraccion de la distancia del origen al
		 * objetivo nos encontramos al colisionar.
		 */

		// TreeMap para ordenar las fixturas por su proximidad al origen según
		// insertamos
		final TreeMap<Float, Bloque> bloquesLinea = new TreeMap<Float, Bloque>();
		HashSet<IPieza> piezasTocadas = new HashSet<IPieza>();
		for (int linea = 0; linea < FILAS; linea++) {
piezasTocadas.clear();
			bloquesLinea.clear();
			
			Vector2 p1; //punto inicial
			Vector2 p2; //punto final
			
			
			p1 = Vector2Pool.obtain(
					0 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, (linea
							* tamaño_bloque + (tamaño_bloque / 2) + MARGENES)
							/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);

			p2 = Vector2Pool.obtain((camara.getWidth())
					/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, p1.y);

			//le pedimos al mundo físico que tire un rayo de p1 a p2  y que nos reporte  las intersecciones
			mundo.rayCast(new RayCastCallback() {

				@Override
				public float reportRayFixture(Fixture fixture, Vector2 point,
						Vector2 normal, float fraction) {
					// cada vez que encuentre una fixture
					// si no pertenece a un cuerpo estático(muro)
					if (!(fixture.getBody().getType() == BodyType.StaticBody)) {
						Vector2 velocidad = fixture.getBody()
								.getLinearVelocity();

					/*
					 * Dado que la estabilidad absoluta es ridículamente dificil de alcanzar
					 * por el método  a base de aproximación que usa el motor fisico.
					 * 
					 * Fijamos una valor  de una quietud aceptable.
					 * 
					 */
						if (Math.abs(velocidad.x) < VELOCIDAD_MAX
								&& Math.abs(velocidad.y) <VELOCIDAD_MAX) {

							// y además se encuentra alineado con los ejes( con
							// un margen de ANGULO_MAX grados arriba o abajo)
							double diferencia = Math.abs(Math.toDegrees(fixture
									.getBody().getAngle()) % 90);
							if (diferencia < ANGULO_MAX
									|| Math.abs(diferencia - 90) < ANGULO_MAX) {
								// lo añadimos a la linea

								bloquesLinea.put(fraction,
										(Bloque) fixture.getUserData());

							}
						}

					}
					// continuamos hasta el final aunque hayamos encontrado algo

					return -1;
				}
			}, p1, p2);
			// si hemos encontrado COLUMNAS bloques alineados tenemos una línea
			// completa

			
			if (bloquesLinea.size() >= COLUMNAS
					&& onQuitarLinea(bloquesLinea.values())) {


				
				
				//por cada bloque ne la línea
				for (Bloque b : bloquesLinea.values()) {

					
					// si el evento dice que este bloque no se toca no lo
					// destruimos
					
					if (!onQuitarBloque(b))
						continue;
					
					// sacamos la pieza y la añadimos a la colección
					IPieza pieza = (IPieza) b.getPadre();
					
					piezasTocadas.add(pieza);
					pieza.quitarBloque(b);
					

					
					
				}
				
				//por cada pieza que hayamos tocado al destruir los bloques de la línea
				for (IPieza tocada : piezasTocadas) {
					//si  no tiene bloques
					if (tocada.getBloques().isEmpty()) {
						//la destruimos (MUAHAHAHAHA :P)
						tocada.destruirPieza();	
					}else{
						//si le quedan bloques  cabe la posibilidad que hayamos  dividido la pieza
						//al quitar bloques
						
						// por cada pieza resultante de la posible división
						for (IPieza p : tocada.Desenlazar()) {
							// la añadimos a la escena
							p.registrarAreasTactiles(this);
							p.registrarGraficos(this.capaBaja);
							piezasEscena.add(p);
	
						}
								
					}
				}

			}
			/*
			 * Devolvemos los vectores a la reserva
			 * 
			 * En  otras aplicaciones más intensivas  en el uso de box2d
			 * la creación  y consiguiente deshecho de vectores  provoca pausas fecuentes por el recolector de basura
			 * Vector2Pool soluciona esto  creando una "piscina" global de vectores que se reciclan.
			 */
			Vector2Pool.recycle(p1);
			Vector2Pool.recycle(p2);

		}
	}

	// *************
	// EVENTOS
	// *************

	

	/* (non-Javadoc)
	 * @see org.andengine.entity.scene.IOnAreaTouchListener#onAreaTouched(org.andengine.input.touch.TouchEvent, org.andengine.entity.scene.ITouchArea, float, float)
	 */
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			final ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {

		/*
		 * Una de las áreas táctiles que registramos   ha sido tocada :-d
		 */
		
		//si estamos dentro de el rango de multitoque que controlamos
		if (pSceneTouchEvent.isActionDown()
				&& pSceneTouchEvent.getPointerID() < MAX_MULTITOQUE){
			final IEntity entity = (IEntity) pTouchArea;
			
			//si este puntero no tiene asignado ya un enlace/articulación
			if (joints[pSceneTouchEvent.getPointerID()] == null) {
				final Bloque bloque = (Bloque) entity.getUserData();
				//final IPieza pieza = (IPieza) bloque.getPadre();

				// particulas bonitas en el  bloque que hemos agarrado
				entity.attachChild(particulasPuntero[pSceneTouchEvent
						.getPointerID()]);
				particulasPuntero[pSceneTouchEvent
									.getPointerID()].setParticlesSpawnEnabled(true);
				//creamos un enlace con el raton (MouseJoint) 
				joints[pSceneTouchEvent.getPointerID()] = this
						.createMouseJoint(entity, pTouchAreaLocalX,
								pTouchAreaLocalY);
				
			}

			return true;
		}
		return false;

	}

	/* (non-Javadoc)
	 * @see org.andengine.entity.scene.IOnSceneTouchListener#onSceneTouchEvent(org.andengine.entity.scene.Scene, org.andengine.input.touch.TouchEvent)
	 */
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {

		/*
		 * Hemos recibido un evento de toque  en general
		 */
		
		/*
		 * Si es el primer puntero...
		 * 
		 * NOTA: andengine hace esencialmente lo que le da la gana con los punteros.
		 * se puede confiar en  distinguir unos de otros pero  le cuesta mucho y a menudo falla
		 *  en  mantener la realcion entre el ID y el orden de toque.
		 * 
		 * A menudo el primer dedo se convierte en el segunod o ternceor o quinto...
		 * 
		 */
		if (pSceneTouchEvent.getPointerID() == 0) {

			// rotamos el degradado respecto al centro de coordenadas de la escena y al puntero
			
			//fórmula alternativa
			// degradadoFondo.setGradientAngle( (float) (180/ Math.PI *
			// Math.atan2(degradadoFondo.getX() - pSceneTouchEvent.getX(),
			// pSceneTouchEvent.getY() - degradadoFondo.getY())));
			
			degradadoFondo.setGradientVector(
					pSceneTouchEvent.getX() - camara.getWidth() / 2,
					pSceneTouchEvent.getY() - camara.getHeight() / 2);
			
		}
		// si no hemos   tocado antes de inicializar la escena
		if (this.mundo != null
				&& pSceneTouchEvent.getPointerID() < MAX_MULTITOQUE) {

			switch (pSceneTouchEvent.getAction()) {
			case TouchEvent.ACTION_DOWN:
				//debug
				
				Log.d("MOUSE", "MOUSE DOWN");
				
				//pasamos el evento
				return false;
			case TouchEvent.ACTION_MOVE:

				//si estamos arrastrando algo
				if (joints[pSceneTouchEvent.getPointerID()] != null) {
					final Vector2 vec = Vector2Pool
							.obtain(pSceneTouchEvent.getX()
									/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
									pSceneTouchEvent.getY()
											/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
					// ponemos el extremo de el enlace en  el puntero
					joints[pSceneTouchEvent.getPointerID()].setTarget(vec);
					

					Vector2Pool.recycle(vec);
				}
				return true;
			case TouchEvent.ACTION_UP:
				// Hemos levantado el dedo
				Log.d("MOUSE", "MOUSE UP");
				
				//nos aseguramos que lo que estamos tocando no ha sido destruido
				if (joints[pSceneTouchEvent.getPointerID()] != null  && joints[pSceneTouchEvent.getPointerID()].getBodyB() != null) {

					
					IPieza pieza = (IPieza) joints[pSceneTouchEvent.getPointerID()].getBodyB().getUserData();
					Log.e("SOLTANDO PIEZA", ""+pieza);
				
					if(pieza.getCuerpo().isActive() && !pieza.getCuerpo().getJointList().isEmpty()){
						
						mundo.destroyJoint(joints[pSceneTouchEvent
													.getPointerID()]);
					}
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
					
				}
				
				joints[pSceneTouchEvent.getPointerID()] = null;
				particulasPuntero[pSceneTouchEvent.getPointerID()]
					.detachSelf();
				particulasPuntero[pSceneTouchEvent.getPointerID()]
						.setParticlesSpawnEnabled(false);
				
				return true;
			}
			return false;
		}
		return false;

	}

	// ha pasado el tiempo de un timer
	/* (non-Javadoc)
	 * @see org.andengine.engine.handler.timer.ITimerCallback#onTimePassed(org.andengine.engine.handler.timer.TimerHandler)
	 */
	@Override
	public void onTimePassed(final TimerHandler pTimerHandler) {

		
		if (pTimerHandler == timerLinea) {

			//esperamos a la siguiente actualización
			EngineLock lock = motor.getEngineLock();
			//bloqueamos el motor
			lock.lock();
					//hacemos nuestras cosas
					comprobarLineas();
					pTimerHandler.reset();

			//devolvemos la vida al motor
			lock.unlock();
			
		}
		if (pTimerHandler == timerPieza) {
			/*
			 * Reutilizamos el update handler de las piezas para
			 * destruir las piezas pendientes
			 */
			
					purgarPiezas();
			
			// encargamos al siguiente fotograma...
			motor.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					onPonerPieza();
					pTimerHandler.reset();

				}
			});

			
		}

	}

	


	
	/**
	 * On quitar linea.
	 * 
	 * @param bloques
	 *            los bloques
	 * @return true para continuar false para  no destruirla
	 */
	public boolean onQuitarLinea(Collection<Bloque> bloques) {
		Text puntos = new Text(camara.getWidth()/2,cartelPuntos.getY(),managerRecursos.fGlobal,"","XXXXXXXXX".length(), 
												new TextOptions(HorizontalAlign.CENTER),vbom);
		
		
		/*
		 * Para determinar la altura de la linea  hacemos la media de la altura de todos los bloques que vamos a quitar
		 */
		
		float mediaY=0f;
		for (Bloque b : bloques){
			
			mediaY = mediaY + b.getGrafico().getSceneCenterCoordinates()[1];
		}
		mediaY = mediaY/ bloques.size();
		puntos.setY(mediaY);
		puntos.setHeight(tamaño_bloque);
		//si entramos en el tiempo de lineas consecutivas
		if(motor.getSecondsElapsedTotal() -  tiempoUltimaLinea < MAX_TIEMPOLINEA){
			lineasConsecutivas ++;
			tiempoUltimaLinea = motor.getSecondsElapsedTotal();
			
		}else{
			lineasConsecutivas =1;
		}
		
		int psuma= PUNTOS_LINEA * MULTIPLICADOR_LINEA * lineasConsecutivas;
		puntuacion= puntuacion +   psuma;
		puntos.setText(Integer.toString(psuma));
		puntos.setScale(0.8f);
		puntos.setAlpha(0.8f);
		
		//ponemos  dos efectos ( transaparencia y desplazado vertical) simultáneos
		puntos.registerEntityModifier(new ParallelEntityModifier(new IEntityModifierListener() {
			
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				
				
			}
			
			//cuendo los dos efectos hayan acabado
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, final IEntity pItem) {
				motor.runOnUpdateThread(new Runnable() {
					
					@Override
					public void run() {
						// nos  autodestruimos
						pItem.detachSelf();
						
					}
				});
				
				
			}
		}, new AlphaModifier(3f,1.0f , 0.0f), new MoveYModifier(3f, puntos.getY(),puntos.getY() + tamaño_bloque * 3)));
		
		//campanita
		managerRecursos.sonidoLinea.play();
		
		
		this.capaAlta.attachChild(puntos);
		
		
		
		motor.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				cartelPuntos.setText(Integer.toString(puntuacion));
				
			}
		});
		
		
		tiempoUltimaLinea= motor.getSecondsElapsedTotal();
		
		
		return true;
	}

	/**
	 * On linea quitada.
	 */
	public void onLineaQuitada() {
	};


	/**
	 * On quitar bloque.
	 * 
	 * @param bloque
	 *            el bloque
	 * @return true para quitarlo o false para  no hacerlo
	 */
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

	/**
	 * On poner pieza.
	 */
	public void onPonerPieza() {
		
	motor.runSafely( new Runnable() {
	
	@Override
	public void run() {
		
		cartelPuntos.setText(Integer.toString(puntuacion));
		
	}
});
		
		puntuacion++;
		IPieza pieza = PiezaFactory.piezaAleatoria(mundo,
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

			//mostramos un diálogo pidiendo el nombre para añadirlo a puntuaciones
			final StringInputDialogBuilder dialogb = 
					new StringInputDialogBuilder(actividadJuego, 
												R.string.dialogo_titulo, 
												R.string.dialogo_mensaje,
												R.string.dialogo_mensaje, 
												R.drawable.ic_launcher,
												new Callback<String>() {

													@Override
													public void onCallback(
															String pCallbackValue) {
														//solo si el usuario escribe un nombre
														if(!pCallbackValue.equalsIgnoreCase("")){
															
															 BDPuntuaciones bd = new BDPuntuaciones(actividadJuego);
															 Puntuacion p = new Puntuacion();
															 p.setNombre(pCallbackValue);
															 p.setPuntos(puntuacion);
															 bd.addContact(p);
															 bd.close();
															ActividadPuntuaciones.lanzar(actividadJuego);
														}
													}
						
				},new OnCancelListener() {
					
					@Override
					public void onCancel(DialogInterface dialog) {
						
					
					}
				});
			
			actividadJuego.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					dialogb.create().show();
					
					
				}
			});

			
		} else if (bloques >= LIMITE_BLOQUES_ALARMA) {

			managerRecursos.sonidoAlarma.play();
		}

	}

	/* (non-Javadoc)
	 * @see proyecto.blocktris.logica.EscenaBase#teclaMenuPresionada()
	 */
	@Override
	public void teclaMenuPresionada() {
		if(!pausado)
			pausarEscena();

	}

	/* (non-Javadoc)
	 * @see proyecto.blocktris.logica.EscenaBase#teclaVolverPreionada()
	 */
	@Override
	public void teclaVolverPresionada() {
		if(!pausado)
			pausarEscena();
	}

	/* (non-Javadoc)
	 * @see proyecto.blocktris.logica.EscenaBase#pausarEscena()
	 */
	@Override
	public void pausarEscena() {
		// parar el sonido
		if(	managerRecursos.musicaFondo.isPlaying())
			managerRecursos.musicaFondo.pause();

		//Obtenemos el lock de el motor para  poder ejecutar el guardado inmediatamente
		// no podemos  encargarlo al siguiente fotograma
		// porque en caso de, por ejemplo ser una pausa externa ( apagar la pantalla o boton home)
		// no  habrá siguiente fotograma
		EngineLock lock = motor.getEngineLock();
		lock.lock();
		
		purgarPiezas();
		guardarEstado();
		lock.unlock();
		/*
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

	/* (non-Javadoc)
	 * @see proyecto.blocktris.logica.EscenaBase#reanudarEscena()
	 */
	@Override
	public void reanudarEscena() {
		motor.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				cargarEstado();

				pausado = false;

			}
		});

		// forzamos una actualización para que el estado se cargue
		// inmediatamente

		
		try {
			motor.onUpdate(1);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		/*
		 * Si es el primer cargado sacamos el menú
		 */
		if (primerCargado) {
			primerCargado = false;
			pausarEscena();
		}

	}

	
	
	
	public boolean isPartidaAcabada() {

		return acabada;
	}

	/* (non-Javadoc)
	 * @see org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener#onMenuItemClicked(org.andengine.entity.scene.menu.MenuScene, org.andengine.entity.scene.menu.item.IMenuItem, float, float)
	 */
	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {

		switch (pMenuItem.getID()) {

		case EscenaMenu.ID_CONTINUAR:
			pMenuScene.back(true);
			pausado = false;
			managerRecursos.musicaFondo.play();
			return false;
		case EscenaMenu.ID_PUNTUACIONES:
			ActividadPuntuaciones
					.lanzar(ManagerRecursos.getInstancia().actividadJuego);
			return true;

		case EscenaMenu.ID_NUEVAPARTIDA:
			finalizarPartida();
			
			
			
			
			
			
			iniciarPartida();
			pMenuScene.back(true);
			pausado = false;
			
			managerRecursos.musicaFondo.seekTo(0);
			managerRecursos.musicaFondo.resume();
			return true;
		case EscenaMenu.ID_INSTRUCCIONES:
			ActividadBluetooth
					.lanzar(ManagerRecursos.getInstancia().actividadJuego);
			return true;
		case EscenaMenu.ID_SALIR:
			System.exit(0); //muerte y destrucción
			return true;

		default:

		}

		return false;
	}

}
