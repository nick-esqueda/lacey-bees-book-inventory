package com.nickesqueda.bookkeeperapi.util;

public final class SqlQueries {
  public static final String FIND_BOOK_CATEGORY_WITH_STATS =
      """
      SELECT
        bc.id,
        bc.name,
        bc.created_at,
        bc.updated_at,
        COUNT(b.id) AS totalBookCount,
        SUM(CASE WHEN b.read_status = 'READ' THEN 1 ELSE 0 END) AS readBookCount,
        SUM(CASE WHEN b.read_status = 'UNREAD' THEN 1 ELSE 0 END) AS unreadBookCount,
        SUM(CASE WHEN b.read_status = 'DID_NOT_FINISH' THEN 1 ELSE 0 END) AS didNotFinishBookCount,
        COUNT(DISTINCT b.author) AS authorCount
      FROM book_categories bc
      LEFT JOIN books b ON bc.id = b.book_category_id
      WHERE bc.id = :bookCategoryId
      GROUP BY bc.id, bc.name, bc.created_at, bc.updated_at;
      """;

  public static final String FIND_ALL_BOOK_CATEGORIES_WITH_STATS =
      """
      SELECT
        bc.id,
        bc.name,
        bc.created_at,
        bc.updated_at,
        COUNT(b.id) AS totalBookCount,
        SUM(CASE WHEN b.read_status = 'READ' THEN 1 ELSE 0 END) AS readBookCount,
        SUM(CASE WHEN b.read_status = 'UNREAD' THEN 1 ELSE 0 END) AS unreadBookCount,
        SUM(CASE WHEN b.read_status = 'DID_NOT_FINISH' THEN 1 ELSE 0 END) AS didNotFinishBookCount,
        COUNT(DISTINCT b.author) AS authorCount
      FROM book_categories bc
      LEFT JOIN books b ON bc.id = b.book_category_id
      GROUP BY bc.id, bc.name, bc.created_at, bc.updated_at;
      """;

  // use CONCAT(:searchTerm, '*')) for prefix matching.
  // note: "IN BOOLEAN MODE" ignores some common words (a.k.a. stopwords)
  public static final String SEARCH_BOOKS =
      """
      SELECT *
      FROM books
      WHERE
        MATCH(title, author, edition, notes)
          AGAINST (CONCAT(:searchTerm, '*') IN BOOLEAN MODE)
        AND (:readStatus IS NULL OR read_status = :readStatus)
        AND (:bookCategoryId IS NULL OR book_category_id = :bookCategoryId)
      """;

  public static final String SEARCH_BOOKS_USING_TAG_FILTER =
      """
      SELECT b.*
      FROM books b
      JOIN books_book_tags jt ON b.id = jt.book_id
      WHERE
        MATCH(title, author, edition, notes)
          AGAINST (CONCAT(:searchTerm, '*') IN BOOLEAN MODE)
        AND (:readStatus IS NULL OR read_status = :readStatus)
        AND (:bookCategoryId IS NULL OR book_category_id = :bookCategoryId)
        AND (jt.book_tag_id IN :bookTagIds)
      GROUP BY b.id
      HAVING COUNT(DISTINCT jt.book_tag_id) >= :bookTagCount
      """;

  public static final String SEARCH_BOOKS_USING_TAG_FILTER_COUNT_QUERY =
      """
      SELECT COUNT(DISTINCT b.id)
      FROM books b
      JOIN books_book_tags jt ON b.id = jt.book_id
      WHERE
        MATCH(title, author, edition, notes)
          AGAINST (CONCAT(:searchTerm, '*') IN BOOLEAN MODE)
        AND (:readStatus IS NULL OR read_status = :readStatus)
        AND (:bookCategoryId IS NULL OR book_category_id = :bookCategoryId)
        AND (jt.book_tag_id IN :bookTagIds)
      """;

  public static final String FIND_BOOKS =
      """
      SELECT *
      FROM books
      WHERE (:readStatus IS NULL OR read_status = :readStatus)
        AND (:bookCategoryId IS NULL OR book_category_id = :bookCategoryId)
      """;

