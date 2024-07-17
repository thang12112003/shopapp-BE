package com.project.shopapp.controllers;
import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.dtos.OrderDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    @PostMapping("") //http://localhost:8088/api/v1/orders
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult result
    ) {
       try{
           if(result.hasErrors()) {////Kiểm tra xem có lỗi kiểm tra dữ liệu nào không
               List<String> errorMessages = result.getFieldErrors()////Lấy danh sách các FieldError từ BindingResult.
                       .stream()////Tạo một luồng (stream) từ danh sách các FieldError.
                       .map(FieldError::getDefaultMessage)////Lấy thông báo lỗi mặc định từ mỗi FieldError.
                       .toList();////Thu thập các thông báo lỗi vào một danh sách.
               return ResponseEntity.badRequest().body(errorMessages);////sử ding để trả về một phản hồi HTTP với mã trạng thái 400 (Bad Request) cùng với một nội dung cụ thể trong phần thân của phản hồi (body).
           }
           return ResponseEntity.ok("createOrder successfully");
       } catch (Exception e){
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }

    @GetMapping("/{user_id}")//Thêm biến đường dẫn
    //Get http://localhost:8088/api/v1/orders/4
    public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long userId){
        try {
            return ResponseEntity.ok("lấy ra danh sách order từ user_id");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    //Put http://localhost:8088/api/v1/orders/4
    //công việc của admin
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable Long id,
            @Valid @RequestBody OrderDTO orderDTO
    ){
        return ResponseEntity.ok("Cập nhật thông tin 1 order");
    }

    @DeleteMapping("/{id}")//Thêm biến đường dẫn
    //Delete http://localhost:8088/api/v1/orders/4
    public ResponseEntity<?> deleteOrder(@Valid @PathVariable Long id){
        //xóa mềm = cập nhật trường active = false
        return ResponseEntity.ok("Order delete successfully");
    }
}
