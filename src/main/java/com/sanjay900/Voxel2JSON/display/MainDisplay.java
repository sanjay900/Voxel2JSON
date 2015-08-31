package com.sanjay900.Voxel2JSON.display;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW_MATRIX;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.util.glu.GLU.gluPerspective;

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

import com.sanjay900.Voxel2JSON.Voxel2JSON;
import com.sanjay900.Voxel2JSON.chunks.voxeldata.RenderVoxel;
import com.sanjay900.Voxel2JSON.json.Pallete.XY;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class MainDisplay {
	private boolean done = false;
	private final String windowTitle = "Display Model Render";
	private DisplayMode displayMode;
	private int texture;           // Storage For One Texture
	private int guitexture;
	private float zoom = 1;

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
		if (Mouse.isGrabbed() && Mouse.isInsideWindow()) {
			zoom -= Mouse.getDWheel()/200f;
			if (zoom < 0.1) zoom = 0.1f;
		}
		if(Display.isCloseRequested()) {                     // Exit if window is closed
			done = true;
		}
	}
	private void initGUI() {
		glTranslatef(-9f, -9f, 0f);
		drawGuiBorder();

		glTranslatef(8f, 8f, -8f);
		glRotatef(30, 1.0f, 0.0f, 0.0f); // Rotate On The X Axis
		glRotatef(-135, 0.0f, 1.0f, 0.0f); // Rotate On The Y Axis
	}
	private boolean render() {
		initView();
		if (Voxel2JSON.frame.type == ViewType.GUI) {
			initGUI();
		}
		rotateView();
		glTranslatef(-8f, -8f, 8f);
		drawCubes();
		return true;
	}
	public void initView() {
		glClearColor(0.53f,0.53f,0.53f,0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
		glMatrixMode(GL_PROJECTION); 
		glLoadIdentity(); // Reset The Current Modelview Matrix
		glOrtho(((640f/480f)*16f)*zoom, -((640f/480f)*16f)*zoom,-16.0*zoom, 16.0*zoom, -50.0, 50.0);
		glMatrixMode(GL_MODELVIEW_MATRIX);
	}
	public void rotateView() {
		glRotatef((Voxel2JSON.frame.rx.getValue()/100f), 0.0f, 0.0f, 1.0f); // Rotate On The Z Axis
		glRotatef((Voxel2JSON.frame.ry.getValue()/100f), 0.0f, 1.0f, 0.0f); // Rotate On The Y Axis
		glRotatef((Voxel2JSON.frame.rz.getValue()/100f), 1.0f, 0.0f, 0.0f); // Rotate On The X Axis
		
		glScalef((Voxel2JSON.frame.sz.getValue()/100f)*0.625f, (Voxel2JSON.frame.sy.getValue()/100f)*0.625f, (Voxel2JSON.frame.sx.getValue()/100f)*0.625f);
		//glTranslatef(8, 5, -1f);
		glTranslatef(Voxel2JSON.frame.tx.getValue()/50f, Voxel2JSON.frame.ty.getValue()/50f, (Voxel2JSON.frame.tz.getValue()/50f)-16f);
	}
	public void drawCubes() {
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
		glBindTexture(GL_TEXTURE_2D, texture); // Select Our Texture
		glBegin(GL_QUADS);
		glTexCoord2f(uvlx, uvly);	
		glVertex3f(x, y, zd);
		glTexCoord2f(uvx, uvly);	
		glVertex3f(xd, y, zd);
		glTexCoord2f(uvx, uvy);		
		glVertex3f(xd, yd, zd); 
		glTexCoord2f(uvlx, uvy);	
		glVertex3f(x, yd, zd);
		glTexCoord2f(uvx, uvly);	
		glVertex3f(x, y, z); 
		glTexCoord2f(uvx, uvy);		
		glVertex3f(x,yd,z);
		glTexCoord2f(uvlx, uvy);	
		glVertex3f(xd, yd, z); 
		glTexCoord2f(uvlx, uvly);	
		glVertex3f(xd, y, z); 
		glTexCoord2f(uvlx, uvy);	
		glVertex3f(x, yd, z); 
		glTexCoord2f(uvlx, uvly);	
		glVertex3f(x, yd, zd); 
		glTexCoord2f(uvx, uvly);	
		glVertex3f(xd, yd, zd);
		glTexCoord2f(uvx, uvy);		
		glVertex3f(xd, yd, z); 
		glTexCoord2f(uvx, uvy); 	
		glVertex3f(x,y,z);
		glTexCoord2f(uvlx, uvly);	
		glVertex3f(xd,y,z);
		glTexCoord2f(uvlx, uvly);	
		glVertex3f(xd,y,zd); 
		glTexCoord2f(uvx, uvly);	
		glVertex3f(x,y,zd);
		glTexCoord2f(uvx, uvly);	
		glVertex3f(xd,y,z); 
		glTexCoord2f(uvx, uvy);		
		glVertex3f(xd,yd,z); 
		glTexCoord2f(uvlx, uvy);	
		glVertex3f(xd,yd,zd); 
		glTexCoord2f(uvlx, uvly);	
		glVertex3f(xd,y,zd); 
		glTexCoord2f(uvlx, uvly);	
		glVertex3f(x,y,z);
		glTexCoord2f(uvx, uvly);	
		glVertex3f(x,y,zd); 
		glTexCoord2f(uvx, uvy);		
		glVertex3f(x,yd,zd); 
		glTexCoord2f(uvlx, uvy);	
		glVertex3f(x,yd,z); 
		glEnd();
	}
	public void drawGuiBorder() {


		glBindTexture(GL_TEXTURE_2D, guitexture); // Select Our Texture

		glBegin(GL_QUADS);
		float texCoord = 1f/16f;
		glTexCoord2f(0, 0);
		glVertex3f(-1, -1, 0); 
		glTexCoord2f(texCoord, 0);
		glVertex3f(17, -1, 0); 
		glTexCoord2f(texCoord, texCoord);
		glVertex3f(17, 0, 0); 
		glTexCoord2f(0, texCoord);
		glVertex3f(-1, 0, 0); 
		glTexCoord2f(0, 0);
		glVertex3f(-1, 16, 0); 
		glTexCoord2f(texCoord, 0);
		glVertex3f(17, 16, 0); 
		glTexCoord2f(texCoord, texCoord);
		glVertex3f(17, 17, 0); 
		glTexCoord2f(0, texCoord);
		glVertex3f(-1, 17, 0); 
		glTexCoord2f(0, 0);
		glVertex3f(-1, -1, 0);
		glTexCoord2f(texCoord, 0);
		glVertex3f(0, -1, 0);
		glTexCoord2f(texCoord, texCoord);
		glVertex3f(0, 17, 0);
		glTexCoord2f(0, texCoord);
		glVertex3f(-1, 17, 0);
		glTexCoord2f(0, 0);
		glVertex3f(16, -1, 0);
		glTexCoord2f(texCoord, 0);
		glVertex3f(17, -1, 0);
		glTexCoord2f(texCoord, texCoord);
		glVertex3f(17, 17, 0); 
		glTexCoord2f(0, texCoord);
		glVertex3f(16, 17, 0); 
		glEnd();
	}
	private void createWindow() throws Exception {
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
		texture = loadTexture(Voxel2JSON.getPath()+".png",GL_TEXTURE0);
		guitexture = loadTexture("textures/gui.png",GL_TEXTURE0);
	}

	private void initGL() {
		glEnable(GL_TEXTURE_2D); // Enable Texture Mapping
		glShadeModel(GL_SMOOTH); // Enable Smooth Shading
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
		glClearDepth(1.0f); // Depth Buffer Setup
		glEnable(GL_DEPTH_TEST); // Enables Depth Testing
		glDepthFunc(GL_LEQUAL); // The Type Of Depth Testing To Do

		glMatrixMode(GL_PROJECTION); // Select The Projection Matrix
		glLoadIdentity(); // Reset The Projection Matrix

		// Calculate The Aspect Ratio Of The Window
		gluPerspective(45.0f, (float) displayMode.getWidth() / (float) displayMode.getHeight(), 0.1f, 100.0f);
		glMatrixMode(GL_MODELVIEW); // Select The Modelview Matrix

		// Really Nice Perspective Calculations
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
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

		int texId = glGenTextures();
		glActiveTexture(textureUnit);
		glBindTexture(GL_TEXTURE_2D, texId);

		// All RGB bytes are aligned to each other and each component is 1 byte
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

		// Upload the texture data and generate mip maps (for scaling)
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, tWidth, tHeight, 0, 
				GL_RGBA, GL_UNSIGNED_BYTE, buf);
		glGenerateMipmap(GL_TEXTURE_2D);

		// Setup the ST coordinate system
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

		// Setup what to do when the texture has to be scaled
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, 
				GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, 
				GL_LINEAR_MIPMAP_LINEAR);
		return texId;
	}
}
