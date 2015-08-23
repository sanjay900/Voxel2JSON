package com.sanjay900.Voxel2JSON.chunks;

import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.sanjay900.Voxel2JSON.Voxel2JSON;
import com.sanjay900.Voxel2JSON.chunks.voxeldata.BlockFace;
import com.sanjay900.Voxel2JSON.chunks.voxeldata.Coordinate;
import com.sanjay900.Voxel2JSON.chunks.voxeldata.RenderVoxel;
import com.sanjay900.Voxel2JSON.chunks.voxeldata.Voxel;
import com.sanjay900.Voxel2JSON.progress.ProgressFrame;
import com.sanjay900.Voxel2JSON.utils.Utils;

public class VoxelChunk extends Chunk{
	public ArrayList<Voxel> voxels = new ArrayList<>();
	public ArrayList<RenderVoxel> voxelsr = new ArrayList<>();
	public HashMap<Coordinate,Voxel> voxelc = new HashMap<>();
	public HashMap<Coordinate,Voxel> voxelc2 = new HashMap<>();
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
		numVoxels = Utils.getInt(ds);
		frame.contentPane.lblConvertingPleaseWait.setText("Converting "+Voxel2JSON.name+".vox to PNG/JSON, please wait");
		frame.setTitle("Converting "+Voxel2JSON.name+".vox to PNG/JSON, please wait");
		frame.contentPane.lblVoxels.setText("Voxels: "+numVoxels);
		frame.contentPane.progressBar.setMaximum(numVoxels);
		frame.contentPane.overallProgress.setMaximum(6);
		frame.contentPane.overallInfo.setText("Creating objects");
		for (int i = 0; i < numVoxels; i++) {
			Voxel v = new Voxel(ds); 
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
		for (Voxel v : voxels) {
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
			Voxel v = voxels.get(0);
			voxels.remove(v);
			removeVoxelC(v);
			Voxel voxel = v.clone();
			if (merge) {
				boolean done2 = false;
				if (v.near.isEmpty()) {
					done2 = true;
				} else {
					BlockFace b = v.near.get(0);
					Voxel vo2 = voxelc.get(v.getRelative(b));
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
		
		
		voxels2.forEach(v -> voxelsr.add(new RenderVoxel(v,main.sizeChunk)));

		frame.contentPane.overallInfo.setText("Tidying up");
		frame.contentPane.overallProgress.setValue(5);
		frame.contentPane.lblMergedVoxels.setText("Merged Voxels: "+(size-voxels2.size())+" / "+size);
	}
	private void removeVoxelC(Voxel v) {
		Iterator<Entry<Coordinate, Voxel>> it = voxelc.entrySet().iterator();
		while (it.hasNext()) {
			if (it.next().getValue() == v) {
				it.remove();
				return;
			}
		}
	}
	
}
