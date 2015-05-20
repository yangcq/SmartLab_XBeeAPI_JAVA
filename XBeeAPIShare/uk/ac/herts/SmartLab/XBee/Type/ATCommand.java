package uk.ac.herts.SmartLab.XBee.Type;

public class ATCommand {
	public static final ATCommand IO_Sampling_Rate = new ATCommand(new byte[] {
			0x49, 0x52 });

	// / <summary>
	// / Set/Read bitfield values for change detect monitoring.
	// / <para>
	// / XBee : Each bit enables monitoring of DIO0 - DIO7 for changes.If
	// detected, data is transmitted with DIO data only. Any samples queued
	// waiting for transmission will be sent first.
	// / </para>
	// / <para>
	// / ZigBee : IC works with the individual pin configuration commands
	// (D0-D8, P0-P2). If a pin is enabled as a digital input/output, the IC
	// command can be used to force an immediate IO sample transmission when the
	// DIO state changes. IC is a bitmask that can be used to enable or disable
	// edge detection on individual channels. Unused bits should be set to 0.
	// / Bit (IO pin):
	// / 0 (DIO0 4 (DIO4) 8 -- 12 (CD/DIC12)
	// / 1 (DIO1) 5 (DIO5) 9 --
	// / 2 (DIO2) 6 (DIO6) 10 (DIO10)
	// / 3 (DIO3) 7 (DIO7) 11 (DIO11)</para>
	// /</summary>
	public static final ATCommand Digital_IO_Change_Detection = new ATCommand(
			new byte[] { 0x49, 0x43 });

	// / <summary>
	// / IS, Force Sample. Forces a read of all enabled digital and analog input
	// lines.
	// / Always read 1 sample, not affect by the Samples_Before_TX (IT) command.
	// / If no inputs are defined (DI or ADC), this command will return error.
	// / </summary>
	public static final ATCommand Force_Sample = new ATCommand(new byte[] {
			0x49, 0x53 });

	// / <summary>
	// / IT, Samples before TX (XBee only, ZigBee always 1). Sets/reads the
	// number of samples to collect before transmitting data. If IT is too
	// large, then it sends the maximum number of samples that will fit in a
	// single frame. No more than 44 samples can fit in a single frame.
	// / </summary>
	public static final ATCommand Samples_Before_TX = new ATCommand(new byte[] {
			0x49, 0x54 });

	public static final ATCommand Node_Descovery = new ATCommand(new byte[] {
			0x4E, 0x44 });

	public static final ATCommand Destination_Node_Descovery = new ATCommand(
			new byte[] { 0x44, 0x4E });

	public static final ATCommand Node_Identifier = new ATCommand(new byte[] {
			0x4E, 0x49 });

	public static final ATCommand Node_Join_Time = new ATCommand(new byte[] {
			0x4E, 0x4A });

	public static final ATCommand Network_Address = new ATCommand(new byte[] {
			0x4D, 0x59 });

	public static final ATCommand PAN_ID = new ATCommand(new byte[] { 0x49,
			0x44 });

	public static final ATCommand Destination_Address_High = new ATCommand(
			new byte[] { 0x44, 0x48 });

	public static final ATCommand Destination_Address_Low = new ATCommand(
			new byte[] { 0x44, 0x4C });

	public static final ATCommand Serial_Number_High = new ATCommand(
			new byte[] { 0x53, 0x48 });

	public static final ATCommand Serial_Number_Low = new ATCommand(new byte[] {
			0x53, 0x4C });

	protected byte[] value;

	public ATCommand() {
	}

	public ATCommand(byte[] commad) {
		this.value = commad;
	}

	public ATCommand(String commad) {
		this.value = commad.getBytes();
	}

	public byte[] GetValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return new String(this.value);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		ATCommand command = null;

		if (obj instanceof ATCommand)
			command = (ATCommand) obj;

		if (command == null)
			return false;

		return this.value[0] == command.value[0]
				&& this.value[1] == command.value[1];
	}

	public boolean equals(ATCommand command) {
		if (command == null)
			return false;

		return this.value[0] == command.value[0]
				&& this.value[1] == command.value[1];
	}
}
