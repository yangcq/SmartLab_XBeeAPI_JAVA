package uk.ac.herts.SmartLab.XBee.Indicator;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.Device.Address;
import uk.ac.herts.SmartLab.XBee.Status.CommandStatus;
import uk.ac.herts.SmartLab.XBee.Type.ATCommand;

public class RemoteCommandIndicator extends CommandIndicatorBase {
	public RemoteCommandIndicator(APIFrame frame) {
		super(frame);
	}

	@Override
	public ATCommand GetRequestCommand() {
		return new ATCommand(new byte[] { this.GetFrameData()[12],
				this.GetFrameData()[13] });
	}

	@Override
	public CommandStatus GetCommandStatus() {
		return CommandStatus.parse(this.GetFrameData()[14] & 0xFF);
	}

	public Address GetRemoteDevice() {
		byte[] cache = new byte[10];
		System.arraycopy(this.GetFrameData(), 2, cache, 0, 10);
		return new Address(cache);
	}

	@Override
	public byte[] GetParameter() {
		int length = this.GetParameterLength();

		if (length <= 0)
			return null;

		byte[] cache = new byte[length];
		System.arraycopy(this.GetFrameData(), 15, cache, 0, length);
		return cache;
	}

	@Override
	public byte GetParameter(int index) {
		return this.GetFrameData()[15 + index];
	}

	@Override
	public int GetParameterLength() {
		return this.GetPosition() - 15;
	}

	@Override
	public int GetParameterOffset() {
		return 15;
	}
}