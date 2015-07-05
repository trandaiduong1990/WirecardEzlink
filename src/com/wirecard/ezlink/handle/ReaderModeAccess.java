package com.wirecard.ezlink.handle;

import java.io.IOException;

import android.nfc.tech.IsoDep;
import android.util.Log;

public class ReaderModeAccess {
	private IsoDep isoDep;
	public String terminalRN;
	public String purseRequest;
	
	public ReaderModeAccess(IsoDep isoDep) {
		this.isoDep = isoDep;
	}
	
	public byte[] init() {
		byte[] initByte = new byte[8];
		initByte[1] = -92;
		initByte[4] = 2;
		initByte[5] = 64;
        byte[] initRespose = null;
		try {
			Log.d("init()", "request: " + Util.hexString(initByte));
			initRespose = isoDep.transceive(initByte);
	        Log.d("init()", "response: " + Util.hexString(initRespose));
		} catch (IOException e) {
			
		}
        return initRespose;
	}
	
	public byte[] getChallenge() {
		byte[] challengeRequest = { (byte) 0x00, (byte) 0x84,
				(byte) 0x00, (byte) 0x00, (byte) 0x08 };
		byte[] challengeResponse = null;
		try {
			challengeResponse = isoDep.transceive(challengeRequest);
			Log.d("challengeResponse",Util.hexString(challengeResponse));
		} catch (IOException e) {
		}
		return challengeResponse;
	}
	
	public String getCardRN(byte[] challengeResponse) {
		String cardRN;
		cardRN = Util.hexString(challengeResponse).substring(0, 16);
		return cardRN;
	}
	
	public String getPurseData() {
		String purseData = null;
		try {
			//		String terminalRN = Util.getRandomHexString(16);
			terminalRN = "CF549C2B7520389C";
			Log.d("terminalRN", terminalRN);
			purseRequest ="903203000A1403" + terminalRN;
			Log.d("Purse Request", purseRequest);
			byte[] purseByte = Util.hexStringToByteArray(purseRequest.toString());
			byte[] purseResponse = isoDep.transceive(purseByte);
			purseData = Util.hexString(purseResponse);
			Log.d("PurseData Response", purseData);
		} catch (IOException e) {
		}
		return purseData;
	}
	
	public String getPurseData(String purseRequest) {
		byte[] purseByte = Util.hexStringToByteArray(purseRequest.toString());
		String purseData = null;
		try {
			byte[] purseResponse = isoDep.transceive(purseByte);
			purseData = Util.hexString(purseResponse);
			Log.d("PurseData Response", purseData);
		} catch (IOException e) {
		}
		return purseData;
	}
	
	public String getReceipt(String debitCommand) {
		String receiptData = null;
		try {
			debitCommand = "90340000" + debitCommand + "00";
			byte[] debitCommandByte = Util.hexStringToByteArray(debitCommand);
			byte[] receiptResponse = isoDep.transceive(debitCommandByte);
			receiptData = Util.hexString(receiptResponse);
			Log.d("ReceiptData", receiptData);
		} catch (IOException e) {
		}
		return receiptData;
	}

	public String getTerminalRN() {
		return terminalRN;
	}

	public String getPurseRequest() {
		return purseRequest;
	}

}
