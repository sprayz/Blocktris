package proyecto.blocktris.logica.fisica.piezas;

import java.util.ArrayList;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;


import proyecto.blocktris.logica.fisica.ObjetoFisico;
import proyecto.blocktris.recursos.ManagerRecursos;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public abstract class PiezaBase implements IPieza {
	
	
static	class Bloque extends ObjetoFisico{
		
		
		public static enum ColorBloque{
			AZUL,VERDE,GRIS,MORADO,ROJO,ARENA
			
		}
		
		public Bloque (PhysicsWorld  mundo , float tamaño_bloque,ColorBloque color ,FixtureDef fixturedef) {
			grafico = new TiledSprite(0,0, tamaño_bloque,tamaño_bloque, ManagerRecursos.getInstancia().trBloques.deepCopy(),ManagerRecursos.getInstancia().vbom );
			cuerpo  = PhysicsFactory.createBoxBody(mundo, grafico, BodyType.DynamicBody , fixturedef);
			
			((TiledSprite)grafico).setCurrentTileIndex(color.ordinal());
			mundo.registerPhysicsConnector(new PhysicsConnector(this.grafico ,this.cuerpo));
		}
	}
	
	
	
	
	
	private static FixtureDef fixturedef ;
	private static BodyDef bodydef ;
	private PIEZAS tipo;
	protected boolean modificada = false;
	protected ArrayList<Bloque> bloques = new ArrayList<Bloque>();
	

	public PiezaBase(PhysicsWorld mundo, float x,float y,float tamaño_bloque,FixtureDef fixturedef)	{}

}
