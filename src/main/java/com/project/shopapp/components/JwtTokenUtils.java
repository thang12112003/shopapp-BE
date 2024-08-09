package com.project.shopapp.components;

import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    //JwtTokenUtil chịu trách nhiệm tạo, trích xuất và xác thực các JWT trong ứng dụng. Nó sử dụng các thư viện từ io.jsonwebtoken để thực hiện các thao tác này một cách an toàn và hiệu quả. JWT thường được sử dụng để bảo mật các API bằng cách xác thực người dùng và trao quyền truy cập dựa trên các thông tin mã hóa trong token.
    @Value("${jwt.expiration}")
    private int expiration; //save to an environment variable trong application.yml
    @Value("${jwt.secretKey}")
    private String secretKey;
    public String generateToken(User user) throws Exception{//Tạo ra một JWT từ thông tin của User.
        //properties => claims
        Map<String, Object> claims = new HashMap<>();
        //this.generateSecretKey();
        claims.put("phoneNumber", user.getPhoneNumber());// Lưu trữ thông tin bổ sung vào JWT (ở đây là phoneNumber).
        claims.put("userId", user.getId());
        try {
            String token = Jwts.builder()
                    .setClaims(claims) //how to extract claims from this ?
                    .setSubject(user.getPhoneNumber())//chủ thể của token là số điện thoại của người dùng.
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))//Đặt thời hạn cho token.
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)//Ký token với khóa bí mật và thuật toán HS256.
                    .compact();//Xây dựng và trả về token dưới dạng chuỗi.
            return token;
        }catch (Exception e) {
            //you can "inject" Logger, instead System.out.println
            throw new InvalidParamException("Cannot create jwt token, error: "+e.getMessage());
            //return null;
        }
    }
    private Key getSignInKey() {//Tạo ra khóa ký từ khóa bí mật được mã hóa base64.
        byte[] bytes = Decoders.BASE64.decode(secretKey);//Giải mã khóa bí mật từ chuỗi base64.
        //Keys.hmacShaKeyFor(Decoders.BASE64.decode("TaqlmGv1iEDMRiFp/pHuID1+T84IABfuA0xXh4GhiUI="));
        return Keys.hmacShaKeyFor(bytes);// Tạo khóa HMAC-SHA từ byte array.
    }
    private String generateSecretKey() {//Tạo ra một khóa bí mật ngẫu nhiên.
        SecureRandom random = new SecureRandom();//Tạo ra byte array ngẫu nhiên
        byte[] keyBytes = new byte[32]; // 256-bit key
        random.nextBytes(keyBytes);
        String secretKey = Encoders.BASE64.encode(keyBytes);
        return secretKey;
    }
    private Claims extractAllClaims(String token) {//Trích xuất tất cả các claims từ token.
        return Jwts.parserBuilder()//Phân tích và trích xuất claims từ token.
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {//Trích xuất một claim cụ thể từ token.
        final Claims claims = this.extractAllClaims(token);// Trích xuất tất cả các claims từ token.Áp dụng hàm truyền vào để trích xuất claim cụ thể.
        return claimsResolver.apply(claims);
    }
    //check expiration
    public boolean isTokenExpired(String token) {//Kiểm tra xem token đã hết hạn hay chưa.
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }
    public String extractPhoneNumber(String token) {
        return extractClaim(token, Claims::getSubject);
    }//Trích xuất số điện thoại từ token.

    //kiểm tra token còn hạn k, Username có trùng phoneNumber
    public boolean validateToken(String token, UserDetails userDetails) {//Xác thực token.
        String phoneNumber = extractPhoneNumber(token);
        return (phoneNumber.equals(userDetails.getUsername()))//Kiểm tra xem số điện thoại có khớp với tên người dùng và token có còn hạn hay không.
                && !isTokenExpired(token);
    }
}
