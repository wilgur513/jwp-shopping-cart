package woowacourse.shoppingcart.exception;

public class InvalidCustomerException extends ShoppingCartException {

    private static final String MESSAGE = "존재하지 않는 유저입니다.";
    private static final int ERROR_CODE = 1002;

    public InvalidCustomerException() {
        super(ERROR_CODE, MESSAGE);
    }
}
