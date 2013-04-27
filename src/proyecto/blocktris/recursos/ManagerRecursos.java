package proyecto.blocktris.recursos;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;


public class ManagerRecursos
{


private static  ManagerRecursos INSTANCIA = null ;

public Engine motor;
public BaseGameActivity actividadJuego;
public Camera camara;
public VertexBufferObjectManager vbom;


// TEXTURAS  Y REGIONES



//Fuentes y Otros

public Font          fGlobal;

public void cargarRecursosGenerales(){
   //cargamos la fuente común
	FontFactory.setAssetBasePath("font/");
	fGlobal= FontFactory.createFromAsset(actividadJuego.getFontManager(), actividadJuego.getTextureManager(), 1024, 1024, actividadJuego.getAssets(),
		    "hyperdigital.ttf", 64, true, android.graphics.Color.WHITE);
	  fGlobal.load();
	  
	  
	
}



public static void prepararManager(Engine motor, BaseGameActivity actividadJuego, Camera camara, VertexBufferObjectManager vbom)
{
    getInstancia().motor = motor;
    getInstancia().actividadJuego = actividadJuego;
    getInstancia().camara = camara;
    getInstancia().vbom = vbom;
}

//---------------------------------------------
// GETTERS AND SETTERS
//---------------------------------------------

public static ManagerRecursos getInstancia()
{
	if(INSTANCIA == null){
		INSTANCIA = new ManagerRecursos();
	}
    return INSTANCIA;
}
}
