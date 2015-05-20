package uk.ac.herts.SmartLab.XBee.Request;

import uk.ac.herts.SmartLab.XBee.Device.Address;
import uk.ac.herts.SmartLab.XBee.Type.API_IDENTIFIER;

public class CreateSourceRouteRequest extends TxBase {
	// 0x21
	// FrameID
	// RemoteDevice (64 + 16)
	// 0x00
	// Number of Address
	// Address List

	// / <summary>
	// /
	// / </summary>
	// / <param name="FrameID"></param>
	// / <param name="AT_Command"></param>
	// / <param name="Parameter_Value">this can be null</param>
	public CreateSourceRouteRequest(int frameID, Address remoteAddress,
			int[] addresses)

	{
		super(12 + (addresses.length << 2), API_IDENTIFIER.Create_Source_Route,
				frameID);
		this.SetContent(remoteAddress.GetAddressValue());
		this.SetContent((byte) 0x00);
		this.SetAddresses(addresses);
	}

	public void SetRemoteAddress(Address remoteAddress) {
		this.SetContent(2, remoteAddress.GetAddressValue());
	}

	public void SetAddresses(int[] addresses) {
		this.SetPosition(13);
		this.SetContent((byte) addresses.length);
		for (int value : addresses) {
			this.SetContent((byte) (value >> 8));
			this.SetContent((byte) value);
		}
	}
}