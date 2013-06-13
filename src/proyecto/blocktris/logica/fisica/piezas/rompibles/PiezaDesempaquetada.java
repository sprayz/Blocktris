/*
 *  @author Pablo Morillas Lozano
 */
package proyecto.blocktris.logica.fisica.piezas.rompibles;

import java.util.List;

import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import proyecto.blocktris.recursos.*;
import proyecto.blocktris.recursos.EstadoJuego.EstadoPieza.EstadoBloque;
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
 *  Pieza a partir de  un {@link EstadoJuego.EstadoPieza}
 */
public class PiezaDesempaquetada extends PiezaBase {

	/**
	 * Crea una pieza  a partir de  un {@link EstadoJuego.EstadoPieza}
	 * 
	 * @param mundo
	 *            el mundo
	 * @param estado
	 *            el estado
	 */
	public PiezaDesempaquetada(PhysicsWorld mundo, EstadoJuego.EstadoPieza estado) {
		
		super(mundo, 
				estado.bodydef.position.x* PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
				estado.bodydef.position.y* PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
				estado.tamaño_bloque, estado.fixturedef, estado.bodydef);
		
		
	
		
		BodyDef bodydef = estado.bodydef;
		cuerpo= mundo.createBody(bodydef);
		cuerpo.setTransform(bodydef.position.x, bodydef.position.y, bodydef.angle);
		 
		cuerpo.setUserData(this); 
		cuerpo.setFixedRotation(false);
		for(EstadoBloque eb : estado.bloques){
			this.bloques.add( new Bloque(mundo, 
										cuerpo, 
										eb.x, eb.y, 
										estado.tamaño_bloque, 
										eb.color, 
										estado.fixturedef));
			
		
			
			
		}
		
		//reestablecemos los adyacentes
		for(int i = 0;i <estado.bloques.size();i++){
			EstadoBloque b =  estado.bloques.get(i);
			
			// por cada adyacente de ese bloque
			for(Integer ad: b.adyacentes){
				this.bloques.get(i).getAdyacentes().add(this.bloques.get(ad));
				
			}
			
			
		}
		
		
		 for(Bloque b: bloques){
			 b.setPadre(this);
			 this.contenedor.attachChild(b.getGrafico());
		 }
		
		 this.mundo = mundo;
			this.conector = new PhysicsConnector(contenedor, cuerpo);
			
			 mundo.registerPhysicsConnector(conector);
		 
	}
	

	

	

}
