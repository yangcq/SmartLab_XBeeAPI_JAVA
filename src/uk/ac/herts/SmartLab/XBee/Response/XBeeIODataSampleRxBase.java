package uk.ac.herts.SmartLab.XBee.Response;

import java.util.HashMap;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.IOSamples;
import uk.ac.herts.SmartLab.XBee.Device.Pin;
import uk.ac.herts.SmartLab.XBee.Device.Pin.Status;

public abstract class XBeeIODataSampleRxBase extends XBeeRxBase {
	public XBeeIODataSampleRxBase(APIFrame frame) {
		super(frame);
	}

	public static IOSamples SamplesParse(byte[] IOSamplePayload, int offset) {
		HashMap<Pin, Status> Digital = new HashMap<Pin, Status>();
		HashMap<Pin, Integer> Analog = new HashMap<Pin, Integer>();
		int index = 0;
		if (IOSamplePayload.length > 3) {
			if ((IOSamplePayload[offset + 1] & 0x01)
					+ IOSamplePayload[offset + 2] == 0)// digital mask
				index = 3;
			else {
				index = 5;
				if ((IOSamplePayload[offset + 2] & 0x01) == 0x01)
					Digital.put(
							Pin.XBee.AD0_DIO0,
							(IOSamplePayload[offset + 4] & 0x01) == 0x01 ? Pin.Status.HIGH
									: Pin.Status.LOW);
				if ((IOSamplePayload[offset + 2] & 0x02) == 0x02)
					Digital.put(
							Pin.XBee.AD1_DIO1,
							(IOSamplePayload[offset + 4] & 0x02) == 0x02 ? Pin.Status.HIGH
									: Pin.Status.LOW);
				if ((IOSamplePayload[offset + 2] & 0x04) == 0x04)
					Digital.put(
							Pin.XBee.AD2_DIO2,
							(IOSamplePayload[offset + 4] & 0x04) == 0x04 ? Pin.Status.HIGH
									: Pin.Status.LOW);
				if ((IOSamplePayload[offset + 2] & 0x08) == 0x08)
					Digital.put(
							Pin.XBee.AD3_DIO3,
							(IOSamplePayload[offset + 4] & 0x08) == 0x08 ? Pin.Status.HIGH
									: Pin.Status.LOW);
				if ((IOSamplePayload[offset + 2] & 0x10) == 0x10)
					Digital.put(
							Pin.XBee.AD4_DIO4,
							(IOSamplePayload[offset + 4] & 0x10) == 0x10 ? Pin.Status.HIGH
									: Pin.Status.LOW);
				if ((IOSamplePayload[offset + 2] & 0x20) == 0x20)
					Digital.put(
							Pin.XBee.ASSOCIATE_AD5_DIO5,
							(IOSamplePayload[offset + 4] & 0x20) == 0x20 ? Pin.Status.HIGH
									: Pin.Status.LOW);
				if ((IOSamplePayload[offset + 2] & 0x40) == 0x40)
					Digital.put(
							Pin.XBee.RTS_AD6_DIO6,
							(IOSamplePayload[offset + 4] & 0x40) == 0x40 ? Pin.Status.HIGH
									: Pin.Status.LOW);
				if ((IOSamplePayload[offset + 2] & 0x80) == 0x80)
					Digital.put(
							Pin.XBee.CTS_DIO7,
							(IOSamplePayload[offset + 4] & 0x80) == 0x80 ? Pin.Status.HIGH
									: Pin.Status.LOW);
				if ((IOSamplePayload[offset + 1] & 0x01) == 0x01)
					Digital.put(
							Pin.XBee.DTR_SLEEP_DIO8,
							(IOSamplePayload[offset + 3] & 0x01) == 0x01 ? Pin.Status.HIGH
									: Pin.Status.LOW);
			}
			if ((IOSamplePayload[offset + 1] & 0x02) == 0x02) {
				Analog.put(Pin.XBee.AD0_DIO0,
						IOSamplePayload[offset + index] << 8
								| IOSamplePayload[offset + index + 1]);
				index += 2;
			}
			if ((IOSamplePayload[offset + 1] & 0x04) == 0x04) {
				Analog.put(Pin.XBee.AD1_DIO1,
						IOSamplePayload[offset + index] << 8
								| IOSamplePayload[offset + index + 1]);
				index += 2;
			}
			if ((IOSamplePayload[offset + 1] & 0x08) == 0x08) {
				Analog.put(Pin.XBee.AD2_DIO2,
						IOSamplePayload[offset + index] << 8
								| IOSamplePayload[offset + index + 1]);
				index += 2;
			}
			if ((IOSamplePayload[offset + 1] & 0x10) == 0x10) {
				Analog.put(Pin.XBee.AD3_DIO3,
						IOSamplePayload[offset + index] << 8
								| IOSamplePayload[offset + index + 1]);
				index += 2;
			}
			if ((IOSamplePayload[offset + 1] & 0x20) == 0x20) {
				Analog.put(Pin.XBee.AD4_DIO4,
						IOSamplePayload[offset + index] << 8
								| IOSamplePayload[offset + index + 1]);
				index += 2;
			}
			if ((IOSamplePayload[offset + 1] & 0x40) == 0x40)
				Analog.put(Pin.XBee.ASSOCIATE_AD5_DIO5, IOSamplePayload[offset
						+ index] << 8
						| IOSamplePayload[offset + index + 1]);
		}
		return new IOSamples(Analog, Digital, 0);
	}

	public abstract IOSamples GetIOSamples();
}