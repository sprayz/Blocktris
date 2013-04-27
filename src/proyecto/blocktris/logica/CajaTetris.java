package proyecto.blocktris.logica;
import org.andengine.extension.physics.box2d.*;
import org.andengine.extension.physics.box2d.util.*;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.primitive.vbo.IRectangleVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;

import android.hardware.SensorManager;

//Un rectángulo que define un mundo físico (PhysicsWorld) configurable

public class CajaTetris{
	//mundo fisico que se alica  en el area
	
	private PhysicsWorld world ;
	//la entidad a la que confinamos nuestra fisica
	private Entity entidad;
	//si tiene muros o no

	/**
	 * 
	 */
	
	
	public CajaTetris(Entity entidad) {
		
		this.entidad = entidad;

		
		// TODO Auto-generated constructor stub
		
		//Y se hizo la luz ;D
		world = new PhysicsWorld(new Vector2(0,SensorManager.GRAVITY_EARTH), false);
	    
		//registramos el gestor de actualizacion(  el mundo)
		// para que  tenga tiempo de hacer sus tareas cuando  nuestro rectangulo(this) se actualice
		//NOTA: Esto deberia constreñir el mundo fisico a las entidades HIJAS de  el rectangulo.Deberia...
		
		
		
		
		
	}

	

	public PhysicsWorld getWorld() {
		return world;
	}

	public Entity getEntidad() {
		return entidad;
	}
	
	
	public void setEntidad(Entity entidad) {
		this.entidad = entidad;
		entidad.registerUpdateHandler(world);
		
	}
	
	public void start(){
		entidad.registerUpdateHandler(world);
	}
	public void stop(){
		entidad.unregisterUpdateHandler(world);
	}

	

	

}
