import type { RouteObject } from "react-router";
import { CustomersListPage } from "./list";
import { CustomerCreatePage } from "./create";
import { EditCustomerPage } from "./edit/index";
import { CustomerDeletePage } from "./delete";

export const customersRoutes: RouteObject[] = [
  {
    path: "/customers",
    children: [
      {
        index: true,
        Component: CustomersListPage,
      },
      {
        path: "/customers/create",
        Component: CustomerCreatePage,
      },
      {
        path: "/customers/:customerId/edit",
        Component: EditCustomerPage,
      },
      {
        path: "/customers/:customerId/delete",
        Component: CustomerDeletePage,
      }
    ],
  },
];
