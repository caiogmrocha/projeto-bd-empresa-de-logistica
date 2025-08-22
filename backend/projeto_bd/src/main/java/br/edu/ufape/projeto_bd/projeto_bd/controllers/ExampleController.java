package br.edu.ufape.projeto_bd.projeto_bd.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Example;
import br.edu.ufape.projeto_bd.projeto_bd.domain.services.ExampleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * ExampleController - Template controller showcasing a simple CRUD for newcomers.
 *
 * Endpoints under /api/examples
 */
@RestController
@RequestMapping("/api/examples")
@RequiredArgsConstructor
@Validated
public class ExampleController {
  private final ExampleService exampleService;

  @PostMapping
  public ResponseEntity<Example> create(@Valid @RequestBody Example body) {
    Example created = exampleService.create(body);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @GetMapping
  public List<Example> list() {
    return exampleService.findAll();
  }

  @GetMapping("/{id}")
  public Example get(@PathVariable Integer id) {
    return exampleService.findById(id);
  }

  @PutMapping("/{id}")
  public Example update(@PathVariable Integer id, @Valid @RequestBody Example body) {
    body.setId(id);
    return exampleService.update(body);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    exampleService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
