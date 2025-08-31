import { createBrowserRouter } from "react-router";
import App from "./App";
import { MainLayout } from "./layouts/main-layout";

export const router = createBrowserRouter([
  {
    element: <MainLayout />,
    children: [
      {
        index: true,
        element: <App />,
      },
      {
        path: "/test1",
        element: <h1>Test 1</h1>,
      },
      {
        path: "/test2",
        element: <h1>Test 2</h1>,
      },
      {
        path: "/test3",
        element: <h1>Test 3</h1>,
      },
    ],
  },
]);
