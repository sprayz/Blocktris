package proyecto.blocktris.logica.fisica.piezas.enlazadas;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;


import proyecto.blocktris.logica.fisica.ObjetoFisico;
import proyecto.blocktris.logica.fisica.piezas.IPieza;
import proyecto.blocktris.logica.fisica.piezas.IPieza.PIEZAS;
import proyecto.blocktris.recursos.ManagerRecursos;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
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
			
			cuerpo.setUserData(this);
			grafico.setUserData(this);
			((TiledSprite)grafico).setCurrentTileIndex(color.ordinal());
			mundo.registerPhysicsConnector(new PhysicsConnector(this.grafico ,this.cuerpo));
		}
	}
	
	
	
	
	
	
	
	private PIEZAS tipo;
	protected boolean modificada = false;
	protected ArrayList<Bloque> bloques = new ArrayList<Bloque>();
	
	
	public void registrarGraficos(IEntity entidad){
		for(Bloque b: bloques){
			entidad.attachChild(b.getGrafico());	
		}
	}
	
	
	
	
	public void registrarAreasTactiles(Scene escena){
		
		for(Bloque b: bloques){
			escena.registerTouchArea(b.getGrafico());	
		}
	}
	public void desregistrarAreasTactiles(Scene escena){
		
		for(Bloque b: bloques){
			escena.unregisterTouchArea(b.getGrafico());	
		}
	}
	
	public void desregistrarGraficos(){
		for(Bloque b: bloques){
			b.getGrafico().detachSelf();	
		}
	}
	
	public PiezaBase(PhysicsWorld mundo, float x,float y,float tamaño_bloque,FixtureDef fixturedef)	{}

}
