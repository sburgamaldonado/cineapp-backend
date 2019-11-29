package com.mitocode.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mitocode.dto.FiltroConsultaDTO;
import com.mitocode.dto.ResumenVentaDTO;
import com.mitocode.dto.VentaDTO;
import com.mitocode.exception.ModeloNotFoundException;
import com.mitocode.model.Venta;
import com.mitocode.model.VentaComida;
import com.mitocode.service.IVentaService;


@RestController
@RequestMapping("/ventas")
public class VentaController {

	@Autowired
	private IVentaService service;

	/*@PostMapping
	public ResponseEntity<Venta> registrar(@Valid @RequestBody Venta obj) {
		Venta pel = service.registrar(obj);
		return new ResponseEntity<Venta>(pel, HttpStatus.CREATED);
	}*/
	
	@PostMapping
	public ResponseEntity<Object> registrar(@Valid @RequestBody VentaDTO obj) {
		Venta ven = service.registrarTransaccional(obj);
		
		// localhost:8080/ventas/2
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(ven.getIdVenta()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping
	public ResponseEntity<Venta> modificar(@Valid @RequestBody Venta obj) {
		Venta pel = service.modificar(obj);
		return new ResponseEntity<Venta>(pel, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Venta> listarPorId(@PathVariable("id") Integer id) {
		Venta pel = service.listarPorId(id);
		if(pel.getIdVenta() == null) {
			throw new ModeloNotFoundException("ID no existe: " + id);
		}
		return new ResponseEntity<Venta>(pel, HttpStatus.OK); 
	}
	
	/*@GetMapping(value = "/{id}")
	public Resource<Venta> listarPorId(@PathVariable("id") Integer id){
		
		Venta pel = service.listarPorId(id);
		if(pel.getIdVenta() == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO : " + id);
		}
		
		Resource<Venta> resource = new Resource<Venta>(pel);
		// /ventas/{4}
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarPorId(id));
		resource.add(linkTo.withRel("venta-resource"));
		
		return resource;
	}*/

	@GetMapping
	public ResponseEntity<List<Venta>> listar() {
		List<Venta> lista = service.listar();
		return new ResponseEntity<List<Venta>>(lista, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminar(@PathVariable("id") Integer id) {
		service.eliminar(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@PostMapping(value = "/generarReporte", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, consumes = "application/json")
	public ResponseEntity<byte[]> generarReporte(@RequestBody VentaDTO venta) {
		byte[] data = null;
		data = service.generarReporte(venta);
		return new ResponseEntity<byte[]>(data, HttpStatus.OK);
	}
	
	@PostMapping(value = "/buscar", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Venta>> buscar(@RequestBody FiltroConsultaDTO filtro) {
		List<Venta> ventas = new ArrayList<>();

		if (filtro != null) {
			if (filtro.getFechaConsulta() != null) {
				ventas = service.buscarfecha(filtro);
			} else {
				ventas = service.buscar(filtro);
			}
		}
		return new ResponseEntity<List<Venta>>(ventas, HttpStatus.OK);
	}
	
	@GetMapping(value = "/buscar/comidas/{idVenta}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<VentaComida>> listar(@PathVariable("idVenta") Integer idVenta) {
		List<VentaComida> ventaComidas = new ArrayList<>();
		ventaComidas = service.listarComidasPorVenta(idVenta);
		return new ResponseEntity<List<VentaComida>>(ventaComidas, HttpStatus.OK);
	}
	
	@GetMapping(value = "/listarResumen", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ResumenVentaDTO>> listarResumen() {
		List<ResumenVentaDTO> ventas = new ArrayList<>();
		ventas = service.listarResumen();
		return new ResponseEntity<List<ResumenVentaDTO>>(ventas, HttpStatus.OK);
	}
}
