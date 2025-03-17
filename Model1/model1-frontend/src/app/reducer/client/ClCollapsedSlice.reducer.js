import { createSlice } from "@reduxjs/toolkit";

const initialState = false;

const ClCollapsedSlice = createSlice({
  name: "clCollapsed",
  initialState,
  reducers: {
    SetFalseToggle: (state, action) => {
      state = false;
      return state;
    },
    SetTrueToggle: (state, action) => {
      state = true;
      return state;
    },
    Toggle: (state, action) => {
      state = action.payload;
      return state;
    },
  },
});

export const { SetFalseToggle, SetTrueToggle, Toggle } = ClCollapsedSlice.actions;

export const GetClCollapsed = (state) => state.clCollapsed;

export default ClCollapsedSlice.reducer;