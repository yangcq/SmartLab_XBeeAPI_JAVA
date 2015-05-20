package uk.ac.herts.SmartLab.XBee;

import java.io.IOException;

public interface ISerial {
	// / <summary>
	// / if success return non zero, -1 means something is wrong
	// / </summary>
	// / <returns></returns>
	int ReadByte() throws IOException;

	void WriteByte(byte data) throws IOException;

	// / <summary>
	// / check if the serial port is already open
	// / </summary>
	// / <returns></returns>
	boolean IsOpen();

	void Open() throws Exception;

	void Close();
}