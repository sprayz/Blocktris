package proyecto.blocktris.logica.fisica;

import java.util.ArrayList;

import org.andengine.entity.IEntity;

import com.badlogic.gdx.physics.box2d.Body;

public abstract class ObjetoFisico {
	
	protected Body cuerpo  ;
	protected IEntity grafico ;
	protected Object padre; 
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
	public IEntity getGrafico() {
		return grafico;
	}
	
	
	
	
	/*
	 * GETTERS Y SETTERS
	 */
	
	
	
	
	
}
