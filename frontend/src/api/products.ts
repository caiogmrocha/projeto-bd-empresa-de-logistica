export enum ProductStatus {
  TESTED = 'TESTED',
  RETURNED = 'RETURNED',
}

// Create payload keyed dynamically by language ISO codes present in the system
export interface CreateProductRequest {
  names: Record<string, string>
  descriptions: Record<string, string>
  warrantyDate: Date
  status: ProductStatus
  minimumSalePrice: number
  stock: number
  categoriesIds: number[]
  warehouseId: number
  supplierId: number
}

export type UpdateProductRequest = CreateProductRequest

// Detailed product shape used in create/edit flows (mocked for now)
export type ProductDetails = {
  id: number
} & CreateProductRequest

// New Product type aligned with backend ProductResponseDTO for listing
export interface Product {
  id: number
  warranty_date: string
  status: ProductStatus | string
  minimumSalePrice: number
  createdAt: string
  updatedAt: string
}

export type SortOrder = 'asc' | 'desc'
export interface GetProductsParams {
  page?: number // 1-based
  limit?: number
  search?: string
  sortBy?: keyof Product | 'id' | 'minimumSalePrice' | 'status' | 'createdAt' | 'updatedAt'
  order?: SortOrder
}

import type { PageResponse } from '@/api/pagination'

const API_BASE_URL = (typeof import.meta !== 'undefined' && (import.meta as any).env?.VITE_API_URL) || process.env.VITE_API_URL || process.env.API_URL || ''

export async function getProducts(params: GetProductsParams = {}): Promise<PageResponse<Product>> {
  const page = params.page ?? 1
  const size = params.limit ?? 10
  const search = params.search ?? ''
  const sortBy = params.sortBy
  const order: SortOrder | undefined = params.order

  const q = new URLSearchParams()
  // Spring Data pageable is 0-based
  q.set('page', String(Math.max(0, page - 1)))
  q.set('size', String(size))
  if (search) q.set('search', search)
  if (sortBy && order) q.set('sort', `${sortBy},${order}`)

  const base = API_BASE_URL?.replace(/\/$/, '') || ''
  const url = `${base}/api/products?${q.toString()}`

  const res = await fetch(url)
  if (!res.ok) {
    throw new Error(`Failed to fetch products: ${res.status} ${res.statusText}`)
  }
  const json = await res.json()
  return json as PageResponse<Product>
}

export async function createProduct(data: CreateProductRequest) {
  const response = await fetch("/api/products", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    throw new Error(`Failed to create product: ${response.statusText}`);
  }

  const product = await response.json();
  return product as ProductDetails;
}

export async function getProduct(id: number | string): Promise<ProductDetails> {
  // Mock: simulate network delay and return a generated product
  await new Promise((r) => setTimeout(r, 300))
  const n = typeof id === 'string' ? parseInt(id, 10) : id
  const safeId = Number.isFinite(n) && (n as number) > 0 ? (n as number) : 1

  return {
    id: safeId,
    names: {
      'pt-BR': `Produto ${safeId}`,
      en: `Product ${safeId}`,
    },
    descriptions: {
      'pt-BR': `Descrição do produto ${safeId}`,
      en: `Product ${safeId} description`,
    },
    warrantyDate: new Date(Date.now() + 1000 * 60 * 60 * 24 * (safeId % 365)),
    status: safeId % 3 === 0 ? ProductStatus.RETURNED : ProductStatus.TESTED,
    minimumSalePrice: Math.round((safeId * 7.5) % 1000) + 9.9,
    stock: (safeId * 13) % 200,
    categoriesIds: [],
    warehouseId: (safeId % 3) + 1,
    supplierId: (safeId % 3) + 1,
  }
}

export async function updateProduct(id: number | string, data: UpdateProductRequest): Promise<ProductDetails> {
  // Mock: simulate a network delay and echo back the updated product
  await new Promise((r) => setTimeout(r, 400))
  return {
    id: typeof id === 'string' ? parseInt(id, 10) : id,
    ...data,
  }
}

export async function deleteProduct(id: number | string): Promise<{ ok: true; id: number }> {
  // Mock: simulate deletion with delay
  await new Promise((r) => setTimeout(r, 300))
  return { ok: true, id: typeof id === 'string' ? parseInt(id, 10) : id }
}
