package net.d_ichi84.GL_test.GL.model;

import java.util.Random;

public class TipNeko {
	static final Random rand = new Random();
	public float x,y;
	float dirX, dirY;
	
	public TipNeko(){
		x= rand.nextFloat() *320;
		y= rand.nextFloat() *480;
		dirX = 50;
		dirY = 50;
	}
	
	public void update(float deltaTime){
		x = x + dirX * deltaTime;
		y = y + dirY * deltaTime;
		
		if(x<0){
			dirX = -dirX;
			//x = 0;
		}
		if(y<0){
			dirY = -dirY;
			//y = 0;
		}
		if(x>320){
			dirX = -dirX;
			//x = 320;
		}
		if(y>480){
			dirY = -dirY;
			//x = 480;
		}
		
		
	}
	
}
