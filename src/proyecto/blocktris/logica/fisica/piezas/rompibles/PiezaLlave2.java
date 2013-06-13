/*
 *  @author Pablo Morillas Lozano
 */
package proyecto.blocktris.logica.fisica.piezas.rompibles;

import java.util.List;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import proyecto.blocktris.logica.fisica.piezas.IPieza;
import proyecto.blocktris.logica.fisica.piezas.IPieza.PIEZAS;
import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaBase.Bloque;
import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaBase.Bloque.ColorBloque;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

// TODO: Auto-generated Javadoc
/**
 * The Class PiezaLlave2.
 */
public class PiezaLlave2 extends PiezaBase {

	/**
	 * Crea una pieza  de tipo Llave2
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
	public PiezaLlave2(PhysicsWorld mundo, float x, float y, float tamaño_bloque,
			FixtureDef fixturedef,BodyDef bodydef) {
		super(mundo, x, y, tamaño_bloque, fixturedef, bodydef);
		tipo= PIEZAS.PIEZA_LLAVE2;
		
		 float xf = x / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		 float yf = y / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
	
		
		cuerpo= mundo.createBody(bodydef);
		bloques.add(new Bloque(mundo,cuerpo,-1.5f,-1f,tamaño_bloque,ColorBloque.VERDE ,fixturedef ));
		bloques.add(new Bloque(mundo,cuerpo,-0.5f,-1f,tamaño_bloque,ColorBloque.VERDE ,fixturedef ));
		bloques.add(new Bloque(mundo,cuerpo,-0.5f,0f,tamaño_bloque,ColorBloque.VERDE ,fixturedef ));
		bloques.add(new Bloque(mundo,cuerpo,0.5f,0f,tamaño_bloque,ColorBloque.VERDE ,fixturedef ));
		cuerpo.setTransform(xf, yf, 0);
		 
		cuerpo.setUserData(this); 
		bloques.get(0).getAdyacentes().add(bloques.get(1));
	
		
		
		bloques.get(1).getAdyacentes().add(bloques.get(2));
		bloques.get(1).getAdyacentes().add(bloques.get(0));
		
		
//	bloques.get(0).getCuerpo().destroyFixture();
		bloques.get(2).getAdyacentes().add(bloques.get(1));
		bloques.get(2).getAdyacentes().add(bloques.get(3));
		
		
		
		bloques.get(3).getAdyacentes().add(bloques.get(2));
		
		 
		 for(Bloque b: bloques){
			 b.setPadre(this);
			 this.contenedor.attachChild(b.getGrafico());
		 }
		
		 this.mundo = mundo;
			this.conector = new PhysicsConnector(contenedor, cuerpo);
			
			 mundo.registerPhysicsConnector(conector);
		 
	}
	

	

	

}
