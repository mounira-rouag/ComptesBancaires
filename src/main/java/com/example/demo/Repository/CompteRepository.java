package com.example.demo.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Compte;

public interface CompteRepository extends JpaRepository<Compte, String>{

	Compte findOne(String codeCpte);

}
