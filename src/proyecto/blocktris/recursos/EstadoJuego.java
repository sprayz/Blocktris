package proyecto.blocktris.recursos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

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
	public ArrayList<EstadoPieza> piezas ;
	
	 public static class EstadoPieza implements Serializable {
		 
		 public static class EstadoBloque{
			
			public ColorBloque color;
			public float x;
			public float y;
			public ArrayList<EstadoBloque> adyacentes;
			
			
		}
		 
		public float tamaño_bloque;
		public BodyDef bodydef;
		public FixtureDef fixturedef;
		public PIEZAS tipo;
		public ArrayList<EstadoBloque> bloques; 
		
		
		
		
		public static  EstadoPieza empaquetar(IPieza pieza ){
			EstadoPieza estado = new EstadoPieza();
			
			estado.bodydef =  Utilidades.bodyToDef(pieza.getCuerpo());
			estado.fixturedef =  Utilidades.fixtureToDef(pieza.getBloques().get(0).getFixtura());
			estado.tipo = pieza.getTipo();
			estado.tamaño_bloque = pieza.getBloques().get(0).getGrafico().getHeight();
			estado.bloques = new ArrayList<EstadoBloque>();
			
			for(Bloque b : pieza.getBloques()){
				EstadoBloque eb = new EstadoBloque();
				
				eb.color = b.getColor();
				eb.x = b.getX();
				eb.y = b.getY();
				eb.adyacentes = new ArrayList<EstadoJuego.EstadoPieza.EstadoBloque>();
				estado.bloques.add(eb);
				
				
			}
			
			int indice ;
			List<EstadoBloque> adyacentes_nuevos;
			for(int i = 0;i <pieza.getBloques().size();i++ )
					//por cada adyacente del antiguo
					for(Bloque adj : pieza.getBloques().get(i).getAdjacentes()){
					
						adyacentes_nuevos = estado.bloques.get(i).adyacentes;
						//añadimos los adyacentes del nuevo que tengan el mismo indice que los adyacentes del viejo
						indice = pieza.getBloques().indexOf(adj);
						if(indice <0)
							break;
						adyacentes_nuevos.add(estado.bloques.get(indice));
						
				
				
				
				
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
