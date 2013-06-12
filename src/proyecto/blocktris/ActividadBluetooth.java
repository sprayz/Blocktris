/*
 *  @author Pablo Morillas Lozano
 */
package proyecto.blocktris;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * Actividad que puebla dinámicamente una lista de dispositivos Bluetooth en
 * rango.
 * 
 * @author Pablo Morillas Lozano
 */
public class ActividadBluetooth extends Activity{
	
	/** The dispositivos. */
	List<BluetoothDevice> dispositivos = new ArrayList<BluetoothDevice>();
	
	/** The resultado. */
	Intent resultado = new Intent();
	
	/** The seleccionado. */
	BluetoothDevice seleccionado = null;
	
	/** The lista. */
	TableLayout lista;
	
	/** The br. */
	BroadcastReceiver br;

	/** The Constant P_BLUETOOTH. */
	private static final int P_BLUETOOTH = 0;
	
	/** The Constant P_EMPAREJAR. */
	private static final int P_EMPAREJAR = P_BLUETOOTH + 1;
	
	/**
	 * Método estático para lanzar esta actividad desde otra.
	 * 
	 * @param c
	 *            the c
	 */
	public static void lanzar(Activity c) {
		Intent i = new Intent(c.getBaseContext(), ActividadBluetooth.class);
		c.startActivityForResult(i, 1);

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.actividad_bluetooth);

		Log.d("Actividad Bluetooth", "Actividad Iniciada");
		// ponemos nuestro dispositivo visible

		Intent discoverableIntent = new Intent(
				BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		// discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,140);
		startActivityForResult(discoverableIntent, P_BLUETOOTH);

		lista = (TableLayout) findViewById(R.id.layoutTablay);

		resultado.putExtra("Oponente", seleccionado);
		// put data that you want returned to activity A
		setResult(Activity.RESULT_OK, resultado);
		
		br = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
			    String action = intent.getAction();

			

			    if (BluetoothDevice.ACTION_FOUND.equals(action)) 
			    {
			       
			    	// sacamos el dispositivo
			        BluetoothDevice d = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			    
			        Log.d("Actividad Blutooth", "Dispositivo de nombre:" + d.getName());
			        //y lo añadimos a la lista para luego ponerlo en la tabla
			        dispositivos.add(d);
			        añadirLista(d);
			        
			    }
			  }
			};
			IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
			registerReceiver(br, filter);
	}

	/**
	 * Dispositivo seleccionado.
	 * 
	 * @param v
	 *            the v
	 */
	public void dispositivoSeleccionado(View v) {

	}

	/**
	 * Añadir lista.
	 * 
	 * @param bd
	 *            the bd
	 */
	private void añadirLista(BluetoothDevice bd) {
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		TableRow tr = new TableRow(this);
		tr.setLayoutParams(lp);

		TextView tvNombre = new TextView(this);
		Button bConectar= new Button(this);
		//tvLeft.setLayoutParams(lp);
		//tvLeft.setBackgroundColor(Color.DKGRAY);
		bConectar.setText(getString(R.string.actividadBluetooth_conectar));
		tvNombre.setText(bd.getName());
		bConectar.setTextColor(Color.rgb(3, 255, 255));
		tr.addView(bConectar);
		tr.addView(tvNombre);
		tr.setTag( Integer.valueOf(lista.getChildCount()));
		bConectar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Button bot = (Button) v;
				TableRow tr = (TableRow) bot.getParent();
				resultado.putExtra("Oponente", dispositivos.get(((Integer)tr.getTag()).intValue()));
				// put data that you want returned to activity A
				setResult(Activity.RESULT_OK, resultado);
				finish();
			}
		});
		lista.addView(tr);
	}
	
	
	
	

	/**
	 * Limpiar lista.
	 */
	private void limpiarLista() {
		if (lista.getChildCount() == 0)
			return;
		for (int i = 0; i < lista.getChildCount(); i++) {

			lista.removeViewAt(i);
		}

	}

	/**
	 * Refrescar.
	 * 
	 * @param v
	 *            the v
	 */
	public void refrescar(View v) {
		// borramos llas entradas actuales
		dispositivos.clear();
		limpiarLista();

		//ponemos el bluetooth a escanear
		BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
		BluetoothAdapter.getDefaultAdapter().startDiscovery(); 
		

		 
		
		
		
	
		
		
		
		

	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		Log.d("Actividad Bluetooth", "Actividad Destruida");
		BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
		unregisterReceiver(br);
		super.onDestroy();
		
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (resultCode) {
		case Activity.RESULT_CANCELED:
			
			switch (requestCode) {
			case P_BLUETOOTH:

				Log.e("ActividadBluetooth", "Error Bluetooth");
				finish();
				break;

			case P_EMPAREJAR:
				break;

			}
			break;
			
			
		case Activity.RESULT_OK:
			switch (requestCode) {
			case P_BLUETOOTH:

				Log.d("ActividadBluetooth", "Bluetooth activado");
				break;

			case P_EMPAREJAR:
				break;

			}
			break;
		}

	}

}
