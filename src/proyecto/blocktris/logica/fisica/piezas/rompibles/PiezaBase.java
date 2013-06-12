/*
 *  @author Pablo Morillas Lozano
 */
package proyecto.blocktris.logica.fisica.piezas.rompibles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import proyecto.blocktris.logica.fisica.ObjetoFisico;
import proyecto.blocktris.logica.fisica.Utilidades;
import proyecto.blocktris.logica.fisica.piezas.IPieza;
import proyecto.blocktris.recursos.ManagerRecursos;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

// TODO: Auto-generated Javadoc
/**
 * The Class PiezaBase.
 */
public  class PiezaBase implements IPieza {
	
	/** The bodydef defecto. */
	public static  BodyDef BODYDEF_DEFECTO;
	static{
		BODYDEF_DEFECTO = new BodyDef();
		
		BODYDEF_DEFECTO.active = true;
		BODYDEF_DEFECTO.allowSleep = true;
		BODYDEF_DEFECTO.angularDamping= 5f;
		BODYDEF_DEFECTO.awake= true;
		BODYDEF_DEFECTO.bullet = true;
		BODYDEF_DEFECTO.fixedRotation= false;
		BODYDEF_DEFECTO.linearDamping = 3f;
		BODYDEF_DEFECTO.type= BodyType.DynamicBody;
	}
	
	
	
/**
 * The Class Bloque.
 */
public static	class Bloque extends ObjetoFisico<AnimatedSprite>{
		
		/** Los bloques  a los que está unido */
		private ArrayList<Bloque> adjacentes;
		
		/** Las propiedades */
		private Fixture fixtura;
		
		/** La coordenada X  en unidades relativas al propio tamaño */
		private float x;
		
		/** La coordenada Y  en unidades relativas al propio tamaño */
		private float y;
		






		/** El color del grafico. */
		private ColorBloque color;
		
		public ColorBloque getColor() {
			return color;
		}

	
		public void setColor(ColorBloque color) {
			this.color = color;
			grafico.setCurrentTileIndex(color.ordinal());
		}

		/**
		 * Enumeracion de los colores
		 */
		public static enum ColorBloque implements Serializable{
			
			
			AZUL,

VERDE,

GRIS,

MORADO,

ROJO,

ARENA
			
		}
		
		public float getX() {
			return x;
		}

		
		public float getY() {
			return y;
		}
		
		/**
		 * Instantiates a new bloque.
		 * 
		 * @param mundo
		 *            el mundo
		 * @param cuerpo_base
		 *            el cuerpo al que pertenece
		 * @param x
		 *            la x en unidades relativas al propio tamaño
		 * @param y
		 *           la y en unidades relativas al propio tamaño
		 * @param tamaño_bloque
		 *            el tamaño del bloque en unidades gráficas
		 * @param color
		 *            el color
		 * @param fixturedef
		 *            las propiedades
		 */
		public Bloque (PhysicsWorld  mundo, Body cuerpo_base ,float x,float y, float tamaño_bloque,ColorBloque color ,FixtureDef fixturedef) {
			this.adjacentes= new ArrayList<Bloque>();
			this.cuerpo = cuerpo_base;
			grafico = new AnimatedSprite(0,0, tamaño_bloque,tamaño_bloque, ManagerRecursos.getInstancia().trBloques.deepCopy(),ManagerRecursos.getInstancia().vbom );
			this.color = color;
			this.mundo=mundo;
			 float tamaño_bloque_fisico = tamaño_bloque / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
			 this.x=x;
			 this.y=y;
			
			 
			 
			 //coordenadas de el centro para la fixtura
			Vector2 centro ;
			centro =Vector2Pool.obtain((x*tamaño_bloque_fisico) + tamaño_bloque_fisico /2 ,
					(y* tamaño_bloque_fisico) +tamaño_bloque_fisico /2  );

			
			//creamos el polígono que nos define (caja)
			fixturedef.shape = new PolygonShape();
			((PolygonShape) fixturedef.shape).setAsBox(tamaño_bloque_fisico /2, tamaño_bloque_fisico /2, centro, 0f);
			
			
			//creamos la fixture 
			fixtura= this.cuerpo.createFixture(fixturedef);
			
			// la documentación dice que el Shape se  clona  al crear la fixture  y que deberíamos deshacernos de él tras su uso
			fixturedef.shape.dispose();
			
		
			fixtura.setUserData(this);
			
			//movemos el eje de posicionamiento a la esquina inferior izquierda
			grafico.setAnchorCenter(0, 0);
			
		
			//colocamos el gráfico 
			grafico.setPosition(x* tamaño_bloque, y*tamaño_bloque);
				Vector2Pool.recycle(centro);
				
			grafico.setUserData(this);
			((TiledSprite)grafico).setCurrentTileIndex(color.ordinal());
		
			
			
		}

