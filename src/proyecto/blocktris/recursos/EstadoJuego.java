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
import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaBase;
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
 * Esta clase  es un contenedor de el estado del juego.
 * Hace lo posible por ser simple de serializar.
 * 
 * Usa Gson para serializarse.
 */
public class EstadoJuego {
		
	
	
	
	
	public boolean acabada;
	
	
	public int puntuacion;
	
	/** Estado de las piezas en escena */
	public ArrayList<EstadoPieza> piezas = new ArrayList<EstadoJuego.EstadoPieza>() ;
	
	
	
	/**
	 * Serializa el objeto a JSON.
	 * 
	 * @return Una cadena de JSON que representa el objeto.
	 * @throws SecurityException
	 *            
	 * @throws NoSuchFieldException
	 *          
	 * @throws ClassNotFoundException
	 *            
	 */
	public String serializarJSON() throws SecurityException, NoSuchFieldException, ClassNotFoundException{

		/*
		 * 
		 * Evitamos serializar el componente Shape de los FixtureDef que
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
	 * Deserializa el objeto a partir de una cadena de JSON.
	 * 
	 * @param json
	 *            String que contiene  el JSON que define un objeto EstadoJuego
	 * @return Un EstadoJuego nuevo creado a  partr de la cadena
	 */
	public static EstadoJuego deserializarJSON(String json){
		Gson gson = new Gson();
		return gson.fromJson(json, EstadoJuego.class);
		
		
	}
	
	
	
	
	
	
	
	
	
	 /**
	 * Esta clase representa  el estado de una pieza.
	 * 
	 * @see IPieza
	 * @see PiezaBase
	 */
 	public static class EstadoPieza {
		 
		 /**
		 * Esta clase representa  el estado de un bloque
		 * 
		 * @see PiezaBase.Bloque
		 */
 		public static class EstadoBloque  {
			
			
			public ColorBloque color;
			
	
			public float x;
			
			
			public float y;
			//para evitar referencias circulares al serializar a JSON
			//(los bloques adyacentes son referencias a otros bloques)
			// guardamos los adyacentes en forma de índices  dentro del mismo array
			public ArrayList<Integer> adyacentes = new ArrayList<Integer>();
			
			
		}


	
			
			
		//evitamos la instanciación
		private EstadoPieza (){};
		
		 
	
		public float tamaño_bloque;
		
		
		public BodyDef bodydef;
		
		
		public FixtureDef fixturedef;
		
		
		public PIEZAS tipo;
		
		
		public ArrayList<EstadoBloque> bloques; 
		
		
		
		
		/**
		 * Este método  guada el estado de una pieza.
		 * 
		 * @see EstadoJuego.EstadoPieza#desempaquetar
		 * 
		 * @param pieza
		 *            the pieza
		 * @return el estado
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
		 * Este método  crea una pieza a a partir de su estado guardado.
		 * 
		 * @see EstadoJuego.EstadoPieza#empaquetar
		 * 
		 * @param mundo
		 *            el  mundo done crear la pieza
		 *            
		 * @param estado
		 *            el estado guardado de la pieza
		 *            
		 * @return Una pieza nueva acorde al estado
		 */
		public static IPieza desempaquetar(PhysicsWorld mundo, EstadoPieza estado){
			IPieza pieza = null;
			
			
			pieza = new PiezaDesempaquetada(mundo, estado);
			
			
			
			return pieza;
		}
		
	}
	
	
	
	
}
