package uk.ac.herts.SmartLab.XBee.Indicator;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.Status.CommandStatus;
import uk.ac.herts.SmartLab.XBee.Type.ATCommand;

public abstract class CommandIndicatorBase extends RxBase {
	public CommandIndicatorBase(APIFrame frame) {
		super(frame);
	}

	public int GetFrameID() {
		return GetFrameData()[1];
	}

	public abstract ATCommand GetRequestCommand();

	public abstract CommandStatus GetCommandStatus();

	public abstract byte[] GetParameter();

	public abstract byte GetParameter(int index);

	public abstract int GetParameterLength();

	public abstract int GetParameterOffset();
}