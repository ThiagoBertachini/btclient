package com.bertachini.btclient.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bertachini.btclient.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long>{
	
	

}
