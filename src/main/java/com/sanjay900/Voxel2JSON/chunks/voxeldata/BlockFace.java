package com.sanjay900.Voxel2JSON.chunks.voxeldata;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BlockFace {
	XADD("west"),XSUB("east"),YADD("south"),YSUB("north"),ZADD("up"),ZSUB("down");
	public String dir;
}
