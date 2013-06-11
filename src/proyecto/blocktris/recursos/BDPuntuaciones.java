package proyecto.blocktris.recursos;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class BDPuntuaciones extends SQLiteOpenHelper {
	public static final String NOMBRE_BD= "Puntuaciones.db";
	public static final int  VERSION_BD = 1;
	public static final String TABLA_PUNTUACIONES = "Puntuaciones";
	
	//Columnas 
	
	public static final String CAMPO_ID = "_id";
	public static final String CAMPO_NOMBRE = "nombre";
	public static final String CAMPO_PUNTOS = "puntos";
	public BDPuntuaciones(Context context) {
		super(context, NOMBRE_BD, null, VERSION_BD);
		
	}


	@Override
	public void onCreate(SQLiteDatabase arg0) {
		 String CREAR = "CREATE TABLE " + TABLA_PUNTUACIONES + "("
	                + CAMPO_ID + " INTEGER PRIMARY KEY," + CAMPO_NOMBRE + " TEXT,"
	                + CAMPO_PUNTOS + " INTEGER" + ")";
	        arg0.execSQL(CREAR);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		  
        arg0.execSQL("DROP TABLE IF EXISTS " + TABLA_PUNTUACIONES);
 
        
        onCreate(arg0);

	}
	
	
	 public Cursor getTop20() {
		   
		    String selectQuery = "SELECT  * FROM " + TABLA_PUNTUACIONES + " ORDER BY " + CAMPO_PUNTOS + " LIMIT 20";
		 
		    SQLiteDatabase db = this.getWritableDatabase();
		    Cursor cursor = db.rawQuery(selectQuery, null);
		  
		   
		    return cursor;
		}
	 
	 
	 public List<Puntuacion> getTop20Lista() {
		    List<Puntuacion> res = new ArrayList<Puntuacion>();
		    
		    Cursor cursor = getTop20();
		 
		  
		    if (cursor.moveToFirst()) {
		        do {
		            Puntuacion puntuacion = new Puntuacion();
		            puntuacion.setId(cursor.getInt(0));
		            puntuacion.setNombre(cursor.getString(1));
		            puntuacion.setPuntos(cursor.getInt(2));
		           
		            res.add(puntuacion);
		        } while (cursor.moveToNext());
		    }
		 
		    return res;
		}
	
	 
	 
	public void addContact(Puntuacion puntuacion) {
	    SQLiteDatabase bd = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(CAMPO_NOMBRE, puntuacion.getNombre()); 
	    values.put(CAMPO_PUNTOS, puntuacion.getPuntos()); 
	 
	    
	    bd.insert(TABLA_PUNTUACIONES, null, values);
	    bd.close(); 
	}
	
	
	

}
