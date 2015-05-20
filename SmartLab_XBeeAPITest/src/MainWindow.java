import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.IOSamples;
import uk.ac.herts.SmartLab.XBeeAPI.SerialData;
import uk.ac.herts.SmartLab.XBeeAPI.XBeeAPI;
import uk.ac.herts.SmartLab.XBee.XBeeAPIResponseListener;
import uk.ac.herts.SmartLab.XBee.Device.Address;
import uk.ac.herts.SmartLab.XBee.Device.Pin;
import uk.ac.herts.SmartLab.XBee.Device.Pin.Status;
import uk.ac.herts.SmartLab.XBee.Options.OptionsBase;
import uk.ac.herts.SmartLab.XBee.Request.*;
import uk.ac.herts.SmartLab.XBee.Indicator.*;
import uk.ac.herts.SmartLab.XBee.Type.APIMode;

import javax.swing.DefaultComboBoxModel;

public class MainWindow extends JFrame implements XBeeAPIResponseListener {

	private JPanel contentPane;
	private JButton btnNewButton;
	private JComboBox<String> comboBox;
	JComboBox<String> comboBox_1;
	private DefaultListModel<String> listModel;
	private XBeeAPI xbee;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		MainWindow frame = new MainWindow();
		frame.setVisible(true);

		frame.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				System.out.println("windowActivated");
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
			
				if (frame.xbee != null)
					frame.xbee.Stop();

