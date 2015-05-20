package uk.ac.herts.SmartLab.XBee.Indicator;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.Device.Address;
import uk.ac.herts.SmartLab.XBee.Status.ReceiveStatus;

public class ZigBeeRxIndicator extends RxPayloadBase {
	public ZigBeeRxIndicator(APIFrame frame) {
		super(frame);
	}

	@Override
	public byte[] GetReceivedData() {
		int length = this.GetReceivedDataLength();

		if (length <= 0)
			return null;

		byte[] cache = new byte[length];
		System.arraycopy(this.GetFrameData(), 12, cache, 0, length);
		return cache;
	}

	@Override
	public int GetReceivedDataOffset() {
		return 12;
	}

	@Override
	public byte GetReceivedData(int index) {
		return this.GetFrameData()[12 + index];
	}

	@Override
	public int GetReceivedDataLength() {
		return this.GetPosition() - 12;
	}

	@Override
	public ReceiveStatus GetReceiveStatus() {
		return ReceiveStatus.parse(this.GetFrameData()[11] & 0xFF);
	}

	@Override
	public Address GetRemoteDevice() {
		byte[] cache = new byte[10];
		System.arraycopy(this.GetFrameData(), 1, cache, 0, 10);
		return new Address(cache);
	}

}