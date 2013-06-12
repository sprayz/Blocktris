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

// TODO: Auto-generated Javadoc
/**
 * The Class BDPuntuaciones.
 */
public class BDPuntuaciones extends SQLiteOpenHelper {
	
	/** The Constant NOMBRE_BD. */
	public static final String NOMBRE_BD= "Puntuaciones.db";
	
	/** The Constant VERSION_BD. */
	public static final int  VERSION_BD = 1;
	
	/** The Constant TABLA_PUNTUACIONES. */
	public static final String TABLA_PUNTUACIONES = "Puntuaciones";
	
	//Columnas 
	
	/** The Constant CAMPO_ID. */
	public static final String CAMPO_ID = "_id";
	
	/** The Constant CAMPO_NOMBRE. */
	public static final String CAMPO_NOMBRE = "nombre";
	
	/** The Constant CAMPO_PUNTOS. */
	public static final String CAMPO_PUNTOS = "puntos";
	
	/**
	 * Instantiates a new bD puntuaciones.
	 * 
	 * @param context
	 *            the context
	 */
	public BDPuntuaciones(Context context) {
		super(context, NOMBRE_BD, null, VERSION_BD);
		
	}


	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase arg0) {
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
		  
        arg0.execSQL("DROP TABLE IF EXISTS " + TABLA_PUNTUACIONES);
 
        
        onCreate(arg0);

	}
	
	
	 /**
	 * Gets the top10.
	 * 
	 * @return the top10
	 */
 	public Cursor getTop10() {
		   
		    String selectQuery = "SELECT  * FROM " + TABLA_PUNTUACIONES + " ORDER BY " + CAMPO_PUNTOS + " DESC LIMIT 10";
		 
		    SQLiteDatabase db = this.getWritableDatabase();
		    Cursor cursor = db.rawQuery(selectQuery, null);
		  
		   
		    return cursor;
		}
	 
	 
	 /**
	 * Gets the top10 lista.
	 * 
	 * @return the top10 lista
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
	 * Adds the contact.
	 * 
	 * @param puntuacion
	 *            the puntuacion
	 */
	public void addContact(Puntuacion puntuacion) {
	    SQLiteDatabase bd = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(CAMPO_NOMBRE, puntuacion.getNombre()); 
	    values.put(CAMPO_PUNTOS, puntuacion.getPuntos()); 
	 
	    
	    bd.insert(TABLA_PUNTUACIONES, null, values);
	    bd.close(); 
	}
	
	
	

}
