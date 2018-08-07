package com.solar.gui.component.formate;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class JTextFieldLimit extends PlainDocument {
	private static final long serialVersionUID = 1L;
	private int limit; // 限制的长度

	public JTextFieldLimit(int limit) {
		super(); // 调用父类构造
		this.limit = limit;
	}

	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		if (str == null)
			return;
		if ((getLength() + str.length()) <= limit) {
			super.insertString(offset, str, attr);// 调用父类方法
		}
	}
}