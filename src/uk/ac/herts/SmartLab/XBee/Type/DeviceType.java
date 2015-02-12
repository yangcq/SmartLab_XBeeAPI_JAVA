package uk.ac.herts.SmartLab.XBee.Type;

public enum DeviceType {
	COORDINATOR, // = 0x00,
	ROUTER, // = 0x01,
	END_DEVICE;// = 0x02;

	public static DeviceType parse(int value) {
		switch (value) {
		case 0x00:
			return COORDINATOR;
		case 0x01:
			return ROUTER;
		case 0x02:
			return END_DEVICE;
		}

		return null;
	}
}
