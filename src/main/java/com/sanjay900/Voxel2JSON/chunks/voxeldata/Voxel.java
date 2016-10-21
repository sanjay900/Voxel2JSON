package com.sanjay900.Voxel2JSON.chunks.voxeldata;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        return new Voxel(x,y,z,xamt,yamt,zamt,colourIndex, uncovered,todraw);
    }
    public Coordinate getRelative(BlockFace b) {
        switch (b) {
            case XADD:
                return new Coordinate(x+xamt,y,z);
            case YADD:
                return new Coordinate(x,y+yamt,z);
            case ZADD:
                return new Coordinate(x,y,z+zamt);
            case XSUB:
                return new Coordinate(x-1,y,z);
            case YSUB:
                return new Coordinate(x,y-1,z);
            case ZSUB:
                return new Coordinate(x,y,z-1);
            default:
                return new Coordinate(x,y,z);
        }

    }
    public List<Voxel> getRelatives(BlockFace b, Map<Coordinate, Voxel> voxels) {
        ArrayList<Voxel> coords = new ArrayList<>();
        switch (b) {
            case XADD:
                for (int yd=0; yd<yamt; yd++) {
                    for (int zd=0; zd<zamt; zd++) {
                        coords.add(voxels.get(new Coordinate(x+xamt,y+yd,z+zd)));
                    }
                }
                break;
            case YADD:
                for (int xd=0; xd<xamt; xd++) {
                    for (int zd=0; zd<zamt; zd++) {
                        coords.add(voxels.get(new Coordinate(x+xd,y+yamt,z+zd)));
                    }
                }
                break;
            case ZADD:
                for (int xd=0; xd<xamt; xd++) {
                    for (int yd=0; yd<yamt; yd++) {
                        coords.add(voxels.get(new Coordinate(x+xd,y+yd,z+zamt)));
                    }
                }
                break;
            default:
        }
        return coords;

    }
    public void expand(BlockFace b) {
        switch (b) {
            case XADD:
                xamt++;
                return;
            case YADD:
                yamt++;
                return;
            case ZADD:
                zamt++;
                return;
            default:
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
    public ArrayList<BlockFace> uncovered = new ArrayList<>();

    public ArrayList<BlockFace> todraw = new ArrayList<>(Arrays.asList(BlockFace.values()));

    @Override
    public String toString() {
        return "Voxel{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", xamt=" + xamt +
                ", yamt=" + yamt +
                ", zamt=" + zamt +
                ", colourIndex=" + colourIndex +
                '}';
    }

    public void reset() {
        xamt=yamt=zamt=1;
    }
}
