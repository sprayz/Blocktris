package proyecto.blocktris;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.*;
import org.andengine.util.adt.color.Color;



import proyecto.blocktris.logica.EscenaBase;
import proyecto.blocktris.recursos.ManagerEscenas.TipoEscena;

public class EscenaJuego extends EscenaBase{


	@Override
	public void crearEscena() {
		setBackground(new Background(Color.WHITE));
		AnimatedSprite caja = new AnimatedSprite(camara.getCenterX(),
				camara.getCenterY(),
				managerRecursos.trBloques.deepCopy() ,vbom);
		caja.setSize(10, 10);
		super.attachChild(caja);
		caja.setVisible(true);
		caja.animate(100);
		
		
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
