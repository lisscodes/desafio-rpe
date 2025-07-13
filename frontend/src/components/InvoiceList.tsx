import React from 'react';

interface Fatura {
  id: number;
  valor: number;
  dataVencimento: string;
  dataPagamento?: string;
  status: 'Pendente' | 'Pago' | 'Atrasada';
}

interface Props {
  clienteId?: string;
}

const faturasPorCliente: Record<string, Fatura[]> = {
  '1': [
    { id: 1, valor: 1200.5, dataVencimento: '2025-07-01', status: 'Pago', dataPagamento: '2025-07-01' },
    { id: 2, valor: 750.0, dataVencimento: '2025-07-10', status: 'Pendente' },
  ],
  '2': [
    { id: 3, valor: 350.0, dataVencimento: '2025-07-03', status: 'Atrasada' },
  ],
  '3': [],
  '4': [
    { id: 4, valor: 199.9, dataVencimento: '2025-07-11', status: 'Pendente' },
  ],
};

const InvoiceList: React.FC<Props> = ({ clienteId }) => {
  const faturas = faturasPorCliente[clienteId ?? ''] || [];

  const handlePagamento = (faturaId: number) => {
    alert(`Simulando pagamento da fatura #${faturaId}`);
  };

  return (
    <div className="bg-white rounded shadow p-4">
      {faturas.length === 0 ? (
        <p className="text-gray-500">Nenhuma fatura encontrada para este cliente.</p>
      ) : (
        <table className="w-full text-left border-collapse">
          <thead className="bg-gray-100">
            <tr>
              <th className="p-2">Valor</th>
              <th className="p-2">Vencimento</th>
              <th className="p-2">Status</th>
              <th className="p-2">Pagamento</th>
            </tr>
          </thead>
          <tbody>
            {faturas.map((f) => (
              <tr key={f.id} className="border-t">
                <td className="p-2">R$ {f.valor.toFixed(2)}</td>
                <td className="p-2">{f.dataVencimento}</td>
                <td className="p-2">
                  <span
                    className={`px-2 py-1 rounded ${
                      f.status === 'Pago'
                        ? 'bg-green-100 text-green-800'
                        : f.status === 'Pendente'
                        ? 'bg-yellow-100 text-yellow-800'
                        : 'bg-red-100 text-red-800'
                    }`}
                  >
                    {f.status}
                  </span>
                </td>
                <td className="p-2">
                  {f.status === 'Pago' ? (
                    f.dataPagamento
                  ) : (
                    <button
                      onClick={() => handlePagamento(f.id)}
                      className="bg-teal-500 hover:bg-teal-600 text-white px-3 py-1 rounded"
                    >
                      Registrar Pagamento
                    </button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default InvoiceList;
