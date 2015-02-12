package uk.ac.herts.SmartLab.XBee.Response;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.Device.Address;
import uk.ac.herts.SmartLab.XBee.Status.ReceiveStatus;
import uk.ac.herts.SmartLab.XBee.Type.DeviceType;
import uk.ac.herts.SmartLab.XBee.Type.SourceEvent;

public class NodeIdentificationResponse extends ZigBeeRxBase {
	private int offset = 0;

	public NodeIdentificationResponse(APIFrame frame) {
		super(frame);
		this.offset = this.GetPosition() - 8;
	}

	@Override
	public int GetReceivedDataLength() {
		return -1;
	}

	@Override
	public byte GetReceivedData(int index) {
		return 0;
	}

	@Override
	public int GetReceivedDataOffset() {
		return -1;
	}

	@Override
	public byte[] GetReceivedData() {
		return null;
	}

	@Override
	public ReceiveStatus GetReceiveStatus() {
		return ReceiveStatus.parse(this.GetFrameData()[11]);
	}

	@Override
	public Address GetRemoteDevice() {
		byte[] data = new byte[10];
		System.arraycopy(this.GetFrameData(), 14, data, 0, 8);
		data[8] = this.GetFrameData()[12];
		data[9] = this.GetFrameData()[13];
		return new Address(data);
	}

	public Address GetSenderDevice() {
		byte[] data = new byte[10];
		System.arraycopy(this.GetFrameData(), 1, data, 0, 10);
		return new Address(data);
	}

	public String GetNIString() {
		return new String(this.GetFrameData(), 22, this.GetReceivedDataLength());
	}

	public int GetParentNetworkAddress() {
		return this.GetFrameData()[offset] << 8
				| this.GetFrameData()[offset + 1];
	}

	public DeviceType GetDeviceType() {
		return DeviceType.parse(this.GetFrameData()[offset + 2]);
	}

	public SourceEvent GetSourceEvent() {
		return SourceEvent.parse(this.GetFrameData()[offset + 3]);
	}

	public int GetDigiProfileID() {
		return this.GetFrameData()[offset + 4] << 8
				| this.GetFrameData()[offset + 5];
	}

	public int GetManufacturerID() {
		return this.GetFrameData()[offset + 6] << 8
				| this.GetFrameData()[offset + 7];
	}
}