/*
 *  @author Pablo Morillas Lozano
 */
package proyecto.blocktris.logica.fisica.piezas;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import java.io.Serializable;
import java.util.*;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.util.adt.array.ArrayUtils;



import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaBase;
import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaBase.Bloque;
import proyecto.blocktris.recursos.ManagerRecursos;


import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;


/**
 * La interfaz IPieza.
 */
public interface IPieza {

	/**
	 * Tipos de pieza
	 * @see PiezaBase#getTipo()
	 */
	public static enum PIEZAS implements Serializable{
		
	
		PIEZA_T,
		
		PIEZA_L1,
		
		
		PIEZA_L2,
		
		
		PIEZA_CUBO,
		
		
		PIEZA_PALO,
		
		
		PIEZA_LLAVE1,
		
	
		PIEZA_LLAVE2;
		
	

		 
  		private static final  List<PIEZAS> VALUES =
		    Collections.unmodifiableList(Arrays.asList(values()));
		  
  	
  		private static final int SIZE = VALUES.size();
		  
  		
  		private static final Random RANDOM = new Random();

		  /**
		 * Este método retorna un elemento aleatoio de la enumeración.
		 * 
		 * @return Un elemento aleatorio  de la enumeración.
		 */
  		public static PIEZAS random()  {
		    return VALUES.get(RANDOM.nextInt(SIZE));
	}
		  }

   
      
      /** Las propiedades por defecto de los bloques */
      public static FixtureDef FIXTUREDEF_DEFECTO=  PhysicsFactory.createFixtureDef(0.1f, 0.0f, 0.4f);
     
     /** Las propiedades por defecto de la pieza */
     public static BodyDef BODYDEF_DEFECTO= null;
     
     
      
      /**
		 * Registra los elementos gráficos de los bloques de la pieza.
		 * 
		 * @param entidad
		 *            la entidad en la que registralos.
		 */
      public void registrarGraficos(IEntity entidad);
      
      /**
		 * Desregistra os elementod g´raficos de la pieza.
		 */
      public void desregistrarGraficos();
      
      /**
		 * Destruye la pieza.
		 * 
		 * Destruye todos sus bloques  y marca  el cuerpo cómo inactivo.
		 * 
		 * @return la pieza destruida
		 */
      public IPieza destruirPieza();
      
      /**
		 * REgistra las áreas táctiles de los bloques en la escena.
		 * 
		 * @param escena
		 *            la escena
		 */
      public void registrarAreasTactiles(Scene escena);
      /**
		 *Desregistra las áreas táctiles de los bloques de la escena.
		 * 
		 * 
		 *
		 * @param escena
		 *            the escena
		 */
      public void desregistrarAreasTactiles(Scene escena);
  		
		/**
		 * Devuelve la lista de bloques de la pieza.
		 * 
		 * @return los bloques
		 */
		public List<Bloque> getBloques();
		
		/**
		 * Devuelve el cuerpo de la pieza.
		 * 
		 * @return el cuerpo
		 */
		public Body getCuerpo();
		
		/**
		 * Quita un bloque de la pieza.
		 * 
		 * NOTA: Este método no tiene en cuenta  las dependecias de los bloques
		 * y no  realiza división( conserva el hueco)
		 * 
		 * @param b
		 *            el bloque
		 * @see IPieza#Desenlazar()
		 */
		public void quitarBloque(Bloque b);
		
		/**
		 * Divide la pieza en fragmentos teniendo en cuenta las dependencias de los bloques.
		 * 
		 * @return El conjunto de piezas ( fragmentos)  que se han creado.
		 * 
		 * @see IPieza#quitarBloque(Bloque)
		 */
		public Collection<IPieza> Desenlazar();
		
		/**
		 * Quita los bloques de la pieza actual  y crea una nueva con bloques idénticos.
		 * 
		 * @param list
		 *            lista de bloques PROPIOS a separar
		 * @return la pieza resultante o this en caso de que se especifique el total de bloques de la pieza
		 */
		public IPieza separarBloques(List<Bloque> list);
		
		
		public PIEZAS getTipo();
		
		/**
		 * Checks if is destruida.
		 * 
		 * @return true, if is destruida
		 */
		public boolean isDestruida();
		
		
	
	
	

}
