package br.edu.ufape.projeto_bd.projeto_bd.domain.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.ProductStockRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.ProductStockResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Product;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.ProductStock;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Warehouse;
import br.edu.ufape.projeto_bd.projeto_bd.domain.mappers.ProductStockMapper;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.ProductRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.ProductStockRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.WarehouseRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.services.IProductStockService;
import br.edu.ufape.projeto_bd.projeto_bd.domain.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import br.edu.ufape.projeto_bd.projeto_bd.domain.exceptions.BusinessRuleException;

@Service
@RequiredArgsConstructor
public class ProductStockService implements IProductStockService {

    private final ProductStockRepository productStockRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductStockMapper productStockMapper;
    
    @Override
    @Transactional
    public ProductStockResponseDTO createProductStock(ProductStockRequestDTO request) {
        
        validateStockUniqueness(request.getProductId(), request.getWarehouseId());
        validateCodeUniqueness(request.getCode());
        
        Product product = findProductByIdOrThrow(request.getProductId());
        Warehouse warehouse = findWarehouseByIdOrThrow(request.getWarehouseId());

        ProductStock newProductStock = productStockMapper.toEntity(request, product, warehouse);
        ProductStock productStockSaved = productStockRepository.save(newProductStock);

        return productStockMapper.toResponseDTO(productStockSaved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductStockResponseDTO findProductStockById(Long id) {

        ProductStock productStock = findProductStockByIdOrThrow(id);

        return productStockMapper.toResponseDTO(productStock);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductStockResponseDTO> findAllProductStocks(Pageable pageable) {
        Page<ProductStock> productStocks = productStockRepository.findAll(pageable);

        return productStocks.map(productStockMapper::toResponseDTO);
    }

    @Override
    @Transactional
    public ProductStockResponseDTO updateProductStock(Long id, ProductStockRequestDTO request) {

        ProductStock productStock = findProductStockByIdOrThrow(id);

        if (!productStock.getProduct().getId().equals(request.getProductId()) || 
            !productStock.getWarehouse().getId().equals(request.getWarehouseId())) {

            validateStockUniqueness(request.getProductId(), request.getWarehouseId());
        }
        
        if (!productStock.getCode().equals(request.getCode())) {
            validateCodeUniqueness(request.getCode());
        }
        
        Product product = findProductByIdOrThrow(request.getProductId());
        Warehouse warehouse = findWarehouseByIdOrThrow(request.getWarehouseId());

        productStockMapper.updateProductStockFromDto(request, product, warehouse, productStock);
        ProductStock productStockUpdated = productStockRepository.save(productStock);

        return productStockMapper.toResponseDTO(productStockUpdated);
    }

    @Override
    @Transactional
    public void deleteProductStock(Long id) {
        ProductStock productStock = productStockRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ProductStock.class, id));

        productStockRepository.delete(productStock);
    }

    // --------- métodos privados ---------

    private void validateStockUniqueness(Long productId, Long warehouseId) {

        if (productStockRepository.existsByProductIdAndWarehouseId(productId, warehouseId)) {
            throw new BusinessRuleException("Já existe um registro de estoque para este produto neste armazém.");
            
        }

    }

    private void validateCodeUniqueness(String code) {
        if (productStockRepository.existsByCode(code)) {
            throw new BusinessRuleException("O código de estoque '" + code + "' já está em uso.");
        }
    }

    private ProductStock findProductStockByIdOrThrow(Long id) {

        return productStockRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ProductStock.class, id));

    }

    private Product findProductByIdOrThrow(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Product.class, id));

    }

    private Warehouse findWarehouseByIdOrThrow(Long id) {

        return warehouseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Warehouse.class, id));
                
    }
}
