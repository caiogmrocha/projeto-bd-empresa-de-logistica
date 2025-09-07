import { createBrowserRouter } from "react-router";
import { MainLayout } from "./layouts/main-layout";
import { companiesRoutes } from "./pages/companies/routes";
import { productsRoutes } from "./pages/products/routes";

export const router = createBrowserRouter([
  {
    element: <MainLayout />,
    children: [
      ...companiesRoutes,
      ...productsRoutes,
    ],
  },
]);
