package uk.ac.herts.SmartLab.XBee.Request;

import uk.ac.herts.SmartLab.XBee.Options.OptionsBase;

    public interface ITx
    {
        public void SetTransmitOptions(OptionsBase transmitOptions);

        public void SetPayload(byte[] data);
    }