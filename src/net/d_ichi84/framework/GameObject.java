package net.d_ichi84.framework;

import net.d_ichi84.framework.math.Rectangle;
import net.d_ichi84.framework.math.Vector2;

public class GameObject {
	public final Vector2 position;
	public final Rectangle bounds;

	public GameObject(float x, float y, float width, float height) {
		this.position = new Vector2(x,y);
		this.bounds = new Rectangle(x-width/2, y-height/2, width, height);
	}
}