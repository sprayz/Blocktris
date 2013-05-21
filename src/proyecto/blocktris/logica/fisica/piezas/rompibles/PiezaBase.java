package proyecto.blocktris.logica.fisica.piezas.rompibles;

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

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

public abstract class PiezaBase implements IPieza {
	
	
public static	class Bloque extends ObjetoFisico{
		
		private Fixture fixture;
		public static enum ColorBloque{
			AZUL,VERDE,GRIS,MORADO,ROJO,ARENA
			
		}
		
		public Bloque (PhysicsWorld  mundo, Body cuerpo_base ,float x_relativo ,float y_relativo, float tamaño_bloque,ColorBloque color ,FixtureDef fixturedef) {
			this.cuerpo = cuerpo_base;
			grafico = new TiledSprite(0,0, tamaño_bloque,tamaño_bloque, ManagerRecursos.getInstancia().trBloques.deepCopy(),ManagerRecursos.getInstancia().vbom );
			fixturedef.shape = new PolygonShape().setAsBox(hx, hy, center, angle);
			
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
