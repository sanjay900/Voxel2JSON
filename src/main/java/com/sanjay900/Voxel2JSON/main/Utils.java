package com.sanjay900.Voxel2JSON.main;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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
}
