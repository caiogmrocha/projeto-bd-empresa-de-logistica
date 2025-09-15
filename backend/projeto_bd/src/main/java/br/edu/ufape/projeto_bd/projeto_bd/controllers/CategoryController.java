package br.edu.ufape.projeto_bd.projeto_bd.controllers;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.CategoryRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.CategoryResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.services.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("api/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final ICategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO request) {
        CategoryResponseDTO newCategory = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
        CategoryResponseDTO category = categoryService.findCategoryById(id);
        return ResponseEntity.ok(category);
    }

    /**
     * Endpoint para listar todas as categorias de forma paginada.
     * Suporta filtros por busca semelhante a produtos via ?search=.
     * Parâmetros de paginação (page, size, sort) são passados na URL.
     * Ex: /categories?page=0&size=10&sort=name,asc&search=eletron
     */
    @GetMapping
    public ResponseEntity<Page<CategoryResponseDTO>> getAllCategories(Pageable pageable, @RequestParam(required = false) String search) {
        Page<CategoryResponseDTO> categories = categoryService.findAllCategories(pageable, search);
        return ResponseEntity.ok(categories);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequestDTO request) {
        CategoryResponseDTO updatedCategory = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(updatedCategory);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
