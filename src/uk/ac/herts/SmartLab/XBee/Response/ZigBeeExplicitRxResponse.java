package uk.ac.herts.SmartLab.XBee.Response;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.Device.ExplicitAddress;
import uk.ac.herts.SmartLab.XBee.Status.ReceiveStatus;

public class ZigBeeExplicitRxResponse extends ZigBeeRxBase {
	public ZigBeeExplicitRxResponse(APIFrame frame) {
		super(frame);
	}

	@Override
	public ExplicitAddress GetRemoteDevice() {
		byte[] address = new byte[10];
		byte[] explicit = new byte[6];
		System.arraycopy(this.GetFrameData(), 1, address, 0, 10);
		System.arraycopy(this.GetFrameData(), 11, explicit, 0, 6);

		return new ExplicitAddress(address, explicit);
	}

	@Override
	public ReceiveStatus GetReceiveStatus() {
		return ReceiveStatus.parse(this.GetFrameData()[17]);
	}

	@Override
	public byte[] GetReceivedData() {
		int len = this.GetReceivedDataLength();
		byte[] data = new byte[len];
		System.arraycopy(this.GetFrameData(), 18, data, 0, len);
		return data;
	}

	@Override
	public int GetReceivedDataOffset() {
		return 18;
	}

	@Override
	public int GetReceivedDataLength() {
		// TODO Auto-generated method stub
		return this.GetPosition() - 18;
	}

	@Override
	public byte GetReceivedData(int index) {
		// TODO Auto-generated method stub
		return this.GetFrameData()[18 + index];
	}
}