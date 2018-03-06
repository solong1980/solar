package com.solar.entity;

import java.util.List;

@SuppressWarnings("serial")
public class SoDevicesBatch extends SoAbtAuth {
	private List<SoDevices> batchAdds;
	private List<SoDevices> batchDels;
	public List<SoDevices> getBatchAdds() {
		return batchAdds;
	}
	public void setBatchAdds(List<SoDevices> batchAdds) {
		this.batchAdds = batchAdds;
	}
	public List<SoDevices> getBatchDels() {
		return batchDels;
	}
	public void setBatchDels(List<SoDevices> batchDels) {
		this.batchDels = batchDels;
	}
	
}
