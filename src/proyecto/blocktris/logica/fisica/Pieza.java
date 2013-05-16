package proyecto.blocktris.logica.fisica;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import proyecto.blocktris.recursos.ManagerRecursos;


import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;


public class Pieza extends ObjetoFisico {

	public static enum PIEZAS {
		PIEZA_T(new boolean[][] 
				{ { true, false, false, false }, 
				{ 	true, true, false, false },
				  { true, false, false, false },
				  { false, false, false, false } }
						),

		PIEZA_CUBO(new boolean[][] 
				{ { true, true, false, false }, 
				{ 	true, true, false, false },
				  { false, false, false, false },
				  { false, false, false, false } }

		), PIEZA_PALO(new boolean[][] 
				{ { true, true, true, true }, 
				{ 	false, false, false, false },
				  { false, false, false, false },
				  { false, false, false, false } }

		), PIEZA_ELE1(new boolean[][] 
				{ { true, false, false, false }, 
				{ 	true, true, true, false },
				 {  false, false, false, false },
				  { false, false, false, false } }
             ),
	       PIEZA_ELE2 (new boolean[][] 
				{ { true, true, true, false }, 
	    		   {true, false, false, false },
				   {false, false, false, false },
				  { false, false, false, false } }

				), 
		PIEZA_LLAVE1(new boolean[][]
				{ { true, true, false, false }, 
				{ 	false, true, true, false },
				  { false, false, false, false },
				  { false, false, false, false } }


		), 
		PIEZA_LLAVE2(new boolean[][] 
				{ { false, true, true, false }, 
				{ 	true, true, false, false },
				  { false, false, false, false },
				  { false, false, false, false } }
		);

		private static final List<PIEZAS> VALUES = Collections
				.unmodifiableList(Arrays.asList(values()));
		private static final int tamaño = VALUES.size();
		private static final Random RANDOM = new Random();

		public static PIEZAS piezaAleatoria() {
			return VALUES.get(RANDOM.nextInt(tamaño));
		}

		PIEZAS(boolean estructura[][]) {
			this.estructura = estructura;

		}

		public boolean[][] getEstructura() {
			return estructura;
		}

		private final boolean estructura[][];

	}


	
	
	private static FixtureDef fixturedef = new FixtureDef();
	private static BodyDef bodydef = new BodyDef();
	private TiledSprite[] graficos = new TiledSprite[16];
	private PIEZAS tipo;
	
