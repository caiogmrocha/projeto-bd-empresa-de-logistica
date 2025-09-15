import { useCallback } from "react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useNavigate } from "react-router";
import { toast } from "sonner";

import { createSupplier, type CreateSupplierRequest, SupplierType } from "../../../api/suppliers";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";

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
  supplierType: z.enum([SupplierType.NATURAL_PERSON, SupplierType.LEGAL_ENTITY]),
  cpf: z.string().optional(),
  cnpj: z.string().optional(),
  address: AddressFormSchema,
}).refine(
  (data) => {
    if (data.supplierType === SupplierType.NATURAL_PERSON) {
      return !!data.cpf;
    }
    if (data.supplierType === SupplierType.LEGAL_ENTITY) {
      return !!data.cnpj;
    }
    return true;
  },
  {
    message: "O CPF ou CNPJ é obrigatório.",
    path: ["cpf", "cnpj"],
  }
);

type SupplierFormValues = z.infer<typeof SupplierFormSchema>;

export const SupplierCreatePage: React.FC = () => {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const form = useForm<SupplierFormValues>({
    resolver: zodResolver(SupplierFormSchema),
    defaultValues: {
      name: "",
      supplierType: SupplierType.LEGAL_ENTITY,
      cpf: "",
      cnpj: "",
      address: {
        street: "",
        number: "",
        city: "",
        state: "",
        zipCode: "",
        country: "",
      },
    },
    mode: "onSubmit",
  });

  const createSupplierMutation = useMutation({
    mutationFn: createSupplier,
    onSuccess: () => {
      toast.success("Fornecedor criado com sucesso");
      queryClient.invalidateQueries({ queryKey: ["suppliers"] });
      navigate("/suppliers");
    },
    onError: (err: Error) => {
      toast.error(err.message);
    },
  });

  const onSubmit = useCallback(async (values: SupplierFormValues) => {
    const payload: CreateSupplierRequest = {
      name: values.name,
      supplierType: values.supplierType,
      address: {
        ...values.address,
        zipCode: values.address.zipCode.replace(/\D/g, ""),
      },
      cpf: values.supplierType === SupplierType.NATURAL_PERSON ? values.cpf?.replace(/\D/g, "") || undefined : undefined,
      cnpj: values.supplierType === SupplierType.LEGAL_ENTITY ? values.cnpj?.replace(/\D/g, "") || undefined : undefined,
    };
    await createSupplierMutation.mutateAsync(payload);
  }, [createSupplierMutation]);

  return (
    <div className="mx-auto w-full max-w-2xl p-6">
      <Card>
        <CardHeader>
          <CardTitle>Cadastrar Fornecedor</CardTitle>
          <CardDescription>Preencha os dados abaixo.</CardDescription>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">

              {/* Campos do formulário */}
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

              <FormField
                control={form.control}
                name="supplierType"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Tipo do fornecedor</FormLabel>
                    <Select onValueChange={field.onChange} value={field.value}>
                      <FormControl>
                        <SelectTrigger className="w-full">
                          <SelectValue placeholder="Selecione o tipo" />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        <SelectItem value={SupplierType.NATURAL_PERSON}>Pessoa Física</SelectItem>
                        <SelectItem value={SupplierType.LEGAL_ENTITY}>Pessoa Jurídica</SelectItem>
                      </SelectContent>
                    </Select>
                    <FormMessage />
                  </FormItem>
                )}
              />

              {form.watch('supplierType') === SupplierType.NATURAL_PERSON && (
                <FormField
                  control={form.control}
                  name="cpf"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>CPF</FormLabel>
                      <FormControl>
                        <Input placeholder="CPF" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              )}

              {form.watch('supplierType') === SupplierType.LEGAL_ENTITY && (
                <FormField
                  control={form.control}
                  name="cnpj"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>CNPJ</FormLabel>
                      <FormControl>
                        <Input placeholder="CNPJ" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              )}

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
                <Button
                  variant="outline"
                  onClick={() => navigate("/suppliers")}
                >
                  Voltar
                </Button>

                <Button type="submit" disabled={createSupplierMutation.isPending}>
                  {createSupplierMutation.isPending ? "Criando..." : "Criar fornecedor"}
                </Button>
              </div>

            </form>
          </Form>
        </CardContent>
      </Card>
    </div>
  );
}