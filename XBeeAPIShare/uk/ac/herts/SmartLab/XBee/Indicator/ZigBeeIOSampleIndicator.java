package uk.ac.herts.SmartLab.XBee.Indicator;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.IOSamples;
import uk.ac.herts.SmartLab.XBee.Device.Address;
import uk.ac.herts.SmartLab.XBee.Status.ReceiveStatus;

public class ZigBeeIOSampleIndicator extends RxIOSampleBase {
	public ZigBeeIOSampleIndicator(APIFrame frame) {
		super(frame);
	}

	@Override
	public Address GetRemoteDevice() {
		byte[] cache = new byte[10];
		System.arraycopy(this.GetFrameData(), 1, cache, 0, 10);
		return new Address(cache);
	}

	@Override
	public ReceiveStatus GetReceiveStatus() {
		return ReceiveStatus.parse(this.GetFrameData()[11] & 0xFF);
	}

	@Override
	public IOSamples[] GetIOSamples() {
		return RxIOSampleBase.ZigBeeSamplesParse(this.GetFrameData(), 12);
	}
}