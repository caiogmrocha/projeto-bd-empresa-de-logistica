package br.edu.ufape.projeto_bd.projeto_bd.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Language;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.LanguageRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/languages")
@RequiredArgsConstructor
@Validated
public class LanguageController {

    private final LanguageRepository languageRepository;

    @GetMapping
    public ResponseEntity<List<Language>> listLanguages() {
        List<Language> langs = languageRepository.findAll();
        return ResponseEntity.ok(langs);
    }
}
