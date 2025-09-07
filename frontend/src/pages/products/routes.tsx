import type { RouteObject } from "react-router";
import { ProductCreatePage } from "./create";

export const productsRoutes: RouteObject[] = [
  {
    path: "/products",
    children: [
      {
        path: "/products/create",
        Component: ProductCreatePage,
      },
    ],
  },
];
