package net.d_ichi84.framework.math;



public class Tool3D {

	// 線分と三角形の交差（実例で学ぶゲーム3D数学より改変）
	// 間違えて改変している可能性あり
	// 引数：ベクトルの始点、方向ベクトル＊長さ、三角ポリゴンの座標1、座標2、座標3、最低距離
	static public float VectorTriangleIntersect(Vector3 Org, Vector3 Direction,
			Vector3 pos0, Vector3 pos1, Vector3 pos2, float minT){
	    final float kNoIntersection = -1000;//交差しない場合の値。ファークリップ面が100程度だからあり得ない数字

	    Vector3 e1;
	    Vector3 e2;
	    Vector3 n;
	    
	    e1 = pos1.sub( pos0 );
	    e2 = pos2.sub( pos1 );	    

	    n = e1.crossProduct(e2);
	    float dot = n.dotProduct(Direction);

	    if (!(dot < 0.0f)){//交差しない
	        return kNoIntersection;
	    }

	    float d = n.dotProduct( pos0);
	    float t = d - n.dotProduct(Org);

	    if (!(t <= 0.0f)){
	        return kNoIntersection;
	    }

	    if (!(t >= dot*minT)){
	        return kNoIntersection;
	    }

	    t /= dot;

	    Vector3 p;
	    p = Org.add( Direction.mul(t));
	     
	    float u0, u1, u2;
	    float v0, v1, v2;

	    if (Math.abs(n.x) > Math.abs(n.y)){
	        if (Math.abs(n.x) > Math.abs(n.z)){
	            u0 = p.y - pos0.y;
	            u1 = pos1.y - pos0.y;
	            u2 = pos2.y - pos0.y;

	            v0 = p.z - pos0.z;
	            v1 = pos1.z - pos0.z;
	            v2 = pos2.z - pos0.z;
	        }
	        else{
	            u0 = p.x - pos0.x;
	            u1 = pos1.x - pos0.x;
	            u2 = pos2.x - pos0.x;

	            v0 = p.y - pos0.y;
	            v1 = pos1.y - pos0.y;
	            v2 = pos2.y - pos0.y;
	        }
	    }
	    else{
	        if (Math.abs(n.x) > Math.abs(n.z)){
	            u0 = p.x - pos0.x;
	            u1 = pos1.x - pos0.x;
	            u2 = pos2.x - pos0.x;

	            v0 = p.z - pos0.z;
	            v1 = pos1.z - pos0.z;
	            v2 = pos2.z - pos0.z;
	        }
	        else{
	            u0 = p.x - pos0.x;
	            u1 = pos1.x - pos0.x;
	            u2 = pos2.x - pos0.x;

	            v0 = p.y - pos0.y;
	            v1 = pos1.y - pos0.y;
	            v2 = pos2.y - pos0.y;
	        }
	    }
	    float temp = u1 * v2 - v1 * u2;
	    if (!(temp != 0.0f)){
	        return kNoIntersection;
	    }
	    temp = 1.0f / temp;

	    float alpha = (u0 * v2 - v0 * u2) * temp;
	    if (!(alpha >= 0.0f)){
	        return kNoIntersection;
	    }

	    float beta = (u1 * v0 - v1 * u0) * temp;
	    if (!(beta >= 0.0f)){
	        return kNoIntersection;
	    }

	    float gamma = 1.0f - alpha - beta;
	    if (!(gamma >= 0.0f)){
	        return kNoIntersection;
	    }

	    return t; //交差する最小の距離。別にいらんけど
	}
}
