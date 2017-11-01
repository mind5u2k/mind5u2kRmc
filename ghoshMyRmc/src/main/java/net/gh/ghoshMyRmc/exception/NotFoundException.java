package net.gh.ghoshMyRmc.exception;

public class NotFoundException extends Exception {

	/**
	 * @author anurag ghosh
	 */

	private static final long serialVersionUID = 1L;

	private String msg;

	public NotFoundException() {
		this("No data found Exception");
	}

	public NotFoundException(String msg) {
		this.msg = System.currentTimeMillis() + " : " + msg;
	}

	public String getMsg() {
		return msg;
	}
}
