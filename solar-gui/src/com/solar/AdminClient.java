package com.solar;

import com.solar.client.ObservableMedia;

public class AdminClient {
	public static void main(String[] args) {
		ObservableMedia media = new ObservableMedia();
		media.sendUpdateConfig();
		System.out.println();
	}
}