/*
 *  @author Pablo Morillas Lozano
 */
package proyecto.blocktris.recursos;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.music.MusicLibrary;
import org.andengine.audio.music.MusicManager;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;


/**
 * ESta clase gestiona el cargado y acceso a los recursos compartidos.
 * 
 * Es un  Singleton, es decir solo se permite una instancia de la misma.
 */
public class ManagerRecursos {

	
	private static ManagerRecursos INSTANCIA = null;
	public Engine motor;
	public BaseGameActivity actividadJuego;
	public Camera camara;
	public VertexBufferObjectManager vbom;

	//Música
	public Music musicaFondo;
	public Sound sonidoLinea;
	public Sound sonidoAlarma;
	
	// TEXTURAS Y REGIONES

	private BuildableBitmapTextureAtlas taBloques;
	public TiledTextureRegion trBloques;
	public TiledTextureRegion trBloquesSombra;
	public TiledTextureRegion trAnimBrillo;
	
	
	// Fuentes y Otros
	public Font fGlobal;

	/**
	 * Cargar recursos generales.
	 * 
	 * Este método carga los recursos comunes a toda la aplicación.
	 */
	public void cargarRecursosGenerales() {
		//cargamos  los sonidos
		try {
			SoundFactory.setAssetBasePath("snd/");
			sonidoLinea = SoundFactory.createSoundFromAsset(motor.getSoundManager(), actividadJuego, "sd_0.wav");
			sonidoLinea.setVolume(1.0f);
			sonidoAlarma = SoundFactory.createSoundFromAsset(motor.getSoundManager(), actividadJuego, "alarm.mp3");
			sonidoAlarma.setVolume(1.0f);
		} catch (IllegalStateException e1) {
			
			e1.printStackTrace();
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
	
		//cargamos la música
		MusicFactory.setAssetBasePath("snd/");
		try {
			
			musicaFondo = MusicFactory.createMusicFromAsset(motor.getMusicManager(), actividadJuego, "tgfcoder-FrozenJam-SeamlessLoop.mp3");
			musicaFondo.setLooping(true);
			musicaFondo.setVolume(0.2f);
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		// cargamos la fuente común
		FontFactory.setAssetBasePath("font/");
		fGlobal = FontFactory.createFromAsset(actividadJuego.getFontManager(),
				actividadJuego.getTextureManager(), 1024, 1024,
				actividadJuego.getAssets(), "hyperdigital.ttf", 64, true,
				android.graphics.Color.WHITE);
		fGlobal.load();

		// cargamos los bloques
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/sprites/");
		taBloques = new BuildableBitmapTextureAtlas(
				actividadJuego.getTextureManager(), 1024, 1024,
				TextureOptions.NEAREST_PREMULTIPLYALPHA);

		// Nuestro spritesheet tiene 6 columnas y 2 filas
		trBloques = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(taBloques, actividadJuego,
						"blocks_2.png", 6, 2);

		trAnimBrillo = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(taBloques, actividadJuego, "clear.png",
						8, 1);

		try {// Magia negra :-D
			/*
			 * Esto es para  cuadrar las regiones en la textura del  atlas.
			 * 
			 * 
			 */
			taBloques
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 1));
			taBloques.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	/**
	 * Asigna ciertos recursos compartidos para su facil acceso a través de la aplicación.
	 * 
	 * @param motor
	 *            el motor
	 * @param actividadJuego
	 *            la actividad inicial
	 * @param camara
	 *           la camara
	 * @param vbom
	 *            el vbom
	 */
	public static void prepararManager(Engine motor,
			BaseGameActivity actividadJuego, Camera camara,
			VertexBufferObjectManager vbom) {
		getInstancia().motor = motor;
		getInstancia().actividadJuego = actividadJuego;
		getInstancia().camara = camara;
		getInstancia().vbom = vbom;
	}

	// ---------------------------------------------
	// GETTERS AND SETTERS
	// ---------------------------------------------

	
	public static ManagerRecursos getInstancia() {
		if (INSTANCIA == null) {
			INSTANCIA = new ManagerRecursos();
		}
		return INSTANCIA;
	}
}
