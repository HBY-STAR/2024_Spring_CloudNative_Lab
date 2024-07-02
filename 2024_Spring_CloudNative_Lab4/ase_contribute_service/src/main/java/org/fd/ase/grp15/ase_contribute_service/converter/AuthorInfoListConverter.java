package org.fd.ase.grp15.ase_contribute_service.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.fd.ase.grp15.ase_contribute_service.entity.AuthorInfo;

import java.io.IOException;
import java.util.List;

@Converter
public class AuthorInfoListConverter implements AttributeConverter<List<AuthorInfo>, String> {
    private static final ObjectMapper mapper = new ObjectMapper();
    @Override
    public String convertToDatabaseColumn(List<AuthorInfo> authorInfos) {
        try {
            return mapper.writeValueAsString(authorInfos);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting list to JSON: " + e.getMessage());
        }
    }

    @Override
    public List<AuthorInfo> convertToEntityAttribute(String s) {
        try {
            return mapper.readValue(s, new TypeReference<List<AuthorInfo>>(){});
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON to list: " + e.getMessage());
        }
    }
}
