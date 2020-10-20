package com.fishtankapps.pictoralfin.jComponents;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

public class JLongField extends JTextField {

	private static final long serialVersionUID = -3115096438706317990L;
	
	

	public JLongField() {
		super();
		((PlainDocument) getDocument()).setDocumentFilter(new TextFieldOnlyLongFilter());
	}

	public JLongField(Document doc, String text, int columns) {
		super(doc, text, columns);
		((PlainDocument) getDocument()).setDocumentFilter(new TextFieldOnlyLongFilter());
	}

	public JLongField(int columns) {
		super(columns);
		((PlainDocument) getDocument()).setDocumentFilter(new TextFieldOnlyLongFilter());
	}

	public JLongField(String text, int columns) {
		super(text, columns);
		((PlainDocument) getDocument()).setDocumentFilter(new TextFieldOnlyLongFilter());
	}

	public JLongField(String text) {
		super(text);
		((PlainDocument) getDocument()).setDocumentFilter(new TextFieldOnlyLongFilter());
	}

	private class TextFieldOnlyLongFilter extends DocumentFilter {
		@Override
		public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
				throws BadLocationException {

			Document doc = fb.getDocument();
			StringBuilder sb = new StringBuilder();
			sb.append(doc.getText(0, doc.getLength()));
			sb.insert(offset, string);

			if (test(sb.toString())) {
				super.insertString(fb, offset, string, attr);
			} else {
				// warn the user and don't allow the insert
			}
		}

		private boolean test(String text) {
			
			if(text.length() == 0)
				return true;
			
			try {
				Long.parseLong(text);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}

		@Override
		public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
				throws BadLocationException {

			Document doc = fb.getDocument();
			StringBuilder sb = new StringBuilder();
			sb.append(doc.getText(0, doc.getLength()));
			sb.replace(offset, offset + length, text);

			if (test(sb.toString())) {
				super.replace(fb, offset, length, text, attrs);
			} else {
				// warn the user and don't allow the insert
			}

		}

		@Override
		public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
			Document doc = fb.getDocument();
			StringBuilder sb = new StringBuilder();
			sb.append(doc.getText(0, doc.getLength()));
			sb.delete(offset, offset + length);

			if (test(sb.toString())) {
				super.remove(fb, offset, length);
			} else {
				// warn the user and don't allow the insert
			}

		}
	}
	
}
