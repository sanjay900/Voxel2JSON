package com.sanjay900.Voxel2JSON.main.chunks;

import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import com.sanjay900.Voxel2JSON.main.Utils;
import com.sanjay900.Voxel2JSON.main.Voxel2JSON;
import com.sanjay900.Voxel2JSON.main.ProgressFrame.ProgressFrame;

import lombok.AllArgsConstructor;

public class VoxelChunk extends Chunk{
	public ArrayList<VoxelOrig> voxels = new ArrayList<>();
	public ArrayList<RenderVoxel> voxelsr = new ArrayList<>();
	public HashMap<Coordinate,VoxelOrig> voxelc = new HashMap<>();
	public HashMap<Coordinate,VoxelOrig> voxelc2 = new HashMap<>();
	public ProgressFrame frame;
	int numVoxels;
	MainChunk main;
	BigDecimal s = new BigDecimal(Voxel2JSON.size);

	BigDecimal xdiff;
	BigDecimal ydiff;
	BigDecimal zdiff;
	public VoxelChunk(DataInputStream ds,MainChunk main, boolean merge) throws IOException {
		super(ds);
		this.main = main;
		frame = new ProgressFrame();
		frame.setVisible(true);
		xdiff = s.divide(main.sizeChunk.x,3, RoundingMode.HALF_UP);
		ydiff = s.divide(main.sizeChunk.y,3, RoundingMode.HALF_UP);
		zdiff = s.divide(main.sizeChunk.z,3, RoundingMode.HALF_UP);
		System.out.println(xdiff);
		System.out.println(ydiff);
		System.out.println(zdiff);
		
		numVoxels = Utils.getInt(ds);
		frame.contentPane.lblConvertingPleaseWait.setText("Converting "+Voxel2JSON.name+".vox to PNG/JSON, please wait");
		frame.setTitle("Converting "+Voxel2JSON.name+".vox to PNG/JSON, please wait");
		frame.contentPane.lblVoxels.setText("Voxels: "+numVoxels);
		frame.contentPane.progressBar.setMaximum(numVoxels);
		frame.contentPane.overallProgress.setMaximum(6);
		frame.contentPane.overallInfo.setText("Creating objects");
		for (int i = 0; i < numVoxels; i++) {
			VoxelOrig v = new VoxelOrig(ds); 
			voxels.add(v);
			voxelc.put(new Coordinate(v), v);
			frame.contentPane.progressBar.setValue(i);
			frame.contentPane.subStatus.setText("Converting Voxel "+i+" out of "+numVoxels);
		}
		frame.contentPane.progressBar.setValue(0);
		frame.contentPane.overallInfo.setText("Calculating Merged faces");
		frame.contentPane.overallProgress.setValue(2);
		frame.contentPane.progressBar.setMaximum(numVoxels);
		voxelc2.putAll(voxelc);
		s = new BigDecimal(Voxel2JSON.size);
		int i =0;
		for (VoxelOrig v : voxels) {
			for (BlockFace b: BlockFace.values()) {
				if (voxelc.containsKey(v.getRelative(b))) {
					v.near.add(b);
				}
			}
			frame.contentPane.progressBar.setValue(++i);
			frame.contentPane.subStatus.setText("Calculating merged face on Voxel "+i+" out of "+numVoxels);
		}
		boolean done = false;
		int size = voxels.size();
		ArrayList<Voxel> voxels2 = new ArrayList<>();

		frame.contentPane.overallInfo.setText("Merging faces");
		frame.contentPane.overallProgress.setValue(3);
		frame.contentPane.progressBar.setValue(0);
		while (!done) {
			VoxelOrig v = voxels.get(0);
			voxels.remove(v);
			removeVoxelC(v);
			Voxel voxel = new Voxel(v);
			if (merge) {
				boolean done2 = false;
				if (v.near.isEmpty()) {
					done2 = true;
				} else {
					BlockFace b = v.near.get(0);
					VoxelOrig vo2 = voxelc.get(v.getRelative(b));
					while (!done2) {
						if (vo2 == null) {
							done2 = true;
						} else {
							if (vo2.colourIndex == v.colourIndex) {
								voxel.expand(b);
								voxels.remove(vo2);
								removeVoxelC(vo2);
								vo2 = voxelc.get(vo2.getRelative(b));
							} else {
								done2 = true;
							}
						}
					}
					for (BlockFace bl : voxel.near) {
						done2 = false;
						while (!done2) {
							for (Coordinate c: voxel.getRelatives(bl)) {
								if (voxelc.get(c) == null || voxelc.get(c).colourIndex != voxel.colourIndex) done2=true;
							}
							if (!done2) {
								voxel.expand(bl);
								for (Coordinate c: voxel.getRelatives(bl)) {
									voxels.remove(voxelc.get(c));
									removeVoxelC(voxelc.get(c));
								}
							}
						}
					}
				}
			}
			voxels2.add(voxel);
			done = voxels.isEmpty();

			frame.contentPane.progressBar.setValue(numVoxels-voxels.size());
			frame.contentPane.subStatus.setText("Merging face "+(numVoxels-voxels.size())+ " out of "+numVoxels);

		}
		if (merge) {

			frame.contentPane.progressBar.setMaximum(voxels2.size());
			frame.contentPane.overallInfo.setText("Removing Unseen faces");
			frame.contentPane.overallProgress.setValue(4);
			int i2 =0;
			for (Voxel voxel2: voxels2) {
				for (BlockFace bl : BlockFace.values()) {
					boolean done2 = false;
					for (Coordinate c: voxel2.getRelatives(bl)) {
						if (voxelc2.get(c) == null) done2=true;
					}
					if (!done2) {
						voxel2.todraw.remove(bl);
					}
				}

				frame.contentPane.progressBar.setValue(++i2);
				frame.contentPane.subStatus.setText("Check if face "+i2+" is covered");
			}
		}
		
		
		voxels2.forEach(v -> voxelsr.add(new RenderVoxel(v)));

		frame.contentPane.overallInfo.setText("Tidying up");
		frame.contentPane.overallProgress.setValue(5);
		frame.contentPane.lblMergedVoxels.setText("Merged Voxels: "+(size-voxels2.size())+" / "+size);
	}
	private void removeVoxelC(VoxelOrig v) {
		Iterator<Entry<Coordinate, VoxelOrig>> it = voxelc.entrySet().iterator();
		while (it.hasNext()) {
			if (it.next().getValue() == v) {
				it.remove();
				return;
			}
		}
	}
	public class Coordinate {
		int x;
		int y;
		int z;
		public Coordinate(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		public Coordinate(VoxelOrig voxel) {
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
		public RenderVoxel(Voxel v) {
			x = new BigDecimal(v.x);
			y = new BigDecimal(v.y);
			z = new BigDecimal(v.z);
			colourIndex = v.colourIndex;
			s = new BigDecimal(Voxel2JSON.size);
			s = s.divide(main.sizeChunk.biggest, 5, RoundingMode.HALF_UP).multiply(main.sizeChunk.x);
			x = x.divide(main.sizeChunk.x,5, RoundingMode.HALF_UP).multiply(s);

			s = new BigDecimal(Voxel2JSON.size);
			s = s.divide(main.sizeChunk.biggest, 5, RoundingMode.HALF_UP).multiply(main.sizeChunk.y);
			y = y.divide(main.sizeChunk.y,5, RoundingMode.HALF_UP).multiply(s);

			s = new BigDecimal(Voxel2JSON.size);
			s = s.divide(main.sizeChunk.biggest, 5, RoundingMode.HALF_UP).multiply(main.sizeChunk.z);
			z = z.divide(main.sizeChunk.z,5, RoundingMode.HALF_UP).multiply(s);
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
	public class Voxel {
		public Voxel(VoxelOrig v) {
			this.x = v.x;
			this.y = v.y;
			this.z = v.z;
			this.colourIndex = v.colourIndex;
			this.near = v.near;
		}


		public ArrayList<Coordinate> getRelatives(BlockFace b) {
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
	public class VoxelOrig {
		public VoxelOrig(DataInputStream ds) throws IOException{
			x = ds.read();
			y = ds.read();
			z = ds.read();
			colourIndex = ds.read();
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

		}
		public int x;
		public int y;
		public int z;
		public int colourIndex;
		public ArrayList<BlockFace> near = new ArrayList<>();
	}
	@Override
	public void printExtraInfo() {

	}
	@AllArgsConstructor
	public enum BlockFace {
		XADD("west"),XSUB("east"),YADD("south"),YSUB("north"),ZADD("up"),ZSUB("down");
		public String dir;
	}
	public static void infoBox(String[] infoMessage, String titleBar)
	{
		JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
	}
}
