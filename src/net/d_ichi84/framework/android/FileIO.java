package net.d_ichi84.framework.android;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileIO {
	public InputStream readAsset(String FileName) throws IOException;
	public InputStream readFile(String FileName) throws IOException;
	public OutputStream writeFile(String FileName) throws IOException;
}
