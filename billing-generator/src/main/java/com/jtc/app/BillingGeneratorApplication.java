package com.jtc.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jtc.app.service.ContractService;

@SpringBootApplication
public class BillingGeneratorApplication implements CommandLineRunner{
	
	@Autowired
	private ContractService contractFeService;
	
	public static void main(String[] args) {
		SpringApplication.run(BillingGeneratorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {		
		/*contractFeService.saveFeContractsFromFile("/p_18.3_FACELDI_2022/18.3.2_TRABAJO/18.3.2.17_BrayanRios/Otros/Source Billing Generator/BD_Facturación.csv");
		contractFeService.saveFeContractsFromFile("/p_18.3_FACELDI_2022/18.3.2_TRABAJO/18.3.2.17_BrayanRios/Otros/Source Billing Generator/BD_Facturación2.csv");
		contractFeService.saveFeContractsFromFile("/p_18.3_FACELDI_2022/18.3.2_TRABAJO/18.3.2.17_BrayanRios/Otros/Source Billing Generator/BD_Facturación3.csv");
		contractFeService.saveFeContractsFromFile("/p_18.3_FACELDI_2022/18.3.2_TRABAJO/18.3.2.17_BrayanRios/Otros/Source Billing Generator/BD_Facturación4.csv");*/
		//contractFeService.saveFeContractsFromFile("/p_18.3_FACELDI_2022/18.3.2_TRABAJO/18.3.2.17_BrayanRios/Otros/Source Billing Generator/BD_Facturación04.csv");
		//contractFeService.saveNeContractsFromFile("/p_18.3_FACELDI_2022/18.3.2_TRABAJO/18.3.2.17_BrayanRios/Otros/Source Billing Generator/BD_Nomina1.csv");
		//contractFeService.saveNeContractsFromFile("/p_18.3_FACELDI_2022/18.3.2_TRABAJO/18.3.2.17_BrayanRios/Otros/Source Billing Generator/BD_Nomina2.csv");
		//contractFeService.saveFeContractsFromFile("/p_18.3_FACELDI_2022/18.3.2_TRABAJO/18.3.2.17_BrayanRios/Otros/Source Billing Generator/Contrato_nuevo.csv");
		contractFeService.saveNeContractsFromFile("/p_18.3_FACELDI_2022/18.3.2_TRABAJO/18.3.2.17_BrayanRios/Otros/Source Billing Generator/BD_Nomina3.csv");
	}

}
