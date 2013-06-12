/*
 *  @author Pablo Morillas Lozano
 */
package proyecto.blocktris.recursos;

import java.io.BufferedOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.andengine.extension.physics.box2d.PhysicsWorld;


import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import proyecto.blocktris.logica.fisica.Utilidades;
import proyecto.blocktris.logica.fisica.piezas.IPieza;
import proyecto.blocktris.logica.fisica.piezas.IPieza.PIEZAS;
import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaDesempaquetada;
import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaBase.Bloque;
import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaBase.Bloque.ColorBloque;


// TODO: Auto-generated Javadoc
/*
 * Esta clase contiene el estado de el juego, es decir
 *  		 --	informacin sobre todas las piezas  que se  encuentren en juego
 *  				 su posición  su estado(fuerzas) 
 *   		 
 *   		 -- Puntuación de la partida
 *   
 * 
 */


/**
 * The Class EstadoJuego.
 */
public class EstadoJuego {
		
	
	
	
	/** The acabada. */
	public boolean acabada;
	
	/** The puntuacion. */
	public int puntuacion;
	
	/** The piezas. */
	public ArrayList<EstadoPieza> piezas = new ArrayList<EstadoJuego.EstadoPieza>() ;
	
	
	
	/**
	 * Serializar json.
	 * 
	 * @return the string
	 * @throws SecurityException
	 *             the security exception
	 * @throws NoSuchFieldException
	 *             the no such field exception
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public String serializarJSON() throws SecurityException, NoSuchFieldException, ClassNotFoundException{

		/*
		 * 
		 * Evito serializar el componente Shape de los FixtureDef que
		 * contiene los vértices porque:  1.No es necesario para recrear la escena. 
		 * 2.Está
		 * diseñado para ser creado y accedido solo por las funciónes de la
		 * librería nativa. En consecuencia es poco más que una referencia a
		 * una dirección de memoria en el espacio de la librería.(a un array de vértices)
		 */
		Gson gson = new GsonBuilder().setExclusionStrategies(
				new ExclStrat(
						"com.badlogic.gdx.physics.box2d.FixtureDef.shape"))
				.create();
	

		return gson.toJson(this);
		
	}
	
	/**
	 * Deserializar json.
	 * 
	 * @param json
	 *            the json
	 * @return the estado juego
	 */
	public static EstadoJuego deserializarJSON(String json){
		Gson gson = new Gson();
		return gson.fromJson(json, EstadoJuego.class);
		
		
	}
	
	
	
	
	
	
	
	
	
	 /**
	 * The Class EstadoPieza.
	 */
 	public static class EstadoPieza implements Serializable {
		 
		 /**
		 * The Class EstadoBloque.
		 */
 		public static class EstadoBloque implements Serializable {
			
			/** The color. */
			public ColorBloque color;
			
			/** The x. */
			public float x;
			
			/** The y. */
			public float y;
			//para eveiutar referencias circulares al serializar a JSON
			// guardamos los adyacentes en forma de índices  destro del mismo array
			// en lugar de  referencias.
			/** The adyacentes. */
			public ArrayList<Integer> adyacentes = new ArrayList<Integer>();
			
			
		}


	
			
			
		/**
		 * Instantiates a new estado pieza.
		 */
		private EstadoPieza (){};
		
		 
		/** The tamaño_bloque. */
		public float tamaño_bloque;
		
		/** The bodydef. */
		public BodyDef bodydef;
		
		/** The fixturedef. */
		public FixtureDef fixturedef;
		
		/** The tipo. */
		public PIEZAS tipo;
		
		/** The bloques. */
		public ArrayList<EstadoBloque> bloques; 
		
		
		
		
		/**
		 * Empaquetar.
		 * 
		 * @param pieza
		 *            the pieza
		 * @return the estado pieza
		 */
		public static  EstadoPieza empaquetar(IPieza pieza ){
			EstadoPieza estado = new EstadoPieza();
			
			
			
			
			estado.bodydef =   Utilidades.bodyToDef(pieza.getCuerpo());
			estado.fixturedef =  Utilidades.fixtureToDef(pieza.getBloques().get(0).getFixtura());
			estado.tipo = pieza.getTipo();
			estado.tamaño_bloque = pieza.getBloques().get(0).getGrafico().getHeight();
			estado.bloques = new ArrayList<EstadoBloque>();
			
			for(Bloque b : pieza.getBloques()){
				EstadoBloque eb = new EstadoBloque();
				
				eb.color = b.getColor();
				eb.x = b.getX();
				eb.y = b.getY();
				estado.bloques.add(eb);
				
				
			}
			
			
		//por cada bloque
			
			
			for(int i = 0;i <pieza.getBloques().size();i++){
				Bloque b =  pieza.getBloques().get(i);
				
				// por cada adyacente de ese bloque
				for(Bloque ad: b.getAdyacentes()){
					estado.bloques.get(i).adyacentes.add(pieza.getBloques().indexOf(ad));
					
				}
				
				
			}
			
			
			
			
			return estado;
		}
		
		
		
		
		/**
		 * Desempaquetar.
		 * 
		 * @param mundo
		 *            the mundo
		 * @param estado
		 *            the estado
		 * @return the i pieza
		 */
		public static IPieza desempaquetar(PhysicsWorld mundo, EstadoPieza estado){
			IPieza pieza = null;
			
			
			pieza = new PiezaDesempaquetada(mundo, estado);
			
			
			
			return pieza;
		}
		
	}
	
	
	
	
}
