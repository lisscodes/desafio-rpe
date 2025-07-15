import React from 'react';

type Cliente = {
  id: number;
  nome: string;
  cpf: string;
  idade: number;
  statusBloqueio: string;
  limiteCredito: number;
};

type ClientesListProps = {
  clientes: Cliente[];
  onVerFaturas: (clienteId: number) => void;
};

const ClientesList: React.FC<ClientesListProps> = ({ clientes, onVerFaturas }) => {
  return (
    <div className="p-4">
      <table className="w-full border text-left text-sm">
        <thead>
          <tr className="bg-gray-100">
            <th className="p-2 border">Nome</th>
            <th className="p-2 border">CPF</th>
            <th className="p-2 border">Idade</th>
            <th className="p-2 border">Bloqueado</th>
            <th className="p-2 border">Limite Crédito</th>
            <th className="p-2 border">Ações</th>
          </tr>
        </thead>
        <tbody>
          {clientes.map((cliente) => (
            <tr key={cliente.id}>
              <td className="p-2 border">{cliente.nome}</td>
              <td className="p-2 border">{cliente.cpf}</td>
              <td className="p-2 border">{cliente.idade}</td>
              <td className="p-2 border">{cliente.statusBloqueio == 'A' ? 'Sim':'Não'}</td>
              <td className="p-2 border">R$ {cliente.limiteCredito.toLocaleString()}</td>
              <td className="p-2 border">
                <button
                  onClick={() => onVerFaturas(cliente.id)}
                  className="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600"
                >
                  Ver Faturas
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ClientesList;
