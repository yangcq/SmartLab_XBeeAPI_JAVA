package uk.ac.herts.SmartLab.XBee.Indicator;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.Status.DeliveryStatus;

public abstract class TxStatusBase extends RxBase {
	public TxStatusBase(APIFrame frame) {
		super(frame);
	}

	public int GetFrameID() {
		return this.GetFrameData()[1] & 0xFF;
	}

	public abstract DeliveryStatus GetDeliveryStatus();
}