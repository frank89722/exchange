package me.frankv.core.mapper;

public interface EntityMapper<ET, DT> {
    ET mapToEntity(DT dto);
    DT mapToDto(ET entity);
}
