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

public class PiezaDesempaquetada extends PiezaBase {

	public PiezaDesempaquetada(PhysicsWorld mundo, EstadoJuego.EstadoPieza estado) {
		super(mundo, 
				estado.bodydef.position.x* PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
				estado.bodydef.position.y* PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
				estado.tamaño_bloque, estado.fixturedef, estado.bodydef);
		
		
	
		
		cuerpo= mundo.createBody(estado.bodydef);
		cuerpo.setTransform(estado.bodydef.position.x, estado.bodydef.position.y, 0);
		 
		cuerpo.setUserData(this); 
		
		for(EstadoBloque eb : estado.bloques){
			this.bloques.add( new Bloque(mundo, 
										cuerpo, 
										eb.x, eb.y, 
										estado.tamaño_bloque, 
										eb.color, 
										estado.fixturedef));
			
		
			
			
		}
		
		//reestablecemos los adyacentes

		int indice ;
		List<Bloque> adyacentes_nuevos;
		for(int i = 0;i <this.getBloques().size();i++ )
				//por cada adyacente del antiguo
				for(EstadoBloque adj : estado.bloques.get(i).adyacentes){
				
					adyacentes_nuevos = bloques.get(i).getAdjacentes();
					//añadimos los adyacentes del nuevo que tengan el mismo indice que los adyacentes del viejo
					indice =estado.bloques.indexOf(adj);
					if(indice <0)
						break;
					adyacentes_nuevos.add(this.getBloques().get(indice));
					
			
			
			
			
		}
		
		
		 for(Bloque b: bloques){
			 b.setPadre(this);
			 this.contenedor.attachChild(b.getGrafico());
		 }
		
		 mundo.registerPhysicsConnector(new PhysicsConnector(contenedor, cuerpo));
		
		 
	}
	

	

	

}
