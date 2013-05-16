package proyecto.blocktris;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.debugdraw.DebugRenderer;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.*;
import org.andengine.util.adt.color.Color;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;



import proyecto.blocktris.logica.EscenaBase;
import proyecto.blocktris.logica.fisica.ObjetoFisico;
import proyecto.blocktris.logica.fisica.Pieza;
import proyecto.blocktris.logica.fisica.Pieza.PIEZAS;
import proyecto.blocktris.recursos.ManagerEscenas.TipoEscena;

public class EscenaJuego extends EscenaBase{
	private PhysicsWorld  pwMundo;
	private Body cuerpo;
	@Override
	public void crearEscena() {
		
		pwMundo=  new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		setBackground(new Background(Color.WHITE));
		AnimatedSprite caja = new AnimatedSprite(camara.getCenterX(),
			camara.getCenterY(),
				managerRecursos.trBloques ,vbom);
		caja.setVisible(false);
		caja.animate(100);
	//cuerpo=	PhysicsFactory.createBoxBody(pwMundo, caja, BodyType.DynamicBody , 
	//			PhysicsFactory.createFixtureDef(1, 1, 1));
	
	//cuerpo.setAwake(false);
	//pwMundo.registerPhysicsConnector(new PhysicsConnector(caja,cuerpo));
	
	
	//Pieza  p2 = new Pieza(50, PIEZAS.PIEZA_ELE1,pwMundo); 
	//p2.adjuntarGraficos(this);
		
	
	Pieza  p = new Pieza(25, PIEZAS.PIEZA_ELE1,pwMundo); 
	p.adjuntarGraficos(this);
	p.getCuerpo().setTransform(camara.getCenterX() /32, camara.getCenterY() /32, 0);
	//p2.getCuerpo().setTransform(camara.getCenterX() /32, camara.getCenterY() /32, 0);
	registerUpdateHandler(pwMundo);
    DebugRenderer debug = new DebugRenderer(pwMundo, vbom);
    debug.setDrawBodies(true);
    debug.setDrawJoints(true);
    attachChild(debug);
    this.attachChild(caja);
	}

	@Override
	public void teclaVolverPreionada() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TipoEscena getTipoEscena() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deshacerEscena() {
		// TODO Auto-generated method stub
		
	}

}
