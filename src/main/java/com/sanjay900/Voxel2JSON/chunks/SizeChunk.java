package com.sanjay900.Voxel2JSON.chunks;

import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigDecimal;

import com.sanjay900.Voxel2JSON.utils.Utils;

public class SizeChunk extends Chunk{
	public int x;
	public int y;
	public int z;
	public SizeChunk(DataInputStream ds) throws IOException {
		super(ds);
		x = Utils.getInt(ds);
		y = Utils.getInt(ds);
		z = Utils.getInt(ds);
	}
}
