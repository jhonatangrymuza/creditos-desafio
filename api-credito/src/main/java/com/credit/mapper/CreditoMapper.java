package com.credit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.util.List;
import com.credit.dto.CreditoDTO;
import com.credit.entity.Credito;

@Mapper(componentModel = "spring")
public interface CreditoMapper {

    @Mapping(target = "simplesNacional", source = "simplesNacional", qualifiedByName = "booleanToString")
    CreditoDTO toDTO(Credito credito);

    List<CreditoDTO> toDTOList(List<Credito> creditos);

    @Named("booleanToString")
    default String booleanToString(boolean value) {
        return value ? "Sim" : "Não";
    }
}