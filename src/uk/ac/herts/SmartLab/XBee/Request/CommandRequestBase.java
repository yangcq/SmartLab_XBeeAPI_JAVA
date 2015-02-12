package uk.ac.herts.SmartLab.XBee.Request;

import uk.ac.herts.SmartLab.XBee.Type.API_IDENTIFIER;
import uk.ac.herts.SmartLab.XBee.Type.ATCommand;

public abstract class CommandRequestBase extends RequestBase {
	public CommandRequestBase(int length, API_IDENTIFIER identifier,
			int FrameID) {
		super(length, identifier, FrameID);
	}

	public abstract void SetAppleChanges(boolean appleChanges);

	public abstract void SetCommand(ATCommand command);

	public abstract void SetParameter(byte[] parameter);

	public abstract void SetParameter(byte[] parameter, int offset, int length);
}
