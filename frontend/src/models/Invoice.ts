export type StatusFatura = 'Paga' | 'Atrasada' | 'Aberta';

export interface Fatura {
  id: number;
  valor: number;
  dataVencimento: string;
  dataPagamento?: string;
  status: StatusFatura;
}
