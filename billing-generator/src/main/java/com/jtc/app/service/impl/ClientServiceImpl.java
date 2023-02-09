package com.jtc.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtc.app.primary.dao.AllianceRepository;
import com.jtc.app.primary.dao.ClientRepository;
import com.jtc.app.primary.entity.Client;
import com.jtc.app.secondary.dao.FEClientRepository;
import com.jtc.app.secondary.entity.FEClient;
import com.jtc.app.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService{

	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private FEClientRepository feClientRepository;
	@Autowired
	private AllianceRepository allianceRepository;
	
	@Override
	public Client saveClient(Client client) throws Exception {
		return clientRepository.save(client);
	}

	@Override
	public List<Client> getClients() {
		return clientRepository.findAll();
	}

	@Override
	public Client getClientByNit(String clientId) {
		Client client = clientRepository.findByNit(clientId);
		if (client == null) {
			throw new RuntimeException("El id de usuario " + clientId + " no fue encontrado.");
		}
		return client;
	}

	@Override
	public void deleteClient(String clientId) {
		Client client = clientRepository.findByNit(clientId);
		if (client == null) {
			throw new RuntimeException("El id de usuario " + clientId + " no fue encontrado.");
		}
		clientRepository.deleteById(clientId);
	}

	@Override
	public List<Client> updateAllClients() {
		List<FEClient> feClients = feClientRepository.getActiveFEClients();
		feClients.forEach(client -> {
			Client tempClient = clientRepository.findByNit(client.getNroIdentificacion());
			if (tempClient == null) {
				tempClient = new Client();
				tempClient.setNit(client.getNroIdentificacion());
				tempClient.setDV(client.getDigitoVerif());
				tempClient.setTipoIdentificacion(client.getTipoIdentificacion());
				tempClient.setTipoPersona(client.getTipoPersona());
				tempClient.setRazonSocial(client.getRazonSocial());
				tempClient.setTipoRegimen(client.getTipoRegimen());
				tempClient.setPrimerApellido(client.getPrimerNombre());
				tempClient.setSegundoApellido(client.getSegundoNombre());
				tempClient.setPrimerNombre(client.getPrimerApellido());
				tempClient.setSegundoNombre(client.getSegundoApellido());
				tempClient.setDepartamento(client.getDepartamento());
				tempClient.setMunicipio(client.getMunicipio());
				tempClient.setDireccion(client.getDireccion());
				tempClient.setZonaPostal(client.getZonaPostal());
				tempClient.setCorreoElectronico(client.getCorreoElectronico());
				tempClient.setDocumentoEquivalente(client.getDocumentoEquivalente());
				if (client.getAlianzaComercial() != null) {
					tempClient.setAlliance(allianceRepository.getById(client.getAlianzaComercial()));					
				} else {
					tempClient.setAlliance(null);
				}
				try {
					clientRepository.save(tempClient);
					System.out.println("Se guardó con exito el cliente " + tempClient.getNit());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("El cliente " + client.getNroIdentificacion() + " ya existe ");
			}
		});
		return clientRepository.findAll();
	}

}
