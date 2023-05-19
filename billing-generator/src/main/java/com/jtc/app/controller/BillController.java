package com.jtc.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtc.app.primary.entity.Bill;
import com.jtc.app.primary.entity.JsonResponse;
import com.jtc.app.service.BillService;

/**
 * Controlador que expone todas las API que interactuan con la información de las facturas 
 * generadas por Kosmos.
 *
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/billing")
public class BillController {

	@Autowired
	private BillService billService;

	/**
	 * API para guardar una factura generada por Kosmos.
	 * @param bill
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/")
	public Bill saveBill(@RequestBody Bill bill) throws Exception {
		return billService.saveBill(bill);
	}

	/**
	 * API para obtener todas las facturas generadas por Kosmos 
	 * y que se encuentran guardadas en la base de datos.
	 * @return Lista de objetos de tipo Bill.
	 */
	@GetMapping("/")
	public List<Bill> getBills() {
		return billService.getBills();
	}

	/**
	 * API para obtener los datos de una factura según su ID.
	 * @param billId (ID de la factura).
	 * @return El objeto con los datos de la factura que corresponda al ID suministrado.
	 */
	@GetMapping("/{billId}")
	public Bill getBillById(@PathVariable("billId") Long billId) {
		return billService.getBillById(billId);
	}

	/**
	 * Calcula los cobros del servicio, año y mes indicado persistiendo dicha información en la base de datos de la 
	 * aplicación.
	 * @param year (Año que se va a facturar).
	 * @param month (Mes que se va a facturar).
	 * @param invoiceNumber (Consecutivo que tendrá la primera factura generada).
	 * @param environment (S -> Simulation ó P -> Production).
	 * @param module (Acrónimo que indica cual servicio se va a facturar, bien sea FE, NE o DS).
	 * @return JsonResponse con un mensaje personalizado indicando que la facturación se emitio correctamente.
	 * @see com.jtc.app.primary.entity.JsonResponse
	 */
	@GetMapping("/generate/{year}/{month}/{number}/{env}/{module}")
	public JsonResponse generateBills(@PathVariable("year") Long year, @PathVariable("month") Long month, 
			@PathVariable("number") Long invoiceNumber, @PathVariable("env") String environment, @PathVariable("module") String module) {
		return billService.generateBills(year, month, invoiceNumber, environment, module);
	}
	
	/**
	 * Calcula los cobros del servicio, año y mes indicado guardando dicha información en la memoria de la 
	 * aplicación.
	 * @param year (Año que se va a facturar).
	 * @param month (Mes que se va a facturar).
	 * @param invoiceNumber (Consecutivo que tendrá la primera factura generada).
	 * @param module (Acrónimo que indica cual servicio se va a facturar, bien sea FE, NE o DS).
	 * @return Object[] que contiene la información de las facturas generadas por la aplicación en el formato requerido 
	 * para generar el archivo plano de Faceldi. 
	 * 
	 */
	@GetMapping("/fe-faceldi/{year}/{month}/{number}/{env}/{module}")
	public Object[] generateTestBills(@PathVariable(name = "year") Long year, @PathVariable(name = "month") Long month, 
			@PathVariable("number") Long invoiceNumber, @PathVariable("module") String module) {
		return billService.generateTestBills(year, month, invoiceNumber, module);
	}
	
	/**
	 * Consulta la información de las facturaciones guardadas en la base de datos (Producción)
	 * o en la memoria (Simulación) según los parámetros dados. 
	 * @param year (Año facturado).
	 * @param month (Mes facturado).
	 * @param environment (S -> Simulation ó P -> Production).
	 * @param module (Acrónimo que indica cual servicio se va a facturar, bien sea FE, NE o DS).
	 * @return Object[] que contiene la información de las facturas generadas por la aplicación en el formato requerido 
	 * para generar el archivo plano de Faceldi. 
	 */
	@GetMapping("/fe-faceldi/{year}/{month}/{env}/{module}")
	public Object[] generateFaceldiFile(@PathVariable(name = "year") Long year, @PathVariable(name = "month") Long month, 
			@PathVariable(name = "env") String environment, @PathVariable("module") String module) {
		return billService.generateFaceldiFile(year, month, environment, module);
	}
	
	/**
	 * Consulta la información de las facturaciones guardadas en la base de datos (Producción)
	 * o en la memoria (Simulación) según los parámetros dados.
	 * @param year (Año facturado).
	 * @param month (Mes facturado).
	 * @param environment (S -> Simulation ó P -> Production).
	 * @param module (Acrónimo que indica cual servicio se va a facturar, bien sea FE, NE o DS).
	 * @return Object[] que contiene la información de las facturas generadas por la aplicación en el formato requerido 
	 * para generar el archivo plano de SMART.
	 */
	@GetMapping("/fe-smart/{year}/{month}/{env}/{module}")
	public Object[] generateSmartFile(@PathVariable(name = "year") Long year, @PathVariable(name = "month") Long month, 
			@PathVariable(name = "env") String environment, @PathVariable("module") String module) {
		return billService.generateSmartFile(year, month, environment, module);
	}

}
