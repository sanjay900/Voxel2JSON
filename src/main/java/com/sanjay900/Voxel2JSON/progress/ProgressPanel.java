package com.sanjay900.Voxel2JSON.progress;

import java.awt.Desktop;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.sanjay900.Voxel2JSON.Voxel2JSON;

public class ProgressPanel extends JPanel {
	public JLabel lblConvertingPleaseWait;
	public JLabel lblOverallProgress;
	public JProgressBar overallProgress;
	public JLabel overallInfo;
	public JLabel lblNewLabel;
	public JProgressBar progressBar;
	public JLabel lblMergedVoxels;
	public JLabel lblVoxels;
	public JLabel subStatus;
	public JLabel lblTotal;
	public JButton btnOpenCompletedFile;
	private JLabel lblvoxFileVersion;

	/**
	 * Create the panel.
	 */
	public ProgressPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("175px"),
				ColumnSpec.decode("106px"),
				ColumnSpec.decode("169px"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("14px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("14px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("14px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("14px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("14px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("14px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("14px"),
				RowSpec.decode("31px"),
				RowSpec.decode("14px"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));

		lblConvertingPleaseWait = new JLabel("Converting, Please Wait");
		lblConvertingPleaseWait.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblConvertingPleaseWait, "1, 2, 3, 1, fill, top");
		
		lblvoxFileVersion = new JLabel(".Vox File Version:");
		add(lblvoxFileVersion, "1, 4, 3, 1, center, default");
		lblvoxFileVersion.setText(".Vox File Version: "+Voxel2JSON.getVersion());
		lblOverallProgress = new JLabel("Overall Progress");
		lblOverallProgress.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblOverallProgress, "1, 6, 3, 1, fill, top");

		overallProgress = new JProgressBar();
		add(overallProgress, "1, 8, 3, 1, fill, top");

		overallInfo = new JLabel("Status:");
		overallInfo.setHorizontalAlignment(SwingConstants.CENTER);
		add(overallInfo, "1, 10, 3, 1, fill, top");

		lblNewLabel = new JLabel("Sub Progress");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblNewLabel, "1, 12, 3, 1, fill, top");

		progressBar = new JProgressBar();
		add(progressBar, "1, 14, 3, 1, fill, top");

		subStatus = new JLabel("Status:");
		subStatus.setHorizontalAlignment(SwingConstants.CENTER);
		add(subStatus, "1, 16, 3, 1, fill, top");

		lblVoxels = new JLabel("Voxels:");
		add(lblVoxels, "1, 18, right, top");

		lblMergedVoxels = new JLabel("Merged Voxels:");
		add(lblMergedVoxels, "3, 18, left, top");

		setBounds(100, 100, 454, 236);

		lblTotal = new JLabel("Total:");
		add(lblTotal, "2, 20, center, default");

		btnOpenCompletedFile = new JButton("Open Completed File");
		btnOpenCompletedFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().open(Paths.get(Voxel2JSON.getPath()).getParent().toFile());
						getTopLevelAncestor().dispatchEvent(new WindowEvent((Window) getTopLevelAncestor(), WindowEvent.WINDOW_CLOSING));
					} catch (IOException ex) { /* TODO: error handling */ }
				} else { /* TODO: error handling */ }
			}
		});
		btnOpenCompletedFile.setEnabled(false);
		add(btnOpenCompletedFile, "1, 22, 3, 1");
	}
}
