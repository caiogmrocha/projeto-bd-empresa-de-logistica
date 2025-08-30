import type React from "react";
import { Route, Routes } from "react-router";
import App from "./App";

export const Router: React.FC = () => {
  return (
    <Routes>
      <Route index element={<App />} />
      <Route path="/test" element={<h1>Test</h1>} />
    </Routes>
  );
}
