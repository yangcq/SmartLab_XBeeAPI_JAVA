package uk.ac.herts.SmartLab.XBee.Request;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.Type.API_IDENTIFIER;

public abstract class TxBase extends APIFrame {
	public TxBase(int Length, API_IDENTIFIER identifier, int frameID) {
		super(Length + 2);
		this.SetFrameType(identifier);
		this.SetFrameID((byte) frameID);
		this.SetPosition(2);
	}

	// / <summary>
	// / this does not affect the position
	// / </summary>
	// / <param name="identifier"></param>
	public void SetFrameID(int frameID) {
		this.SetContent(1, (byte) frameID);
	}

	public byte GetFrameID() {
		return this.GetFrameData()[1];
	}
}