import React from "react";
import { useParams } from "react-router";

export const DeleteCompanyModal: React.FC = () => {
  const { companyId = "Not Provided" } = useParams<{ companyId: string }>();

  return <h1>Delete Company {companyId}</h1>;
};
