package uk.ac.herts.SmartLab.XBee.Type;

public enum OneWireSensor {
	AD_SENSOR_READ, // = 0x01,
	TEMPERATURE_SENSOR_READ, // = 0x02,
	HUMIDITY_SENSOR_READ, // = 0x03,
	WATER_PRESENT;// = 0x60,

	public static OneWireSensor parse(int value) {
		switch (value) {
		case 0x01:
			return AD_SENSOR_READ;
		case 0x02:
			return TEMPERATURE_SENSOR_READ;
		case 0x03:
			return HUMIDITY_SENSOR_READ;
		case 0x60:
			return WATER_PRESENT;
		}

		return null;
	}
}