		/**
		 * Devuelve los bloques adyacentes inmediatos.
		 * 
		 * @see Bloque#getAdyacentesRecursivo()
		 * 
		 * @return el conjunto de clos bloques adyacentes inmediatos
		 */
		public ArrayList<Bloque> getAdyacentes() {
			return adjacentes;
		}
		
		
		
		/* (non-Javadoc)
		 * @see proyecto.blocktris.logica.fisica.ObjetoFisico#destruir()
		 */
		@Override
		public void destruir() {
			
			cuerpo.destroyFixture(fixtura);
			
			grafico.detachSelf();
			
		}

		
		public Fixture getFixtura() {
			return fixtura;
		}
		
		
	
	
		
		
		/**
		 * Devuelve todos los bloques adyacentes de manera recursiva
		 * @see Bloque#getAdyacentes()
		 * 
		 * @return  boques adyacentes recursivos
		 */
		public Set<Bloque> getAdyacentesRecursivo(){
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
			 * 
			 */
			
			
			
		Deque<Bloque> candidatos = new LinkedList<Bloque>(); //candidatos a considerear 
			
			Set <Bloque> navegados = new HashSet<Bloque>(); // candidatos descartados(ya visitados)
			
		
			Bloque actual;		//Bloque  en el que estamos

			
		  	
			candidatos.addFirst(this);
			
		
			/*
			 * podemos usar poll() poruq no se va a dar el caso de elementos nulos
			 */
			//por cada candidato
			while( !( (actual= candidatos.pollLast()) ==null  )  ){
				//sacamos sus adyacentes
				for( Bloque b : actual.getAdyacentes()){
					//por cada adyacente , l añadimos a los navegados y a los candidatos
					//para comprobar y tiene hijos que navegar
					
					
					if(navegados.contains(b)==false){
						candidatos.add(b);		
					}
					navegados.add(b);
				}	
				
			}
			return navegados;
	}
	
	
	
	
}
	
	/* (non-Javadoc)
	 * @see proyecto.blocktris.logica.fisica.piezas.IPieza#getTipo()
	 */
	public PIEZAS getTipo() {
	return tipo;
}

/* (non-Javadoc)
 * @see proyecto.blocktris.logica.fisica.piezas.IPieza#getBloques()
 */
public List<Bloque> getBloques() {
	return bloques;
}







	/** El tipo. */
	protected PIEZAS tipo;
	
	/** Si se le han quitado bloques. */
	protected boolean modificada = false;
	
	/** Los bloques. */
	protected ArrayList<Bloque> bloques = new ArrayList<Bloque>();
	
	/** El cuerpo. */
	protected Body cuerpo;
	
	/** La escena. */
	protected Scene escena;
	
	/** El tamaño de bloque. */
	protected float tamaño_bloque;
	
	/** El conector. */
	protected PhysicsConnector conector;
	
	/** El mundo. */
	protected PhysicsWorld mundo;
	
	/** Entidad contenedora para los gráficos contenedor. */
	protected IEntity contenedor = new Entity();
	
