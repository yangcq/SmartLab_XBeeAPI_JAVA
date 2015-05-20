package uk.ac.herts.SmartLab.XBee.Status;

public enum ZigBeeDiscoveryStatus {
	NO_DISCOVERY_OVERHEAD, // = 0x00,
	ADDRESS_DISCOVERY, // = 0x01,
	ROUTE_DISCOVERY, // = 0x02,
	ADDRESS_AND_ROUTE, // = 0x03,
	EXTENED_TIMEOUT_DISCOVERY, // = 0x40,
	ADDRESS_AND_EXTENED_TIMEOUT_DISCOVERY, // = 0x41,
	ROUTE_AND_EXTENED_TIMEOUT_DISCOVERY, // = 0x42,
	ADDRESS_ROUTE_AND_EXTENED_TIMEOUT_DISCOVERY;// = 0x43,

	public static ZigBeeDiscoveryStatus parse(int value) {
		switch (value) {
		case 0x00:
			return NO_DISCOVERY_OVERHEAD;
		case 0x01:
			return ADDRESS_DISCOVERY;
		case 0x02:
			return ROUTE_DISCOVERY;
		case 0x03:
			return ADDRESS_AND_ROUTE;
		case 0x40:
			return EXTENED_TIMEOUT_DISCOVERY;
		case 0x41:
			return ADDRESS_AND_EXTENED_TIMEOUT_DISCOVERY;
		case 0x42:
			return ROUTE_AND_EXTENED_TIMEOUT_DISCOVERY;
		case 0x43:
			return ADDRESS_ROUTE_AND_EXTENED_TIMEOUT_DISCOVERY;
		}

		return null;
	}
}
