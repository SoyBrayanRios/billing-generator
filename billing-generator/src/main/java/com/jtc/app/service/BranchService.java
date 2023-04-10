package com.jtc.app.service;

import java.util.List;

import com.jtc.app.primary.entity.Branch;

public interface BranchService {
	
	public Branch saveBranch(Branch branch) throws Exception;
	public List<Branch> getBranches();
	public List<Branch> getBranchesWithoutContract(String module);
	public Branch getBranchById(Long branchId);
	public void deleteBranch(Long branchId);
	public Branch findByClientId(String clientId);
	public List<Branch> updateAllBranches();
}
