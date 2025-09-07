package br.edu.ufape.projeto_bd.projeto_bd.domain.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.SupplierResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.LegalEntity;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.NaturalPerson;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Supplier;

@Mapper(componentModel = "spring", uses = {AddressMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SupplierMapper {

    SupplierResponseDTO toDTO(NaturalPerson entity);
    SupplierResponseDTO toDTO(LegalEntity entity);

    default SupplierResponseDTO toDTO(Supplier entity) {
        if (entity instanceof NaturalPerson) {
            return toDTO((NaturalPerson) entity);
        } else if (entity instanceof LegalEntity) {
            return toDTO((LegalEntity) entity);
        }
        return null;
    }
}