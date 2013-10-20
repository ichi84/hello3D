package net.d_ichi84.GL_test.GL.model;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Color;

import net.d_ichi84.framework.android.AndroidFileIO;
import net.d_ichi84.framework.android.Image;
import net.d_ichi84.framework.gl.GLGame;
import net.d_ichi84.framework.gl.GLGraphics;
import net.d_ichi84.framework.gl.Vertices;
import net.d_ichi84.framework.math.Vector3;

public class Map {
	public int unit_width;
	public int unit_height;
	final float YSCALE = 32f;
	
	GLGraphics glGraphics;
	public final List<MapPeace> mapPeaces = new ArrayList<MapPeace>(); //マップの一マス
	
	final Vector3[] v1 = new Vector3[3];
	final Vector3[] v2 = new Vector3[3];
	
	//描画オブジェクト
	Vertices	 map;  		//全体マップ(屋根)
	Vertices	 map_foot;  //全体マップ(崖部分)
	int gakeNum =0;
	
	public Map(GLGraphics glGraphics){
		this.glGraphics = glGraphics;
		
		for(int i=0; i<v1.length; i++)
			v1[i] = new Vector3();
		
		for(int i=0; i<v2.length; i++)
			v2[i] = new Vector3();
	}
	public float getHeight(int x, int z){
		int z_n = -z;
		return mapPeaces.get(z_n*unit_width + x).height;
	}
	
	public Vector3[] getTriangle1(int x, int z){
		v1[0].x = mapPeaces.get((-z)*unit_width + x).vertexRoofBuffer[0];
		v1[0].y = mapPeaces.get((-z)*unit_width + x).vertexRoofBuffer[1];
		v1[0].z = mapPeaces.get((-z)*unit_width + x).vertexRoofBuffer[2];
		
		v1[1].x = mapPeaces.get((-z)*unit_width + x).vertexRoofBuffer[5];
		v1[1].y = mapPeaces.get((-z)*unit_width + x).vertexRoofBuffer[6];
		v1[1].z = mapPeaces.get((-z)*unit_width + x).vertexRoofBuffer[7];
		
		v1[2].x = mapPeaces.get((-z)*unit_width + x).vertexRoofBuffer[10];
		v1[2].y = mapPeaces.get((-z)*unit_width + x).vertexRoofBuffer[11];
		v1[2].z = mapPeaces.get((-z)*unit_width + x).vertexRoofBuffer[12];
	
		return v1;
	}
	public Vector3[] getTriangle2(int x, int z){
		v2[0].x = mapPeaces.get((-z)*unit_width + x).vertexRoofBuffer[0];
		v2[0].y = mapPeaces.get((-z)*unit_width + x).vertexRoofBuffer[1];
		v2[0].z = mapPeaces.get((-z)*unit_width + x).vertexRoofBuffer[2];
		
		v2[1].x = mapPeaces.get((-z)*unit_width + x).vertexRoofBuffer[10];
		v2[1].y = mapPeaces.get((-z)*unit_width + x).vertexRoofBuffer[11];
		v2[1].z = mapPeaces.get((-z)*unit_width + x).vertexRoofBuffer[12];
		
		v2[2].x = mapPeaces.get((-z)*unit_width + x).vertexRoofBuffer[15];
		v2[2].y = mapPeaces.get((-z)*unit_width + x).vertexRoofBuffer[16];
		v2[2].z = mapPeaces.get((-z)*unit_width + x).vertexRoofBuffer[17];
		
		return v2;
	}
	
	public void draw(){
		map.bind();
		map.draw(GL10.GL_TRIANGLES, 0, mapPeaces.get(0).indexRoofBuffer.length *unit_width*unit_height);
		map.unbind();
		
		map_foot.bind();
		map_foot.draw(GL10.GL_TRIANGLES, 0, gakeNum*24);
		map_foot.unbind();
	}

