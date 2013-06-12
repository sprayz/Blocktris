package proyecto.blocktris;

import proyecto.blocktris.recursos.BDPuntuaciones;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.widget.SimpleAdapter;

/**
 * Esta clase representa la lista de puntuaciones
 * 
 * @author Pablo Morillas Lozano
 *
 */
public class ActividadPuntuaciones extends ListActivity {

	/**
	 * 
	 * Método estático para lanzar esta actividad desde otra.
	 */
	public static void lanzar(Activity c) {
		
		
		
		Intent i = new Intent(c.getBaseContext(), ActividadPuntuaciones.class);
		c.startActivityForResult(i, 2);

	}

		
		
		protected void onCreate(Bundle savedInstanceState) {
			BDPuntuaciones  bd = new BDPuntuaciones(this);
		    super.onCreate(savedInstanceState);
		 
		    
		    String[] from = { BDPuntuaciones.CAMPO_NOMBRE ,BDPuntuaciones.CAMPO_PUNTOS };
		    int[] to = { android.R.id.text1, android.R.id.text2 };

		    @SuppressWarnings("deprecation")
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(this ,
		        android.R.layout.simple_list_item_activated_2,bd.getTop10(), from, to);
		    setListAdapter(adapter);
		bd.close();
	}

	

}

