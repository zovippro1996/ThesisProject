package com.example.thesis.booktrading.gnutellaprotocol;

public class ChatHit extends Packet {
    public ChatHit(byte payload, int length) {
        super(payload, length);
    }

    public ChatHit(byte[] rawdata) {
        super(rawdata);
    }
}
