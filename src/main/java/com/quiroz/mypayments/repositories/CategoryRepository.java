package com.quiroz.mypayments.repositories;

import com.quiroz.mypayments.entities.Category;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);
    Optional<Category> findByNameAndCodeIgnoreCase(String name, String code);

    Optional<Category> findByNameAndParentId(String name, long categoryId);

    @Query("SELECT ca FROM Category ca WHERE ca.parentId is null")
    Page<Category> findAllByParentIdIsNullWithPagination(Pageable pageable);

    List<Category> findByParentId(Long parentId);
}
