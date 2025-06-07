package brb.team.olvanback.exception;

public class UserPasswordNotMatchException extends RuntimeException {
    public UserPasswordNotMatchException(String str) {
        super(str);
    }
}
