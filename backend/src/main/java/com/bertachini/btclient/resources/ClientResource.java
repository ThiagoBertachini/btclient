package com.bertachini.btclient.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bertachini.btclient.dto.ClientDTO;
import com.bertachini.btclient.services.ClientService;

@RestController
@RequestMapping("/clients")
public class ClientResource {
	
	@Autowired
	ClientService service;
	
	@PostMapping
	public ResponseEntity<ClientDTO> insert(@RequestBody ClientDTO clientDTO){
		ClientDTO dto = service.insert(clientDTO);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{/id}")
				.buildAndExpand(clientDTO.getId()).toUri();
		
		return ResponseEntity.created(uri).body(dto);
	}
	
	@GetMapping
    public ResponseEntity<Page<ClientDTO>> findAllPaged(
		   @RequestParam(value = "page", defaultValue = "0") Integer page,
		   @RequestParam(value = "linesPerPage", defaultValue = "6") Integer linesPerPage,
		   @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
		   @RequestParam(value = "direction", defaultValue = "ASC") String direction){
			
	   PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
	   Page<ClientDTO> response = service.findAllPaged(pageRequest);
	   return ResponseEntity.ok().body(response);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<ClientDTO> findById(@PathVariable Long id){
		return ResponseEntity.ok().body(service.findByID(id));
	}
		
	@PutMapping(value = "/{id}")
	public ResponseEntity<ClientDTO> update(@PathVariable Long id, @RequestBody ClientDTO dto){
		ClientDTO updatedDTO = service.update(id, dto);
		return ResponseEntity.ok().body(updatedDTO);
	}
	
	@DeleteMapping(value ="/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
