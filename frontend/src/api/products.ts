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
  return product;
}
