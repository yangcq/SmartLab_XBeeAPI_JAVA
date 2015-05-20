package uk.ac.herts.SmartLab.XBee.Indicator;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.Device.Address;
import uk.ac.herts.SmartLab.XBee.Status.ReceiveStatus;

public class XBeeRx16Indicator extends RxPayloadBase {
	public XBeeRx16Indicator(APIFrame frame) {
		super(frame);
	}

	@Override
	public byte[] GetReceivedData() {
		int length = this.GetReceivedDataLength();

		if (length <= 0)
			return null;

		byte[] cache = new byte[length];
		System.arraycopy(this.GetFrameData(), 5, cache, 0, length);
		return cache;
	}

	@Override
	public int GetReceivedDataOffset() {
		return 5;
	}

	@Override
	public byte GetReceivedData(int index) {
		return this.GetFrameData()[5 + index];
	}

	@Override
	public int GetReceivedDataLength() {
		return this.GetPosition() - 5;
	}

	@Override
	public int GetRSSI() {
		return this.GetFrameData()[3] * -1;
	}

	@Override
	public ReceiveStatus GetReceiveStatus() {
		return ReceiveStatus.parse(this.GetFrameData()[4] & 0xFF);
	}

	@Override
	public Address GetRemoteDevice() {
		return new Address(new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, GetFrameData()[1], GetFrameData()[2] });
	}
}