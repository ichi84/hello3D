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
	
	//マップの1マスを生成
	private static Vertices createMapPeace(GLGraphics glGraphics){
		float y0 = 1;
		float y1 = 1;
		float y2 = 1;
		float y3 = 1;
		
		float[] v={ //x,y,z,u,v
				  0f,  y0, 0f,  0, 0,  //天面：上から見て左上　
				  0f,  y1, 1f,  0, 1,  //天面：上から見て左下
				  1f,  y2, 1f,  1, 1,  //天面：上から見て右下
				  1f,  y3, 0f,  1, 0,  //天面：上から見て右上
/*
				  0f,  0f, 0f,  0, 0,  //底面：上から見て左上　
				  0f,  0f, 1f,  0, 1,  //底面：上から見て左下
				  1f,  0f, 1f,  1, 1,  //底面：上から見て右下
				  1f,  0f, 0f,  1, 0,  //底面：上から見て右上
*/
				  0f,  y0, 0f,  0, 0,  //左側面：左上
				  0f,  0f, 0f,  0, 1,  //左側面：左下
				  0f,  0f, 1f,  1, 1,  //左側面：右下
				  0f,  y1, 1f,  1, 0,  //左側面:右上
		
				  0f,  y1, 1f,  0, 0,  //下側面：左上
				  0f,  0f, 1f,  0, 1,  //下側面：左下
				  1f,  0f, 1f,  1, 1,  //下側面：右下
				  1f,  y2, 1f,  1, 0,  //下側面:右上
				  
				  1f,  y2, 1f,  0, 0,  //右側面：左上
				  1f,  0f, 1f,  0, 1,  //右側面：左下
				  1f,  0f, 0f,  1, 1,  //右側面：右下
				  1f,  y3, 0f,  1, 0,  //右側面:右上
		
				  1f,  y3, 0f,  0, 0,  //上側面：左上
				  1f,  0f, 0f,  0, 1,  //上側面：左下
				  0f,  0f, 0f,  1, 1,  //上側面：右下
				  0f,  y0, 0f,  1, 0,  //上側面:右上
		};
		
		//インデックスバッファ
		short[] i ={  0,  1,  2,  0,  2,  3,
				      4,  5,  6,  4,  6,  7,
				      8,  9, 10,  8, 10, 11,
				     12, 13, 14, 12, 14, 15,
				     16, 17, 18, 16, 18, 19,
		};
		//頂点クラス  、頂点20, インデックス30、カラーなし、テクスチャあり
				Vertices vertices = new Vertices(glGraphics, 20, 30, false, true);
				
		vertices.setVertices(v , 0, v.length);
		vertices.setIndices(i, 0, i.length);
		
		return vertices;
	}

}
