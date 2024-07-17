package com.project.shopapp.repositories;

import com.project.shopapp.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);// kiểm tra xem có một bản ghi nào đó trong cơ sở dữ liệu có tồn tại với giá trị cụ thể của trường name hay không.
    Page<Product> findAll(Pageable pageable);//phân trang  bao nhiêu phần tử 1 trang
}
