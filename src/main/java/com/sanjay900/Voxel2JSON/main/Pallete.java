package com.sanjay900.Voxel2JSON.main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sanjay900.Voxel2JSON.main.chunks.PalleteChunk;

public class Pallete {
	BufferedImage bi;
	public Pallete(DataInputStream ds) throws IOException{
		if (ds != null) {
		bi = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		PalleteChunk pc = new PalleteChunk(ds);
		for (int i = 1; i < 256; i++) {
			drawPixel(pc.pallete[i],i);
		}
		savePng();
		}
	}
	public XY getXY(int index) {
		int y = index/16;
		int x = index %16;
		return new XY(x,y);
	}
	public void drawPixel(Color c, int index) {
		int y = index/16;
		int x = index %16;
		bi.setRGB(x, y, c.getRGB());
	}
	public void savePng() throws IOException {
		ImageIO.write(bi, "PNG", new File(Voxel2JSON.getPath()+".png"));
	}
	public class XY {
		public XY(int x2, int y2) {
			x = x2;
			y=y2;
		}
		public int x;
		public int y;
		public String toString() {
			return "["+x+","+y+"]";
		}
	}
}
