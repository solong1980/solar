package com.solar.gui.component.formate;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class NJTextField extends JTextField {
	private static final long serialVersionUID = 1L;
	private int thisLen = 0;
	private int maxLen = Integer.MAX_VALUE;

	public NJTextField() {
		super();
	}

	public NJTextField(int cols, int maxLen) {
		super(cols);
		this.maxLen = maxLen;
	}

	public NJTextField(int maxLen) {
		super(0);
		this.maxLen = maxLen;
	}

	protected Document createDefaultModel() {
		return new LengthLimitedDocument();
	}

	class LengthLimitedDocument extends PlainDocument {
		private static final long serialVersionUID = 1L;

		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
			if (str == null || thisLen >= maxLen)
				return;
			if (thisLen + str.length() > maxLen)
				str = str.substring(0, maxLen - thisLen);
			thisLen += str.length();
			super.insertString(offs, str, a);
		}

		public void remove(int offs, int len) throws BadLocationException {
			thisLen -= len;
			super.remove(offs, len);
		}
	}

}
