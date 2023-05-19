package com.jtc.app.service;

import java.util.List;

import com.jtc.app.secondary.entity.FEClient;

/**
 * Esta interface define los servicios relacionados a la clase FEClient.
 *
 */
public interface FEClientService {

	public List<FEClient> getFEClients();
	public List<FEClient> getFEActiveClients();
	public FEClient getClientById(Long clientId);
}
