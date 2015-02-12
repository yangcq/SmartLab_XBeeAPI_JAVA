package uk.ac.herts.SmartLab.XBee.Response;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.Status.CommandStatus;
import uk.ac.herts.SmartLab.XBee.Type.ATCommand;

public class ATCommandResponse extends CommandResponseBase {
	public ATCommandResponse(APIFrame frame) {
		super(frame);
	}

	public ATCommand GetRequestCommand() {
		return new ATCommand(new byte[] { this.GetFrameData()[2],
				this.GetFrameData()[3] });
	}

	@Override
	public CommandStatus GetCommandStatus() {
		return CommandStatus.parse(this.GetFrameData()[4]);
	}

	// / <summary>
	// / if parameter not presented, null will be returned.
	// / </summary>
	// / <returns></returns>
	@Override
	public byte[] GetParameter() {
		int len = this.GetParameterLength();
		if (len > 0) {
			byte[] data = new byte[len];
			System.arraycopy(this.GetFrameData(), 5, data, 0, len);
			return data;
		} else
			return null;
	}

	@Override
	public int GetParameterOffset() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public int GetParameterLength() {
		// TODO Auto-generated method stub
		return this.GetPosition() - 5;
	}

	@Override
	public byte GetParameter(int index) {
		// TODO Auto-generated method stub
		return this.GetFrameData()[5 + index];
	}
}