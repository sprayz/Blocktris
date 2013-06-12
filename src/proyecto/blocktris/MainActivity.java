/*
 *  @author Pablo Morillas Lozano
 */
package proyecto.blocktris;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.engine.splitscreen.DoubleSceneSplitScreenEngine;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.andengine.ui.activity.BaseGameActivity;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.view.KeyEvent;

import proyecto.blocktris.recursos.ManagerEscenas;
import proyecto.blocktris.recursos.ManagerEscenas.TipoEscena;
import proyecto.blocktris.recursos.ManagerRecursos;

// TODO: Auto-generated Javadoc
/**
 * La actividad principal del juego.
 * 
 * Esta actividad es el punto de entrada del juego y es donde  se cargan
 * e inicializan los recursos.
 * 
 * <p>
 * Sigue un  orden  definido por la superclase.
 * 
 * @see BaseGameActivity
 */
public class MainActivity extends BaseGameActivity{

	/** The recursos. */
	private ManagerRecursos recursos = ManagerRecursos.getInstancia();
	
	/** The escenas. */
	private ManagerEscenas  escenas = ManagerEscenas.getInstancia();
	
	/** The camara. */
	private Camera camara;
	
	/** The primer cargado. */
	private boolean primerCargado = true;
	
	/* (non-Javadoc)
	 * @see org.andengine.ui.activity.BaseGameActivity#onCreateEngine(org.andengine.engine.options.EngineOptions)
	 */
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) 
	{
		pEngineOptions.getRenderOptions().setDithering(true); //para el degradado del fondo
		pEngineOptions.getAudioOptions().setNeedsMusic(true); //para la música
		pEngineOptions.getAudioOptions().setNeedsSound(true);
	    return new Engine(pEngineOptions);
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see org.andengine.ui.activity.BaseGameActivity#onPauseGame()
	 */
	@Override
	public void onPauseGame()
	{ 
	    super.onPauseGame();
	    
	    if (this.isGameLoaded()){
	    	
	    	if (escenas.getEscenaActual() != null)
			{
	    		
	    		escenas.getEscenaActual().pausarEscena();
	    		
			}
	    }
	    
	    	
	    	
	        
	}

	
	/* (non-Javadoc)
	 * @see org.andengine.ui.activity.BaseGameActivity#onResumeGame()
	 */
	@Override
	public
	synchronized void onResumeGame()
	{
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
    
	    super.onResumeGame();
	    
	    
	    /*
		 * Aparentemente Android considera necesario llamar a onpause y onresume inmediatamente despues de 
		 *  bloquear la pantalla.
		 *  
		 *  Porque evidentemente cuando una actividad  pierde visibilidad significa que
		 *  
		 *   se vuelve INvisble, inmediatamente visible  e inmediatamente INvisible de nuevo.
		 *  
		 *  Extremadamente intuitivo.
		 */
	    
	    if (this.isGameLoaded() &&   pm.isScreenOn() ){
	    	
	    	if (escenas.getEscenaActual() != null)
			{
	    		
	    			escenas.getEscenaActual().reanudarEscena();
	    		
			}
	    }
	   
	}


	

	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyUp(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (escenas.getEscenaActual() != null)
		{
			
			if (keyCode == KeyEvent.KEYCODE_BACK)
		    {
		        escenas.getEscenaActual().teclaVolverPresionada();
		    }
			
			if (keyCode == KeyEvent.KEYCODE_MENU)
		    {
		        escenas.getEscenaActual().teclaMenuPresionada();
		    }
		
		
		}
		 //no queremos que el evento se propague
	
		    return true;
	}





	/* (non-Javadoc)
	 * @see org.andengine.ui.IGameInterface#onCreateEngineOptions()
	 */
	public EngineOptions onCreateEngineOptions()
	{
	
	camara = new Camera(0, 0,  480,800);// Pantalla fullHD? Mala suerte...
    EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(480,800), this.camara);
    // la RatioResolutionPolicy se encarga de añadir márgenes si fuese necesario para que  la escena  mantenga las proporciones.
   
    //necesitamos sonido, internamente inicializa el player
    engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
    engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON); // que no se apaque la pantalla cuando estamos  jugando.
    return engineOptions;
	}

	    /* (non-Javadoc)
    	 * @see org.andengine.ui.IGameInterface#onCreateResources(org.andengine.ui.IGameInterface.OnCreateResourcesCallback)
    	 */
    	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException
	    {
    			//rellenamos el manager de los recursos
    		
	    	    ManagerRecursos.prepararManager(getEngine(), this, camara, getVertexBufferObjectManager());
	    	
	    	    recursos = ManagerRecursos.getInstancia();
	    	    //inicializamos las texturas , sonidos y demás
	    	    recursos.cargarRecursosGenerales();
	    	    pOnCreateResourcesCallback.onCreateResourcesFinished();
	    }

	    /* (non-Javadoc)
    	 * @see org.andengine.ui.IGameInterface#onCreateScene(org.andengine.ui.IGameInterface.OnCreateSceneCallback)
    	 */
    	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException
	    {
	    
    		//creamos la escena principal
	    	escenas.crearEscenaJuego();
	    	
	        escenas.setEscena(TipoEscena.ESCENA_JUEGO );
	        pOnCreateSceneCallback.onCreateSceneFinished(escenas.getEscenaActual());
	    }

	    /* (non-Javadoc)
    	 * @see org.andengine.ui.IGameInterface#onPopulateScene(org.andengine.entity.scene.Scene, org.andengine.ui.IGameInterface.OnPopulateSceneCallback)
    	 */
    	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException
	    {
	        /*
	         * Generalmente usado por juegos donde la jugabilidad está  dividida en múltiples
	         * actividades. 
	         * 
	         * En cualquier caso es  más práctico  usar múltiples  ESCENAS porque cada 
	         * actividad nueva     inicializa su propio motor con sus recursos asociados
	         *  y esto lleva tiempo y es redundante.
	         */
    		
	    	pOnPopulateSceneCallback.onPopulateSceneFinished();
	    }
	    
	    
	    /* (non-Javadoc)
    	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
    	 */
    	@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		    switch(requestCode) {
		        case EscenaMenu.ACTIVIDAD_BLUETOOTH :
		          //TODO: recibir resultado de la actividad bluetooth  
		        	
		            break;
		        
		    }
		}
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	}
