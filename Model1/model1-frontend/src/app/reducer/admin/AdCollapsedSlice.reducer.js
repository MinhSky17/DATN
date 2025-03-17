import { createSlice } from "@reduxjs/toolkit";

const initialState = false;

const AdCollapsedSlice = createSlice({
  name: "adCollapsed",
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

export const { SetFalseToggle, SetTrueToggle, Toggle } =
  AdCollapsedSlice.actions;

export const GetAdCollapsed = (state) => state.adCollapsed;

export default AdCollapsedSlice.reducer;
