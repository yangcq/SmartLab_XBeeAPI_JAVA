package uk.ac.herts.SmartLab.XBee.Indicator;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.Status.DeliveryStatus;

public class XBeeTxStatusIndicator extends TxStatusBase {
	public XBeeTxStatusIndicator(APIFrame frame) {
		super(frame);
	}

	@Override
	public DeliveryStatus GetDeliveryStatus() {
		return DeliveryStatus.parse(this.GetFrameData()[2] & 0xFF);
	}
}