package ca.uqac.nyemo.utils.wavelet; /**
 * Copyright 2014 Mark Bishop This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details: http://www.gnu.org/licenses
 * 
 * The author makes no warranty for the accuracy, completeness, safety, or
 * usefulness of any information provided and does not represent that its use
 * would not infringe privately owned right.
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerListModel;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Class responsibility: Provides a GUI with spinners for setting wavelet
 * transform parameters.
 *
 */
public class ParameterInput extends JDialog {

	private static final long serialVersionUID = 1L;
	public int[] oresults = new int[2];
	public Wavelet wavelet;
	public int coarsestScale;
	public int waveletParameter;
	String mText = "m";
	JLabel lblM = new JLabel(mText);

	int[] parameters;
	int[] scales;
	int fnLength;
	JSpinner spinnerWavelet;
	JSpinner spinnerParam = new JSpinner();
	JSpinner spinnerCoarseScale = new JSpinner();

	/**
	 * @wbp.parser.constructor
	 */
	public ParameterInput(int signalLength) {
		this.wavelet = Wavelet.Daubechies;
		this.fnLength = signalLength;
		initGUI();
	}

	private void updateSpinners() {
		wavelet = (Wavelet) spinnerWavelet.getValue();
		ArrayList<Integer> validParameters = OrthogonalFilters
				.validParameters(wavelet);
		String[] params = new String[validParameters.size()];
		for (int i = 0; i < params.length; i++) {
			params[i] = Integer.toString(validParameters.get(i));
		}
		spinnerParam.setModel(new SpinnerListModel(params));
		try {
			ArrayList<Integer> validScales = OrthogonalFilters.validScales(
					validParameters.get(0), wavelet, fnLength);
			String[] scales = new String[validScales.size()];
			for (int i = 0; i < scales.length; i++) {
				scales[i] = Integer.toString(validScales.get(i));
			}
			spinnerCoarseScale.setModel(new SpinnerListModel(scales));
			spinnerCoarseScale.setEnabled(true);
			stateChange();
		} catch (Exception e) {

		}
	}

	private void stateChange() {
		int param = Integer.valueOf((String) spinnerParam.getValue());
		try {
			ArrayList<Integer> validScales = OrthogonalFilters.validScales(
					param, wavelet, fnLength);
			String[] scales = new String[validScales.size()];
			for (int i = 0; i < scales.length; i++) {
				scales[i] = Integer.toString(validScales.get(i));
			}
			spinnerCoarseScale.setModel(new SpinnerListModel(scales));
			spinnerCoarseScale.setEnabled(true);

		} catch (Exception e) {
			spinnerCoarseScale.setEnabled(false);
		}
	}

	public void initGUI() {
		setTitle("Set transform parameters");
		getContentPane().setLayout(null);
		spinnerParam = new JSpinner();
		spinnerCoarseScale = new JSpinner();
		spinnerCoarseScale.setEnabled(false);
		spinnerWavelet = new JSpinner();
		spinnerWavelet.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				updateSpinners();
			}
		});
		spinnerWavelet.setModel(new SpinnerListModel(Wavelet.values()));
		spinnerWavelet.setBounds(12, 12, 165, 20);
		getContentPane().add(spinnerWavelet);

		updateSpinners();

		spinnerParam.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				stateChange();
			}
		});

		spinnerParam.setBounds(12, 42, 47, 20);
		getContentPane().add(spinnerParam);
		spinnerCoarseScale.setBounds(12, 74, 47, 20);
		getContentPane().add(spinnerCoarseScale);

		JLabel lblN = new JLabel("L");
		lblN.setBounds(73, 73, 18, 20);
		getContentPane().add(lblN);

		lblM = new JLabel("parameter");
		lblM.setBounds(73, 44, 90, 15);
		getContentPane().add(lblM);

		JButton close = new JButton("OK");
		close.setBounds(12, 104, 73, 25);
		close.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				waveletParameter = Integer.parseInt((String) spinnerParam
						.getValue());
				coarsestScale = Integer.parseInt((String) spinnerCoarseScale
						.getValue());
				wavelet = (Wavelet) spinnerWavelet.getValue();
				dispose();
			}
		});

		close.setAlignmentX(0.5f);
		getContentPane().add(close);

		JTextArea txtrLLevel = new JTextArea();
		txtrLLevel.setBackground(UIManager.getColor("DesktopIcon.background"));
		txtrLLevel.setWrapStyleWord(true);
		txtrLLevel.setEditable(false);
		txtrLLevel.setLineWrap(true);
		txtrLLevel.setText("L = level of coarsest scale (2^-L)");
		txtrLLevel.setBounds(119, 76, 165, 91);
		getContentPane().add(txtrLLevel);

		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Enter Dimensions");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(319, 208);
	}
}
