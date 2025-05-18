package com.model1.application.repository;

import com.model1.application.entity.ProductColor;
import com.model1.application.entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductColorRepository extends JpaRepository<ProductColor,Long> {
    //Lấy color của sản phẩm
    @Query(nativeQuery = true,value = "SELECT ps.code FROM product_color ps WHERE ps.product_id = ?1 AND ps.quantity > 0")
    List<String> findAllColorOfProduct(String id);

    //Lay ds cua san pham
    List<ProductColor> findByProductId(String id);

    //Kiểm trả color sản phẩm
    @Query(value = "SELECT * FROM product_color WHERE product_id = ?1 AND code = ?2 AND quantity >0 ", nativeQuery = true)
    ProductColor checkProductAndColorAvailable(String productId, String code);

    //Trừ 1 sản phẩm theo color
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "Update product_color set quantity = quantity - 1 where product_id = ?1 and code = ?2")
    public void minusOneProductByColor(String id, String code);

    //Cộng 1 sản phẩm theo color
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "Update product_color set quantity = quantity + 1 where product_id = ?1 and code = ?2")
    public void plusOneProductByColor(String id, String code);

//    @Query(value = "SELECT * FROM product_Color ps WHERE ps.Color = ?1 AND ps.product_id = ?2",nativeQuery = true)
//    Optional<ProductColor> getProductColorByColor(int Color,String productId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "Delete from product_color where product_id = ?1")
    public void deleteByProductId(String id);
}
