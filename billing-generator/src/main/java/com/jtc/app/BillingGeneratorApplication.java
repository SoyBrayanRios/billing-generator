package com.jtc.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jtc.app.service.ContractService;

@SpringBootApplication
public class BillingGeneratorApplication implements CommandLineRunner{
	
	@Autowired
	private ContractService contractService;
	
	public static void main(String[] args) {
		SpringApplication.run(BillingGeneratorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {		
		/*contractService.saveContractsFromFile("/p_18.3_FACELDI_2022/18.3.2_TRABAJO/18.3.2.17_BrayanRios/Otros/Source Billing Generator/BD_Facturación.csv");
		contractService.saveContractsFromFile("/p_18.3_FACELDI_2022/18.3.2_TRABAJO/18.3.2.17_BrayanRios/Otros/Source Billing Generator/BD_Facturación2.csv");*/
	}

}
