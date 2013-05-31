package proyecto.blocktris.logica.fisica;

import org.andengine.entity.IEntity;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.util.math.MathUtils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class ExtendedPhysicsConnector extends PhysicsConnector {
	private float mAnchorX;
	private float mAnchorY;

	public ExtendedPhysicsConnector(IEntity pAreaShape, Body pBody,
			boolean pUdatePosition, boolean pUpdateRotation, float anchorX,
			float anchorY) {
		super(pAreaShape, pBody, pUdatePosition, pUpdateRotation);
		mAnchorX = anchorX;
		mAnchorY = anchorY;
	}

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		final IEntity entity = this.mEntity;
		final Body body = this.mBody;

		if (this.mUpdatePosition) {
			final Vector2 position = body.getPosition();
			final float pixelToMeterRatio = this.mPixelToMeterRatio;
			entity.setPosition(position.x * pixelToMeterRatio - mAnchorX,
					position.y * pixelToMeterRatio - mAnchorY);
		}

		if (this.mUpdateRotation) {
			final float angle = body.getAngle();
			entity.setRotation(MathUtils.radToDeg(angle));
		}
	}
}