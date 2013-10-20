package net.d_ichi84.framework.gl;

import javax.microedition.khronos.opengles.GL10;

public class SpriteBatcher {
	GLGraphics glGraphics;
	final float[] verticesBuffer;
	int bufferIndex;
	final Vertices2D vertices;
	int numSprite;
	public SpriteBatcher(GLGraphics glGraphics, int maxSprite){
		this.glGraphics = glGraphics;
		this.verticesBuffer = new float[maxSprite*4*4];
		
		//頂点数スプライトの数*4、インデクス数スプライト数*6、色なし、テクスチャ有り
		this.vertices = new Vertices2D(glGraphics, maxSprite*6, maxSprite*6, false,true);
		
		this.bufferIndex = 0;
		this.numSprite = 0;
		
		//インデックスバッファを作って埋める
		short[] indices = new short[maxSprite *6];
		int len = indices.length;
		short j =0;
		for(int i=0; i <len; i+=6, j+=4){
			indices[i + 0] = (short)(j+0);
			indices[i + 1] = (short)(j+1);
			indices[i + 2] = (short)(j+2);
			indices[i + 3] = (short)(j+2);
			indices[i + 4] = (short)(j+3);
			indices[i + 5] = (short)(j+0);
		}
		vertices.setIndices(indices, 0, indices.length);
	}
	
	public void beginBatch(Texture texture){
		texture.bind();
		numSprite = 0;
		bufferIndex =0;
	}
	public void endBatch(){
	
		vertices.setVertices(verticesBuffer, 0, bufferIndex);
		vertices.bind();
		
		vertices.draw(GL10.GL_TRIANGLES, 0, numSprite*6);
		vertices.unbind();
	}
	
	public void drawSprite(float x, float y,float z, float width, float height, TextureRegion region){
		
		float halfWidth = width/2f;
	//	float halfHeight = height/2f;
		
		float x1 = x - halfWidth;
		float y1 = y - y;
		float z1 = z;
		
		float x2 = x + halfWidth;
		float y2 = y + height;

		//頂点とUVマッピング
		verticesBuffer[bufferIndex++] = x1; 	//左上
		verticesBuffer[bufferIndex++] = y1;
		verticesBuffer[bufferIndex++] = z1;
		verticesBuffer[bufferIndex++] = region.u1;
		verticesBuffer[bufferIndex++] = region.v2;

		verticesBuffer[bufferIndex++] = x2;		//右上
		verticesBuffer[bufferIndex++] = y1;
		verticesBuffer[bufferIndex++] = z1;
		verticesBuffer[bufferIndex++] = region.u2;
		verticesBuffer[bufferIndex++] = region.v2;

		verticesBuffer[bufferIndex++] = x2;		//右下
		verticesBuffer[bufferIndex++] = y2;
		verticesBuffer[bufferIndex++] = z1;
		verticesBuffer[bufferIndex++] = region.u2;
		verticesBuffer[bufferIndex++] = region.v1;

		verticesBuffer[bufferIndex++] = x1;		//左下
		verticesBuffer[bufferIndex++] = y2;
		verticesBuffer[bufferIndex++] = z1;
		verticesBuffer[bufferIndex++] = region.u1;
		verticesBuffer[bufferIndex++] = region.v1;
		
		numSprite++;
	}
}
