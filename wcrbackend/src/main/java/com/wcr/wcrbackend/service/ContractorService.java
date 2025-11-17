package com.wcr.wcrbackend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wcr.wcrbackend.entity.Contractor;
import com.wcr.wcrbackend.repo.ContractorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContractorService {

    private final ContractorRepository repo;

    public List<Contractor> getAll() {
        return repo.findAll();
    }

    public Contractor getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Contractor not found"));
    }

    // CREATE
    public Contractor create(Contractor c) {

        // Generate new contractor ID
        String newId = generateContractorId();
        c.setContractorId(newId);

        return repo.save(c);
    }

    public String generateContractorId() {
        Contractor last = repo.findTopByOrderByContractorIdDesc();

        if (last == null) {
            return "CON0001";
        }

        int num = Integer.parseInt(last.getContractorId().substring(3));
        num++;

        return "CON" + String.format("%04d", num);
    }

    // UPDATE 
    public Contractor update(String id, Contractor c) {

        Contractor existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Contractor not found"));

        existing.setContractorName(c.getContractorName());
        existing.setPanNumber(c.getPanNumber());
        existing.setSpecilaization(c.getSpecilaization());
        existing.setAddress(c.getAddress());
        existing.setPrimaryContact(c.getPrimaryContact());
        existing.setPhoneNumber(c.getPhoneNumber());
        existing.setEmail(c.getEmail());
        existing.setGstNumber(c.getGstNumber());
        existing.setBankName(c.getBankName());
        existing.setIfscCode(c.getIfscCode());
        existing.setAccountNo(c.getAccountNo());
        existing.setBankAddress(c.getBankAddress());
        existing.setRemarks(c.getRemarks());

        return repo.save(existing);
    }
}
