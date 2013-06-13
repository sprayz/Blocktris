/*
 *  @author Pablo Morillas Lozano
 */
package proyecto.blocktris;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;


/**
 * ESta clase representa el diálogo de instrucciones.
 */
public class ActividadInstrucciones extends Activity {

	
	
	/**
	 * Método estático para lanzar esta actividad desde otra.
	 * 
	 * @param c
	 *            la actividad desde la que se invoca
	 */
	public static void lanzar(Activity c) {
		
		
		
		Intent i = new Intent(c.getBaseContext(), ActividadInstrucciones.class);
		c.startActivityForResult(i, 2);

	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actividad_instrucciones);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	
		
		return true;
	}

}
