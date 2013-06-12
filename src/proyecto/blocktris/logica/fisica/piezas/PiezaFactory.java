/*
 *  @author Pablo Morillas Lozano
 */
package proyecto.blocktris.logica.fisica.piezas;

import org.andengine.extension.physics.box2d.PhysicsWorld;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import proyecto.blocktris.logica.fisica.piezas.IPieza.PIEZAS;
import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaCuadrado;
import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaL1;
import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaL2;
import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaLlave1;
import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaLlave2;
import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaPalo;
import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaT;
/**
 * Fabrica para crear piezas
 * @see IPieza
 */
public class PiezaFactory {

	//evitamos la instanciación
	private PiezaFactory() {}
	
	/**
	 * Método para crear una pieza de tipo aleatorio
	 * 
	 * @param mundo
	 *            el mundo
	 * @param x
	 *            coordenada x
	 * @param y
	 *            coordenada y
	 * @param tamaño_bloque
	 *            tamaño del bloque
	 * @param fixturedef
	 *            propiedades del bloque
	 * @param bodydef
	 *            propiedades de la pieza
	 * @return Una pieza  aleatoria
	 */
	public static IPieza piezaAleatoria(PhysicsWorld mundo, float x,float y,float tamaño_bloque,FixtureDef fixturedef,BodyDef bodydef){
		
		return piezaPorTipo(PIEZAS.random(), mundo, x, y, tamaño_bloque, fixturedef,bodydef);
		
		
		
		
	}
	

	/**
	 * método para crear una pieza por su tipo.
	 * 
	 * @param tipo 
	 * 			  el tipo de la pieza  a crear
	 * 
	 * @param mundo
	 *            el mundo
	 * @param x
	 *            coordenada x
	 * @param y
	 *            coordenada y
	 * @param tamaño_bloque
	 *            tamaño del bloque
	 * @param fixturedef
	 *            propiedades del bloque
	 * @param bodydef
	 *            propiedades de la pieza
	 * @return Una pieza del tipo especificado
	 */
	public static IPieza piezaPorTipo(PIEZAS tipo,PhysicsWorld mundo, float x,float y,float tamaño_bloque,FixtureDef fixturedef,BodyDef bodydef){
		IPieza res = null;
		
		switch (tipo){
			case PIEZA_CUBO:
				res = new PiezaCuadrado(mundo, x, y, tamaño_bloque, fixturedef,bodydef);
			break;
			case PIEZA_L1:
				res = new PiezaL1(mundo, x, y, tamaño_bloque, fixturedef,bodydef);
			break;
			case PIEZA_L2:
				res = new PiezaL2(mundo, x, y, tamaño_bloque, fixturedef,bodydef);
			break;
			case PIEZA_LLAVE1:
				res = new PiezaLlave1(mundo, x, y, tamaño_bloque, fixturedef,bodydef);
			break;
			case PIEZA_LLAVE2:
				res = new PiezaLlave2(mundo, x, y, tamaño_bloque, fixturedef,bodydef);
			break;
			case PIEZA_PALO:
				res = new PiezaPalo(mundo, x, y, tamaño_bloque, fixturedef,bodydef);
			break;
			case PIEZA_T:
				res = new PiezaT(mundo, x, y, tamaño_bloque, fixturedef,bodydef);
			break;
	
		
		}
		
		
		
		return res;
		
		
		
	}
}
