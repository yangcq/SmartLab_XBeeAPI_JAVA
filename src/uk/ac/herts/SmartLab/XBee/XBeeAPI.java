package uk.ac.herts.SmartLab.XBee;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import uk.ac.herts.SmartLab.XBee.Device.*;
import uk.ac.herts.SmartLab.XBee.Options.*;
import uk.ac.herts.SmartLab.XBee.Response.*;
import uk.ac.herts.SmartLab.XBee.Type.*;

public class XBeeAPI {
	private XBeeAPIResponseListener listeners;

	private final byte KEY = 0x7E;
	private final byte ESCAPED = 0x7D;
	private final byte XON = 0x11;
	private final byte XOFF = 0x13;
	private final int INITIAL_FRAME_LENGTH = 100;

	private InputStream input;
	private OutputStream output;

	private APIFrame response;
	private APIMode mode;

	private APIFrame safeResponse;
	private APIFrame request;
	private boolean isSignal = false;
	private int waitFrameID;
	private API_IDENTIFIER waitFrameType;
	private final int DEFAULT_WAIT = 10000;

	private boolean isRunning = false;
	private boolean isChecksum = true;

	public XBeeAPI(InputStream input, OutputStream output, APIMode mode) {
		this.mode = mode;
		this.response = new APIFrame(INITIAL_FRAME_LENGTH);
		this.safeResponse = new APIFrame(INITIAL_FRAME_LENGTH);
		this.request = new APIFrame(INITIAL_FRAME_LENGTH);

		this.output = output;
		this.input = input;
	}

	// / <summary>
	// / get or set whether to verify receive packet's checksum
	// / </summary>
	public void setVerifyChecksum(boolean state) {
		this.isChecksum = state;
	}

	public boolean getVerifyChecksum() {
		return this.isChecksum;
	}

	// / <summary>
	// / to start send and process response, must be called before any function
	// / </summary>
	public void Start() {
		if (!isRunning) {
			isRunning = true;
			new Thread(readingThread).start();
		}
	}

	// / <summary>
	// / stop so the serial port can be used for other purpose
	// / </summary>
	public void Stop() {
		if (isRunning)
			isRunning = false;
	}

