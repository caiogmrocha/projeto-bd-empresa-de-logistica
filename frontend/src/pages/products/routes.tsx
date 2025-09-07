import type { RouteObject } from "react-router";
import { ProductCreatePage } from "./create";
import { ProductEditPage } from "./edit";
import { ProductsListPage } from "./list";
import { ProductDeletePage } from "./delete";

export const productsRoutes: RouteObject[] = [
  {
    path: "/products",
    children: [
      {
        index: true,
        Component: ProductsListPage,
      },
      {
        path: "/products/list",
        Component: ProductsListPage,
      },
      {
        path: "/products/create",
        Component: ProductCreatePage,
      },
      {
        path: "/products/:productId/edit",
        Component: ProductEditPage,
      },
      {
        path: "/products/:productId/delete",
        Component: ProductDeletePage,
      },
    ],
  },
];
