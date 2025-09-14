package br.edu.ufape.projeto_bd.projeto_bd.domain.services.impl;

import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Category;
import br.edu.ufape.projeto_bd.projeto_bd.domain.exceptions.BusinessRuleException;
import br.edu.ufape.projeto_bd.projeto_bd.domain.exceptions.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.CategoryRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.CategoryResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.mappers.CategoryMapper;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.CategoryRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.services.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    
    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO request) {

        Category category = categoryMapper.toEntity(request);
        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toResponseDTO(savedCategory);

    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDTO findCategoryById(Long id) {

        Category category = findCategoryByIdOrThrow(id);
    
        return categoryMapper.toResponseDTO(category);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponseDTO> findAllCategories(Pageable pageable) {

        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(categoryMapper::toResponseDTO);

    }

    @Override
    @Transactional
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO request) {
        Category existingCategory = findCategoryByIdOrThrow(id);
    
        categoryMapper.updateFromDto(request, existingCategory);

        Category updatedCategory = categoryRepository.save(existingCategory);

        return categoryMapper.toResponseDTO(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category existingCategory = findCategoryByIdOrThrow(id);

        if (!existingCategory.getProducts().isEmpty()) {
            throw new BusinessRuleException(
                "Não é possível excluir a categoria '" + existingCategory.getName() + 
                "' pois ela está associada a " + existingCategory.getProducts().size() + " produto(s)."
            );
        }

        categoryRepository.delete(existingCategory);
    }

    private Category findCategoryByIdOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Category.class, id));
    }
    
}
