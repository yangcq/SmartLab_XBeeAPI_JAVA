package uk.ac.herts.SmartLab.XBee.Response;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.Status.ModemStatus;

public class ModemStatusResponse extends ResponseBase {
	public ModemStatusResponse(APIFrame frame) {
		super(frame);
	}

	public ModemStatus GetModemStatus() {
		return ModemStatus.parse(this.GetFrameData()[1]);
	}
}