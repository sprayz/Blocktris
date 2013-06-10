package proyecto.blocktris.logica.fisica.piezas.rompibles;

import java.util.List;

import org.andengine.extension.physics.box2d.PhysicsConnector;
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

public class PiezaCuadrado extends PiezaBase {

	public PiezaCuadrado(PhysicsWorld mundo, float x, float y, float tamaño_bloque,
			FixtureDef fixturedef,BodyDef bodydef) {
		super(mundo, x, y, tamaño_bloque, fixturedef,bodydef);
		
		tipo= PIEZAS.PIEZA_CUBO;
		
		 float xf = x / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		 float yf = y / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		
		
		cuerpo= mundo.createBody(bodydef);
		bloques.add(new Bloque(mundo,cuerpo,-1,0,tamaño_bloque,ColorBloque.MORADO ,fixturedef ));
		bloques.add(new Bloque(mundo,cuerpo,0,0,tamaño_bloque,ColorBloque.MORADO ,fixturedef ));
		bloques.add(new Bloque(mundo,cuerpo,-1,-1,tamaño_bloque,ColorBloque.MORADO ,fixturedef ));
		bloques.add(new Bloque(mundo,cuerpo,0,-1,tamaño_bloque,ColorBloque.MORADO ,fixturedef ));
		cuerpo.setTransform(xf, yf, 0);
		cuerpo.setUserData(this); 
		
		bloques.get(0).getAdjacentes().add(bloques.get(1));
		bloques.get(0).getAdjacentes().add(bloques.get(2));
	
		
		
		bloques.get(1).getAdjacentes().add(bloques.get(0));
		bloques.get(1).getAdjacentes().add(bloques.get(3));
		
		
//	bloques.get(0).getCuerpo().destroyFixture();
		bloques.get(2).getAdjacentes().add(bloques.get(0));
		bloques.get(2).getAdjacentes().add(bloques.get(3));
		
		
		
		bloques.get(3).getAdjacentes().add(bloques.get(1));
		bloques.get(3).getAdjacentes().add(bloques.get(2));
		
		 
		 for(Bloque b: bloques){
			 b.setPadre(this);
			 this.contenedor.attachChild(b.getGrafico());
		 }
		
		 this.mundo = mundo;
			this.conector = new PhysicsConnector(contenedor, cuerpo);
			
			 mundo.registerPhysicsConnector(conector);
	}
	

	

	

}
