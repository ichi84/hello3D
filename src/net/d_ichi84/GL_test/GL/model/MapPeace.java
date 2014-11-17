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
		//�}�b�v��1�}�X�𐶐�
		this.vertexRoofBuffer = new float[]{
				  0f+xoffset,  y0, 0f+zoffse,  0, 0,  //�V�ʁF�ォ�猩�č��� 0
				  0f+xoffset,  y1, -1f+zoffse,  0, 1,  //�V�ʁF�ォ�猩�č���  1
				  1f+xoffset,  y2, -1f+zoffse,  1, 1,  //�V�ʁF�ォ�猩�ĉE�� 2
				  1f+xoffset,  y3, 0f+zoffse,  1, 0,  //�V�ʁF�ォ�猩�ĉE�� 3	
		};
		
		//�}�b�v��1�}�X�𐶐�
		this.vertexBuffer = new float[]{ //x,y,z,u,v
				  0f+xoffset,  y0, 0f+zoffse,  0, 0,  //�����ʁF����  4
				  0f+xoffset,  0f, 0f+zoffse,  0, 1,  //�����ʁF���� 5
				  0f+xoffset,  0f, -1f+zoffse,  1, 1,  //�����ʁF�E�� 6
				  0f+xoffset,  y1, -1f+zoffse,  1, 0,  //������:�E�� 7

				  0f+xoffset,  y1, -1f+zoffse,  0, 0,  //�����ʁF���� 8
				  0f+xoffset,  0f, -1f+zoffse,  0, 1,  //�����ʁF���� 9
				  1f+xoffset,  0f, -1f+zoffse,  1, 1,  //�����ʁF�E�� 10
				  1f+xoffset,  y2, -1f+zoffse,  1, 0,  //������:�E�� 11
				  
				  1f+xoffset,  y2, -1f+zoffse,  0, 0,  //�E���ʁF���� 12
				  1f+xoffset,  0f, -1f+zoffse,  0, 1,  //�E���ʁF���� 13
				  1f+xoffset,  0f, 0f+zoffse,  1, 1,  //�E���ʁF�E�� 14
				  1f+xoffset,  y3, 0f+zoffse,  1, 0,  //�E����:�E�� 15
		
				  1f+xoffset,  y3, 0f+zoffse,  0, 0,  //�㑤�ʁF���� 16
				  1f+xoffset,  0f, 0f+zoffse,  0, 1,  //�㑤�ʁF���� 17
				  0f+xoffset,  0f, 0f+zoffse,  1, 1,  //�㑤�ʁF�E�� 18
				  0f+xoffset,  y0, 0f+zoffse,  1, 0,  //�㑤��:�E�� 19
		};
		
		//�C���f�b�N�X�o�b�t�@
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
