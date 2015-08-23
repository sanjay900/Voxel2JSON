package com.sanjay900.Voxel2JSON.chunks.voxeldata;

public class Coordinate {
	int x;
	int y;
	int z;
	public Coordinate(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Coordinate(Voxel voxel) {
		x = voxel.x;
		y = voxel.y;
		z = voxel.z;
	}
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Coordinate)) return false;
		Coordinate oc = (Coordinate) other;
		return (oc.x == x &&oc.y == y&&oc.z == z);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		int temp;
		temp = x;
		result = prime * result + (temp ^ (temp >>> 32));
		temp = y;
		result = prime * result + (temp ^ (temp >>> 32));
		temp = z;
		result = prime * result + (temp ^ (temp >>> 32));
		return result;
	}
	@Override
	public String toString() {
		return x+","+y+","+z;
	}
}
