package proyecto.blocktris;

import java.util.Random;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.debugdraw.DebugRenderer;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.opengl.texture.region.*;
import org.andengine.util.adt.color.Color;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;



import proyecto.blocktris.logica.EscenaBase;
import proyecto.blocktris.logica.fisica.piezas.IPieza;
import proyecto.blocktris.logica.fisica.piezas.PiezaPalo;
import proyecto.blocktris.recursos.ManagerEscenas.TipoEscena;

public class EscenaJuego extends EscenaBase implements IAccelerationListener{

	public static FixtureDef fdef_muro = PhysicsFactory.createFixtureDef(1.0f, 0, 1.0f);
	public Body suelo ; 
	public Body techo; 
	public Body pared_izquierda ; 
	public Body pared_derecha ; 
	PhysicsWorld mundo ; 
	float tamaño_bloque;
	static final int FILAS = 12 ;
	static final int COLUMNAS = 8;
	
	@Override
	public void crearEscena() {
		// dejamos  1/10 de el tamaño del bloque  de margen
		tamaño_bloque = camara.getWidth() /  ( COLUMNAS);
		
		tamaño_bloque = tamaño_bloque - 0.2f / COLUMNAS;
		
	
		//fondo
		setBackground(new Background(Color.RED));
		
		//mundo fisico
		mundo= new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		//activamos le sensor de rientacion para cambiar la gravedad
		managerRecursos.actividadJuego.getEngine().enableAccelerationSensor(managerRecursos.actividadJuego,this);
		
		//sprite de referencia marcando el centro
		AnimatedSprite caja = new AnimatedSprite(camara.getCenterX(),
				camara.getCenterY(),
				
				managerRecursos.trBloques.deepCopy() ,vbom);
		caja.setSize(10, 10);
		super.attachChild(caja);
		caja.setVisible(true);
		caja.animate(100);
		
		//creammos las paredes
		suelo =PhysicsFactory.createLineBody(mundo, 0, 0, camara.getWidth(), 0, fdef_muro);
		techo=PhysicsFactory.createLineBody(mundo, 0, camara.getHeight(),camara.getWidth() , camara.getHeight(), fdef_muro);
		pared_izquierda=PhysicsFactory.createLineBody(mundo, 0, 0,0, camara.getHeight(), fdef_muro);
		pared_derecha=PhysicsFactory.createLineBody(mundo, camara.getWidth(), 0,camara.getWidth() , camara.getHeight(), fdef_muro);
		
		Random rnd = new Random();
		for(int i =0;i <3;i++){
			PiezaPalo pieza = new PiezaPalo(mundo, camara.getWidth() * rnd.nextFloat(), camara.getHeight()* rnd. nextFloat(), tamaño_bloque, IPieza.FIXTUREDEF_DEFECTO ); 
			pieza.registrarGraficos(this);
			
		}
		
		
		
		registerUpdateHandler(mundo); 
	    DebugRenderer debug = new DebugRenderer(mundo, vbom);
	    debug.setDrawBodies(true);
	    debug.setDrawJoints(true);
	    attachChild(debug); 
	}

	@Override
	public void teclaVolverPreionada() {
		// TODO Auto-generated method stub
		System.exit(0);
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
	
	@Override
	public void onAccelerationAccuracyChanged(final AccelerationData pAccelerationData) {

	}

	@Override
	public void onAccelerationChanged(final AccelerationData pAccelerationData) {
		final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX(), pAccelerationData.getY());
		mundo.setGravity(gravity);
		Vector2Pool.recycle(gravity);
	}
	
	

}
