package com.yzy.diary.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Diary implements Serializable {
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		public int _id;
		public String label;
		public String content ;
		public Timestamp date;
		
		public Diary() {
		}
		
		public Diary(String label, String content) {
			this.label = label;
			this.content = content;
		}
		public void setLabel(String label) {
			this.label = label;
		}

		public void setContent(String content) {
			this.content = content;
		}
}
