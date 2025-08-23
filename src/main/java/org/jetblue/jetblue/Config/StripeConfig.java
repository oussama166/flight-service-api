package org.jetblue.jetblue.Config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.stripe.Stripe.*;

@Service
public class StripeConfig {

    @Value("${stripe.secretKey}")
    private String pk_stripe;
    @PostConstruct
    public void init() {
        apiKey = pk_stripe;
    }
}
