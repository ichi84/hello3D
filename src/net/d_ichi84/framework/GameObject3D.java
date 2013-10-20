package net.d_ichi84.framework;

import net.d_ichi84.framework.math.Vector3;

public class GameObject3D {
	public final Vector3 position;
//	public final Sphere bounds;

	public GameObject3D(float x, float y, float z) {
		this.position = new Vector3(x,y,z);
//		this.bounds = new Sphere(x, y, z, radius);
	}
}
