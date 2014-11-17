package net.d_ichi84.GL_test.game;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;
import net.d_ichi84.framework.gl.GLGraphics;
import net.d_ichi84.framework.gl.LookAtCamera;
import net.d_ichi84.framework.gl.ViewPoint;
import net.d_ichi84.framework.math.Vector2;
import net.d_ichi84.framework.math.Vector3;

public class WorldRenderer {
	GLGraphics glGraphics;
	public static LookAtCamera camera;
	World world;

	public float cameraDistance;		//�����_����J�����̋���
	public float cameraRotateH = 0.0f;	//�J�����̐��������̉�]�p�x�i���S�͒����_�j
	public Vector3 cameraDir;			//�J�����̌����Ă�����̕����x�N�g���i�P�ʃx�N�g���j
	
	public WorldRenderer(GLGraphics glGraphics, World world){
		this.glGraphics = glGraphics;
		float aspect = glGraphics.getWidth()/(float)glGraphics.getHeight();
		camera = new LookAtCamera(Constants.fovY, aspect, Constants.nearZ, Constants.farZ );
		camera.getLookAt().set(0, 0, 0);
		
		this.world = world;
	}
	
	public void render( float deltaTime, ViewPoint viewpoint ){
		GL10 gl = glGraphics.gl;
		
		//�����_�̓v���C���[�ʒu
		camera.getLookAt().x = world.player.position.x + 0.5f;
		camera.getLookAt().y = world.player.position.y;
		camera.getLookAt().z = world.player.position.z - 0.5f;
		
		//��]����������
		cameraRotateH = viewpoint.rotateH*Vector2.TO_RADIANS;
		//�����_����̋�����ύX����		
		cameraDistance = viewpoint.dist;
	
		//�����Ɗp�x������W�����߂�
		float x = camera.getLookAt().x + cameraDistance*FloatMath.sin(cameraRotateH)*FloatMath.cos(Constants.angleV);
		float y = camera.getLookAt().y + cameraDistance*FloatMath.sin(Constants.angleV);
		float z = camera.getLookAt().z + cameraDistance*FloatMath.cos(cameraRotateH)*FloatMath.cos(Constants.angleV);
		camera.getPosition().set(x, y, z);
		camera.setMatrices(gl);
		
		cameraDir=  camera.getLookAt().sub(camera.getPosition());
		cameraDir = cameraDir.normalize(); //�P�ʃx�N�g���ɂ���B
		//�����܂ŃJ�����Z�b�e�B���O

		//�������烌���_�����O
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		renderMap(gl);
		renderCharacter(gl, 1);

		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisable(GL10.GL_DEPTH_TEST);
	}
	
	private void renderMap(GL10 gl){
		Assets.maptestTexture.bind();
		world.map.draw();
	}
	
	private void renderCharacter(GL10 gl,  int numSprite){
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
	    // ���݂̃��f���r���[�ϊ��s������o��
	    gl.glMatrixMode(GL10.GL_MODELVIEW);
		for(int i=0; i<numSprite; i++){
			gl.glPushMatrix();
			world.player.draw(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z);
			world.player2.draw(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z);
			gl.glPopMatrix();
		}
		gl.glDisable(GL10.GL_BLEND);
	}
	
	public void resume(){
		world.player.resume();
		world.player2.resume();
	}
	
	public void update(float deltaTime, Vector3[] Touch){
		world.update(deltaTime, Touch);
	}
	
	public void pause(){
	}
	
	
}
