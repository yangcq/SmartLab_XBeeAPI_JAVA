package uk.ac.herts.SmartLab.XBee.Indicator;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.Device.Address;
import uk.ac.herts.SmartLab.XBee.Status.ReceiveStatus;

public class ZigBeeRouteRecordIndicator extends RxBase {
	public ZigBeeRouteRecordIndicator(APIFrame frame) {
		super(frame);
	}

	public Address GetRemoteDevice() {
		byte[] cache = new byte[10];
		System.arraycopy(this.GetFrameData(), 1, cache, 0, 10);
		return new Address(cache);
	}

	public ReceiveStatus GetReceiveStatus() {
		return ReceiveStatus.parse(this.GetFrameData()[11]);
	}

	public int GetNumberOfAddresses() {
		return this.GetFrameData()[12];
	}

	public int[] GetAddresses() {
		int[] records = new int[GetNumberOfAddresses()];

		for (int i = 0; i < records.length; i++)
			records[i] = this.GetFrameData()[13 + (i << 2)] << 8
					| this.GetFrameData()[13 + (i << 2) + 1];

		return records;
	}
}