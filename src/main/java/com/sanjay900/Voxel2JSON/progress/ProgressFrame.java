package com.sanjay900.Voxel2JSON.progress;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;

public class ProgressFrame extends javax.swing.JFrame {

	public ProgressPanel2 contentPane;
	/**
	 * Create the frame.
	 */
	public ProgressFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 486, 300);
		contentPane = new ProgressPanel2();
		contentPane.getVoxelCount().setText("Total: Calculating");
		contentPane.getMergedVoxelCount().setText("Merged Voxels: Calculating");
		setContentPane(contentPane.$$$getRootComponent$$$());
	}

}
