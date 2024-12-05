package com.back.domain.converter;

import com.back.domain.UserRoleType;
import jakarta.persistence.AttributeConverter;

public class UserRoleTypeConverter implements AttributeConverter<UserRoleType, String> {

    @Override
    public String convertToDatabaseColumn(UserRoleType attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.getName();
    }

    @Override
    public UserRoleType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        return UserRoleType.getUserRoleType(dbData);
    }

}
