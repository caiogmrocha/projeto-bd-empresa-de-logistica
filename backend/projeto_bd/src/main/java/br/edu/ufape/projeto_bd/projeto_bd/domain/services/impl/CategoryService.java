package br.edu.ufape.projeto_bd.projeto_bd.domain.services.impl;

import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Category;
import br.edu.ufape.projeto_bd.projeto_bd.domain.exceptions.BusinessRuleException;
import br.edu.ufape.projeto_bd.projeto_bd.domain.exceptions.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    public Page<CategoryResponseDTO> findAllCategories(Pageable pageable, String search) {
        Page<Category> categories;
        if (search == null || search.trim().isEmpty()) {
            categories = categoryRepository.findAll(pageable);
        } else {
            String term = search.trim();
            Specification<Category> spec = buildSearchSpecification(term);
            categories = categoryRepository.findAll(spec, pageable);
        }
        return categories.map(categoryMapper::toResponseDTO);

    }

    private Specification<Category> buildSearchSpecification(String term) {
        return (root, query, cb) -> {
            Long idValue = null;
            try { idValue = Long.parseLong(term); } catch (NumberFormatException ignored) {}

            var predicates = cb.disjunction();
            if (idValue != null) {
                predicates.getExpressions().add(cb.equal(root.get("id"), idValue));
            }
            // name LIKE %term%
            predicates.getExpressions().add(cb.like(cb.lower(root.get("name")), "%" + term.toLowerCase() + "%"));
            // description LIKE %term% (if not null)
            predicates.getExpressions().add(cb.like(cb.lower(cb.coalesce(root.get("description"), "")), "%" + term.toLowerCase() + "%"));
            return predicates;
        };
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
