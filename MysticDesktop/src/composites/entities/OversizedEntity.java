package composites.entities;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import composites.map.Chunk;
import gebd.Render;

public class OversizedEntity extends Entity{
	
	protected int frameNumberSync = 0;
	protected int uniqueId = -1;
	
	protected ArrayList<Chunk> chunksILiveIn;
	
	public OversizedEntity (String modelPath, String texturePath, Vector3f position, Vector3f size, Vector3f rotation, ArrayList<Chunk> chunksILiveIn){
		super(modelPath, texturePath, position, size, rotation);
		this.chunksILiveIn = chunksILiveIn;
	}
	
	public boolean hasBeenPreparedThisFrame(){
		if(frameNumberSync == Render.frameNumber){
			return true;
		}
		frameNumberSync = Render.frameNumber;
		return false;
	}
}
