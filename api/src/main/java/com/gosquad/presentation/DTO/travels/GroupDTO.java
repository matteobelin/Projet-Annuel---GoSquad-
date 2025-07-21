package com.gosquad.presentation.DTO.travels;

import java.util.List;

public record GroupDTO(
        Integer id,
        String nom,
        List<ParticipantDTO> participants
) {}
