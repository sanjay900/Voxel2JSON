package com.sanjay900.Voxel2JSON.chunks.voxeldata;

import lombok.AllArgsConstructor;

import java.util.Comparator;

@AllArgsConstructor
public enum BlockFace {
	XADD("west", (v1, v2)-> v1.x-v2.x),YADD("north",(v1, v2)-> v1.y-v2.y),ZADD("down",(v1, v2)-> v1.z-v2.z),XSUB("east",null),YSUB("south",null),ZSUB("up",null);
	public String dir;
	public Comparator<Voxel> order;
}
