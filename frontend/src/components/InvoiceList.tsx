import React, { useEffect, useState } from 'react';
import { Invoice } from '../models/Invoice';
import { faturaService } from '../services/InvoiceService';

interface Props {
  clienteId?: string;
}

const InvoiceList: React.FC<Props> = ({ clienteId }) => {
  const [faturas, setFaturas] = useState<Invoice[]>([]);

  useEffect(() => {
    if (clienteId) {
      faturaService.listarPorCliente(clienteId)
        .then(setFaturas)
        .catch(err => console.error('Erro ao buscar faturas:', err));
    }
  }, [clienteId]);

  const handlePagamento = (id: number) => {
    faturaService.registrarPagamento(id)
      .then(faturaAtualizada => {
        setFaturas(prev =>
          prev.map(f => f.id === id ? faturaAtualizada : f)
        );
      })
      .catch(err => console.error('Erro ao registrar pagamento:', err));
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
            {faturas.map(f => (
              <tr key={f.id} className="border-t">
                <td className="p-2">R$ {f.valor.toFixed(2)}</td>
                <td className="p-2">{f.dataVencimento}</td>
                <td className="p-2">
                  <span className={`px-2 py-1 rounded ${
                    f.status === 'Paga'
                      ? 'bg-green-100 text-green-800'
                      : f.status === 'Aberta'
                      ? 'bg-yellow-100 text-yellow-800'
                      : 'bg-red-100 text-red-800'
                  }`}>
                    {f.status}
                  </span>
                </td>
                <td className="p-2">
                  {f.status === 'Paga' ? (
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
