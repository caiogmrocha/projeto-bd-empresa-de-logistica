package br.edu.ufape.projeto_bd.projeto_bd.domain.services.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Example;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.ExampleRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.services.ExampleService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ExampleServiceImpl implements ExampleService {
  private final ExampleRepository repository;

  @Override
  public Example create(Example example) {
    example.setId(null);
    return repository.save(example);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Example> findAll() {
    return repository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Example findById(Integer id) {
    return repository.findById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Example not found"));
  }

  @Override
  public Example update(Example example) {
    if (example.getId() == null || !repository.existsById(example.getId())) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Example not found");
    }
    return repository.save(example);
  }

  @Override
  public void delete(Integer id) {
    Example existing = findById(id);
    repository.delete(existing);
  }
}

