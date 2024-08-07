import React, { useEffect, useState } from "react";
import { Badge, Button, Col, Container, Row } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router-dom";
import { fetchBookCategoryAsync } from "../features/bookCategories/bookCategoriesSlice";
import BookList from "../components/BookList";
import EditCategoryModal from "../components/EditCategoryModal";
import LoadingSpinner from "../components/LoadingSpinner";
import { faCircleCheck } from "@fortawesome/free-regular-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faCircleHalfStroke,
  faCircleNotch,
  faList,
  faListOl,
  faUser,
} from "@fortawesome/free-solid-svg-icons";

const CategoryPage = () => {
  // show all the books in a category. edit the category
  // MVP: list all books in a grid. edit/delete category functionality.
  // GOAL: page through books. sort books by title/author/etc.

  const { categoryId } = useParams();
  const dispatch = useDispatch();

  const { entities, loading, error } = useSelector(
    (state) => state.bookCategories
  );
  const category = entities[categoryId];
  const [showModal, setShowModal] = useState(false);

  useEffect(() => {
    dispatch(fetchBookCategoryAsync(categoryId));
  }, [dispatch, categoryId]);

  if (loading || !category) {
    return <LoadingSpinner />;
  }

  if (error) {
    return <p>Error: {error}</p>;
  }

  return (
    <Container>
      <Row>
        <Col className="text-center">
          <h2>{category.name}</h2>
          <p>All books in the {category.name} category.</p>

          <p className="d-flex justify-content-center gap-3">
            <Badge bg="success">
              <FontAwesomeIcon icon={faCircleCheck} className="me-2" />
              {category.readBookCount} Read
            </Badge>
            <Badge bg="danger">
              <FontAwesomeIcon icon={faCircleNotch} className="me-2" />
              {category.unreadBookCount} Unread
            </Badge>
            <Badge bg="warning" text="dark">
              <FontAwesomeIcon icon={faCircleHalfStroke} className="me-2" />
              {category.didNotFinishBookCount} DNF
            </Badge>
          </p>

          <p className="d-flex justify-content-center gap-3">
            <Badge bg="primary">
              <FontAwesomeIcon icon={faListOl} className="me-2" />
              {category.totalBookCount} Total
            </Badge>
            <Badge bg="secondary">
              <FontAwesomeIcon icon={faUser} className="me-2" />
              {category.authorCount} Authors
            </Badge>
          </p>

          <Button variant="link" size="sm" onClick={() => setShowModal(true)}>
            Edit name
          </Button>
          <EditCategoryModal
            show={showModal}
            onHide={() => setShowModal(false)}
            category={category}
          />
        </Col>
      </Row>

      <Row>
        <BookList queryParams={{ bookCategoryId: categoryId }} />
      </Row>
    </Container>
  );
};

export default CategoryPage;
