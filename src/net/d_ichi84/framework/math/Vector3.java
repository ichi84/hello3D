package net.d_ichi84.framework.math;

import android.opengl.Matrix;
import android.util.FloatMath;

public class Vector3 {
	private static final float[] matrix = new float[16];
	private static final float[] inVec = new float[4];
	private static final float[] outVec = new float[4];
	public float x, y, z;
	
	public Vector3(){}
	
	public Vector3(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3(Vector3 other){
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}
	
	public Vector3 copy(){
		return new Vector3(x, y, z);
	}
	
	public Vector3 set(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	public Vector3 set(Vector3 other){
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
		return this;
	}
	
	public Vector3 add(float x, float y, float z){
		Vector3 out = new Vector3();
		out.x = this.x + x;
		out.y = this.y + y;
		out.z = this.z + z;
		return out;
	}
	
	public Vector3 add(Vector3 other){
		Vector3 out = new Vector3();
		out.x = this.x + other.x;
		out.y = this.y + other.y;
		out.z = this.z + other.z;
		return out;
	}
	public Vector3 fastAdd(Vector3 other){
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;
		return this;
	}
	
	public Vector3 sub(float x, float y, float z){
		Vector3 out = new Vector3();
		out.x = this.x - x;
		out.y = this.y - y;
		out.z = this.z - z;
		return out;
	}
	
	public Vector3 sub(Vector3 other){
		Vector3 out = new Vector3();
		
		out.x = this.x - other.x;
		out.y = this.y - other.y;
		out.z = this.z - other.z;
		return out;
	}
	
	public Vector3 mul(float scarar){
		Vector3 out = new Vector3();
		out.x = this.x * scarar;
		out.y = this.y * scarar;
		out.z = this.z * scarar;
		return out;
	}
	public Vector3 fastMul(float scarar){
		this.x *= scarar;
		this.y *= scarar;
		this.z *= scarar;
		return this;
	}
	
	public float len(){
		return FloatMath.sqrt(x*x + y*y + z*z);
	}
	
	public Vector3 normalize(){
		Vector3 out = new Vector3(0,0,0);
		float len = len();
		if(len != 0){
			out.x = this.x / len;
			out.y = this.y / len;
			out.z = this.z / len;
		}
		return out;
	}
	
	public Vector3 rotate(float angle, float axisX, float axisY, float axisZ ){
		inVec[0] = x;
		inVec[1] = y;
		inVec[2] = z;
		inVec[3] = 1;
		
		Matrix.setIdentityM(matrix, 0);
		Matrix.rotateM(matrix, 0, angle, axisX, axisY, axisZ);
		Matrix.multiplyMV(outVec, 0, matrix, 0, inVec, 0);
		
		Vector3 out = new Vector3(0,0,0);
		out.x = outVec[0];
		out.y = outVec[1];
		out.z = outVec[2];
		
		return out;
	}
	
	public float dist(Vector3 other){
		float distX = this.x - other.x;
		float distY = this.y - other.y;
		float distZ = this.z - other.z;
		return FloatMath.sqrt(distX*distX + distY*distY + distZ*distZ);
	}
	
	public float dist(float x, float y, float z){
		float distX = this.x - x;
		float distY = this.y - y;
		float distZ = this.z - z;
		return FloatMath.sqrt(distX*distX + distY*distY + distZ*distZ);
	}
	
	public float distSquared(Vector3 other){
		float distX = this.x - other.x;
		float distY = this.y - other.y;
		float distZ = this.z - other.z;
		return distX*distX + distY*distY + distZ*distZ;
	}
	
	public float distSquared(float x, float y, float z){
		float distX = this.x - x;
		float distY = this.y - y;
		float distZ = this.z - z;
		return distX*distX + distY*distY + distZ*distZ;
	}
	
	public float angle(Vector3 other){
		float cos_c = (this.x*other.x + this.y*other.y + this.z*other.z)/
				(FloatMath.sqrt(this.x*this.x + this.y*this.y + this.z*this.z)
					* FloatMath.sqrt(other.x*other.x + other.y*other.y + other.z*other.z));
		if(cos_c>1.0) cos_c = 1.0f;
		if(cos_c<-1.0) cos_c = -1.0f;
		float c= (float) Math.acos(cos_c);
		
		
		return c*Vector2.TO_DEGREES;
	}


	// 外積計算（ans=vec1×vec2）
	public Vector3 crossProduct(Vector3 other){
		Vector3 ans = new Vector3(0,0,0);  
	    ans.x = this.y*other.z - this.z*other.y;
	    ans.y = this.z*other.x - this.x*other.z;
	    ans.z = this.x*other.y - this.y*other.x;
	    return ans;
	}

	// 内積計算（ans=vec1・vec2）
	float dotProduct( Vector3 ohter){
	    float ans = this.x*ohter.x + this.y*ohter.y + this.z*ohter.z;
	    return ans;
	}
	
}
