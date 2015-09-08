package org.gamestartschool.codemage.python;

import org.python.core.PyObject;

public class PythonMethodCall {
	public PyObject method;
	public PyObject[] args;
	public PyObject result;
	public String message;
	public boolean isDone;

	public PythonMethodCall(PyObject method, PyObject[] args) {
		this.method = method;
		this.args = args;
	}

	public PyObject get() {
		return result;
	}

	public boolean isDone() {
		return isDone;
	}
}