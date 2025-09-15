import type { RouteObject } from "react-router";
import { CompaniesListPage } from "./list";
import { CompanyCreatePage } from "./create";
import { CompanyDetailsPage } from "./details";
import { EditCompanyPage } from "./edit";
import { DeleteCompanyModal } from "./delete";

export const companiesRoutes: RouteObject[] = [
  {
    path: "/companies",
    children: [
      {
        index: true,
        Component: CompaniesListPage,
      },
      {
        path: "create",
        Component: CompanyCreatePage,
      },
      {
        path: ":companyId",
        Component: CompanyDetailsPage,
      },
      {
        path: ":companyId/edit",
        Component: EditCompanyPage,
      },
      {
        path: ":companyId/delete",
        Component: DeleteCompanyModal,
      },
    ],
  },
];