package woowacourse.shoppingcart.application;

import org.springframework.stereotype.Service;
import woowacourse.shoppingcart.dao.AccountDao;
import woowacourse.shoppingcart.domain.Account;
import woowacourse.auth.support.PasswordEncoder;
import woowacourse.shoppingcart.dto.SignUpRequest;
import woowacourse.shoppingcart.dto.AccountUpdateRequest;
import woowacourse.shoppingcart.exception.DuplicateAccountException;
import woowacourse.shoppingcart.exception.InvalidAccountException;

@Service
public class AccountService {

    private final AccountDao accountDao;

    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public Long registerCustomer(SignUpRequest request) {
        if (accountDao.existByEmail(request.getEmail())) {
            throw new DuplicateAccountException();
        }
        String encryptPassword = PasswordEncoder.encrypt(request.getPassword());
        Account account = accountDao.save(
                new Account(request.getEmail(), encryptPassword, request.getNickname()));
        return account.getId();
    }

    public Account findByEmail(String email) {
        return accountDao.findByEmail(email)
                .orElseThrow(InvalidAccountException::new);
    }

    public void deleteByEmail(String email) {
        if (!accountDao.existByEmail(email)) {
            throw new InvalidAccountException();
        }
        accountDao.deleteByEmail(email);
    }

    public void updateCustomer(String email, AccountUpdateRequest request) {
        Account account = findByEmail(email);
        String encryptPassword = PasswordEncoder.encrypt(request.getPassword());
        accountDao.update(new Account(account.getId(), account.getEmail(), encryptPassword,
                request.getNickname()));
    }
}