	public void generateMap(){
		Image image = new Image(
				new AndroidFileIO(GLGame.context.getAssets()),
				"map1.png");

		float y0; //左上
		float y1; //左下
		float y2; //右下
		float y3; //右上
		
		unit_width = (image.width-1) /3;  //横のマス数
		unit_height = (image.height-1) /3;//縦のマス数
		float []y_map = new float [4*unit_width*unit_height];
		
		//高さマップを読み込み
		for(int y = 0; y<unit_height; y++ ){
			for(int x=0; x<unit_width; x++){
				int color = image.bitmap.getPixel(3*x+1, 3*y+1);
				int green = Color.green(color);
				y0 = green;
				
				color = image.bitmap.getPixel(3*x+1, 3*y+2);
				green = Color.green(color);
				y1 = green;
				
				color = image.bitmap.getPixel(3*x+2, 3*y+2);
				green = Color.green(color);
				y2 = green;
				
				color = image.bitmap.getPixel(3*x+2, 3*y+1);
				green = Color.green(color);
				y3 = green;
				
				y_map[4* (y*unit_width+x)]    = y0;//左上1
				y_map[4* (y*unit_width+x) +1] = y1;//左下2
				y_map[4* (y*unit_width+x) +2] = y2;//右下3
				y_map[4* (y*unit_width+x) +3] = y3;//右上4
			}
		}
		
		//段差をつないでなめらかにする
		float []y_map_n = new float [4*unit_width*unit_height];
		for(int i=0;i<4*unit_width*unit_height; i++)
			y_map_n[i] = y_map[i];
		
		for(int y = 0; y<unit_height-1; y++ ){
			for(int x=0; x<unit_width-1; x++){
				//今見てるマス
				float p_y2 = y_map[4* (y*unit_width+x) +2];//右下3	
				//右隣
				float r_y1 = y_map[4* (y*unit_width+x+1) +1];//左下2
				
				//下
				float u_y3 = y_map[4* ((y+1)*unit_width+x) +3];//右上4
				//右下
				float ur_y0 = y_map[4* ((y+1)*unit_width+x+1)];//左上1
			
				float out=p_y2;
				int num=1;
				int color; 
				color = image.bitmap.getPixel(3*x+3, 3*y+1);//右側
				int red= Color.red(color);
				if(red !=255){
					out += r_y1;
					num++;
				}
				color = image.bitmap.getPixel(3*x+1, 3*y+3);//　下
				red= Color.red(color);
				if(red !=255){
					out += u_y3;
					num++;
				}
				color = image.bitmap.getPixel(3*x+3, 3*y+3);//右下
				red= Color.red(color);
				if(red !=255){
					out += ur_y0;
					num++;
				}
				
				//4マスの中心、右下を平均値にする
				y_map_n[4* (y*unit_width+x) +2] = 
						out/(float)num;
			}
		}
		
		for(int y = 0; y<unit_height-1; y++ ){
			for(int x=1; x<unit_width; x++){
				//今見てるマス
				float p_y1 = y_map[4* (y*unit_width+x) +1];//左下2
				//左隣
				float l_y2 = y_map[4* (y*unit_width+x-1) +2];//右下3
				//下
				float u_y0 = y_map[4* ((y+1)*unit_width+x)];//左上1
				
				//左下
				float ul_y3 = y_map[4* ((y+1)*unit_width+x-1) +3];//右上4
				
				float out=p_y1;
				int num=1;
				int color; 
				color = image.bitmap.getPixel(3*x+0, 3*y+1);//左側
				int red= Color.red(color);
				if(red !=255){
					out += l_y2;
					num++;
				}
				color = image.bitmap.getPixel(3*x+1, 3*y+3);//　下
				red= Color.red(color);
				if(red !=255){
					out += u_y0;
					num++;
				}
				color = image.bitmap.getPixel(3*x+0, 3*y+3);//左下
				red= Color.red(color);
				if(red !=255){
					out += ul_y3;
					num++;
				}
				
				//4マスの中心、左下を平均値にする
				y_map_n[4* (y*unit_width+x) +1] = 
						out/(float)num;
			}
		}
		
		for(int y = 1; y<unit_height; y++ ){
			for(int x=0; x<unit_width-1; x++){
				//今見てるマス
				float p_y3 = y_map[4* (y*unit_width+x) +3];//右上4
				//右隣
				float r_y0 = y_map[4* (y*unit_width+x+1)];//左上1
				//上
				float u_y2 = y_map[4* ((y-1)*unit_width+x) +2];//右下3
				//右上
				float ur_y1 = y_map[4* ((y-1)*unit_width+x+1) +1];//左下2
				
				float out=p_y3;
				int num=1;
				int color; 
				color = image.bitmap.getPixel(3*x+3, 3*y+1);//右側
				int red= Color.red(color);
				if(red !=255){
					out += r_y0;
					num++;
				}
				color = image.bitmap.getPixel(3*x+1, 3*y+0);//　上
				red= Color.red(color);
				if(red !=255){
					out += u_y2;
					num++;
				}
				color = image.bitmap.getPixel(3*x+3, 3*y+0);//右上
				red= Color.red(color);
				if(red !=255){
					out += ur_y1;
					num++;
				}		
				//4マスの中心、右上を平均値にする
				y_map_n[4* (y*unit_width+x) +3] = 
						out/(float)num;
			}
		}
		
		
		for(int y = 1; y<unit_height; y++ ){
			for(int x=1; x<unit_width; x++){
				//今見てるマス
				float p_y3 = y_map[4* (y*unit_width+x) +0];//右上4
				//左隣
				float l_y3 = y_map[4* (y*unit_width+x-1) +3];//右上4
				//上
				float u_y1 = y_map[4* ((y-1)*unit_width+x) +1];//左下2
				//左上
				float ul_y2 = y_map[4* ((y-1)*unit_width+x-1) +2];//右下3
				
				
				float out=p_y3;
				int num=1;
				int color; 
				color = image.bitmap.getPixel(3*x+0, 3*y+1);//左側
				int red= Color.red(color);
				if(red !=255){
					out += l_y3;
					num++;
				}
				color = image.bitmap.getPixel(3*x+1, 3*y+0);//　上
				red= Color.red(color);
				if(red !=255){
					out += u_y1;
					num++;
				}
				color = image.bitmap.getPixel(3*x+0, 3*y+0);//左上
				red= Color.red(color);
				if(red !=255){
					out += ul_y2;
					num++;
				}
				
				//4マスの中心、左上を平均値にする
				y_map_n[4* (y*unit_width+x) +0] = 
						out/(float)num;
			}
		}
		//端っこ。上端と下端
		for(int x=0; x <unit_width-1 ;x++){
			int color; 
			color = image.bitmap.getPixel(3*x+0, 0);
			int red= Color.red(color);
			if(red !=255){
				y_map_n[4*x+3] = y_map_n[4*(x+1)];
			}
			
			color = image.bitmap.getPixel(3*x+0, 3*unit_height+0);
			red= Color.red(color);
			if(red !=255){
				y_map_n[4* ((unit_height-1)*unit_width+x)+2] 
					= y_map_n[4*((unit_height-1)*unit_width +x+1) +1];//左下
			}
		}
		//端っこ。左端と右端
		for(int y=0; y <unit_height-1 ;y++){
			int color; 
			color = image.bitmap.getPixel(3, 3*y+3);
			int red= Color.red(color);
			if(red !=255){
				y_map_n[4* ((y+1)*unit_width)] = y_map_n[4* (y*unit_width) +1];//左下
			}
			color = image.bitmap.getPixel(3*unit_width+0, 3*y+3);
			red= Color.red(color);
			if(red !=255){
				y_map_n[4* ((y+1)*unit_width + unit_width-1)+3] 
					= y_map_n[4* (y*unit_width +unit_width-1) +2];//左下
			}
		}
		
		//マップを1マスずつ生成
		for(int z=0, i=0; z>-unit_height ; z--)
			for(int x=0; x<unit_width ; x++, i++){
			{
				int Y =  unit_height+z -1;
				mapPeaces.add(  new MapPeace(x, 0, z,
				y_map_n[4* (Y*unit_width+x) +1]/YSCALE,//左下2		
				y_map_n[4* (Y*unit_width+x)]   /YSCALE,//左上1 
				y_map_n[4* (Y*unit_width+x) +3]/YSCALE,//右上4
				y_map_n[4* (Y*unit_width+x) +2]/YSCALE,//右下3
				 i, x, z) );
			}
		}
		
		//まとめて１つのVerticesにする
		//まずは領域確保。要素数を数える
		int totalVertexNum = 0;
		int totalRoofVertexNum = 0;
		int totalIndexNum = 0;
		int totalRoofIndexNum = 0;
		
		for(int z=0, i=0; z>-unit_height ; z--){
			for(int x=0 ; x<unit_width ; x++, i++){
				totalVertexNum += mapPeaces.get(i).vertexNum;
				totalRoofVertexNum += mapPeaces.get(i).vertexRoofNum;
				totalIndexNum  += mapPeaces.get(i).indexNum;
				totalRoofIndexNum  += mapPeaces.get(i).indexNum;
			}
		}
		float [] totalVertexBuffer = new float[totalVertexNum];
		float [] totalRoofVertexBuffer = new float[totalRoofVertexNum];
		short [] totalIndexBuffer = new short[totalIndexNum];    
		short [] totalIndexRoofBuffer = new short[totalRoofIndexNum];    
		
		
		//崖部分のマップを作る
		boolean [] gake = new boolean[unit_width*unit_height];
		for(int y=0; y<unit_height ; y++){
			for(int x=0; x<unit_width ; x++){
				int color_left = image.bitmap.getPixel(3*x, 3*y +1);
				int color_up = image.bitmap.getPixel(3*x+1, 3*y +0);
				int color_under = image.bitmap.getPixel(3*x+1, 3*y +3);
				int color_Right = image.bitmap.getPixel(3*x+3, 3*y+1);
				int color_upLeft = image.bitmap.getPixel(3*x, 3*y);  //バグ対策
				int color_upRight = image.bitmap.getPixel(3*x+3, 3*y);//バグ対策
				
				//上下左右どこかに赤がある
				if(Color.red(color_left)==255 ||
					Color.red(color_up)==255 ||
					Color.red(color_under)==255 ||
					Color.red(color_Right)==255 ||
					Color.red(color_upLeft) ==255 ||
					Color.red(color_upRight) ==255 ){
					gake[y*unit_width+x] = true; //崖だよ
				}else{
					gake[y*unit_width+x] = false;//崖じゃないよ
				}			
			}
		}
		
		//一つずつ地道にコピる		
		for(int z=0, i=0; z>-unit_height ; z--){
			for(int x=0; x<unit_width ; x++, i++){

				if( gake[(unit_height+z-1)*unit_width + x] ){//崖周辺だったら
					for(int j =0; j<mapPeaces.get(i).vertexNum; j++){ //頂点を加える
						totalVertexBuffer[gakeNum*mapPeaces.get(i).vertexNum + j] 
								= mapPeaces.get(i).vertexBuffer[j];
					}
					gakeNum++;
				}
				
				for(int j =0; j<mapPeaces.get(i).vertexRoofNum; j++){//ネスト深くてごめんなさい
					totalRoofVertexBuffer[i*mapPeaces.get(i).vertexRoofNum + j] //自分でも訳わかんなくなってきた
							= mapPeaces.get(i).vertexRoofBuffer[j];
				}
				
				
				for(int j =0; j<mapPeaces.get(i).indexNum; j++){
					totalIndexBuffer[i*mapPeaces.get(i).indexNum + j]
							= mapPeaces.get(i).indexBuffer[j];
				}
				
				for(int j =0; j<mapPeaces.get(i).indexRoofNum; j++){
					totalIndexRoofBuffer[i*mapPeaces.get(i).indexRoofNum + j]
							= mapPeaces.get(i).indexRoofBuffer[j];
				}
				
				
			}
		}
		
		//頂点クラス  、カラーなし、テクスチャあり
		map_foot = new Vertices(glGraphics, gakeNum*16, gakeNum*24, false, true);
		map_foot.setVertices(totalVertexBuffer , 0, gakeNum*16*5);
		map_foot.setIndices(totalIndexBuffer, 0,  gakeNum*24);
		
		map = new Vertices(glGraphics, totalRoofVertexNum/5, totalRoofIndexNum, false, true);
		map.setVertices(totalRoofVertexBuffer , 0, totalRoofVertexBuffer.length);
		map.setIndices(totalIndexRoofBuffer, 0, totalIndexRoofBuffer.length);
	}

}
