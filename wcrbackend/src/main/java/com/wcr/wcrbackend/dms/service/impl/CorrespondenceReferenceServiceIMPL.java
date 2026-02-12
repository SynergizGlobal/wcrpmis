package com.wcr.wcrbackend.dms.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.dms.dto.ReferenceLetterDTO;
import com.wcr.wcrbackend.dms.repository.CorrespondenceReferenceRepository;
import com.wcr.wcrbackend.dms.service.CorrespondenceReferenceService;

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
