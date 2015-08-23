package com.sanjay900.Voxel2JSON.chunks.voxeldata;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import com.sanjay900.Voxel2JSON.Voxel2JSON;
import com.sanjay900.Voxel2JSON.chunks.SizeChunk;

public class RenderVoxel {

	public BigDecimal x;
	public BigDecimal y;
	public BigDecimal z;
	public BigDecimal xamt;
	public BigDecimal yamt;
	public BigDecimal zamt;
	public int colourIndex;
	public ArrayList<BlockFace> near = new ArrayList<>();
	public ArrayList<BlockFace> todraw = new ArrayList<>();
	public RenderVoxel(Voxel v, SizeChunk sizeChunk) {
		x = new BigDecimal(v.x);
		y = new BigDecimal(v.y);
		z = new BigDecimal(v.z);
		colourIndex = v.colourIndex;
		BigDecimal s = new BigDecimal(Voxel2JSON.size);
		BigDecimal xdiff = s.divide(sizeChunk.x,3, RoundingMode.HALF_UP);
		s = s.divide(sizeChunk.biggest, 5, RoundingMode.HALF_UP).multiply(sizeChunk.x);
		x = x.divide(sizeChunk.x,5, RoundingMode.HALF_UP).multiply(s);
		s = new BigDecimal(Voxel2JSON.size);
		s = s.divide(sizeChunk.biggest, 5, RoundingMode.HALF_UP).multiply(sizeChunk.y);
		y = y.divide(sizeChunk.y,5, RoundingMode.HALF_UP).multiply(s);
		s = new BigDecimal(Voxel2JSON.size);
		s = s.divide(sizeChunk.biggest, 5, RoundingMode.HALF_UP).multiply(sizeChunk.z);
		z = z.divide(sizeChunk.z,5, RoundingMode.HALF_UP).multiply(s);
		s = new BigDecimal(Voxel2JSON.size==16?16:32);
		s = s.subtract(xdiff);
		x = s.subtract(x);
		s = new BigDecimal(Voxel2JSON.size==16?0:-16);
		y = s.add(y);
		z = s.add(z);
		xamt = new BigDecimal(v.xamt);
		yamt = new BigDecimal(v.yamt);
		zamt = new BigDecimal(v.zamt);
		near = v.near;
		todraw = v.todraw;
	}
}
