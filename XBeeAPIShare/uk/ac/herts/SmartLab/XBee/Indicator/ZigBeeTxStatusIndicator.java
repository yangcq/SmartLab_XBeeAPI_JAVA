package uk.ac.herts.SmartLab.XBee.Indicator;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.Status.DeliveryStatus;
import uk.ac.herts.SmartLab.XBee.Status.ZigBeeDiscoveryStatus;

public class ZigBeeTxStatusIndicator extends TxStatusBase {
	public ZigBeeTxStatusIndicator(APIFrame frame) {
		super(frame);
	}

	@Override
	public DeliveryStatus GetDeliveryStatus() {
		return DeliveryStatus.parse(this.GetFrameData()[5] & 0xFF);
	}

	public int GetDestinationAddress16() {
		return this.GetFrameData()[2] << 8 | this.GetFrameData()[3];
	}

	public byte GetTransmitRetryCount() {
		return this.GetFrameData()[4];
	}

	public ZigBeeDiscoveryStatus GetDiscoveryStatus() {
		return ZigBeeDiscoveryStatus.parse(this.GetFrameData()[6] & 0xFF);
	}
}
