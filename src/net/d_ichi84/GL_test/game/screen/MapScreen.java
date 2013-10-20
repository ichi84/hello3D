package net.d_ichi84.GL_test.game.screen;


import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;


import android.opengl.GLU;
import android.util.FloatMath;

import net.d_ichi84.GL_test.game.Constants;
import net.d_ichi84.GL_test.game.World;
import net.d_ichi84.GL_test.game.WorldRenderer;
import net.d_ichi84.framework.Game;
import net.d_ichi84.framework.Screen;
import net.d_ichi84.framework.gl.FPSCounter;
import net.d_ichi84.framework.gl.GLGame;
import net.d_ichi84.framework.gl.GLGraphics;
import net.d_ichi84.framework.gl.ViewPoint;
import net.d_ichi84.framework.math.Vector2;
import net.d_ichi84.framework.math.Vector3;

public class MapScreen extends Screen{
	GLGraphics glGraphics;
	
	Vector2 touchPos;
	float lastX0, lastX1;
	float lastY0, lastY1;
	ViewPoint viewpoint = new ViewPoint(); //視点
	final float taikaku;	//画面対角線の長さ
	
	World world;
	WorldRenderer renderer;
	FPSCounter fpsCounter = new FPSCounter();
	
	public MapScreen(Game game){
		super(game);
		
		glGraphics = ((GLGame) game).getGLGraphics();
		touchPos = new Vector2();
		world = new World(glGraphics);
		renderer = new WorldRenderer(glGraphics, world);
		taikaku = FloatMath.sqrt(glGraphics.getWidth()*glGraphics.getWidth() + glGraphics.getHeight()*glGraphics.getHeight() );
	}
	
	   
	@Override
	public void update(float deltaTime) {		
		//ここから入力系。そのうち分離する
		game.getInput().getTouchEvents();
		float x0 = game.getInput().getTouchX(0);
		float y0 = game.getInput().getTouchY(0);
		float x1 = game.getInput().getTouchX(1);
		float y1 = game.getInput().getTouchY(1);
		Vector3 touchVector[] = null;
		
		if(game.getInput().isTouchDown(1)){ //2カ所タッチ中
			if(lastX1== -1){ //前回のタッチ座標がない
				lastX1 = x1;
				lastY1 = y1;
			}else{
				//回転対応
				Vector2 v1 = new Vector2(lastX1-x0, lastY1-y0);
				Vector2 v2 = new Vector2(x1-x0, y1-y0);
				
				//2つのベクトルのなす角
				float c = v1.angle(v2);
				viewpoint.rotateH += c*5; //そのままだと遅すぎるので適当に増やす。
				viewpoint.rotateV = 0;	   //今のところx軸回転は封印
				
				//くぱぁでズーム
				float lastKupaa = 
						FloatMath.sqrt( (lastX0-lastX1)*(lastX0-lastX1) 
								      + (lastY0-lastY1)*(lastY0-lastY1) );//前回の2点間の距離
				float Kupaa = FloatMath.sqrt( (x0-x1)*(x0-x1) + (y0-y1)*(y0-y1) );//今回の2点間の距離
				float zoom = lastKupaa - Kupaa;
				if(zoom > taikaku/10) zoom = taikaku/10; //リミット
				if(zoom < -taikaku/10) zoom = -taikaku/10; //リミット
				viewpoint.dist = viewpoint.dist + zoom/(taikaku/50);//対角線の10分の1ほどを係数にかけて増減する 
				if(viewpoint.dist > Constants.CAMERA_FAR_LIMIT) viewpoint.dist = Constants.CAMERA_FAR_LIMIT;
				if(viewpoint.dist < Constants.CAMERA_NEAR_LIMIT) viewpoint.dist = Constants.CAMERA_NEAR_LIMIT;
				
				//今回の座標を保存
				lastX0 = x0;
				lastY0 = y0;
				lastX1 = x1;
				lastY1 = y1;
			}
		}else	if(game.getInput().isTouchDown(0)){//一カ所タッチ中
			GL11 gl11 = (GL11)glGraphics.gl;
			int[] bits = new int[16];
	    	float[] model = new float[16];
	    	float[] proj = new float[16];
	    	gl11.glGetIntegerv(GL11.GL_MODELVIEW_MATRIX_FLOAT_AS_INT_BITS_OES, bits, 0);
	    	for(int i = 0; i < bits.length; i++){
	    		model[i] = Float.intBitsToFloat(bits[i]);
	    	}
	    	gl11.glGetIntegerv(GL11.GL_PROJECTION_MATRIX_FLOAT_AS_INT_BITS_OES, bits, 0);
	    	for(int i = 0; i < bits.length; i++){
	    		proj[i] = Float.intBitsToFloat(bits[i]);
	    	}
	    	int[] viewport = new int[16];
			gl11.glGetIntegerv(GL11.GL_VIEWPORT, viewport,0);
		
	    	float[] near = new float[4];
	    	float[] far = new float[4];
	    	
	    	//近い平面上の3次元座標
	    	GLU.gluUnProject(x0, viewport[3]-y0, 0f, model, 0, proj, 0, viewport, 0, near, 0);
	    	Vector3 nearV = new Vector3(near[0]/near[3], near[1]/near[3], near[2]/near[3]);
	    	
	    	//遠い平面上の3次元座標
	    	GLU.gluUnProject(x0, viewport[3]-y0, 1f, model, 0, proj, 0, viewport,0, far, 0);
	    	Vector3 farV = new Vector3(far[0]/far[3], far[1]/far[3], far[2]/far[3]);
	        	
	    	//boolean t = picked(farV, nearV, new Vector3(0.5f, 0f, -0.5f), 1);
	    	touchVector = new Vector3[2];
			touchVector[0] = farV;
			touchVector[1] = nearV;
	    		
	    	//今回の座標を保存
			lastX0 = x0;
			lastY0 = y0;
		}
		
		if( !game.getInput().isTouchDown(0) ){ //1カ所もタッチしてない
			lastX0 = -1;
			lastY0 = -1;
			lastX1 = -1;
			lastY1 = -1;
		}
		if( !game.getInput().isTouchDown(1) ){ //2カ所タッチしてない
			
		}
		//ここまで入力系
		
		renderer.update(deltaTime, touchVector);
	}

	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.gl;
		
		gl.glClearColor(0f, 0f, 1f, 1f); //青
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | //背景
				   GL10.GL_DEPTH_BUFFER_BIT); //深度バッファ
		
		renderer.render(deltaTime, viewpoint);
	
		fpsCounter.logFrame();
	}

	@Override
	public void pause() {
		renderer.pause();
	}

	@Override
	public void resume() {
		renderer.resume();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}
}
