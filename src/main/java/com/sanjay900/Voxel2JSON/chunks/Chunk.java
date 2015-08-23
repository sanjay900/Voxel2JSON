package com.sanjay900.Voxel2JSON.chunks;

import java.io.DataInputStream;
import java.io.IOException;

import com.sanjay900.Voxel2JSON.utils.Utils;

public abstract class Chunk {
	String id;
	int size;
	int childSize;
	public Chunk (DataInputStream ds) throws IOException{
		id = Utils.readString(ds);
		size = Utils.getInt(ds);
		childSize = Utils.getInt(ds);
	}
}
