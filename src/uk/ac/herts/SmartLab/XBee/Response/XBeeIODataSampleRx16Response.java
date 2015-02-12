package uk.ac.herts.SmartLab.XBee.Response;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.IOSamples;
import uk.ac.herts.SmartLab.XBee.Device.Address;
import uk.ac.herts.SmartLab.XBee.Status.ReceiveStatus;

public class XBeeIODataSampleRx16Response extends XBeeIODataSampleRxBase {
	public XBeeIODataSampleRx16Response(APIFrame frame) {
		super(frame);
	}

	@Override
	public int GetRSSI() {
		return GetFrameData()[3] * -1;
	}

	@Override
	public int GetReceivedDataLength() {
		return 0;
	}

	@Override
	public byte GetReceivedData(int index) {
		return 0;
	}

	@Override
	public IOSamples GetIOSamples() {
		return SamplesParse(this.GetFrameData(), 5);
	}

	@Override
	public ReceiveStatus GetReceiveStatus() {
		return ReceiveStatus.parse(this.GetFrameData()[4]);
	}

	@Override
	public Address GetRemoteDevice() {
		return new Address(new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, GetFrameData()[1], GetFrameData()[2] });
	}

	@Override
	public byte[] GetReceivedData() {
		return null;
	}

	@Override
	public int GetReceivedDataOffset() {
		return -1;
	}
}