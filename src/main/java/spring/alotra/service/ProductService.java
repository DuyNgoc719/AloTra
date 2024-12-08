package spring.alotra.service;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
=======
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
>>>>>>> 2ae07abf677d712ef8580f875064008b3c87ee31
import org.springframework.stereotype.Service;
import spring.alotra.entity.*;
import spring.alotra.repository.*;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;


    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProduct(long id) {
        productRepository.deleteById(id);
    }
    public Page<Product> findProductsByPriceLessThanEqual(double price, Pageable  page) {

        return productRepository.findByPricesLessThanEqual(price, page);
    }

}
