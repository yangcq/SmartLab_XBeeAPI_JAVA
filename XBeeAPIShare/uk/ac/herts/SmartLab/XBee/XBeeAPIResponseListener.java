package uk.ac.herts.SmartLab.XBee;

import uk.ac.herts.SmartLab.XBee.Indicator.*;

public interface XBeeAPIResponseListener {
	public void onChecksumErrorIndicator(APIFrame indicator);

	public void onUndefinedPacketIndicator(APIFrame indicator);

	public void onATCommandIndicator(ATCommandIndicator indicator);

	public void onModemStatusIndicator(ModemStatusIndicator indicator);

	public void onNodeIdentificationIndicator(NodeIdentificationIndicator indicator);

	public void onRemoteCommandIndicator(RemoteCommandIndicator indicator);

	public void onXBeeIODataSampleRx16Indicator(XBeeRx16IOSampleIndicator indicator);

	public void onXBeeIODataSampleRx64Indicator(XBeeRx64IOSampleIndicator indicator);

	public void onXBeeRx16Indicator(XBeeRx16Indicator indicator);

	public void onXBeeRx64Indicator(XBeeRx64Indicator indicator);

	public void onXBeeSensorReadIndicator(SensorReadIndicator indicator);

	public void onXBeeTransmitStatusIndicator(XBeeTxStatusIndicator indicator);

	public void onZigBeeExplicitRxIndicator(ZigBeeExplicitRxIndicator indicator);

	public void onZigBeeIODataSampleRXIndicator(ZigBeeIOSampleIndicator indicator);

	public void onZigBeeReceivePacketIndicator(ZigBeeRxIndicator indicator);

	public void onZigBeeTransmitStatusIndicator(ZigBeeTxStatusIndicator indicator);

	public void onManyToOneRequestIndicator(ZigBeeManyToOneIndicator indicator);

	public void onRouteRecordIndicator(ZigBeeRouteRecordIndicator indicator);
}
