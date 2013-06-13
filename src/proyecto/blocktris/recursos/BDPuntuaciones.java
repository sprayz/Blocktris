/*
 *  @author Pablo Morillas Lozano
 */
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


/**
 * Esta clase representa la base de datos SQLite que contiene las puntuaciones.
 */
public class BDPuntuaciones extends SQLiteOpenHelper {
	
	/** El nombre de la base de datos. */
	public static final String NOMBRE_BD= "Puntuaciones.db";
	
	/** La versión. */
	public static final int  VERSION_BD = 1;
	
	/** Nombre de la tabla  de puntuaciones. */
	public static final String TABLA_PUNTUACIONES = "Puntuaciones";
	
	//Columnas 
	
	/** Columna id. */
	public static final String CAMPO_ID = "_id";
	
	/** Columna nombre. */
	public static final String CAMPO_NOMBRE = "nombre";
	
	/** Columna puntuación. */
	public static final String CAMPO_PUNTOS = "puntos";
	
	public BDPuntuaciones(Context context) {
		super(context, NOMBRE_BD, null, VERSION_BD);
		
	}


	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		
		//creamos la base de datos acorde a las constantes
		String CREAR = "CREATE TABLE " + TABLA_PUNTUACIONES + "("
	                + CAMPO_ID + " INTEGER PRIMARY KEY," + CAMPO_NOMBRE + " TEXT,"
	                + CAMPO_PUNTOS + " INTEGER" + ")";
	        arg0.execSQL(CREAR);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		 //si cambió el campo versión borramos la tabla y la recreamos 
        arg0.execSQL("DROP TABLE IF EXISTS " + TABLA_PUNTUACIONES);
 
        
        onCreate(arg0);

	}
	
	
	 /**
	 * Este método obtiene  las 10 primeras filas ordenadas por puntuación descendentemente.
	 * 
	 * @return cursor con los resultados
	 */
 	public Cursor getTop10() {
		   
		    String selectQuery = "SELECT  * FROM " + TABLA_PUNTUACIONES + " ORDER BY " + CAMPO_PUNTOS + " DESC LIMIT 10";
		 
		    SQLiteDatabase db = this.getWritableDatabase();
		    Cursor cursor = db.rawQuery(selectQuery, null);
		  
		   
		    return cursor;
		}
	 
	 
 	 /**
 		 * Este método obtiene  las 10 primeras filas ordenadas por puntuación descendentemente.
 		 * 
 		 * @return Una lista con los resultados
 		 */
 	public List<Puntuacion> getTop10Lista() {
		    List<Puntuacion> res = new ArrayList<Puntuacion>();
		    
		    Cursor cursor = getTop10();
		 
		  
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
	
	 
	 
	/**
	 * Este método añade un objeto Puntuacion.
	 * 
	 * Ignora el campo ID del objeto en favor de la configuración de la tabla.
	 * 
	 * @param puntuacion
	 *            el objeto a añadir
	 */
	public void añadirPuntuacion(Puntuacion puntuacion) {
	    SQLiteDatabase bd = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(CAMPO_NOMBRE, puntuacion.getNombre()); 
	    values.put(CAMPO_PUNTOS, puntuacion.getPuntos()); 
	 
	    
	    bd.insert(TABLA_PUNTUACIONES, null, values);
	    bd.close(); 
	}
	
	
	

}
