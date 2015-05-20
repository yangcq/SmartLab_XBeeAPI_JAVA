package uk.ac.herts.SmartLab.XBee;

import java.io.IOException;
import java.util.ArrayList;

import uk.ac.herts.SmartLab.XBee.Device.*;
import uk.ac.herts.SmartLab.XBee.Helper.ATInterpreter;
import uk.ac.herts.SmartLab.XBee.Indicator.*;
import uk.ac.herts.SmartLab.XBee.Options.*;
import uk.ac.herts.SmartLab.XBee.Status.*;
import uk.ac.herts.SmartLab.XBee.Type.*;

public class Core {
	private static final byte KEY = 0x7E;
	private static final byte ESCAPED = 0x7D;
	private static final byte XON = 0x11;
	private static final byte XOFF = 0x13;
	private static final int INITIAL_FRAME_LENGTH = 100;

	private ISerial serial;
	private APIMode mode;

	private APIFrame request;
	private APIFrame response;
	private boolean isSignal = false;
	private byte waitFrameID;
	private API_IDENTIFIER waitFrameType;
	private static final int DEFAULT_WAIT = 10000;

	private boolean isRunning = false;
	private boolean isChecksum = true;

	private ArrayList<XBeeAPIResponseListener> listeners = new ArrayList<XBeeAPIResponseListener>();

	public Core(ISerial serial, APIMode mode) {
		this.serial = serial;
		this.mode = mode;
		this.response = new APIFrame(INITIAL_FRAME_LENGTH);
		this.request = new APIFrame(INITIAL_FRAME_LENGTH);
	}

	// #region General Function

	// / <summary>
	// / get or set whether to verify receive packet's checksum
	// / </summary>
	public void SetVerifyChecksum(boolean state) {
		this.isChecksum = state;
	}

	public boolean GetVerifyChecksum() {
		return this.isChecksum;
	}

	public void AddResponseListener(XBeeAPIResponseListener listener) {
		this.listeners.add(listener);
	}

	public void RemoveResponseListener(XBeeAPIResponseListener listener) {
		this.listeners.remove(listener);
	}

	// / <summary>
	// / to start send and process response, must be called before any function
	// / </summary>
	public void Start() throws Exception {
		if (!isRunning) {
			if (!serial.IsOpen())
				this.serial.Open();

			new Thread(readingThread).start();
			isRunning = true;
		}
	}

	// / <summary>
	// / stop so the serial port can be used for other purpose
	// / </summary>
	public void Stop() {
		if (isRunning) {
			isRunning = false;
			serial.Close();
		}
	}

	// / <summary>
	// / a general function to send frame out, do not process response
	// / </summary>
	// / <param name="request"></param>
	public void Send(APIFrame request) {
		if (!isRunning)
			return;

		synchronized (serial) {
			request.CalculateChecksum();

			byte msb = (byte) (request.GetPosition() >> 8);
			byte lsb = (byte) request.GetPosition();

			try {
				serial.WriteByte(KEY);

				WriteByte(msb);
				WriteByte(lsb);

				for (int i = 0; i < request.GetPosition(); i++)
					WriteByte(request.GetFrameData()[i]);

				WriteByte((byte)request.GetCheckSum());
			} catch (Exception e) {
			}
		}
	}

	// #endregion

	// #region Advance Function

	public XBeeTxStatusIndicator SendXBeeTx16(Address remoteAddress,
			OptionsBase option, byte[] payload) {
		return SendXBeeTx16(remoteAddress, option, payload, 0, payload.length);
	}

