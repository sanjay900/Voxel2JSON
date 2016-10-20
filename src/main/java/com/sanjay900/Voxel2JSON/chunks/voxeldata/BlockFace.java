package com.sanjay900.Voxel2JSON.chunks.voxeldata;

import lombok.AllArgsConstructor;

import java.util.Comparator;

@AllArgsConstructor
public enum BlockFace {
	XADD("east", (v1, v2)-> v1.x-v2.x),YADD("up",(v1, v2)-> v1.y-v2.y),ZADD("south",(v1, v2)-> v1.z-v2.z),XSUB("west",null),YSUB("down",null),ZSUB("north",null);
	public String dir;
	public Comparator<Voxel> order;
}
