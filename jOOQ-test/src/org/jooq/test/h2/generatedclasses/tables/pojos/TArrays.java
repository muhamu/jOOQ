/**
 * This class is generated by jOOQ
 */
package org.jooq.test.h2.generatedclasses.tables.pojos;

/**
 * This class is generated by jOOQ.
 */
public class TArrays implements org.jooq.test.h2.generatedclasses.tables.interfaces.ITArrays {

	private static final long serialVersionUID = 1088877553;

	private java.lang.Integer  id;
	private java.lang.Object[] stringArray;
	private java.lang.Object[] numberArray;
	private java.lang.Object[] dateArray;

	@Override
	public java.lang.Integer getId() {
		return this.id;
	}

	@Override
	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	@Override
	public java.lang.Object[] getStringArray() {
		return this.stringArray;
	}

	@Override
	public void setStringArray(java.lang.Object[] stringArray) {
		this.stringArray = stringArray;
	}

	@Override
	public java.lang.Object[] getNumberArray() {
		return this.numberArray;
	}

	@Override
	public void setNumberArray(java.lang.Object[] numberArray) {
		this.numberArray = numberArray;
	}

	@Override
	public java.lang.Object[] getDateArray() {
		return this.dateArray;
	}

	@Override
	public void setDateArray(java.lang.Object[] dateArray) {
		this.dateArray = dateArray;
	}
}
