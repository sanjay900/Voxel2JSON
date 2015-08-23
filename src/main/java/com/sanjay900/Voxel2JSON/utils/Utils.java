package com.sanjay900.Voxel2JSON.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.swing.JOptionPane;

import com.sanjay900.Voxel2JSON.Voxel2JSON;

public class Utils {

	public static String readString(DataInputStream ds) throws IOException{
		byte[] b = new byte[4];
		ds.read(b, 0, 4);
		return new String(b, "UTF-8");
	}
	public static int getInt(DataInputStream ds) throws IOException{
		byte[] b = new byte[4];
		ds.read(b, 0, 4);
		ByteBuffer bb2 = ByteBuffer.wrap(b);
		bb2.order(ByteOrder.LITTLE_ENDIAN);
		return bb2.getInt();
	}
	public static double[] floatc(BigDecimal[] old) {
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
	public static void infoBox(String infoMessage, String titleBar)
	{
		JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
	}
	public static void errorBox(String infoMessage, String titleBar)
	{
		JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.ERROR_MESSAGE);
	}
}
