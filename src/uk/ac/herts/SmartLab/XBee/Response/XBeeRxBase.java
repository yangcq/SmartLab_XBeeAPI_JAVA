package uk.ac.herts.SmartLab.XBee.Response;

import uk.ac.herts.SmartLab.XBee.APIFrame;

public abstract class XBeeRxBase extends RxResponseBase implements IRSSI {
	public XBeeRxBase(APIFrame frame) {
		super(frame);
	}

	public abstract int GetRSSI();
}