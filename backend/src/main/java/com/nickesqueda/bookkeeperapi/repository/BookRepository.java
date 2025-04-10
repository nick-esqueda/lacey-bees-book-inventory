package com.nickesqueda.bookkeeperapi.repository;

import static com.nickesqueda.bookkeeperapi.util.SqlQueries.*;

import com.nickesqueda.bookkeeperapi.entity.Book;
import com.nickesqueda.bookkeeperapi.exception.ResourceNotFoundException;
import com.nickesqueda.bookkeeperapi.model.ReadStatus;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Integer> {

  @Query(value = SEARCH_BOOKS, nativeQuery = true)
  Page<Book> searchBooks(
      @Param("searchTerm") String searchTerm,
      @Param("readStatus") String readStatus,
      @Param("bookCategoryId") Integer bookCategoryId,
      Pageable pageable);

  @Query(
      value = SEARCH_BOOKS_USING_TAG_FILTER,
      // BUGFIX: provide custom count query to Spring JPA or else JPA will generate a count query
      // with incorrect syntax when pagination is needed (for tags having a higher book count)
      countQuery = SEARCH_BOOKS_USING_TAG_FILTER_COUNT_QUERY,
      nativeQuery = true)
  Page<Book> searchBooksUsingTagFilter(
      @Param("searchTerm") String searchTerm,
      @Param("readStatus") String readStatus,
      @Param("bookCategoryId") Integer bookCategoryId,
      @Param("bookTagIds") List<Integer> bookTagIds,
      @Param("bookTagCount") Integer bookTagCount,
      Pageable pageable);

  @Query(value = FIND_BOOKS, nativeQuery = true)
  Page<Book> findBooks(
      @Param("readStatus") String readStatus,
      @Param("bookCategoryId") Integer bookCategoryId,
      Pageable pageable);

  @Query(
      value = FIND_BOOKS_USING_TAG_FILTER,
      // BUGFIX: provide custom count query to Spring JPA or else JPA will generate a count query
      // with incorrect syntax when pagination is needed (for tags having a higher book count)
      countQuery = FIND_BOOKS_USING_TAG_FILTER_COUNT_QUERY,
      nativeQuery = true)
  Page<Book> findBooksUsingTagFilter(
      @Param("readStatus") String readStatus,
      @Param("bookCategoryId") Integer bookCategoryId,
      @Param("bookTagIds") List<Integer> bookTagIds,
      @Param("bookTagCount") Integer bookTagCount,
      Pageable pageable);

  int countByBookCategoryId(int bookCategoryId);

  int countByReadStatus(ReadStatus readStatus);

  int countByBookCategoryIdAndReadStatus(int bookCategoryId, ReadStatus readStatus);

  int countByBookTags_Id(int bookTagId);

  int countByReadStatusAndBookTags_Id(ReadStatus readStatus, int bookTagId);

  @Query(value = COUNT_DISTINCT_AUTHORS, nativeQuery = true)
  int countAuthors();

  @Query(value = COUNT_DISTINCT_AUTHORS_IN_BOOK_CATEGORY, nativeQuery = true)
  int countAuthorsInBookCategory(@Param("bookCategoryId") int bookCategoryId);

  @Query(value = COUNT_DISTINCT_AUTHORS_IN_BOOK_TAG, nativeQuery = true)
  int countAuthorsInBookTag(@Param("bookTagId") int bookTagId);

  default Book retrieveOrElseThrow(int bookId) {
    return this.findById(bookId)
        .orElseThrow(() -> new ResourceNotFoundException(Book.class, bookId));
  }
}
