package br.edu.ufape.projeto_bd.projeto_bd.domain.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.edu.ufape.projeto_bd.projeto_bd.domain.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.RequestDTO.CustomerRequestDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.dtos.ResponseDTO.CustomerResponseDTO;
import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.Customer;
import br.edu.ufape.projeto_bd.projeto_bd.domain.mappers.CustomerMapper;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.CustomerRepository;
import br.edu.ufape.projeto_bd.projeto_bd.domain.services.ICustomerService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {
    
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional
    public CustomerResponseDTO createCustomer(CustomerRequestDTO request) {
        Customer customerToCreate = customerMapper.toEntity(request);
        Customer createdCustomer = customerRepository.save(customerToCreate);
        return customerMapper.toResponseDTO(createdCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDTO findCustomerById(Long id) {
        Customer searchedCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Customer.class, id));
        return customerMapper.toResponseDTO(searchedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponseDTO> findAllCustomers(Pageable pageable) {
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        return customerPage.map(customerMapper::toResponseDTO);
    }

    @Override
    @Transactional		
    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Customer.class, id));
        
        customerMapper.updateCustomerFromDto(request, customer);

        Customer updatedCustomer = customerRepository.save(customer);

        return customerMapper.toResponseDTO(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Customer.class, id));
    
        customerRepository.delete(customer);
    }
}