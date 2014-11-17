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
	
	int lengthW = 9; //�A�j���[�V�������B���ɕ���ł鐔�B�Ƃ肠����9�R�}
	int lengthH = 1; //�A�j���[�V�������B�c�ɕ���ł鐔�B
	int width  = 48; //1�R�}�̕�
	int height = 96; //1�R�}�̍���
	public final float unitHeight = 2.0f;	//�X�v���C�g�̍���
	public final float unitWidth = 1.0f;	//�X�v���C�g�̍������B���s��������
	
	public Player(GLGraphics glGraphics, float x, float y, float z){
		super(x,y,z);
		this.glGraphics = glGraphics;
		this.walkingTime = 0;
		
		spriteBatcher = new SpriteBatcher(glGraphics, 10);//�Ƃ肠����10��
		
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
		
		anime = new Animation(1f/15f, texRegion);//15����1�b/�R�}
	}
	
	public void draw(float cameraX, float cameraY, float cameraZ){
		GL10 gl = glGraphics.gl;
		TextureRegion keyFrame = anime.getKeyFrame(walkingTime, Animation.ANIMAION_LOOPING);
		gl.glPushMatrix();
		
		//�������畽�ʂ��J���������Ɍ����邽�߂̃x�N�g���v�Z���낢��
		//�J�������璍���_�ւ̕����x�N�g��
		float CtoLook_X = WorldRenderer.camera.getLookAt().x- cameraX;
		//float CtoLook_Y = 0;//WorldRenderer.camera.getLookAt().y- cameraY;
		float CtoLook_Z = WorldRenderer.camera.getLookAt().z- cameraZ;		
		
		//�J��������I�u�W�F�N�g�ւ̕����x�N�g��
		float CtoA_X = position.x +unitWidth/2.0f- cameraX;
		float CtoA_Y = position.y - cameraY;
		float CtoA_Z = position.z -unitWidth/2.0f- cameraZ;
		Vector3 CtoA3 = new Vector3(CtoA_X, CtoA_Y, CtoA_Z);
		//����XZ���ʂւ̓��e
		Vector2 XZtoO = new Vector2(CtoA_X , CtoA_Z);
		Vector3 XZtoO3 = new Vector3(CtoA_X, 0 ,  CtoA_Z);
		
		//�J��������]�����Ȃ�������]�p0�x�̈ʒu�̐^��Y=0�̓_
		float Xaxis_X = position.x +unitWidth/2.0f;
		float Xaxis_Z = 1;
		//������I�u�W�F�N�g�̐^���ւ̕��������x�N�g��
		float XatoO_X = position.x +unitWidth/2.0f - Xaxis_X;
		float XatoO_Z = position.z +unitWidth/2.0f - Xaxis_Z;
		Vector2 XatoO = new Vector2(XatoO_X, XatoO_Z);
		
		//���������̂Ȃ��p
		float c_V =CtoA3.angle(XZtoO3);
		c_V = -c_V;
		
		//���������̂Ȃ��p Y
		float c_H = XZtoO.angle(XatoO);

		//�����_�A�J�����A�I�u�W�F�N�g�̂Ȃ��p	
		Vector2 v = new Vector2(CtoLook_X,CtoLook_Z);
		Vector2 v2 = new Vector2(CtoA_X,CtoA_Z);
		float c_HH = v.angle(v2);

		//�����ڂ�ς����ɏ��A�ǂ��痣�����J���������ɓ������ďk������
		//�I�u�W�F�N�g����J�����ւ̃x�N�g��
		float OtoC_X = -CtoA_X;
    	float OtoC_Y = -CtoA_Y;
		float OtoC_Z = -CtoA_Z;
		//���̑傫���i�X�J���[�j
		float Scalar = FloatMath.sqrt( OtoC_X*OtoC_X + OtoC_Y*OtoC_Y + OtoC_Z*OtoC_Z);
		//�P�ʃx�N�g���ɂ���
		OtoC_X /= Scalar;
		OtoC_Y /= Scalar;
		OtoC_Z /= Scalar;
		
		//�J���������ւ̈ړ���
		float moveScala =  - unitHeight * FloatMath.cos( (90-c_V)*Vector2.TO_RADIANS ) ;
		float scale = 0.95f;//unitHeight * FloatMath.sin((90-c_V)*Vector2.TO_RADIANS );
		moveScala *= 1.4f; //������
		
		gl.glTranslatef(position.x + unitWidth/2.0f,       //�{���̈ʒu��
				position.y , position.z - unitWidth/2.0f); //�����j�b�g����
		gl.glTranslatef(OtoC_X*moveScala, OtoC_Y*moveScala, OtoC_Z*moveScala);//�ǂɂ߂荞�܂Ȃ����炢�J���������ɓ�����
		gl.glScalef(scale, scale, scale); //�X�P�[���ϊ��B��Ŏ�O�ɓ������̂ł���ɍ��킹�ď���������
		gl.glRotatef(c_H, 0, 1, 0);
		gl.glRotatef(c_V, 1, 0, 0);
		gl.glRotatef(c_HH, 0, 0, 1);
		gl.glTranslatef(0, -0.2f, 0); //�������B�p�x��ς�����v�ύX�B
		
		spriteBatcher.beginBatch(texture);
		spriteBatcher.drawSprite(0, 0, 0, 1, 2, keyFrame);
		spriteBatcher.endBatch();
		
		gl.glPopMatrix();
	}
}
