package net.d_ichi84.GL_test.game.screen;

import javax.microedition.khronos.opengles.GL10;



import net.d_ichi84.framework.Game;
import net.d_ichi84.framework.Screen;
import net.d_ichi84.framework.android.AndroidFileIO;
import net.d_ichi84.framework.gl.EulerCamera;
import net.d_ichi84.framework.gl.FPSCounter;
import net.d_ichi84.framework.gl.GLGame;
import net.d_ichi84.framework.gl.GLGraphics;
import net.d_ichi84.framework.gl.Texture;
import net.d_ichi84.framework.gl.Vertices;
import net.d_ichi84.framework.math.Vector2;

public class EulerCameraScreen extends Screen{
	GLGraphics glGraphics;
	Texture crateTexture;
	Vertices cube;
	EulerCamera camera;
	
	Vector2 touchPos;
	float lastX;
	float lastY;
	
	FPSCounter fpsCounter = new FPSCounter();
	
	public EulerCameraScreen(Game game){
		super(game);
		
		glGraphics = ((GLGame) game).getGLGraphics();
		crateTexture = new Texture(
				glGraphics,
				new AndroidFileIO(context.getAssets()),
				"neko_ss.jpg");
		
		cube = createCube();
		float aspect = glGraphics.getWidth()/(float)glGraphics.getHeight();
		camera = new EulerCamera(67, aspect, 1, 100);
		camera.getPosition().add(0, 3f, 0);
		touchPos = new Vector2();
	}
	
	public Vertices createCube(){	
		float[] v={ //x,y,z,u,v
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
	public void update(float deltaTime) {
		
		game.getInput().getTouchEvents();
		float x = game.getInput().getTouchX(0);
		float y = game.getInput().getTouchY(0);
		
		if(game.getInput().isTouchDown(0)){
//			if(false){//touchPos.x < 64 && touchPos.y <64){			//(0,0)-(64,64)の部分にボタンがあることになっている。
//				Vector3 direction = camera.getDirection();	//そこをタッチした場合カメラの方向（つまり今向いている前方向）に
//				camera.getPosition().add(direction.mul(deltaTime));	//deltaTime分移動する（つまり1秒で1進む）
//			}else{					//それ以外はスワイプ扱い
				if(lastX== -1){ //前回のタッチ座標がない
					lastX = x;
					lastY = y;
				}else{			//前回からの増分
					camera.rotate((x-lastX)/10, (y-lastY)/10);
					lastX = x;
					lastY = y;
				}
//			}
		}else{
			lastX = -1;
			lastY = -1;
		}
		
	}

	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.gl;
		
		gl.glClearColor(0f, 0f, 1f, 1f); //青
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | //背景
				   GL10.GL_DEPTH_BUFFER_BIT); //深度バッファ
		
		gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
		camera.setMatrices(gl);
		
		//Vector3 direction = camera.getDirection();
		
		
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		//gl.glEnable(GL10.GL_LIGHTING);
		
		crateTexture.bind();
		cube.bind();
		/*
		gl.glTranslatef(0, 0, -3); //平行移動
		gl.glTranslatef(0, -1, 0);  //平行移動
		gl.glRotatef(45, 1, 0, 0);
		cube.draw(GL10.GL_TRIANGLES, 0, 36);
		*/
		for(int z = 0; z>= -8; z-=2){ //箱を0から2おきに5つ並べる
			for(int x = -4; x<= 4; x+=2){//-4から4まで5つ並べる
				gl.glPushMatrix(); //現在の変換行列をスタックに待避　
				gl.glTranslatef(x, 0, z);
				cube.draw(GL10.GL_TRIANGLES, 0, 36);
				gl.glPopMatrix();  //スタックに待避したのをおろす
			}	
		}
		
		cube.unbind();
		//gl.glDisable(GL10.GL_LIGHTING);
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		crateTexture.reload();		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
