package net.d_ichi84.GL_test.game.screen;

import javax.microedition.khronos.opengles.GL10;


import android.opengl.GLU;

import net.d_ichi84.framework.Game;
import net.d_ichi84.framework.Screen;
import net.d_ichi84.framework.android.AndroidFileIO;
import net.d_ichi84.framework.gl.FPSCounter;
import net.d_ichi84.framework.gl.GLGame;
import net.d_ichi84.framework.gl.GLGraphics;
import net.d_ichi84.framework.gl.Texture;
import net.d_ichi84.framework.gl.Vertices;



public class TestScreen3D extends Screen{
	GLGraphics glGraphics;
	Texture texture;
	Vertices cube;
	float angle= 0;
	
	FPSCounter fpsCounter = new FPSCounter();
	
	public TestScreen3D(Game game){
		super(game);
		glGraphics = ((GLGame) game).getGLGraphics();
		
		cube = createCube();
		
		texture = new Texture(
				glGraphics,
				new AndroidFileIO(context.getAssets()),
				"neko_ss.jpg");
	}

	public Vertices createCube(){	
		float[] v={ //x,y,z
				 -0.5f,  -0.5f, 0.5f, 0, 1,
				  0.5f,  -0.5f, 0.5f, 1, 1,
				  0.5f,   0.5f, 0.5f, 1, 0,
				 -0.5f,   0.5f, 0.5f, 0, 0,
				 
				  0.5f,  -0.5f,  0.5f, 0, 1,
				  0.5f,  -0.5f, -0.5f, 1, 1,
				  0.5f,   0.5f, -0.5f, 1, 0,
				  0.5f,   0.5f,  0.5f, 0, 0,
		
				  0.5f,  -0.5f, -0.5f, 0, 1,
				 -0.5f,  -0.5f, -0.5f, 1, 1,
				 -0.5f,   0.5f, -0.5f, 1, 0,
				  0.5f,   0.5f, -0.5f, 0, 0,
				  
				 -0.5f,  -0.5f, -0.5f, 0, 1,
				 -0.5f,  -0.5f,  0.5f, 1, 1,
				 -0.5f,   0.5f,  0.5f, 1, 0,
				 -0.5f,   0.5f, -0.5f, 0, 0,
				 
				 -0.5f,   0.5f,  0.5f, 0, 1,
				  0.5f,   0.5f,  0.5f, 1, 1,
				  0.5f,   0.5f, -0.5f, 1, 0,
				 -0.5f,   0.5f, -0.5f, 0, 0,
				 
				 -0.5f,  -0.5f,  0.5f, 0, 1,
				  0.5f,  -0.5f,  0.5f, 1, 1,
				  0.5f,  -0.5f, -0.5f, 1, 0,
				 -0.5f,  -0.5f, -0.5f, 0, 0,
				};
		//頂点クラス  、頂点24, インデックス36、カラーなし、テクスチャあり
		Vertices vertices = new Vertices(glGraphics, 24, 36, false, true);
		
		//インデックスバッファ
		short[] i ={ 0, 1, 3, 1, 2, 3,
				     4, 5, 7, 5, 6, 7,
				     8, 9,11, 9,10,11,
				     12,13,15,13,14,15,
				     16,17,19,17,18,19,
				     20,21,23,21,22,23};
		
		vertices.setVertices(v , 0, v.length);
		vertices.setIndices(i, 0, i.length);
		
		return vertices;
	}
	
	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.gl;

		gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
		gl.glClearColor(0f, 0f, 1f, 1f); //青
		
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT |	//塗りつぶし
				   GL10.GL_DEPTH_BUFFER_BIT);	//Zバッファ消去

		//投影行列
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(
				gl, 67, 
				glGraphics.getWidth()/(float)glGraphics.getHeight(),
				0.1f, 10f);
		
		
		//モデル座標系
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		texture.bind();
		
		cube.bind();
		gl.glTranslatef(0, 0, -3); //平行移動
		gl.glTranslatef(0, -1, 0);  //平行移動
		gl.glRotatef(angle, 0, 1, 0);
		gl.glRotatef(45, 1, 0, 0);
		cube.draw(GL10.GL_TRIANGLES, 0, 36);	
		cube.unbind();
		
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		
		fpsCounter.logFrame();
	}
	
	@Override
	public void update(float deltaTime) {
		angle +=45*deltaTime;
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void resume() {
		//テクスチャ
		texture.reload();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}