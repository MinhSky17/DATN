import DefaultLayout from "../layout/DefaultLayout";
import MiddlewareRouter from "../middleware/AuthGuard";
import NotAuthorized from "../pages/401";
import Forbidden from "../pages/403";
import NotFound from "../pages/404";
import Oops from "../pages/500";
import BrandManagement from "../pages/admin/brandManagement/BrandManagement";
import NotAceptable from "../pages/not-aceptable";
import React from "react";
import { Navigate } from "react-router-dom";
import { SCREEN } from "./screen";
import Home from "../pages/guest/Home";
import GuestGuard from "../middleware/GuestGuard";
import DashboardGuest from "../layout/guest/DashboardGuest";
import UserManagement from "../pages/admin/user-management/UserManagement";

const generalRoutes = [
  { path: "*", element: <NotFound /> },
  { path: "/not-found", element: <NotFound /> },
  { path: "/not-authorization", element: <NotAuthorized /> },
  { path: "/forbidden", element: <Forbidden /> },
  { path: "/error", element: <Oops /> },
  { path: "/not-aceptable/*", element: <NotAceptable /> },
];

const guestRoutes = [
  {
    path: "/",
    element: (
      <GuestGuard>
        <DashboardGuest>
          <Home />
        </DashboardGuest>
      </GuestGuard>
    ),
  },
];

const adminRoutes = [
  {
    path: "/admin",
    element: <Navigate replace to={SCREEN.brandManagement.path} />,
  },
  {
    path: SCREEN.brandManagement.path,
    element: (
      <MiddlewareRouter>
        <DefaultLayout>
          <BrandManagement />
        </DefaultLayout>
      </MiddlewareRouter>
    ),
  },
  {
    path: "/admin/user-management",
    element: (
      <MiddlewareRouter>
        <DefaultLayout>
          <UserManagement />
        </DefaultLayout>
      </MiddlewareRouter>
    ),
  },
];

const routes = [...generalRoutes, ...guestRoutes, ...adminRoutes];

export default routes;
