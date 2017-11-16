package com.solar;

import com.solar.client.ObservableMedia;

public class EndpotClient {
	public static void main(String[] args) {
		System.out.println("EndpotClient");
		ObservableMedia media = new ObservableMedia();
		for (int i = 0; i < 10; i++) {
			media.sendData();
		}
		System.out.println();
	}
}