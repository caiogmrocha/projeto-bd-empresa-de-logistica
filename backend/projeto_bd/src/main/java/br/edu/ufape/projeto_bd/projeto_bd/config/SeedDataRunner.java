package br.edu.ufape.projeto_bd.projeto_bd.config;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.javafaker.Faker;

import br.edu.ufape.projeto_bd.projeto_bd.domain.entities.*;
import br.edu.ufape.projeto_bd.projeto_bd.domain.enums.ProductStatus;
import br.edu.ufape.projeto_bd.projeto_bd.domain.enums.SupplierType;
import br.edu.ufape.projeto_bd.projeto_bd.domain.repositories.*;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Profile({"dev", "default"})
public class SeedDataRunner implements CommandLineRunner {
    private final CategoryRepository categoryRepository;
    private final LanguageRepository languageRepository;
    private final WarehouseRepository warehouseRepository;
    private final SupplierRepository supplierRepository;
    private final NaturalPersonRepository naturalPersonRepository;
    private final LegalEntityRepository legalEntityRepository;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ProductTranslationRepository productTranslationRepository;
    private final ProductStockRepository productStockRepository;
    private final JdbcTemplate jdbcTemplate;

    private final Faker faker = new Faker();
    private final Random random = new Random();

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        seedLanguagesIfEmpty();
        seedCategoriesIfEmpty(12);
        seedCompaniesIfEmpty(3);
        seedCustomersIfEmpty(6);
        seedWarehousesIfEmpty(3);
        seedSuppliersIfEmpty(8);
        seedProductsIfEmpty(20);
        seedOrdersIfEmpty(8); // also seeds orders_products
        seedDeliveriesIfEmpty(5);
    }

    private void seedLanguagesIfEmpty() {
        if (languageRepository.count() > 0) return;
        // Fallback minimal languages in case Flyway seed didn't run
        Language pt = new Language();
        pt.setLanguageName("Português");
        pt.setIsoCode("pt");
        Language en = new Language();
        en.setLanguageName("English");
        en.setIsoCode("en");
        languageRepository.saveAll(List.of(pt, en));
    }

    private void seedCategoriesIfEmpty(int howMany) {
        if (categoryRepository.count() > 0) return;
        List<Category> list = new ArrayList<>();
        for (int i = 0; i < howMany; i++) {
            Category c = new Category();
            c.setName(faker.commerce().department() + " " + (i+1));
            c.setDescription(faker.lorem().sentence());
            list.add(c);
        }
        categoryRepository.saveAll(list);
    }

    private Address fakeAddress() {
        Address a = new Address();
        a.setCountry("BR");
        a.setState(faker.address().stateAbbr());
        a.setCity(faker.address().city());
        a.setStreet(faker.address().streetName());
        a.setNumber(String.valueOf(faker.number().numberBetween(1, 9999)));
        a.setZipCode(faker.address().zipCode());
        return a;
    }

    private void seedCompaniesIfEmpty(int howMany) {
        if (companyRepository.count() > 0) return;
        List<Company> list = new ArrayList<>();
        for (int i = 0; i < howMany; i++) {
            Company c = new Company();
            c.setLegalName(faker.company().name());
            c.setTradeName(faker.company().industry() + " " + faker.company().suffix());
            c.setCnpj(fakeCnpj());
            c.setPhones(List.of(faker.phoneNumber().cellPhone(), faker.phoneNumber().phoneNumber()));
            c.setEmails(List.of(faker.internet().emailAddress(), faker.internet().safeEmailAddress()));
            c.setAddress(fakeAddress());
            list.add(c);
        }
        companyRepository.saveAll(list);
    }

    private void seedCustomersIfEmpty(int howMany) {
        if (customerRepository.count() > 0) return;
        List<Customer> list = new ArrayList<>();
        for (int i = 0; i < howMany; i++) {
            Customer c = new Customer();
            c.setName(faker.name().fullName());
            c.setAddress(fakeAddress());
            c.setCreditLimit(new BigDecimal(faker.number().numberBetween(500, 10_000)));
            list.add(c);
        }
        customerRepository.saveAll(list);
    }

    private void seedWarehousesIfEmpty(int howMany) {
        if (warehouseRepository.count() > 0) return;
        List<Warehouse> list = new ArrayList<>();
        for (int i = 0; i < howMany; i++) {
            Warehouse w = new Warehouse();
            w.setName("Armazém " + (i+1) + " - " + faker.address().cityName());
            w.setAddress(fakeAddress());
            list.add(w);
        }
        warehouseRepository.saveAll(list);
    }

    private String onlyDigits(String s) { return s.replaceAll("\\D", ""); }
    private String fakeCpf() {
        // Simple random numeric string for demo (not a valid CPF algorithm)
        return String.format("%011d", Math.abs(random.nextLong()) % 1_000_000_00000L);
    }
    private String fakeCnpj() {
        return String.format("%014d", Math.abs(random.nextLong()) % 100_000_000_000_000L);
    }

    private void seedSuppliersIfEmpty(int howMany) {
        if (supplierRepository.count() > 0) return;
        int half = Math.max(1, howMany / 2);
        for (int i = 0; i < half; i++) {
            NaturalPerson p = new NaturalPerson();
            p.setName(faker.name().fullName());
            p.setSupplierType(SupplierType.NATURAL_PERSON);
            p.setAddress(fakeAddress());
            // ensure unique
            String cpf;
            do { cpf = onlyDigits(fakeCpf()); } while (naturalPersonRepository.existsByCpf(cpf));
            p.setCpf(cpf);
            naturalPersonRepository.save(p);
        }
        for (int i = 0; i < howMany - half; i++) {
            LegalEntity e = new LegalEntity();
            e.setName(faker.company().name());
            e.setSupplierType(SupplierType.LEGAL_ENTITY);
            e.setAddress(fakeAddress());
            String cnpj;
            do { cnpj = onlyDigits(fakeCnpj()); } while (legalEntityRepository.existsByCnpj(cnpj));
            e.setCnpj(cnpj);
            legalEntityRepository.save(e);
        }
    }

    private void seedProductsIfEmpty(int howMany) {
        if (productRepository.count() > 0) return;
        List<Category> categories = categoryRepository.findAll();
        List<Warehouse> warehouses = warehouseRepository.findAll();
        Optional<Language> pt = languageRepository.findByIsoCode("pt");
        Optional<Language> en = languageRepository.findByIsoCode("en");
        if (warehouses.isEmpty()) return; // need at least one warehouse

        for (int i = 0; i < howMany; i++) {
            Product p = new Product();
            p.setWarranty_date(LocalDateTime.now().plusDays(faker.number().numberBetween(30, 365)));
            p.setStatus(random.nextBoolean() ? ProductStatus.TESTED : ProductStatus.RETURNED);
            p.setMinimumSalePrice(new BigDecimal(faker.commerce().price(50, 1500)));
            // set some categories
            Set<Category> pick = new HashSet<>();
            if (!categories.isEmpty()) {
                pick.add(categories.get(random.nextInt(categories.size())));
                if (categories.size() > 1 && random.nextBoolean()) {
                    pick.add(categories.get(random.nextInt(categories.size())));
                }
            }
            p.setCategories(pick);
            p = productRepository.save(p);

            // translations
            List<ProductTranslation> trs = new ArrayList<>();
            if (pt.isPresent()) {
                ProductTranslation t = new ProductTranslation();
                t.setId(new ProductTranslationId(p.getId(), pt.get().getId()));
                t.setProduct(p);
                t.setLanguage(pt.get());
                t.setName(faker.commerce().productName());
                t.setDescription(faker.lorem().sentence());
                trs.add(t);
            }
            if (en.isPresent()) {
                ProductTranslation t = new ProductTranslation();
                t.setId(new ProductTranslationId(p.getId(), en.get().getId()));
                t.setProduct(p);
                t.setLanguage(en.get());
                t.setName(faker.commerce().productName());
                t.setDescription(faker.lorem().sentence());
                trs.add(t);
            }
            productTranslationRepository.saveAll(trs);

            // product stock (one per first warehouse)
            Warehouse w = warehouses.get(0);
            ProductStock ps = new ProductStock();
            ps.setProduct(p);
            ps.setWarehouse(w);
            ps.setCode("STK-" + p.getId() + "-" + w.getId());
            ps.setAmount((long) faker.number().numberBetween(0, 300));
            productStockRepository.save(ps);
        }
    }

    private void seedOrdersIfEmpty(int howMany) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM orders", Integer.class);
        if (count != null && count > 0) return;
        List<Long> customerIds = customerRepository.findAll().stream().map(Customer::getId).toList();
        List<Product> products = productRepository.findAll();
        if (customerIds.isEmpty() || products.isEmpty()) return;

        for (int i = 0; i < howMany; i++) {
            Long customerId = customerIds.get(random.nextInt(customerIds.size()));
            String method = random.nextBoolean() ? "online" : "in_person";
            String status = switch (random.nextInt(5)) {
                case 0 -> "pending";
                case 1 -> "processing";
                case 2 -> "shipped";
                case 3 -> "delivered";
                default -> "canceled";
            };

            KeyHolder kh = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO orders (order_method, status, customers_id, ordered_at, expected_to_deliver_at, created_at, updated_at) VALUES (?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, method);
                ps.setString(2, status);
                ps.setLong(3, customerId);
                LocalDateTime orderedAt = LocalDateTime.now().minusDays(random.nextInt(10));
                LocalDateTime expected = orderedAt.plusDays(3 + random.nextInt(10));
                ps.setObject(4, orderedAt);
                ps.setObject(5, expected);
                ps.setObject(6, LocalDateTime.now());
                ps.setObject(7, LocalDateTime.now());
                return ps;
            }, kh);
            Number key = kh.getKey();
            if (key == null) continue;
            long orderId = key.longValue();

            int items = 1 + random.nextInt(3);
            for (int it = 0; it < items; it++) {
                Product p = products.get(random.nextInt(products.size()));
                long amount = 1 + random.nextInt(5);
                BigDecimal min = p.getMinimumSalePrice();
                BigDecimal salePrice = min.add(BigDecimal.valueOf(random.nextInt(1000) / 10.0));
                jdbcTemplate.update(
                    "INSERT INTO orders_products (orders_id, products_id, amount, sale_price) VALUES (?,?,?,?)",
                    orderId, p.getId(), amount, salePrice
                );
            }
        }
    }

    private void seedDeliveriesIfEmpty(int howMany) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM deliveries", Integer.class);
        if (count != null && count > 0) return;
        List<Company> companies = companyRepository.findAll();
        List<Warehouse> warehouses = warehouseRepository.findAll();
        Integer anyOrderId = jdbcTemplate.queryForObject("SELECT MIN(id) FROM orders", Integer.class);
        if (companies.isEmpty() || warehouses.isEmpty() || anyOrderId == null) return;

        for (int i = 0; i < howMany; i++) {
            Company c = companies.get(random.nextInt(companies.size()));
            Warehouse w = warehouses.get(random.nextInt(warehouses.size()));
            long orderId = jdbcTemplate.queryForObject("SELECT id FROM orders ORDER BY RAND() LIMIT 1", Long.class);
            // create a destination address
            Address a = fakeAddress();
            // persist address to get id
            KeyHolder kh = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO addresses (country, state, city, street, number, zip_code, created_at, updated_at) VALUES (?,?,?,?,?,?,NOW(),NOW())",
                    Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, a.getCountry());
                ps.setString(2, a.getState());
                ps.setString(3, a.getCity());
                ps.setString(4, a.getStreet());
                ps.setString(5, a.getNumber());
                ps.setString(6, a.getZipCode());
                return ps;
            }, kh);
            long addressId = kh.getKey().longValue();

            jdbcTemplate.update(
                "INSERT INTO deliveries (companies_id, orders_id, price, status, addresses_destination_id, warehouses_source_id, created_at, updated_at) VALUES (?,?,?,?,?,?,NOW(),NOW())",
                c.getId(), orderId, BigDecimal.valueOf(faker.number().numberBetween(10, 300)), "processing", addressId, w.getId()
            );
        }
    }
}
