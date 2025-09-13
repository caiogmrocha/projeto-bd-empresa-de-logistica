import { useEffect } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { useParams, useNavigate } from "react-router";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "sonner";

import { getSupplier, updateSupplier, type Supplier, type UpdateSupplierRequest } from "@/api/suppliers";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";

const AddressFormSchema = z.object({
  street: z.string().min(1, "Rua é obrigatória."),
  number: z.string().min(1, "Número é obrigatório."),
  city: z.string().min(1, "Cidade é obrigatória."),
  state: z.string().min(1, "Estado é obrigatório."),
  zipCode: z.string().min(1, "CEP é obrigatório."),
  country: z.string().min(1, "País é obrigatório."),
});

const SupplierFormSchema = z.object({
  name: z.string().min(1, "Nome é obrigatório.").max(100),
  address: AddressFormSchema,
});

type SupplierFormValues = z.infer<typeof SupplierFormSchema>;

export const SupplierEditPage: React.FC = () => {
  const { supplierId = "" } = useParams<{ supplierId: string }>();
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const supplierQuery = useQuery<Supplier>({
    queryKey: ["supplier", supplierId],
    queryFn: () => getSupplier(supplierId),
    enabled: !!supplierId,
  });

  const form = useForm<SupplierFormValues>({
    resolver: zodResolver(SupplierFormSchema),
    defaultValues: {
      name: "",
      address: {
        street: "",
        number: "",
        city: "",
        state: "",
        zipCode: "",
        country: "",
      },
    },
  });

  useEffect(() => {
    if (supplierQuery.data) {
      const s = supplierQuery.data;
      form.reset({
        name: s.name,
        address: s.address
      });
    }
  }, [supplierQuery.data, form]);

  const mutation = useMutation({
    mutationFn: (values: UpdateSupplierRequest) => updateSupplier(supplierId, values),
    onSuccess: () => {
      toast.success("Fornecedor atualizado com sucesso");
      queryClient.invalidateQueries({ queryKey: ["suppliers"] });
      navigate("/suppliers");
    },
    onError: (err: Error) => toast.error(err.message)
  });

  const onSubmit = (values: SupplierFormValues) => {
    mutation.mutate(values);
  };

  if (supplierQuery.isLoading) {
    return <div className="p-6">Carregando fornecedor...</div>;
  }

  if (supplierQuery.isError) {
    return <div className="p-6 text-red-500">Erro ao carregar fornecedor.</div>;
  }

  return (
    <div className="mx-auto w-full max-w-2xl p-6">
      <Card>
        <CardHeader>
          <CardTitle>Editar Fornecedor</CardTitle>
          <CardDescription>Atualize os dados abaixo.</CardDescription>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
              <FormField
                control={form.control}
                name="name"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Nome</FormLabel>
                    <FormControl>
                      <Input placeholder="Nome do fornecedor" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <Card>
                <CardHeader>
                  <CardTitle>Endereço</CardTitle>
                </CardHeader>
                <CardContent className="grid gap-4 md:grid-cols-2">
                  <FormField
                    control={form.control}
                    name="address.street"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Rua</FormLabel>
                        <FormControl>
                          <Input placeholder="Rua" {...field} />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <FormField
                    control={form.control}
                    name="address.number"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Número</FormLabel>
                        <FormControl>
                          <Input placeholder="Número" {...field} />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <FormField
                    control={form.control}
                    name="address.city"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Cidade</FormLabel>
                        <FormControl>
                          <Input placeholder="Cidade" {...field} />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <FormField
                    control={form.control}
                    name="address.state"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Estado</FormLabel>
                        <FormControl>
                          <Input placeholder="Estado" {...field} />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <FormField
                    control={form.control}
                    name="address.zipCode"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>CEP</FormLabel>
                        <FormControl>
                          <Input placeholder="CEP" {...field} />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                  <FormField
                    control={form.control}
                    name="address.country"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>País</FormLabel>
                        <FormControl>
                          <Input placeholder="País" {...field} />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                </CardContent>
              </Card>

              <div className="flex justify-between mt-4">
                <Button variant="outline" onClick={() => navigate("/suppliers")}>
                  Voltar
                </Button>

                <Button type="submit" disabled={mutation.isPending}>
                  {mutation.isPending ? "Salvando..." : "Salvar alterações"}
                </Button>
              </div>

            </form>
          </Form>
        </CardContent>
      </Card>
    </div>
  );
};