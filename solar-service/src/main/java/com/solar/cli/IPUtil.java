package com.solar.cli;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class IPUtil {
	public static void main(String[] args) throws Exception {
		InetAddress hostLANAddress = getLocalHostLANAddress();
		
		System.out.println(hostLANAddress.getHostAddress());
	}
	
	public static InetAddress getLocalHostLANAddress() throws Exception {
	    try {
	        InetAddress candidateAddress = null;
	        // 遍历所有的网络接口
	        for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
	            NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
	            // 在所有的接口下再遍历IP
	            for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
	                InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
	                boolean linkLocalAddress = inetAddr.isLinkLocalAddress();
	                boolean loopbackAddress = inetAddr.isLoopbackAddress();
	                System.out.println("linkLocalAddress="+linkLocalAddress);
	                System.out.println("loopbackAddress="+loopbackAddress);
	                System.out.println("getHostAddress="+inetAddr.getHostAddress());
	                System.out.println("getCanonicalHostName="+inetAddr.getCanonicalHostName());
	                if (!inetAddr.isLoopbackAddress()) {
	                	// 排除loopback类型地址
	                    if (inetAddr.isSiteLocalAddress()) {
	                        // 如果是site-local地址，就是它了
	                        return inetAddr;
	                    } else if (candidateAddress == null) {
	                        // site-local类型的地址未被发现，先记录候选地址
	                        candidateAddress = inetAddr;
	                    }
	                }
	            }
	        }
	        if (candidateAddress != null) {
	            return candidateAddress;
	        }
	        // 如果没有发现 non-loopback地址.只能用最次选的方案
	        InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
	        return jdkSuppliedAddress;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
}
