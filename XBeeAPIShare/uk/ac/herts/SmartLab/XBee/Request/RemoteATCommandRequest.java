package uk.ac.herts.SmartLab.XBee.Request;

import uk.ac.herts.SmartLab.XBee.Device.Address;
import uk.ac.herts.SmartLab.XBee.Options.OptionsBase;
import uk.ac.herts.SmartLab.XBee.Type.API_IDENTIFIER;
import uk.ac.herts.SmartLab.XBee.Type.ATCommand;

public class RemoteATCommandRequest extends CommandRequestBase {
	// 0x17
	// FrameID;
	// RemoteDevice
	// Remote Command Options
	// AT_Command
	// Parameter_Value

	public RemoteATCommandRequest(int frameID, Address remoteAddress,
			ATCommand command, OptionsBase transmitOptions, byte[] parameter) {
		this(frameID, remoteAddress, command, transmitOptions, parameter, 0,
				parameter == null ? 0 : parameter.length);
	}

	// / <summary>
	// /
	// / </summary>
	// / <param name="FrameID"></param>
	// / <param name="RemoteDevice"></param>
	// / <param name="options">RemoteCommandOptions</param>
	// / <param name="AT_Command"></param>
	// / <param name="Parameter_Value">this can be null</param>
	public RemoteATCommandRequest(int frameID, Address remoteAddress,
			ATCommand command, OptionsBase transmitOptions, byte[] parameter,
			int parameterOffset, int parameterLength) {
		super(13 + (parameter == null ? 0 : parameter.length),
				API_IDENTIFIER.Remote_Command_Request, frameID);
		this.SetContent(remoteAddress.GetAddressValue());
		this.SetContent(transmitOptions.GetValue());
		this.SetContent(command.GetValue());

		if (parameter != null)
			this.SetContent(parameter, parameterOffset, parameterLength);
	}

	public void SetTransmitOptions(OptionsBase TransmitOptions) {
		this.SetContent(12, TransmitOptions.GetValue());
	}

	@Override
	public void SetAppleChanges(boolean appleChanges) {
		if (appleChanges)
			this.GetFrameData()[12] |= 0x02;
		else
			this.GetFrameData()[12] &= 0xFD;
	}

	@Override
	public void SetCommand(ATCommand command) {
		this.SetContent(13, command.GetValue());
	}

	@Override
	public void SetParameter(byte[] parameter) {
		this.SetParameter(parameter, 0, parameter.length);
	}

	@Override
	public void SetParameter(byte[] parameter, int offset, int length) {
		this.SetPosition(15);
		this.SetContent(parameter, offset, length);
	}

	public void SetRemoteAddress(Address remoteAddress) {
		this.SetContent(2, remoteAddress.GetAddressValue());
	}
}