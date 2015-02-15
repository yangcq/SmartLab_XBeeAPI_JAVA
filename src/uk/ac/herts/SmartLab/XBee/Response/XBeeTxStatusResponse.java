package uk.ac.herts.SmartLab.XBee.Response;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.Status.DeliveryStatus;

public class XBeeTxStatusResponse extends TxStatusBase {
	public XBeeTxStatusResponse(APIFrame frame) {
		super(frame);
	}

	@Override
	public DeliveryStatus GetDeliveryStatus() {
		return DeliveryStatus.parse(this.GetFrameData()[2] & 0xFF);
	}
}