package uk.ac.herts.SmartLab.XBee.Response;

import uk.ac.herts.SmartLab.XBee.APIFrame;

public abstract class ZigBeeRxBase extends RxResponseBase {
	public ZigBeeRxBase(APIFrame frame) {
		super(frame);
	}
}