	// / <summary>
	// / a general function to send frame out, do not process response
	// / </summary>
	// / <param name="request"></param>
	public void Send(APIFrame request) {

		try {
			request.CalculateChecksum();

			byte msb = (byte) (request.GetPosition() >> 8);
			byte lsb = (byte) request.GetPosition();

			_WriteByte(KEY);

			WriteByte(msb);
			WriteByte(lsb);

			for (int i = 0; i < request.GetPosition(); i++)
				WriteByte(request.GetFrameData()[i]);

			WriteByte(request.GetCheckSum());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public XBeeTxStatusResponse SendXBeeTx16(Address remoteAddress,
			OptionsBase option, byte[] payload) {
		return SendXBeeTx16(remoteAddress, option, payload, 0, payload.length);
	}

	public XBeeTxStatusResponse SendXBeeTx16(Address remoteAddress,
			OptionsBase option, byte[] payload, int offset, int length) {
		waitFrameID++;
		if (waitFrameID == 0)
			waitFrameID = 0x01;

		waitFrameType = API_IDENTIFIER.XBee_Transmit_Status;

		request.Rewind();
		request.SetContent((byte) API_IDENTIFIER.Tx16_Request.getValue());
		request.SetContent((byte) waitFrameID);
		request.SetContent((byte) (remoteAddress.GetNetworkAddress() >> 8));
		request.SetContent((byte) remoteAddress.GetNetworkAddress());
		request.SetContent(option.GetValue());
		request.SetContent(payload, offset, length);

		isSignal = true;

		Send(request);

		synchronized (XBeeAPI.this) {
			try {
				XBeeAPI.this.wait(DEFAULT_WAIT);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (isSignal) {
			isSignal = false;
			return null;
		}

		return new XBeeTxStatusResponse(safeResponse);
	}

	public XBeeTxStatusResponse SendXBeeTx64(Address remoteAddress,
			OptionsBase option, byte[] payload) {
		return SendXBeeTx64(remoteAddress, option, payload, 0, payload.length);
	}

	public XBeeTxStatusResponse SendXBeeTx64(Address remoteAddress,
			OptionsBase option, byte[] payload, int offset, int length) {
		waitFrameID++;
		if (waitFrameID == 0)
			waitFrameID = 0x01;

		waitFrameType = API_IDENTIFIER.XBee_Transmit_Status;

		request.Rewind();
		request.SetContent((byte) API_IDENTIFIER.Tx64_Request.getValue());
		request.SetContent((byte) waitFrameID);
		request.SetContent(remoteAddress.GetAddressValue(), 0, 8);
		request.SetContent(option.GetValue());
		request.SetContent(payload, offset, length);

		isSignal = true;

		Send(request);

		synchronized (XBeeAPI.this) {
			try {
				XBeeAPI.this.wait(DEFAULT_WAIT);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (isSignal) {
			isSignal = false;
			return null;
		}

		return new XBeeTxStatusResponse(safeResponse);
	}

	public ATCommandResponse SendATCommand(ATCommand command,
			boolean applyChange, byte[] parameter) {
		if (parameter == null)
			return SendATCommand(command, applyChange, parameter, 0, 0);
		else
			return SendATCommand(command, applyChange, parameter, 0,
					parameter.length);
	}

	public ATCommandResponse SendATCommand(ATCommand command,
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
		request.SetContent((byte) waitFrameID);
		request.SetContent(command.GetValue());
		if (parameter != null)
			request.SetContent(parameter, offset, length);

		isSignal = true;

		Send(request);

		synchronized (XBeeAPI.this) {
			try {
				XBeeAPI.this.wait(DEFAULT_WAIT);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (isSignal) {
			isSignal = false;
			return null;
		}

		return new ATCommandResponse(safeResponse);
	}

	public RemoteCommandResponse SendRemoteATCommand(Address remoteAddress,
			ATCommand command, OptionsBase transmitOptions, byte[] parameter) {
		if (parameter == null)
			return SendRemoteATCommand(remoteAddress, command, transmitOptions,
					parameter, 0, 0);
		else
			return SendRemoteATCommand(remoteAddress, command, transmitOptions,
					parameter, 0, parameter.length);
	}

	public RemoteCommandResponse SendRemoteATCommand(Address remoteAddress,
			ATCommand command, OptionsBase transmitOptions, byte[] parameter,
			int parameterOffset, int parameterLength) {
		waitFrameID++;
		if (waitFrameID == 0)
			waitFrameID = 0x01;

		waitFrameType = API_IDENTIFIER.Remote_Command_Response;

		request.Rewind();
		request.SetContent((byte) API_IDENTIFIER.Remote_Command_Request
				.getValue());
		request.SetContent((byte) waitFrameID);

		request.SetContent(remoteAddress.GetAddressValue());
		request.SetContent(transmitOptions.GetValue());
		request.SetContent(command.GetValue());

		if (parameter != null)
			request.SetContent(parameter, parameterOffset, parameterLength);

		isSignal = true;

		Send(request);

		synchronized (XBeeAPI.this) {
			try {
				XBeeAPI.this.wait(DEFAULT_WAIT);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (isSignal) {
			isSignal = false;
			return null;
		}

		return new RemoteCommandResponse(safeResponse);
	}

	public ZigBeeTxStatusResponse SendZigBeeTx(Address remoteAddress,
			OptionsBase option, byte[] payload) {
		return SendZigBeeTx(remoteAddress, option, payload, 0, payload.length);
	}

	public ZigBeeTxStatusResponse SendZigBeeTx(Address remoteAddress,
			OptionsBase option, byte[] payload, int offset, int length) {
		waitFrameID++;
		if (waitFrameID == 0)
			waitFrameID = 0x01;

		waitFrameType = API_IDENTIFIER.ZigBee_Transmit_Status;

		request.Rewind();
		request.SetContent((byte) API_IDENTIFIER.ZigBee_Transmit_Request
				.getValue());
		request.SetContent((byte) waitFrameID);
		request.SetContent(remoteAddress.GetAddressValue());
		request.SetContent((byte) 0x00);
		request.SetContent(option.GetValue());
		request.SetContent(payload, offset, length);

		isSignal = true;

		Send(request);

		synchronized (XBeeAPI.this) {
			try {
				XBeeAPI.this.wait(DEFAULT_WAIT);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (isSignal) {
			isSignal = false;
			return null;
		}

		return new ZigBeeTxStatusResponse(safeResponse);
	}

	public ZigBeeTxStatusResponse SendZigBeeExplicitTx(
			ExplicitAddress remoteAddress, OptionsBase option, byte[] payload) {
		return SendZigBeeExplicitTx(remoteAddress, option, payload, 0,
				payload.length);
	}

	public ZigBeeTxStatusResponse SendZigBeeExplicitTx(
			ExplicitAddress remoteAddress, OptionsBase option, byte[] payload,
			int offset, int length) {
		waitFrameID++;
		if (waitFrameID == 0)
			waitFrameID = 0x01;

		waitFrameType = API_IDENTIFIER.ZigBee_Transmit_Status;

		request.Rewind();
		request.SetContent((byte) API_IDENTIFIER.Explicit_Addressing_ZigBee_Command_Frame
				.getValue());
		request.SetContent((byte) waitFrameID);
		request.SetContent(remoteAddress.GetAddressValue());
		request.SetContent(remoteAddress.GetExplicitValue());
		request.SetContent((byte) 0x00);
		request.SetContent(option.GetValue());
		request.SetContent(payload, offset, length);

		isSignal = true;

		Send(request);

		synchronized (XBeeAPI.this) {
			try {
				XBeeAPI.this.wait(DEFAULT_WAIT);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (isSignal) {
			isSignal = false;
			return null;
		}

		return new ZigBeeTxStatusResponse(safeResponse);
	}

	public ATCommandResponse SetPinFunction(Pin pin, Pin.Functions function) {
		return SendATCommand(new ATCommand(pin.getCommand()), true,
				new byte[] { (byte) function.getValue() });
	}

	public ATCommandResponse SetIODetection(Pin[] pins) {
		return SendATCommand(ATCommand.Digital_IO_Change_Detection, true,
				Pin.IOChangeDetectionConfiguration(pins));
	}

	public RemoteCommandResponse SetRemotePinFunction(Address remoteAddress,
			Pin pin, Pin.Functions function) {
		return SendRemoteATCommand(remoteAddress,
				new ATCommand(pin.getCommand()),
				RemoteCommandOptions.ApplyChanges,
				new byte[] { (byte) function.getValue() });
	}

	public RemoteCommandResponse SetRemoteIODetection(Address remoteAddress,
			Pin[] pins) {
		return SendRemoteATCommand(remoteAddress,
				ATCommand.Digital_IO_Change_Detection,
				RemoteCommandOptions.ApplyChanges,
				Pin.IOChangeDetectionConfiguration(pins));
	}

	private void PacketProcess() {
		if (isChecksum) {
			if (!response.VerifyChecksum()) {
				if (this.listeners != null)
					listeners.onChecksumError(response);
				return;
			}
		}

		if (isSignal && response.GetFrameData()[1] == request.GetFrameData()[1]
				&& response.GetFrameType() == waitFrameType) {
			isSignal = false;
			safeResponse.Rewind();
			safeResponse.SetContent(response.GetFrameData(), 0,
					response.GetPosition());

			synchronized (XBeeAPI.this) {
				XBeeAPI.this.notify();
			}

			return;
		}

		if (this.listeners == null)
			return;

		switch (response.GetFrameType()) {
		case Rx64_Receive_Packet:
			listeners.onXBeeRx64Indicator(new XBeeRx64Response(response));
			break;
		case Rx16_Receive_Packet:
			listeners.onXBeeRx16Indicator(new XBeeRx16Response(response));
			break;
		case Rx64_IO_Data_Sample_Rx_Indicator:
			listeners
					.onXBeeIODataSampleRx64Response(new XBeeIODataSampleRx64Response(
							response));
			break;
		case Rx16_IO_Data_Sample_Rx_Indicator:
			listeners
					.onXBeeIODataSampleRx16Response(new XBeeIODataSampleRx16Response(
							response));
			break;
		case XBee_Transmit_Status:
			listeners.onXBeeTransmitStatusResponse(new XBeeTxStatusResponse(
					response));
			break;
		case AT_Command_Response:
			listeners.onATCommandResponse(new ATCommandResponse(response));
			break;
		case Modem_Status:
			listeners.onModemStatusResponse(new ModemStatusResponse(response));
			break;
		case ZigBee_Transmit_Status:
			listeners
					.onZigBeeTransmitStatusResponse(new ZigBeeTxStatusResponse(
							response));
			break;
		case ZigBee_Receive_Packet:
			listeners.onZigBeeReceivePacketResponse(new ZigBeeRxResponse(
					response));
			break;
		case ZigBee_Explicit_Rx_Indicator:
			listeners.onZigBeeExplicitRxResponse(new ZigBeeExplicitRxResponse(
					response));
			break;
		case ZigBee_IO_Data_Sample_Rx_Indicator:
			listeners
					.onZigBeeIODataSampleRXResponse(new ZigBeeIODataSampleRxResponse(
							response));
			break;
		case XBee_Sensor_Read_Indicato:
			listeners
					.onXBeeSensorReadResponse(new SensorReadResponse(response));
			break;
		case Node_Identification_Indicator:
			listeners
					.onNodeIdentificationResponse(new NodeIdentificationResponse(
							response));
			break;
		case Remote_Command_Response:
			listeners.onRemoteCommandResponse(new RemoteCommandResponse(
					response));
			break;
		case Over_the_Air_Firmware_Update_Status:
			break;
		case Route_Record_Indicator:
			break;
		case Many_to_One_Route_Request_Indicator:
			break;
		default:
			listeners.onUndefinedPacket(response);
			break;
		}
	}

	private final Runnable readingThread = new Runnable() {
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

	// / <summary>
	// / if success return non zero, -1 means something is wrong
	// / </summary>
	// / <returns></returns>
	private int _ReadByte() throws IOException {
		return input.read();
	}

	// / <summary>
	// / read one byte payload, which allready handle the escape char, if less
	// than 0 means error occured
	// / </summary>
	// / <returns></returns>
	private int ReadByte() throws IOException {
		int value = _ReadByte();

		if (mode == APIMode.ESCAPED && value == ESCAPED)
			return _ReadByte() ^ 0x20;

		return value;
	}

	private void _WriteByte(byte data) throws IOException {
		output.write(data);
	}

	// / <summary>
	// / write one byte to the payload, which allready handle the escape char
	// / </summary>
	// / <param name="data"></param>
	private void WriteByte(byte data) throws IOException {
		if (mode == APIMode.ESCAPED) {
			if (data == KEY || data == ESCAPED || data == XON || data == XOFF) {
				_WriteByte(ESCAPED);
				_WriteByte((byte) (data ^ 0x20));
				return;
			}
		}

		_WriteByte(data);
	}
}