				System.out.println("windowClosed");
			}

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				System.out.println("windowClosing");
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				System.out.println("windowDeactivated");
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				System.out.println("windowDeiconified");
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				System.out.println("windowIconified");
			}

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				System.out.println("windowOpened");
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 500);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		comboBox = new JComboBox<String>();
		comboBox.setBounds(10, 11, 130, 23);
		contentPane.add(comboBox);

		btnNewButton = new JButton("Connect");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				System.out.println("Select : " + comboBox.getSelectedItem());

				try {
					xbee = new XBeeAPI((String) comboBox.getSelectedItem(),
							Integer.parseInt((String) comboBox_1
									.getSelectedItem()), APIMode.ESCAPED);

					xbee.AddResponseListener(MainWindow.this);

					xbee.Start();

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		btnNewButton.setBounds(290, 11, 130, 23);
		contentPane.add(btnNewButton);

		listModel = new DefaultListModel<String>();

		JButton btnNewButton_1 = new JButton("New button");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Address add = new Address(0x13a200, 0x40a925b1, 0x1234);
				XBeeTx64Request zigtx = new XBeeTx64Request(1, add,
						OptionsBase.DEFAULT, "Hello1".getBytes());
				
				for (int i = 0; i< 1000 ; i++)
				{
					//xbee.Send(zigtx);
					System.out.println("\r\n" + i);
					long s = System.currentTimeMillis();
					
					
					 XBeeTxStatusIndicator re = xbee.SendXBeeTx16(add, OptionsBase.DEFAULT, ("payload:" + i).getBytes());
				
					
					if (re != null)
						System.out.println((System.currentTimeMillis() - s) + " " + re.GetDeliveryStatus());
					else System.out.println("timeout");
					
					
				}
				
					
					
				/*
				// TxStatusBase re = xbee.SendXBeeTx16(new Address(0, 0, 0x501),
				// OptionsBase.DEFAULT, "Hello1".getBytes());

				// Address add = new Address(0x13a200, 0x40af318e, 0);
				Address add = new Address(0x13a200, 0x40a881f8, 0);

				XBeeTx16Request zigtx = new XBeeTx16Request(1, add,
						OptionsBase.DEFAULT, "Hello1".getBytes());
				xbee.Send(zigtx);

				zigtx.SetPayload("hhhhhhhhhhhhhhhhhhhhh fdaf".getBytes());

				xbee.Send(zigtx);

				zigtx.SetPayload("12".getBytes());

				xbee.Send(zigtx);

				XBeeTxStatusIndicator re = xbee.SendXBeeTx64(add,
						OptionsBase.DEFAULT, "payload".getBytes());

				if (re != null)
					System.out.println(re.GetDeliveryStatus());
				
				re = xbee.SendXBeeTx16(add,
						OptionsBase.DEFAULT, "payload".getBytes());

				if (re != null)
					System.out.println(re.GetDeliveryStatus());
				*/
			}
		});
		btnNewButton_1.setBounds(96, 134, 89, 23);
		contentPane.add(btnNewButton_1);

		comboBox_1 = new JComboBox<String>();
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] { "9600",
				"19200", "38400", "57600", "115200" }));
		comboBox_1.setBounds(150, 11, 130, 23);
		contentPane.add(comboBox_1);

		textField = new JTextField();
		textField.setBounds(76, 45, 130, 20);
		contentPane.add(textField);
		textField.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setBounds(216, 45, 130, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);

		textField_2 = new JTextField();
		textField_2.setBounds(356, 45, 130, 20);
		contentPane.add(textField_2);
		textField_2.setColumns(10);

		JLabel lblAddress = new JLabel("Address :");
		lblAddress.setBounds(20, 48, 46, 14);
		contentPane.add(lblAddress);

		JButton btnIo = new JButton("IO");
		btnIo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(xbee.ForceXBeeLocalIOSample());
			}
		});
		btnIo.setBounds(342, 134, 89, 23);
		contentPane.add(btnIo);

		JButton btnRio = new JButton("RIO");
		btnRio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Address add = new Address(0x13a200, 0x40a881f8, 0);
				IOSamples samples = xbee.ForceXBeeRemoteIOSample(add);

				if (samples == null)
					return;

				System.out.println("Digital :");
				for (Entry<Pin, Status> pin : samples.GetDigitals().entrySet())
					System.out.println(pin.getKey().getNumber() + " " + pin.getValue());
				System.out.println("Analog :");
				for (Entry<Pin, Integer> pin : samples.GetAnalogs().entrySet())
					System.out.println(pin.getKey().getNumber() + " " + pin.getValue());
			}
		});
		btnRio.setBounds(461, 134, 89, 23);
		contentPane.add(btnRio);

		GetAvaliablePort();
	}

	private void GetAvaliablePort() {

		for (String name : SerialData.getSerialPort())
			comboBox.addItem(name);

	}

	@Override
	public void onChecksumErrorIndicator(APIFrame indicator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUndefinedPacketIndicator(APIFrame indicator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onATCommandIndicator(ATCommandIndicator indicator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onModemStatusIndicator(ModemStatusIndicator indicator) {
		// TODO Auto-generated method stub
		System.out.println(indicator.GetModemStatus());
	}

	@Override
	public void onNodeIdentificationIndicator(
			NodeIdentificationIndicator indicator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRemoteCommandIndicator(RemoteCommandIndicator indicator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onXBeeIODataSampleRx16Indicator(
			XBeeRx16IOSampleIndicator indicator) {
		// TODO Auto-generated method stub

		IOSamples[] ios = indicator.GetIOSamples();

		for (IOSamples io : ios) {
			System.out.println("Digital :");
			for (Entry<Pin, Status> pin : io.GetDigitals().entrySet())
				System.out.println(pin.getKey().getNumber() + " " + pin.getValue());
			System.out.println("Analog :");
			for (Entry<Pin, Integer> pin : io.GetAnalogs().entrySet())
				System.out.println(pin.getKey().getNumber() + " " + pin.getValue());
		}
	}

	@Override
	public void onXBeeIODataSampleRx64Indicator(
			XBeeRx64IOSampleIndicator indicator) {
		// TODO Auto-generated method stub

		IOSamples[] ios = indicator.GetIOSamples();

		for (IOSamples io : ios) {
			System.out.println("Digital :");
			for (Entry<Pin, Status> pin : io.GetDigitals().entrySet())
				System.out.println(pin.getKey().getNumber() + " " + pin.getValue());
			System.out.println("Analog :");
			for (Entry<Pin, Integer> pin : io.GetAnalogs().entrySet())
				System.out.println(pin.getKey().getNumber() + " " + pin.getValue());
		}
	}

	@Override
	public void onXBeeRx16Indicator(XBeeRx16Indicator indicator) {
		// TODO Auto-generated method stub

		System.out.println(new String(indicator.GetFrameData(), indicator
				.GetReceivedDataOffset(), indicator.GetReceivedDataLength()));

	}

	@Override
	public void onXBeeRx64Indicator(XBeeRx64Indicator indicator) {
		// TODO Auto-generated method stub

		System.out.println(new String(indicator.GetFrameData(), indicator
				.GetReceivedDataOffset(), indicator.GetReceivedDataLength()));

	}

	@Override
	public void onXBeeSensorReadIndicator(SensorReadIndicator indicator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onXBeeTransmitStatusIndicator(XBeeTxStatusIndicator indicator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onZigBeeExplicitRxIndicator(ZigBeeExplicitRxIndicator indicator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onZigBeeIODataSampleRXIndicator(
			ZigBeeIOSampleIndicator indicator) {
		// TODO Auto-generated method stub

		IOSamples[] ios = indicator.GetIOSamples();

		for (IOSamples io : ios) {
			System.out.println("Digital :");
			for (Entry<Pin, Status> pin : io.GetDigitals().entrySet())
				System.out.println(pin.getKey().getNumber() + " " + pin.getValue());
			System.out.println("Analog :");
			for (Entry<Pin, Integer> pin : io.GetAnalogs().entrySet())
				System.out.println(pin.getKey().getNumber() + " " + pin.getValue());
		}
	}

	@Override
	public void onZigBeeReceivePacketIndicator(ZigBeeRxIndicator indicator) {
		// TODO Auto-generated method stub

		System.out.println(new String(indicator.GetFrameData(), indicator
				.GetReceivedDataOffset(), indicator.GetReceivedDataLength()));
	}

	@Override
	public void onZigBeeTransmitStatusIndicator(
			ZigBeeTxStatusIndicator indicator) {
		// TODO Auto-generated method stub
		System.out.println(indicator.GetDeliveryStatus());
	}

	@Override
	public void onManyToOneRequestIndicator(ZigBeeManyToOneIndicator indicator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRouteRecordIndicator(ZigBeeRouteRecordIndicator indicator) {
		// TODO Auto-generated method stub

	}
}
