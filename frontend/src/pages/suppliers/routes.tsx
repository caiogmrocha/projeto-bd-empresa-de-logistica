import type { RouteObject } from "react-router";
import { SupplierCreatePage } from "./create";
import { SupplierEditPage } from "./edit";
import { SuppliersListPage } from "./list";
import { SupplierDeletePage } from "./delete";
import { SupplierDetailsPage } from "./details";

export const suppliersRoutes: RouteObject[] = [
  {
    path: "/suppliers",
    children: [
      {
        index: true,
        Component: SuppliersListPage,
      },
      {
        path: "/suppliers/list",
        Component: SuppliersListPage,
      },
      {
        path: "/suppliers/create",
        Component: SupplierCreatePage,
      },
            {
        path: "/suppliers/:supplierId",
        Component: SupplierDetailsPage, 
      },
      {
        path: "/suppliers/:supplierId/edit",
        Component: SupplierEditPage,
      },
      {
        path: "/suppliers/:supplierId/delete",
        Component: SupplierDeletePage,
      },
    ],
  },
];
