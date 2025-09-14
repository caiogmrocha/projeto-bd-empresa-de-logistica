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
  names?: Record<string, string>
  descriptions?: Record<string, string>
  categoriesIds?: number[]
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
  // Backend ProductRequestDTO expects: warranty_date, status, minimumSalePrice, names, descriptions
  const payload = {
    warranty_date: new Date(data.warrantyDate).toISOString(),
    status: data.status,
    minimumSalePrice: Number(data.minimumSalePrice ?? 0),
    names: data.names,
    descriptions: data.descriptions,
    categoriesIds: data.categoriesIds ?? [],
  }

  const base = API_BASE_URL?.replace(/\/$/, '') || ''
  const response = await fetch(`${base}/api/products`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    throw new Error(`Failed to create product: ${response.status} ${response.statusText}`);
  }

  const product = await response.json();
  return product as ProductDetails;
}

export async function getProduct(id: number | string): Promise<ProductDetails> {
  const base = API_BASE_URL?.replace(/\/$/, '') || ''
  const res = await fetch(`${base}/api/products/${id}`)
  if (!res.ok) {
    throw new Error(`Failed to fetch product: ${res.status} ${res.statusText}`)
  }
  const p = await res.json() as Product
  return {
    id: p.id,
    names: p.names ?? {},
    descriptions: p.descriptions ?? {},
    warrantyDate: new Date(p.warranty_date),
    status: p.status as ProductStatus,
    minimumSalePrice: Number(p.minimumSalePrice ?? 0),
    stock: 0,
    categoriesIds: p.categoriesIds ?? [],
    warehouseId: 1,
    supplierId: 1,
  }
}

export async function updateProduct(id: number | string, data: UpdateProductRequest): Promise<ProductDetails> {
  const payload = {
    warranty_date: new Date(data.warrantyDate).toISOString(),
    status: data.status,
    minimumSalePrice: Number(data.minimumSalePrice ?? 0),
    names: data.names,
    descriptions: data.descriptions,
    categoriesIds: data.categoriesIds ?? [],
  }
  const base = API_BASE_URL?.replace(/\/$/, '') || ''
  const res = await fetch(`${base}/api/products/update/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })
  if (!res.ok) {
    throw new Error(`Failed to update product: ${res.status} ${res.statusText}`)
  }
  const p = await res.json() as Product
  return {
    id: p.id,
    names: p.names ?? {},
    descriptions: p.descriptions ?? {},
    warrantyDate: new Date(p.warranty_date),
    status: p.status as ProductStatus,
    minimumSalePrice: Number(p.minimumSalePrice ?? 0),
    stock: 0,
    categoriesIds: p.categoriesIds ?? [],
    warehouseId: 1,
    supplierId: 1,
  }
}

export async function deleteProduct(id: number | string): Promise<{ ok: true; id: number }> {
  // Mock: simulate deletion with delay
  await new Promise((r) => setTimeout(r, 300))
  return { ok: true, id: typeof id === 'string' ? parseInt(id, 10) : id }
}
