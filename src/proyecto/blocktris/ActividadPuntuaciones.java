/*
 *  @author Pablo Morillas Lozano
 */
package proyecto.blocktris;

import proyecto.blocktris.recursos.BDPuntuaciones;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.widget.SimpleAdapter;

// TODO: Auto-generated Javadoc
/**
 * Esta clase representa la lista de puntuaciones.
 * 
 * @author Pablo Morillas Lozano
 */
public class ActividadPuntuaciones extends ListActivity {

	/**
	 * Método estático para lanzar esta actividad desde otra.
	 * 
	 * @param c
	 *            la actividad desde la que se invoca
	 */
	public static void lanzar(Activity c) {
		
		
		
		Intent i = new Intent(c.getBaseContext(), ActividadPuntuaciones.class);
		c.startActivityForResult(i, 2);

	}

		
		
		/* (non-Javadoc)
		 * @see android.app.Activity#onCreate(android.os.Bundle)
		 */
		protected void onCreate(Bundle savedInstanceState) {
			
		    super.onCreate(savedInstanceState);
		 
		    //abrimos la BD
			BDPuntuaciones  bd = new BDPuntuaciones(this);
		    
			//campos de la BD que vamos a representar
		    String[] from = { BDPuntuaciones.CAMPO_NOMBRE ,BDPuntuaciones.CAMPO_PUNTOS };
		    //controles a usar para representar estos campos.
		    int[] to = { android.R.id.text1, android.R.id.text2 };

		    //Vudú
		    @SuppressWarnings("deprecation")
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(this ,
		        android.R.layout.simple_list_item_activated_2,bd.getTop10(), from, to);
		   setListAdapter(adapter);
		
		   //cerramos la BD
		   bd.close();
	}

	

}

