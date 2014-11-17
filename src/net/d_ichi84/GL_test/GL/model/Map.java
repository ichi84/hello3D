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
	public final List<MapPeace> mapPeaces = new ArrayList<MapPeace>(); //�}�b�v�̈�}�X
	
	final Vector3[] v1 = new Vector3[3];
	final Vector3[] v2 = new Vector3[3];
	
	//�`��I�u�W�F�N�g
	Vertices	 map;  		//�S�̃}�b�v(����)
	Vertices	 map_foot;  //�S�̃}�b�v(�R����)
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

		float y0; //����
		float y1; //����
		float y2; //�E��
		float y3; //�E��
		
		unit_width = (image.width-1) /3;  //���̃}�X��
		unit_height = (image.height-1) /3;//�c�̃}�X��
		float []y_map = new float [4*unit_width*unit_height];
		
		//�����}�b�v��ǂݍ���
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
				
				y_map[4* (y*unit_width+x)]    = y0;//����1
				y_map[4* (y*unit_width+x) +1] = y1;//����2
				y_map[4* (y*unit_width+x) +2] = y2;//�E��3
				y_map[4* (y*unit_width+x) +3] = y3;//�E��4
			}
		}
		
		//�i�����Ȃ��łȂ߂炩�ɂ���
		float []y_map_n = new float [4*unit_width*unit_height];
		for(int i=0;i<4*unit_width*unit_height; i++)
			y_map_n[i] = y_map[i];
		
		for(int y = 0; y<unit_height-1; y++ ){
			for(int x=0; x<unit_width-1; x++){
				//�����Ă�}�X
				float p_y2 = y_map[4* (y*unit_width+x) +2];//�E��3	
				//�E��
				float r_y1 = y_map[4* (y*unit_width+x+1) +1];//����2
				
				//��
				float u_y3 = y_map[4* ((y+1)*unit_width+x) +3];//�E��4
				//�E��
				float ur_y0 = y_map[4* ((y+1)*unit_width+x+1)];//����1
			
				float out=p_y2;
				int num=1;
				int color; 
				color = image.bitmap.getPixel(3*x+3, 3*y+1);//�E��
				int red= Color.red(color);
				if(red !=255){
					out += r_y1;
					num++;
				}
				color = image.bitmap.getPixel(3*x+1, 3*y+3);//�@��
				red= Color.red(color);
				if(red !=255){
					out += u_y3;
					num++;
				}
				color = image.bitmap.getPixel(3*x+3, 3*y+3);//�E��
				red= Color.red(color);
				if(red !=255){
					out += ur_y0;
					num++;
				}
				
				//4�}�X�̒��S�A�E���𕽋ϒl�ɂ���
				y_map_n[4* (y*unit_width+x) +2] = 
						out/(float)num;
			}
		}
		
		for(int y = 0; y<unit_height-1; y++ ){
			for(int x=1; x<unit_width; x++){
				//�����Ă�}�X
				float p_y1 = y_map[4* (y*unit_width+x) +1];//����2
				//����
				float l_y2 = y_map[4* (y*unit_width+x-1) +2];//�E��3
				//��
				float u_y0 = y_map[4* ((y+1)*unit_width+x)];//����1
				
				//����
				float ul_y3 = y_map[4* ((y+1)*unit_width+x-1) +3];//�E��4
				
				float out=p_y1;
				int num=1;
				int color; 
				color = image.bitmap.getPixel(3*x+0, 3*y+1);//����
				int red= Color.red(color);
				if(red !=255){
					out += l_y2;
					num++;
				}
				color = image.bitmap.getPixel(3*x+1, 3*y+3);//�@��
				red= Color.red(color);
				if(red !=255){
					out += u_y0;
					num++;
				}
				color = image.bitmap.getPixel(3*x+0, 3*y+3);//����
				red= Color.red(color);
				if(red !=255){
					out += ul_y3;
					num++;
				}
				
				//4�}�X�̒��S�A�����𕽋ϒl�ɂ���
				y_map_n[4* (y*unit_width+x) +1] = 
						out/(float)num;
			}
		}
		
		for(int y = 1; y<unit_height; y++ ){
			for(int x=0; x<unit_width-1; x++){
				//�����Ă�}�X
				float p_y3 = y_map[4* (y*unit_width+x) +3];//�E��4
				//�E��
				float r_y0 = y_map[4* (y*unit_width+x+1)];//����1
				//��
				float u_y2 = y_map[4* ((y-1)*unit_width+x) +2];//�E��3
				//�E��
				float ur_y1 = y_map[4* ((y-1)*unit_width+x+1) +1];//����2
				
				float out=p_y3;
				int num=1;
				int color; 
				color = image.bitmap.getPixel(3*x+3, 3*y+1);//�E��
				int red= Color.red(color);
				if(red !=255){
					out += r_y0;
					num++;
				}
				color = image.bitmap.getPixel(3*x+1, 3*y+0);//�@��
				red= Color.red(color);
				if(red !=255){
					out += u_y2;
					num++;
				}
				color = image.bitmap.getPixel(3*x+3, 3*y+0);//�E��
				red= Color.red(color);
				if(red !=255){
					out += ur_y1;
					num++;
				}		
				//4�}�X�̒��S�A�E��𕽋ϒl�ɂ���
				y_map_n[4* (y*unit_width+x) +3] = 
						out/(float)num;
			}
		}
		
		
		for(int y = 1; y<unit_height; y++ ){
			for(int x=1; x<unit_width; x++){
				//�����Ă�}�X
				float p_y3 = y_map[4* (y*unit_width+x) +0];//�E��4
				//����
				float l_y3 = y_map[4* (y*unit_width+x-1) +3];//�E��4
				//��
				float u_y1 = y_map[4* ((y-1)*unit_width+x) +1];//����2
				//����
				float ul_y2 = y_map[4* ((y-1)*unit_width+x-1) +2];//�E��3
				
				
				float out=p_y3;
				int num=1;
				int color; 
				color = image.bitmap.getPixel(3*x+0, 3*y+1);//����
				int red= Color.red(color);
				if(red !=255){
					out += l_y3;
					num++;
				}
				color = image.bitmap.getPixel(3*x+1, 3*y+0);//�@��
				red= Color.red(color);
				if(red !=255){
					out += u_y1;
					num++;
				}
				color = image.bitmap.getPixel(3*x+0, 3*y+0);//����
				red= Color.red(color);
				if(red !=255){
					out += ul_y2;
					num++;
				}
				
				//4�}�X�̒��S�A����𕽋ϒl�ɂ���
				y_map_n[4* (y*unit_width+x) +0] = 
						out/(float)num;
			}
		}
		//�[�����B��[�Ɖ��[
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
					= y_map_n[4*((unit_height-1)*unit_width +x+1) +1];//����
			}
		}
		//�[�����B���[�ƉE�[
		for(int y=0; y <unit_height-1 ;y++){
			int color; 
			color = image.bitmap.getPixel(3, 3*y+3);
			int red= Color.red(color);
			if(red !=255){
				y_map_n[4* ((y+1)*unit_width)] = y_map_n[4* (y*unit_width) +1];//����
			}
			color = image.bitmap.getPixel(3*unit_width+0, 3*y+3);
			red= Color.red(color);
			if(red !=255){
				y_map_n[4* ((y+1)*unit_width + unit_width-1)+3] 
					= y_map_n[4* (y*unit_width +unit_width-1) +2];//����
			}
		}
		
		//�}�b�v��1�}�X������
		for(int z=0, i=0; z>-unit_height ; z--)
			for(int x=0; x<unit_width ; x++, i++){
			{
				int Y =  unit_height+z -1;
				mapPeaces.add(  new MapPeace(x, 0, z,
				y_map_n[4* (Y*unit_width+x) +1]/YSCALE,//����2		
				y_map_n[4* (Y*unit_width+x)]   /YSCALE,//����1 
				y_map_n[4* (Y*unit_width+x) +3]/YSCALE,//�E��4
				y_map_n[4* (Y*unit_width+x) +2]/YSCALE,//�E��3
				 i, x, z) );
			}
		}
		
		//�܂Ƃ߂ĂP��Vertices�ɂ���
		//�܂��͗̈�m�ہB�v�f���𐔂���
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
		
		
		//�R�����̃}�b�v�����
		boolean [] gake = new boolean[unit_width*unit_height];
		for(int y=0; y<unit_height ; y++){
			for(int x=0; x<unit_width ; x++){
				int color_left = image.bitmap.getPixel(3*x, 3*y +1);
				int color_up = image.bitmap.getPixel(3*x+1, 3*y +0);
				int color_under = image.bitmap.getPixel(3*x+1, 3*y +3);
				int color_Right = image.bitmap.getPixel(3*x+3, 3*y+1);
				int color_upLeft = image.bitmap.getPixel(3*x, 3*y);  //�o�O�΍�
				int color_upRight = image.bitmap.getPixel(3*x+3, 3*y);//�o�O�΍�
				
				//�㉺���E�ǂ����ɐԂ�����
				if(Color.red(color_left)==255 ||
					Color.red(color_up)==255 ||
					Color.red(color_under)==255 ||
					Color.red(color_Right)==255 ||
					Color.red(color_upLeft) ==255 ||
					Color.red(color_upRight) ==255 ){
					gake[y*unit_width+x] = true; //�R����
				}else{
					gake[y*unit_width+x] = false;//�R����Ȃ���
				}			
			}
		}
		
		//����n���ɃR�s��		
		for(int z=0, i=0; z>-unit_height ; z--){
			for(int x=0; x<unit_width ; x++, i++){

				if( gake[(unit_height+z-1)*unit_width + x] ){//�R���ӂ�������
					for(int j =0; j<mapPeaces.get(i).vertexNum; j++){ //���_��������
						totalVertexBuffer[gakeNum*mapPeaces.get(i).vertexNum + j] 
								= mapPeaces.get(i).vertexBuffer[j];
					}
					gakeNum++;
				}
				
				for(int j =0; j<mapPeaces.get(i).vertexRoofNum; j++){//�l�X�g�[���Ă��߂�Ȃ���
					totalRoofVertexBuffer[i*mapPeaces.get(i).vertexRoofNum + j] //�����ł���킩��Ȃ��Ȃ��Ă���
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
		
		//���_�N���X  �A�J���[�Ȃ��A�e�N�X�`������
		map_foot = new Vertices(glGraphics, gakeNum*16, gakeNum*24, false, true);
		map_foot.setVertices(totalVertexBuffer , 0, gakeNum*16*5);
		map_foot.setIndices(totalIndexBuffer, 0,  gakeNum*24);
		
		map = new Vertices(glGraphics, totalRoofVertexNum/5, totalRoofIndexNum, false, true);
		map.setVertices(totalRoofVertexBuffer , 0, totalRoofVertexBuffer.length);
		map.setIndices(totalIndexRoofBuffer, 0, totalIndexRoofBuffer.length);
	}

}
