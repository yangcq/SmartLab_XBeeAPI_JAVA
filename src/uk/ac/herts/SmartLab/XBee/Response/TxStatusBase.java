package uk.ac.herts.SmartLab.XBee.Response;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.Status.DeliveryStatus;

public abstract class TxStatusBase extends RxBase {
	public TxStatusBase(APIFrame frame) {
		super(frame);
	}

	public int GetFrameID() {
		return this.GetFrameData()[1];
	}

	public abstract DeliveryStatus GetDeliveryStatus();
}
