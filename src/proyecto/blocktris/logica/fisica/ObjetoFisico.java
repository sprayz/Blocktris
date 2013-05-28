package proyecto.blocktris.logica.fisica;

import java.util.ArrayList;

import org.andengine.entity.IEntity;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.physics.box2d.Body;

public abstract class ObjetoFisico <T extends IEntity> {
	
	protected Body cuerpo  ;
	protected T grafico ;
	protected Object padre; 
	protected PhysicsWorld mundo;
	/*
	 * Esta clase es práctica cómo base para   cualquier elemento físisco.
	 * De esta manear se puede  incluir una referencia en  los userdata de los objetos y tener
	 * siempre a disposicion el tipo completo y no solo la parte (fisica o gráfica)
	 */
	public ObjetoFisico(){}

	public Object getPadre() {
		return padre;
	}
	public void setPadre(Object padre) {
		this.padre = padre;
	}
	public Body getCuerpo() {
		return cuerpo;
	}
	public T getGrafico() {
		return grafico;
	}
	
	public void destruir(){
		cuerpo.setActive(false);
		mundo.destroyBody(cuerpo);
		grafico.detachSelf();
		
	}
	
	public PhysicsWorld getMundo() {
		return mundo;
	}

	
	public void registrarAreaTactil (Scene escena){
		
		escena.registerTouchArea(this.grafico); 
	}
	public void desregistrarAreaTactil (Scene escena){
		
		escena.unregisterTouchArea(this.grafico); 
	}
	public void registrarGrafico (IEntity entidad){
		
		entidad.attachChild(this.grafico); 
		}
	public void desregistrarGrafico (){
		
	 this.grafico.detachSelf();
		}
		
	
	/*
	 * GETTERS Y SETTERS
	 */
	
	
	
	
	
}
