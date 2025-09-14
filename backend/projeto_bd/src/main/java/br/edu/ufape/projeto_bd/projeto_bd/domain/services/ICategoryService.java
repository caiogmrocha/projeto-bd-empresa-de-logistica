package br.edu.ufape.projeto_bd.projeto_bd.domain.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.CategoryRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.CategoryResponseDTO;

public interface ICategoryService {
    public CategoryResponseDTO createCategory (CategoryRequestDTO request);

    public CategoryResponseDTO findCategoryById(Long id);

    Page<CategoryResponseDTO> findAllCategories(Pageable pageable);

    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO request);

    public void deleteCategory(Long id);
}