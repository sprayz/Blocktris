package proyecto.blocktris.logica.fisica.piezas.rompibles;

import java.util.List;

import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import proyecto.blocktris.logica.fisica.piezas.IPieza;
import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaBase.Bloque.ColorBloque;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

public class PiezaT extends PiezaBase {

	public PiezaT(PhysicsWorld mundo, float x, float y, float tamaño_bloque,
			FixtureDef fixturedef) {
		super(mundo, x, y, tamaño_bloque, fixturedef);
		
		
		 float xf = x / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		 float yf = y / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		BodyDef bdef = new BodyDef ();
		bdef.active=true;
		bdef.awake= true;
		bdef.type = BodyType.DynamicBody ;
		
		cuerpo= mundo.createBody(bdef);
		bloques.add(new Bloque(mundo,cuerpo,-1,0,tamaño_bloque,ColorBloque.ROJO ,IPieza.FIXTUREDEF_DEFECTO ));
		bloques.add(new Bloque(mundo,cuerpo,0,0,tamaño_bloque,ColorBloque.ROJO ,IPieza.FIXTUREDEF_DEFECTO ));
		bloques.add(new Bloque(mundo,cuerpo,1,0,tamaño_bloque,ColorBloque.ROJO ,IPieza.FIXTUREDEF_DEFECTO ));
		bloques.add(new Bloque(mundo,cuerpo,0,1,tamaño_bloque,ColorBloque.ROJO ,IPieza.FIXTUREDEF_DEFECTO ));
		cuerpo.setTransform(xf, yf, 0);
		 
		
		bloques.get(0).getAdjacentes().add(bloques.get(1));
	
		
		
		bloques.get(1).getAdjacentes().add(bloques.get(2));
		bloques.get(1).getAdjacentes().add(bloques.get(3));
		
		
//	bloques.get(0).getCuerpo().destroyFixture();
		bloques.get(2).getAdjacentes().add(bloques.get(1));

		
		
		
		bloques.get(3).getAdjacentes().add(bloques.get(1));
		
		 
		 for(Bloque b: bloques){
			 b.setPadre(this);
			 
		 }
		
		 
	}
	

	

	

}