	/*Tenemos que hacer un jodido cuerpo compuesto a base de fixtures
	 * La gracia está en que al no poder conectar sprites a las fixtures tenemos que
	 * conectar todos los sprites al cuerpo y
	 * utilizar el  ancla para posicionarlos acorde respecto al centro  como si
	 * estuviesen atadas a  las conrespondientes fixtures.
	 * 
	 *  
	 *  
	 *  PORQUE NO SE PUEDE CREAR UN PHYSICS CONNECTOR QUE ACTUALICE LOS SPRITE CON LAS FIXTURES!?!
	 *  
	 *  ESTA JODIDA MANERA ES LA "RECOMENDADA". FML.
	 */
	
	
	 public Pieza(float tamaño_bloque, PIEZAS tipo,PhysicsWorld mundo) {
		 fixturedef.friction = 0.5f;
		 fixturedef.restitution = 1f; //elasticidad (rebote)
		 fixturedef.density =1f;
		 fixturedef.isSensor = false; //los sensores registran eventos pero no actúan sobre ellos.
		 
		 bodydef.active = true; //obviamente empezamos activos
		 bodydef.allowSleep =false; // en permanente simulación
		bodydef.type =BodyType.DynamicBody ;
		 //las "balas" no atraviesan  otros objetos móviles por brutas que sean las fuerzas
		 //incrementea la precisión de la simulación a costa de bastante rendimiento
		 bodydef.bullet = false; 
		 
		 this.cuerpo = mundo.createBody(bodydef);
		 this.tipo = tipo;
		
		 
		 float tamaño_bloque_fisico = tamaño_bloque / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		 
		 PolygonShape forma; //COLORFORMAS!!!!! :D
		
		 Fixture fixture;
		 
		 TiledSprite sprite;
		 float[] coordsCentroLocalSprite = null ;
		 
		 //las definiciones de las piezas son cuadrículas imaginarias de 4x4
		 for (int y = 0,cuenta=0; y < tipo.getEstructura().length; y++) {
				for (int x = 0; x < tipo.getEstructura()[0].length; x++) {
					if(tipo.getEstructura()[x][y] == true || true == true){
						
						/*
						 * esta es la parte interesante.El yo pasado agradecera que prestes atencion.
						 * 
						 * Básicamente tenemos que definir el polígono de manera que quede desplazado respecto 
						 * al centro del cuerpo.Después aplicamos el mismo desplazamiento a el sprite correspondiente
						 * haciendo parecer que, el sprite y la fixture esan unidos el uno al otro (coinciden).
						 * 
						 * Screw you Andengine!
						 */
					
						//forma poligonal, normalmente hechas de un array de vértices
						forma = new PolygonShape();
						
						//Pero esta función de utilidad nos permite hacer rectángulos(cajas) sin meter los
						//vértices a mano
						
						//ajustamos el desplazamiento basándonos en  donde nos situamos en la 
						//cuadricula imaginaria  con un Vector2 de la reserva
						// le restamos  la mitad de el tamaño de la cuadricula para  que el centro
						//del cuerpo  quede en el centro de la misma.
						
						//y le sumamos la mitad del tamaño físico de un bloque por que el ancla inicial esta en ela esquina izquierda inferior
						// y no en el centro como en el motor grafico
						Vector2 centro = Vector2Pool.obtain(((x-tipo.getEstructura().length/2)*tamaño_bloque_fisico) + tamaño_bloque_fisico /2 ,
															((y- tipo.getEstructura()[0].length /2) * tamaño_bloque_fisico) +tamaño_bloque_fisico /2  );
						
				
						//sin rotación respecto al cuerpo
						forma.setAsBox( tamaño_bloque_fisico /2, tamaño_bloque_fisico/2,centro,0f); 
						fixturedef.shape=forma;
						//añadimos la fixture al cuerpo y rezamos apra que todo haya ido bien 
						fixture= this.cuerpo.createFixture(fixturedef);
						
						sprite = new TiledSprite(0,0, tamaño_bloque,tamaño_bloque, ManagerRecursos.getInstancia().trBloques.deepCopy(),ManagerRecursos.getInstancia().vbom );
						
							coordsCentroLocalSprite[0] +=	(x-tipo.getEstructura().length/2 );
							coordsCentroLocalSprite[1] +=	(y- tipo.getEstructura()[0].length /2);
						
						
						//dios que función más obtusa	
						//Según parece el anchorcenter se  especifica en  relacion con el tamaño de  el sprite
						//Es decir  el valor por defecto en esta rama ,AnchorCenter(adivinas por qué?) , 0.5 
							//coloca en ancla en la mitad del sprite
							
						//Ya rozaba los lindes de la locura cuando me dió por mirar la definición de
						//este método. Por lo visto incluso los jodidos comentarios sobran cuando el código es,
						//según esta panda de  patanes, "suficientemente descriptivo" o " autodocumentado"
						sprite.setAnchorCenter(coordsCentroLocalSprite[0],coordsCentroLocalSprite[1]);
 						
						
						
						Log.i("FIGURA SPRITE", sprite.getOffsetCenterX() + "  " +sprite.getOffsetCenterY());
						mundo.registerPhysicsConnector(new PhysicsConnector( sprite, cuerpo));
						graficos[cuenta] = sprite;
						
						//Vector2Pool.recycle(centro);
						
						
						
						
						
						
						
						cuenta ++;
					}
					
				}
			}
		 
		 
		 
		 
	}
	 public void adjuntarGraficos(Entity entidad){
		 for (TiledSprite s : graficos)
			 entidad.attachChild(s);
			 
		 
	 }
	 

}
