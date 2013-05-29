package proyecto.blocktris.logica.fisica.piezas;

import org.andengine.extension.physics.box2d.PhysicsWorld;

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

public interface IPieza {

	public static enum PIEZAS {
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

		  public static PIEZAS random()  {
		    return VALUES.get(RANDOM.nextInt(SIZE));
	}
		  }

      public static FixtureDef PROPIEDADES_DEFECTO = PhysicsFactory.createFixtureDef(1.0f, 0.5f, 0.1f);

      
      public static FixtureDef FIXTUREDEF_DEFECTO=  PhysicsFactory.createFixtureDef(0.1f, 0.0f, 0.4f);
     public static BodyDef BODYDEF_DEFECTO= null;
     
     
      
      public void registrarGraficos(IEntity entidad);
      public void desregistrarGraficos();
      public IPieza destruirPieza();
      public void registrarAreasTactiles(Scene escena);
      public void desregistrarAreasTactiles(Scene escena);
  		public IPieza separarBloques(List<Bloque> list);
		public List<Bloque> getBloques();
		public Body getCuerpo();
		public Collection<IPieza> quitarBloqueDesenlazar(Bloque bloque);
		
		public PIEZAS getTipo();
	
	

	
	
	

}
