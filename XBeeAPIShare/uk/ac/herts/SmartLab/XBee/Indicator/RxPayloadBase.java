package uk.ac.herts.SmartLab.XBee.Indicator;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.Device.Address;
import uk.ac.herts.SmartLab.XBee.Status.ReceiveStatus;

public abstract class RxPayloadBase extends RxBase {
	public RxPayloadBase(APIFrame frame) {
		super(frame);
	}

	public abstract ReceiveStatus GetReceiveStatus();

	public abstract Address GetRemoteDevice();

	public abstract byte[] GetReceivedData();

	public abstract int GetReceivedDataOffset();

	public abstract byte GetReceivedData(int index);

	public abstract int GetReceivedDataLength();

	// / <summary>
	// / not apply to ZigBee
	// / </summary>
	// / <returns></returns>
	public int GetRSSI() {
		return 0;
	}
}