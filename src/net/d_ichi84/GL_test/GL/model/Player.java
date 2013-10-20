package net.d_ichi84.GL_test.GL.model;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;

import net.d_ichi84.GL_test.game.WorldRenderer;
import net.d_ichi84.framework.DynamicGameObject3D;
import net.d_ichi84.framework.android.AndroidFileIO;
import net.d_ichi84.framework.gl.Animation;
import net.d_ichi84.framework.gl.GLGame;
import net.d_ichi84.framework.gl.GLGraphics;
import net.d_ichi84.framework.gl.SpriteBatcher;
import net.d_ichi84.framework.gl.Texture;
import net.d_ichi84.framework.gl.TextureRegion;
import net.d_ichi84.framework.math.Vector2;
import net.d_ichi84.framework.math.Vector3;

public class Player extends DynamicGameObject3D{
	GLGraphics glGraphics;
	
	public float walkingTime;
	Texture texture;
	Animation anime;
	SpriteBatcher spriteBatcher;
	
	int lengthW = 9; //アニメーション数。横に並んでる数。とりあえず9コマ
	int lengthH = 1; //アニメーション数。縦に並んでる数。
	int width  = 48; //1コマの幅
	int height = 96; //1コマの高さ
	public final float unitHeight = 2.0f;	//スプライトの高さ
	public final float unitWidth = 1.0f;	//スプライトの高さ幅。奥行きも同じ
	
	public Player(GLGraphics glGraphics, float x, float y, float z){
		super(x,y,z);
		this.glGraphics = glGraphics;
		this.walkingTime = 0;
		
		spriteBatcher = new SpriteBatcher(glGraphics, 10);//とりあえず10個分
		
		texture = new Texture(
				glGraphics,
				new AndroidFileIO(GLGame.context.getAssets()),
				"player_move.png");
	}
	
	public void update(float deltaTime){
		//position.add(position)
		
		walkingTime += deltaTime; 
	}
	
	public void resume(){
		texture.reload();
		

		TextureRegion[] texRegion = new TextureRegion[lengthW*lengthH];
		
		for(int i=0; i < lengthH;  i++){
			for(int j=0; j < lengthW;  j++){	
				texRegion[i*j +j] 
					= new TextureRegion(texture, width*j, height*i, width, height );
			}
		}
		
		anime = new Animation(1f/15f, texRegion);//15分の1秒/コマ
	}
	
	public void draw(float cameraX, float cameraY, float cameraZ){
		GL10 gl = glGraphics.gl;
		TextureRegion keyFrame = anime.getKeyFrame(walkingTime, Animation.ANIMAION_LOOPING);
		gl.glPushMatrix();
		
		//ここから平面をカメラ方向に向けるためのベクトル計算いろいろ
		//カメラから注視点への方向ベクトル
		float CtoLook_X = WorldRenderer.camera.getLookAt().x- cameraX;
		//float CtoLook_Y = 0;//WorldRenderer.camera.getLookAt().y- cameraY;
		float CtoLook_Z = WorldRenderer.camera.getLookAt().z- cameraZ;		
		
		//カメラからオブジェクトへの方向ベクトル
		float CtoA_X = position.x +unitWidth/2.0f- cameraX;
		float CtoA_Y = position.y - cameraY;
		float CtoA_Z = position.z -unitWidth/2.0f- cameraZ;
		Vector3 CtoA3 = new Vector3(CtoA_X, CtoA_Y, CtoA_Z);
		//↑のXZ平面への投影
		Vector2 XZtoO = new Vector2(CtoA_X , CtoA_Z);
		Vector3 XZtoO3 = new Vector3(CtoA_X, 0 ,  CtoA_Z);
		
		//カメラを回転させない水平回転角0度の位置の真下Y=0の点
		float Xaxis_X = position.x +unitWidth/2.0f;
		float Xaxis_Z = 1;
		//↑からオブジェクトの真下への方向方向ベクトル
		float XatoO_X = position.x +unitWidth/2.0f - Xaxis_X;
		float XatoO_Z = position.z +unitWidth/2.0f - Xaxis_Z;
		Vector2 XatoO = new Vector2(XatoO_X, XatoO_Z);
		
		//垂直方向のなす角
		float c_V =CtoA3.angle(XZtoO3);
		c_V = -c_V;
		
		//水平方向のなす角 Y
		float c_H = XZtoO.angle(XatoO);

		//注視点、カメラ、オブジェクトのなす角	
		Vector2 v = new Vector2(CtoLook_X,CtoLook_Z);
		Vector2 v2 = new Vector2(CtoA_X,CtoA_Z);
		float c_HH = v.angle(v2);

		//見た目を変えずに床、壁から離す→カメラ方向に動かして縮小する
		//オブジェクトからカメラへのベクトル
		float OtoC_X = -CtoA_X;
    	float OtoC_Y = -CtoA_Y;
		float OtoC_Z = -CtoA_Z;
		//その大きさ（スカラー）
		float Scalar = FloatMath.sqrt( OtoC_X*OtoC_X + OtoC_Y*OtoC_Y + OtoC_Z*OtoC_Z);
		//単位ベクトルにする
		OtoC_X /= Scalar;
		OtoC_Y /= Scalar;
		OtoC_Z /= Scalar;
		
		//カメラ方向への移動量
		float moveScala =  - unitHeight * FloatMath.cos( (90-c_V)*Vector2.TO_RADIANS ) ;
		float scale = 0.95f;//unitHeight * FloatMath.sin((90-c_V)*Vector2.TO_RADIANS );
		moveScala *= 1.4f; //微調整
		
		gl.glTranslatef(position.x + unitWidth/2.0f,       //本来の位置へ
				position.y , position.z - unitWidth/2.0f); //半ユニット奥へ
		gl.glTranslatef(OtoC_X*moveScala, OtoC_Y*moveScala, OtoC_Z*moveScala);//壁にめり込まないくらいカメラ方向に動かす
		gl.glScalef(scale, scale, scale); //スケール変換。後で手前に動かすのでそれに合わせて小さくする
		gl.glRotatef(c_H, 0, 1, 0);
		gl.glRotatef(c_V, 1, 0, 0);
		gl.glRotatef(c_HH, 0, 0, 1);
		gl.glTranslatef(0, -0.2f, 0); //微調整。角度を変えたら要変更。
		
		spriteBatcher.beginBatch(texture);
		spriteBatcher.drawSprite(0, 0, 0, 1, 2, keyFrame);
		spriteBatcher.endBatch();
		
		gl.glPopMatrix();
	}
}
