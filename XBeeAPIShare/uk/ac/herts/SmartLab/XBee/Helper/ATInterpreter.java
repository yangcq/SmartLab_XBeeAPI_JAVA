package uk.ac.herts.SmartLab.XBee.Helper;

import uk.ac.herts.SmartLab.XBee.IOSamples;
import uk.ac.herts.SmartLab.XBee.Device.Address;
import uk.ac.herts.SmartLab.XBee.Device.XBeeDiscoverAddress;
import uk.ac.herts.SmartLab.XBee.Device.ZigBeeDiscoverAddress;
import uk.ac.herts.SmartLab.XBee.Indicator.CommandIndicatorBase;
import uk.ac.herts.SmartLab.XBee.Indicator.RxIOSampleBase;
import uk.ac.herts.SmartLab.XBee.Status.CommandStatus;

public class ATInterpreter {
	// / <summary>
	// / Get node discovery result form a ND command.
	// / </summary>
	// / <param name="indicator"></param>
	// / <returns></returns>
	public static Address FromND(CommandIndicatorBase indicator) {
		return Address.Parse(indicator);
	}

	public static XBeeDiscoverAddress FromXBeeND(CommandIndicatorBase indicator) {
		return XBeeDiscoverAddress.Parse(indicator);
	}

	public static ZigBeeDiscoverAddress FromZigBeeND(
			CommandIndicatorBase indicator) {
		return ZigBeeDiscoverAddress.Parse(indicator);
	}

	public static IOSamples[] FromXBeeIS(CommandIndicatorBase indicator) {
		if (indicator == null)
			return null;

		if (indicator.GetCommandStatus() != CommandStatus.OK)
			return null;

		if (!indicator.GetRequestCommand().toString().equalsIgnoreCase("IS"))
			return null;

		if (indicator.GetParameterLength() <= 0)
			return null;

		return RxIOSampleBase.XBeeSamplesParse(indicator.GetFrameData(),
				indicator.GetParameterOffset());
	}

	// / <summary>
	// / Parse remote AT command "IS" into IO sample details.
	// / </summary>
	// / <param name="indicator"></param>
	// / <returns></returns>
	public static IOSamples[] FromZigBeeIS(CommandIndicatorBase indicator) {
		if (indicator == null)
			return null;

		if (indicator.GetCommandStatus() != CommandStatus.OK)
			return null;

		if (!indicator.GetRequestCommand().toString().equalsIgnoreCase("IS"))
			return null;

		if (indicator.GetParameterLength() <= 0)
			return null;

		return RxIOSampleBase.ZigBeeSamplesParse(indicator.GetFrameData(),
				indicator.GetParameterOffset());
	}

	public static ActiveScanResult FromAS(CommandIndicatorBase indicator) {
		if (indicator == null)
			return null;

		if (indicator.GetCommandStatus() != CommandStatus.OK)
			return null;

		if (!indicator.GetRequestCommand().toString().equalsIgnoreCase("AS"))
			return null;

		if (indicator.GetParameterLength() <= 0)
			return null;

		ActiveScanResult result = new ActiveScanResult();

		int index = indicator.GetParameterOffset();
		byte[] data = indicator.GetFrameData();

		result.AS_Type = data[index];
		result.Channel = data[index + 1];

		result.PanID = new byte[] { data[index + 2], data[index + 3] };
		result.ExtendedPanID = new byte[] { data[index + 4], data[index + 5],
				data[index + 6], data[index + 7], data[index + 8],
				data[index + 8], data[index + 10], data[index + 11] };

		result.AllowJoin = (data[index + 12] == 0x00 ? false : true);

		result.StackProfile = data[index + 13];
		result.LQI = data[index + 14];
		result.RSSI = data[index + 15];

		return result;
	}
}