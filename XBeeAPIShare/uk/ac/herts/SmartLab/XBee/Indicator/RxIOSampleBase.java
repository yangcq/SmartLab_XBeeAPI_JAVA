package uk.ac.herts.SmartLab.XBee.Indicator;

import java.util.HashMap;

import uk.ac.herts.SmartLab.XBee.APIFrame;
import uk.ac.herts.SmartLab.XBee.IOSamples;
import uk.ac.herts.SmartLab.XBee.Device.Address;
import uk.ac.herts.SmartLab.XBee.Device.Pin;
import uk.ac.herts.SmartLab.XBee.Status.ReceiveStatus;

public abstract class RxIOSampleBase extends RxBase {
	public RxIOSampleBase(APIFrame frame) {
		super(frame);
	}

	public static IOSamples[] XBeeSamplesParse(byte[] IOSamplePayload,
			int offset) {
		// at least 3 bytes, 1 byte of [number of samples] + 2 bytes of [digital
		// channel mask] and [analog channel mask].
		if (IOSamplePayload.length - offset < 3)
			return null;

		int numofsamples = IOSamplePayload[offset];

		if (numofsamples <= 0)
			return null;

		// first byte is the number of sample
		int index = offset + 1;

		IOSamples[] smaplearray = new IOSamples[numofsamples];

		int digitMask = (IOSamplePayload[index] & 0x01) << 8
				| IOSamplePayload[index + 1];
		int analogMask = IOSamplePayload[index] & 0xFE;

		// sample start at +2 [mask]
		index += 2;

		for (int i = 0; i < numofsamples; i++) {
			HashMap<Pin, Pin.Status> Digital = new HashMap<Pin, Pin.Status>();
			HashMap<Pin, Integer> Analog = new HashMap<Pin, Integer>();
			if (digitMask != 0) {
				if ((digitMask & 0x01) == 0x01)
					Digital.put(
							Pin.XBee.P20_AD0_DIO0,
							(IOSamplePayload[index + 1] & 0x01) == 0x01 ? Pin.Status.HIGH
									: Pin.Status.LOW);
				if ((digitMask & 0x02) == 0x02)
					Digital.put(
							Pin.XBee.P19_AD1_DIO1,
							(IOSamplePayload[index + 1] & 0x02) == 0x02 ? Pin.Status.HIGH
									: Pin.Status.LOW);
				if ((digitMask & 0x04) == 0x04)
					Digital.put(
							Pin.XBee.P18_AD2_DIO2,
							(IOSamplePayload[index + 1] & 0x04) == 0x04 ? Pin.Status.HIGH
									: Pin.Status.LOW);
				if ((digitMask & 0x08) == 0x08)
					Digital.put(
							Pin.XBee.P17_AD3_DIO3,
							(IOSamplePayload[index + 1] & 0x08) == 0x08 ? Pin.Status.HIGH
									: Pin.Status.LOW);
				if ((digitMask & 0x10) == 0x10)
					Digital.put(
							Pin.XBee.P11_AD4_DIO4,
							(IOSamplePayload[index + 1] & 0x10) == 0x10 ? Pin.Status.HIGH
									: Pin.Status.LOW);
				if ((digitMask & 0x20) == 0x20)
					Digital.put(
							Pin.XBee.P15_ASSOCIATE_AD5_DIO5,
							(IOSamplePayload[index + 1] & 0x20) == 0x20 ? Pin.Status.HIGH
									: Pin.Status.LOW);
				if ((digitMask & 0x40) == 0x40)
					Digital.put(
							Pin.XBee.P16_RTS_AD6_DIO6,
							(IOSamplePayload[index + 1] & 0x40) == 0x40 ? Pin.Status.HIGH
									: Pin.Status.LOW);
				if ((digitMask & 0x80) == 0x80)
					Digital.put(
							Pin.XBee.P12_CTS_DIO7,
							(IOSamplePayload[index + 1] & 0x80) == 0x80 ? Pin.Status.HIGH
									: Pin.Status.LOW);
				if (((digitMask >> 8) & 0x01) == 0x01)
					Digital.put(
							Pin.XBee.P9_DTR_SLEEP_DIO8,
							(IOSamplePayload[index] & 0x01) == 0x01 ? Pin.Status.HIGH
									: Pin.Status.LOW);

				// skip the 2 [digital sample]
				index += 2;
			}

			if (analogMask != 0) {
				if ((analogMask & 0x02) == 0x02)
					Analog.put(Pin.XBee.P20_AD0_DIO0, new Integer(
							(IOSamplePayload[index++] & 0xFF) << 8
									| (IOSamplePayload[index++] & 0xFF)));

				if ((analogMask & 0x04) == 0x04)
					Analog.put(Pin.XBee.P19_AD1_DIO1, new Integer(
							(IOSamplePayload[index++] & 0xFF) << 8
									| (IOSamplePayload[index++] & 0xFF)));

				if ((analogMask & 0x08) == 0x08)
					Analog.put(Pin.XBee.P18_AD2_DIO2, new Integer(
							(IOSamplePayload[index++] & 0xFF) << 8
									| (IOSamplePayload[index++] & 0xFF)));

				if ((analogMask & 0x10) == 0x10)
					Analog.put(Pin.XBee.P17_AD3_DIO3, new Integer(
							(IOSamplePayload[index++] & 0xFF) << 8
									| (IOSamplePayload[index++] & 0xFF)));

				if ((analogMask & 0x20) == 0x20)
					Analog.put(Pin.XBee.P11_AD4_DIO4, new Integer(
							(IOSamplePayload[index++] & 0xFF) << 8
									| (IOSamplePayload[index++] & 0xFF)));

				if ((analogMask & 0x40) == 0x40)
					Analog.put(Pin.XBee.P15_ASSOCIATE_AD5_DIO5, new Integer(
							(IOSamplePayload[index++] & 0xFF) << 8
									| (IOSamplePayload[index++] & 0xFF)));
			}

			smaplearray[i] = new IOSamples(Analog, Digital, 0);
		}

		return smaplearray;
	}

