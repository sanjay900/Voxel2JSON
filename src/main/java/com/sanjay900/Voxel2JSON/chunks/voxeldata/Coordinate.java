package com.sanjay900.Voxel2JSON.chunks.voxeldata;

public class Coordinate {
	public int x;
    public int y;
    public int z;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        return x == that.x && y == that.y && z == that.z;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }

    @Override
	public String toString() {
		return x+","+y+","+z;
	}
}
