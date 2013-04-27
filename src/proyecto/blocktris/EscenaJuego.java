package proyecto.blocktris;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;

import proyecto.blocktris.logica.EscenaBase;
import proyecto.blocktris.recursos.ManagerEscenas.TipoEscena;

public class EscenaJuego extends EscenaBase{


	@Override
	public void crearEscena() {
		Rectangle  caja = new Rectangle(camara.getCenterX(), camara.getCenterY(), camara.getWidth(), camara.getHeight() /2, vbom);
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