  public static final String FIND_BOOKS_USING_TAG_FILTER =
      """
      SELECT b.*
      FROM books b
      JOIN books_book_tags jt ON b.id = jt.book_id
      WHERE (:readStatus IS NULL OR b.read_status = :readStatus)
        AND (:bookCategoryId IS NULL OR b.book_category_id = :bookCategoryId)
        AND (jt.book_tag_id IN :bookTagIds)
      GROUP BY b.id
      HAVING COUNT(DISTINCT jt.book_tag_id) >= :bookTagCount
      """;

  public static final String FIND_BOOKS_USING_TAG_FILTER_COUNT_QUERY =
      // BUGFIX: provide custom count query to Spring JPA or else JPA will generate a count query
      // with incorrect syntax when pagination is needed (for tags having a higher book count)
      """
      SELECT COUNT(DISTINCT b.id)
      FROM books b
      JOIN books_book_tags jt ON b.id = jt.book_id
      WHERE (:readStatus IS NULL OR b.read_status = :readStatus)
        AND (:bookCategoryId IS NULL OR b.book_category_id = :bookCategoryId)
        AND (jt.book_tag_id IN :bookTagIds)
      """;

  public static final String COUNT_DISTINCT_AUTHORS_IN_BOOK_CATEGORY =
      """
      SELECT COUNT(DISTINCT author)
      FROM books
      WHERE book_category_id = :bookCategoryId
      GROUP BY book_category_id;
      """;

  public static final String FIND_ALL_BOOK_TAGS_WITH_STATS =
      """
      SELECT
        bt.id,
        bt.name,
        bt.created_at,
        bt.updated_at,
        COUNT(b.id) AS totalBookCount,
        SUM(CASE WHEN b.read_status = 'READ' THEN 1 ELSE 0 END) AS readBookCount,
        SUM(CASE WHEN b.read_status = 'UNREAD' THEN 1 ELSE 0 END) AS unreadBookCount,
        SUM(CASE WHEN b.read_status = 'DID_NOT_FINISH' THEN 1 ELSE 0 END) AS didNotFinishBookCount,
        COUNT(DISTINCT b.author) AS authorCount
      FROM book_tags bt
      LEFT JOIN books_book_tags jt ON bt.id = jt.book_tag_id
      LEFT JOIN books b ON jt.book_id = b.id
      GROUP BY bt.id, bt.name, bt.created_at, bt.updated_at;
      """;

  public static final String FIND_BOOK_TAG_WITH_STATS =
      """
      SELECT
        bt.id,
        bt.name,
        bt.created_at,
        bt.updated_at,
        COUNT(b.id) AS totalBookCount,
        SUM(CASE WHEN b.read_status = 'READ' THEN 1 ELSE 0 END) AS readBookCount,
        SUM(CASE WHEN b.read_status = 'UNREAD' THEN 1 ELSE 0 END) AS unreadBookCount,
        SUM(CASE WHEN b.read_status = 'DID_NOT_FINISH' THEN 1 ELSE 0 END) AS didNotFinishBookCount,
        COUNT(DISTINCT b.author) AS authorCount
      FROM book_tags bt
      LEFT JOIN books_book_tags jt ON bt.id = jt.book_tag_id
      LEFT JOIN books b ON jt.book_id = b.id
      WHERE bt.id = :bookTagId
      GROUP BY bt.id, bt.name, bt.created_at, bt.updated_at;
      """;

  public static final String COUNT_DISTINCT_AUTHORS_IN_BOOK_TAG =
      """
      SELECT COUNT(DISTINCT b.author)
      FROM books b
      JOIN books_book_tags jt ON b.id = jt.book_id
      WHERE jt.book_tag_id = :bookTagId;
      """;

  public static final String COUNT_DISTINCT_AUTHORS =
      """
      SELECT COUNT(DISTINCT author)
      FROM books
      """;
}
