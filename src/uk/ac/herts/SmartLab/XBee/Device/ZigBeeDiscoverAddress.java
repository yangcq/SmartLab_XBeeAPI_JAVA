package uk.ac.herts.SmartLab.XBee.Device;

import uk.ac.herts.SmartLab.XBee.Response.CommandResponseBase;
import uk.ac.herts.SmartLab.XBee.Type.DeviceType;

public class ZigBeeDiscoverAddress extends XBeeDiscoverAddress {
	// total 8 bytes
	// 2 bytes of ParentAddress16 + 1 byte of Type + 1 byte of Status (Reserved)
	// + 2 bytes of ProfileID + 2 bytes of ManufacturerID
	private byte[] zigbeeAdditional;

	public int ManufacturerID() {
		return (zigbeeAdditional[6] << 8) | zigbeeAdditional[7];
	}

	public int GetProfileID() {
		return (zigbeeAdditional[4] << 8) | zigbeeAdditional[5];
	}

	public int GetParentNetworkAddress16() {
		return (zigbeeAdditional[0] << 8) | zigbeeAdditional[1];
	}

	public DeviceType GetDeviceType() {
		return DeviceType.parse(zigbeeAdditional[2]);
	}

	// / <summary>
	// / extension method for convert DN / ND (with or without NI String)
	// response to address
	// / </summary>
	// / <param name="response">muset be non null parameter</param>
	// / <returns></returns>
	public static ZigBeeDiscoverAddress Parse(CommandResponseBase response) {
		if (response == null)
			return null;

		if (!response.GetRequestCommand().toString().equalsIgnoreCase("ND"))
			return null;

		int length = response.GetParameterLength();
		if (length <= 0)
			return null;

		ZigBeeDiscoverAddress device = new ZigBeeDiscoverAddress();
		int offset = response.GetParameterLength() - 8;

		System.arraycopy(response.GetFrameData(),
				response.GetParameterOffset() + 2, device.value, 0, 8);
		device.value[8] = response.GetParameter(0);
		device.value[9] = response.GetParameter(1);

		device.NIString = new String(response.GetFrameData(),
				response.GetParameterOffset() + 10,
				response.GetParameterLength() - 18);
		System.arraycopy(response.GetFrameData(), response.GetParameterOffset()
				+ offset, device.zigbeeAdditional, 0, 8);

		return device;
	}
}