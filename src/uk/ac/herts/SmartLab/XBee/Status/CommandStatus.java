package uk.ac.herts.SmartLab.XBee.Status;

public enum CommandStatus {
	OK, // = 0x00,
	ERROR, // = 0x01,
	INVALID_COMMAND, // = 0x02,
	INVALID_Parameter, // = 0x03,
	TRANSMISSION_FAILED;// = 0x04,

	public static CommandStatus parse(int value) {
		switch (value) {
		case 0x00:
			return OK;
		case 0x01:
			return ERROR;
		case 0x02:
			return INVALID_COMMAND;
		case 0x03:
			return INVALID_Parameter;
		case 0x04:
			return TRANSMISSION_FAILED;
		}

		return null;
	}
}
