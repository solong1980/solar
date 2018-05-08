package com.solar.test.press;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GenDevNo {

	public static void main(String[] args) {
		char[] as = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		// FEC722EC
		char[] a = new char["FEC722EC".length()];
		Set<String> set = new HashSet<>();
		for (int j = 0; j < 10000; j++) {
			for (int i = 0; i < "FEC722EC".length(); i++) {
				Random random = new Random();
				a[i] = as[random.nextInt(as.length - 1)];
			}
			StringBuilder stringBuilder = new StringBuilder(a.length);
			for (int i = 0; i < a.length; i++) {
				stringBuilder.append(a[i]);
			}
			set.add(stringBuilder.toString());
		}
		for (String s : set) {
			String a1 = "INSERT INTO `so_devices` (`dev_no`, `project_id`, `box_no`, `location_id`, `sw0`, `sw1`, `sw2`, `sw3`, `sw4`, `sw5`, `sw6`, `sw7`, `gps_info`, `ip_addr`, `data_server_ip`, `data_server_port`, `create_time`) VALUES ('";
			String a2 = "', 5, 1, '420802', 0, 0, 0, 0, 2, 2, 2, 2, NULL, NULL, NULL, NULL, '2018-01-29 16:53:41');";
			// INSERT INTO `so_devices` (`dev_no`, `project_id`, `box_no`, `location_id`,
			// `sw0`, `sw1`, `sw2`, `sw3`, `sw4`, `sw5`, `sw6`, `sw7`, `gps_info`,
			// `ip_addr`, `data_server_ip`, `data_server_port`, `create_time`) VALUES
			// ('FEC722EC', 5, 1, '420802', 0, 0, 0, 0, 2, 2, 2, 2, NULL, NULL, NULL, NULL,
			// '2018-01-29 16:53:41');
			System.out.println(a1 + s + a2);
		}

	}

}
