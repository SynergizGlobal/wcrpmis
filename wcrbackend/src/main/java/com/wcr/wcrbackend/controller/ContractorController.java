package com.wcr.wcrbackend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcr.wcrbackend.entity.Contractor;
import com.wcr.wcrbackend.service.ContractorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/contractors")
@RequiredArgsConstructor
public class ContractorController {

    private final ContractorService service;

    @GetMapping
    public List<Contractor> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Contractor getById(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping
    public Contractor create(@RequestBody Contractor contractor) {
        return service.create(contractor);
    }

    @PutMapping("/{id}")
    public Contractor update(@PathVariable String id, @RequestBody Contractor contractor) {
        return service.update(id, contractor);
    }
}
