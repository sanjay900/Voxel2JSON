package com.sanjay900.Voxel2JSON.chunks;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import com.sanjay900.Voxel2JSON.Voxel2JSON;
import com.sanjay900.Voxel2JSON.chunks.voxeldata.BlockFace;
import com.sanjay900.Voxel2JSON.chunks.voxeldata.Coordinate;
import com.sanjay900.Voxel2JSON.chunks.voxeldata.Voxel;
import com.sanjay900.Voxel2JSON.progress.ProgressFrame;
import com.sanjay900.Voxel2JSON.utils.Utils;

import static com.sanjay900.Voxel2JSON.chunks.voxeldata.BlockFace.XADD;
import static com.sanjay900.Voxel2JSON.chunks.voxeldata.BlockFace.YADD;
import static com.sanjay900.Voxel2JSON.chunks.voxeldata.BlockFace.ZADD;

public class VoxelChunk extends Chunk{
    public ArrayList<Voxel> voxels = new ArrayList<>();
    public Map<Coordinate,Voxel> voxelc = new HashMap<>();
    public ProgressFrame frame;
    int numVoxels;
    MainChunk main;
    public VoxelChunk(DataInputStream ds, MainChunk main, boolean merge) throws IOException {
        super(ds);
        this.main = main;
        frame = new ProgressFrame();
        frame.contentPane.getVersionLbl().setText(".vox File Version: "+Voxel2JSON.getVersion());
        frame.setVisible(true);
        numVoxels = Utils.getInt(ds);
        frame.contentPane.getHeaderLbl().setText("Converting "+Voxel2JSON.name+".vox to PNG/JSON, please wait");
        frame.setTitle("Converting "+Voxel2JSON.name+".vox to PNG/JSON, please wait");
        frame.contentPane.getVoxelCount().setText("Voxels: "+numVoxels);
        frame.contentPane.getActionProgress().setMaximum(numVoxels);
        frame.contentPane.getOverallProgress().setMaximum(6);
        frame.contentPane.getAction().setText("Creating objects");
        for (int i = 0; i < numVoxels; i++) {
            Voxel v = new Voxel(ds);
            voxels.add(v);
            voxelc.put(new Coordinate(v), v);
            frame.contentPane.getActionProgress().setValue(i);
            frame.contentPane.getActionSubtitle().setText("Converting Voxel "+i+" out of "+numVoxels);
        }
        frame.contentPane.getActionProgress().setValue(0);
        int size = voxels.size();
        frame.contentPane.getAction().setText("Merging faces");
        frame.contentPane.getOverallProgress().setValue(2);
        frame.contentPane.getActionProgress().setValue(0);
        frame.contentPane.getActionProgress().setMaximum(size);

       Stream.of(XADD,YADD,ZADD).forEach(bf -> {
            Collections.sort(voxels, bf.order);
            ArrayList<Voxel> voxelTemp = new ArrayList<>(voxels);
            while (!voxelTemp.isEmpty()) {
                Voxel v = voxelTemp.get(0);
                List<Voxel> facing = v.getRelatives(bf,voxelc);
                if (facing.isEmpty() || facing.stream().anyMatch(rel -> rel == null || rel.colourIndex != v.colourIndex)) {
                    voxelTemp.remove(v);
                } else {
                    AtomicBoolean shouldEx = new AtomicBoolean(true);
                    facing.forEach(rel -> {
                        if (rel.xamt >1 || rel.yamt > 1||rel.zamt>1) {
                            switch(bf) {
                                case XADD:
                                    if (rel.y != v.y || rel.z != v.z || rel.yamt != v.yamt || rel.zamt != v.zamt) {
                                        shouldEx.set(false);
                                        return;
                                    }
                                    break;
                                case YADD:
                                    if (rel.x != v.x || rel.z != v.z || rel.xamt != v.xamt || rel.zamt != v.zamt) {
                                        shouldEx.set(false);
                                        return;
                                    }
                                case ZADD:
                                    if (rel.y != v.y || rel.x != v.x || rel.yamt != v.yamt || rel.xamt != v.xamt) {
                                        shouldEx.set(false);
                                        return;
                                    }
                            }
                        }
                        voxels.remove(rel);
                        voxelTemp.remove(rel);
                    });
                    if (!shouldEx.get()){
                        voxelTemp.remove(v);
                        continue;
                    }
                    v.expand(bf);
                }
            }
        });
        int i =0;
        for (Voxel v : voxels) {
            for (BlockFace b: BlockFace.values()) {
                //if (!voxelc.containsKey(v.getRelative(b))) {
                    v.uncovered.add(b);
                //}
            }
            frame.contentPane.getActionProgress().setValue(++i);
            frame.contentPane.getActionSubtitle().setText("Calculating merged face on Voxel "+i+" out of "+numVoxels);
        }
        frame.contentPane.getAction().setText("Tidying up");
        frame.contentPane.getActionProgress().setValue(5);
        frame.contentPane.getMergedVoxelCount().setText("Merged Voxels: "+(size-voxels.size())+" / "+size);
    }

}
