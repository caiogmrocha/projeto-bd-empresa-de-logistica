import type { Address } from "./suppliers";

export interface Company {
  id: number;
  legalName: string;
  tradeName: string;
  cnpj: string;
  phones: string[];
  emails: string[];
  address: Address;
  createdAt: string;
}

export interface CompanyRequest {
  legalName: string;
  tradeName: string;
  cnpj: string;
  phones: string[];
  emails: string[];
  address: Address;
}

export interface CompanyUpdate {
    legalName?: string;
    tradeName?: string;
    phones?: string[];
    emails?: string[];
    address?: Address;
}

export interface CompaniesPage {
    content: Company[];
    page: number;
    size: number;
    totalElements: number;
}

async function handleFetchErrors(response: Response, defaultErrorMessage: string) {
    if (!response.ok) {
      try {
        const errorData = await response.json();
        throw new Error(errorData.message || defaultErrorMessage);
      } catch {
        throw new Error(defaultErrorMessage);
      }
    }
}

export async function createCompany(data: CompanyRequest): Promise<Company> {
    const response = await fetch("/api/companies", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
    });
    await handleFetchErrors(response, "Falha ao criar empresa");
    return response.json() as Promise<Company>;
}

export async function getCompanies(params: {
    page?: number;
    size?: number;
    tradeName?: string;
    sortBy?: string;
    direction?: "asc" | "desc";
} = {}): Promise<CompaniesPage> {
    const query = new URLSearchParams({
        page: String(params.page ?? 0),
        size: String(params.size ?? 10),
        sortBy: params.sortBy ?? "id",
        direction: params.direction ?? "asc",
    });

    if (params.tradeName) {
        query.set("tradeName", params.tradeName);
    }

    const response = await fetch(`/api/companies?${query.toString()}`);
    await handleFetchErrors(response, "Falha ao buscar empresas");
    return response.json() as Promise<CompaniesPage>;
}

export async function getCompany(id: number | string): Promise<Company> {
    const response = await fetch(`/api/companies/${id}`);
    await handleFetchErrors(response, "Falha ao buscar empresa");
    return response.json() as Promise<Company>;
}

export async function updateCompany(id: number | string, data: CompanyUpdate): Promise<Company> {
    const response = await fetch(`/api/companies/${id}`, {
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
    });
    await handleFetchErrors(response, "Falha ao atualizar empresa");
    return response.json() as Promise<Company>;
}

export async function deleteCompany(id: number | string): Promise<{ ok: true; id: number | string }> {
    const response = await fetch(`/api/companies/${id}`, {
        method: "DELETE",
    });
    await handleFetchErrors(response, "Falha ao deletar empresa");
    
    return { ok: true, id };
}