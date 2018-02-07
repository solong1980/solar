package com.solar.cli.netty.session;

import java.io.Closeable;

import com.solar.command.message.response.ResponseMsg;

public interface ISession<T> extends Closeable {

	/**
	 * 发送消息给客户端
	 * 
	 * @param msg
	 * @return
	 * @throws InterruptedException
	 */
	public T sendMsg(ResponseMsg msg);

	/**
	 * 回写字符串消息给客户端
	 * 
	 * @param msg
	 * @return
	 */
	public T sendMsg(String msg);

	/**
	 * 回写字节数组消息给客户端
	 * 
	 * @param msg
	 * @return
	 */
	public T sendMsg(byte[] data);

	/**
	 *
	 * @return
	 */
	public String getAddress();

	/**
	 *
	 * @param isLogin
	 */
	public void setLogin(boolean isLogin);

	/**
	 * 是否登录
	 * 
	 * @return
	 */
	public boolean isLogin();

	/**
	 * 保存会话鉴权信息
	 * 
	 * @param obj
	 */
	public void setEnti(Object obj);

	/**
	 * 得到会话鉴权信息
	 */
	public <E> E getEnti(Class<E> e);

	/**
	 * 关闭SESSION
	 */
	public void close();

	public int getTime();

	public void addTime(int i);

	public void addBlockFailTime(int i);

	public int getBlockFailTime();

}
