package net.d_ichi84.GL_test.game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import net.d_ichi84.GL_test.game.screen.MapScreen;
import net.d_ichi84.framework.Screen;
import net.d_ichi84.framework.gl.GLGame;



public class GLGameTest extends GLGame {
	boolean firstTimeCreate = true;

	public Screen getStartScreen() {
		return new MapScreen(this);
		//return new TestScreen(this);
	}
	

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		super.onSurfaceCreated(gl, config);
		if (firstTimeCreate) {		
			Assets.load(this);
			firstTimeCreate = false;
		} else {
			Assets.reload();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}
