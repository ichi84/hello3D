package net.d_ichi84.framework.gl;

public class Animation {
	public static final int ANIMAION_LOOPING=0;
	public static final int ANIMAION_NONLOOPING=1;
	final TextureRegion[] keyFrames;
	final float frameDuration;
	
	public Animation(float frameDuration, TextureRegion ...  keyFrames){
		this.frameDuration = frameDuration;
		this.keyFrames = keyFrames;
	}
	
	public TextureRegion getKeyFrame(float stateTime, int mode){
		int frameNumber = (int)(stateTime / frameDuration);
		
		if(mode == ANIMAION_NONLOOPING){
			frameNumber = Math.min(keyFrames.length-1, frameNumber);
		}else{
			frameNumber = frameNumber % keyFrames.length;
		}
		
		return keyFrames[frameNumber];
	}
}
