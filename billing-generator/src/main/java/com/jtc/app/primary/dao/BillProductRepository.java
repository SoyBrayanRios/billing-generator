package com.jtc.app.primary.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtc.app.primary.entity.BillProduct;

/**
 * Repositorio que contiene todas las consultas relacionadas con la tabla "bill_product".
 *
 */
public interface BillProductRepository extends JpaRepository<BillProduct, Long> {
	
	/**
	 * Consulta a la base de datos de Kosmos para obtener el detalle de una factura generada a una sucursal
	 * según el ID de la factura.
	 * @param billId (ID de la factura).
	 * @return Lista con los objetos que representan al detalle de la factura que corresponda al ID suministrado.
	 */
	@Query(value = "select * from bill_product where bill=:billId ", nativeQuery = true)
	public List<BillProduct> getByBillId(Long billId);
}
