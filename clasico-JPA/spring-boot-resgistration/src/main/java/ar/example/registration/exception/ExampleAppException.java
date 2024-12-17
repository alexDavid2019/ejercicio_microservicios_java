package ar.example.registration.exception;

import org.springframework.http.HttpStatus;

public class ExampleAppException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private HttpStatus httpStatus;
	private String mensaje;

	public ExampleAppException(HttpStatus estado, String mensaje) {
		super();
		this.httpStatus = estado;
		this.mensaje = mensaje;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus estado) {
		this.httpStatus = estado;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

}
