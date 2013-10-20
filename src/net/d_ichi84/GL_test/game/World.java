package net.d_ichi84.GL_test.game;

import android.util.FloatMath;
import android.util.Log;

import net.d_ichi84.GL_test.GL.model.Map;
import net.d_ichi84.GL_test.GL.model.Player;
import net.d_ichi84.framework.gl.GLGraphics;
import net.d_ichi84.framework.math.Vector2;
import net.d_ichi84.framework.math.Vector3;

public class World {
	public interface WorldListener{
		public void shot();
	}
	
	Map map;
	WorldListener listener;
	
	final Player player;
	final Player player2; //デバグ用
	GLGraphics	 glGraphics;
		
	public World(GLGraphics glGraphics){
		map = new Map(glGraphics);
		map.generateMap();
		player = new Player(glGraphics, 12,map.getHeight(12, -6),-6);//プレイヤー初期位置
		player2 = new Player(glGraphics, 7,map.getHeight(7, -2),-2);//プレイヤー2初期位置
		this.glGraphics = glGraphics;

	}
		
	public void setWorldListener(WorldListener listener){
		this.listener = listener;
	}
	
	public void update(float deltaTime, Vector3[] Touch){
		Vector2 v1 = new Vector2();
		if(Touch != null){	//全探索するとすごく遅いのでピラミッド探索する
			v1 = rPiramid(Touch, new Vector2(map.unit_width/2,-map.unit_height/2),
					new Vector2(0,0), new Vector2(map.unit_width,-map.unit_height), map.unit_width/2);
			if(v1!=null)
				Log.e("Touch", "(x,z)=("+Float.toString(v1.x) + ","+Float.toString(v1.y)+")" );
		}
		player.update(deltaTime);
	}
	
	//ピラミッド探索
	private Vector2 rPiramid(Vector3[] Touch, Vector2 start,Vector2 min, Vector2 max, int step){
		if(step==0)
			return start;	//見つかった
		
		if(min.x <0) min.x=0;
		if(min.y >0) min.y=0;
		if(max.x >map.unit_width) max.x=map.unit_width;
		if(max.y <-map.unit_height) max.y=-map.unit_height;
		
		for(int z=(int)min.y ; z>max.y; z-=step){
			for(int x=(int)min.x; x<max.x; x+=step){
				Vector3 []tri1 = map.getTriangle1(x, z);
				Vector3 []tri2 = map.getTriangle2(x, z);
				Vector3 g1 =tri1[0].fastAdd(tri1[1].fastAdd(tri1[2])).fastMul(1/3f);
				Vector3 g2 =tri2[0].fastAdd(tri2[1].fastAdd(tri2[2])).fastMul(1/3f);
				g1.fastAdd(g2).fastMul(1/2f);
				g1.x -= 0.5f;
				g1.z += 0.5f;
				boolean t = picked(Touch[0], Touch[1], g1, step);
				if(t){
					return  rPiramid(Touch, new Vector2(x,z),
							new Vector2(x-step,z+step),new Vector2(x+step,z-step),  step/2);
				}
			}
		}
		return null;	//見つからなかった
	}
	
	//交差判定
	private boolean picked(Vector3 a, Vector3 b, Vector3 q, float r) {
	       float ab = FloatMath.sqrt((a.x-b.x)*(a.x-b.x)+(a.y-b.y)*(a.y-b.y)+(a.z-b.z)*(a.z-b.z));
	       float aq = FloatMath.sqrt((a.x-q.x)*(a.x-q.x)+(a.y-q.y)*(a.y-q.y)+(a.z-q.z)*(a.z-q.z));
	       float bq = FloatMath.sqrt((b.x-q.x)*(b.x-q.x)+(b.y-q.y)*(b.y-q.y)+(b.z-q.z)*(b.z-q.z));

	       float p = (ab + aq + bq) / 2;

	       float hh = (float) Math.sqrt(p * (p - ab) * (p - aq) * (p - bq));
	       float h;
	       if (ab!=0)
	    	   h = 2 * hh / ab; else h = 2*hh;
	       if (aq<h)
	    	   h = aq;
	       if (bq<h)
	    	   h = bq;

	       if (h<r)
	    	   return true; 
	       else 
	    	   return false;
	}
}
