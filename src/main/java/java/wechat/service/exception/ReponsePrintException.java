package java.wechat.service.exception;


public class ReponsePrintException extends BaseException {
	private static final long serialVersionUID = 9211911225220917889L;

	@Override
	public String getErrorCode() {
		return "REPONSE0010";
	}

}