	/** Si esta pieza ha sido destruida. */
	protected boolean destruida = false;
	
	

	
	public IEntity getContenedor() {
		return contenedor;
	}
	
	
	public Scene getEscena() {
		return escena;
	}

	
	public boolean isModificada() {
		return modificada;
	}

	/* (non-Javadoc)
	 * @see proyecto.blocktris.logica.fisica.piezas.IPieza#getCuerpo()
	 */
	public Body getCuerpo() {
		return cuerpo;
	}

	
	@SuppressWarnings("unused")
	private PiezaBase(){}
	
	/**
	 * Crea una pieza a partir  de un conjunto de bloques.
	 * 
	 * @param mundo
	 *            el mundo
	 * @param lista_bloques
	 *            los bloques
	 */
	private PiezaBase(PhysicsWorld mundo,List<Bloque> lista_bloques){
		ArrayList<Bloque> bloques_nuevos = new ArrayList<Bloque>();
		BodyDef bdef;
		FixtureDef fdef;
		Body cuerpo_antiguo = lista_bloques.get(0).getCuerpo();
		
		
		
		
		
		//sacamos la definicion del cuerpo anterior
		bdef = Utilidades.bodyToDef(cuerpo_antiguo);
	
		//y creamos uno nuevo idéntico
		this.cuerpo = mundo.createBody(bdef);
		
		
		this.cuerpo.setUserData(this);
		for(Bloque b : lista_bloques){
			fdef = Utilidades.fixtureToDef(b.getFixtura());
			Bloque nuevo = new Bloque(mundo,this.cuerpo,b.getX(),b.getY(),b.getGrafico().getWidth(),b.getColor(),fdef);
			nuevo.setPadre(this);
			bloques.add(nuevo);
			contenedor.attachChild( nuevo.getGrafico());
			
		}
		List<Bloque> adyacentes_nuevos;
		int indice ;
		//para cada bloque
		for(int i=0;i< lista_bloques.size();i++){
			//por cada adyacente del antiguo
			for(Bloque adj : lista_bloques.get(i).getAdyacentes()){
			
				adyacentes_nuevos = bloques.get(i).getAdyacentes();
				//añadimos los adyacentes del nuevo que tengan el mismo indice que los adyacentes del viejo
				indice = lista_bloques.indexOf(adj);
				if(indice <0)
					break;
				adyacentes_nuevos.add(bloques.get(indice));
				
		}
			
			
		}
		this.mundo = mundo;
		this.conector = new PhysicsConnector(contenedor, cuerpo);
		
		 mundo.registerPhysicsConnector(conector);
	
	}
	
	/**
	 * Quita el bloque de la pieza teniendo en cuenta las dependencias.
	 * Divide la pieza en varias si fuese necesario.
	 * @return Esta función retorna un ArrayList de piezas en las  que se divide la original 
	 */
	public List<IPieza> Desenlazar(){
		
	
		List<IPieza> resultado = new ArrayList<IPieza>();
		//matriz de matrices (una para cada pieza resultado
		ArrayList<Set<Bloque>> listaPieza = new ArrayList<Set<Bloque>>();
		ArrayList<Bloque> aSaltarse = new ArrayList<Bloque>();
		for(int i=0;i<bloques.size();i++){
			//representa todos los bloques a los que se puede llegar desde  uno
			//siguiendo los adyacentes.
			Set<Bloque> isla ; 
			
			
			Bloque b = bloques.get(i);
		
				if(b.getAdyacentes().isEmpty()){
					Set<Bloque> temp = new HashSet<Bloque>();
					temp.add(b);
					listaPieza.add(temp);
					continue;
				}
					//si ya está incluido en otro conjunto nos lo saltamos
				if(!aSaltarse.contains(b)){
					//en caso contrario sacamos todos sus adyacentes
					isla = b.getAdyacentesRecursivo();
					//si sus adyacentes contienen alguno de los adyacentes del bloque a quitar
					//siginifica que están en el mismo fragmento
					for(Bloque c : isla){
						if(bloques.contains(c)){
							aSaltarse.add(c);
						}			
					}
					//añadimos la "isla a la lista para mas tarde hacer una pieza con ella
					listaPieza.add(isla);
					
					
					
					
					
					
				}
				
	 		}
		
			

	 		for(Set<Bloque> bloques : listaPieza){
				if(bloques.size() == this.getBloques().size()){
					
					break;
				}
					
	 			resultado.add(separarBloques(new ArrayList<Bloque>(bloques)));
				
			}
	 			
					
					
			return resultado;
			}
			
			
			
			
		
 		
	
		

	
	
