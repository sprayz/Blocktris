package proyecto.blocktris;

import proyecto.blocktris.recursos.BDPuntuaciones;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;

public class ActividadPuntuaciones extends Activity {

	public static void lanzar(Activity c) {
		Intent i = new Intent(c.getBaseContext(), ActividadPuntuaciones.class);
		c.startActivityForResult(i, 2);

	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actividad_puntuaciones);
		BDPuntuaciones  bd = new BDPuntuaciones(this);
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(
			    this,
			    R.id.listView1,
			    bd.getTop20(), // getting cursor for database query here.
			    new String[] { BDPuntuaciones.CAMPO_NOMBRE, BDPuntuaciones.CAMPO_PUNTOS },
			   new int [] { R.id.textView1 , R.id.textView2}
			    
			);        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.actividad_puntuaciones, menu);
		return true;
	}

}
