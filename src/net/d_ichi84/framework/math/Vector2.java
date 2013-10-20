package net.d_ichi84.framework.math;

import android.util.FloatMath;

public class Vector2 {
	public static float TO_RADIANS = (1/180.0f)*(float)Math.PI;
	public static float TO_DEGREES = (1/(float)Math.PI)*180;
	public float x,y;
	
	public Vector2(){}
	
	public Vector2(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public Vector2(Vector2 other){
		this.x = other.x;
		this.y = other.y;
	}
	
	public Vector2 copy(){
		return new Vector2(x,y);
	}
	
	public Vector2 set(float x, float y){
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Vector2 set(Vector2 other){
		this.x = other.x;
		this.y = other.y;
		return this;
	}
	
	public Vector2 add(float x, float y){
		Vector2 out = new Vector2(0,0);
		out.x = this.x + x;
		out.y = this.y + y;
		return out;
	}
	
	public Vector2 add(Vector2 other){
		Vector2 out = new Vector2(0,0);
		out.x = this.x + other.x;
		out.y = this.y + other.y;
		return out;
	}
	
	public Vector2 sub(float x, float y){
		Vector2 out = new Vector2(0,0);
		out.x = this.x - x;
		out.y = this.y - y;
		return out;
	}
	
	public Vector2 sub(Vector2 other){
		Vector2 out = new Vector2(0,0);
		out.x = this.x - other.x;
		out.y = this.y - other.y;
		return out;
	}
	
	public Vector2 mul(float scalar){
		Vector2 out = new Vector2(0,0);
		out.x = this.x * scalar;
		out.y = this.y * scalar;
		return out;
	}
	public float len(){
		return FloatMath.sqrt(x*x + y*y);
	}
	
	public Vector2 normalize(){
		Vector2 out = new Vector2(0,0);
		float len = len();
		if(len!=0){
			out.x = this.x / len;
		    out.y = this.y / len;
		}
		return out;
	}
	
	public float angle(){
		float angle = (float)Math.atan2(y, x)*TO_DEGREES;
		if(angle<0){
			angle+=360;
		}
		return angle;
	}
	
	public float angle(Vector2 other){
		float cos_c = (this.x*other.x + this.y*other.y)/
				(FloatMath.sqrt(this.x*this.x + this.y*this.y)
					* FloatMath.sqrt(other.x*other.x + other.y*other.y));
		if(cos_c>1.0) cos_c = 1.0f;
		if(cos_c<-1.0) cos_c = -1.0f;
		float c= (float) Math.acos(cos_c);
		
		float direction = 	//‰ñ“]•ûŒü‚ð‹‚ß‚é
				this.x * other.y  
				-  other.x * this.y;
		if(direction<0) c = -c;
		return c*TO_DEGREES;
	}
	
	public Vector2 rotate(float angle){
		Vector2 out = new Vector2(0,0);
		float rad = angle *TO_RADIANS;
		float cos = FloatMath.cos(rad);
		float sin = FloatMath.sin(rad);
		
		out.x = this.x*cos - this.y*sin;
		out.y = this.x*sin + this.y*cos;
		return out;
	}
	
	public float dist(Vector2 other){
		float distX = this.x - other.x;
		float distY = this.y - other.y;
		return FloatMath.sqrt(distX*distX + distY*distY);
	}
	public float dist(float x, float y){
		float distX = this.x - x;
		float distY = this.y - y;
		return FloatMath.sqrt(distX*distX + distY*distY);
	}
}
