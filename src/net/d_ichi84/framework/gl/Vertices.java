package net.d_ichi84.framework.gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;


//頂点を保持するクラス
public class Vertices {
	final GLGraphics 	glGraphics;
	final boolean		hasColor;
	final boolean		hasTexCoords;
	final int			vertexSize;
	final IntBuffer		vertices;
	final ShortBuffer	indices;
	final int[]			tmpBuffer;
	
	//コンストラクタ
	public Vertices(GLGraphics glGraphics, int maxVertices, int maxIndices, boolean hasColor, boolean hasTexCoords){
		this.glGraphics = glGraphics;
		this.hasColor = hasColor;
		this.hasTexCoords = hasTexCoords;
		this.vertexSize = (3 + (hasColor?4:0) + (hasTexCoords?2:0) ) *4;
		ByteBuffer buffer = ByteBuffer.allocateDirect(maxVertices * vertexSize);
		buffer.order(ByteOrder.nativeOrder());
		this.tmpBuffer = new int[maxVertices * vertexSize *4];
		vertices = buffer.asIntBuffer();
		
		if(maxIndices>0){
			buffer = ByteBuffer.allocateDirect(maxIndices * Short.SIZE / 8);
			buffer.order(ByteOrder.nativeOrder());
			indices = buffer.asShortBuffer();
		}else{
			indices = null;
		}
	}
	
	public void setVertices(float[] vertices, int offset, int length){
		this.vertices.clear();
		int len = offset + length;
		for(int i = offset, j=0; i<len; i++, j++){ //floatBufferのバグ対策
			tmpBuffer[j] = Float.floatToRawIntBits(vertices[i]);
		}
		
		this.vertices.put(tmpBuffer, 0, length);
		this.vertices.flip();
	}
	
	public void setIndices(short[] indices, int offset, int length){
		this.indices.clear();
		this.indices.put(indices, offset, length);
		this.indices.flip();
	}
	
	public void draw(int primitiveType, int offset, int numVertices){
		GL10 gl = glGraphics.gl;
	
		//インデックスバッファ
		if(indices!=null){
			indices.position(offset);
			gl.glDrawElements(primitiveType, numVertices, GL10.GL_UNSIGNED_SHORT, indices);
		}else{
			gl.glDrawArrays(primitiveType, offset, numVertices);
		}
	
	}
	
	public void bind(){
		GL10 gl = glGraphics.gl;
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		vertices.position(0);
		gl.glVertexPointer(3, GL10.GL_FLOAT, vertexSize, vertices);
		
		//カラーがあるとき
		if(hasColor){
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			vertices.position(3);
			gl.glColorPointer(4, GL10.GL_FLOAT, vertexSize, vertices);
		}
		
		//テクスチャあるとき
		if(hasTexCoords){
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			vertices.position( hasColor?7:3 );
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, vertexSize, vertices);
		}
	}
	
	public void unbind(){
		GL10 gl = glGraphics.gl;
		
		//使用済みのは元に戻しておく
		if(hasTexCoords){
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		}
		if(hasColor){
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		}
	}
	
	public int getNumIndices() {
		return indices.limit();
	}
	
	public int getNumVertices() {
		return vertices.limit() / (vertexSize / 4);
	}
}
