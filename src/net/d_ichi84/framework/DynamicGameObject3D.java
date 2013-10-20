package net.d_ichi84.framework;

import net.d_ichi84.framework.math.Vector3;


public class DynamicGameObject3D extends GameObject3D {
	public final Vector3 velocity;
	public final Vector3 accel;

	public DynamicGameObject3D(float x, float y, float z) {
		super(x, y, z);
		velocity = new Vector3();
		accel = new Vector3();
	}		
}

