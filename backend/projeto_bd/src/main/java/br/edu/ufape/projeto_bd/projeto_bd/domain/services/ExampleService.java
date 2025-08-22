package br.edu.ufape.projeto_bd.projeto_bd.domain.services;

import java.util.List;

import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Example;

public interface ExampleService {
  Example create(Example example);
  List<Example> findAll();
  Example findById(Integer id);
  Example update(Example example);
  void delete(Integer id);
}

