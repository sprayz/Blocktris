package proyecto.blocktris.logica.fisica.piezas;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.physics.box2d.FixtureDef;

public class PiezaPalo extends PiezaBase {

	public PiezaPalo(PhysicsWorld mundo, float x, float y, float tamaño_bloque,
			FixtureDef fixturedef) {
		super(mundo, x, y, tamaño_bloque, fixturedef);
		
	}

	

}
