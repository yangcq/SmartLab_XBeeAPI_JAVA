package uk.ac.herts.SmartLab.XBee.Device;

import uk.ac.herts.SmartLab.XBee.Response.ICommandResponse;

public class XBeeDiscoverAddress extends Address {
	private int RSSI;
	protected String NIString;

	// / <summary>
	// / not apply to ZigBee Discovery
	// / </summary>
	// / <returns></returns>
	public int GetRSSI() {
		return RSSI;
	}

	public String GetNIString() {
		return NIString;
	}

	// / <summary>
	// / extension method for convert DN / ND (with or without NI String)
	// response to address
	// / </summary>
	// / <param name="response">muset be non null parameter</param>
	// / <returns></returns>
	public static Address Parse(ICommandResponse response) {
		byte[] message = response.GetParameter();
		if (message != null)
			if (response.GetRequestCommand().toString().equalsIgnoreCase("ND")) {
				XBeeDiscoverAddress device = new XBeeDiscoverAddress();

				device.value[0] = message[2];
				device.value[1] = message[3];
				device.value[2] = message[4];
				device.value[3] = message[5];
				device.value[4] = message[6];
				device.value[5] = message[7];
				device.value[6] = message[8];
				device.value[7] = message[9];

				device.value[8] = message[0];
				device.value[9] = message[1];

				device.RSSI = message[10] * -1;

				device.NIString = new String(message, 11, message.length - 11);

				return device;
			}
		return null;
	}
}