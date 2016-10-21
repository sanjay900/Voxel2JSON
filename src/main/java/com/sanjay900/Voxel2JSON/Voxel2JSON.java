package com.sanjay900.Voxel2JSON;

import java.awt.Dimension;
import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.sanjay900.Voxel2JSON.chunks.voxeldata.BlockFace;
import com.sanjay900.Voxel2JSON.chunks.voxeldata.Voxel;
import net.tangentmc.processing.ProcessingRunner;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sanjay900.Voxel2JSON.chunks.MainChunk;
import com.sanjay900.Voxel2JSON.display.MainFrame;
import com.sanjay900.Voxel2JSON.json.Pallete;
import com.sanjay900.Voxel2JSON.json.Pallete.XY;
import com.sanjay900.Voxel2JSON.json.VoxFileFilter;
import com.sanjay900.Voxel2JSON.utils.Utils;

import lombok.Getter;
import processing.core.PApplet;
import processing.core.PImage;
import processing.opengl.PGraphics3D;

@Getter
public class Voxel2JSON extends PApplet{
	@Getter
	static String path;
	public static MainChunk mainChunk;
	public static Pallete p;
	public static String name;
	public static double MC_SIZE = 16;
	public static MainFrame frame;
	@Getter
	static int version;
	final static JFileChooser fc = new JFileChooser();
	@SuppressWarnings("serial")
	static class LabelAccessory extends JPanel {
		public JCheckBox bigmodel = new JCheckBox();
		public JCheckBox uv = new JCheckBox();
		public JCheckBox merge = new JCheckBox();
		public LabelAccessory() {
			setPreferredSize(new Dimension(150, 200));
			JLabel text = new JLabel("Item Model?", SwingConstants.CENTER);
			text.setPreferredSize(new Dimension(140, 10));
			this.add(text);
			text = new JLabel("(Bigger MC_SIZE)", SwingConstants.CENTER);
			text.setPreferredSize(new Dimension(140, 10));
			this.add(text);
			this.add(bigmodel);
			text = new JLabel("UVTextures?", SwingConstants.CENTER);
			text.setPreferredSize(new Dimension(140, 10));
			this.add(text);
			this.add(uv);
			uv.setSelected(true);
			text = new JLabel("Merge Voxels?", SwingConstants.CENTER);
			text.setPreferredSize(new Dimension(140, 10));
			this.add(text);
			this.add(merge);
			merge.setSelected(true);
		}

	}

