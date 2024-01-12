package co.deepmindz.adminorghierservice.exception;

import java.time.LocalDateTime;


//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
public class ErrorDetails {
    private LocalDateTime timeStamp;
    private String server_message;
    private long errorCode;
    private String errorMessage;
    private String path;
	public ErrorDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ErrorDetails(LocalDateTime timeStamp, String server_message, long errorCode, String errorMessage,
			String path) {
		super();
		this.timeStamp = timeStamp;
		this.server_message = server_message;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.path = path;
	}
	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getServer_message() {
		return server_message;
	}
	public void setServer_message(String server_message) {
		this.server_message = server_message;
	}
	public long getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(long errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
    
    
}
