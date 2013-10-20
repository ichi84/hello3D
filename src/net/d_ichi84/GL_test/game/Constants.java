package net.d_ichi84.GL_test.game;

import net.d_ichi84.framework.math.Vector2;

public class Constants {
	public static final float CAMERA_NEAR_LIMIT = 10.0f;	//カメラから注視点への距離の最短リミット
	public static final float CAMERA_FAR_LIMIT  = 40.0f;	//カメラから注視点への距離の最長リミット
	public static final float angleV = 67.0f *Vector2.TO_RADIANS; //見下ろす角度
	public static final float fovY = 45;	//視野角
	public static final float nearZ = 0.1f;	//ニアクリップ面
	public static final float farZ = 100;	//ファークリップ面
}
