package com.payment.service.controller;

import com.payment.service.dto.response.BranchResponse;
import com.payment.service.service.BranchService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/branches")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BranchController {
    BranchService branchService;

    @GetMapping("/{code}")
    BranchResponse getLangByCode(@PathVariable("code") String code) {
        return branchService.getBranchByCode(code);
    }

    @GetMapping("")
    List<BranchResponse> getAllLang() {
        return branchService.getAllSa();
    }
}
