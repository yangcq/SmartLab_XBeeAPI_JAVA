package uk.ac.herts.SmartLab.XBee.Response;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.IOSamples;
import uk.ac.herts.SmartLab.XBee.Device.Address;
import uk.ac.herts.SmartLab.XBee.Status.ReceiveStatus;

public class XBeeIODataSampleRx64Response extends XBeeIODataSampleRxBase {
	public XBeeIODataSampleRx64Response(APIFrame frame) {
		super(frame);
	}

	@Override
	public int GetRSSI() {
		return this.GetFrameData()[9] * -1;
	}

	@Override
	public IOSamples GetIOSamples() {
		return SamplesParse(this.GetFrameData(), 11);
	}

	@Override
	public ReceiveStatus GetReceiveStatus() {
		return ReceiveStatus.parse(this.GetFrameData()[10]);
	}

	@Override
	public Address GetRemoteDevice() {
		return new Address(new byte[] { GetFrameData()[1], GetFrameData()[2],
				GetFrameData()[3], GetFrameData()[4], GetFrameData()[5],
				GetFrameData()[6], GetFrameData()[7], GetFrameData()[8], 0x00,
				0x00 });
	}

	@Override
	public byte[] GetReceivedData() {
		return null;
	}

	@Override
	public int GetReceivedDataOffset() {
		return -1;
	}

	@Override
	public int GetReceivedDataLength() {
		return 0;
	}

	@Override
	public byte GetReceivedData(int index) {
		return 0;
	}
}