package net.d_ichi84.GL_test.game;

import net.d_ichi84.framework.android.AndroidFileIO;
import net.d_ichi84.framework.gl.GLGame;
import net.d_ichi84.framework.gl.GLGraphics;
import net.d_ichi84.framework.gl.Texture;
import net.d_ichi84.framework.gl.Vertices;

public class Assets {
	public static Texture map1Texture;
	public static Texture maptestTexture;
	public static Texture player1Texture;
	
	public static Vertices mapPeace;
	
	public static void load(GLGame game){
		map1Texture = new Texture(
				game.getGLGraphics(),
				new AndroidFileIO(GLGame.context.getAssets()),
				"floor.jpg");
		
		maptestTexture = new Texture(
				game.getGLGraphics(),
				new AndroidFileIO(GLGame.context.getAssets()),
				"floorTest.jpg");
		
		player1Texture = new Texture(
				game.getGLGraphics(),
				new AndroidFileIO(GLGame.context.getAssets()),
				"player_move.png");
		
		
		mapPeace = createMapPeace( game.getGLGraphics() );
	}
	
	public static void reload(){
		map1Texture.reload();
	}
	
	//�}�b�v��1�}�X�𐶐�
	private static Vertices createMapPeace(GLGraphics glGraphics){
		float y0 = 1;
		float y1 = 1;
		float y2 = 1;
		float y3 = 1;
		
		float[] v={ //x,y,z,u,v
				  0f,  y0, 0f,  0, 0,  //�V�ʁF�ォ�猩�č���@
				  0f,  y1, 1f,  0, 1,  //�V�ʁF�ォ�猩�č���
				  1f,  y2, 1f,  1, 1,  //�V�ʁF�ォ�猩�ĉE��
				  1f,  y3, 0f,  1, 0,  //�V�ʁF�ォ�猩�ĉE��
/*
				  0f,  0f, 0f,  0, 0,  //��ʁF�ォ�猩�č���@
				  0f,  0f, 1f,  0, 1,  //��ʁF�ォ�猩�č���
				  1f,  0f, 1f,  1, 1,  //��ʁF�ォ�猩�ĉE��
				  1f,  0f, 0f,  1, 0,  //��ʁF�ォ�猩�ĉE��
*/
				  0f,  y0, 0f,  0, 0,  //�����ʁF����
				  0f,  0f, 0f,  0, 1,  //�����ʁF����
				  0f,  0f, 1f,  1, 1,  //�����ʁF�E��
				  0f,  y1, 1f,  1, 0,  //������:�E��
		
				  0f,  y1, 1f,  0, 0,  //�����ʁF����
				  0f,  0f, 1f,  0, 1,  //�����ʁF����
				  1f,  0f, 1f,  1, 1,  //�����ʁF�E��
				  1f,  y2, 1f,  1, 0,  //������:�E��
				  
				  1f,  y2, 1f,  0, 0,  //�E���ʁF����
				  1f,  0f, 1f,  0, 1,  //�E���ʁF����
				  1f,  0f, 0f,  1, 1,  //�E���ʁF�E��
				  1f,  y3, 0f,  1, 0,  //�E����:�E��
		
				  1f,  y3, 0f,  0, 0,  //�㑤�ʁF����
				  1f,  0f, 0f,  0, 1,  //�㑤�ʁF����
				  0f,  0f, 0f,  1, 1,  //�㑤�ʁF�E��
				  0f,  y0, 0f,  1, 0,  //�㑤��:�E��
		};
		
		//�C���f�b�N�X�o�b�t�@
		short[] i ={  0,  1,  2,  0,  2,  3,
				      4,  5,  6,  4,  6,  7,
				      8,  9, 10,  8, 10, 11,
				     12, 13, 14, 12, 14, 15,
				     16, 17, 18, 16, 18, 19,
		};
		//���_�N���X  �A���_20, �C���f�b�N�X30�A�J���[�Ȃ��A�e�N�X�`������
				Vertices vertices = new Vertices(glGraphics, 20, 30, false, true);
				
		vertices.setVertices(v , 0, v.length);
		vertices.setIndices(i, 0, i.length);
		
		return vertices;
	}

}
