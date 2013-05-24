package proyecto.blocktris.logica.fisica.piezas.rompibles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		
		public float distanciaA(Bloque b){
			
			return   Math.abs(this.getX() - b.getX())  +   Math.abs(this.getY() - b.getY());
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
		
		//FML  no quiero empezar con problemas de pathfinding
		
		/*
		 * en principio  es un algoritmo A* muy simplificado por varios motivos:
		 * 
		 * 1.Cada bloque  guarda su posición absoluta en coordenadas
		 * 
		 * 2.Cada bloque  guarda una lista  de sus adjuntos
		 * 
		 * En este caso calcular el coste es tan facil cómo restar sus 
		 * coordenadas absolutas.
		 * 
		 * Y cómo los adjuntos están precomputados no hay  que  pegarse cón problemas de 
		 * obstáculos.
		 * 
		 * 
		 * 
		 */
		/**
		 * Esta funcion busca  si  hay un camino entre BloqueA y BloqueB.
		 * En caso negativo  puebla  ListaIsla con todos los bloques, alcanzables desde 
		 * BloqueA
		 * 
		 * 
		 * 
		 */
		
		
		
		
		public boolean caminoEntreBloques(Bloque inicio, Bloque  objetivo, Set<Bloque> alcanzablesDesdeInicio ){
			/* Aunque Bloque no implementa hashCode() ni equals() podemos usar  un HashSet
			 * aceptando que solo diferenciará entre referencias al mismo objeto
			 * 
			 * Es decir, dos instancias de Bloque  con exactamente los mismos datos se considerarán 
			 * diferentes.Pero dos referencias al mismo Bloque (Adyacentes etc) se considerarán iguales.
			 * 
			 * Teniendo en cuenta que se usa la comparación de igualdad  para comprobar duplicados.
			 * es decir (IMPORTANTE!):
			 * 
			 *  1 A.equals(B)   significa que   A.hashCode() == B.hashcode()
			 *  2 !A.equals(B)  significa que  A.hashCode() != B.hashcode()
			 *  
			 *  Al no implementar Bloque ninguno de los dos métodos la distinción se hace
			 *  a nivel de Object.
			 * 
			 * 
			 */
			Set<Bloque> candidatos = new HashSet<Bloque>(); //candidatos a considerear 
			Set <Bloque> navegados = new HashSet<Bloque>(); // candidatos descartados(ya visitados)
			Bloque actual;		//Bloque  en el que estamos
			float puntuacionActual;
			float puntuacionCandidato;
			actual = inicio;
			
			
			
			candidatos.add(inicio);
			while(!candidatos.isEmpty()){
				/*
				 * en pathfinding real  llevaria la cuenta de la puntuación
				 * peor en esta caso  dados los pocos nodos a atravesar
				 * lo mas probable es que  simplemente alojar la memoria para llevar la puntuación 
				 * a parte  cueste más tiempo que  calcularla.
				 * 
				 * TODO: Revisar si el rendimiento es aceptable
				 */
				
				//sacamos el candidato  con la menor puntuación
				
				for (Bloque b: candidatos){
					
					puntuacionCandidato = b.distanciaA(objetivo);
					if(puntuacionCandidato < puntuacionActual){
						
						
					}
				}
				
				
				
				
				
				
				
			}
			if(alcanzablesDesdeInicio != null)
				alcanzablesDesdeInicio = navegados;
			
			
			
			return  false;
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
		
		
		float maxDimX= lista_bloques.get(0).getX();
		
		
		
		float minDimX= lista_bloques.get(0).getX();
		float maxDimY= lista_bloques.get(0).getY();
		float minDimY= lista_bloques.get(0).getY();
		
		for(Bloque b : lista_bloques){
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
	
		 float dimX =(maxDimX - minDimX);
		 float dimY=(maxDimY - minDimY);
		 
		 //TODO: reajustar el centro(anchor) del cuerpo
		 
		
		
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
	
	/**
	 * Quita el bloque de la pieza teniendo en cuenta las dependencias.
	 * Divide la pieza en varias si fuese necesario.
	 * @return Esta función retorna una lista de piezas que se divide la original 
	 */
	public ArrayList<IPieza> quitarBloqueDesenlazar(Bloque b){
		boolean division = true;
		
		//quitamos el bloque de los bloque adyacentes , de los contiguos a él mismo
		//y  comprobamos  si tienen algún bloque adyacente en común
		//si fiuese el caso no hace falta dividir la pieza
		//en caso contrario cada adyacente ( y  sus resèctivos adyacentes recursivamente)
		//representa una sección nueva de la pieza
		List<Bloque> comunes=new ArrayList<Bloque>();
		for (Bloque bloque : b.getAdjacentes()){
			
			//quitamos el bloque de los ad
			bloque.getAdjacentes().remove(b);
			comunes.retainAll(bloque.getAdjacentes());
			
			
			// si  tienen algúin bloque en común  entonces 
			// no hace falta dividir
			if ( !comunes.isEmpty()){
				division=false;
				break;
				
			}
				
			
			//creamos una lista con  los adyacentes del bloque
			comunes =  new ArrayList<Bloque>(bloque.getAdjacentes());
			//si la lista contiene 
			
		}
		
		
		// si division es verdadero
		if(division){
			for(Bloque  bloque_inicial : b.getAdjacentes()  ){
				
				
			}
			
				
				
		}
		
		
		return null;
	}
	
	
	public boolean quitarBloque(Bloque b){
		Bloque res= null;
		if(bloques.remove(b)){
		
			for(Bloque bloque : bloques){
				
				bloque.getAdjacentes().remove(b);	
			}
			b.destruir();
			return true;
		}
		return false;	
		
		
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
