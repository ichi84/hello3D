package net.d_ichi84.framework.android;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import net.d_ichi84.framework.android.FileIO;


public class Image {
	FileIO fileIO;
	String FileName;
	int textureID;
	int minFilter;
	int magFilter;
	
	public int width;
	public int height;
	public Bitmap bitmap;
	
	public Image( FileIO fileIO, String Filename){
		this.FileName = Filename;
		this.fileIO = fileIO;
		load();
	}
	
	private void load(){
		int[] textureIDs = new int[1];
		textureID = textureIDs[0];
		
		InputStream in = null;
		try{
			in = fileIO.readAsset(FileName);
			bitmap =BitmapFactory.decodeStream(in);
			width = bitmap.getWidth();
            height = bitmap.getHeight();
            Log.d("Touch", " " +  bitmap.getConfig() );
		}catch(Exception e){
			throw new RuntimeException("Couldn't load bitmap:" + FileName,e);
		}finally{
			if(in != null){
				try{
					in.close();
				}catch(Exception e){}
			}
		}
	}
	
}
