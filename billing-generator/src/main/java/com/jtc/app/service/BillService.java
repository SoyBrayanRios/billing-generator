package com.jtc.app.service;

import java.util.List;

import com.jtc.app.primary.entity.Bill;
import com.jtc.app.primary.entity.BillProduct;
import com.jtc.app.primary.entity.JsonResponse;

/**
 * Esta interface define los servicios relacionados a la clase Bill.
 *
 */
public interface BillService {

	public Bill saveBill(Bill bill) throws Exception;
	public List<Bill> getBills();
	public void deleteBill(Long billId);
	public Bill getBillById(Long billId);
	public List<Bill> getFaceldiReport(Long year, Long month);
	
	/**
	 * Permite realizar todo el cálculo de la facturación mensual de un servicio.
	 * @param year (Año de facturación).
	 * @param month (Mes de facturación).
	 * @param invoiceNumber (Nùmero inicial de la facturación de ese mes).
	 * @param environment (S para simulación y P para producción).
	 * @param module (Acrónimo que indica el servicio que se va a facturar, bien sea FE, NE o DS).
	 * @return El mensaje de confirmación indicando que se generó la facturación correctamente.  
	 */
	public JsonResponse generateBills(Long year, Long month, Long invoiceNumber, String environment, String module);
	
	/**
	 * Permite realizar todo el cálculo de la facturación mensual de prueba de un servicio.
	 * @param year (Año de facturación).
	 * @param month (Mes de facturación).
	 * @param invoiceNumber (Nùmero inicial de la facturación de ese mes).
	 * @param module (Acrónimo que indica el servicio que se va a facturar, bien sea FE, NE o DS).
	 * @return Array con la información de todas las facturas generadas en el mes.  
	 */
	public Object[] generateTestBills(Long year, Long month, Long invoiceNumber, String module);
	
	/**
	 * Permite estructurar la información requerida para el arme del archivo plano de faceldi.
	 * @param year (Año de facturación).
	 * @param month (Mes de facturación).
	 * @param simulation (S para simulación y P para producción).
	 * @param module (Acrónimo que indica el servicio que se va a facturar, bien sea FE, NE o DS).
	 * @return Array con la información de todas las lìneas que componen el archivo plano para cargue en Faceldi.
	 */
	public Object[] generateFaceldiFile(Long year, Long month, String simulation, String module);
	
	/**
	 * Permite estructurar la información requerida para el arme de los archivos planos de SMART.
	 * @param year (Año de facturación).
	 * @param month (Mes de facturación).
	 * @param simulation (S para simulación y P para producción).
	 * @param module (Acrónimo que indica el servicio que se va a facturar, bien sea FE, NE o DS).
	 * @return Array con la información de todas las lìneas que componen los archivos planos para cargue en SMART.
	 */
	public Object[] generateSmartFile(Long year, Long month, String simulation, String module);
	
	public Object[] getSmartFile();
}
