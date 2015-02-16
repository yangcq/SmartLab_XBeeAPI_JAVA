package uk.ac.herts.SmartLab.XBee;

import java.util.HashMap;

import uk.ac.herts.SmartLab.XBee.Device.Pin;
import uk.ac.herts.SmartLab.XBee.Device.Pin.Status;

public class IOSamples {
	private int SUPPLY_VOLTAGE;
	private HashMap<Pin, Integer> analog;
	private HashMap<Pin, Status> digital;

	// / <summary>
	// / SUPPLY_VOLTAGE not required by XBee
	// / </summary>
	// / <param name="analog"></param>
	// / <param name="digital"></param>
	// / <param name="SUPPLY_VOLTAGE"></param>
	public IOSamples(HashMap<Pin, Integer> analog,
			HashMap<Pin, Status> digital, int SUPPLY_VOLTAGE) {
		this.analog = analog;
		this.digital = digital;
		this.SUPPLY_VOLTAGE = SUPPLY_VOLTAGE;
	}

	public HashMap<Pin, Integer> GetAnalogs() {
		return this.analog;
	}

	public int GetAnalog(Pin pin) {
		if (this.analog.containsKey(pin))
			return (int) analog.get(pin);
		return 0;
	}

	public Status GetDigital(Pin pin) {
		if (this.digital.containsKey(pin))
			return (Status) digital.get(pin);
		return null;
	}

	public HashMap<Pin, Status> GetDigitals() {
		return this.digital;
	}

	// / <summary>
	// / only avaliable in ZigBee device and when it is battery powered
	// / </summary>
	// / <returns></returns>
	public int GetSupplyVoltage() {
		return this.SUPPLY_VOLTAGE;
	}
}