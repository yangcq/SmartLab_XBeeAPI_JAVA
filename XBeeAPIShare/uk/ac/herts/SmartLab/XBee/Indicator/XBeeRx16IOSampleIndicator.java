package uk.ac.herts.SmartLab.XBee.Indicator;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.IOSamples;
import uk.ac.herts.SmartLab.XBee.Device.Address;
import uk.ac.herts.SmartLab.XBee.Status.ReceiveStatus;

public class XBeeRx16IOSampleIndicator extends RxIOSampleBase {
	public XBeeRx16IOSampleIndicator(APIFrame frame) {
		super(frame);
	}

	@Override
	public int GetRSSI() {
		return GetFrameData()[3] * -1;
	}

	@Override
	public IOSamples[] GetIOSamples() {
		return RxIOSampleBase.XBeeSamplesParse(this.GetFrameData(), 5);
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