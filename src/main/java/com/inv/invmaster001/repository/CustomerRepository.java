package com.inv.invmaster001.repository;


import com.inv.invmaster001.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {


    List<Customer> findByCompanyIdAndDeletedAtIsNull(Long companyId);


}
