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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class ActividadBluetooth extends Activity{
	List<BluetoothDevice> dispositivos = new ArrayList<BluetoothDevice>();
	Intent resultado = new Intent();
	BluetoothDevice seleccionado = null;
	TableLayout lista;
	BroadcastReceiver br;

	private static final int P_BLUETOOTH = 0;
	private static final int P_EMPAREJAR = P_BLUETOOTH + 1;

	public static void lanzar(Activity c) {
		Intent i = new Intent(c.getBaseContext(), ActividadBluetooth.class);
		c.startActivityForResult(i, 1);

	}

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

	}

	public void dispositivoSeleccionado(View v) {

	}

	private void añadirLista(BluetoothDevice bd) {
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		TableRow tr = new TableRow(this);
		tr.setLayoutParams(lp);

		TextView tvLeft = new TextView(this);
		//tvLeft.setLayoutParams(lp);
		tvLeft.setBackgroundColor(Color.RED);
		tvLeft.setText(bd.getName());
		
		tr.addView(tvLeft);
		lista.addView(tr);
	}
	
	
	private void añadirListap(String prueba) {
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		TableRow tr = new TableRow(this);
		tr.setLayoutParams(lp);

		TextView tvLeft = new TextView(this);
	tvLeft.setLayoutParams(lp);
		tvLeft.setBackgroundColor(Color.RED);
		tvLeft.setText(prueba);
		tr.addView(tvLeft);
		lista.addView(tr);

	}
	
	

	private void limpiarLista() {
		if (lista.getChildCount() == 0)
			return;
		for (int i = 0; i < lista.getChildCount(); i++) {

			lista.removeViewAt(i);
		}

	}

	public void refrescar(View v) {
		// borramos llas entradas actuales
		//dispositivos.clear();
		//limpiarLista();

		//ponemos el bluetooth a escanear
		
		BluetoothAdapter.getDefaultAdapter().startDiscovery(); 
		br = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
		    String action = intent.getAction();

		

		    if (BluetoothDevice.ACTION_FOUND.equals(action)) 
		    {
		       
		    	// sacamos el dispositivo
		        BluetoothDevice d = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		        Log.d("Actividad Blutooth", "Dispositivo de nombre:" + d.getName() );
		        //y lo añadimos a la lista para luego ponerlo en la tabla
		        dispositivos.add(d);
		        añadirLista(d);
		        
		    }
		  }
		};

		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND); 
		registerReceiver(br, filter);
		
		
		añadirListap("adsasd");
		// añadimos las entradas de los dispositivos conocidos
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
				.getBondedDevices();

		for (BluetoothDevice bt : pairedDevices)
			dispositivos.add(bt);
		
		
		
		
		

	}

	@Override
	protected void onDestroy() {
		Log.d("Actividad Bluetooth", "Actividad Destruida");
		super.onDestroy();
		
		unregisterReceiver(br);
	}

	// Wheeee! en la era de la multitarea tenemos una API que asume una de dos
	// cosas:
	// 1.La multitarea es una leyenda
	// 2.El programador/usuario es hyperactivo
	// FML con android y su diseño autoritario

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
