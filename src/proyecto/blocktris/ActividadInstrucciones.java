package proyecto.blocktris;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actividad_instrucciones);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	
		
		return true;
	}

}
