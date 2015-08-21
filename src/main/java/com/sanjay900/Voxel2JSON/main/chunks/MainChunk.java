package com.sanjay900.Voxel2JSON.main.chunks;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.sanjay900.Voxel2JSON.main.Pallete;
import com.sanjay900.Voxel2JSON.main.Voxel2JSON;

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
			Voxel2JSON.infoBox(new String[]{"Pallete not found! Using default."},"Warning");
			System.out.println("Pallete not found! Using default.");
			
			Files.copy(Voxel2JSON.class.getResourceAsStream("/default.png"), new File(Voxel2JSON.getPath()+".png").toPath(),
					   StandardCopyOption.REPLACE_EXISTING);
			
		}
	}
	@Override
	public void printExtraInfo() {
		sizeChunk.printInfo();
		voxelChunk.printInfo();
	}
	

}