	public void settings() {
		size(500,500,P3D);
		noLoop();
	}
	boolean shouldRender = false;
	public void draw() {
		background(100);
		noStroke();
		((PGraphics3D) g).textureSampling(3);
		if (!shouldRender) return;
		if (image == null)
			image = loadImage(Voxel2JSON.getPath()+".png");
		pushMatrix();
		translate(width/2,height/2);
		translate(-mainChunk.sizeChunk.x/2,-mainChunk.sizeChunk.y/2,-mainChunk.sizeChunk.z/2);
		rotateX(radians(frame.getRx().getValue()/100f));
		rotateY(radians(frame.getRy().getValue()/100f));
		rotateZ(radians(frame.getRz().getValue()/100f));
		translate(mainChunk.sizeChunk.x/2,mainChunk.sizeChunk.y/2,mainChunk.sizeChunk.z/2);
		scale(2);
		if (frame.getSx().getValue() != 0 && frame.getSx().getValue() != 0 && frame.getSx().getValue() != 0) {
			scale(frame.getSx().getValue() / 100f, frame.getSy().getValue() / 100f, frame.getSz().getValue() / 100f);
			mainChunk.voxelChunk.voxels.forEach(this::createBox);
			scale(100 / frame.getSx().getValue(), 100 / frame.getSy().getValue(), 100 / frame.getSz().getValue());
		}
		translate(-10,-10,-10);
		stroke(255,0,0);
		line(0,0,0,10,0,0);
		stroke(0,255,0);
		line(0,0,0,0,10,0);
		stroke(0,0,255);
		line(0,0,0,0,0,10);

		popMatrix();
	}
	public static void main(String[] args) {
		try {
			new Voxel2JSON();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	PImage image;
	public void createBox(Voxel voxel) {
		pushMatrix();
		translate(voxel.x, voxel.y, voxel.z);
		scale(voxel.xamt,voxel.yamt,voxel.zamt);
		XY xy = p.getXY(voxel.colourIndex);
		float u=xy.x+0.01f, v=xy.y+0.99f, u2=xy.x+0.01f, v2=xy.y+0.99f;
		textureMode(IMAGE);
		beginShape(QUADS);
		texture(image);

		// +Z "front" face
		vertex(0, 0,  1, u, v);
		vertex( 1, 0,  1, u2, v);
		vertex( 1,  1,  1, u2, v2);
		vertex(0,  1,  1, u, v2);

		// -Z "back" face
		vertex( 1, 0, 0, u, v);
		vertex(0, 0, 0, u2, v);
		vertex(0,  1, 0, u2, v2);
		vertex( 1,  1, 0, u, v2);

		// +Y "bottom" face
		vertex(0,  1,  1, u, v);
		vertex( 1,  1,  1, u2, v);
		vertex( 1,  1, 0, u2, v2);
		vertex(0,  1, 0, u, v2);

		// -Y "top" face
		vertex(0, 0, 0, u, v);
		vertex( 1, 0, 0, u2, v);
		vertex( 1, 0,  1, u2, v2);
		vertex(0, 0,  1, u, v2);

		// +X "right" face
		vertex( 1, 0,  1, u, v);
		vertex( 1, 0, 0, u2, v);
		vertex( 1,  1, 0, u2, v2);
		vertex( 1,  1,  1, u, v2);

		// -X "left" face
		vertex(0, 0, 0, u, v);
		vertex(0, 0,  1, u2, v);
		vertex(0,  1,  1, u2, v2);
		vertex(0,  1, 0, u, v2);

		endShape();
		popMatrix();

	}
	public Voxel2JSON() throws IOException {
		ProcessingRunner.run(this);
		if (new File("Voxel2Json.settings").exists()) {
			String s = Files.lines(new File("Voxel2Json.settings").toPath()).findFirst().orElse(null);
			fc.setCurrentDirectory(new File(s));
		}
		LabelAccessory la = new LabelAccessory();
		fc.setAcceptAllFileFilterUsed(false);
		fc.setDialogTitle("Select a Magica Model to JSONify");
		fc.setFileFilter(new VoxFileFilter());
		fc.setAccessory(la);
		fc.setAcceptAllFileFilterUsed(false);
		if (fc.showOpenDialog(null) == JFileChooser.CANCEL_OPTION) return;
		boolean uv = la.uv.isSelected();
		MC_SIZE = la.bigmodel.isSelected()?48:16;
		path = fc.getSelectedFile().getPath();
		path = FilenameUtils.removeExtension(path);
		name = FilenameUtils.getBaseName(fc.getSelectedFile().getPath());
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("Voxel2JSON.settings"), "utf-8"))) {
			writer.write(fc.getSelectedFile().getParent());
		}
		File file = new File(path+".vox");
		FileInputStream f = new FileInputStream(file);
		DataInputStream ds = new DataInputStream(f);
		String magic = Utils.readString(ds);
		version = Utils.getInt(ds);
		if ( !magic.equals("VOX ")) {
			Utils.errorBox("The file you attempted to open was not recognised.", "Error!");
			main(args);
			return;
		}
		mainChunk = new MainChunk(ds,la.merge.isSelected());
		ds.close();
		f.close();
		JSONObject object = new JSONObject();
		JSONArray elements = new JSONArray();
		double xdiff = MC_SIZE/mainChunk.sizeChunk.x;
		double ydiff = MC_SIZE/mainChunk.sizeChunk.y;
		double zdiff = MC_SIZE/mainChunk.sizeChunk.z;
		double xmult = mainChunk.sizeChunk.x;
		double ymult = mainChunk.sizeChunk.y;
		double zmult = mainChunk.sizeChunk.z;
		int i = 0;
		mainChunk.voxelChunk.frame.contentPane.getActionProgress().setMaximum(mainChunk.voxelChunk.voxels.size());
		for (Voxel v:mainChunk.voxelChunk.voxels) {
			JSONObject element = new JSONObject();
			double ox = v.x/xmult*MC_SIZE, oy = v.y/ymult*MC_SIZE, oz = v.z/zmult*MC_SIZE,
					dx = ox+(xdiff*v.xamt), dy = oy + (ydiff*v.yamt), dz = oz+ +(zdiff*v.zamt);
			element.put("to", new double[]{MC_SIZE-ox,oz,oy});
			element.put("from",new double[]{MC_SIZE-dx,dz,dy});
			JSONObject faces = new JSONObject();
			JSONObject face = new JSONObject();
			if (uv) {
				XY xy = p.getXY(v.colourIndex);
				face.put("uv", new double[]{xy.x+0.01,xy.y+0.01,xy.x+0.99,xy.y+0.99});
			}
			face.put("texture", "#"+name);
			//"north","south","east","west","up","down"
			Arrays.asList("north","south","east","west","up","down").forEach(bface -> faces.put(bface, face));

			if (faces.length() != 0) {
				element.put("faces", faces);
				elements.put(element);
			}
			mainChunk.voxelChunk.frame.contentPane.getActionSubtitle().setText("Saving face "+ (++i)+" to JSON model");

			mainChunk.voxelChunk.frame.contentPane.getActionProgress().setValue(i);
		}
		frame = new MainFrame();
		frame.setVisible(true);
		if (new File(path+".json").exists()) {
			try {
				JSONObject orig = new JSONObject(StringUtils.join(Files.readAllLines(new File(path + ".json").toPath()), " "));
				if (orig.has("display")) {
					frame.fromDisplay(orig.getJSONObject("display"));
				}
			} catch (Exception ex) {
				Utils.errorBox("There was an error reading the previous model.\n"+ex.getLocalizedMessage(),"Error");
			}
		}
		mainChunk.voxelChunk.frame.contentPane.getVoxelCount().setText("Total: "+elements.length());
		JSONObject textures = new JSONObject();
		textures.put(name, "blocks/"+name);
		textures.put("particle", "blocks/"+name);
		object.put("textures", textures);
		JSONObject display = frame.getDisplay();
		if (!(display.length() == 0))
			object.put("display", display);
		object.put("elements",elements);
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(path+".json"), "utf-8"))) {
			writer.write(object.toString());
		}
		mainChunk.voxelChunk.frame.contentPane.getActionSubtitle().setText("Saved Model!");
		mainChunk.voxelChunk.frame.contentPane.getOverallProgress().setValue(6);
		loop();
		shouldRender = true;
	}

}