import React from "react";
import { useParams } from "react-router";

export const EditCompanyPage: React.FC = () => {
  const { companyId = "Not Provided" } = useParams<{ companyId: string }>();

  return <h1>Edit Company {companyId}</h1>;
};
