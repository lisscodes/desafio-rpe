export type StatusFatura = 'Paga' | 'Atrasada' | 'Aberta';

export interface Invoice {
  id: number;
  valor: number;
  dataVencimento: string;
  dataPagamento?: string;
  status: StatusFatura;
}
