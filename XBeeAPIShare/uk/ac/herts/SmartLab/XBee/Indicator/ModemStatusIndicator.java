package uk.ac.herts.SmartLab.XBee.Indicator;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.Status.ModemStatus;

public class ModemStatusIndicator extends RxBase {
	public ModemStatusIndicator(APIFrame frame) {
		super(frame);
	}

	public ModemStatus GetModemStatus() {
		return ModemStatus.parse(this.GetFrameData()[1] & 0xFF);
	}
}