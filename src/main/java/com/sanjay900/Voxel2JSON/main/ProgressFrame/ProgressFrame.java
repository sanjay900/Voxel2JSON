package com.sanjay900.Voxel2JSON.main.ProgressFrame;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;

public class ProgressFrame extends javax.swing.JFrame {

	public ProgressPanel contentPane;
	/**
	 * Create the frame.
	 */
	public ProgressFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 486, 300);
		contentPane = new ProgressPanel();
		contentPane.lblTotal.setText("Total: Calculating");
		contentPane.lblMergedVoxels.setText("Merged Voxels: Calculating");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
	}

}
