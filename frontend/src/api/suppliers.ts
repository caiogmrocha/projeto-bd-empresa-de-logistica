export enum SupplierType {
  NATURAL_PERSON = "NATURAL_PERSON",
  LEGAL_ENTITY = "LEGAL_ENTITY",
}

export interface Address {
  street: string
  number: string
  city: string
  state: string
  zipCode: string
  country: string
}

export interface CreateSupplierRequest {
  name: string
  supplierType: SupplierType
  cpf?: string
  cnpj?: string
  address: Address
}

export interface UpdateSupplierRequest {
  name: string
  address: Address
}

export interface SuppliersPage {
  content: Supplier[]
  page: number
  size: number
  totalElements: number
}

export type Supplier = {
  id: number
} & CreateSupplierRequest

async function handleFetchErrors(response: Response, defaultErrorMessage: string) {
  if (!response.ok) {
    try {
      const errorData = await response.json()
      throw new Error(errorData.message || defaultErrorMessage)
    } catch {
      throw new Error(defaultErrorMessage)
    }
  }
}

export async function createSupplier(data: CreateSupplierRequest): Promise<Supplier> {
  const response = await fetch("/api/suppliers", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  })

  await handleFetchErrors(response, "Falha ao criar fornecedor")
  return response.json() as Promise<Supplier>
}

export async function getSuppliers(params: {
  page?: number
  size?: number
  name?: string
  sortBy?: string
  direction?: "asc" | "desc"
} = {}): Promise<SuppliersPage> {
  const query = new URLSearchParams({
    page: String(params.page ?? 0),
    size: String(params.size ?? 10),
    sortBy: params.sortBy ?? "id",
    direction: params.direction ?? "asc",
  })

  if (params.name) {
    query.set("name", params.name)
  }

  const response = await fetch(`/api/suppliers?${query.toString()}`)
  await handleFetchErrors(response, "Falha ao buscar fornecedores")
  return response.json() as Promise<SuppliersPage>
}

export async function getSupplier(id: number | string): Promise<Supplier> {
  const response = await fetch(`/api/suppliers/${id}`)
  await handleFetchErrors(response, "Falha ao buscar o fornecedor")
  return response.json() as Promise<Supplier>
}

export async function updateSupplier(id: number | string, data: UpdateSupplierRequest): Promise<Supplier> {
  const response = await fetch(`/api/suppliers/${id}`, {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  })

  await handleFetchErrors(response, "Falha ao atualizar fornecedor")
  return response.json() as Promise<Supplier>
}

export async function deleteSupplier(id: number | string): Promise<{ ok: true; id: number | string }> {
  const response = await fetch(`/api/suppliers/${id}`, {
    method: "DELETE",
  })

  await handleFetchErrors(response, "Falha ao excluir fornecedor")

  if (response.status === 204) {
    return { ok: true, id }
  }
  return { ok: true, id }
}