import type { RouteObject } from "react-router";
import { CostumersListPage } from "./list";
import { CostumerCreatePage } from "./create";
import { CostumerDetailsPage } from "./details/index";
import { EditCostumerPage } from "./edit/index";
import { CostumerDeletePage } from "./delete";

export const costumersRoutes: RouteObject[] = [
  {
    path: "/costumers",
    children: [
      {
        index: true,
        Component: CostumersListPage,
      },
      {
        path: "/costumers/create",
        Component: CostumerCreatePage,
      },
      {
        path: "/costumers/:costumerId",
        Component: CostumerDetailsPage,
      },
      {
        path: "/costumers/:costumerId/edit",
        Component: EditCostumerPage,
      },
      {
        path: "/costumers/:costumerId/delete",
        Component: CostumerDeletePage,
      }
    ],
  },
];
