package com.project.shopapp.configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration//chứa các phương thức annotated với @Bean, mỗi phương thức sẽ trả về một đối tượng bean mà Spring sẽ quản lý và inject vào các component khác khi cần thiết.
public class MapperConfiguration {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
