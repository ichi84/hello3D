package net.d_ichi84.framework.android;

import net.d_ichi84.framework.android.Graphics.PixmapFormat;

public interface Pixmap {
    public int getWidth();
 
    public int getHeight();
    
    public PixmapFormat getFormat();
    
    public void dispose();
}
