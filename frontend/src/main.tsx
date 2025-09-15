import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { RouterProvider } from 'react-router'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { router } from './routes.tsx'

import "./assets/css/index.css"
import { ThemeProvider } from './components/theme-provider.tsx'
import "@/lib/zod-ptbr"
import { Toaster } from './components/ui/sonner.tsx'

const queryClient = new QueryClient()

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <QueryClientProvider client={queryClient}>
      <ThemeProvider>
          <RouterProvider router={router} />
      </ThemeProvider>
    </QueryClientProvider>
    <Toaster />
  </StrictMode>,
)
