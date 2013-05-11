package proyecto.blocktris.recursos;

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

public class ManagerRecursos {

	private static ManagerRecursos INSTANCIA = null;

	public Engine motor;
	public BaseGameActivity actividadJuego;
	public Camera camara;
	public VertexBufferObjectManager vbom;

	// TEXTURAS Y REGIONES
	private BitmapTextureAtlas  taBloques;
	public TiledTextureRegion trBloques;
	public TiledTextureRegion trBloquesSombra;
	public TiledTextureRegion trAnimBrillo;

	// Fuentes y Otros

	public Font fGlobal;

	
	public void cargarRecursosGenerales() {
		// cargamos la fuente común
		FontFactory.setAssetBasePath("font/");
		fGlobal = FontFactory.createFromAsset(actividadJuego.getFontManager(),
				actividadJuego.getTextureManager(), 1024, 1024,
				actividadJuego.getAssets(), "hyperdigital.ttf", 64, true,
				android.graphics.Color.WHITE);
		fGlobal.load();

		// cargamos los bloques
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/sprites/");
taBloques = new BitmapTextureAtlas(
				actividadJuego.getTextureManager(), 1024, 1024,TextureOptions.NEAREST_PREMULTIPLYALPHA);
			

		//Nuestro spritesheet tiene 6 columnas y 2 filas
		trBloques = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset( taBloques, actividadJuego,"blocks_2.png",0,0 ,6,2 );
		
	trAnimBrillo= BitmapTextureAtlasTextureRegionFactory
			.createTiledFromAsset(taBloques, actividadJuego,
					"clear.png",0,0 ,6, 2);
		taBloques.load();
	}

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
