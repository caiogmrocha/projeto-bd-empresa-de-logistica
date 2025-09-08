import { createBrowserRouter} from "react-router";
import { MainLayout } from "./layouts/main-layout";
import { companiesRoutes } from "./pages/companies/routes";
import { warehousesRoutes } from "./pages/warehouses/routes";
import { costumersRoutes } from "./pages/costumer/routes";

export const router = createBrowserRouter([
  {
    element: <MainLayout />,
    children: [
      ...companiesRoutes, ...warehousesRoutes, ...costumersRoutes
    ],
  },
]);
