package uk.ac.herts.SmartLab.XBee.Response;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.Device.Address;
import uk.ac.herts.SmartLab.XBee.Status.ReceiveStatus;

public class XBeeRx16Response extends XBeeRxBase {
	public XBeeRx16Response(APIFrame frame) {
		super(frame);
	}

	@Override
	public ReceiveStatus GetReceiveStatus() {
		return ReceiveStatus.parse(this.GetFrameData()[4]);
	}

	@Override
	public byte[] GetReceivedData() {
		int len = this.GetReceivedDataLength();
		byte[] data = new byte[len];
		System.arraycopy(this.GetFrameData(), 5, data, 0, len);
		return data;
	}

	@Override
	public int GetReceivedDataOffset() {
		return 5;
	}

	@Override
	public Address GetRemoteDevice() {
		return new Address(new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, GetFrameData()[1], GetFrameData()[2] });
	}

	@Override
	public int GetRSSI() {
		return this.GetFrameData()[3] * -1;
	}

	@Override
	public int GetReceivedDataLength() {
		return this.GetPosition() - 5;
	}

	@Override
	public byte GetReceivedData(int index) {
		return this.GetFrameData()[5 + index];
	}
}