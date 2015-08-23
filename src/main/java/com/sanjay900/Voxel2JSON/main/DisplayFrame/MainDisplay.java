package com.sanjay900.Voxel2JSON.main.DisplayFrame;
/*
 *      This Code Was Created By Jeff Molofee 2000
 *      A HUGE Thanks To Fredric Echols For Cleaning Up
 *      And Optimizing The Base Code, Making It More Flexible!
 *      If You've Found This Code Useful, Please Let Me Know.
 *      Visit My Site At nehe.gamedev.net
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.glu.GLU;

import com.sanjay900.Voxel2JSON.main.Pallete.XY;
import com.sanjay900.Voxel2JSON.main.Voxel2JSON;
import com.sanjay900.Voxel2JSON.main.chunks.VoxelChunk.RenderVoxel;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class MainDisplay {
	private boolean done = false;
	private boolean fullscreen = false;
	private final String windowTitle = "Display Model Render";
	private DisplayMode displayMode;
	private int texture;           // Storage For One Texture
	private int guitexture;

	public MainDisplay() {
		try {
			init();
			while (!done) {
				mainloop();
				render();
				Display.update();
			}
			cleanup();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		System.exit(0);
	}
	private void mainloop() {
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {       // Exit if Escape is pressed
			done = true;
		}
		if (Mouse.isInsideWindow() && (Mouse.isButtonDown(0) || Mouse.isButtonDown(1) )&& !Mouse.isGrabbed()) {
			Mouse.setGrabbed(true);
		} 
		if (!Mouse.isButtonDown(0) && !Mouse.isButtonDown(1)) {
			Mouse.setGrabbed(false);
		}
		if(Display.isCloseRequested()) {                     // Exit if window is closed
			done = true;
		}
	}

	private boolean render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer

		GL11.glLoadIdentity(); // Reset The Current Modelview Matrix

		GL11.glMatrixMode(GL11.GL_MODELVIEW_MATRIX);
		GL11.glTranslatef(-9f, -9f, -50f);
		drawGuiBorder();
		GL11.glTranslatef(8f,8f,-8f);
		GL11.glRotatef(30, 1.0f, 0.0f, 0.0f); // Rotate On The X Axis
		GL11.glRotatef(-135, 0.0f, 1.0f, 0.0f); // Rotate On The Y Axis
		GL11.glRotatef((Voxel2JSON.frame.rx.getValue()/100f), 1.0f, 0.0f, 0.0f); // Rotate On The X Axis
		GL11.glRotatef(Voxel2JSON.frame.ry.getValue()/100f, 0.0f, 1.0f, 0.0f); // Rotate On The Y Axis
		GL11.glRotatef((Voxel2JSON.frame.rz.getValue()/100f), 0.0f, 0.0f, 1.0f); // Rotate On The Z Axis
		GL11.glScalef((Voxel2JSON.frame.sx.getValue()/100f)-1.6f, (Voxel2JSON.frame.sy.getValue()/100f)-1.6f, (Voxel2JSON.frame.sz.getValue()/100f)-1.6f);
		GL11.glTranslatef(Voxel2JSON.frame.tx.getValue()/100f, Voxel2JSON.frame.ty.getValue()/100f, (Voxel2JSON.frame.tz.getValue()/100f)-16f);
		GL11.glTranslatef(-8f, -8f, 8f);

		BigDecimal s = new BigDecimal(Voxel2JSON.size);
		BigDecimal xdiff = s.divide(Voxel2JSON.mainChunk.sizeChunk.x,3, RoundingMode.HALF_UP);
		BigDecimal ydiff = s.divide(Voxel2JSON.mainChunk.sizeChunk.y,3, RoundingMode.HALF_UP);
		BigDecimal zdiff = s.divide(Voxel2JSON.mainChunk.sizeChunk.z,3, RoundingMode.HALF_UP);
		for (RenderVoxel v : Voxel2JSON.mainChunk.voxelChunk.voxelsr) {
			XY xy = Voxel2JSON.p.getXY(v.colourIndex);
			float x = (v.xamt.compareTo(BigDecimal.ZERO)!=-1?v.x:v.x.add(xdiff.multiply(v.xamt))).floatValue();
			float z = (v.yamt.compareTo(BigDecimal.ZERO)!=-1?v.y:v.y.add(ydiff.multiply(v.yamt))).floatValue();
			float y = (v.zamt.compareTo(BigDecimal.ZERO)!=-1?v.z:v.z.add(zdiff.multiply(v.zamt))).floatValue();
			float xd = (v.xamt.compareTo(BigDecimal.ZERO)==-1?v.x.add(xdiff):v.x.add(xdiff.multiply(v.xamt))).floatValue();
			float zd = (v.yamt.compareTo(BigDecimal.ZERO)==-1?v.y.add(ydiff):v.y.add(ydiff.multiply(v.yamt))).floatValue();
			float yd = (v.zamt.compareTo(BigDecimal.ZERO)==-1?v.z.add(zdiff):v.z.add(zdiff.multiply(v.zamt))).floatValue();
			drawCube(x,y,z,xd,yd,zd,xy.x+0.01f,xy.y+0.01f,xy.x+0.99f,xy.y+0.99f);
		}
		return true;
	}
	/**
	 * 
	 * @param x loc x
	 * @param y loc y
	 * @param z loc z
	 * @param dx width
	 * @param dy height
	 * @param dz z size
	 * @param uvlx lower x uv
	 * @param uvly lower y uv
	 * @param uvx upper x uv
	 * @param uvy upper y uv
	 */
	private void drawCube(float x, float y, float z, float xd, float yd, float zd, float uvlx, float uvly, float uvx, float uvy) {
		uvx = uvx/16;
		uvy = uvy/16;
		uvlx = uvlx/16;
		uvly = uvly/16;


		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture); // Select Our Texture
		GL11.glBegin(GL11.GL_QUADS);
		// Front Face
		GL11.glTexCoord2f(uvlx, uvly);
		GL11.glVertex3f(x, y, zd); // Bottom Left Quad
		GL11.glTexCoord2f(uvx, uvly);
		GL11.glVertex3f(xd, y, zd); // Bottom Right Quad
		GL11.glTexCoord2f(uvx, uvy);
		GL11.glVertex3f(xd, yd, zd); // Top Right Quad
		GL11.glTexCoord2f(uvlx, uvy);
		GL11.glVertex3f(x, yd, zd); // Top Left Quad
		// Back Face
		GL11.glTexCoord2f(uvx, uvly);
		GL11.glVertex3f(x, y, z); // Bottom Right Quad
		GL11.glTexCoord2f(uvx, uvy);
		GL11.glVertex3f(x,yd,z); // Top Right Quad
		GL11.glTexCoord2f(uvlx, uvy);
		GL11.glVertex3f(xd, yd, z); // Top Left Quad
		GL11.glTexCoord2f(uvlx, uvly);
		GL11.glVertex3f(xd, y, z); // Bottom Left Quad
		// Top Face
		GL11.glTexCoord2f(uvlx, uvy);
		GL11.glVertex3f(x, yd, z); // Top Left Quad
		GL11.glTexCoord2f(uvlx, uvly);
		GL11.glVertex3f(x, yd, zd); // Bottom Left Quad
		GL11.glTexCoord2f(uvx, uvly);
		GL11.glVertex3f(xd, yd, zd); // Bottom Right Quad
		GL11.glTexCoord2f(uvx, uvy);
		GL11.glVertex3f(xd, yd, z); // Top Right Quad
		// Bottom Face
		GL11.glTexCoord2f(uvx, uvy);
		GL11.glVertex3f(x,y,z); // Top Right Quad
		GL11.glTexCoord2f(uvlx, uvly);
		GL11.glVertex3f(xd,y,z); // Top Left Quad
		GL11.glTexCoord2f(uvlx, uvly);
		GL11.glVertex3f(xd,y,zd); // Bottom Left Quad
		GL11.glTexCoord2f(uvx, uvly);
		GL11.glVertex3f(x,y,zd); // Bottom Right Quad
		// Right face
		GL11.glTexCoord2f(uvx, uvly);
		GL11.glVertex3f(xd,y,z); // Bottom Right Quad
		GL11.glTexCoord2f(uvx, uvy);
		GL11.glVertex3f(xd,yd,z); // Top Right Quad
		GL11.glTexCoord2f(uvlx, uvy);
		GL11.glVertex3f(xd,yd,zd); // Top Left Quad
		GL11.glTexCoord2f(uvlx, uvly);
		GL11.glVertex3f(xd,y,zd); // Bottom Left Quad
		// Left Face
		GL11.glTexCoord2f(uvlx, uvly);
		GL11.glVertex3f(x,y,z); // Bottom Left Quad
		GL11.glTexCoord2f(uvx, uvly);
		GL11.glVertex3f(x,y,zd); // Bottom Right Quad
		GL11.glTexCoord2f(uvx, uvy);
		GL11.glVertex3f(x,yd,zd); // Top Right Quad
		GL11.glTexCoord2f(uvlx, uvy);
		GL11.glVertex3f(x,yd,z); // Top Left Quad
		GL11.glEnd();
	}
	public void drawGuiBorder() {


		GL11.glBindTexture(GL11.GL_TEXTURE_2D, guitexture); // Select Our Texture

		GL11.glBegin(GL11.GL_QUADS);

		// -1,-1,-1 to 16,0,0
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(-1, -1, 0); 
		GL11.glTexCoord2f(0.0625f, 0);
		GL11.glVertex3f(17, -1, 0); 
		GL11.glTexCoord2f(0.0625f, 0.0625f);
		GL11.glVertex3f(17, 0, 0); 
		GL11.glTexCoord2f(0, 0.0625f);
		GL11.glVertex3f(-1, 0, 0); 
		// -1,16,-1 to 16,17,0
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(-1, 16, 0); 
		GL11.glTexCoord2f(0.0625f, 0);
		GL11.glVertex3f(17, 16, 0); 
		GL11.glTexCoord2f(0.0625f, 0.0625f);
		GL11.glVertex3f(17, 17, 0); 
		GL11.glTexCoord2f(0, 0.0625f);
		GL11.glVertex3f(-1, 17, 0); 
		// 0,-1,-1 to -1,17,0
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(-1, -1, 0); // Bottom Left Quad
		GL11.glTexCoord2f(0.0625f, 0);
		GL11.glVertex3f(0, -1, 0); // Bottom Right Quad
		GL11.glTexCoord2f(0.0625f, 0.0625f);
		GL11.glVertex3f(0, 17, 0); // Top Right Quad
		GL11.glTexCoord2f(0, 0.0625f);
		GL11.glVertex3f(-1, 17, 0); // Top Left Quad
		// 16,-1,-1 to 17,17,0
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(16, -1, 0); // Bottom Left Quad
		GL11.glTexCoord2f(0.0625f, 0);
		GL11.glVertex3f(17, -1, 0); // Bottom Right Quad
		GL11.glTexCoord2f(0.0625f, 0.0625f);
		GL11.glVertex3f(17, 17, 0); // Top Right Quad
		GL11.glTexCoord2f(0, 0.0625f);
		GL11.glVertex3f(16, 17, 0); // Top Left Quad

		GL11.glEnd();
	}
	private void createWindow() throws Exception {
		Display.setFullscreen(fullscreen);
		DisplayMode d[] = Display.getAvailableDisplayModes();
		for (int i = 0; i < d.length; i++) {
			if (d[i].getWidth() == 640
					&& d[i].getHeight() == 480
					&& d[i].getBitsPerPixel() == 32) {
				displayMode = d[i];
				break;
			}
		}
		Display.setDisplayMode(displayMode);
		Display.setTitle(windowTitle);
		Display.create();
	}

	private void init() throws Exception {
		createWindow();

		loadTextures();
		initGL();
	}

	private void loadTextures() {
		texture = loadTexture(Voxel2JSON.getPath()+".png",GL13.GL_TEXTURE0);
		guitexture = loadTexture("textures/gui.png",GL13.GL_TEXTURE0);
	}

	private void initGL() {
		GL11.glEnable(GL11.GL_TEXTURE_2D); // Enable Texture Mapping
		GL11.glShadeModel(GL11.GL_SMOOTH); // Enable Smooth Shading
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
		GL11.glClearDepth(1.0f); // Depth Buffer Setup
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
		GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Testing To Do

		GL11.glMatrixMode(GL11.GL_PROJECTION); // Select The Projection Matrix
		GL11.glLoadIdentity(); // Reset The Projection Matrix

		// Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(45.0f, (float) displayMode.getWidth() / (float) displayMode.getHeight(), 0.1f, 100.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW); // Select The Modelview Matrix

		// Really Nice Perspective Calculations
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
	}
	private void cleanup() {
		Display.destroy();
	}
	/**
	 * Texture loading using DevIL
	 * Example created by Mark Bernard
	 */
	private int loadTexture(String path, int textureUnit) {
		ByteBuffer buf = null;
		int tWidth = 0;
		int tHeight = 0;
		try {
			// Open the PNG file as an InputStream
			InputStream in = new FileInputStream(path);
			// Link the PNG decoder to this stream
			PNGDecoder decoder = new PNGDecoder(in);

			// Get the width and height of the texture
			tWidth = decoder.getWidth();
			tHeight = decoder.getHeight();


			// Decode the PNG file in a ByteBuffer
			buf = ByteBuffer.allocateDirect(
					4 * decoder.getWidth() * decoder.getHeight());
			decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
			buf.flip();

			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		int texId = GL11.glGenTextures();
		GL13.glActiveTexture(textureUnit);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);

		// All RGB bytes are aligned to each other and each component is 1 byte
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

		// Upload the texture data and generate mip maps (for scaling)
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, tWidth, tHeight, 0, 
				GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

		// Setup the ST coordinate system
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

		// Setup what to do when the texture has to be scaled
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, 
				GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, 
				GL11.GL_LINEAR_MIPMAP_LINEAR);
		return texId;
	}
}
