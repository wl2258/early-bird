package com.ssonzm.userservice.common.converter;

import com.ssonzm.userservice.common.util.AesUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@Convert
public class StringEncryptUniqueConverter implements AttributeConverter<String, String> {
    private final AesUtil aesUtil;

    public StringEncryptUniqueConverter(AesUtil aesUtil) {
        this.aesUtil = aesUtil;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (StringUtils.isBlank(attribute)) return attribute;

        return aesUtil.encodeUnique(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (StringUtils.isBlank(dbData)) return dbData;

        return aesUtil.decodeUnique(dbData);
    }
}
