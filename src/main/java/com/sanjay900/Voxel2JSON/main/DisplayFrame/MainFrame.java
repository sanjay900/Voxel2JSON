package com.sanjay900.Voxel2JSON.main.DisplayFrame;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import lombok.AllArgsConstructor;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	JSlider rx;
	JSlider ry;
	JSlider rz;
	JSlider tx;
	JSlider ty;
	JSlider tz;
	JSlider sx;
	JSlider sy;
	JSlider sz;
	ViewType type = ViewType.IN_HAND;
	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("Item Display Properties - In Hand");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 490, 411);
		setPreferredSize(new Dimension(490,411));

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JButton btnInHand = new JButton("In Hand");
		btnInHand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				type = ViewType.IN_HAND;
				setTitle("Item Display Properties - "+((JButton)e.getSource()).getText());
			}
		});
		menuBar.add(btnInHand);
		
		JButton btnOnHead = new JButton("On Head");
		btnOnHead.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				type = ViewType.ON_HEAD;
				setTitle("Item Display Properties - "+((JButton)e.getSource()).getText());
			}
		});
		menuBar.add(btnOnHead);
		
		JButton btnOnFloor = new JButton("On Floor");
		menuBar.add(btnOnFloor);
		btnOnFloor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				type = ViewType.ON_FLOOR;
				setTitle("Item Display Properties - "+((JButton)e.getSource()).getText());
			}
		});
		JButton btnOnWall = new JButton("On Wall");
		menuBar.add(btnOnWall);
		btnOnWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				type = ViewType.ON_WALL;
				setTitle("Item Display Properties - "+((JButton)e.getSource()).getText());
			}
		});
		JButton btnstPerson = new JButton("First Person");
		menuBar.add(btnstPerson);
		btnstPerson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				type = ViewType.FIRST_PERSON;
				setTitle("Item Display Properties - "+((JButton)e.getSource()).getText());
			}
		});
		JButton btnGui = new JButton("GUI");
		menuBar.add(btnGui);
		btnGui.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				type = ViewType.GUI;
				setTitle("Item Display Properties - "+((JButton)e.getSource()).getText());
			}
		});
		contentPane = new JPanel();
		contentPane.setBounds(0,0, 489, 310);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(3, 1, 0, 0));

		JPanel rotation = new JPanel();
		rotation.setBorder(BorderFactory.createTitledBorder("Rotation"));
		contentPane.add(rotation);
		rotation.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("400px"),
				ColumnSpec.decode("40px"),},
				new RowSpec[] {
						FormSpecs.PARAGRAPH_GAP_ROWSPEC,
						RowSpec.decode("26px"),
						RowSpec.decode("26px"),
						RowSpec.decode("26px"),}));
		JPanel translation = new JPanel();
		translation.setBorder(BorderFactory.createTitledBorder("Translation"));
		contentPane.add(translation);
		translation.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("400px"),
				ColumnSpec.decode("40px"),},
				new RowSpec[] {
						FormSpecs.PARAGRAPH_GAP_ROWSPEC,
						RowSpec.decode("26px"),
						RowSpec.decode("26px"),
						RowSpec.decode("26px"),}));
		JPanel scale = new JPanel();
		scale.setBorder(BorderFactory.createTitledBorder("Scale"));
		contentPane.add(scale);
		scale.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("400px"),
				ColumnSpec.decode("40px"),},
				new RowSpec[] {
						FormSpecs.PARAGRAPH_GAP_ROWSPEC,
						RowSpec.decode("26px"),
						RowSpec.decode("26px"),
						RowSpec.decode("26px"),}));

		JTextField rxl = new JTextField("0");
		JTextField ryl = new JTextField("0");
		JTextField rzl = new JTextField("0");
		JTextField txl = new JTextField("0");
		JTextField tyl = new JTextField("0");
		JTextField tzl = new JTextField("0");
		JTextField sxl = new JTextField("1");
		JTextField syl = new JTextField("1");
		JTextField szl = new JTextField("1");
		rx = new JSlider();
		rx.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				rxl.setText(rx.getValue()/100f+"");
			}
		});
		rx.setMinimum(-18000);
		rx.setMaximum(18000);
		rx.setValue(0);

		ry = new JSlider();
		ry.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ryl.setText(ry.getValue()/100f+"");
			}
		});
		ry.setValue(0);
		ry.setMinimum(-18000);
		ry.setMaximum(18000);

		rz = new JSlider();
		rz.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				rzl.setText(rz.getValue()/100f+"");
			}
		});
		rz.setMinimum(-18000);
		rz.setMaximum(18000);
		rz.setValue(0);
		rotation.add(rx, "2, 2, fill, fill");
		rotation.add(rxl, "3, 2, default, fill");
		rotation.add(ry, "2, 3, fill, fill");
		rotation.add(ryl, "3, 3, fill, fill");
		rotation.add(rz, "2, 4, fill, fill");
		rotation.add(rzl, "3, 4, fill, fill");

		//Translation
		tx = new JSlider();
		tx.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				txl.setText(tx.getValue()/100f+"");
			}
		});
		tx.setMinimum(-1500);
		tx.setMaximum(1500);
		tx.setValue(0);

		ty = new JSlider();
		ty.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				tyl.setText(ty.getValue()/100f+"");
			}
		});
		ty.setValue(0);
		ty.setMinimum(-1500);
		ty.setMaximum(1500);

		tz = new JSlider();
		tz.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				tzl.setText(tz.getValue()/100f+"");
			}
		});
		tz.setMinimum(-1500);
		tz.setMaximum(1500);
		tz.setValue(0);
		translation.add(tx, "2, 2, fill, fill");
		translation.add(txl, "3, 2, default, fill");
		translation.add(ty, "2, 3, fill, fill");
		translation.add(tyl, "3, 3, fill, fill");
		translation.add(tz, "2, 4, fill, fill");
		translation.add(tzl, "3, 4, fill, fill");
		//scale
		sx = new JSlider();
		sx.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sxl.setText(sx.getValue()/100f+"");
			}
		});
		sx.setMinimum(-400);
		sx.setMaximum(400);
		sx.setValue(100);

		sy = new JSlider();
		sy.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				syl.setText(sy.getValue()/100f+"");
			}
		});
		sy.setValue(100);
		sy.setMinimum(-400);
		sy.setMaximum(400);

		sz = new JSlider();
		sz.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				szl.setText(sz.getValue()/100f+"");
			}
		});
		sz.setMinimum(-400);
		sz.setMaximum(400);
		sz.setValue(100);
		scale.add(sx, "2, 2, fill, fill");
		scale.add(sxl, "3, 2, default, fill");
		scale.add(sy, "2, 3, fill, fill");
		scale.add(syl, "3, 3, fill, fill");
		scale.add(sz, "2, 4, fill, fill");
		scale.add(szl, "3, 4, fill, fill");

		rxl.addActionListener(new SliderSync(rxl,rx));
		ryl.addActionListener(new SliderSync(ryl,ry));
		rzl.addActionListener(new SliderSync(rzl,rz));
		txl.addActionListener(new SliderSync(txl,tx));
		tyl.addActionListener(new SliderSync(tyl,ty));
		tzl.addActionListener(new SliderSync(tzl,tz));
		sxl.addActionListener(new SliderSync(sxl,sx));
		syl.addActionListener(new SliderSync(syl,sy));
		szl.addActionListener(new SliderSync(szl,sz));
	}
	@AllArgsConstructor
	public class SliderSync implements ActionListener {
		JTextField component;
		JSlider slider;
		@Override
		public void actionPerformed(ActionEvent e) {
			float f = 0;
			try {
				f = Float.parseFloat(component.getText());
				slider.setValue((int) (f*100));
			} catch (Exception ex) {
				component.setText(slider.getValue()/100F+"");
			}
		}
		
	}

}
