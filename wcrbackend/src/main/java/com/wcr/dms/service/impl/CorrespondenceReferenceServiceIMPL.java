package com.wcr.dms.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.wcr.dms.dto.ReferenceLetterDTO;
import com.wcr.dms.repository.CorrespondenceReferenceRepository;
import com.wcr.dms.service.CorrespondenceReferenceService;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CorrespondenceReferenceServiceIMPL implements CorrespondenceReferenceService {

    private final CorrespondenceReferenceRepository correspondenceReferenceRepository;


    @Override
    public List<ReferenceLetterDTO> getReferenceLettersByCorrespondenceId(Long correspondenceId) {
        return correspondenceReferenceRepository.findReferenceLettersByCorrespondenceId(correspondenceId)
                .stream()
                .map(ref -> new ReferenceLetterDTO(ref.getRefId(), ref.getRefLetters()))
                .toList();
    }
}
