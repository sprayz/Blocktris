/*
 *  @author Pablo Morillas Lozano
 */
package proyecto.blocktris.logica.fisica;





import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * Clase estática que contiene métodos auxiliares sobre el motor box2d.
 */
public final class Utilidades {

	
	
	
	
	
	
	
	
	
	/**
	 * Body a BodyDef.
	 * 
	 * @param original
	 *            el original
	 * @return un {@link BodyDef} con las carácteristicas actuales del  {@link Body} original
	 */
	public static BodyDef bodyToDef(Body original){
		BodyDef bdef = new BodyDef();
		
		
		bdef.active = original.isActive();
		bdef.angle=original.getAngle();
		bdef.allowSleep = original.isSleepingAllowed();
		bdef.awake = original.isAwake();
		bdef.bullet = original.isBullet();
		bdef.angularDamping= original.getAngularDamping();
		bdef.angularVelocity = original.getAngularVelocity();
		bdef.fixedRotation = original.isFixedRotation();
		bdef.linearDamping = original.getLinearDamping();
		bdef.angularDamping = original.getAngularDamping();
		bdef.linearVelocity.set( original.getLinearVelocity());
		bdef.position.set(original.getPosition());
		bdef.type = original.getType();
		
		
		return bdef;
	}
	
	/**
	 * Fixture a FixtureDef.
	 * 
	 * @param original
	 *            el original
	 * @return un {@link FixtureDef} con las carácteristicas actuales del  {@link Fixture} original
	 */
	public static FixtureDef fixtureToDef(Fixture original){
		FixtureDef fdef = new FixtureDef();
		fdef.density = original.getDensity();
		fdef.filter.categoryBits = original.getFilterData().categoryBits;
		fdef.filter.groupIndex = original.getFilterData().groupIndex;
		fdef.filter.maskBits = original.getFilterData().maskBits;
		
		fdef.friction = original.getFriction();
		fdef.isSensor = original.isSensor();
		fdef.restitution = original.getRestitution();
		/*
		 * Copiamos la lista de vértices
		 */
		PolygonShape forma = new PolygonShape();
		Vector2[] vertices = new Vector2[((PolygonShape)original.getShape()).getVertexCount()];
		for( int i= 0 ;i < vertices.length ; i++ ){
			vertices[i]= new Vector2();
			((PolygonShape)original.getShape()).getVertex(i, vertices[i]);
			
		}
		forma.set(vertices);
		fdef.shape = forma ;
		

		return fdef;
		
	}
}
