
// Create payload keyed dynamically by language ISO codes present in the system
export interface CreateWarehouseRequest {
  name: string
  address: Address
}

export interface Address {
  country: string
  state: string
  city: string
  street: string
  number: string
  zipCode: string
}

export type UpdateWarehouseRequest = CreateWarehouseRequest

export type Warehouse = {
  id: number
} & CreateWarehouseRequest

export async function createWarehouse(data: CreateWarehouseRequest) {
  const response = await fetch("http://localhost:8080/api/warehouses/create", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    throw new Error(`Failed to create warehouse: ${response.statusText}`);
  }

  const warehouse = await response.json();
  return warehouse as Warehouse;
}

export async function getWarehouse(id: number | string): Promise<Warehouse> {
  const response = await fetch(`http://localhost:8080/api/warehouses/${id}`);

  if (!response.ok) {
    throw new Error(`Failed to fetch warehouse: ${response.statusText}`);
  }
  const warehouse = await response.json();
  return warehouse as Warehouse;
}

export async function updateWarehouse(id: number | string, data: UpdateWarehouseRequest): Promise<Warehouse> {
  const response = await fetch(`http://localhost:8080/api/warehouses/update/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    throw new Error(`Falha ao atualizar armazém: ${response.statusText}`);
  }

  const warehouse = await response.json();
  return warehouse as Warehouse;
}

export async function deleteWarehouse(id: number | string): Promise<{ ok: true; id: number }> {
  const response = await fetch(`http://localhost:8080/api/warehouses/delete/${id}`, {
    method: "DELETE",
  });
  if (response.status === 400) {
    throw new Error(`Falha ao deletar armazém: ${response.statusText}`);
  }
  return { ok: true, id: Number(id) };
}

export interface GetWarehousesParams {
  page?: number
  limit?: number
  search?: string
}

export interface WarehousesPage {
  content: Warehouse[]
  total: number
  page: number
  limit: number
}

export async function getWarehouses(params: GetWarehousesParams = {}): Promise<WarehousesPage> {
  const page = params.page ?? 0
  const size = params.limit ?? 10
  const search = params.search ?? ""

  // Adiciona search somente se tiver algo
  const searchParam = search ? `&name=${encodeURIComponent(search)}` : ""

  const response = await fetch(`http://localhost:8080/api/warehouses?page=${page}&size=${size}${searchParam}`)

  if (!response.ok) {
    throw new Error(`Failed to fetch warehouses: ${response.statusText}`)
  }

  const data = await response.json()
  return {
    content: data.content as Warehouse[],
    total: data.totalElements as number,
    page: data.number as number,
    limit: data.size as number,
  }
}