	/* (non-Javadoc)
	 * @see proyecto.blocktris.logica.fisica.piezas.IPieza#quitarBloque(proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaBase.Bloque)
	 */
	public void quitarBloque(Bloque b){
		modificada =  true;
		Bloque res= null;
		if(bloques.remove(b)){
		
			for(Bloque bloque : bloques){
				
				bloque.getAdyacentes().remove(b);	
			}
			
			b.destruir();
			if(this.escena != null)
				b.desregistrarAreaTactil(this.escena);
			
		}
		return;	
		
		
	}
	
	
	/* (non-Javadoc)
	 * @see proyecto.blocktris.logica.fisica.piezas.IPieza#separarBloques(java.util.List)
	 */
	public IPieza separarBloques(List<Bloque> list){
		
		
		if(list.size()== bloques.size())
			return this;
		
		
	
		IPieza res = new PiezaBase(this.bloques.get(0).getMundo(),list);
		
		for(Bloque b : list){
			quitarBloque(b);
		}
		return res;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/* (non-Javadoc)
	 * @see proyecto.blocktris.logica.fisica.piezas.IPieza#registrarGraficos(org.andengine.entity.IEntity)
	 */
	public void registrarGraficos(IEntity entidad){
		entidad.attachChild(contenedor);
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see proyecto.blocktris.logica.fisica.piezas.IPieza#registrarAreasTactiles(org.andengine.entity.scene.Scene)
	 */
	public void registrarAreasTactiles(Scene escena){
		this.escena= escena;
		for(Bloque b: bloques){
			b.registrarAreaTactil(escena);
			
		}
	}
	
	/* (non-Javadoc)
	 * @see proyecto.blocktris.logica.fisica.piezas.IPieza#desregistrarAreasTactiles(org.andengine.entity.scene.Scene)
	 */
	public void desregistrarAreasTactiles(Scene escena){
		this.escena = escena;
		for(Bloque b: bloques){
			b.desregistrarAreaTactil(escena);
		}
	}
	
	/* (non-Javadoc)
	 * @see proyecto.blocktris.logica.fisica.piezas.IPieza#desregistrarGraficos()
	 */
	public void desregistrarGraficos(){
		contenedor.detachSelf();
	}
	
	/**
	 * 
	 * 
	 * @param mundo
	 *            el mundo
	 * @param x
	 *            coordenada x
	 * @param y
	 *            coordenada y
	 * @param tamaño_bloque
	 *            el tamaño del bloque
	 * @param fixturedef
	 *            la propiedades de los bloques
	 * @param bodydef
	 *            las propiedades de la pieza
	 */
	public PiezaBase(PhysicsWorld mundo, float x,float y,float tamaño_bloque,FixtureDef fixturedef,BodyDef bodydef)	{}







	/* (non-Javadoc)
	 * @see proyecto.blocktris.logica.fisica.piezas.IPieza#destruirPieza()
	 */
	@Override
	public IPieza destruirPieza() {
		desregistrarAreasTactiles(escena);
		desregistrarGraficos();
		
		for(Bloque b : bloques ){
			b.destruir();
		}
		bloques.clear();
		
		
		destruida= true;
		this.cuerpo.setActive(false);
		
		mundo.unregisterPhysicsConnector(conector);
		//mundo.destroyBody(this.cuerpo);
		return this;
	}

	/* (non-Javadoc)
	 * @see proyecto.blocktris.logica.fisica.piezas.IPieza#isDestruida()
	 */
	public boolean isDestruida() {
		return destruida;
	}

	

	

}
