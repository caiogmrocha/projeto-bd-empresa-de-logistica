import type { RouteObject } from "react-router";
import { WarehousesListPage } from "./list";
import { WarehouseCreatePage } from "./create";
import { WarehouseDetailsPage } from "./details/index";
import { EditWarehousePage } from "./edit/index";
import { WarehouseDeletePage } from "./delete";

export const warehousesRoutes: RouteObject[] = [
  {
    path: "/warehouses",
    children: [
      {
        index: true,
        Component: WarehousesListPage,
      },
      {
        path: "/warehouses/create",
        Component: WarehouseCreatePage,
      },
      {
        path: "/warehouses/:warehouseId/edit",
        Component: EditWarehousePage,
      },
      {
        path: "/warehouses/:warehouseId/delete",
        Component: WarehouseDeletePage
      }
    ],
  },
];
