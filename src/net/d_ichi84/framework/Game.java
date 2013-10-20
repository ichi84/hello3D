package net.d_ichi84.framework;


import net.d_ichi84.framework.android.Audio;
import net.d_ichi84.framework.android.FileIO;
import net.d_ichi84.framework.android.Graphics;
import net.d_ichi84.framework.android.Input;

public interface Game {
	public Input getInput();
	public FileIO getFileIO();
	public Graphics getGraphics();
	public Audio getAudio();
	public void setScreen(Screen screen);
	public Screen getCurrentScreen();
	public Screen getStartScreen();
}
