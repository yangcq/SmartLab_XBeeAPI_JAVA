package uk.ac.herts.SmartLab.XBeeAPI;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import uk.ac.herts.SmartLab.XBee.ISerial;

public class SerialData implements ISerial {

	public static ArrayList<String> getSerialPort() {

		ArrayList<String> list = new ArrayList<String>();
		Enumeration portList = CommPortIdentifier.getPortIdentifiers();

		while (portList.hasMoreElements()) {

			CommPortIdentifier portId = (CommPortIdentifier) portList
					.nextElement();

			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL)
				list.add(portId.getName());
		}
		
		return list;
	}

	private String com;
	private int baudRate;
	private SerialPort serialPort;

	private InputStream input;
	private OutputStream output;

	public SerialData(String COM) {
		this(COM, 9600);
	}

	public SerialData(String COM, int baudRate) {
		this.com = COM;
		this.baudRate = baudRate;
	}

	@Override
	public int ReadByte() throws IOException {
		// TODO Auto-generated method stub
		return input.read();
	}

	@Override
	public void WriteByte(byte data) throws IOException {
		// TODO Auto-generated method stub
		output.write(data);
	}

	@Override
	public boolean IsOpen() {
		// TODO Auto-generated method stub
		if (serialPort == null)
			return false;

		return true;
	}

	@Override
	public void Open() throws Exception {
		// TODO Auto-generated method stub
		serialPort = (SerialPort) CommPortIdentifier
				.getPortIdentifier(this.com).open("SmartLab.XBee.API", 10000);

		serialPort.setSerialPortParams(this.baudRate, SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

		// block call
		serialPort.disableReceiveTimeout();
		serialPort.enableReceiveThreshold(1);

		input = serialPort.getInputStream();
		output = serialPort.getOutputStream();
	}

	@Override
	public void Close() {
		// TODO Auto-generated method stub
		try {
			if (input != null)
				input.close();

			if (output != null)
				output.close();
		} catch (IOException e) {
		}

		if (serialPort != null)
			serialPort.close();
	}

}