	public XBeeTxStatusIndicator SendXBeeTx16(Address remoteAddress,
			OptionsBase option, byte[] payload, int offset, int length) {
		waitFrameID++;
		if (waitFrameID == 0)
			waitFrameID = 0x01;

		waitFrameType = API_IDENTIFIER.XBee_Transmit_Status;

		request.Rewind();
		request.SetContent((byte) API_IDENTIFIER.Tx16_Request.getValue());
		request.SetContent(waitFrameID);
		request.SetContent((byte) (remoteAddress.GetNetworkAddress() >> 8));
		request.SetContent((byte) remoteAddress.GetNetworkAddress());
		request.SetContent(option.GetValue());
		request.SetContent(payload, offset, length);

		isSignal = true;

		Send(request);

		synchronized (Core.this) {
			try {
				Core.this.wait(DEFAULT_WAIT);
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
		}

		if (isSignal) {
			isSignal = false;
			return null;
		}

		return new XBeeTxStatusIndicator(request);
	}

	public XBeeTxStatusIndicator SendXBeeTx64(Address remoteAddress,
			OptionsBase option, byte[] payload) {
		return SendXBeeTx64(remoteAddress, option, payload, 0, payload.length);
	}

	public XBeeTxStatusIndicator SendXBeeTx64(Address remoteAddress,
			OptionsBase option, byte[] payload, int offset, int length) {
		waitFrameID++;
		if (waitFrameID == 0)
			waitFrameID = 0x01;

		waitFrameType = API_IDENTIFIER.XBee_Transmit_Status;

		request.Rewind();
		request.SetContent((byte) API_IDENTIFIER.Tx64_Request.getValue());
		request.SetContent(waitFrameID);
		request.SetContent(remoteAddress.GetAddressValue(), 0, 8);
		request.SetContent(option.GetValue());
		request.SetContent(payload, offset, length);

		isSignal = true;

		Send(request);

		synchronized (Core.this) {
			try {
				Core.this.wait(DEFAULT_WAIT);
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
		}

		if (isSignal) {
			isSignal = false;
			return null;
		}

		return new XBeeTxStatusIndicator(request);
	}

	public ATCommandIndicator SendATCommand(ATCommand command,
			boolean applyChange, byte[] parameter) {
		if (parameter == null)
			return SendATCommand(command, applyChange, parameter, 0, 0);
		else
			return SendATCommand(command, applyChange, parameter, 0,
					parameter.length);
	}

	public ATCommandIndicator SendATCommand(ATCommand command,
			boolean applyChange, byte[] parameter, int offset, int length) {
		waitFrameID++;
		if (waitFrameID == 0)
			waitFrameID = 0x01;

		waitFrameType = API_IDENTIFIER.AT_Command_Response;

		request.Rewind();
		if (applyChange)
			request.SetContent((byte) API_IDENTIFIER.AT_Command.getValue());
		else
			request.SetContent((byte) API_IDENTIFIER.AT_Command_Queue_Parameter_Value
					.getValue());
		request.SetContent(waitFrameID);
		request.SetContent(command.GetValue());
		if (parameter != null)
			request.SetContent(parameter, offset, length);

		isSignal = true;

		Send(request);

		synchronized (Core.this) {
			try {
				Core.this.wait(DEFAULT_WAIT);
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
		}

		if (isSignal) {
			isSignal = false;
			return null;
		}

		return new ATCommandIndicator(request);
	}

	public RemoteCommandIndicator SendRemoteATCommand(Address remoteAddress,
			ATCommand command, OptionsBase transmitOptions, byte[] parameter) {
		if (parameter == null)
			return SendRemoteATCommand(remoteAddress, command, transmitOptions,
					parameter, 0, 0);
		else
			return SendRemoteATCommand(remoteAddress, command, transmitOptions,
					parameter, 0, parameter.length);
	}

	public RemoteCommandIndicator SendRemoteATCommand(Address remoteAddress,
			ATCommand command, OptionsBase transmitOptions, byte[] parameter,
			int parameterOffset, int parameterLength) {
		waitFrameID++;
		if (waitFrameID == 0)
			waitFrameID = 0x01;

		waitFrameType = API_IDENTIFIER.Remote_Command_Response;

		request.Rewind();
		request.SetContent((byte) API_IDENTIFIER.Remote_Command_Request
				.getValue());
		request.SetContent(waitFrameID);

		request.SetContent(remoteAddress.GetAddressValue());
		request.SetContent(transmitOptions.GetValue());
		request.SetContent(command.GetValue());

		if (parameter != null)
			request.SetContent(parameter, parameterOffset, parameterLength);

		isSignal = true;

		Send(request);

		synchronized (Core.this) {
			try {
				Core.this.wait(DEFAULT_WAIT);
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
		}

		if (isSignal) {
			isSignal = false;
			return null;
		}

		return new RemoteCommandIndicator(request);
	}

	public ZigBeeTxStatusIndicator SendZigBeeTx(Address remoteAddress,
			OptionsBase option, byte[] payload) {
		return SendZigBeeTx(remoteAddress, option, payload, 0, payload.length);
	}

	public ZigBeeTxStatusIndicator SendZigBeeTx(Address remoteAddress,
			OptionsBase option, byte[] payload, int offset, int length) {
		waitFrameID++;
		if (waitFrameID == 0)
			waitFrameID = 0x01;

		waitFrameType = API_IDENTIFIER.ZigBee_Transmit_Status;

		request.Rewind();
		request.SetContent((byte) API_IDENTIFIER.ZigBee_Transmit_Request
				.getValue());
		request.SetContent(waitFrameID);
		request.SetContent(remoteAddress.GetAddressValue());
		request.SetContent((byte) 0x00);
		request.SetContent(option.GetValue());
		request.SetContent(payload, offset, length);

		isSignal = true;

		Send(request);

		synchronized (Core.this) {
			try {
				Core.this.wait(DEFAULT_WAIT);
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
		}

		if (isSignal) {
			isSignal = false;
			return null;
		}

		return new ZigBeeTxStatusIndicator(request);
	}

	public ZigBeeTxStatusIndicator SendZigBeeExplicitTx(
			ExplicitAddress remoteAddress, OptionsBase option, byte[] payload) {
		return SendZigBeeExplicitTx(remoteAddress, option, payload, 0,
				payload.length);
	}

	public ZigBeeTxStatusIndicator SendZigBeeExplicitTx(
			ExplicitAddress remoteAddress, OptionsBase option, byte[] payload,
			int offset, int length) {
		waitFrameID++;
		if (waitFrameID == 0)
			waitFrameID = 0x01;

		waitFrameType = API_IDENTIFIER.ZigBee_Transmit_Status;

		request.Rewind();
		request.SetContent((byte) API_IDENTIFIER.Explicit_Addressing_ZigBee_Command_Frame
				.getValue());
		request.SetContent(waitFrameID);
		request.SetContent(remoteAddress.GetAddressValue());
		request.SetContent(remoteAddress.GetExplicitValue());
		request.SetContent((byte) 0x00);
		request.SetContent(option.GetValue());
		request.SetContent(payload, offset, length);

		isSignal = true;

		Send(request);

		synchronized (Core.this) {
			try {
				Core.this.wait(DEFAULT_WAIT);
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
		}

		if (isSignal) {
			isSignal = false;
			return null;
		}

		return new ZigBeeTxStatusIndicator(request);
	}

	public ATCommandIndicator SetPinFunction(Pin pin, Pin.Functions function) {
		return SendATCommand(new ATCommand(pin.getCommand()), true,
				new byte[] { (byte) function.getValue() });
	}

	public ATCommandIndicator SetIODetection(Pin[] pins) {
		return SendATCommand(ATCommand.Digital_IO_Change_Detection, true,
				Pin.IOChangeDetectionConfiguration(pins));
	}

	public RemoteCommandIndicator SetRemotePinFunction(Address remoteAddress,
			Pin pin, Pin.Functions function) {
		return SendRemoteATCommand(remoteAddress,
				new ATCommand(pin.getCommand()),
				RemoteCommandOptions.ApplyChanges,
				new byte[] { (byte) function.getValue() });
	}

	public RemoteCommandIndicator SetRemoteIODetection(Address remoteAddress,
			Pin[] pins) {
		return SendRemoteATCommand(remoteAddress,
				ATCommand.Digital_IO_Change_Detection,
				RemoteCommandOptions.ApplyChanges,
				Pin.IOChangeDetectionConfiguration(pins));
	}

	// #region IO Sample

	// / <summary>
	// / The command will immediately return an "OK" response. The data will
	// follow in the normal API format for DIO data event.
	// / </summary>
	// / <returns>true if the command is "OK", false if no IO is
	// enabled.</returns>
	public boolean ForceXBeeLocalIOSample() {
		ATCommandIndicator re = SendATCommand(ATCommand.Force_Sample, true,
				null);

		if (re == null)
			return false;

		if (re.GetCommandStatus() != CommandStatus.OK)
			return false;

		return true;
	}

	// / <summary>
	// / Return 1 IO sample from the local module.
	// / </summary>
	// / <returns></returns>
	public IOSamples ForceZigBeeLocalIOSample() {
		ATCommandIndicator re = SendATCommand(ATCommand.Force_Sample, true,
				null);

		if (re == null)
			return null;

		IOSamples[] array = ATInterpreter.FromZigBeeIS(re);

		if (array != null && array.length > 0)
			return array[0];

		return null;
	}

	// / <summary>
	// / Return 1 IO sample only, Samples before TX (IT) does not affect.
	// / </summary>
	// / <param name="remote"Remote address of the device></param>
	// / <returns></returns>
	public IOSamples ForceXBeeRemoteIOSample(Address remote) {
		RemoteCommandIndicator re = SendRemoteATCommand(remote,
				ATCommand.Force_Sample, OptionsBase.DEFAULT, null);

		IOSamples[] array = ATInterpreter.FromXBeeIS(re);

		if (array != null && array.length > 0)
			return array[0];

		return null;
	}

	// / <summary>
	// / Return 1 IO sample only.
	// / </summary>
	// / <param name="remote">Remote address of the device</param>
	// / <returns></returns>
	public IOSamples ForceZigBeeRemoteIOSample(Address remote) {
		RemoteCommandIndicator re = SendRemoteATCommand(remote,
				ATCommand.Force_Sample, OptionsBase.DEFAULT, null);

		IOSamples[] array = ATInterpreter.FromZigBeeIS(re);

		if (array != null && array.length > 0)
			return array[0];

		return null;
	}

	// #endregion

	// #endregion

	// #region Packet Process

	private void PacketProcess() {
		if (isChecksum) {
			if (!response.VerifyChecksum()) {
				for (XBeeAPIResponseListener listener : listeners)
					listener.onChecksumErrorIndicator(response);
				return;
			}
		}

		if (isSignal && response.GetFrameData()[1] == request.GetFrameData()[1]
				&& response.GetFrameType() == waitFrameType) {
			isSignal = false;
			request.Rewind();
			request.SetContent(response.GetFrameData(), 0,
					response.GetPosition());
			synchronized (Core.this) {
				Core.this.notify();
			}
			return;
		}

		switch (response.GetFrameType()) {
		case Rx64_Receive_Packet:
			for (XBeeAPIResponseListener listener : listeners)
				listener.onXBeeRx64Indicator(new XBeeRx64Indicator(response));
			break;
		case Rx16_Receive_Packet:
			for (XBeeAPIResponseListener listener : listeners)
				listener.onXBeeRx16Indicator(new XBeeRx16Indicator(response));
			break;
		case Rx64_IO_Data_Sample_Rx_Indicator:
			for (XBeeAPIResponseListener listener : listeners)
				listener.onXBeeIODataSampleRx64Indicator(new XBeeRx64IOSampleIndicator(
						response));
			break;
		case Rx16_IO_Data_Sample_Rx_Indicator:
			for (XBeeAPIResponseListener listener : listeners)
				listener.onXBeeIODataSampleRx16Indicator(new XBeeRx16IOSampleIndicator(
						response));
			break;
		case XBee_Transmit_Status:
			for (XBeeAPIResponseListener listener : listeners)
				listener.onXBeeTransmitStatusIndicator(new XBeeTxStatusIndicator(
						response));
			break;
		case AT_Command_Response:
			for (XBeeAPIResponseListener listener : listeners)
				listener.onATCommandIndicator(new ATCommandIndicator(response));
			break;
		case Modem_Status:
			for (XBeeAPIResponseListener listener : listeners)
				listener.onModemStatusIndicator(new ModemStatusIndicator(
						response));
			break;
		case ZigBee_Transmit_Status:
			for (XBeeAPIResponseListener listener : listeners)
				listener.onZigBeeTransmitStatusIndicator(new ZigBeeTxStatusIndicator(
						response));
			break;
		case ZigBee_Receive_Packet:
			for (XBeeAPIResponseListener listener : listeners)
				listener.onZigBeeReceivePacketIndicator(new ZigBeeRxIndicator(
						response));
			break;
		case ZigBee_Explicit_Rx_Indicator:
			for (XBeeAPIResponseListener listener : listeners)
				listener.onZigBeeExplicitRxIndicator(new ZigBeeExplicitRxIndicator(
						response));
			break;
		case ZigBee_IO_Data_Sample_Rx_Indicator:
			for (XBeeAPIResponseListener listener : listeners)
				listener.onZigBeeIODataSampleRXIndicator(new ZigBeeIOSampleIndicator(
						response));
			break;
		case XBee_Sensor_Read_Indicato:
			for (XBeeAPIResponseListener listener : listeners)
				listener.onXBeeSensorReadIndicator(new SensorReadIndicator(
						response));
			break;
		case Node_Identification_Indicator:
			for (XBeeAPIResponseListener listener : listeners)
				listener.onNodeIdentificationIndicator(new NodeIdentificationIndicator(
						response));
			break;
		case Remote_Command_Response:
			for (XBeeAPIResponseListener listener : listeners)
				listener.onRemoteCommandIndicator(new RemoteCommandIndicator(
						response));
			break;
		case Route_Record_Indicator:
			for (XBeeAPIResponseListener listener : listeners)
				listener.onRouteRecordIndicator(new ZigBeeRouteRecordIndicator(
						response));
			break;
		case Many_to_One_Route_Request_Indicator:
			for (XBeeAPIResponseListener listener : listeners)
				listener.onManyToOneRequestIndicator(new ZigBeeManyToOneIndicator(
						response));
			break;
		case Create_Source_Route:
			break;
		default:
			for (XBeeAPIResponseListener listener : listeners)
				listener.onUndefinedPacketIndicator(response);
		}
	}

	private Runnable readingThread = new Runnable() {
		@Override
		public void run() {
			while (isRunning) {
				try {
					if (ReadByte() != KEY)
						continue;

					int length = getLength();

					response.Allocate(length);

					readPayLoad(length);

					PacketProcess();
				} catch (Exception e) {
					break;
				}
			}
		}
	};

	private int getLength() throws IOException {
		int msb = ReadByte();

		int lsb = ReadByte();

		return (msb << 8) | lsb;
	}

	private void readPayLoad(int length) throws IOException {
		for (int i = 0; i < length; i++)
			response.SetContent((byte) ReadByte());

		response.SetCheckSum((byte) ReadByte());
	}

	// #endregion

	// #region IO additional layer to handle ESCAPE automatically
	// / <summary>
	// / read one byte payload, which allready handle the escape char, if less
	// than 0 means error occured
	// / </summary>
	// / <returns></returns>
	private int ReadByte() throws IOException {
		int value = serial.ReadByte();

		if (mode == APIMode.ESCAPED && value == ESCAPED)
			return serial.ReadByte() ^ 0x20;

		return value;
	}

	// / <summary>
	// / write one byte to the payload, which allready handle the escape char
	// / </summary>
	// / <param name="data"></param>
	private void WriteByte(byte data) throws IOException {
		if (mode == APIMode.ESCAPED) {
			if (data == KEY || data == ESCAPED || data == XON || data == XOFF) {
				serial.WriteByte(ESCAPED);
				serial.WriteByte((byte) (data ^ 0x20));
				return;
			}
		}

		serial.WriteByte(data);
	}
	// #endregion
}