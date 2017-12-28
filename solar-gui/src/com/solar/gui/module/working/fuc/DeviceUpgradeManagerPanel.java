package com.solar.gui.module.working.fuc;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.solar.client.ObservableMedia;
import com.solar.client.SoRet;
import com.solar.common.context.ConnectAPI;
import com.solar.common.util.JsonUtilTool;
import com.solar.entity.SoAbtAuth;
import com.solar.gui.module.working.BasePanel;

@SuppressWarnings("serial")
public class DeviceUpgradeManagerPanel extends BasePanel implements Observer {
	public DeviceUpgradeManagerPanel() {
		super(new BorderLayout());
		JButton upBtn = new JButton(getBoldHTML("更新升级文件缓存"));
		add(upBtn, BorderLayout.CENTER);
		upBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ObservableMedia.getInstance().devUpFileBlockCacheClean();
			}
		});
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof SoRet) {
			SoRet ret = (SoRet) arg;
			int code = ret.getCode();
			int status = ret.getStatus();
			if (status == 0) {
				switch (code) {
				case ConnectAPI.DEVICES_UPGRADECTR_RESPONSE:
					String json = ret.getRet();
					SoAbtAuth abAuth = JsonUtilTool.fromJson(json, SoAbtAuth.class);
					JOptionPane.showMessageDialog(this, abAuth.getMsg());
					break;
				default:
					break;
				}
			}
		}
	}

}
