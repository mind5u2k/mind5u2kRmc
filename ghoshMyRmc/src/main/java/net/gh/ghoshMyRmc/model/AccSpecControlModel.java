package net.gh.ghoshMyRmc.model;

import net.gh.ghoshMyRmcBackend.dto.Control;

public class AccSpecControlModel {

	private Control control;
	private boolean status;

	public Control getControl() {
		return control;
	}

	public void setControl(Control control) {
		this.control = control;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}
