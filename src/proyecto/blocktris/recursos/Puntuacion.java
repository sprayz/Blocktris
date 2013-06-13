/*
 *  @author Pablo Morillas Lozano
 */
package proyecto.blocktris.recursos;


/**
 * ESta clase representa una fila de la tabla de puntuaciones
 * 
 * @see BDPuntuaciones
 */
public class Puntuacion {
		
		
		private int id;
		
		
		private String nombre;
		
		
		private int puntos;
		
		
		
		public int getId() {
			return id;
		}
		
		public void setId(int id) {
			this.id = id;
		}
		
		
		public String getNombre() {
			return nombre;
		}
		
		
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		
		public int getPuntos() {
			return puntos;
		}
		
		
		public void setPuntos(int puntos) {
			this.puntos = puntos;
		}
		
		
		
}
