package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder//hàm khởi tạo thành phần thuộc tính
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//tự tăng lên 1 để k trùng
    private Long id;

    @Column(name = "name", nullable = false)//name trong db k đc null
    private String name;
}

