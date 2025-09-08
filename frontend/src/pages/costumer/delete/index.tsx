import React from "react";
import { useParams } from "react-router";

export const CostumerDeletePage: React.FC = () => {
  const { costumerId = "Not Provided" } = useParams<{ costumerId: string }>();

  return <h1>Delete Costumer {costumerId}</h1>;
};
