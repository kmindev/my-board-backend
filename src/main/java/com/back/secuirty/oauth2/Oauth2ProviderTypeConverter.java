package com.back.secuirty.oauth2;

import org.springframework.core.convert.converter.Converter;

public class Oauth2ProviderTypeConverter implements Converter<String, Oauth2ProviderType> {
    @Override
    public Oauth2ProviderType convert(String source) {
        return Oauth2ProviderType.from(source);
    }
}
