package uk.ac.herts.SmartLab.XBeeAPI;

import uk.ac.herts.SmartLab.XBee.Core;
import uk.ac.herts.SmartLab.XBee.ISerial;
import uk.ac.herts.SmartLab.XBee.Type.APIMode;

public class XBeeAPI extends Core {
	public XBeeAPI(String COM) {
		this(new SerialData(COM), APIMode.NORMAL);
	}

	public XBeeAPI(String COM, APIMode mode) {
		this(new SerialData(COM), mode);
	}

	public XBeeAPI(String COM, int baudRate, APIMode mode) {
		this(new SerialData(COM, baudRate), mode);
	}

	public XBeeAPI(ISerial serial, APIMode mode) {
		super(serial, mode);
	}
}
