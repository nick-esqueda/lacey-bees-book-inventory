import { createSlice } from "@reduxjs/toolkit";
import { fetchBooks } from "./booksApi";

const initialState = {
  entities: {},
  ids: [],
  loading: false,
  error: null,
};

const booksSlice = createSlice({
  name: "books",
  initialState,
  reducers: {
    setBooks: (state, action) => {
      state.entities = {};
      state.ids = [];
      action.payload.forEach((book) => {
        state.entities[book.id] = book;
        state.ids.push(book.id);
      });
      state.loading = false;
      state.error = null;
    },
    addBook: (state, action) => {
      const book = action.payload;
      state.entities[book.id] = book;
      state.ids.push(book.id);
    },
    updateBook: (state, action) => {
      const book = action.payload;
      if (state.entities[book.id]) {
        state.entities[book.id] = book;
      }
    },
    removeBook: (state, action) => {
      const id = action.payload;
      delete state.entities[id];
      state.ids = state.ids.filter((bookId) => bookId !== id);
    },
    setLoading: (state, action) => {
      state.loading = action.payload;
    },
    setError: (state, action) => {
      state.loading = false;
      state.error = action.payload;
    },
  },
});

export const {
  setBooks,
  addBook,
  updateBook,
  removeBook,
  setLoading,
  setError,
} = booksSlice.actions;

export const fetchBooksAsync = () => async (dispatch) => {
  dispatch(setLoading(true));
  try {
    const books = await fetchBooks();
    dispatch(setBooks(books));
  } catch (error) {
    dispatch(setError(error.message));
  }
};

export default booksSlice.reducer;
