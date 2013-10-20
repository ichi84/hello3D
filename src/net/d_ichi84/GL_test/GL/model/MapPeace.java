package net.d_ichi84.GL_test.GL.model;

import net.d_ichi84.framework.GameObject3D;

public class MapPeace extends GameObject3D{
	public final float[] vertexBuffer;
	public final float[] vertexRoofBuffer;
	public final int vertexNum;
	public final int vertexRoofNum;
	
	public final short[] indexBuffer;
	public final short[] indexRoofBuffer;
	public final int indexNum;
	public final int indexRoofNum;
	public final float height;
	
	public MapPeace(float x, float y, float z,
			float y0, float y1, float y2, float y3 , int IndexOffset, float xoffset, float zoffse){
		super(x, y, z);
	
		height = (y0+y1+y2+y3)/4.0f;
		//マップの1マスを生成
		this.vertexRoofBuffer = new float[]{
				  0f+xoffset,  y0, 0f+zoffse,  0, 0,  //天面：上から見て左上 0
				  0f+xoffset,  y1, -1f+zoffse,  0, 1,  //天面：上から見て左下  1
				  1f+xoffset,  y2, -1f+zoffse,  1, 1,  //天面：上から見て右下 2
				  1f+xoffset,  y3, 0f+zoffse,  1, 0,  //天面：上から見て右上 3	
		};
		
		//マップの1マスを生成
		this.vertexBuffer = new float[]{ //x,y,z,u,v
				  0f+xoffset,  y0, 0f+zoffse,  0, 0,  //左側面：左上  4
				  0f+xoffset,  0f, 0f+zoffse,  0, 1,  //左側面：左下 5
				  0f+xoffset,  0f, -1f+zoffse,  1, 1,  //左側面：右下 6
				  0f+xoffset,  y1, -1f+zoffse,  1, 0,  //左側面:右上 7

				  0f+xoffset,  y1, -1f+zoffse,  0, 0,  //下側面：左上 8
				  0f+xoffset,  0f, -1f+zoffse,  0, 1,  //下側面：左下 9
				  1f+xoffset,  0f, -1f+zoffse,  1, 1,  //下側面：右下 10
				  1f+xoffset,  y2, -1f+zoffse,  1, 0,  //下側面:右上 11
				  
				  1f+xoffset,  y2, -1f+zoffse,  0, 0,  //右側面：左上 12
				  1f+xoffset,  0f, -1f+zoffse,  0, 1,  //右側面：左下 13
				  1f+xoffset,  0f, 0f+zoffse,  1, 1,  //右側面：右下 14
				  1f+xoffset,  y3, 0f+zoffse,  1, 0,  //右側面:右上 15
		
				  1f+xoffset,  y3, 0f+zoffse,  0, 0,  //上側面：左上 16
				  1f+xoffset,  0f, 0f+zoffse,  0, 1,  //上側面：左下 17
				  0f+xoffset,  0f, 0f+zoffse,  1, 1,  //上側面：右下 18
				  0f+xoffset,  y0, 0f+zoffse,  1, 0,  //上側面:右上 19
		};
		
		//インデックスバッファ
		this.indexBuffer = new short[]{ 
				0,  1,  2,  0,  2,  3,
				4,  5,  6,  4,  6,  7,
				8,  9, 10,  8, 10, 11,
				12, 13, 14, 12, 14, 15,
		};
		
		
		this.indexRoofBuffer = new short[]{ 
				0,  1,  2,  0,  2,  3,
		};
		
		
		for(int i =0; i<indexBuffer.length; i++){
			indexBuffer[i] = (short) (indexBuffer[i] +IndexOffset*16);
		}
		
		for(int i =0; i<indexRoofBuffer.length; i++){
			indexRoofBuffer[i] = (short) (indexRoofBuffer[i] +IndexOffset*4);
		}
		
		vertexNum = vertexBuffer.length;
		vertexRoofNum = vertexRoofBuffer.length;
		indexNum = indexBuffer.length;
		indexRoofNum = indexRoofBuffer.length;
	}
}
