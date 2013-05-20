package proyecto.blocktris.logica.fisica.piezas;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import proyecto.blocktris.logica.fisica.piezas.PiezaBase.Bloque.ColorBloque;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

public class PiezaPalo extends PiezaBase {

	public PiezaPalo(PhysicsWorld mundo, float x, float y, float tamaño_bloque,
			FixtureDef fixturedef) {
		super(mundo, x, y, tamaño_bloque, fixturedef);
		 bloques.add(new Bloque(mundo,tamaño_bloque,ColorBloque.ROJO ,fixturedef));
		 bloques.add(new Bloque(mundo,tamaño_bloque,ColorBloque.ROJO ,fixturedef));
		 bloques.add(new Bloque(mundo,tamaño_bloque,ColorBloque.ROJO ,fixturedef));
		 bloques.add(new Bloque(mundo,tamaño_bloque,ColorBloque.ROJO ,fixturedef));
		 
		 WeldJointDef jointdef = new  WeldJointDef();
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
		
		 
	}

	

}
