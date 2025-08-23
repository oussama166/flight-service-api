package org.jetblue.jetblue.Service.Implementation;

import lombok.SneakyThrows;
import org.jetblue.jetblue.Mapper.CreditCard.CreditCardMapper;
import org.jetblue.jetblue.Mapper.CreditCard.CreditCardRequest;
import org.jetblue.jetblue.Mapper.CreditCard.CreditCardResponse;
import org.jetblue.jetblue.Mapper.CreditCard.FullCreditCardInfoResponse;
import org.jetblue.jetblue.Models.DAO.CreditCard;
import org.jetblue.jetblue.Models.DAO.User;
import org.jetblue.jetblue.Repositories.CreditCardRepo;
import org.jetblue.jetblue.Repositories.UserRepo;
import org.jetblue.jetblue.Service.CreditCardService;
import org.jetblue.jetblue.Utils.EncryptInfoUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.jetblue.jetblue.Models.ENUM.CardType.*;
import static org.jetblue.jetblue.Utils.UserUtils.validateUser;

@Service
public class CreditCardServiceImpl implements CreditCardService {
    EncryptInfoUtils encryptInfoUtils;
    PasswordEncoder passwordEncoder;
    UserRepo userRepo;
    CreditCardRepo creditCardRepo;

    public CreditCardServiceImpl(EncryptInfoUtils encryptInfoUtils, UserRepo userRepo, CreditCardRepo creditCardRepo) {
        this.encryptInfoUtils = encryptInfoUtils;
        this.userRepo = userRepo;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.creditCardRepo = creditCardRepo;
    }

    @Override
    public boolean validateCardNumber(String cardNumber) {
        return false;
    }

    @Override
    public String getCardType(String cardNumber) throws Exception {
        // encode the card type from the card number
        if (cardNumber == null || cardNumber.isBlank()) throw new Exception("Card number is null or blank");
        if (cardNumber.startsWith("4")) return fromCardType(VISA);
        if (cardNumber.startsWith("5")) return fromCardType(MASTERCARD);
        if (cardNumber.startsWith("34") || cardNumber.startsWith("37")) return
                fromCardType(AMERICAN_EXPRESS);
        if (cardNumber.startsWith("6")) return fromCardType(DISCOVER);
        if (cardNumber.startsWith("35")) return fromCardType(JCB);
        if (cardNumber.startsWith("30") || cardNumber.startsWith("36") || cardNumber.startsWith("38"))
            return fromCardType(DINERS_CLUB);
        if (cardNumber.startsWith("62")) return fromCardType(UNIONPAY);
        return "";
    }

    @Override
    public CreditCardResponse addCreditCardToUser(String username, CreditCardRequest creditCardRequest) throws Exception {
        // check if the user exists and connected
        if (username == null || username.isBlank()) return null;

        Optional<User> user = userRepo.findByUsername(username);

        if (user.isEmpty()) throw new Exception("User not found");

        validateUser(user.get().getUsername());

        // Validate the credit card request and add the creditcard to the user
        if (creditCardRequest == null) return null;

        String encryptedCardNumber = encryptInfoUtils.encrypt(creditCardRequest.cardNumber());
        String hashedCarNumber = passwordEncoder.encode(creditCardRequest.cardNumber());
        String lastFourDigits = creditCardRequest.cardNumber().substring(creditCardRequest.cardNumber().length() - 4);

        // format MM/YY or MM/YYYY to LocalDate
        LocalDate expirationDate;
        String[] expParts = creditCardRequest.expirationDate().split("/");
        if (expParts.length != 2) throw new Exception("Invalid expiration date format");
        int month = Integer.parseInt(expParts[0]);
        int year = Integer.parseInt(expParts[1]);
        if (year < 100) {
            year += 2000;
        }
        expirationDate = LocalDate.of(year, month, 1).plusMonths(1).minusDays(1);

        CreditCard creditCard = CreditCard
                .builder()
                .cardHolderName(creditCardRequest.cardHolderName())
                .cardNumber(encryptedCardNumber)
                .hashedCardNumber(hashedCarNumber)
                .lastFourDigits(lastFourDigits)
                .cvv(encryptInfoUtils.encrypt(creditCardRequest.cvv()))
                .expirationDate(expirationDate.toString())
                .billingAddress(creditCardRequest.billingAddress())
                .user(user.get())
                .cardType(
                        fromString(creditCardRequest.cardType())
                )
                .build();

        CreditCard save = creditCardRepo.save(creditCard);
        return CreditCardMapper.toCreditCardResponse(save);
    }

    @SneakyThrows
    @Override
    public List<FullCreditCardInfoResponse> CreditCardByUsername(String username) {
        if (username == null || username.isBlank()) return null;
        Optional<User> user = userRepo.findByUsername(username);
        if (user.isEmpty()) throw new Exception("User not found");
        validateUser(user.get().getUsername());

        List<CreditCard> creditCard = creditCardRepo.findByUser_Username((user.get().getUsername()));
        if (creditCard.isEmpty()) return null;
        return creditCard.stream().map((el)->{
            try {
                return CreditCardMapper.toFullCreditCardInfoResponse(el);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());

    }

    @Override
    public boolean DeleteCreditCardByUsername(String username, String lastFourDigits) {
        if (username == null || username.isBlank()) return false;
        Optional<User> user = userRepo.findByUsername(username);
        if (user.isEmpty()) return false;
        validateCardNumber(username);

        Optional<CreditCard> creditCard = creditCardRepo.findByUser_UsernameAndLastFourDigits(
                user.get().getUsername(), lastFourDigits
        );
        if (creditCard.isPresent()) {
            creditCardRepo.delete(creditCard.get());
            return true;
        }
        return false;
    }
}
