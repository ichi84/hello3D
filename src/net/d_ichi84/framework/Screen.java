package net.d_ichi84.framework;

import net.d_ichi84.framework.gl.GLGame;
import android.content.Context;

public abstract class Screen {
	protected final Game game;
	protected Context context;
	public Screen(Game game){
		this.game = game;
		context = (GLGame)game;
	}
	public abstract void update(float deltaTime);
	public abstract void present(float deltaTime);
	public abstract void pause();
	public abstract void resume();
	public abstract void dispose();
}
