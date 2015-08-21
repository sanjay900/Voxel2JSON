package com.sanjay900.Voxel2JSON.main;

import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sanjay900.Voxel2JSON.main.Pallete.XY;
import com.sanjay900.Voxel2JSON.main.chunks.MainChunk;
import com.sanjay900.Voxel2JSON.main.chunks.VoxelChunk;
import com.sanjay900.Voxel2JSON.main.chunks.VoxelChunk.RenderVoxel;
import com.sanjay900.Voxel2JSON.main.chunks.VoxelChunk.VoxelOrig;

public class Voxel2JSON {
	static String path;
	public static MainChunk mainChunk;
	public static Pallete p;
	static String name;
	public static int size = 16;
	final static JFileChooser fc = new JFileChooser();
	@SuppressWarnings("serial")
	static class LabelAccessory extends JPanel {
		public JCheckBox bigmodel = new JCheckBox();
		public JCheckBox uv = new JCheckBox();
		public JTextField[] thirdperson = new JTextField[9];
		public JTextField[] firstperson = new JTextField[9];
		public JTextField[] gui = new JTextField[9];
		public JTextField[] head = new JTextField[9];
		public JTextField[] ground = new JTextField[9];
		public JTextField[] fixed = new JTextField[9];
		private JTextField[] generateBoxes(JTextField[] array) {
			JLabel text = new JLabel("Position", SwingConstants.CENTER);
			this.add(text);
			for (int i =0; i<9;i++) {
				if (i > 5) {
					array[i] = new JTextField("1");
				} else {
					array[i] = new JTextField("0");
				}
				array[i].setPreferredSize(new Dimension(25, 20));
				this.add(array[i]);
				if (i==2) {
					text = new JLabel("Rotation", SwingConstants.CENTER);
					this.add(text);
				}
				if (i==5) {
					text = new JLabel("Scale", SwingConstants.CENTER);
					this.add(text);
				}
			}
			return array;
		}
		public LabelAccessory() {
			setPreferredSize(new Dimension(150, 600));
			JLabel text = new JLabel("Item Model?", SwingConstants.CENTER);
			text.setPreferredSize(new Dimension(140, 10));
			this.add(text);
			text = new JLabel("(Bigger size)", SwingConstants.CENTER);
			text.setPreferredSize(new Dimension(140, 10));
			this.add(text);
			this.add(bigmodel);
			text = new JLabel("UVTextures?", SwingConstants.CENTER);
			text.setPreferredSize(new Dimension(140, 10));
			this.add(text);
			this.add(uv);
			uv.setSelected(true);
			text = new JLabel("Third Person", SwingConstants.CENTER);
			text.setPreferredSize(new Dimension(150, 10));
			this.add(text);
			thirdperson = generateBoxes(thirdperson);
			text = new JLabel("First Person", SwingConstants.CENTER);
			text.setPreferredSize(new Dimension(150, 10));
			this.add(text);
			firstperson = generateBoxes(firstperson);
			text = new JLabel("Gui", SwingConstants.CENTER);
			text.setPreferredSize(new Dimension(150, 10));
			this.add(text);
			gui = generateBoxes(gui);
			text = new JLabel("Head", SwingConstants.CENTER);
			text.setPreferredSize(new Dimension(150, 10));
			this.add(text);
			head = generateBoxes(head);
			text = new JLabel("Ground", SwingConstants.CENTER);
			text.setPreferredSize(new Dimension(150, 10));
			this.add(text);
			ground = generateBoxes(ground);
			text = new JLabel("Item Frame", SwingConstants.CENTER);
			text.setPreferredSize(new Dimension(150, 10));
			this.add(text);
			fixed = generateBoxes(fixed);
		}
		public void populate(JSONObject obj) {
			if (obj.has("thirdperson")) {
				getFields(thirdperson,obj.getJSONObject("thirdperson"));
			}
			if (obj.has("firstperson")) {
				getFields(firstperson,obj.getJSONObject("firstperson"));
			}
			if (obj.has("firstperson")) {
				getFields(gui,obj.getJSONObject("gui"));
			}
			if (obj.has("head")) {
				getFields(head,obj.getJSONObject("head"));
			}
			if (obj.has("ground")) {
				getFields(ground,obj.getJSONObject("ground"));
			}
			if (obj.has("fixed")) {
				getFields(fixed,obj.getJSONObject("fixed"));
			}
		}
	}
	public static boolean hasChange(JTextField[] fields) {
		for (int i =0; i < fields.length; i++) {
			if (!fields[i].getText().equals(i>5?"1":"0"))
				return true;
		}
		return false;
	}
	public static void getFields(JTextField[] fields,JSONObject obj) {
		JSONArray trans= obj.getJSONArray("translation");
		for (int i =0;i<3;i++) {
			fields[i].setText(trans.getDouble(i)+"");
		}
		JSONArray rot= obj.getJSONArray("rotation");
		for (int i =0;i<3;i++) {
			fields[i+3].setText(rot.getDouble(i)+"");
		}
		JSONArray scale= obj.getJSONArray("scale");
		for (int i =0;i<3;i++) {
			fields[i+6].setText(scale.getDouble(i)+"");
		}
	}
	public static JSONObject getDisplay(JTextField[] fields) {
		JSONObject display = new JSONObject();
		JSONArray rotation = new JSONArray();
		rotation.put(Double.valueOf(fields[0].getText()));
		rotation.put(Double.valueOf(fields[1].getText()));
		rotation.put(Double.valueOf(fields[2].getText()));
		display.put("translation", rotation);
		JSONArray translation = new JSONArray();
		translation.put(Double.valueOf(fields[3].getText()));
		translation.put(Double.valueOf(fields[4].getText()));
		translation.put(Double.valueOf(fields[5].getText()));
		display.put("rotation", translation);
		JSONArray scale = new JSONArray();
		scale.put(Double.valueOf(fields[6].getText()));
		scale.put(Double.valueOf(fields[7].getText()));
		scale.put(Double.valueOf(fields[8].getText()));
		display.put("scale", scale);
		return display;
	}
	public static void main(String[] args) throws Exception {
		if (new File("Voxel2Json.settings").exists()) {
			String s = Files.lines(new File("Voxel2Json.settings").toPath()).findFirst().orElse(null);
			fc.setCurrentDirectory(new File(s));
		}
		LabelAccessory la = new LabelAccessory();
		fc.setDialogTitle("Select a JSON model to pull display from");
		fc.setFileFilter(new JSONFileFilter());
		fc.setAcceptAllFileFilterUsed(false);
		if (fc.showOpenDialog(null) != JFileChooser.CANCEL_OPTION) {
			String json = String.join(" ", Files.readAllLines(fc.getSelectedFile().toPath(), Charset.defaultCharset()));
			JSONObject obj = new JSONObject(json);
			if (obj.has("display"))
				la.populate(obj.getJSONObject("display"));
		}

		fc.setDialogTitle("Select a Magica Model to JSONify");
		fc.setFileFilter(new VoxFileFilter());
		fc.setAccessory(la);
		fc.setAcceptAllFileFilterUsed(false);
		if (fc.showOpenDialog(null) == JFileChooser.CANCEL_OPTION) return;
		boolean uv = la.uv.isSelected();
		size = la.bigmodel.isSelected()?48:16;
		JSONObject display = new JSONObject();
		if (hasChange(la.thirdperson)) {
			display.put("thirdperson", getDisplay(la.thirdperson));
		}
		if (hasChange(la.firstperson)) {
			display.put("firstperson", getDisplay(la.firstperson));
		}
		if (hasChange(la.gui)) {
			display.put("gui", getDisplay(la.gui));
		}
		if (hasChange(la.head)) {
			display.put("head", getDisplay(la.head));
		}
		if (hasChange(la.ground)) {
			display.put("ground", getDisplay(la.ground));
		}
		if (hasChange(la.fixed)) {
			display.put("fixed", getDisplay(la.fixed));
		}
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
		System.out.println("Magic:" + Utils.readString(ds)+" Version:"+Utils.getInt(ds));
		mainChunk = new MainChunk(ds);
		ds.close();
		f.close();
		JSONObject object = new JSONObject();
		JSONArray elements = new JSONArray();
		BigDecimal s = new BigDecimal(size);
		BigDecimal xdiff = s.divide(mainChunk.sizeChunk.x,3, RoundingMode.HALF_UP);
		BigDecimal ydiff = s.divide(mainChunk.sizeChunk.y,3, RoundingMode.HALF_UP);
		BigDecimal zdiff = s.divide(mainChunk.sizeChunk.z,3, RoundingMode.HALF_UP);
		for (RenderVoxel v:mainChunk.voxelChunk.voxelsr) {
			JSONObject element = new JSONObject();
			BigDecimal zero = BigDecimal.ZERO;
			element.put("from", floatc(new BigDecimal[]{
					v.xamt.compareTo(zero)!=-1?v.x:v.x.add(xdiff.multiply(v.xamt)),
					v.zamt.compareTo(zero)!=-1?v.z:v.z.add(zdiff.multiply(v.zamt)),
					v.yamt.compareTo(zero)!=-1?v.y:v.y.add(ydiff.multiply(v.yamt))}));
			element.put("to",floatc(new BigDecimal[]{
					v.xamt.compareTo(zero)==-1?v.x.add(xdiff):v.x.add(xdiff.multiply(v.xamt)),
					v.zamt.compareTo(zero)==-1?v.z.add(zdiff):v.z.add(zdiff.multiply(v.zamt)),
					v.yamt.compareTo(zero)==-1?v.y.add(ydiff):v.y.add(ydiff.multiply(v.yamt))}));
			JSONObject faces = new JSONObject();
			JSONObject face = new JSONObject();
			if (uv) {
				XY xy = p.getXY(v.colourIndex);
				face.put("uv", new double[]{xy.x+0.01,xy.y+0.01,xy.x+0.99,xy.y+0.99});
			}
			face.put("texture", "#"+name);
			v.todraw.forEach(bface -> faces.put(bface.dir, face));
			if (faces.length() != 0) {
				element.put("faces", faces);
				elements.put(element);
			}
		}
		infoBox(new String[]{"Voxel Count: " +elements.length()},"Voxel Count");
		JSONObject textures = new JSONObject();
		textures.put(name, "blocks/"+name);
		textures.put("particle", "blocks/"+name);
		object.put("textures", textures);
		if (!(display.length() == 0))
			object.put("display", display);
		object.put("elements",elements);
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(path+".json "), "utf-8"))) {
			writer.write(object.toString());
		}
	}
	private static double[] floatc(BigDecimal[] old) {
		double[] floats=new double[old.length];
		for (int i = 0;i<old.length;i++) {
			floats[i] = old[i].doubleValue();
			if (floats[i] > (Voxel2JSON.size==16?16:32)) {
				floats[i] = Voxel2JSON.size==16?16:32;
			} else if (floats[i] < (Voxel2JSON.size==16?0:-16)){
				floats[i] = Voxel2JSON.size==16?0:-16;
			}
		}
		return floats;
	}
	public static void infoBox(String[] infoMessage, String titleBar)
	{
		JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
	}
	public static String getPath() {
		return path;
	}
}