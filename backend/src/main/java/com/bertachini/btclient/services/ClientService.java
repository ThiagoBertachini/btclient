package com.bertachini.btclient.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bertachini.btclient.dto.ClientDTO;
import com.bertachini.btclient.entities.Client;
import com.bertachini.btclient.repositories.ClientRepository;
import com.bertachini.btclient.services.exceptions.DataBaseException;
import com.bertachini.btclient.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {
	
	@Autowired
	ClientRepository repository;
	
	@Transactional(readOnly = true)
	public ClientDTO insert(ClientDTO dto) {
		Client entity = new Client();
		copyDtoToClient(dto, entity);
		entity = repository.save(entity);
		return new ClientDTO(entity);
	}

	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {
		Page<Client> list = repository.findAll(pageRequest);
		return list.map(clientPage -> new ClientDTO(clientPage));
	}
	
	@ExceptionHandler
	@Transactional(readOnly = true)
	public ClientDTO findByID(Long id) {
		Optional<Client> obj = repository.findById(id);
		
		//tenta buscar obj dentro do optional, se define uma
		//chamada de uma excessao Com uma funcao lambda que leva 
		//para uma nova excessao.
		Client entity = obj.orElseThrow(
				() -> new ResourceNotFoundException("Entity not found"));
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
		Client entity = new Client();
		//getone não "toca" no banco de dados
		entity = repository.getOne(id);
		copyDtoToClient(dto, entity);
		entity = repository.save(entity);
		return new ClientDTO(entity);
		}catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("id -" + id + "- not found");
		}
	}
	
	public void delete(Long id) {
		try {
		repository.deleteById(id);
		}catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("id not found");
		}catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity violation");
			// TODO: handle exception
		}
	} 
	
	
	private void copyDtoToClient(ClientDTO dto, Client entity) {
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		entity.setBirthDate(dto.getBirthDate());
		entity.setChildren(dto.getChildren());
	}




}
