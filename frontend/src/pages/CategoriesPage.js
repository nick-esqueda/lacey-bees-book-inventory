import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { fetchBookCategoriesAsync } from '../features/bookCategories/bookCategoriesSlice';


const CategoriesPage = () => {
  const dispatch = useDispatch();
  const bookCategories = useSelector((state) => state.bookCategories.entities);
  const loading = useSelector((state) => state.bookCategories.loading);
  const error = useSelector((state) => state.bookCategories.error);

  useEffect(() => {
    dispatch(fetchBookCategoriesAsync());
  }, [dispatch]);

  if (loading) {
    return <p>Loading...</p>;
  }
  
  if (error) {
    return <p>Error: {error}</p>;
  }

  return (
    <div>
      <h2>Book Categories</h2>
      <ul>
        {bookCategories.map((bookCategory) => (
          <li key={bookCategory.id}>{bookCategory.name}</li>
        ))}
      </ul>
    </div>
  );
};

export default CategoriesPage;