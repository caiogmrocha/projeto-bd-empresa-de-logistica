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

export type Product = {
  id: number
} & CreateProductRequest

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
  return product as Product;
}

export async function getProduct(id: number | string): Promise<Product> {
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

export async function updateProduct(id: number | string, data: UpdateProductRequest): Promise<Product> {
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

export interface GetProductsParams {
  page?: number
  limit?: number
  search?: string
}

export interface ProductsPage {
  items: Product[]
  total: number
  page: number
  limit: number
}

export async function getProducts(params: GetProductsParams = {}): Promise<ProductsPage> {
  const page = params.page ?? 1
  const limit = params.limit ?? 10
  const search = (params.search ?? "").toLowerCase()

  // Mocked dataset
  const MOCK: Product[] = Array.from({ length: 57 }).map((_, i) => {
    const id = i + 1
    const names = {
      'pt-BR': `Produto ${id}`,
      en: `Product ${id}`,
    } as Record<string, string>
    return {
      id,
      names,
      descriptions: {
        'pt-BR': `Descrição do produto ${id}`,
        en: `Product ${id} description`,
      },
      warrantyDate: new Date(Date.now() + 1000 * 60 * 60 * 24 * (id % 365)),
      status: id % 3 === 0 ? ProductStatus.RETURNED : ProductStatus.TESTED,
      minimumSalePrice: Math.round((id * 7.5) % 1000) + 9.9,
      stock: (id * 13) % 200,
      categoriesIds: [],
      warehouseId: (id % 3) + 1,
      supplierId: (id % 3) + 1,
    }
  })

  // Filter by search (in any name value)
  const filtered = search
    ? MOCK.filter((p) => Object.values(p.names).some((n) => n.toLowerCase().includes(search)))
    : MOCK

  const total = filtered.length
  const start = (page - 1) * limit
  const end = start + limit
  const slice = filtered.slice(start, end)

  // Simulate network latency
  await new Promise((r) => setTimeout(r, 300))

  return {
    items: slice,
    total,
    page,
    limit,
  }
}
