package com.jtc.app.service;

import java.util.List;

import com.jtc.app.primary.entity.Client;

/**
 * Esta interface define los servicios relacionados a la clase Client.
 *
 */
public interface ClientService {

	public Client saveClient(Client client) throws Exception;
	public List<Client> getClients();
	public Client getClientByNit(String clientId);
	public void deleteClient(String clientId);
	public List<Client> updateAllClients();
}
