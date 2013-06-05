package proyecto.blocktris.recursos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.andengine.extension.physics.box2d.PhysicsWorld;


import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.google.gson.Gson;

import proyecto.blocktris.logica.fisica.Utilidades;
import proyecto.blocktris.logica.fisica.piezas.IPieza;
import proyecto.blocktris.logica.fisica.piezas.IPieza.PIEZAS;
import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaDesempaquetada;
import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaBase.Bloque;
import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaBase.Bloque.ColorBloque;


/*
 * Esta clase contiene el estado de el juego, es decir
 *  		 --	informacin sobre todas las piezas  que se  encuentren en juego
 *  				 su posición  su estado(fuerzas) 
 *   		 
 *   		 -- Puntuación de la partida
 *   
 * 
 */


public class EstadoJuego implements Serializable {
		
	
	
	
	
	int puntuacion;
	public ArrayList<EstadoPieza> piezas = new ArrayList<EstadoJuego.EstadoPieza>() ;
	
	 public static class EstadoPieza implements Serializable {
		 
		 public static class EstadoBloque implements Serializable {
			
			public ColorBloque color;
			public float x;
			public float y;
			//para eveiutar referencias circulares al serializar a JSON
			// guardamos los adyacentes en forma de índices  destro del mismo array
			// en lugar de  referencias.
			public ArrayList<Integer> adyacentes = new ArrayList<Integer>();
			
			
		}


	
			
			
		private EstadoPieza (){};
		
		 
		public float tamaño_bloque;
		public BodyDef bodydef;
		public FixtureDef fixturedef;
		public PIEZAS tipo;
		public ArrayList<EstadoBloque> bloques; 
		
		
		
		
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
				for(Bloque ad: b.getAdjacentes()){
					estado.bloques.get(i).adyacentes.add(pieza.getBloques().indexOf(ad));
					
				}
				
				
			}
			
			
			
			
			return estado;
		}
		
		
		
		
		public static IPieza desempaquetar(PhysicsWorld mundo, EstadoPieza estado){
			IPieza pieza = null;
			
			
			pieza = new PiezaDesempaquetada(mundo, estado);
			
			
			
			return pieza;
		}
		
	}
	
	
	
	
}
