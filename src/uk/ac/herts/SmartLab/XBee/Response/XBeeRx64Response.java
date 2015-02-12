package uk.ac.herts.SmartLab.XBee.Response;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.Device.Address;
import uk.ac.herts.SmartLab.XBee.Status.ReceiveStatus;

public class XBeeRx64Response extends XBeeRxBase {
	public XBeeRx64Response(APIFrame frame) {
		super(frame);
	}

	@Override
	public ReceiveStatus GetReceiveStatus() {
		return ReceiveStatus.parse(this.GetFrameData()[10]);
	}

	@Override
	public int GetReceivedDataOffset() {
		return 11;
	}

	@Override
	public byte[] GetReceivedData() {
		int len = this.GetReceivedDataLength();
		byte[] data = new byte[len];
		System.arraycopy(this.GetFrameData(), 11, data, 0, len);
		return data;
	}

	@Override
	public Address GetRemoteDevice() {
		return new Address(new byte[] { GetFrameData()[1], GetFrameData()[2],
				GetFrameData()[3], GetFrameData()[4], GetFrameData()[5],
				GetFrameData()[6], GetFrameData()[7], GetFrameData()[8], 0x00,
				0x00 });
	}

	@Override
	public int GetRSSI() {
		return this.GetFrameData()[9] * -1;
	}

	@Override
	public int GetReceivedDataLength() {
		return this.GetPosition() - 11;
	}

	@Override
	public byte GetReceivedData(int index) {
		return this.GetFrameData()[11 + index];
	}
}