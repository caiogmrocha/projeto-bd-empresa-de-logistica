import * as React from "react";
import { useNavigate, useParams } from "react-router";
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { deleteCompany } from "@/api/companies";
import { toast } from "sonner";
import { useMutation, useQueryClient } from "@tanstack/react-query";

export const DeleteCompanyModal: React.FC = () => {
  const { companyId = "" } = useParams<{ companyId: string }>();
  const navigate = useNavigate();
  const [open, setOpen] = React.useState(true);
  const queryClient = useQueryClient();

  const mutation = useMutation({
    mutationFn: () => deleteCompany(companyId),
    onSuccess: () => {
      toast.success("Empresa excluída com sucesso");
      queryClient.invalidateQueries({ queryKey: ["companies"] });
      onClose();
    },
    onError: (error) => {
      toast.error((error as Error)?.message || "Falha ao excluir empresa");
    },
  });

  const onClose = () => {
    setOpen(false);
    navigate("/companies");
  };

  const onConfirm = () => {
    if (!companyId) return;
    mutation.mutate();
  };

  return (
    <Dialog open={open} onOpenChange={(v) => { if (!v) onClose() }}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Confirmar exclusão</DialogTitle>
          <DialogDescription>
            Tem certeza que deseja excluir a empresa {companyId}? Esta ação não pode ser desfeita.
          </DialogDescription>
        </DialogHeader>
        <DialogFooter>
          <Button variant="outline" onClick={onClose} disabled={mutation.isPending}>Cancelar</Button>
          <Button variant="destructive" onClick={onConfirm} disabled={mutation.isPending}>
            {mutation.isPending ? "Excluindo..." : "Excluir"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};