	// / <summary>
	// / Parse byte array into IO sample details.
	// / </summary>
	// / <param name="IOSamplePayload">Source data frame</param>
	// / <param name="offset">The begin index of the source data (the first byte
	// should be the number of sample)</param>
	// / <returns></returns>
	public static IOSamples[] ZigBeeSamplesParse(byte[] IOSamplePayload,
			int offset) {
		// at least 4 bytes, 1 byte of [number of samples] + 2 bytes of [digital
		// channel mask] + 1 bytes of [analog channel mask].
		if (IOSamplePayload.length - offset < 4)
			return null;

		// the [number of samples] always set to 1.
		int numofsamples = IOSamplePayload[offset];

		if (numofsamples <= 0)
			return null;

		int index = offset + 1;

		int digitMask = IOSamplePayload[index++] << 8
				| IOSamplePayload[index++];
		int analogMask = IOSamplePayload[index++];

		IOSamples[] smaplearray = new IOSamples[numofsamples];

		for (int i = 0; i < numofsamples; i++) {
			HashMap<Pin, Pin.Status> Digital = new HashMap<Pin, Pin.Status>();
			HashMap<Pin, Integer> Analog = new HashMap<Pin, Integer>();
			int SUPPLY_VOLTAGE = 0;

			if (digitMask != 0) {
				if ((digitMask & 0x01) == 0x01)
					Digital.put(
							Pin.ZigBee.P20_AD0_DIO0_COMMISSIONONG_BUTTON,
							(IOSamplePayload[index + 1] & 0x01) == 0x01 ? Pin.Status.HIGH
									: Pin.Status.LOW);
				if ((digitMask & 0x02) == 0x02)
					Digital.put(
							Pin.ZigBee.P19_AD1_DIO1,
							(IOSamplePayload[index + 1] & 0x02) == 0x02 ? Pin.Status.HIGH
									: Pin.Status.LOW);
				if ((digitMask & 0x04) == 0x04)
					Digital.put(
							Pin.ZigBee.P18_AD2_DIO2,
							(IOSamplePayload[index + 1] & 0x04) == 0x04 ? Pin.Status.HIGH
									: Pin.Status.LOW);
				if ((digitMask & 0x08) == 0x08)
					Digital.put(
							Pin.ZigBee.P17_AD3_DIO3,
							(IOSamplePayload[index + 1] & 0x08) == 0x08 ? Pin.Status.HIGH
									: Pin.Status.LOW);
				if ((digitMask & 0x10) == 0x10)
					Digital.put(
							Pin.ZigBee.P11_DIO4,
							(IOSamplePayload[index + 1] & 0x10) == 0x10 ? Pin.Status.HIGH
									: Pin.Status.LOW);
				if ((digitMask & 0x20) == 0x20)
					Digital.put(
							Pin.ZigBee.P15_ASSOCIATE_DIO5,
							(IOSamplePayload[index + 1] & 0x20) == 0x20 ? Pin.Status.HIGH
									: Pin.Status.LOW);
				if ((digitMask & 0x40) == 0x40)
					Digital.put(
							Pin.ZigBee.P16_RTS_DIO6,
							(IOSamplePayload[index + 1] & 0x40) == 0x40 ? Pin.Status.HIGH
									: Pin.Status.LOW);
				if ((digitMask & 0x80) == 0x80)
					Digital.put(
							Pin.ZigBee.P12_CTS_DIO7,
							(IOSamplePayload[index + 1] & 0x80) == 0x80 ? Pin.Status.HIGH
									: Pin.Status.LOW);

				if (((digitMask >> 8) & 0x04) == 0x04)
					Digital.put(
							Pin.ZigBee.P6_RSSI_PWM_DIO10,
							(IOSamplePayload[index] & 0x04) == 0x04 ? Pin.Status.HIGH
									: Pin.Status.LOW);
				if (((digitMask >> 8) & 0x08) == 0x08)
					Digital.put(
							Pin.ZigBee.P7_PWM_DIO11,
							(IOSamplePayload[index] & 0x08) == 0x08 ? Pin.Status.HIGH
									: Pin.Status.LOW);
				if (((digitMask >> 8) & 0x10) == 0x10)
					Digital.put(
							Pin.ZigBee.P4_DIO12,
							(IOSamplePayload[index] & 0x10) == 0x10 ? Pin.Status.HIGH
									: Pin.Status.LOW);

				index += 2;// 2 [digital sample]
			}
			if (analogMask != 0x00)// analog mask
			{
				if ((analogMask & 0x01) == 0x01)
					Analog.put(Pin.ZigBee.P20_AD0_DIO0_COMMISSIONONG_BUTTON,
							new Integer((IOSamplePayload[index++] & 0xFF) << 8
									| (IOSamplePayload[index++] & 0xFF)));

				if ((analogMask & 0x02) == 0x02)
					Analog.put(Pin.ZigBee.P19_AD1_DIO1, new Integer(
							(IOSamplePayload[index++] & 0xFF) << 8
									| (IOSamplePayload[index++] & 0xFF)));

				if ((analogMask & 0x04) == 0x04)
					Analog.put(Pin.ZigBee.P18_AD2_DIO2, new Integer(
							(IOSamplePayload[index++] & 0xFF) << 8
									| (IOSamplePayload[index++] & 0xFF)));

				if ((analogMask & 0x08) == 0x08)
					Analog.put(Pin.ZigBee.P17_AD3_DIO3, new Integer(
							(IOSamplePayload[index++] & 0xFF) << 8
									| (IOSamplePayload[index++] & 0xFF)));

				if ((analogMask & 0x80) == 0x80)
					SUPPLY_VOLTAGE = (IOSamplePayload[index++] & 0xFF) << 8
							| (IOSamplePayload[index++] & 0xFF);
			}
			smaplearray[i] = new IOSamples(Analog, Digital, SUPPLY_VOLTAGE);
		}

		return smaplearray;
	}

	public abstract IOSamples[] GetIOSamples();

	public abstract ReceiveStatus GetReceiveStatus();

	public abstract Address GetRemoteDevice();

	// / <summary>
	// / not apply to ZigBee
	// / </summary>
	// / <returns></returns>
	public int GetRSSI() {
		return 0;
	}
}