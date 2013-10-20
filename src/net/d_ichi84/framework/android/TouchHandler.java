package net.d_ichi84.framework.android;

import java.util.List;

import net.d_ichi84.framework.android.Input.TouchEvent;


import android.view.View.OnTouchListener;


public interface TouchHandler extends OnTouchListener {
	public boolean isTouchDown(int pointer);
	public int getTouchX(int pointer);
	public int getTouchY(int pointer);
	public List<TouchEvent> getTouchEvents();
}

