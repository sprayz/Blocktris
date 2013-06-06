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

public class MainActivity extends BaseGameActivity{

	private ManagerRecursos recursos = ManagerRecursos.getInstancia();
	private ManagerEscenas  escenas = ManagerEscenas.getInstancia();
	private Camera camara;
	private boolean primerCargado = true;
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) 
	{
		pEngineOptions.getRenderOptions().setDithering(true); //para el degradado del fondo
	    return new Engine(pEngineOptions);
	}
	
	
	
	/*
	 * 
	 * Aparentemente Android considera necesario llamar a onpause y onresume inmediatamente despues de 
	 *  bloquear la pantalla.
	 *  
	 *  Porque evidentemente cuando una actividad  pierde visibilidad significa que
	 *  
	 *   se vuelve INvisble, inmediatamente visible  e inmediatamente INvisible de nuevo.
	 *  
	 *  Extremadamente intuitivo.
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

	@Override
	public
	synchronized void onResumeGame()
	{
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
    
	    super.onResumeGame();
	    if (this.isGameLoaded() &&   pm.isScreenOn() ){
	    	
	    	if (escenas.getEscenaActual() != null)
			{
	    		
	    			escenas.getEscenaActual().reanudarEscena();
	    		
			}
	    }
	   
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//super.onDestroy();
		 if (this.isGameLoaded()){
		        System.exit(0);    
		  }
	}


	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (escenas.getEscenaActual() != null)
		{
			
			if (keyCode == KeyEvent.KEYCODE_BACK)
		    {
		        escenas.getEscenaActual().teclaVolverPreionada();
		    }
			
			if (keyCode == KeyEvent.KEYCODE_MENU)
		    {
		        escenas.getEscenaActual().teclaMenuPresionada();
		    }
		
		
		}
		 //no queremos que el evento se propague
		    return true;
	}





	public EngineOptions onCreateEngineOptions()
	{
	
	camara = new Camera(0, 0,  320,480);
    EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(320,480), this.camara);
    engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
    engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
    return engineOptions;
	}

	    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException
	    {
	    	    ManagerRecursos.prepararManager(getEngine(), this, camara, getVertexBufferObjectManager());
	    	    recursos = ManagerRecursos.getInstancia();
	    	    recursos.cargarRecursosGenerales();
	    	    pOnCreateResourcesCallback.onCreateResourcesFinished();
	    }

	    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws IOException
	    {
	    	escenas.crearEscenaMenu();
	    	escenas.crearEscenaJuego();
	    	
	        escenas.setEscena(TipoEscena.ESCENA_JUEGO );
	        pOnCreateSceneCallback.onCreateSceneFinished(escenas.getEscenaActual());
	    }

	    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException
	    {
	        
	    	pOnPopulateSceneCallback.onPopulateSceneFinished();
	    }
	    
	    
	    @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		    switch(requestCode) {
		        case EscenaMenu.ACTIVIDAD_BLUETOOTH :
		            
		        	
		            break;
		        
		    }
		}
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	}
