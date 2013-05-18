package proyecto.blocktris.logica.fisica;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.Entity;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.shape.Shape;
import org.andengine.util.IDisposable;

import com.badlogic.gdx.physics.box2d.Body;

/*
 * Ocurre que los distintos eventos referencian o bien la parte grafica o la parte física
 * Es util tener  una referencia a ambas partes.
 * La idea es aprovecharse del userdatapara mantener una referencia al objeto padre, que ,
 * a su vez retiene una referencia a ambas partes.
 * 
 */
public abstract class ObjetoFisico {
	protected Body cuerpo;
	protected List<Entity> graficos = new ArrayList<Entity>();
	/**
	 * @param cuerpo
	 * @param grafico
	 */
	public ObjetoFisico(){}
	
	public ObjetoFisico(Body cuerpo,List<Entity> graficos) {
		super();
		this.cuerpo = cuerpo;
		this.graficos = graficos;
	}
	
	
	/*
	 * GETTERS Y SETTERS
	 */
	
	
	public Body getCuerpo() {
		return cuerpo;
	}

	public void setCuerpo(Body cuerpo) {
		this.cuerpo = cuerpo;
	}

	public List<Entity> getGraficos() {
		return graficos;
	}

	

	
}
