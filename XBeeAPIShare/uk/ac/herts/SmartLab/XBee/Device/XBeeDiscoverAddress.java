package uk.ac.herts.SmartLab.XBee.Device;

import uk.ac.herts.SmartLab.XBee.Indicator.CommandIndicatorBase;

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
	public static XBeeDiscoverAddress Parse(CommandIndicatorBase response) {
		if (response == null)
			return null;

		if (!response.GetRequestCommand().toString().equalsIgnoreCase("ND"))
			return null;

		int length = response.GetParameterLength();
		if (length <= 0)
			return null;

		XBeeDiscoverAddress device = new XBeeDiscoverAddress();

		System.arraycopy(response.GetFrameData(),
				response.GetParameterOffset() + 2, device.value, 0, 8);
		device.value[8] = response.GetParameter(0);
		device.value[9] = response.GetParameter(1);

		device.RSSI = response.GetParameter(10) * -1;

		device.NIString = new String(response.GetFrameData(),
				response.GetParameterOffset() + 11,
				response.GetParameterLength() - 11);

		return device;
	}
}