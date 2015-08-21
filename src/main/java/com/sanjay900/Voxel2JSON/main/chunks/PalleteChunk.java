package com.sanjay900.Voxel2JSON.main.chunks;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

public class PalleteChunk extends Chunk{
	public Color[] pallete = new Color[256];
	public PalleteChunk(DataInputStream ds) throws IOException {
		super(ds);
		for (int i = 1; i < 256; i++) {
			int r = ds.read();
			int g = ds.read();
			int b = ds.read();
			ds.read();
			pallete[i] = new Color(r,g,b);
		}
	}
	@Override
	public void printExtraInfo() {
		System.out.println("Pallete: "+Arrays.toString(pallete));
		
	}

}
