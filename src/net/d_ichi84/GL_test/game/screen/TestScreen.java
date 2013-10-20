package net.d_ichi84.GL_test.game.screen;

import javax.microedition.khronos.opengles.GL10;


import net.d_ichi84.GL_test.GL.model.TipNeko;
import net.d_ichi84.framework.Game;
import net.d_ichi84.framework.Screen;
import net.d_ichi84.framework.android.AndroidFileIO;
import net.d_ichi84.framework.gl.FPSCounter;
import net.d_ichi84.framework.gl.GLGame;
import net.d_ichi84.framework.gl.GLGraphics;
import net.d_ichi84.framework.gl.Texture;
import net.d_ichi84.framework.gl.Vertices;



public class TestScreen extends Screen{
	GLGraphics glGraphics;
	Texture texture;
	Vertices vertices;
	
	TipNeko[] nekos;
	FPSCounter fpsCounter = new FPSCounter();
	final int NEKO = 100;
	public TestScreen(Game game){
		super(game);
		glGraphics = ((GLGame) game).getGLGraphics();
		
		//頂点クラス  頂点4つ、インデックス的には6つ、カラーなし、テクスチャ有り
		vertices = new Vertices(glGraphics, 4, 6, false, true);
		vertices.setVertices(
				new float[]{
				  0.0f,   0.0f, 0f, 0.0f, 1.0f,	//左下
				 64.0f,   0.0f, 0f, 1.0f, 1.0f,	//右下
				  0.0f,  64.0f, 0f, 0.0f, 0.0f,	//左上
				 64.0f,  64.0f, 0f, 1.0f, 0.0f,	//右上
				},
				0, 20);
		vertices.setIndices(new short[]{
				0, 1, 3, 3, 2, 0
		}, 0, 6);
		
		texture = new Texture(
				glGraphics,
				new AndroidFileIO(context.getAssets()),
				"neko_ss.jpg");
		
		nekos = new TipNeko[NEKO];
		for(int i =0; i<NEKO; i++){
			nekos[i] = new TipNeko();
		}
	}

	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.gl;

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);	//塗りつぶし

		vertices.bind();
		gl.glEnable(GL10.GL_TEXTURE_2D);
		texture.bind();
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		for(int i=0; i<NEKO; i++){
			gl.glLoadIdentity();
			gl.glTranslatef(nekos[i].x, nekos[i].y, 0);
			vertices.draw(GL10.GL_TRIANGLES, 0, 6);	
		}
		vertices.unbind();
		
		fpsCounter.logFrame();
	}
	
	@Override
	public void update(float deltaTime) {
		for(int i =0; i<NEKO; i++){
			nekos[i].update(deltaTime);
		}
	}
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void resume() {
		GL10 gl = glGraphics.gl;
		gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
		
		gl.glClearColor(0f, 0f, 1f, 1f); //青
		
		//投影行列
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0, 320, 0, 480, 1, -1);
		
		//テクスチャ
		texture.reload();
		gl.glEnable(GL10.GL_TEXTURE_2D);
		texture.bind();
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}