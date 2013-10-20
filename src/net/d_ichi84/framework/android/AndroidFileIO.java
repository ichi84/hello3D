package net.d_ichi84.framework.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import android.content.res.AssetManager;
import android.os.Environment;

public class AndroidFileIO implements FileIO {
	AssetManager assets;
	String externalStragePath;
	
	public AndroidFileIO(AssetManager assets){
		this.assets = assets;
		this.externalStragePath = 
				Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
	}
	

	public InputStream readAsset(String FileName) throws IOException {
		return assets.open(FileName);
	}

	public InputStream readFile(String FileName) throws IOException {
		return new FileInputStream(externalStragePath + FileName);
	}

	public OutputStream writeFile(String FileName) throws IOException {
		return new FileOutputStream(externalStragePath + FileName);
	}

}
