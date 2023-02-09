package com.jtc.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtc.app.primary.entity.Branch;
import com.jtc.app.service.BranchService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/branches")
public class BranchController {
	
	@Autowired
	private BranchService branchService;
	
	@PostMapping("/")
	public Branch saveBranch(@RequestBody Branch branch) throws Exception {
		return branchService.saveBranch(branch);
	}

	@GetMapping("/all")
	public List<Branch> getBranches() {
		return branchService.getBranches();
	}

	@GetMapping("/{branchId}")
	public Branch getBranchById(@PathVariable("branchId") Long branchId) {
		return branchService.getBranchById(branchId);
	}
	
	@DeleteMapping("/{branchId}")
	public void deleteBranch(Long branchId) {
		branchService.deleteBranch(branchId);
	}
	
	@GetMapping("/u")
	public List<Branch> updateAllBranches() {
		return branchService.updateAllBranches();
	}
	
	@GetMapping("/to-update")
	public List<Branch> getBranchesWithoutContract() {
		return branchService.getBranchesWithoutContractByMonthYear();
	}
}
