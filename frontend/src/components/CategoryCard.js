import React from "react";
import { Badge, Card } from "react-bootstrap";
import { useSelector } from "react-redux";
import { Link } from "react-router-dom";

const CategoryCard = ({ categoryId }) => {
  const category = useSelector(
    (state) => state.bookCategories.entities[categoryId]
  );

  return (
    <Link to={`/categories/${category.id}`} style={{ textDecoration: "none" }}>
      <Card className="shadow-lg">
        <Card.Header className="text-center">Category</Card.Header>
        <Card.Body className="text-center m-5">
          <Card.Title as={"h3"}>
            {category.name}
          </Card.Title>
          <Card.Text className="d-flex justify-content-center gap-2 mt-3">
              <Badge bg="success">{category.readBookCount} Read</Badge>
              <Badge bg="primary">{category.totalBookCount} Total</Badge>
          </Card.Text>
        </Card.Body>
      </Card>
    </Link>
  );
};

export default CategoryCard;
