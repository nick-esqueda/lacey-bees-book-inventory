import { combineReducers } from "@reduxjs/toolkit";
import bookCategoriesReducer from "../features/bookCategories/bookCategoriesSlice";
import booksReducer from "../features/books/booksSlice";
import bookTagsReducer from "../features/bookTags/bookTagsSlice";

const rootReducer = combineReducers({
  bookCategories: bookCategoriesReducer,
  books: booksReducer,
  bookTags: bookTagsReducer,
});

export default rootReducer;
