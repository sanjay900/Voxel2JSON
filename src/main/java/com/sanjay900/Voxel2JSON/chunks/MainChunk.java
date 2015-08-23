package com.sanjay900.Voxel2JSON.chunks;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.sanjay900.Voxel2JSON.Voxel2JSON;
import com.sanjay900.Voxel2JSON.json.Pallete;

public class MainChunk extends Chunk {
	public SizeChunk sizeChunk;
	public VoxelChunk voxelChunk;
	public MainChunk(DataInputStream ds, boolean b) throws IOException {
		super(ds);
		sizeChunk = new SizeChunk(ds);
		voxelChunk = new VoxelChunk(ds,this,b);
		if (ds.available() > 0) {
			Voxel2JSON.p = new Pallete(ds);
		} else {
			Voxel2JSON.p = new Pallete(null);
			System.out.println("Pallete not found! Using default.");
			Files.copy(Voxel2JSON.class.getResourceAsStream("/default.png"), new File(Voxel2JSON.getPath()+".png").toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
	}
}
