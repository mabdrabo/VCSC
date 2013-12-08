package edu.cmu.sphinx.demo.ScientificCalculatorT14T5;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class Calculator {

	private JFrame frmVoiceControlledScientific;
	ScientificCalculator sc;
	HashMap<String, String> returnMap;
	JLabel lblEquation;
	JLabel lblAnswer;
	Timer progressTimer = new Timer(100, null);
	JProgressBar progressBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Calculator window = new Calculator();
					window.frmVoiceControlledScientific.setVisible(true);
					window.sc = new ScientificCalculator();	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Calculator() {
		initialize();	// initialize the UI
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private synchronized void initialize() {
		frmVoiceControlledScientific = new JFrame();
		frmVoiceControlledScientific.setTitle("Voice Controlled Scientific Calculator");
		frmVoiceControlledScientific.setResizable(false);
		frmVoiceControlledScientific.setBounds(100, 100, 450, 360);
		frmVoiceControlledScientific.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmVoiceControlledScientific.getContentPane().setLayout(null);
		
		lblEquation = new JLabel("");
		lblEquation.setLocation(6, 6);
		lblEquation.setBackground(Color.LIGHT_GRAY);
		lblEquation.setFont(new Font("Bank Gothic", Font.BOLD, 20));
		lblEquation.setVerticalAlignment(SwingConstants.TOP);
		lblEquation.setHorizontalAlignment(SwingConstants.LEFT);
		lblEquation.setOpaque(true);
		lblEquation.setSize(438, 21);
		frmVoiceControlledScientific.getContentPane().add(lblEquation);
		
		lblAnswer = new JLabel("");
		lblAnswer.setVerticalAlignment(SwingConstants.BOTTOM);
		lblAnswer.setOpaque(true);
		lblAnswer.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAnswer.setFont(new Font("Bank Gothic", Font.BOLD, 20));
		lblAnswer.setBackground(Color.LIGHT_GRAY);
		lblAnswer.setBounds(6, 27, 438, 21);
		frmVoiceControlledScientific.getContentPane().add(lblAnswer);
		
		final JPanel beforeVoicepanel = new JPanel();
		beforeVoicepanel.setLayout(null);
		beforeVoicepanel.setBackground(Color.GRAY);
		beforeVoicepanel.setBounds(6, 51, 438, 39);
		frmVoiceControlledScientific.getContentPane().add(beforeVoicepanel);
		
		final JPanel afterVoicePanel = new JPanel();
		afterVoicePanel.setVisible(false);
		afterVoicePanel.setBackground(Color.GRAY);
		afterVoicePanel.setBounds(6, 51, 438, 39);
		frmVoiceControlledScientific.getContentPane().add(afterVoicePanel);
		afterVoicePanel.setLayout(null);
		
		JLabel lblVoice = new JLabel("");
		lblVoice.setBackground(Color.LIGHT_GRAY);
		lblVoice.setBounds(32, 0, 370, 39);
		beforeVoicepanel.add(lblVoice);
		lblVoice.setIcon(new ImageIcon(Calculator.class.getResource("/edu/cmu/sphinx/demo/ScientificCalculatorT14T5/10_device_access_mic.png")));
		lblVoice.addMouseListener(getVoiceMouseAdapter(lblVoice));
		lblVoice.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				returnMap = sc.start(); 
				if (returnMap != null) {
					beforeVoicepanel.setVisible(false);
					afterVoicePanel.setVisible(true);
					lblEquation.setText(returnMap.get("command"));
					lblAnswer.setText("");
					progressTimer.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							if (progressBar.getValue() < 100)
								progressBar.setValue(progressBar.getValue()+1);
							else {
								progressBar.setValue(0);
								progressTimer.stop();
								if (returnMap != null) {
									lblAnswer.setText(returnMap.get("result").toString());
								}
								afterVoicePanel.setVisible(false);
								beforeVoicepanel.setVisible(true);
							}
						}
					});
					progressTimer.restart();
				}
			}
		});
		lblVoice.setHorizontalAlignment(SwingConstants.CENTER);
		lblVoice.setOpaque(true);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(0, 0, 438, 5);
		afterVoicePanel.add(progressBar);
		
		final JLabel lblCalculate = new JLabel("Calculate");
		lblCalculate.setBackground(Color.LIGHT_GRAY);
		lblCalculate.setBounds(0, 9, 190, 30);
		afterVoicePanel.add(lblCalculate);
		lblCalculate.setIcon(new ImageIcon(Calculator.class.getResource("/edu/cmu/sphinx/demo/ScientificCalculatorT14T5/1_navigation_accept.png")));
		lblCalculate.addMouseListener(getVoiceMouseAdapter(lblCalculate));
		lblCalculate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (returnMap != null) {
					lblAnswer.setText(returnMap.get("result").toString());
				}
				progressTimer.stop();
				progressBar.setValue(0);
				afterVoicePanel.setVisible(false);
				beforeVoicepanel.setVisible(true);
			}
		});
		lblCalculate.setHorizontalAlignment(SwingConstants.CENTER);
		lblCalculate.setOpaque(true);
		
		final JLabel lblCancel = new JLabel("Cancel");
		lblCancel.setBackground(Color.LIGHT_GRAY);
		lblCancel.setBounds(248, 9, 190, 30);
		afterVoicePanel.add(lblCancel);
		lblCancel.setIcon(new ImageIcon(Calculator.class.getResource("/edu/cmu/sphinx/demo/ScientificCalculatorT14T5/1_navigation_cancel.png")));
		lblCancel.addMouseListener(getVoiceMouseAdapter(lblCancel));
		lblCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				returnMap = null;
				progressTimer.stop();
				progressBar.setValue(0);
				lblEquation.setText("");
				lblAnswer.setText("");
				afterVoicePanel.setVisible(false);
				beforeVoicepanel.setVisible(true);
			}
		});
		lblCancel.setHorizontalAlignment(SwingConstants.CENTER);
		lblCancel.setOpaque(true);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBounds(6, 94, 438, 238);
		frmVoiceControlledScientific.getContentPane().add(buttonsPanel);
		buttonsPanel.setLayout(new GridLayout(6, 5, 2, 2));
		
		JLabel lbl7 = new JLabel("7");
		lbl7.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lbl7.addMouseListener(getUIMouseAdapter("7"));
		lbl7.setOpaque(true);
		lbl7.setBackground(Color.GRAY);
		lbl7.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lbl7);
		
		JLabel lbl8 = new JLabel("8");
		lbl8.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lbl8.addMouseListener(getUIMouseAdapter("8"));
		lbl8.setOpaque(true);
		lbl8.setBackground(Color.GRAY);
		lbl8.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lbl8);
		
		JLabel lbl9 = new JLabel("9");
		lbl9.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lbl9.addMouseListener(getUIMouseAdapter("9"));
		lbl9.setOpaque(true);
		lbl9.setBackground(Color.GRAY);
		lbl9.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lbl9);
		
		JLabel lblClr = new JLabel("CLEAR");
		lblClr.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblClr.addMouseListener(getUIMouseAdapter("CLEAR"));
		lblClr.setBackground(Color.DARK_GRAY);
		lblClr.setForeground(Color.WHITE);
		lblClr.setOpaque(true);
		lblClr.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblClr.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lblClr);
		
		JLabel lblSqrt = new JLabel("SQRT");
		lblSqrt.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblSqrt.addMouseListener(getUIMouseAdapter("SQRT"));
		lblSqrt.setBackground(Color.LIGHT_GRAY);
		lblSqrt.setForeground(Color.BLUE);
		lblSqrt.setOpaque(true);
		lblSqrt.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lblSqrt);
		
		JLabel lbl4 = new JLabel("4");
		lbl4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lbl4.addMouseListener(getUIMouseAdapter("4"));
		lbl4.setOpaque(true);
		lbl4.setBackground(Color.GRAY);
		lbl4.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lbl4);

		JLabel lbl5 = new JLabel("5");
		lbl5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lbl5.addMouseListener(getUIMouseAdapter("5"));
		lbl5.setOpaque(true);
		lbl5.setBackground(Color.GRAY);
		lbl5.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lbl5);

		JLabel lbl6 = new JLabel("6");
		lbl6.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lbl6.addMouseListener(getUIMouseAdapter("6"));
		lbl6.setOpaque(true);
		lbl6.setBackground(Color.GRAY);
		lbl6.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lbl6);

		JLabel lblPlus = new JLabel("+");
		lblPlus.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblPlus.addMouseListener(getUIMouseAdapter("+"));
		lblPlus.setBackground(Color.LIGHT_GRAY);
		lblPlus.setForeground(Color.BLUE);
		lblPlus.setOpaque(true);
		lblPlus.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lblPlus);

		JLabel lblCbrt = new JLabel("CBRT");
		lblCbrt.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblCbrt.addMouseListener(getUIMouseAdapter("CBRT"));
		lblCbrt.setBackground(Color.LIGHT_GRAY);
		lblCbrt.setForeground(Color.BLUE);
		lblCbrt.setOpaque(true);
		lblCbrt.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lblCbrt);

		JLabel lbl1 = new JLabel("1");
		lbl1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lbl1.addMouseListener(getUIMouseAdapter("1"));
		lbl1.setOpaque(true);
		lbl1.setBackground(Color.GRAY);
		lbl1.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lbl1);
		
		JLabel lbl2 = new JLabel("2");
		lbl2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lbl2.addMouseListener(getUIMouseAdapter("2"));
		lbl2.setOpaque(true);
		lbl2.setBackground(Color.GRAY);
		lbl2.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lbl2);
		
		JLabel lbl3 = new JLabel("3");
		lbl3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lbl3.addMouseListener(getUIMouseAdapter("3"));
		lbl3.setOpaque(true);
		lbl3.setBackground(Color.GRAY);
		lbl3.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lbl3);
		
		JLabel lblMinus = new JLabel("-");
		lblMinus.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblMinus.addMouseListener(getUIMouseAdapter("-"));
		lblMinus.setBackground(Color.LIGHT_GRAY);
		lblMinus.setForeground(Color.BLUE);
		lblMinus.setOpaque(true);
		lblMinus.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lblMinus);
		
		JLabel lblLog = new JLabel("LOG");
		lblLog.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblLog.addMouseListener(getUIMouseAdapter("LOG"));
		lblLog.setBackground(Color.LIGHT_GRAY);
		lblLog.setForeground(Color.BLUE);
		lblLog.setOpaque(true);
		lblLog.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lblLog);
		
		JLabel lblA = new JLabel("A");
		lblA.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblA.addMouseListener(getUIMouseAdapter("A"));
		lblA.setBackground(Color.DARK_GRAY);
		lblA.setForeground(Color.GREEN);
		lblA.setOpaque(true);
		lblA.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lblA);
		
		JLabel lbl0 = new JLabel("0");
		lbl0.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lbl0.addMouseListener(getUIMouseAdapter("0"));
		lbl0.setOpaque(true);
		lbl0.setBackground(Color.GRAY);
		lbl0.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lbl0);
		
		JLabel lblEqual = new JLabel("=");
		lblEqual.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblEqual.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String command = lblEquation.getText();
				command = command.replace("e", "eu");
				command = command.replace("+", " plus ");
				command = command.replace("-", " minus ");
				command = command.replace("x", " multiply ");
				command = command.replace("/", " divide ");
				command = command.replace("+", " plus ");
				command = command.replace("^", " power ");
				command = command.replace("M", "retrieve last result");
				command = command.replace("SQRT", "square root of ");
				command = command.replace("CBRT", "cubic root of ");
				command = command.replace("LOG", "log ");
				command = command.toLowerCase();
				System.out.println("command: " + command);
				if (lblEquation.getText().contains("str"))
					command = command.replace("str", "store ") + " " + lblAnswer.getText().trim();
				if (lblEquation.getText().equals("str"))
					command = "store last result";
				System.out.println("command: " + command);
				returnMap = sc.parse(command);
				if (returnMap != null) {
					lblAnswer.setText(returnMap.get("result").toString());
				} else lblEquation.setText("Error");
			}
		});
		lblEqual.setBackground(Color.DARK_GRAY);
		lblEqual.setForeground(Color.WHITE);
		lblEqual.setOpaque(true);
		lblEqual.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lblEqual);
		
		JLabel lblMultiply = new JLabel("x");
		lblMultiply.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblMultiply.addMouseListener(getUIMouseAdapter("x"));
		lblMultiply.setBackground(Color.LIGHT_GRAY);
		lblMultiply.setForeground(Color.BLUE);
		lblMultiply.setOpaque(true);
		lblMultiply.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lblMultiply);

		JLabel lblPwr = new JLabel("PWR");
		lblPwr.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblPwr.addMouseListener(getUIMouseAdapter("^"));
		lblPwr.setBackground(Color.LIGHT_GRAY);
		lblPwr.setForeground(Color.BLUE);
		lblPwr.setOpaque(true);
		lblPwr.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lblPwr);

		JLabel lblB = new JLabel("B");
		lblB.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblB.addMouseListener(getUIMouseAdapter("B"));
		lblB.setBackground(Color.DARK_GRAY);
		lblB.setForeground(Color.GREEN);
		lblB.setOpaque(true);
		lblB.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lblB);

		JLabel lblPi = new JLabel("Pi");
		lblPi.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblPi.addMouseListener(getUIMouseAdapter("Pi"));
		lblPi.setBackground(Color.DARK_GRAY);
		lblPi.setForeground(Color.YELLOW);
		lblPi.setOpaque(true);
		lblPi.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 13));
		lblPi.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lblPi);

		JLabel lblE = new JLabel("e");
		lblE.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblE.addMouseListener(getUIMouseAdapter("e"));
		lblE.setBackground(Color.DARK_GRAY);
		lblE.setForeground(Color.YELLOW);
		lblE.setOpaque(true);
		lblE.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 13));
		lblE.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lblE);

		JLabel lblDivide = new JLabel("/");
		lblDivide.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblDivide.addMouseListener(getUIMouseAdapter("/"));
		lblDivide.setBackground(Color.LIGHT_GRAY);
		lblDivide.setForeground(Color.BLUE);
		lblDivide.setOpaque(true);
		lblDivide.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lblDivide);
		
		JLabel lblStr = new JLabel("STR");
		lblStr.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblStr.addMouseListener(getUIMouseAdapter("str"));
		lblStr.setBackground(Color.DARK_GRAY);
		lblStr.setForeground(Color.RED);
		lblStr.setOpaque(true);
		lblStr.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lblStr);
		
		JLabel lblC = new JLabel("C");
		lblC.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblC.addMouseListener(getUIMouseAdapter("C"));
		lblC.setBackground(Color.DARK_GRAY);
		lblC.setForeground(Color.GREEN);
		lblC.setOpaque(true);
		lblC.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lblC);
		
		JLabel lblX = new JLabel("X");
		lblX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblX.addMouseListener(getUIMouseAdapter("X"));
		lblX.setBackground(Color.DARK_GRAY);
		lblX.setForeground(Color.GREEN);
		lblX.setOpaque(true);
		lblX.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lblX);
		
		JLabel lblY = new JLabel("Y");
		lblY.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblY.addMouseListener(getUIMouseAdapter("Y"));
		lblY.setBackground(Color.DARK_GRAY);
		lblY.setForeground(Color.GREEN);
		lblY.setOpaque(true);
		lblY.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lblY);
		
		JLabel lblZ = new JLabel("Z");
		lblZ.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblZ.addMouseListener(getUIMouseAdapter("Z"));
		lblZ.setBackground(Color.DARK_GRAY);
		lblZ.setForeground(Color.GREEN);
		lblZ.setOpaque(true);
		lblZ.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lblZ);
		
		JLabel lblM = new JLabel("M");
		lblM.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblM.addMouseListener(getUIMouseAdapter("M"));
		lblM.setForeground(Color.RED);
		lblM.setBackground(Color.DARK_GRAY);
		lblM.setOpaque(true);
		lblM.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblM.setHorizontalAlignment(SwingConstants.CENTER);
		buttonsPanel.add(lblM);
	}
	
	public MouseAdapter getVoiceMouseAdapter(final JComponent component) { 
		return new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				component.setBackground(Color.GRAY);
				component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				component.setBackground(Color.LIGHT_GRAY);
			}
	 	};
	}
	
	public MouseAdapter getUIMouseAdapter(final String name) {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblEquation.setText(lblEquation.getText() + name);
				if (name.equals("CLEAR"))
					lblEquation.setText("");
			}
		};
	}
}
