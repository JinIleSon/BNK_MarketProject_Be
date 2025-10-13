package kr.co.bnk_marketproject_be.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ORDER_ITEMS")
public class OrderItems {

    @Id
    private int id;

    private int orders_id;
    private int products_id;
    private int product_options_id;
    private int quentity;
    private int price;
}
