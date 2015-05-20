package uk.ac.herts.SmartLab.XBee.Type;

public enum API_IDENTIFIER {
	Tx64_Request(0x00), Tx16_Request(0x01), AT_Command(0x08), AT_Command_Queue_Parameter_Value(
			0x09), ZigBee_Transmit_Request(0x10), Explicit_Addressing_ZigBee_Command_Frame(
			0x11), Remote_Command_Request(0x17), Create_Source_Route(0x21), Register_Joining_Device(
			0x24), Rx64_Receive_Packet(0x80), Rx16_Receive_Packet(0x81), Rx64_IO_Data_Sample_Rx_Indicator(
			0x82), Rx16_IO_Data_Sample_Rx_Indicator(0x83), AT_Command_Response(
			0x88), XBee_Transmit_Status(0x89), Modem_Status(0x8A), ZigBee_Transmit_Status(
			0x8B), ZigBee_Receive_Packet(0x90), ZigBee_Explicit_Rx_Indicator(
			0x91), ZigBee_IO_Data_Sample_Rx_Indicator(0x92), XBee_Sensor_Read_Indicato(
			0x94), Node_Identification_Indicator(0x95), Remote_Command_Response(
			0x97), Over_the_Air_Firmware_Update_Status(0xA0), Route_Record_Indicator(
			0xA1), Device_Authenticated_Indicator(0xA2), Many_to_One_Route_Request_Indicator(
			0xA3);

	private int value;

	API_IDENTIFIER(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

	public static API_IDENTIFIER parse(int value) {
		switch (value) {
		case 0x00:
			return Tx64_Request;
		case 0x01:
			return Tx16_Request;
		case 0x08:
			return AT_Command;
		case 0x09:
			return AT_Command_Queue_Parameter_Value;
		case 0x10:
			return ZigBee_Transmit_Request;
		case 0x11:
			return Explicit_Addressing_ZigBee_Command_Frame;
		case 0x17:
			return Remote_Command_Request;
		case 0x21:
			return Create_Source_Route;
		case 0x24:
			return Register_Joining_Device;
		case 0x80:
			return Rx64_Receive_Packet;
		case 0x81:
			return Rx16_Receive_Packet;
		case 0x82:
			return Rx64_IO_Data_Sample_Rx_Indicator;
		case 0x83:
			return Rx16_IO_Data_Sample_Rx_Indicator;
		case 0x88:
			return AT_Command_Response;
		case 0x89:
			return XBee_Transmit_Status;
		case 0x8A:
			return Modem_Status;
		case 0x8B:
			return ZigBee_Transmit_Status;
		case 0x90:
			return ZigBee_Receive_Packet;
		case 0x91:
			return ZigBee_Explicit_Rx_Indicator;
		case 0x92:
			return ZigBee_IO_Data_Sample_Rx_Indicator;
		case 0x94:
			return XBee_Sensor_Read_Indicato;
		case 0x95:
			return Node_Identification_Indicator;
		case 0x97:
			return Remote_Command_Response;
		case 0xA0:
			return Over_the_Air_Firmware_Update_Status;
		case 0xA1:
			return Route_Record_Indicator;
		case 0xA2:
			return Device_Authenticated_Indicator;
		case 0xA3:
			return Many_to_One_Route_Request_Indicator;
		}

		return null;
	}
}