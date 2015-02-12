package uk.ac.herts.SmartLab.XBee.Request;

import uk.ac.herts.SmartLab.XBee.Device.Address;
import uk.ac.herts.SmartLab.XBee.Options.OptionsBase;
import uk.ac.herts.SmartLab.XBee.Type.API_IDENTIFIER;

public abstract class TxRequestBase extends RequestBase implements ITx {
	public TxRequestBase(int length, API_IDENTIFIER identifier, int frameID) {
		super(length, identifier, frameID);
	}

	public abstract void SetPayload(byte[] data);

	public abstract void SetPayload(byte[] data, int offset, int length);

	public abstract void SetTransmitOptions(OptionsBase transmitOptions);

	public abstract void SetRemoteAddress(Address remoteAddress);
}