package ru.hebi.check_manadas.java_imperative.adapter.rest.incoming.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.hebi.check_manadas.java_imperative.adapter.rest.incoming.dto.UserDto;
import ru.hebi.check_manadas.java_imperative.domain.User;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface UserMapper {

    User toDomain(UserDto userDto);

}