package proyecto.blocktris.logica.fisica.piezas.rompibles;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;


import proyecto.blocktris.logica.fisica.ObjetoFisico;
import proyecto.blocktris.logica.fisica.Utilidades;
import proyecto.blocktris.logica.fisica.piezas.IPieza;
import proyecto.blocktris.logica.fisica.piezas.IPieza.PIEZAS;
import proyecto.blocktris.recursos.ManagerRecursos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

public  class PiezaBase implements IPieza {
	
	
public static	class Bloque extends ObjetoFisico{
		private ArrayList<Bloque> adjacentes;
		private Fixture fixtura;
		private float x;
		private float y;
		
		

		private ColorBloque color;
		
		public ColorBloque getColor() {
			return color;
		}

		public void setColor(ColorBloque color) {
			this.color = color;
		}

		public static enum ColorBloque{
			AZUL,VERDE,GRIS,MORADO,ROJO,ARENA
			
		}
		
		public float getX() {
			return x;
		}

		public float getY() {
			return y;
		}
		
		public Bloque (PhysicsWorld  mundo, Body cuerpo_base ,float x,float y, float tamaño_bloque,ColorBloque color ,FixtureDef fixturedef) {
			this.adjacentes= new ArrayList<Bloque>();
			this.cuerpo = cuerpo_base;
			grafico = new TiledSprite(0,0, tamaño_bloque,tamaño_bloque, ManagerRecursos.getInstancia().trBloques.deepCopy(),ManagerRecursos.getInstancia().vbom );
			this.color = color;
			this.mundo=mundo;
			 float tamaño_bloque_fisico = tamaño_bloque / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
			
			 this.x=x;
			 this.y=y;
			//NO TOCAR
			//Yo futuro, puede que creas que sabes lo que haces, TE EQUIVOCAS.
			Vector2 centro ;
			centro =Vector2Pool.obtain((x*tamaño_bloque_fisico) + tamaño_bloque_fisico /2 ,
					(y* tamaño_bloque_fisico) +tamaño_bloque_fisico /2  );

			fixturedef.shape = new PolygonShape();
			((PolygonShape) fixturedef.shape).setAsBox(tamaño_bloque_fisico /2, tamaño_bloque_fisico /2, centro, 0f);
			
			fixtura= this.cuerpo.createFixture(fixturedef);
			fixturedef.shape.dispose();
			fixtura.setUserData(this);
			grafico.setAnchorCenter(-x  ,
					-y);
				Vector2Pool.recycle(centro);
			grafico.setUserData(this);
			((TiledSprite)grafico).setCurrentTileIndex(color.ordinal());
			mundo.registerPhysicsConnector(new PhysicsConnector(this.grafico ,this.cuerpo));
		}

		public ArrayList<Bloque> getAdjacentes() {
			return adjacentes;
		}
		

		@Override
		public void destruir() {
			//cuerpo.setActive(false);
			cuerpo.destroyFixture(fixtura);
			grafico.detachSelf();
			
		}

		public Fixture getFixtura() {
			return fixtura;
		}
		
	}
	
	
	
	
	
	
	
	public PIEZAS getTipo() {
	return tipo;
}

public ArrayList<Bloque> getBloques() {
	return bloques;
}







	private PIEZAS tipo;
	protected boolean modificada = false;
	protected ArrayList<Bloque> bloques = new ArrayList<Bloque>();
	protected Body cuerpo;
	
	public boolean isModificada() {
		return modificada;
	}

	public Body getCuerpo() {
		return cuerpo;
	}

	private PiezaBase(){}
	
	private PiezaBase(PhysicsWorld mundo,List<Bloque> lista_bloques){
		ArrayList<Bloque> bloques_nuevos = new ArrayList<Bloque>();
		BodyDef bdef;
		FixtureDef fdef;
		Body cuerpo_antiguo = lista_bloques.get(0).getCuerpo();
	
		//Las figuras originales pueden ser  de tantos bloques cómo quieran
		//teneos que sacar la distancia entre  los bloques para la nueva 
		//para poder centrar el cuerpo y posicionar las fixturas adecuadamente.
		
		/*
		int maxDimX= bloques_antiguos.get(0).getX();
		
		
		
		int minDimX= bloques_antiguos.get(0).getX();
		int maxDimY= bloques_antiguos.get(0).getY();
		int minDimY= bloques_antiguos.get(0).getY();
		
		for(Bloque b : bloques_antiguos){
			//para las X
			if(b.getX() > maxDimX){
				maxDimX = b.getX();
			}
			if(b.getX() < minDimX){
				minDimX = b.getX();
			}
			
			//para las Y
			if(b.getY() > maxDimY){
				maxDimY = b.getY();
			} 
			if(b.getY() < minDimY){
				minDimY = b.getY();
			} 
			
			
		}
		
		//nuestra nueva pieza ocupa una cuadrícula virtual de (dimX+1) por (dimY+1) 
	
		 int dimX =(maxDimX - minDimX);
		 int dimY=(maxDimY - minDimY);
		 
		 */
		
		
		//sacamos la definicion del cuerpo anterior
		bdef = Utilidades.bodyToDef(cuerpo_antiguo);
	
		//y creamos uno nuevo idéntico
		this.cuerpo = mundo.createBody(bdef);
		
		
		
		for(Bloque b : lista_bloques){
			fdef = Utilidades.fixtureToDef(b.getFixtura());
			bloques.add(new Bloque(mundo,this.cuerpo,b.getX(),b.getY(),b.getGrafico().getWidth(),b.getColor(),fdef));
			
			
		}
		List<Bloque> adyacentes_nuevos;
		int indice ;
		//para cada bloque
		for(int i=0;i< lista_bloques.size();i++){
			//por cada adyacente del antiguo
			for(Bloque adj : lista_bloques.get(i).getAdjacentes()){
			
				adyacentes_nuevos = bloques.get(i).getAdjacentes();
				//añadimos los adyacentes del nuevo que tengan el mismo indice que los adyacentes del viejo
				indice = lista_bloques.indexOf(adj);
				if(indice <0)
					break;
				adyacentes_nuevos.add(bloques.get(indice));
				
		}
			
			
		}
	
	}
	
	
	
	
	public Bloque quitarBloque(Bloque b){
		Bloque res= null;
		b.destruir();
		bloques.remove(b);
		return res;
		
	}
	
	
	public IPieza separarBloques(List<Bloque> list){
		if(list.size()== bloques.size())
			return this;
		IPieza res = new PiezaBase(list.get(0).getMundo(),list);
		
		for(Bloque b : list){
			quitarBloque(b);
		}
		return res;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
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







	@Override
	public IPieza destruirPieza(List<Fixture> a_borrar) {
		
		
		
		return null;
	}

	

}
