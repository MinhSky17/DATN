import axios from "axios";
import { AppConfig } from "../../AppConfig";
import { message } from "antd";
import {
  STATUS_BAD_REQUEST,
  STATUS_ERROR_INTERNAL,
  STATUS_FORBIDDEN,
  STATUS_NOT_ACCEPTABLE,
  STATUS_NOT_AUTHORIZATION,
  STATUS_NOT_FOUND,
  STATUS_TOKEN_EXPIRED,
} from "../common/constants";
import { store } from "../../app/store";
import { SetLoading } from "../../app/reducer/common/LoadingSlice.reducer";

const errorPages = {
  [STATUS_NOT_AUTHORIZATION]: "/not-authorization",
  [STATUS_NOT_FOUND]: "/not-found",
  [STATUS_FORBIDDEN]: "/forbidden",
  [STATUS_NOT_ACCEPTABLE]: (data) => `/not-acceptable/status=${data}`,
  [STATUS_ERROR_INTERNAL]: "/error",
};

const handleErrorRedirect = (status, data) => {
  const path = errorPages[status];
  if (typeof path === "function") {
    window.location.href = path(data);
  } else if (path) {
    window.location.href = path;
  }
};

const handleErrorNotification = (status, data) => {
  if (status === STATUS_BAD_REQUEST && data.message) {
    message.error(data.message);
  }
};

export const request = axios.create({
  baseURL: AppConfig.apiUrl,
  withCredentials: true, // Enable for CSRF protection
});

// Helper function to refresh token
const refreshToken = async () => {
  const refreshToken = localStorage.getItem("refreshToken");
  if (refreshToken) {
    try {
      const response = await axios.post(`${AppConfig.apiUrl}/refresh-token`, {
        refreshToken,
      });
      localStorage.setItem("token", response.data.token);
      return response.data.token;
    } catch (error) {
      // If token refresh fails, redirect to login
      window.location.href = "/login";
    }
  }
  return null;
};

// Interceptor request
request.interceptors.request.use(
  async (config) => {
    let token = localStorage.getItem("token");

    // Check token expiration logic here if necessary
    if (!token) {
      token = await refreshToken();
    }

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

// Interceptor response
request.interceptors.response.use(
  (response) => response,
  async (error) => {
    store.dispatch(SetLoading(false));

    if (error.response) {
      const { status, data } = error.response;

      // Handle token expiration
      if (status === STATUS_TOKEN_EXPIRED) {
        const newToken = await refreshToken();
        if (newToken) {
          error.config.headers.Authorization = `Bearer ${newToken}`;
          return request(error.config); // Retry the original request with the new token
        }
      }

      handleErrorRedirect(status, data);
      handleErrorNotification(status, data);
    }

    throw error;
  }
);
