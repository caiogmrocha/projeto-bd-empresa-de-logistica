import * as React from "react"
import { useNavigate, useParams } from "react-router"
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { deleteWarehouse } from "@/api/warehouses"
import { toast } from "sonner"
import { Toaster } from "sonner"

export const WarehouseDeletePage: React.FC = () => {
  const { warehouseId = "" } = useParams<{ warehouseId: string }>()
  const navigate = useNavigate()
  const [open, setOpen] = React.useState(true)
  const [pending, setPending] = React.useState(false)

  const onClose = () => {
    setOpen(false)
    // navigate back to list after close
    navigate("/warehouses")
  }

  const onConfirm = async () => {
    if (!warehouseId) return
    try {
      setPending(true)
      await deleteWarehouse(warehouseId)
      toast.success("Armazém excluído com sucesso")
      onClose()
    } catch (error) {
      toast.error((error as Error)?.message || "Falha ao excluir armazém")
      setPending(false)
    }
  }

  return (
    <Dialog open={open} onOpenChange={(v) => { if (!v) onClose() }}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Confirmar exclusão</DialogTitle>
          <DialogDescription>
            Tem certeza que deseja excluir o armazém? Esta ação não pode ser desfeita.
          </DialogDescription>
        </DialogHeader>
        <DialogFooter>
          <Button variant="outline" onClick={onClose} disabled={pending}>Cancelar</Button>
          <Button variant="destructive" onClick={onConfirm} disabled={pending}>
            {pending ? "Excluindo..." : "Excluir"}
          </Button>
        </DialogFooter>
      </DialogContent>
      <Toaster />
    </Dialog>
  )
}