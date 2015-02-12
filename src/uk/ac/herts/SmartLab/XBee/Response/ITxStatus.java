package uk.ac.herts.SmartLab.XBee.Response;

import uk.ac.herts.SmartLab.XBee.Status.DeliveryStatus;

public interface ITxStatus {
	public int GetFrameID();

	public DeliveryStatus GetDeliveryStatus();
}
