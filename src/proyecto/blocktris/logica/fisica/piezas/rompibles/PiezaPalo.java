package proyecto.blocktris.logica.fisica.piezas.rompibles;

import java.util.List;

import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import proyecto.blocktris.logica.fisica.piezas.IPieza;
import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaBase.Bloque.ColorBloque;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

public class PiezaPalo extends PiezaBase {

	public PiezaPalo(PhysicsWorld mundo, float x, float y, float tamaño_bloque,
			FixtureDef fixturedef) {
		super(mundo, x, y, tamaño_bloque, fixturedef);
		 bloques.add(new Bloque(mundo,tamaño_bloque,ColorBloque.ROJO ,fixturedef));
		 bloques.add(new Bloque(mundo,tamaño_bloque,ColorBloque.ROJO ,fixturedef));
		 bloques.add(new Bloque(mundo,tamaño_bloque,ColorBloque.ROJO ,fixturedef));
		 bloques.add(new Bloque(mundo,tamaño_bloque,ColorBloque.ROJO ,fixturedef));
		 
		 
		 float xf = x / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		 float yf = y / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		 float tamaño_bloque_fisico = tamaño_bloque / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		 WeldJointDef jointdef = new  WeldJointDef();
		 
		 jointdef.referenceAngle=0;
		
		 bloques.get(0).getCuerpo().setTransform(xf, yf, 0);
		 bloques.get(1).getCuerpo().setTransform(xf, yf+tamaño_bloque_fisico , 0);
		 bloques.get(2).getCuerpo().setTransform(xf, yf+tamaño_bloque_fisico*2, 0);
		 bloques.get(3).getCuerpo().setTransform(xf, yf+tamaño_bloque_fisico*3, 0);
		 //0-1
		 
		
		 jointdef.initialize(bloques.get(0).getCuerpo(), //cuerpo 1
				 			bloques.get(1).getCuerpo(),  //cuerpo 2
				 			bloques.get(0).getCuerpo().getWorldCenter()	); 
		 mundo.createJoint(jointdef);
		 
		 
		 
		 //1-2
		 jointdef.initialize(bloques.get(1).getCuerpo(), //cuerpo 1
		 			bloques.get(2).getCuerpo(),  //cuerpo 2
		 			bloques.get(1).getCuerpo().getWorldCenter()	); 
		 mundo.createJoint(jointdef);
		 
		 
		 
		 
		 
		 
		 //2-3
		 jointdef.initialize(bloques.get(2).getCuerpo(), //cuerpo 1
			bloques.get(3).getCuerpo(),  //cuerpo 2
			bloques.get(2).getCuerpo().getWorldCenter()	); 
		 mundo.createJoint(jointdef);
		 
		 
		 
		 for(Bloque b: bloques){
			 b.setPadre(this);
			 
		 }
		
		 
	}

	@Override
	public IPieza destruirPieza(List<Fixture> a_borrar) {
		// TODO Auto-generated method stub
		return null;
	}

	

	

}
