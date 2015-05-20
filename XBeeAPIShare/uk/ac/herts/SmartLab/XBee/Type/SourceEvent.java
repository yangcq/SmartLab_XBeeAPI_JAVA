package uk.ac.herts.SmartLab.XBee.Type;

public enum SourceEvent {
	FRAME_SENT_BY_NODE_IDENTIFICATION_PUSHBUTTON_EVENT, // = 0x01,
	FRAME_SENT_AFTER_JOINING_EVENT_OCCURRED, // = 0x02,
	FRAME_SENT_AFTER_POWER_CYCLE_EVENT_OCCURRED;// = 0x03,

	public static SourceEvent parse(int value) {
		switch (value) {
		case 0x01:
			return FRAME_SENT_BY_NODE_IDENTIFICATION_PUSHBUTTON_EVENT;
		case 0x02:
			return FRAME_SENT_AFTER_JOINING_EVENT_OCCURRED;
		case 0x03:
			return FRAME_SENT_AFTER_POWER_CYCLE_EVENT_OCCURRED;
		}

		return null;
	}
}
