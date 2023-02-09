package com.jtc.app.primary.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FaceldiReportRow {
	
	private static final Integer fcTipoOperacion = 10;
	private static final String fcPrefijo = "";
	private Long fcConsecutivo;
	private Date fcFechaDocumento;
	private Date fcFechaVencimiento;
	private static final String fcTipoDocumento = "01";
	private String fcObservacionDocumento;
	private static final String fcDivisa = "COP";
	private static final Integer fcTasaCambio = 0;
	private Date fcFechaTasaCambio;
	private Date fcFechaInicioPeriodoFacturacion;
	private Date fcFechaFinPeriodoFacturacion;
	private static final String ntFacturaAplicada = "";
	private static final String ntConceptoNotas = "";
	private String fcReferenciaOrdenCompra;
	private static final String fcOrdenPedido = "";
	private static final String fcOrdenEntrega = "";
	private static final String fcReferenciaTransaccion = "";
	private static final String fcNumeroConvenio = "";
	private static final String fcNumeroPedido = "";
	private static final String fcDocumentoReferenciado = "";
	private static final Integer emCodigoSucursal = 1;
	private static final Integer fcFormaPago = 2;
	private static final Integer fcMedioPago = 41;
	private static final Integer fcIdAnticipo = 0;
	private static final Integer fcValorAnticipo = 0;
	private static final String fcFechaAnticipo = "";
	private static final Integer fcPorcentajeDescuento = 0;
	private static final String fcCodigoDescuento = "09";
	private static final Integer fcPorcentajeCargo = 0;
	private Long clTipoOrganizacion;
	private Long clTipoRegimen;
	private String clNombreComercial;
	private String clCodigoDepMunicipio;
	private static final String clCodigoPais = "CO";
	private String clCodigoPostal;
	private String clDireccion;
	private String clNombreLegal;
	private Long clTipoIdAdquiriente;
	private String clNumeroIdAdquiriente;
	private static final Integer clCodAdquiriente = 1;
	private static final String clCodigoRespFiscales = "ZZ";
	private String clCodigoMunicipio;
	private String clCodigoPostal2;
	private String clDireccion2;
	private static final String clCodigoPais2 = "CO";
	private static final String clMatriculaMercantil = "";
	private static final String clNombreContacto = "No aplica";
	private String clTelefono;
	private String clCelular;
	private String clCorreo; //Add the finance ones
	private Long prCodigoProducto;
	private String prDescripcion;
	private String prDescripcionAdicional;
	private static final String prInformacionAdicionalNombre = "";
	private static final String prInformacionAdicionalValor = "";
	private Long prPrecioUnitario;
	private Long prCantidad;
	private static final Integer prUnidadMedida = 94;
	private static final Integer prPorcentajeDescuento = 0;
	private static final Integer prPorcentajeCargo = 0;
	private static final String prRazonDescuento = "";
	private Long prBaseGravable;
	private static final Integer prIva = 19;
	private static final Integer prIc = 0;
	private static final Integer prIca = 0;
	private static final Integer prInc = 0;
	private static final Integer prReteIva = 0;
	private static final Integer prReteFuente = 0;
	private static final Integer prReteIca = 0;
	private static final Integer prFtoHorticultura = 0;
	private static final Integer prTimbre = 0;
	private static final Integer prBolsas = 0;
	private static final Integer prIncarbono = 0;
	private static final Integer prInCombustibles = 0;
	private static final Integer prSobretasaCombustibles = 0;
	private static final Integer prSordicom = 0;
	private static final Integer prOtrasContribuciones = 0;
	private static final String prMarcaProducto = "";
	private static final String prModeloProducto = "";
	private static final String prTipoIdMandante = "";
	private static final String prNumeroIdMandante = "";
	private static final String expCondicionesEntrega = "";
	private static final String expSubpartidaArancelaria = "";
	private static final String campo85 = "";
	private static final String entDepMunicipio = "";
	private static final String entCodigoPostal = "";
	private static final String entDireccionEntrega = "";
	private static final String campo89 = "";
	private static final String otrElaborado = "";
	private static final String otrRevisado = "";
	private static final String otrVendedor = "";
	private static final String fcObservacionesItem = "";
	private static final String campo94 = "";
	private static final String campo95 = "";
	private static final String campo96 = "";
	private static final String campo97 = "";
	
	public static Integer getFctipooperacion() {
		return fcTipoOperacion;
	}
	public static String getFcprefijo() {
		return fcPrefijo;
	}
	public static String getFctipodocumento() {
		return fcTipoDocumento;
	}
	public static String getFcdivisa() {
		return fcDivisa;
	}
	public static Integer getFctasacambio() {
		return fcTasaCambio;
	}
	public static String getNtfacturaaplicada() {
		return ntFacturaAplicada;
	}
	public static String getNtconceptonotas() {
		return ntConceptoNotas;
	}
	public static String getFcordenpedido() {
		return fcOrdenPedido;
	}
	public static String getFcordenentrega() {
		return fcOrdenEntrega;
	}
	public static String getFcreferenciatransaccion() {
		return fcReferenciaTransaccion;
	}
	public static String getFcnumeroconvenio() {
		return fcNumeroConvenio;
	}
	public static String getFcnumeropedido() {
		return fcNumeroPedido;
	}
	public static String getFcdocumentoreferenciado() {
		return fcDocumentoReferenciado;
	}
	public static Integer getEmcodigosucursal() {
		return emCodigoSucursal;
	}
	public static Integer getFcformapago() {
		return fcFormaPago;
	}
	public static Integer getFcmediopago() {
		return fcMedioPago;
	}
	public static Integer getFcidanticipo() {
		return fcIdAnticipo;
	}
	public static Integer getFcvaloranticipo() {
		return fcValorAnticipo;
	}
	public static String getFcfechaanticipo() {
		return fcFechaAnticipo;
	}
	public static Integer getFcporcentajedescuento() {
		return fcPorcentajeDescuento;
	}
	public static String getFccodigodescuento() {
		return fcCodigoDescuento;
	}
	public static Integer getFcporcentajecargo() {
		return fcPorcentajeCargo;
	}
	public static String getClcodigopais() {
		return clCodigoPais;
	}
	public static Integer getClcodadquiriente() {
		return clCodAdquiriente;
	}
	public static String getClcodigorespfiscales() {
		return clCodigoRespFiscales;
	}
	public static String getClcodigopais2() {
		return clCodigoPais2;
	}
	public static String getClmatriculamercantil() {
		return clMatriculaMercantil;
	}
	public static String getClnombrecontacto() {
		return clNombreContacto;
	}
	public static String getPrinformacionadicionalnombre() {
		return prInformacionAdicionalNombre;
	}
	public static String getPrinformacionadicionalvalor() {
		return prInformacionAdicionalValor;
	}
	public static Integer getPrunidadmedida() {
		return prUnidadMedida;
	}
	public static Integer getPrporcentajedescuento() {
		return prPorcentajeDescuento;
	}
	public static Integer getPrporcentajecargo() {
		return prPorcentajeCargo;
	}
	public static String getPrrazondescuento() {
		return prRazonDescuento;
	}
	public static Integer getPriva() {
		return prIva;
	}
	public static Integer getPric() {
		return prIc;
	}
	public static Integer getPrica() {
		return prIca;
	}
	public static Integer getPrinc() {
		return prInc;
	}
	public static Integer getPrreteiva() {
		return prReteIva;
	}
	public static Integer getPrretefuente() {
		return prReteFuente;
	}
	public static Integer getPrreteica() {
		return prReteIca;
	}
	public static Integer getPrftohorticultura() {
		return prFtoHorticultura;
	}
	public static Integer getPrtimbre() {
		return prTimbre;
	}
	public static Integer getPrbolsas() {
		return prBolsas;
	}
	public static Integer getPrincarbono() {
		return prIncarbono;
	}
	public static Integer getPrincombustibles() {
		return prInCombustibles;
	}
	public static Integer getPrsobretasacombustibles() {
		return prSobretasaCombustibles;
	}
	public static Integer getPrsordicom() {
		return prSordicom;
	}
	public static Integer getProtrascontribuciones() {
		return prOtrasContribuciones;
	}
	public static String getPrmarcaproducto() {
		return prMarcaProducto;
	}
	public static String getPrmodeloproducto() {
		return prModeloProducto;
	}
	public static String getPrtipoidmandante() {
		return prTipoIdMandante;
	}
	public static String getPrnumeroidmandante() {
		return prNumeroIdMandante;
	}
	public static String getExpcondicionesentrega() {
		return expCondicionesEntrega;
	}
	public static String getExpsubpartidaarancelaria() {
		return expSubpartidaArancelaria;
	}
	public static String getCampo85() {
		return campo85;
	}
	public static String getEntdepmunicipio() {
		return entDepMunicipio;
	}
	public static String getEntcodigopostal() {
		return entCodigoPostal;
	}
	public static String getEntdireccionentrega() {
		return entDireccionEntrega;
	}
	public static String getCampo89() {
		return campo89;
	}
	public static String getOtrelaborado() {
		return otrElaborado;
	}
	public static String getOtrrevisado() {
		return otrRevisado;
	}
	public static String getOtrvendedor() {
		return otrVendedor;
	}
	public static String getFcobservacionesitem() {
		return fcObservacionesItem;
	}
	public static String getCampo94() {
		return campo94;
	}
	public static String getCampo95() {
		return campo95;
	}
	public static String getCampo96() {
		return campo96;
	}
	public static String getCampo97() {
		return campo97;
	}
	@Override
	public String toString() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		return  fcTipoOperacion + ";" + fcPrefijo + ";" + fcConsecutivo + ";" + sf.format(fcFechaDocumento)
				+ ";" + sf.format(fcFechaVencimiento) + ";" + fcTipoDocumento + ";" + fcObservacionDocumento
				+ ";" + fcDivisa + ";" + fcTasaCambio + ";" + sf.format(fcFechaTasaCambio) + ";" + sf.format(fcFechaInicioPeriodoFacturacion)
				+ ";" + sf.format(fcFechaFinPeriodoFacturacion) + ";" + ntFacturaAplicada + ";" + ntConceptoNotas + ";" + fcReferenciaOrdenCompra
				+ ";" + fcOrdenPedido + ";" + fcOrdenEntrega + ";" + fcReferenciaTransaccion + ";" + fcNumeroConvenio 
				+ ";" + fcNumeroPedido + ";" + fcDocumentoReferenciado + ";" + emCodigoSucursal + ";" + fcFormaPago
				+ ";" + fcMedioPago + ";" + fcIdAnticipo + ";" + fcValorAnticipo + ";" + fcFechaAnticipo + ";" + fcPorcentajeDescuento
				+ ";" + fcCodigoDescuento + ";" + fcPorcentajeCargo + ";" + clTipoOrganizacion + ";" + clTipoRegimen
				+ ";" + clNombreComercial + ";" + clCodigoDepMunicipio + ";" + clCodigoPais + ";" + clCodigoPostal + ";" + clDireccion
				+ ";" + clNombreLegal + ";" + clTipoIdAdquiriente + ";" + clNumeroIdAdquiriente + ";" + clCodAdquiriente
				+ ";" + clCodigoRespFiscales + ";" + clCodigoMunicipio + ";" + clCodigoPostal2 + ";" + clDireccion2
				+ ";" + clCodigoPais2 + ";" + clMatriculaMercantil + ";" + clNombreContacto + ";" + clTelefono + ";" + clCelular
				+ ";" + clCorreo + ";" + prCodigoProducto + ";" + prDescripcion + ";" + prDescripcionAdicional
				+ ";" + prInformacionAdicionalNombre + ";" + prInformacionAdicionalValor + ";" + prPrecioUnitario + ";" + prCantidad
				+ ";" + prUnidadMedida + ";" + prPorcentajeDescuento + ";" + prPorcentajeCargo + ";" + prRazonDescuento
				+ ";" + prBaseGravable + ";" + prIva + ";" + prIc + ";" + prIca + ";" + prInc + ";" + prReteIva
				+ ";" + prReteFuente + ";" + prReteIca + ";" + prFtoHorticultura + ";" + prTimbre + ";" + prBolsas + ";" + prIncarbono
				+ ";" + prInCombustibles + ";" + prSobretasaCombustibles + ";" + prSordicom + ";" + prOtrasContribuciones
				+ ";" + prMarcaProducto + ";" + prModeloProducto + ";" + prTipoIdMandante + ";" + prNumeroIdMandante
				+ ";" + expCondicionesEntrega + ";" + expSubpartidaArancelaria + ";" + campo85 + ";" + entDepMunicipio
				+ ";" + entCodigoPostal + ";" + entDireccionEntrega + ";" + campo89 + ";" + otrElaborado + ";" + otrRevisado
				+ ";" + otrVendedor + ";" + fcObservacionesItem + ";" + campo94 + ";" + campo95 + ";" + campo96 + ";" + campo97;
	}	
	
}
