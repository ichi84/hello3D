package net.d_ichi84.framework.gl;

import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

import net.d_ichi84.framework.android.FileIO;


public class Texture {
	GLGraphics glGraphics;
	FileIO fileIO;
	String FileName;
	int textureID;
	int minFilter;
	int magFilter;
	
	public int width;
	public int height;
	    
	public Texture(GLGraphics glGraphics, FileIO fileIO, String Filename){
		this.glGraphics = glGraphics;
		this.FileName = Filename;
		this.fileIO = fileIO;
		load();
	}
	
	private void load(){
		GL10 gl = glGraphics.gl;
		int[] textureIDs = new int[1];
		gl.glGenTextures(1, textureIDs, 0);
		textureID = textureIDs[0];
		
		InputStream in = null;
		try{
			in = fileIO.readAsset(FileName);
			Bitmap bitmap =BitmapFactory.decodeStream(in);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			setFilters(GL10.GL_NEAREST, GL10.GL_NEAREST);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
			width = bitmap.getWidth();
            height = bitmap.getHeight();
            Log.d("Touch", " " +  bitmap.getConfig() );
            bitmap.recycle();
		}catch(Exception e){
			throw new RuntimeException("Couldn't load texture:" + FileName,e);
		}finally{
			if(in != null){
				try{
					in.close();
				}catch(Exception e){}
			}
		}
	}
	
	public void reload(){
		load();
		bind();
		setFilters(minFilter, magFilter);
		glGraphics.gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
	}
	
	public void setFilters(int minFilter, int magFilter){
		this.minFilter = minFilter;
		this.magFilter = magFilter;
		GL10 gl = glGraphics.gl;
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, minFilter);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, magFilter);
	}

	public void bind(){
		GL10 gl = glGraphics.gl;
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID);
	}
	public void dispose(){
		GL10 gl = glGraphics.gl;
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID);
		int[] textureIDs = {textureID};
		gl.glDeleteTextures(1, textureIDs, 0);
	}
}
