import React, { useEffect } from "react";
import { useQuery } from "@tanstack/react-query";
import { useParams, Link } from "react-router";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { getCompany, type Company } from "@/api/companies";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Form, FormControl, FormField, FormItem, FormLabel } from "@/components/ui/form";

const AddressFormSchema = z.object({
  street: z.string(),
  number: z.string(),
  city: z.string(),
  state: z.string(),
  zipCode: z.string(),
  country: z.string(),
});

const CompanyFormSchema = z.object({
  tradeName: z.string(),
  legalName: z.string(),
  cnpj: z.string(),
  phones: z.array(z.string()).optional(),
  emails: z.array(z.string()).optional(),
  address: AddressFormSchema,
});

type CompanyFormValues = z.infer<typeof CompanyFormSchema>;

export const CompanyDetailsPage: React.FC = () => {
  const { companyId = "" } = useParams<{ companyId: string }>();

  const companyQuery = useQuery<Company>({
    queryKey: ["company", companyId],
    queryFn: () => getCompany(companyId),
    enabled: !!companyId,
  });

  const form = useForm<CompanyFormValues>({
    resolver: zodResolver(CompanyFormSchema),
  });

  useEffect(() => {
    if (companyQuery.data) {
      form.reset(companyQuery.data);
    }
  }, [companyQuery.data, form]);

  if (companyQuery.isLoading) {
    return <div className="p-6">Carregando empresa...</div>;
  }

  if (companyQuery.isError) {
    return <div className="p-6 text-red-500">Erro ao carregar empresa.</div>;
  }
  
  const companyData = companyQuery.data;

  return (
    <div className="mx-auto w-full max-w-2xl p-6">
      <Card>
        <CardHeader>
          <CardTitle>Detalhes da Empresa</CardTitle>
          <CardDescription>Visualize as informações da empresa abaixo.</CardDescription>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form className="space-y-6">
              <FormField control={form.control} name="tradeName" render={({ field }) => (<FormItem><FormLabel>Nome Fantasia</FormLabel><FormControl><Input {...field} disabled /></FormControl></FormItem>)} />
              <FormField control={form.control} name="legalName" render={({ field }) => (<FormItem><FormLabel>Razão Social</FormLabel><FormControl><Input {...field} disabled /></FormControl></FormItem>)} />
              <FormField control={form.control} name="cnpj" render={({ field }) => (<FormItem><FormLabel>CNPJ</FormLabel><FormControl><Input {...field} disabled /></FormControl></FormItem>)} />
              
              <FormItem>
                <FormLabel>Telefones</FormLabel>
                <div className="space-y-2">
                  {companyData?.phones?.length ? (
                    companyData.phones.map((phone, index) => <Input key={index} value={phone} disabled />)
                  ) : (
                    <Input value="Nenhum telefone cadastrado" disabled />
                  )}
                </div>
              </FormItem>

              <FormItem>
                <FormLabel>Emails</FormLabel>
                <div className="space-y-2">
                  {companyData?.emails?.length ? (
                    companyData.emails.map((email, index) => <Input key={index} value={email} disabled />)
                  ) : (
                    <Input value="Nenhum email cadastrado" disabled />
                  )}
                </div>
              </FormItem>

              <Card>
                <CardHeader>
                  <CardTitle>Endereço</CardTitle>
                </CardHeader>
                <CardContent className="grid gap-4 md:grid-cols-2">
                  <FormField control={form.control} name="address.street" render={({ field }) => ( <FormItem><FormLabel>Rua</FormLabel><FormControl><Input {...field} disabled /></FormControl></FormItem> )} />
                  <FormField control={form.control} name="address.number" render={({ field }) => ( <FormItem><FormLabel>Número</FormLabel><FormControl><Input {...field} disabled /></FormControl></FormItem> )} />
                  <FormField control={form.control} name="address.city" render={({ field }) => ( <FormItem><FormLabel>Cidade</FormLabel><FormControl><Input {...field} disabled /></FormControl></FormItem> )} />
                  <FormField control={form.control} name="address.state" render={({ field }) => ( <FormItem><FormLabel>Estado</FormLabel><FormControl><Input {...field} disabled /></FormControl></FormItem> )} />
                  <FormField control={form.control} name="address.zipCode" render={({ field }) => ( <FormItem><FormLabel>CEP</FormLabel><FormControl><Input {...field} disabled /></FormControl></FormItem> )} />
                  <FormField control={form.control} name="address.country" render={({ field }) => ( <FormItem><FormLabel>País</FormLabel><FormControl><Input {...field} disabled /></FormControl></FormItem> )} />
                </CardContent>
              </Card>

              <div className="flex gap-2 mt-4">
                <Button asChild>
                  <Link to="/companies">Voltar</Link>
                </Button>
              </div>
            </form>
          </Form>
        </CardContent>
      </Card>
    </div>
  );
};