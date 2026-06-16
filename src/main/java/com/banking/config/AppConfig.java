package com.banking.config;

import com.banking.strategy.CardPayment;
import com.banking.strategy.CashPayment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// [CONCEPT: @Configuration]
// Marks this class as Spring configuration source
// Spring reads this class FIRST when ApplicationContext boots
// It is itself a Spring bean — Spring manages this class too

@Configuration

// [CONCEPT: @ComponentScan]
// Tells Spring: "scan everything under com.banking package"
// Spring will find all @Component, @Service, @Repository, @Primary
// annotations automatically and register them as beans
// Without this — Spring finds NOTHING

@ComponentScan(basePackages = "com.banking")
public class AppConfig {

    // [CONCEPT: @Bean — Method 1]
    // CardPayment COULD just use @Component
    // But imagine it needed extra setup — like setting a card network
    // or reading from a config file
    // In that case you'd remove @Component and define it here instead
    // This shows you HOW @Bean works for custom bean creation
    //
    // Spring calls this method once, stores the returned object
    // as a bean named "customCardPayment" in the container
    @Bean(name = "customCardPayment")
    public CardPayment customCardPayment() {
        System.out.println("[Config] Creating CardPayment bean manually");
        // here you could pass constructor args, set properties, etc.
        return new CardPayment();
    }

    // [CONCEPT: @Bean — Method 2]
    // Same for CashPayment — demonstrating @Bean
    // with a custom bean name
    @Bean(name = "customCashPayment")
    public CashPayment customCashPayment() {
        System.out.println("[Config] Creating CashPayment bean manually");
        return new CashPayment();
    }
}