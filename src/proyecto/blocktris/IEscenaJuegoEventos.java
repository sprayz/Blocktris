package proyecto.blocktris;

import java.util.Collection;

import proyecto.blocktris.logica.fisica.piezas.rompibles.PiezaBase.Bloque;

public interface IEscenaJuegoEventos {
	
	public boolean onIniciarPartida();
	public void onPartidaIniciada();
	public boolean onFinalizarPartida();
	public void  onPartidaFinalizada();
	public boolean onQuitarBloque(final Bloque bloque) ;
	public  boolean onQuitarLinea(Collection<Bloque> bloques);
	public void onLineaQuitada();
	
	
}
