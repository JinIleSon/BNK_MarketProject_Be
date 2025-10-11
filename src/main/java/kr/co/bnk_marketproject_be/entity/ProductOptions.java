package kr.co.bnk_marketproject_be.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product_options")
public class ProductOptions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String option_name;
    private int product_code;
}
