package com.task.second.trip.advisor.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Setter
@Getter
public class CurrencyProviderConfig {

    @Value("${currency.data.provider.api.key}")
    private String apiKey;
    @Value("${currency.data.provider.latest.url}")
    private String ratesUrl;


}
