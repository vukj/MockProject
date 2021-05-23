package com.fa.training.mockproject.services.mapper;

import com.fa.training.mockproject.entities.UserAccounts;
import com.fa.training.mockproject.entities.dto.UserAccountsLoginDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserAccountsMapper {

    UserAccountsMapper INSTANCE = Mappers.getMapper(UserAccountsMapper.class);

    UserAccountsLoginDTO toDTO(UserAccounts userAccounts);

    UserAccounts fromDTO(UserAccountsLoginDTO userAccountsDTO);
}
