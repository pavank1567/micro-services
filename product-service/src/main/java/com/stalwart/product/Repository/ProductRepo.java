package com.stalwart.product.Repository;

import com.stalwart.product.model.Product;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface ProductRepo extends CassandraRepository<Product, UUID> {

}
