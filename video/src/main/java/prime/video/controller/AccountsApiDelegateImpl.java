package prime.video.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import prime.video.api.AccountsApiDelegate;
import prime.video.model.AccountSummary;
import prime.video.repository.UserRepository;

@Service
public class AccountsApiDelegateImpl implements AccountsApiDelegate {

    private final UserRepository userRepository;

    public AccountsApiDelegateImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<AccountSummary> getMyAccount() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .map(u -> {
                    AccountSummary summary = new AccountSummary();
                    summary.setId(u.getId());
                    summary.setEmail(u.getEmail());
                    summary.setSubscriptionStatus(AccountSummary.SubscriptionStatusEnum.ACTIVE);
                    summary.setSubscriptionPlan(AccountSummary.SubscriptionPlanEnum.FREE);
                    return new ResponseEntity<>(summary, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }
}
