import type { RouteObject } from "react-router";
import { CompaniesListPage } from "./list";
import { CreateCompanyPage } from "./create";
import { CompanyDetailsPage } from "./details";
import { EditCompanyPage } from "./edit";

export const companiesRoutes: RouteObject[] = [
  {
    path: "/companies",
    children: [
      {
        index: true,
        Component: CompaniesListPage,
      },
      {
        path: "/companies/create",
        Component: CreateCompanyPage,
      },
      {
        path: "/companies/:companyId",
        Component: CompanyDetailsPage,
      },
      {
        path: "/companies/:companyId/edit",
        Component: EditCompanyPage,
      },
    ],
  },
];
