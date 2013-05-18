package proyecto.blocktris.logica.fisica;

import java.util.ArrayList;

import org.andengine.entity.IEntity;

import com.badlogic.gdx.physics.box2d.Body;

public abstract class ObjetoFisico {
	
	protected ArrayList<Body> cuerpos = new ArrayList<Body>();
	protected ArrayList<IEntity> graficos = new ArrayList<IEntity>();
	
	/*
	 * Esta clase es práctica cómo base para   cualquier elemento físisco.
	 * De esta manear se puede  incluir una referencia en  los userdata de los objetos y tener
	 * siempre a disposicion el tipo completo y no solo la parte (fisica o gráfica)
	 */
	public ObjetoFisico(){}
	
	
	
	
	/*
	 * GETTERS Y SETTERS
	 */
	
	
	public ArrayList<Body> getCuerpos() {
		return cuerpos;
	}




	public ArrayList<IEntity> getGraficos() {
		return graficos;
	}

	

	
	
}
