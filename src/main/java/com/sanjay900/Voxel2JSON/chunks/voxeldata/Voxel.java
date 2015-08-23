package com.sanjay900.Voxel2JSON.chunks.voxeldata;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import lombok.AllArgsConstructor;
@AllArgsConstructor
public class Voxel {
	public Voxel(DataInputStream ds) throws IOException{
		x = ds.read();
		y = ds.read();
		z = ds.read();
		colourIndex = ds.read();
	}
	public Voxel clone() {
		return new Voxel(x,y,z,xamt,yamt,zamt,colourIndex,near,todraw);
	}
	public Coordinate getRelative(BlockFace b) {
		switch (b) {
		case XADD:
			return new Coordinate(x+1,y,z);
		case XSUB:
			return new Coordinate(x-1,y,z);
		case YADD:
			return new Coordinate(x,y+1,z);
		case YSUB:
			return new Coordinate(x,y-1,z);
		case ZADD:
			return new Coordinate(x,y,z+1);
		case ZSUB:
			return new Coordinate(x,y,z-1);
		default: 
			return new Coordinate(x,y,z);
		}

	}public ArrayList<Coordinate> getRelatives(BlockFace b) {
		ArrayList<Coordinate> coords = new ArrayList<>();
		coords.add(new Coordinate(xamt<0?0:xamt,yamt<0?0:yamt,zamt<0?0:zamt));
		switch (b) {
		case XSUB:
			for (int yd=yamt<0?yamt:0; yd<(yamt<0?0:yamt); yd++ ) {
				for (int zd=zamt<0?zamt:0; zd<(zamt<0?0:zamt); zd++ ) {
					if (xamt > 0)
						coords.add(new Coordinate(x+xamt+1,y+yd,z+zd));
					else
						coords.add(new Coordinate(x+1,y+yd,z+zd));

				}
			}
			break;
		case XADD:
			for (int yd=yamt<0?yamt:0; yd<(yamt<0?0:yamt); yd++ ) {
				for (int zd=zamt<0?zamt:0; zd<(zamt<0?0:zamt); zd++ ) {
					if (xamt > 0)
						coords.add(new Coordinate(x-1,y+yd,z+zd));
					else
						coords.add(new Coordinate(x+xamt-1,y+yd,z+zd));
				}
			}
			break;
		case YADD:
			for (int xd=xamt<0?xamt:0; xd<(xamt<0?0:xamt); xd++ ) {
				for (int zd=zamt<0?zamt:0; zd<(zamt<0?0:zamt); zd++ ) {
					if (yamt > 0)
						coords.add(new Coordinate(x+xd,y+yamt+1,z+zd));
					else
						coords.add(new Coordinate(x+xd,y+1,z+zd));

				}
			}
			break;
		case YSUB:
			for (int xd=xamt<0?xamt:0; xd<(xamt<0?0:xamt); xd++ ) {
				for (int zd=zamt<0?zamt:0; zd<(zamt<0?0:zamt); zd++ ) {
					if (yamt > 0)
						coords.add(new Coordinate(x+xd,y-1,z+zd));
					else
						coords.add(new Coordinate(x+xd,y+yamt-1,z+zd));
				}
			}
			break;
		case ZADD:
			for (int xd=xamt<0?xamt:0; xd<(xamt<0?0:xamt); xd++ ) {
				for (int yd=yamt<0?yamt:0; yd<(yamt<0?0:yamt); yd++ ) {
					if (zamt > 0)
						coords.add(new Coordinate(x+xd,y+yd,z+zamt+1));
					else
						coords.add(new Coordinate(x+xd,y+yd,z+1));
				}
			}
			break;
		case ZSUB:
			for (int xd=xamt<0?xamt:0; xd<(xamt<0?0:xamt); xd++ ) {
				for (int yd=yamt<0?yamt:0; yd<(yamt<0?0:yamt); yd++ ) {
					if (zamt > 0)
						coords.add(new Coordinate(x+xd,y+yd,z-1));
					else
						coords.add(new Coordinate(x+xd,y+yd,z+zamt-1));
				}
			}
			break;
		default: 
		}
		return coords;
		
	}
	public void expand(BlockFace b) {
		switch (b) {
		case XSUB:
			xamt++;
			return;
		case XADD:
			xamt--;
			xamt = xamt==0?-1:xamt;
			return;
		case YADD:
			yamt++;
			return;
		case YSUB:
			yamt--;
			yamt = yamt==0?-1:yamt;
			return;
		case ZSUB:
			zamt++;
			return;
		case ZADD:
			zamt--;
			zamt = zamt==0?-1:zamt;
			return;
		default: 
			return;
		}
	}
	public int x;
	public int y;
	public int z;
	//Size of voxel
	public int xamt = 1;
	public int yamt = 1;
	public int zamt = 1;
	public int colourIndex;
	public ArrayList<BlockFace> near = new ArrayList<>();

	public ArrayList<BlockFace> todraw = new ArrayList<>(Arrays.asList(BlockFace.values()));
}
