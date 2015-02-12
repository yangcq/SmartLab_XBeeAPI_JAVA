package uk.ac.herts.SmartLab.XBee.Response;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.Device.Address;
import uk.ac.herts.SmartLab.XBee.Status.ReceiveStatus;

public class ZigBeeRxResponse extends ZigBeeRxBase {
	public ZigBeeRxResponse(APIFrame frame) {
		super(frame);
	}

	@Override
	public ReceiveStatus GetReceiveStatus() {
		return ReceiveStatus.parse(this.GetFrameData()[11]);
	}

	@Override
	public byte[] GetReceivedData() {
		int len = this.GetReceivedDataLength();
		byte[] data = new byte[len];
		System.arraycopy(this.GetFrameData(), 12, data, 0, len);
		return data;
	}

	@Override
	public Address GetRemoteDevice() {
		byte[] data = new byte[10];
		System.arraycopy(this.GetFrameData(), 1, data, 0, 10);
		return new Address(data);
	}

	@Override
	public int GetReceivedDataOffset() {
		return 12;
	}

	@Override
	public int GetReceivedDataLength() {
		// TODO Auto-generated method stub
		return this.GetPosition() - 12;
	}

	@Override
	public byte GetReceivedData(int index) {
		// TODO Auto-generated method stub
		return this.GetFrameData()[12 + index];
	}
}